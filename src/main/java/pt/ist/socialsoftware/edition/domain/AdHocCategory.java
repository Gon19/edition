package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdHocCategory extends AdHocCategory_Base {

	@Override
	public AdHocCategory init(Taxonomy taxonomy) {
		super.init(taxonomy);
		setType(CategoryType.ADHOC);

		return this;
	}

	@Override
	public AdHocCategory init(Taxonomy taxonomy, String name) {
		super.init(taxonomy, name);
		setType(CategoryType.ADHOC);

		return this;
	}

	@Override
	public List<Tag> getSortedTags() {
		List<Tag> results = new ArrayList<Tag>(getTagSet());

		Collections.sort(results);

		return results;
	}

	public void createUserTagInTextPortion(Annotation annotation, String tag) {
		new UserTagInTextPortion().init(this, annotation);

	}

}
