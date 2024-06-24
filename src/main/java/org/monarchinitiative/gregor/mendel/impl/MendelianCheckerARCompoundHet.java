package org.monarchinitiative.gregor.mendel.impl;

import org.monarchinitiative.gregor.mendel.*;
import org.monarchinitiative.gregor.pedigree.Disease;
import org.monarchinitiative.gregor.pedigree.Pedigree;
import org.monarchinitiative.gregor.pedigree.Person;

import java.util.*;
import java.util.stream.Collectors;

// TODO: also return no-call/not-observed variant

/**
 * Helper class for checking a {@link Collection} of {@link GenotypeCalls} for compatibility with a {@link Pedigree} and
 * autosomal recessive compound het mode of inheritance.
 *
 * <h2>Compatibility Check</h2>
 * <p>
 * In the case of a single individual, we require at least two heterozygous genotype calls.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 */
public class MendelianCheckerARCompoundHet extends AbstractMendelianChecker {

	/**
	 * list of siblings for each person in {@link #pedigree}
	 */
	private final Map<Person, List<Person>> siblings;

	public MendelianCheckerARCompoundHet(MendelianInheritanceChecker parent) {
		super(parent);

		this.siblings = queryDecorator.buildSiblings();
	}

	/**
	 * @param calls Genotypes for all pedigree members at all sites of the 'unit' being investigated (e.g., a gene, or a regulon).
	 * @return Genotypes for all variants that are compatible with autosomal recessive compound heterozygous inheritance.
	 */
	@Override
	public List<GenotypeCalls> filterCompatibleRecords(Collection<GenotypeCalls> calls)
		throws IncompatiblePedigreeException {
		List<GenotypeCalls> autosomalCalls = calls.stream()
				.filter(call -> call.getChromType() == ChromosomeType.AUTOSOMAL)
				.collect(Collectors.toList());
		return pedigree.getNMembers() == 1 ?
				filterCompatibleRecordsSingleSample(autosomalCalls) :
				filterCompatibleRecordsMultiSample(autosomalCalls);
	}

	/**
	 * In the single sample case, if we find two or more heterozygous variants, then there is compatibility with
	 * autosomal recessive compound heterozygous inheritance.
	 */
	List<GenotypeCalls> filterCompatibleRecordsSingleSample(Collection<GenotypeCalls> calls) {
		List<GenotypeCalls> builder = new ArrayList<>();
		for (GenotypeCalls gc : calls) {
			if (gc.getGenotypeBySampleNo(0).isHet())
				builder.add(gc);
		}

		if (builder.size() > 1)
			return builder;
		else
			return List.of();
	}

	private List<GenotypeCalls> filterCompatibleRecordsMultiSample(Collection<GenotypeCalls> calls) {
		// First, collect candidate genotype call lists from trios around affected individuals
		ArrayList<Candidate> candidates = collectTrioCandidates(calls);

		// Then, check the candidates for all trios around affected individuals
		Set<GenotypeCalls> result = new HashSet<>();
		for (Candidate c : candidates) {
			if (isCompatibleWithTriosAroundAffected(c)) {
				// If candidate holds, check all unaffected for not being homozygous alt
				if (isCompatibleWithUnaffected(c)) {
					result.add(c.maternal());
					result.add(c.paternal());
				}
			}
		}
		return List.copyOf(result);
	}

