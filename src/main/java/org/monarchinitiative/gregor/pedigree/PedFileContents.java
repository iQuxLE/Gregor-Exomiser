package org.monarchinitiative.gregor.pedigree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the contents of a pedigree file.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 */
public class PedFileContents {

	/**
	 * headers for extra columns (column 7 and beyond)
	 */
	private final List<String> extraColumnHeaders;

	/**
	 * the individuals in the PED file
	 */
	private final List<PedPerson> individuals;

	/**
	 * mapping of name to PedPerson
	 */
	private final Map<String, PedPerson> nameToPerson; // TODO(holtgrew): Test this!

	public PedFileContents(List<String> extraColumnHeaders, List<PedPerson> individuals) {
		this.extraColumnHeaders = extraColumnHeaders;
		this.individuals = individuals;

		Map<String, PedPerson> map = new LinkedHashMap<>();
		for (PedPerson p : individuals)
			map.put(p.getName(), p);
		this.nameToPerson = Collections.unmodifiableMap(map);
	}


	/**
	 * @return headers for extra columns (column 7 and beyond)
	 */
	public List<String> getExtraColumnHeaders() {
		return Collections.unmodifiableList(extraColumnHeaders);
	}

	/**
	 * @return the individuals in the PED file
	 */
	public List<PedPerson> getIndividuals() {
		return Collections.unmodifiableList(individuals);
	}

	/**
	 * @return mapping of name to PedPerson
	 */
	public Map<String, PedPerson> getNameToPerson() {
		return nameToPerson;
	}

}
