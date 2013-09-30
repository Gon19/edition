package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Edition.SourceType;

public class ExpertEditionInter extends ExpertEditionInter_Base {

	public ExpertEditionInter() {
		super();
	}

	@Override
	public void remove() {
		setExpertEdition(null);

		super.remove();
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
	public SourceType getSourceType() {
		return SourceType.EDITORIAL;
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

		result = result + "Data: " + getDate() + "<br>";

		result = result + "Notas: " + getNotes();

		return result;
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
}
