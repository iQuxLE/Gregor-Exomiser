package org.monarchinitiative.gregor.mendel;

import org.monarchinitiative.gregor.pedigree.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MendelianCompatibilityCheckerADSingletonTest extends MendelianCompatibilityCheckerTestBase {

	MendelianInheritanceChecker checker;
	List<GenotypeCalls> gcList;
	Map<ModeOfInheritance, List<GenotypeCalls>> result;

	@BeforeEach
	public void setUp() throws Exception {
		List<PedPerson> individuals = List.of(
			new PedPerson("ped", "I.1", "0", "0", Sex.MALE, Disease.AFFECTED)
		);
		PedFileContents pedFileContents = new PedFileContents(List.of(),
			individuals);
		this.pedigree = new Pedigree(pedFileContents, "ped");
		this.names = List.of("I.1");

		this.checker = new MendelianInheritanceChecker(this.pedigree);

		this.result = null;
		this.gcList = null;
	}

	@Test
	public void testSizeOfPedigree() {
		Assertions.assertEquals(1, pedigree.getMembers().size());
	}

	@Test
	public void testCaseNegativesOneVariant1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(ALT), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCaseNegativesOneVariant2() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCaseNegativesOneVariant3() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCaseNegativesTwoVariants1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(ALT), lst(REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCaseNegativesTwoVariants2() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(REF), lst(UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCaseNegativesTwoVariants3() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(UKN), lst(ALT), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCasePositiveOneVariant1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCasePositiveTwoVariants1() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET), lst(REF), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCasePositiveTwoVariants2() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET), lst(HET), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(2, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCasePositiveTwoVariants3() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET), lst(ALT), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

	@Test
	public void testCasePositiveTwoVariants4() throws IncompatiblePedigreeException {
		gcList = getGenotypeCallsList(lst(HET), lst(UKN), ChromosomeType.AUTOSOMAL);
		result = checker.checkMendelianInheritance(gcList);

		Assertions.assertEquals(1, result.get(ModeOfInheritance.AUTOSOMAL_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.AUTOSOMAL_RECESSIVE).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_DOMINANT).size());
		Assertions.assertEquals(0, result.get(ModeOfInheritance.X_RECESSIVE).size());
		Assertions.assertEquals(2, result.get(ModeOfInheritance.ANY).size());
	}

}
