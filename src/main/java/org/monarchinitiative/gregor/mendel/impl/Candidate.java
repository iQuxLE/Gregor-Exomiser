package org.monarchinitiative.gregor.mendel.impl;

import org.monarchinitiative.gregor.mendel.GenotypeCalls;

/**
 * Helper type for collecting candidate pairs of {@link GenotypeCalls} objects
 * The paternal list of genotypes refers to the genotypes in all pedigree members for a variant that is heterozygous in an affected
 * person and either HET or NOCALL in the father and HOM-REF or NOCALL in the mother, and analogously for maternal.
 * Together, the variants referred to by maternal and paternal represent a candidate compound heterozygous pair of variants that
 * were found in one trio. Subsequent code will determine whether the candidate pair is compatible with AR compoound-het
 * inheritance across the entire pedigree.
 *
 * @param paternal one VCF record compatible with mutation in father
 * @param maternal one VCF record compatible with mutation in mother
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
 */
record Candidate(GenotypeCalls paternal, GenotypeCalls maternal) {

	/**
	 * @return one VCF record compatible with mutation in father
	 */
	@Override
	public GenotypeCalls paternal() {
		return paternal;
	}

	/**
	 * @return one VCF record compatible with mutation in mother
	 */
	@Override
	public GenotypeCalls maternal() {
		return maternal;
	}

}
