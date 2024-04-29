package org.monarchinitiative.gregor.pedigree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class PedFileWriterTest {

	File tmpFile;

	@BeforeEach
	public void setUp() throws IOException {
		this.tmpFile = File.createTempFile("output", "ped");
	}

	@Test
	public void testWrite() throws IOException {
		List<PedPerson> individuals = List.of(
			new PedPerson("fam", "father", "0", "0", Sex.MALE, Disease.UNKNOWN),
			new PedPerson("fam", "mother", "0", "0", Sex.FEMALE, Disease.UNKNOWN),
			new PedPerson("fam", "son", "father", "mother", Sex.MALE, Disease.UNKNOWN),
			new PedPerson("fam", "daughter", "father", "mother", Sex.FEMALE, Disease.UNKNOWN)
			);
		PedFileContents pedFileContents = new PedFileContents(List.of(),
			individuals);

		File tmpFile = File.createTempFile("test", ".ped");
		tmpFile.deleteOnExit();  // Ensure the file is deleted when the JVM exits

		PedFileWriter writer = new PedFileWriter(tmpFile);
		writer.write(pedFileContents);

		String fileContents = Files.readString(tmpFile.toPath());

		String expectedContents = "#PEDIGREE\tNAME\tFATHER\tMOTHER\tSEX\tDISEASE\n" +
				"fam\tfather\t0\t0\t1\t0\n" +
				"fam\tmother\t0\t0\t2\t0\n" +
				"fam\tson\tfather\tmother\t1\t0\n" +
				"fam\tdaughter\tfather\tmother\t2\t0\n";

		Assertions.assertEquals(expectedContents, fileContents);
	}

}
