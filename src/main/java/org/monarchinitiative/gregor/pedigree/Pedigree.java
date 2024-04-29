package org.monarchinitiative.gregor.pedigree;

import java.util.*;

// TODO(holtgrem): Test me!

/**
 * Represent one pedigree from a PED file.
 *
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 */
public final class Pedigree {

	/**
	 * the pedigree's name
	 */
	private final String name;

	/**
	 * the pedigree's members
	 */
	private final List<Person> members;

	/**
	 * mapping from member name to member
	 */
	private final Map<String, IndexedPerson> nameToMember;

	/**
	 * Initialize the object with the given values
	 *
	 * @param name    the name of the pedigree/family
	 * @param members list of the members
	 */
	public Pedigree(String name, Collection<Person> members) {
		this.name = name;
		this.members = List.copyOf(members);

		Map<String, IndexedPerson> map = new LinkedHashMap<>();
		int i = 0;
		for (Person person : members)
			map.put(person.getName(), new IndexedPerson(i++, person));
		this.nameToMember = Collections.unmodifiableMap(map);
	}

	/**
	 * Initialize the object with the members of <code>contents</code> that have the pedigree name equal to
	 * <code>pedigreeName</code>.
	 *
	 * @param contents     contents from the pedigree file
	 * @param pedigreeName name of the pedigree to extract
	 * @throws PedParseException in the case of problems with references to individuals for mother and father
	 */
	public Pedigree(PedFileContents contents, String pedigreeName) throws PedParseException {
		this(pedigreeName, new PedigreeExtractor(pedigreeName, contents).run());
	}

	/**
	 * @return number of members in pedigree
	 */
	public int getNMembers() {
		return members.size();
	}

	/**
	 * @return the pedigree's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the pedigree's members
	 */
	public List<Person> getMembers() {
		return members;
	}

	/**
	 * @return mapping from member name to member
	 */
	public Map<String, IndexedPerson> getNameToMember() {
		return nameToMember;
	}

	/**
	 * Obtain subset of members in a pedigree or change order.
	 * <p>
	 * If a {@link Person} is selected that has parents in <code>this</code> but the parent's name is not in
	 * <code>name</code> then the {@link Person} will have <code>null</code> as the parent object.
	 *
	 * @return <code>Pedigree</code> with the members from <code>names</code> in the given order
	 */
	public Pedigree subsetOfMembers(Collection<String> names) {
		HashSet<String> nameSet = new HashSet<String>();
		nameSet.addAll(names);

		ArrayList<Person> tmpMembers = new ArrayList<Person>();
		for (String name : names)
			if (hasPerson(name)) {
				Person p = nameToMember.get(name).getPerson();
				Person father = nameSet.contains(p.getFather().getName()) ? p.getFather() : null;
				Person mother = nameSet.contains(p.getMother().getName()) ? p.getMother() : null;

				tmpMembers.add(new Person(p.getName(), father, mother, p.getSex(), p.getDisease(), p.getExtraFields()));
			}
		return new Pedigree(name, tmpMembers);
	}

	/**
	 * @return a pedigree with one affected sample
	 */
	public static Pedigree constructSingleSamplePedigree(String sampleName) {
		final Person person = new Person(sampleName, null, null, Sex.UNKNOWN, Disease.AFFECTED);
		return new Pedigree("pedigree", List.of(person));
	}

	/**
	 * @return <code>true</code> if the pedigree contains a sample with the given <code>name</code>.
	 */
	public boolean hasPerson(String name) {
		return nameToMember.containsKey(name);
	}

	/**
	 * @return list of members, in the same order as in {@link #members}.
	 */
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		for (Person p : members)
			names.add(p.getName());
		return Collections.unmodifiableList(names);
	}

	@Override
	public String toString() {
		return "Pedigree [name=" + name + ", members=" + members + ", nameToMember=" + nameToMember + "]";
	}

	/**
	 * Helper class, used in the name to member map.
	 */
	public static class IndexedPerson {
		private final int idx;
		private final Person person;

		public IndexedPerson(int idx, Person person) {
			this.idx = idx;
			this.person = person;
		}

		/**
		 * @return numeric index of person in pedigree
		 */
		public int getIdx() {
			return idx;
		}

		/**
		 * @return the wrapped {@link Person}
		 */
		public Person getPerson() {
			return person;
		}
	}

}
