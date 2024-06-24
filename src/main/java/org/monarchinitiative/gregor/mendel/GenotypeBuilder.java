package org.monarchinitiative.gregor.mendel;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for building {@link Genotype} objects
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class GenotypeBuilder {

	/**
	 * Allele numbers
	 */
	private final List<Integer> alleleNumbers;

	public GenotypeBuilder() {
		this.alleleNumbers = new ArrayList<>();
	}

	public Genotype build() {
		return new Genotype(alleleNumbers);
	}

	public List<Integer> getAlleleNumbers() {
		return alleleNumbers;
	}

}
