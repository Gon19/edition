package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

public class Category extends Category_Base implements Comparable<Category> {

	public Category init(Taxonomy taxonomy) {
		setTaxonomy(taxonomy);

		return this;
	}

	public Category init(Taxonomy taxonomy, String name) {
		setTaxonomy(taxonomy);
		setName(name);

		return this;
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		setTaxonomy(null);

		deleteDomainObject();
	}

	@Atomic(mode = TxMode.WRITE)
	@Override
	public void setName(String name) {
		if (name == null || name.equals("")) {
			throw new LdoDException();
		}

		for (Category category : getTaxonomy().getCategoriesSet()) {
			if ((category != this) && (category.getName().equals(name))) {
				throw new LdoDDuplicateNameException();
			}
		}
		super.setName(name);
	}

	@Override
	public int compareTo(Category other) {
		return getName().compareTo(other.getName());
	}

	public Tag getTag(FragInter fragInter) {
		for (Tag tag : getTagSet()) {
			if (tag.getInter() == fragInter)
				return tag;
		}
		return null;
	}

	public List<Tag> getSortedTags() {
		List<Tag> tags = new ArrayList<Tag>(getTagSet());

		Collections.sort(tags);

		return tags;
	}

	public int getWeight(VirtualEditionInter inter) {
		return getTagSet().stream().filter(t -> t.getInter() == inter).collect(Collectors.toSet()).size();
	}

}
