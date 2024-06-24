package org.monarchinitiative.gregor.pedigree;

/**
 * Codes used to denote affection status of a person in a pedigree.
 *
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 */
public enum Disease {
	/**
	 * corresponds to 0 = unknown disease status in the PED file.
	 */
	UNKNOWN,
	/**
	 * corresponds to 1 = unaffected in the PED file.
	 */
	UNAFFECTED,
	/**
	 * corresponds to 2 = affected in the PED file.
	 */
	AFFECTED;

	/**
	 * @return <code>int</code> value representation for PED file.
	 */
	public int toInt() {
        return switch (this) {
            case AFFECTED -> 2;
            case UNAFFECTED -> 1;
			case UNKNOWN -> 0;
        };
	}

	/**
	 * Parse {@link String} into a <code>Disease</code> value.
	 *
	 * @param s String to parse
	 * @return resulting <code>Disease</code> object
	 * @throws PedParseException if <code>s</code> was not equal to <code>"0"</code>, <code>"1"</code>, or <code>"2"</code>.
	 */
	public static Disease toDisease(String s) throws PedParseException {
        return switch (s) {
            case "0" -> UNKNOWN;
            case "1" -> UNAFFECTED;
            case "2" -> AFFECTED;
            default -> throw new PedParseException("Invalid PED disease status value: " + s);
        };
	}

}
