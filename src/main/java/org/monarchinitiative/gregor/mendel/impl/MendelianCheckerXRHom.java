package org.monarchinitiative.gregor.mendel.impl;

import org.monarchinitiative.gregor.mendel.*;
import org.monarchinitiative.gregor.pedigree.Disease;
import org.monarchinitiative.gregor.pedigree.Pedigree;
import org.monarchinitiative.gregor.pedigree.Person;
import org.monarchinitiative.gregor.pedigree.Sex;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Helper class for checking a {@link GenotypeCalls} for compatibility with a
 * {@link Pedigree} and XR homozygous mode
 *
 * <h2>Compatibility Check</h2>
 * <p>
 * In the case of a single individual, we require hom. alt.
 * <p>
 * In the case of multiple individuals, we require that the affects are
 * compatible, that the unaffected parents of hom. alt. unaffected females are
 * not are not hom. ref., and that all unaffected individuals are not hom. alt.
 * The affected individuals are compatible if no affected individual is hom.
 * ref. or het. and there is at least one affected individual that is hom. alt.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:max.schubach@bihealth.de">Max Schubach</a>
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 * @version 0.15-SNAPSHOT
 */
public class MendelianCheckerXRHom extends AbstractMendelianChecker {

	public MendelianCheckerXRHom(MendelianInheritanceChecker parent) {
		super(parent);
	}

	@Override
	public List<GenotypeCalls> filterCompatibleRecords(Collection<GenotypeCalls> calls)
		throws IncompatiblePedigreeException {
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
	 * @return whether <code>calls</code> is compatible with XR homozygous
	 * inheritance in the case of a single individual in the pedigree
	 */
	private boolean isCompatibleSingleton(GenotypeCalls calls) {
		if (calls.getNSamples() == 0)
			return false; // no calls!
		if (calls.getGenotypeBySampleNo(0).isHomAlt())
			return true;
		else if (pedigree.getMembers().get(0).getSex() != Sex.FEMALE && calls.getGenotypeBySampleNo(0).isHet())
			return true;
		else
			return false;
	}

	/**
	 * @return whether <code>calls</code> is compatible with XR homozygous
	 * inheritance in the case of multiple individuals in the pedigree
	 */
	private boolean isCompatibleFamily(GenotypeCalls calls) {
		return (affectedsAreCompatible(calls) && parentsAreCompatible(calls) && unaffectedsAreCompatible(calls));
	}

	private boolean affectedsAreCompatible(GenotypeCalls calls) {
		int numVar = 0;

		for (Person p : pedigree.getMembers()) {
			final String name = p.getName();
			final Genotype gt = calls.getGenotypeForSample(name);
			if (p.getDisease() == Disease.AFFECTED) {
				if (gt.isHomRef()) {
					// Cannot be disease-causing mutation, an affected male or female does not have
					// it
					return false;
				} else if (p.getSex() == Sex.FEMALE && gt.isHet()) {
					// Cannot be disease-causing mutation if a female have it heterozygous. For a
					// male we think it is a
					// misscall (alt instead of het)
					return false;
				} else if (gt.isHomAlt() || (p.getSex() != Sex.FEMALE && gt.isHet())) {
					numVar += 1;
				}
			}
		}

		return (numVar > 0);
	}

	/**
	 * We only have to look for the parents of the female affected for inherited
	 * variants. Here the variant must be inherited from both!
	 * <p>
	 * For the parents of male affected it is really special. Because it can be
	 * inherited from the mother or the father! Not from both. Because we do not
	 * know the specific parents of one affected (only all of them) at this part we
	 * have to skip the parents of male affected.
	 *
	 * @param calls
	 * @return
	 */
	private boolean parentsAreCompatible(GenotypeCalls calls) {
		Set<String> femaleParentNames = Collections.unmodifiableSet(queryDecorator.getAffectedFemaleParentNames());

		for (Person p : pedigree.getMembers()) {
			final Genotype gt = calls.getGenotypeForSample(p.getName());
			if (femaleParentNames.contains(p.getName())) {
				if (p.getSex() == Sex.MALE && p.getDisease() == Disease.UNAFFECTED) {
					// Must always be affected. If affected it is already checked!
					return false;
				}
				if (p.getSex() == Sex.FEMALE && (gt.isHomAlt() || gt.isHomRef())) {
					// Cannot be disease-causing mutation if mother of patient is homozygous or not
					// the carrier
					return false;
				}
			}
		}

		return true;
	}

	private boolean unaffectedsAreCompatible(GenotypeCalls calls) {
		Set<String> unaffectedNames = Collections.unmodifiableSet(queryDecorator.getUnaffectedNames());

		for (Person p : pedigree.getMembers()) {
			if (unaffectedNames.contains(p.getName())) {
				final Genotype gt = calls.getGenotypeForSample(p.getName());
				// Strict handling. Males cannot be called heterozygous (will be seen as a
				// homozygous mutation)
				if (p.isMale() && (gt.isHet() || gt.isHomAlt()))
					return false;
				else if (gt.isHomAlt())
					return false; // cannot be disease-causing mutation (female or unknown)
			}
		}

		return true;
	}

}
