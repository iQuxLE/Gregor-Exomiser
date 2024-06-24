package org.monarchinitiative.gregor.mendel;

/**
 * An enumeration of the main Mendelian modes of inheritance for prioritizing exome data
 *
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 * @version 0.1.2 (September 15, 2017)
 */
public enum ModeOfInheritance {
	/**
	 * autosomal dominant inheritance
	 */
	AUTOSOMAL_DOMINANT,
	/**
	 * autosomal recessive inheritance
	 */
	AUTOSOMAL_RECESSIVE,
	/**
	 * recessive inheritance on X chromosome
	 */
	X_RECESSIVE,
	/**
	 * dominant inheritance on X chromosome
	 */
	X_DOMINANT,
	/**
	 * Mitochondrial inheritance
	 */
	MITOCHONDRIAL,
	/**
	 * value for encoding uninitialized values
	 */
	ANY;

	/**
	 * @return <code>true</code> if is recessive MOI
	 */
	public boolean isRecessive() {
		switch (this) {
			case AUTOSOMAL_RECESSIVE:
			case X_RECESSIVE:
				return true;
			default:
				return false;
		}
	}

	/**
	 * @return <code>true</code> if is recessive MOI
	 */
	public boolean isDominant() {
        return switch (this) {
            case AUTOSOMAL_DOMINANT, X_DOMINANT -> true;
            default -> false;
        };
	}

	/**
	 * @return two-letter shortcut for the ModeOfInheritance
	 */
	public String getAbbreviation() {
        return switch (this) {
            case AUTOSOMAL_DOMINANT -> "AD";
            case AUTOSOMAL_RECESSIVE -> "AR";
            case X_DOMINANT -> "XD";
            case X_RECESSIVE -> "XR";
            case MITOCHONDRIAL -> "MT";
            default -> null;
        };
	}

}
