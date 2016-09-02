package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.search.Indexer;

public class ExpertEditionInter extends ExpertEditionInter_Base {

	public ExpertEditionInter() {
		setNotes("");
		setVolume("");
	}

	@Override
	public void remove() {
		setExpertEdition(null);

		String externalId = getExternalId();

		super.remove();

		// remove from Lucene
		List<String> externalIds = new ArrayList<String>();
		externalIds.add(externalId);
		Indexer indexer = new Indexer();
		indexer.cleanMissingHits(externalIds);

		// remove from mallet directory
		TopicModeler topicModeler = new TopicModeler();
		topicModeler.deleteFile(externalId);
	}

	@Override
	public String getShortName() {
		return getExpertEdition().getEditorShortName();
	}

	@Override
	public String getTitle() {
		String fragTitle = super.getTitle();
		if (fragTitle == null || fragTitle.trim().equals("")) {
			return getFragment().getTitle();
		} else {
			return fragTitle;
		}
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.EDITORIAL;
	}

	public int compareExpertEditionInter(ExpertEditionInter other) {
		String myEditor = getExpertEdition().getEditor();
		String otherEditor = other.getExpertEdition().getEditor();

		if (myEditor.equals(otherEditor)) {
			return compareNumber(other);
		} else if (myEditor.equals(ExpertEdition.COELHO)) {
			return -1;
		} else if (otherEditor.equals(ExpertEdition.COELHO)) {
			return 1;
		} else if (myEditor.equals(ExpertEdition.CUNHA)) {
			return -1;
		} else if (otherEditor.equals(ExpertEdition.CUNHA)) {
			return 1;
		} else if (myEditor.equals(ExpertEdition.ZENITH)) {
			return -1;
		} else if (otherEditor.equals(ExpertEdition.ZENITH)) {
			return 1;
		} else {
			assert false : "To extend when new expert editions are include";
			return 0;
		}
	}

	public int compareNumber(ExpertEditionInter other) {
		if (getNumber() == other.getNumber()) {
			return comparePage(getStartPage(), other.getStartPage());
		} else if (getNumber() < other.getNumber()) {
			return -1;
		} else
			return 1;
	}

	private int comparePage(int page1, int page2) {
		if (page1 < page2)
			return -1;
		else if (page1 == page2)
			return 0;
		else
			return 1;

	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Título: " + getTitle() + "<br>";

		result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";

		String number = getNumber() == 0 ? "" : Integer.toString(getNumber());
		result = result + "Número: " + number + "<br>";

		result = result + "Volume: " + getVolume() + "<br>";

		result = result + "Página: " + getStartPage() + "<br>";

		if (getLdoDDate() != null) {
			String precision = getLdoDDate().getPrecision() != null
					? " Precisão: " + getLdoDDate().getPrecision().getDesc() : "";

			result = result + "Data: " + getLdoDDate().print() + precision + "<br>";
		}

		result = result + "Notas: " + getNotes() + "<br>";

		return result + super.getMetaTextual();
	}

	@Override
	public boolean belongs2Edition(Edition edition) {
		return this.getExpertEdition() == edition;
	}

	@Override
	public FragInter getLastUsed() {
		return this;
	}

	@Override
	public Edition getEdition() {
		return getExpertEdition();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = new ArrayList<FragInter>();
		return listUses;
	}

	@Override
	public String getReference() {
		return Integer.toString(getNumber());
	}

	@Override
	public Set<Category> getAllDepthCategories() {
		return new HashSet<Category>();
	}

	@Override
	public Set<Annotation> getAllDepthAnnotations() {
		return new HashSet<Annotation>();
	}

	@Override
	public Set<Tag> getAllDepthTags() {
		return new HashSet<Tag>();
	}

}
