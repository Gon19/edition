package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.utils.TopicDTO;
import pt.ist.socialsoftware.edition.utils.TopicInterPercentageDTO;
import pt.ist.socialsoftware.edition.utils.TopicListDTO;

public class Taxonomy extends Taxonomy_Base {

	public Taxonomy() {
		setOpenManagement(false);
		setOpenVocabulary(true);
		setOpenAnnotation(false);
	}

	public String getName() {
		return getEdition().getTitle();
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setEdition(null);

		for (Category category : getCategoriesSet()) {
			category.remove();
		}

		for (TaxonomyWeight taxonomyWeight : getTaxonomyWeightSet()) {
			taxonomyWeight.remove();
		}

		deleteDomainObject();
	}

	public Set<Tag> getTagSet(VirtualEditionInter inter) {
		Set<Tag> set = new HashSet<Tag>();
		for (Tag tag : inter.getTagSet()) {
			if (tag.getCategory().getTaxonomy() == this) {
				set.add(tag);
			}
		}
		return set;
	}

	public List<Tag> getSortedTags(VirtualEditionInter fragInter) {
		List<Tag> tags = new ArrayList<Tag>(getTagSet(fragInter));
		Collections.sort(tags);
		return tags;
	}

	public List<VirtualEditionInter> getSortedFragInter() {
		Set<VirtualEditionInter> set = new HashSet<VirtualEditionInter>();
		for (Category category : getCategoriesSet()) {
			for (Tag tag : category.getTagSet()) {
				set.add(tag.getInter());
			}
		}
		List<VirtualEditionInter> list = new ArrayList<VirtualEditionInter>(set);
		Collections.sort(list);

		return list;
	}

	public Set<LdoDUser> getTagContributorSet(VirtualEditionInter inter) {
		Set<LdoDUser> contributors = new HashSet<LdoDUser>();
		for (Tag tag : getTagSet(inter)) {
			contributors.add(tag.getContributor());
		}
		return contributors;
	}

	public Category getCategory(String name) {
		for (Category category : getCategoriesSet()) {
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public Category merge(List<Category> categories) {

		String name = categories.stream().map(c -> c.getName()).collect(Collectors.joining(";"));

		while (getCategory(name) != null) {
			name = name + "1";
		}

		Category category = new Category().init(this, name);

		categories.stream().flatMap(c -> c.getTagSet().stream()).forEach(t -> category.addTag(t));

		categories.stream().forEach(c -> c.remove());

		return category;
	}

	@Atomic(mode = TxMode.WRITE)
	public Category extract(Category category, Set<Tag> tags) {
		Set<Tag> remainingTags = new HashSet<Tag>(category.getSortedTags());
		remainingTags.removeAll(tags);

		Category newCategory = new Category().init(this, category.getName() + "_Ext");

		tags.stream().forEach(t -> newCategory.addTag(t));

		return newCategory;
	}

	public void createTag(VirtualEditionInter virtualEditionInter, String categoryName, Annotation annotation,
			LdoDUser ldoDUser) {
		if (!getOpenVocabulary() && getCategory(categoryName) == null)
			return;
		new Tag().init(virtualEditionInter, categoryName, annotation, ldoDUser);
	}

	@Atomic(mode = TxMode.WRITE)
	public Category createCategory(String name) {
		return new Category().init(this, name);
	}

	public String getCategoriesJSON() {
		ObjectMapper mapper = new ObjectMapper();

		List<CategoryDTO> categories = getCategoriesSet().stream().map(c -> new CategoryDTO(c))
				.collect(Collectors.toList());

		try {
			return mapper.writeValueAsString(categories);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public void createGeneratedCategories(TopicListDTO topicList) {
		LdoDUser user = LdoD.getInstance().getUser(topicList.getUsername());

		for (TopicDTO topic : topicList.getTopics()) {
			Category category = new Category();
			category.init(this, topic.getName());
			for (TopicInterPercentageDTO topicPercentage : topic.getInters()) {
				VirtualEditionInter inter = FenixFramework.getDomainObject(topicPercentage.getExternalId());
				new GeneratedTagInFragInter().init(inter, category, user, topicPercentage.getPercentage());
			}
		}

	}

	@Atomic(mode = TxMode.WRITE)
	public void delete(List<Category> categories) {
		categories.stream().forEach(c -> c.remove());
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(boolean openManagement, boolean openVocabulary, boolean openAnnotation) {
		setOpenManagement(openManagement);
		setOpenVocabulary(openVocabulary);
		setOpenAnnotation(openAnnotation);
	}

	public boolean canManipulateAnnotation(LdoDUser user) {
		if (user != null && getOpenAnnotation())
			return true;
		else
			return getEdition().getParticipantSet().contains(user);
	}

	public boolean canManipulateTaxonomy(LdoDUser user) {
		if (getOpenManagement())
			return getEdition().getParticipantSet().contains(user);
		else
			return getEdition().getAdminSet().contains(user);
	}

}
