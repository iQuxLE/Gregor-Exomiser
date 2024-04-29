package org.monarchinitiative.gregor.pedigree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO(holtgrem): Test me!

/**
 * Decorator of {@link Pedigree} that allows for the easy querying.
 *
 * @author <a href="mailto:manuel.holtgrewe@charite.de">Manuel Holtgrewe</a>
 * @author <a href="mailto:Peter.Robinson@jax.org">Peter N Robinson</a>
 */
public class PedigreeQueryDecorator {

	/**
	 * the pedigree
	 */
	private final Pedigree pedigree;

	/**
	 * Initialize decorator.
	 */
	public PedigreeQueryDecorator(Pedigree pedigree) {
		this.pedigree = pedigree;
	}

	/**
	 * @return the decorated pedigree
	 */
	public Pedigree getPedigree() {
		return pedigree;
	}

	/**
	 * @param person the person to check
	 * @return <code>true</code> if the nth person in the PED file is parent of an affected child
	 */
	public boolean isParentOfAffected(Person person) {
		for (Person member : pedigree.getMembers())
			if (member.getFather() == person || member.getMother() == person)
				return true;
		return false;
	}

	/**
	 * @return set with the name of the unaffected persons
	 */
	public Set<String> getUnaffectedNames() {
		Set<String> resultNames = new HashSet<>();
		for (Person member : pedigree.getMembers())
			if (member.getDisease() == Disease.UNAFFECTED)
				resultNames.add(member.getName());
		return Collections.unmodifiableSet(resultNames);
	}

	/**
	 * @return set with the name of the parents
	 */
	public Set<String> getParentNames() {
		Set<String> parentNames = new HashSet<>();
		for (Person member : pedigree.getMembers()) {
			if (member.getFather() != null)
				parentNames.add(member.getFather().getName());
			if (member.getMother() != null)
				parentNames.add(member.getMother().getName());
		}
		return Collections.unmodifiableSet(parentNames);
	}

	public Set<String> getParentsNames() {
		return pedigree.getMembers().stream()
				.flatMap(member -> Stream.of(member.getFather(), member.getMother()))
				.filter(Objects::nonNull)
				.map(Person::getName)
				.collect(Collectors.toUnmodifiableSet());
	}

	/**
	 * @return set with the name of the parents from affected females.
	 */
	public Set<String> getAffectedFemaleParentNames() {
		Set<String> parentNames = new HashSet<>();
		for (Person member : pedigree.getMembers()) {
			if (member.isAffected() && member.isFemale()) {
				if (member.getFather() != null)
					parentNames.add(member.getFather().getName());
				if (member.getMother() != null)
					parentNames.add(member.getMother().getName());
			}
		}
		return Collections.unmodifiableSet(parentNames);
	}

	/**
	 * @return set with the name of the parents from affected males.
	 */
	public Set<String> getAffectedMaleParentNames() {
		Set<String> parentNames = new HashSet<>();
		for (Person member : pedigree.getMembers()) {
			if (member.isAffected() && member.isMale()) {
				if (member.getFather() != null)
					parentNames.add(member.getFather().getName());
				if (member.getMother() != null)
					parentNames.add(member.getMother().getName());
			}
		}
		return Collections.unmodifiableSet(parentNames);
	}

	/**
	 * @return list of parents in the same order as in {@link Pedigree#members pedigree.getMembers()}
	 */
	public List<Person> getParents() {
		Set<String> parentNames = getParentNames();

		List<Person> parents = new ArrayList<>();
		for (Person member : pedigree.getMembers())
			if (parentNames.contains(member.getName()))
				parents.add(member);
		return Collections.unmodifiableList(parents);
	}

	/**
	 * @return number of parents in pedigree
	 */
	public int getNumberOfParents() {
		HashSet<String> parentNames = new HashSet<String>();
		for (Person member : pedigree.getMembers()) {
			if (member.getFather() != null)
				parentNames.add(member.getFather().getName());
			if (member.getMother() != null)
				parentNames.add(member.getMother().getName());
		}
		return parentNames.size();
	}

	/**
	 * @return number of affected individuals in the pedigree
	 */
	public int getNumberOfAffecteds() {
		int result = 0;
		for (Person member : pedigree.getMembers())
			if (member.getDisease() == Disease.AFFECTED)
				result += 1;
		return result;
	}

	/**
	 * @return number of unaffected individuals in the pedigree
	 */
	public int getNumberOfUnaffecteds() {
		int result = 0;
		for (Person member : pedigree.getMembers())
			if (member.getDisease() == Disease.UNAFFECTED)
				result += 1;
		return result;
	}

	/**
	 * @return sibling map for each {@link Person} in {@link Pedigree}, both parents must be in {@link Pedigree} and the
	 * same pedigree
	 */
	public Map<Person, List<Person>> buildSiblings() {
		Map<Person, List<Person>> map = new LinkedHashMap<>();

		for (Person p1 : pedigree.getMembers()) {
			if (p1.getMother() == null || p1.getFather() == null)
				continue;
			List<Person> listBuilder = new ArrayList<>();
			for (Person p2 : pedigree.getMembers()) {
				if (p1.equals(p2) || !p1.getMother().equals(p2.getMother()) || !p1.getFather().equals(p2.getFather()))
					continue;
				listBuilder.add(p2);
			}
			map.put(p1, Collections.unmodifiableList(listBuilder));
		}

;		return Collections.unmodifiableMap(map);
	}

}
