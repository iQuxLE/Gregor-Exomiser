package org.monarchinitiative.gregor.pedigree;

import org.monarchinitiative.gregor.mendel.GregorException;

import java.io.Serial;

/**
 * Exception that occurs during parsing of PEDfiles.
 *
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 */
public class PedParseException extends GregorException {

	@Serial
	private static final long serialVersionUID = 2L;

	public PedParseException() {
		super();
	}

	public PedParseException(String msg) {
		super(msg);
	}

	public PedParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}