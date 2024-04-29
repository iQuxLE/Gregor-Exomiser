package org.monarchinitiative.gregor.mendel;

/**
 * Thrown when the pedigree does not fit to the {@link GenotypeCalls}
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class IncompatiblePedigreeException extends GregorException {

	private static final long serialVersionUID = 1L;

	public IncompatiblePedigreeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public IncompatiblePedigreeException(String msg) {
		super(msg);
	}

}
