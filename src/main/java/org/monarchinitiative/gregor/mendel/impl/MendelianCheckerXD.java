package org.monarchinitiative.gregor.mendel.impl;

import org.monarchinitiative.gregor.mendel.ChromosomeType;
import org.monarchinitiative.gregor.mendel.Genotype;
import org.monarchinitiative.gregor.mendel.GenotypeCalls;
import org.monarchinitiative.gregor.mendel.MendelianInheritanceChecker;
import org.monarchinitiative.gregor.pedigree.Disease;
import org.monarchinitiative.gregor.pedigree.Person;
import org.monarchinitiative.gregor.pedigree.Sex;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Implementation of Mendelian compatibility check for autosomal dominant case
 *
 * <h2>Compatibility Check</h2>
 * <p>
 * For X-chromosomal dominant inheritance, there must be at least one {@link Genotype} that is shared by all affected
 * individuals but no unaffected individuals in the pedigree.
 *
 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class MendelianCheckerXD extends AbstractMendelianChecker {

	public MendelianCheckerXD(MendelianInheritanceChecker parent) {
		super(parent);
	}

	@Override
	public List<GenotypeCalls> filterCompatibleRecords(Collection<GenotypeCalls> calls) {
		// Determine compatibility checking method based on the number of pedigree members
		Predicate<GenotypeCalls> compatibilityChecker = (this.pedigree.getNMembers() == 1) ?
				this::isCompatibleSingleton : this::isCompatibleFamily;
		// Stream the calls, filter by chromosome type and compatibility
		return calls.stream()
				.filter(call -> call.getChromType() == ChromosomeType.X_CHROMOSOMAL)
				.filter(compatibilityChecker)
				.toList();
	}

	/**
	 * @return whether <code>calls</code> is compatible with AD inheritance in the case of a single individual in the
	 * pedigree
	 */
	private boolean isCompatibleSingleton(GenotypeCalls calls) {
		if (calls.getNSamples() == 0)
			return false; // no calls!
		final Genotype gt = calls.getGenotypeBySampleNo(0);
		if (pedigree.getMembers().get(0).getSex() == Sex.FEMALE) {
			// Allow only heterozygous calls
			return calls.getGenotypeBySampleNo(0).isHet();
		} else {
			// We allow homozygous (actually hemizygous) and heterozygous (false call)
			return (gt.isHet() || gt.isHomAlt());
		}
	}

	/**
	 * @return whether <code>calls</code> is compatible with AD inheritance in the case of multiple individuals in the
	 * pedigree
	 */
	private boolean isCompatibleFamily(GenotypeCalls calls) {
		int numAffectedWithVar = 0;

		for (Person p : pedigree.getMembers()) {
			final Sex sex = p.getSex();
			final Genotype gt = calls.getGenotypeForSample(p.getName());
			final Disease d = p.getDisease();

			if (d == Disease.AFFECTED) {
				if (gt.isHomRef() || (sex == Sex.FEMALE && gt.isHomAlt())) {
					// We do not allow hom. alternative for females to have the same behaviour as AD for females
					return false;
				} else if (sex == Sex.FEMALE && gt.isHet()) {
					numAffectedWithVar++;
				} else if (sex != Sex.FEMALE && (gt.isHet() || gt.isHomAlt())) {
					// We allow heterozygous here as well in the case of mis-calls in the one X copy in the male or
					// unknown
					numAffectedWithVar++;
				}
			} else if (d == Disease.UNAFFECTED) {
				if (gt.isHet() || gt.isHomAlt())
					return false; // unaffected must not have it!
			}
		}

		return (numAffectedWithVar > 0);
	}

}
