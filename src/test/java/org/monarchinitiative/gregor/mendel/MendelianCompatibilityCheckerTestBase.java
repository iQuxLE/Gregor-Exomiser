package org.monarchinitiative.gregor.mendel;

import org.monarchinitiative.gregor.pedigree.Pedigree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Base class for tests for MendelianCompatibilityChecker JUnit tests
 *
 * @author <a href="mailto:manuel.holtgrewe@bihealth.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
 */
public class MendelianCompatibilityCheckerTestBase {

	/**
	 * Helper enum type for simple genotypes
	 */
	protected enum SimpleGenotype {
		/**
		 * heterozygous
		 */
		HET,
		/**
		 * homozygous ref
		 */
		REF,
		/**
		 * homozygous alt
		 */
		ALT,
		/**
		 * unknown/no-call/not observed
		 */
		UKN
	}

	protected final SimpleGenotype HET = SimpleGenotype.HET;
	protected final SimpleGenotype REF = SimpleGenotype.REF;
	protected final SimpleGenotype ALT = SimpleGenotype.ALT;
	protected final SimpleGenotype UKN = SimpleGenotype.UKN;

	protected List<String> names;
	protected Pedigree pedigree;

	/**
	 * @return a {@link com.google.common.collect.ImmutableList} object.
	 */
	protected List<SimpleGenotype> lst(SimpleGenotype... gts) {
		List<SimpleGenotype> genotypes = new ArrayList<>();
		for (int i = 0; i < gts.length; ++i)
			genotypes.add(gts[i]);
		return Collections.unmodifiableList(genotypes);
	}

	protected List<GenotypeCalls> getGenotypeCallsList(List<SimpleGenotype> genotypes,
													   ChromosomeType chromosomeType) {
		HashMap<String, Genotype> entries = new HashMap<String, Genotype>();
		for (int i = 0; i < names.size(); ++i) {
			switch (genotypes.get(i)) {
				case HET:
					entries.put(names.get(i), new Genotype(List.of(Genotype.REF_CALL, 1)));
					break;
				case REF:
					entries.put(names.get(i), new Genotype(List.of(Genotype.REF_CALL, Genotype.REF_CALL)));
					break;
				case ALT:
					entries.put(names.get(i), new Genotype(List.of(1, 1)));
					break;
				case UKN:
					entries.put(names.get(i), new Genotype(List.of(Genotype.NO_CALL, Genotype.NO_CALL)));
					break;
			}
		}

		List<GenotypeCalls> gcs = new ArrayList<GenotypeCalls>();
		gcs.add(new GenotypeCalls(chromosomeType, entries.entrySet()));
		return gcs;
	}

	@SuppressWarnings("unchecked")
	protected List<GenotypeCalls> getGenotypeCallsList(List<SimpleGenotype> genotypes1,
													   List<SimpleGenotype> genotypes2, ChromosomeType chromosomeType) {
		List<GenotypeCalls> gcs = new ArrayList<GenotypeCalls>();
		for (Object obj : new Object[]{genotypes1, genotypes2}) {
			List<SimpleGenotype> genotypes = (List<SimpleGenotype>) obj;
			HashMap<String, Genotype> entries = new HashMap<String, Genotype>();
			for (int i = 0; i < names.size(); ++i) {
				switch (genotypes.get(i)) {
					case HET:
						entries.put(names.get(i), new Genotype(List.of(Genotype.REF_CALL, 1)));
						break;
					case REF:
						entries.put(names.get(i), new Genotype(List.of(Genotype.REF_CALL, Genotype.REF_CALL)));
						break;
					case ALT:
						entries.put(names.get(i), new Genotype(List.of(1, 1)));
						break;
					case UKN:
						entries.put(names.get(i), new Genotype(List.of(Genotype.NO_CALL, Genotype.NO_CALL)));
						break;
				}
			}

			gcs.add(new GenotypeCalls(chromosomeType, entries.entrySet()));
		}
		return gcs;
	}

}
