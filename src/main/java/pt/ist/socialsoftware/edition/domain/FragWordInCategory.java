package pt.ist.socialsoftware.edition.domain;

public class FragWordInCategory extends FragWordInCategory_Base implements
		Comparable<FragWordInCategory> {

	public FragWordInCategory(Category category, FragWord fragWord, int weight) {
		setCategory(category);
		setFragWord(fragWord);
		setWeight(weight);
	}

	public void remove() {
		setCategory(null);
		setFragWord(null);

		deleteDomainObject();
	}

	@Override
	public int compareTo(FragWordInCategory o) {
		if (this.getWeight() < o.getWeight())
			return 1;
		else if (this.getWeight() > o.getWeight())
			return -1;
		else
			return 0;
	}

}
