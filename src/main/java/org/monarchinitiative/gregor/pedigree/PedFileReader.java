package org.monarchinitiative.gregor.pedigree;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

// TODO(holtgrem): Test me!

/**
 * Allows reading of {@link PedFileContents} from a {@link InputStream} or {@link File}.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:max.schubach@charite.de">Max Schubach</a>
 */
public final class PedFileReader {

	/**
	 * the file to read from
	 */
	private final File file;

	/**
	 * Initialize object with the given file.
	 */
	public PedFileReader(File file) {
		this.file = file;
	}

	/**
	 * Read in the pedigree file in {@link #file}.
	 *
	 * @throws IOException       in the case of problems with reading from {@link #file}
	 * @throws PedParseException in the case of problems with parsing the data from {@link #file}
	 */
	public PedFileContents read() throws IOException, PedParseException {
		return read(new FileInputStream(file));
	}

	/**
	 * Static method for parsing a PED file into a {@link PedFileContents} object.
	 *
	 * @param stream input stream to read from
	 * @return resulting {@link PedFileContents} representing the contents of the file
	 * @throws IOException       in the case of problems with reading from <code>stream</code>
	 * @throws PedParseException in the case of problems with parsing the data from <code>stream</code>
	 */
	public static PedFileContents read(InputStream stream) throws IOException, PedParseException {
		BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

		// Parse header.
		List<String> extraHeaders = List.of(); // default to empty
		String line = in.readLine();
		if (line != null && line.startsWith("#")) {
			extraHeaders = parseHeader(line);
			line = in.readLine();
		}

		// Parse individuals.
		List<PedPerson> individuals = new ArrayList<PedPerson>();
		while (line != null) {
			line = line.trim(); // trim leading and trailing whitespace
			if (line.length() != 0) // ignore empty lines
				individuals.add(readIndividual(line));

			line = in.readLine(); // read next
		}

		return new PedFileContents(extraHeaders, Collections.unmodifiableList(individuals));
	}

	/**
	 * Parse header and return extra header fields, <code>line</code> must start with <code>'#'</code>.
	 */
	private static List<String> parseHeader(String line) {
		return Arrays.stream(line.trim().substring(1).split("\t"))
				.skip(6)
				.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
	}


	/**
	 * Parse individual from the given line.
	 *
	 * @throws PedParseException on problems with the parsing
	 */
	private static PedPerson readIndividual(String line) throws PedParseException {
		try {
			String[] fields = line.trim().split("\\s+");
			if (fields.length < 6) {
				throw new PedParseException("Insufficient number of fields in line: \"" + line + "\"");
			}

			String pedigree = fields[0];
			String name = fields[1];
			String father = fields[2];
			String mother = fields[3];
			String sex = fields[4];
			String disease = fields[5];

			List<String> extraFields = new ArrayList<>(Arrays.asList(fields).subList(6, fields.length));

			return new PedPerson(pedigree, name, father, mother, Sex.toSex(sex), Disease.toDisease(disease),
					Collections.unmodifiableList(extraFields));
		} catch (NoSuchElementException e) {
			throw new PedParseException("Insufficient number of fields in line: \"" + line + "\"");
		}
	}

}
