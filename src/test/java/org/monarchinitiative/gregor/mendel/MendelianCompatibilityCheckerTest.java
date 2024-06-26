package org.monarchinitiative.gregor.mendel;

import org.monarchinitiative.gregor.pedigree.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class MendelianCompatibilityCheckerTest extends MendelianCompatibilityCheckerTestBase {

	MendelianInheritanceChecker checker;
	List<GenotypeCalls> gcList;
	Map<ModeOfInheritance, List<GenotypeCalls>> result;

	@BeforeEach
	public void setUp() throws PedParseException {
		List<PedPerson> individuals = List.of(
			new PedPerson("ped", "I.1", "0", "0", Sex.MALE, Disease.UNAFFECTED), // father
			new PedPerson("ped", "I.2", "0", "0", Sex.FEMALE, Disease.UNAFFECTED), // mother
			new PedPerson("ped", "II.1", "I.1", "I.2", Sex.MALE, Disease.AFFECTED), // son
			new PedPerson("ped", "II.2", "I.1", "I.2", Sex.FEMALE, Disease.UNAFFECTED) // daughter
	);
		PedFileContents pedFileContents = new PedFileContents(List.of(),
			individuals);
		this.pedigree = new Pedigree(pedFileContents, "ped");

		this.names = List.of("I.1", "I.2", "II.1", "II.2");

		this.checker = new MendelianInheritanceChecker(this.pedigree);

		this.result = null;
		this.gcList = null;
	}

	@Test
	public void checkADTest1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, HET, HET, HET), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest2() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, REF, REF, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest3() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(ALT, ALT, ALT, ALT), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest4() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, UKN, UKN, UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest5() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, REF, HET, HET), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest6() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, HET, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest7() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, REF, REF, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest8() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, REF, HET, ALT), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest9() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, REF, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest10() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, REF, HET, HET), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest11() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, UKN, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADTest12() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, UKN, HET, UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADARTest1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, HET, HET, UKN), lst(HET, REF, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADARTest2() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, HET, ALT, UKN), lst(REF, REF, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADARTest3() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, HET, ALT, UKN), lst(REF, REF, HET, REF), ChromosomeType.X_CHROMOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void checkADARTest4() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, REF, ALT, UKN), lst(REF, REF, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, HET, HET, HET), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest2() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, UKN, UKN, UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest3() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, UKN, HET, UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest4() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, UKN, ALT, UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest5() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, UKN, ALT, UKN), lst(HET, HET, ALT, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest6() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF, HET, HET, UKN), lst(HET, REF, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest7() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET, HET, ALT, HET), lst(REF, REF, HET, REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest8() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, HET, ALT, HET), lst(REF, REF, ALT, REF), ChromosomeType.X_CHROMOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void getAllCompatibleModesTest9() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN, HET, ALT, UKN), ChromosomeType.X_CHROMOSOMAL);
		result = checker.checkMendelianInheritance(gcList);
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

}
