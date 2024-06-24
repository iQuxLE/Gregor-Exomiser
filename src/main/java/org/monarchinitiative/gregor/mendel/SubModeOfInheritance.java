package org.monarchinitiative.gregor.mendel;

// TODO: test me!

/**
 * Enum for refined representation of {@link ModeOfInheritance}
 * <p>
 * In contrast to {@link ModeOfInheritance}, this type can reflect whether autosomal recessive inheritance is compound
 * heterozygous or homozygous alternative.
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public enum SubModeOfInheritance {

	/**
	 * autosomal dominant inheritance
	 */
	AUTOSOMAL_DOMINANT,
	/**
	 * autosomal recessive inheritance, compound het
	 */
	AUTOSOMAL_RECESSIVE_COMP_HET,
	/**
	 * autosomal recessive inheritance, hom alt
	 */
	AUTOSOMAL_RECESSIVE_HOM_ALT,
	/**
	 * recessive inheritance on X chromosome, compound het
	 */
	X_RECESSIVE_COMP_HET,
	/**
	 * recessive inheritance on X chromosome, hom alt
	 */
	X_RECESSIVE_HOM_ALT,
	/**
	 * dominant inheritance on X chromosome
	 */
	X_DOMINANT,
	/**
	 * mitochondrial inheritance
	 */
	MITOCHONDRIAL,
	/**
	 * value for encoding uninitialized values
	 */
	ANY;

	public boolean isRecessive() {
		return toModeOfInheritance().isRecessive();
	}

	public boolean isDominant() {
		return toModeOfInheritance().isDominant();
	}

	/**
	 * @return shortcut for the ModeOfInheritance
	 */
	public String getAbbreviation() {
        return switch (this) {
            case AUTOSOMAL_DOMINANT -> "AD";
            case AUTOSOMAL_RECESSIVE_COMP_HET -> "AR_COMP_HET";
            case AUTOSOMAL_RECESSIVE_HOM_ALT -> "AR_HOM_ALT";
            case X_DOMINANT -> "XD";
            case X_RECESSIVE_COMP_HET -> "XR_COMP_HET";
            case X_RECESSIVE_HOM_ALT -> "XR_HOM_ALT";
            case MITOCHONDRIAL -> "MT";
            default -> null;
        };
	}

	/**
	 * @return coarsened value from {@link ModeOfInheritance}
	 */
	public ModeOfInheritance toModeOfInheritance() {
        return switch (this) {
            case AUTOSOMAL_DOMINANT -> ModeOfInheritance.AUTOSOMAL_DOMINANT;
            case AUTOSOMAL_RECESSIVE_COMP_HET, AUTOSOMAL_RECESSIVE_HOM_ALT -> ModeOfInheritance.AUTOSOMAL_RECESSIVE;
            case X_DOMINANT -> ModeOfInheritance.X_DOMINANT;
            case X_RECESSIVE_COMP_HET, X_RECESSIVE_HOM_ALT -> ModeOfInheritance.X_RECESSIVE;
            case MITOCHONDRIAL -> ModeOfInheritance.MITOCHONDRIAL;
            default -> ModeOfInheritance.ANY;
        };
	}

}