	private boolean isCompatibleWithUnaffected(Candidate c) {
		for (Person p : pedigree.getMembers()) {
			if (p.getDisease() == Disease.UNAFFECTED) {
				boolean patHet = false;
				boolean matHet = false;
				// None of the genotypes from the paternal or maternal call lists may be homozygous in the index
				if (c.paternal() != null) {
					final Genotype pGT = c.paternal().getGenotypeForSample(p.getName());
					if (pGT.isHomAlt())
						return false;
					if (pGT.isHet())
						patHet = true;
				}
				if (c.maternal() != null) {
					final Genotype mGT = c.maternal().getGenotypeForSample(p.getName());
					if (mGT.isHomAlt())
						return false;
					if (mGT.isHet())
						matHet = true;
				}

				// If mat and pat variant are heterozygous in an unaffected, check if they are on the same allele or not
				// The variants in the Candidate are labeled paternal/maternal according to where they were found
				// ppGT is the genotype of the father of p for the 'paternal' variant
				// mpGT is the genotype of the mother of p for the 'paternal' variant
				// pmGT is the genotype of the father of p for the 'maternal' variant
				// mmGT is the genotype of the mother of p for the 'maternal' variant
				// If an unaffected person is compound het for a pair of variants and the parents of p eachcontribute one variant, then
				// it cannot be a cause of autosomal recessive disease since p is unaffected.
				if (patHet && matHet) {
					if (c.paternal() != null && p.getFather() != null && c.maternal() != null
						&& p.getMother() != null) {
						final Genotype ppGT = c.paternal().getGenotypeForSample(p.getFather().getName());
						final Genotype mpGT = c.paternal().getGenotypeForSample(p.getMother().getName());
						final Genotype pmGT = c.maternal().getGenotypeForSample(p.getFather().getName());
						final Genotype mmGT = c.maternal().getGenotypeForSample(p.getMother().getName());
						// way one (paternal and maternal can now be switched around!
						if (ppGT.isHet() && mpGT.isHomRef() && pmGT.isHomRef() && mmGT.isHet())
							return false;
						if (ppGT.isHomRef() && mpGT.isHet() && pmGT.isHet() && mmGT.isHomRef())
							return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * This function looks for candidate pairs of variants in each of the affected-parent trios of the pedigree.
	 *
	 * @return A list of {@link Candidate} pairs of variants for each member of the pedigree.
	 */
	private ArrayList<Candidate> collectTrioCandidates(Collection<GenotypeCalls> calls) {
		ArrayList<Candidate> result = new ArrayList<Candidate>();

		// fist collect the candidates only over the affected if at least one parent is avaiable
		boolean noParent = true;
		for (Person p : pedigree.getMembers()) {
			// Check if at least one parent is available
			if (p.getDisease() == Disease.AFFECTED && (p.getFather() != null || p.getMother() != null)) {
				collectTroCandidatesWithParents(calls, result, p);
				noParent = false;
			}
		}

		// If no parent was available (e.g. only siblings)
		if (noParent) {
			for (Person p : pedigree.getMembers()) {
				// Check if at least one parent is available
				if (p.getDisease() == Disease.AFFECTED) {
					collectTroCandidatesWithoutParents(calls, result, p);
				}
			}
		}
		return result;
	}

	private void collectTroCandidatesWithParents(Collection<GenotypeCalls> calls, ArrayList<Candidate> result,
												 Person p) {
		List<GenotypeCalls> paternal = new ArrayList<GenotypeCalls>();
		List<GenotypeCalls> maternal = new ArrayList<GenotypeCalls>();

		// Collect candidates towards the paternal side (heterozygous or not observed in child and father, not
		// hom_alt or het in mother)
		for (GenotypeCalls gc : calls) {
			final Genotype gtP = gc.getGenotypeForSample(p.getName());
			final Genotype gtF = (p.getFather() == null) ? null : gc.getGenotypeForSample(p.getFather().getName());
			final Genotype gtM = (p.getMother() == null) ? null : gc.getGenotypeForSample(p.getMother().getName());

			if ((gtP.isHet() || gtP.isNotObserved()) && (gtF == null || gtF.isHet() || gtF.isNotObserved())
				&& (gtM == null || gtM.isNotObserved() || gtM.isHomRef()))
				paternal.add(gc);
		}
		// Collect candidates towards the paternal side (heterozygous or not observed in child and mother. Not
		// hom_alt or het in father)
		for (GenotypeCalls gc : calls) {
			final Genotype gtP = gc.getGenotypeForSample(p.getName());
			final Genotype gtF = (p.getFather() == null) ? null : gc.getGenotypeForSample(p.getFather().getName());
			final Genotype gtM = (p.getMother() == null) ? null : gc.getGenotypeForSample(p.getMother().getName());

			if ((gtP.isHet() || gtP.isNotObserved()) && (gtM == null || gtM.isHet() || gtM.isNotObserved())
				&& (gtF == null || gtF.isNotObserved() || gtF.isHomRef()))
				maternal.add(gc);

			// Combine compatible paternal and maternal heterozygous variants
			MendelianCheckerXRCompoundHet.findCandidate(result, p, paternal, maternal);
		}
	}

	private void collectTroCandidatesWithoutParents(Collection<GenotypeCalls> calls, ArrayList<Candidate> result,
													Person p) {
		List<GenotypeCalls> paternal = new ArrayList<GenotypeCalls>();
		List<GenotypeCalls> maternal = new ArrayList<GenotypeCalls>();

		// Collect candidates and do not look at the parents
		for (GenotypeCalls gc : calls) {
			final Genotype gtP = gc.getGenotypeForSample(p.getName());
			if (gtP.isHet() || gtP.isNotObserved()) {
				paternal.add(gc);
				maternal.add(gc);
			}
		}
		// Combine compatible paternal and maternal heterozygous variants
		for (GenotypeCalls pat : paternal)
			for (GenotypeCalls mat : maternal) {
				if (pat == mat) // FIXME what means this NOW?
					continue; // exclude if variants are identical
				else if (pat.getGenotypeForSample(p.getName()).isNotObserved()
					&& mat.getGenotypeForSample(p.getName()).isNotObserved())
					continue;

				result.add(new Candidate(pat, mat));
			}
	}

	/**
	 * This function takes a candidate pair of compound het variants and checks whether it is compatible with all affecteds in the pedigree.
	 */
	private boolean isCompatibleWithTriosAroundAffected(Candidate c) {
		for (Person p : pedigree.getMembers()) {
			if (p.getDisease() == Disease.AFFECTED) {
				// We have to check this for paternal,maternal and vice versa. Paternal maternal inheritance can be
				// different for other parents in the pedigree.
				if (isCompatibleWithTriosAndMaternalPaternalInheritanceAroundAffected(p, c.paternal(),
						c.maternal()))
					if (isCompatibleWithTriosAndMaternalPaternalInheritanceAroundAffected(p, c.maternal(),
							c.paternal()))
						return false;
			}
		}

		return true;
	}

	/**
	 * For each person being tested (p is assumed to be affected by an autosomal recessive disease), we test whether the
	 * person has a HET or NOCALL genotype. Both parents (if any) of p mustbe HET or NOCALL for the mutations, whereby one
	 * of the variants must be inherited from the father of p and one from the mother of p (the variant is not filtered out
	 * if some or all of this data is missing). For each of the unaffected siblings of the affected person, it is checked
	 * whether the sibling is compound het for the variants, inwhich case they are filtered out.
	 *
	 * @return true if this candidate pair of variants is compatible with AR compound het inheritance.
	 */
	private boolean isCompatibleWithTriosAndMaternalPaternalInheritanceAroundAffected(Person p, GenotypeCalls paternal,
																					  GenotypeCalls maternal) {
		// None of the genotypes from the paternal or maternal call lists may be homozygous in the index
		if (paternal != null) {
			final Genotype pGT = paternal.getGenotypeForSample(p.getName());
			if (pGT.isHomAlt() || pGT.isHomRef())
				return true;
		}
		if (maternal != null) {
			final Genotype mGT = maternal.getGenotypeForSample(p.getName());
			if (mGT.isHomAlt() || mGT.isHomRef())
				return true;
		}

		// The paternal variant may not be homozygous in the father of p, if any
		if (paternal != null && p.getFather() != null) {
			final Genotype pGT = paternal.getGenotypeForSample(p.getFather().getName());
			if (pGT.isHomAlt() || pGT.isHomRef())
				return true;
		}

		// The maternal variant may not be homozygous in the mother of p, if any
		if (maternal != null && p.getMother() != null) {
			final Genotype mGT = maternal.getGenotypeForSample(p.getMother().getName());
			if (mGT.isHomAlt() || mGT.isHomRef())
				return true;
		}

		// None of the unaffected siblings may have the same genotypes as p
		if (siblings != null && !siblings.isEmpty() && siblings.containsKey(p))
			for (Person sibling : siblings.get(p))
				if (sibling.getDisease() == Disease.UNAFFECTED) {
                    assert paternal != null;
                    final Genotype pGT = paternal.getGenotypeForSample(sibling.getName());
                    assert maternal != null;
                    final Genotype mGT = maternal.getGenotypeForSample(sibling.getName());
					if (pGT.isHet() && mGT.isHet())
						return true;
				}
		return false;
	}

}
