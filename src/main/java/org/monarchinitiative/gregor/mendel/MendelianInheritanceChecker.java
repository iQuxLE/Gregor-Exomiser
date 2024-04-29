package org.monarchinitiative.gregor.mendel;

import org.monarchinitiative.gregor.mendel.impl.*;
import org.monarchinitiative.gregor.pedigree.Pedigree;
import org.monarchinitiative.gregor.pedigree.PedigreeQueryDecorator;

import java.util.*;

/**
 * Facade class for checking lists of {@link GenotypeCalls} for compatibility with mendelian inheritance
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class MendelianInheritanceChecker {

	/**
	 * Pedigree to use for mendelian inheritance checking
	 */
	final private Pedigree pedigree;
	/**
	 * Helper for querying a pedigree
	 */
	final private PedigreeQueryDecorator queryPed;
	/**
	 * Mendelian compatibility checker for each sub mode of inheritance
	 */
	final private Map<SubModeOfInheritance, AbstractMendelianChecker> checkers;

	/**
	 * Construct checker with the pedigree to use
	 *
	 * @param pedigree The pedigree to use for the mendelian inheritance checking
	 */
	public MendelianInheritanceChecker(Pedigree pedigree) {
		this.pedigree = pedigree;
		this.queryPed = new PedigreeQueryDecorator(pedigree);

		Map<SubModeOfInheritance, AbstractMendelianChecker> map = new LinkedHashMap<>();
		map.put(SubModeOfInheritance.AUTOSOMAL_DOMINANT, new MendelianCheckerAD(this));
		map.put(SubModeOfInheritance.AUTOSOMAL_RECESSIVE_COMP_HET, new MendelianCheckerARCompoundHet(this));
		map.put(SubModeOfInheritance.AUTOSOMAL_RECESSIVE_HOM_ALT, new MendelianCheckerARHom(this));
		map.put(SubModeOfInheritance.X_DOMINANT, new MendelianCheckerXD(this));
		map.put(SubModeOfInheritance.X_RECESSIVE_COMP_HET, new MendelianCheckerXRCompoundHet(this));
		map.put(SubModeOfInheritance.X_RECESSIVE_HOM_ALT, new MendelianCheckerXRHom(this));
		map.put(SubModeOfInheritance.MITOCHONDRIAL, new InheritanceCheckerMT(this));
		this.checkers = map;
	}

	/**
	 * Perform checking for compatible mode of inheritance
	 *
	 * @param calls          {@link Collection} of {@link GenotypeCalls} objects to perform the mode of inheritance check for in
	 *                       case of non-recessive mode of inheritance
	 * @param recessiveCalls {@link Collection} of {@link GenotypeCalls} objects to perform the mode of
	 *                       inheritance check for in case of recessive mode of inheritance
	 * @return {@link Map} that, for each {@link ModeOfInheritance}, contains the {@link Collection} of compatible
	 * {@link GenotypeCalls} from <code>list</code>
	 * @throws IncompatiblePedigreeException if the individuals in <code>calls</code> do not fit to the pedigree
	 */
	public Map<ModeOfInheritance, List<GenotypeCalls>> checkMendelianInheritance(
		Collection<GenotypeCalls> calls, Collection<GenotypeCalls> recessiveCalls) throws IncompatiblePedigreeException {
		Map<ModeOfInheritance, List<GenotypeCalls>> map = new LinkedHashMap<>();
		for (ModeOfInheritance mode : ModeOfInheritance.values()) {
			if (mode == ModeOfInheritance.ANY) {
				map.put(mode, List.copyOf(calls));
			} else {
				if (mode == ModeOfInheritance.AUTOSOMAL_RECESSIVE || mode == ModeOfInheritance.X_RECESSIVE) {
					map.put(mode, filterCompatibleRecords(recessiveCalls, mode));
				} else {
					map.put(mode, filterCompatibleRecords(calls, mode));
				}
			}
		}
		return map;
	}

	public Map<ModeOfInheritance, List<GenotypeCalls>> checkMendelianInheritance(
		Collection<GenotypeCalls> calls) throws IncompatiblePedigreeException {
		return Collections.unmodifiableMap(checkMendelianInheritance(calls, calls));
	}


	/**
	 * Perform checking for compatible sub mode of inheritance
	 *
	 * @param calls                 {@link Collection} of {@link GenotypeCalls} objects to perform the mode of inheritance check for
	 * @param compHetRecessiveCalls {@link Collection} of {@link GenotypeCalls} objects to perform the mode of
	 *                              inheritance check for in case of compound het. recessive mode of inheritance
	 * @return {@link Map} that, for each {@link SubModeOfInheritance}, contains the {@link Collection} of compatible
	 * {@link GenotypeCalls} from <code>list</code>
	 * @throws IncompatiblePedigreeException if the individuals in <code>calls</code> do not fit to the pedigree
	 */
	public Map<SubModeOfInheritance, List<GenotypeCalls>> checkMendelianInheritanceSub(
		Collection<GenotypeCalls> calls, Collection<GenotypeCalls> compHetRecessiveCalls) throws IncompatiblePedigreeException {
		Map<SubModeOfInheritance, List<GenotypeCalls>> map = new LinkedHashMap<>();
		for (SubModeOfInheritance mode : SubModeOfInheritance.values()) {
			if (mode == SubModeOfInheritance.ANY) {
				map.put(mode, List.copyOf(calls));
			} else {
				if (mode == SubModeOfInheritance.AUTOSOMAL_RECESSIVE_COMP_HET || mode == SubModeOfInheritance.X_RECESSIVE_COMP_HET) {
					map.put(mode, filterCompatibleRecordsSub(compHetRecessiveCalls, mode));
				} else {
					map.put(mode, filterCompatibleRecordsSub(calls, mode));
				}
			}
		}
		return map;
	}

	public Map<SubModeOfInheritance, List<GenotypeCalls>> checkMendelianInheritanceSub(
		Collection<GenotypeCalls> calls) throws IncompatiblePedigreeException {
		return Collections.unmodifiableMap(checkMendelianInheritanceSub(calls, calls));
	}

	/**
	 * Filters records in <code>calls</code> for compatibility with <code>mode</code>
	 *
	 * @param calls List of {@link GenotypeCalls} to filter
	 * @param mode  {@link ModeOfInheritance} to check for
	 * @return List of {@link GenotypeCalls} from <code>calls</code> that are compatible with <code>mode</code>
	 * @throws IncompatiblePedigreeException if the individuals in <code>calls</code> do not fit to the pedigree
	 */
	public List<GenotypeCalls> filterCompatibleRecords(Collection<GenotypeCalls> calls, ModeOfInheritance mode)
		throws IncompatiblePedigreeException {
		// Check for compatibility of calls with pedigree
		if (!calls.stream().allMatch(c -> isCompatibleWithPedigree(c)))
			throw new IncompatiblePedigreeException("GenotypeCalls not compatible with pedigree");
		// Filter down to the compatible records
		Set<GenotypeCalls> calls1;
		Set<GenotypeCalls> calls2;
		List<GenotypeCalls> result;
		switch (mode) {
			case AUTOSOMAL_DOMINANT:
				return Collections.unmodifiableList(checkers.get(SubModeOfInheritance.AUTOSOMAL_DOMINANT).filterCompatibleRecords(calls));
			case AUTOSOMAL_RECESSIVE:
				calls1 = new HashSet<>(checkers.get(SubModeOfInheritance.AUTOSOMAL_RECESSIVE_HOM_ALT).filterCompatibleRecords(calls));
				calls2 = new HashSet<>(checkers.get(SubModeOfInheritance.AUTOSOMAL_RECESSIVE_COMP_HET).filterCompatibleRecords(calls));
				result = new ArrayList<>();
				for (GenotypeCalls c : calls)
					if (calls1.contains(c) || calls2.contains(c))
						result.add(c);
				return Collections.unmodifiableList(result);
			case X_DOMINANT:
				return Collections.unmodifiableList(checkers.get(SubModeOfInheritance.X_DOMINANT).filterCompatibleRecords(calls));
			case X_RECESSIVE:
				calls1 = new HashSet<>(checkers.get(SubModeOfInheritance.X_RECESSIVE_HOM_ALT).filterCompatibleRecords(calls));
				calls2 = new HashSet<>(checkers.get(SubModeOfInheritance.X_RECESSIVE_COMP_HET).filterCompatibleRecords(calls));
				result = new ArrayList<>();
				for (GenotypeCalls c : calls)
					if (calls1.contains(c) || calls2.contains(c))
						result.add(c);
				return Collections.unmodifiableList(result);
			case MITOCHONDRIAL:
				return Collections.unmodifiableList(checkers.get(SubModeOfInheritance.MITOCHONDRIAL).filterCompatibleRecords(calls));
			default:
			case ANY:
				return List.copyOf(calls);
		}
	}

	/**
	 * Filters records in <code>calls</code> for compatibility with <code>subMode</code>
	 *
	 * @param calls   List of {@link GenotypeCalls} to filter
	 * @param subMode {@link SubModeOfInheritance} to check for
	 * @return List of {@link GenotypeCalls} from <code>calls</code> that are compatible with <code>mode</code>
	 * @throws IncompatiblePedigreeException if the individuals in <code>calls</code> do not fit to the pedigree
	 */
	public List<GenotypeCalls> filterCompatibleRecordsSub(Collection<GenotypeCalls> calls,
																   SubModeOfInheritance subMode) throws IncompatiblePedigreeException {
		// Check for compatibility of calls with pedigree
		if (!calls.stream().allMatch(c -> isCompatibleWithPedigree(c)))
			throw new IncompatiblePedigreeException("GenotypeCalls not compatible with pedigree");
		// Filter down to the compatible records
		if (subMode == SubModeOfInheritance.ANY)
			return List.copyOf(calls);
		else
			return checkers.get(subMode).filterCompatibleRecords(calls);
	}

	/**
	 * @return {@link Pedigree} to use
	 */
	public Pedigree getPedigree() {
		return pedigree;
	}

	/**
	 * @return <code>true</code> if <code>call</code> is compatible with this pedigree
	 */
	private boolean isCompatibleWithPedigree(GenotypeCalls calls) {
		return pedigree.getNames().containsAll(calls.getSampleNames());
	}

}
