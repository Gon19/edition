package pt.ist.socialsoftware.edition.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.recommendation.VSMFragInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.search.options.SearchOption;
import pt.ist.socialsoftware.edition.utils.RangeJson;

public class VirtualEditionInter extends VirtualEditionInter_Base {

	public VirtualEditionInter(Section section, FragInter inter, int number) {
		setFragment(inter.getFragment());
		setHeteronym(null);
		setDate(null);
		setSection(section);
		setNumber(number);
		setUses(inter);
	}

	@Override
	public void remove() {
		setSection(null);

		setUses(null);

		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		for (FragInter inter : getIsUsedBySet()) {
			inter.remove();
		}

		for (Annotation annotation : getAnnotationSet()) {
			annotation.remove();
		}

		super.remove();
	}

	@Override
	public String getShortName() {
		return getVirtualEdition().getAcronym();
	}

	@Override
	public String getTitle() {
		return getUses().getTitle();
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.VIRTUAL;
	}

	public int compareVirtualEditionInter(VirtualEditionInter other) {
		int diff = getNumber() - other.getNumber();
		int result = diff > 0 ? 1 : (diff < 0) ? -1 : 0;
		if (result != 0) {
			return result;
		} else {
			String myTitle = getTitle();
			String otherTitle = other.getTitle();
			return myTitle.compareTo(otherTitle);
		}
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Edição Virtual: " + getVirtualEdition().getTitle() + "(" + getVirtualEdition().getAcronym()
				+ ")" + "<br>";

		result = result + "Edição Base: " + getLastUsed().getShortName() + "<br>";

		result = result + "Título: " + getTitle() + "<br>";

		if (getHeteronym() != null) {
			result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";
		}

		if (getDate() != null) {
			result = result + "Data: " + getDate().toString("dd-MM-yyyy") + "<br>";
		}

		return result;
	}

	@Override
	public boolean belongs2Edition(Edition edition) {
		return getVirtualEdition() == edition;
	}

	@Override
	public FragInter getLastUsed() {
		return getUses().getLastUsed();
	}

	@Override
	public Edition getEdition() {
		return getVirtualEdition();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = getUses().getListUsed();
		listUses.add(0, getUses());
		return listUses;
	}

	@Override
	public String getReference() {
		return Integer.toString(getNumber());
	}

	public Set<LdoDUser> getAnnotationContributorSet() {
		Set<LdoDUser> contributors = new HashSet<LdoDUser>();
		for (Annotation annotation : getAnnotationSet()) {
			contributors.add(annotation.getUser());
		}
		return contributors;
	}

	@Override
	public boolean accept(SearchOption option) {
		return option.visit(this);
	}

	@Override
	public Collection<Double> accept(Property property) {
		return property.visit(this);
	}

	public VirtualEdition getVirtualEdition() {
		return getSection().getRootSection().getVirtualEdition();
	}

	public FragInter getNextInter() {
		LdoDUser user = LdoDUser.getAuthenticatedUser();
		VSMFragInterRecommender recommender = new VSMFragInterRecommender();
		Collection<Property> properties = user.getRecommendationWeights(getVirtualEdition()).getProperties();
		return recommender.getMostSimilarItem(this, getVirtualEdition().getIntersSet(), properties);
	}

	@Atomic(mode = TxMode.WRITE)
	public Annotation createAnnotation(String quote, String text, LdoDUser user, List<RangeJson> rangeList,
			List<String> tagList) {

		SimpleText startText = getFragment().getTextPortion().getSimpleText(getLastUsed(), 0,
				rangeList.get(0).getStartOffset());
		SimpleText endText = getFragment().getTextPortion().getSimpleText(getLastUsed(), 0,
				rangeList.get(0).getEndOffset());

		Annotation annotation = new Annotation(this, startText, endText, quote, text, user);

		for (RangeJson rangeJson : rangeList) {
			new Range(annotation, rangeJson.getStart(), rangeJson.getStartOffset(), rangeJson.getEnd(),
					rangeJson.getEndOffset());
		}

		Taxonomy taxonomy = getVirtualEdition().getTaxonomy();
		for (String tag : tagList) {
			taxonomy.createTagInTextPortion(annotation, tag);
		}

		return annotation;
	}

	@Atomic(mode = TxMode.WRITE)
	public void associate(LdoDUser ldoDUser, Taxonomy taxonomy, Set<Category> categories) {
		for (Category category : categories) {
			if (!getTagSet().stream().filter(t -> (t.getCategory() == category) && (t.getAnnotation() == null))
					.findFirst().isPresent())
				new Tag().init(this, category, null, ldoDUser);
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public void dissociate(Category category) {
		Set<Tag> tags = getTagSet().stream().filter(t -> t.getCategory() == category).collect(Collectors.toSet());
		for (Tag tag : tags) {
			tag.remove();
		}

		Set<Annotation> annotations = getAnnotationSet().stream()
				.filter(a -> a.getTagSet().isEmpty() && a.getText() == null).collect(Collectors.toSet());
		for (Annotation annotation : annotations) {
			annotation.remove();
		}
	}

}
