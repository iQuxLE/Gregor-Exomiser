package org.monarchinitiative.gregor.mendel;

import java.util.*;
import java.util.Map.Entry;

/**
 * A list of genotypes (at an implicitely assumed site) in multiple individuals
 * <p>
 * This list contains the core information for the filtration of variants by mendelian inheritance.
 * <p>
 * This list is not called <code>GenotypeList</code> as "list" indicates more of a "vertical" arrangement (multiple
 * sites) of genotypes instead of a "horizontal" one (one site, multiple samples).
 * <p>
 * Note: of course, the class is only immutable as long as <code>payload</code> is immutable!
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 */
public final class GenotypeCalls implements Iterable<Entry<String, Genotype>> {

	private final static Genotype GT_NO_CALL = new Genotype(List.of(Genotype.NO_CALL));

	/**
	 * Type of the chromosome that the variant lies on (autosomal, X-chromosomal, etc.)
	 */
	private final ChromosomeType chromType;
	/**
	 * Mapping from sample name to {@link Genotype}
	 */
	private final Map<String, Genotype> sampleToGenotype;
	/**
	 * List of sample names
	 */
	private final List<String> sampleNames;
	/**
	 * A payload object for later easier reidentification
	 */
	private final Object payload;

	/**
	 * Initialize {@link GenotypeCalls} with mapping from sample to genotype
	 *
	 * @param chromType        type of the chromosome of this genotype call site
	 * @param sampleToGenotype {@link Iterable} with mapping from sample name to {@link Genotype}
	 */
	public GenotypeCalls(ChromosomeType chromType, Iterable<? extends Entry<String, Genotype>> sampleToGenotype) {
		this(chromType, sampleToGenotype, null);
	}

	/**
	 * Initialize {@link GenotypeCalls} with mapping from sample to genotype and an additional "payload" object
	 *
	 * @param chromType        type of the chromosome of this genotype call site
	 * @param sampleToGenotype {@link Iterable} with mapping from sample name to {@link Genotype}
	 * @param payload          An arbitrary payload object. This could be something to later match the constructed
	 *                         <code>GenotypeCalls</code> back to an object in your application (e.g., the HTSJDK
	 *                         <code>VariantContext</code> that was used for constructing the {@link GenotypeCalls}).
	 */
	public GenotypeCalls(ChromosomeType chromType, Iterable<? extends Entry<String, Genotype>> sampleToGenotype,
						 Object payload) {
		this.chromType = chromType;

		Map<String, Genotype> temp = new LinkedHashMap<>();
		for (Entry<String, Genotype> entry : sampleToGenotype){
			temp.put(entry.getKey(), entry.getValue());
		}
		this.sampleToGenotype = Collections.unmodifiableMap(temp);
		this.sampleNames = List.copyOf(this.sampleToGenotype.keySet());
		this.payload = payload;
	}

	/**
	 * @return number of samples in genotype list
	 */
	public int getNSamples() {
		return sampleNames.size();
	}

	/**
	 * @param sample name of the sample to return {@link Genotype} for
	 * @return {@link Genotype} for the given sample, a not-observed genotype if the sample is unknown instead of
	 * <code>null</code>
	 */
	public Genotype getGenotypeForSample(String sample) {
		Genotype result = sampleToGenotype.get(sample);
		// TODO(holtgrewe): using Optional<> here would make handling empty return values more elegant in the calling
		// code such that the behaviour could change then
		if (result == null)
			return GT_NO_CALL;
		return result;
	}

	/**
	 * @param sampleNo 0-based sample number to return {@link Genotype} for
	 * @return {@link Genotype} by sample number
	 */
	public Genotype getGenotypeBySampleNo(int sampleNo) {
		return sampleToGenotype.get(sampleNames.get(sampleNo));
	}

	/**
	 * @return type of the chromosome
	 */
	public ChromosomeType getChromType() {
		return chromType;
	}

	/**
	 * @return Sample to genotype map
	 */
	public Map<String, Genotype> getSampleToGenotype() {
		return Collections.unmodifiableMap(sampleToGenotype);
	}

	/**
	 * @return Sample names
	 */
	public List<String> getSampleNames() {
		return sampleNames;
	}

	/**
	 * @return Payload object
	 */
	public Object getPayload() {
		return payload;
	}

	@Override
	public String toString() {
		return "GenotypeCalls [chromType=" + chromType + ", sampleToGenotype=" + sampleToGenotype + ", sampleNames="
			+ sampleNames + ", payload=" + payload + "]";
	}

	@Override
	public Iterator<Entry<String, Genotype>> iterator() {
		return sampleToGenotype.entrySet().iterator();
	}

	@Override
	public int hashCode() {
		// Yes, we really need object identity here
		return System.identityHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof GenotypeCalls genotype) {
            return chromType == genotype.chromType && sampleNames.equals(genotype.sampleNames)
					&& payload.equals(genotype.payload);
		}
		if (obj == null)
			return false;
		// Yes, we really need object identity here
		return (this.hashCode() == obj.hashCode());
	}

}
