package org.monarchinitiative.gregor.pedigree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PedigreeQueryDecoratorTest {

	Pedigree pedigree;
	PedigreeQueryDecorator decorator;

	@BeforeEach
	public void setUp() throws PedParseException {
		List<PedPerson> individuals = List.of(
			new PedPerson("fam", "father", "0", "0", Sex.MALE, Disease.UNAFFECTED),
			new PedPerson("fam", "mother", "0", "0", Sex.FEMALE, Disease.UNKNOWN),
			new PedPerson("fam", "son", "father", "mother", Sex.MALE, Disease.AFFECTED),
			new PedPerson("fam", "daughter", "father", "mother", Sex.FEMALE, Disease.UNKNOWN)
			);
		PedFileContents pedFileContents = new PedFileContents(List.of(),
			individuals);

		this.pedigree = new Pedigree(pedFileContents, "fam");
		this.decorator = new PedigreeQueryDecorator(pedigree);
	}

	@Test
	public void testIsParentOfAffected() {
		Assertions.assertTrue(decorator.isParentOfAffected(pedigree.getMembers().get(0)));
		Assertions.assertTrue(decorator.isParentOfAffected(pedigree.getMembers().get(1)));
		Assertions.assertFalse(decorator.isParentOfAffected(pedigree.getMembers().get(2)));
		Assertions.assertFalse(decorator.isParentOfAffected(pedigree.getMembers().get(3)));
	}

	@Test
	public void testGetUnaffectedNames() {
		Assertions.assertEquals(Set.of("father"), decorator.getUnaffectedNames());
	}

	@Test
	public void testGetParentNames() {
		Assertions.assertEquals(Set.of("father", "mother"), decorator.getParentNames());
	}

	@Test
	public void testGetParents() {
		Assertions.assertEquals(List.of(pedigree.getMembers().get(0), pedigree.getMembers().get(1)), decorator.getParents());
	}

	@Test
	public void testGetNumberOfParents() {
		Assertions.assertEquals(2, decorator.getNumberOfParents());
	}

	@Test
	public void testGetNumberOfAffecteds() {
		Assertions.assertEquals(1, decorator.getNumberOfAffecteds());
	}

	@Test
	public void testGetNumberOfUnaffecteds() {
		Assertions.assertEquals(1, decorator.getNumberOfUnaffecteds());
	}

}
