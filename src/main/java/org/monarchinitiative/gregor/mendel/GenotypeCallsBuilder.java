package org.monarchinitiative.gregor.mendel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Helper class for building {@link GenotypeCalls} objects
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public class GenotypeCallsBuilder {

	/**
	 * Type of the chromosome that the variant lies on
	 */
	private ChromosomeType chromType;
	/**
	 * Mapping from sample name to {@link Genotype}
	 */
	private Map<String, Genotype> sampleToGenotype;
	/**
	 * A payload object for later easier reidentification
	 */
	private Object payload;

	public GenotypeCallsBuilder() {
		this.chromType = null;
		// TODO: Does this have to be a TreeMap or does LinkedHAshMap makes sense?
		this.sampleToGenotype = new TreeMap<>();
		this.payload = null;
	}

	public GenotypeCalls build() {
		return new GenotypeCalls(chromType, sampleToGenotype.entrySet(), payload);
	}

	public ChromosomeType getChromType() {
		return chromType;
	}

	public void setChromType(ChromosomeType chromType) {
		this.chromType = chromType;
	}

	public Map<String, Genotype> getSampleToGenotype() {
		return sampleToGenotype;
	}

	// TODO: add or put method// builder.setChromType(ChromosomeType.AUTOSOMAL);
	// builder stays mutable,  GenotypeCalls not


	// TODO sampleToGenotype maybe needs LinkedHAshMap --> Try with Tree, HashMap and LinkedHashMap in Tests
	public void setSampleToGenotype(TreeMap<String, Genotype> sampleToGenotype) {
		this.sampleToGenotype = sampleToGenotype;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

}
