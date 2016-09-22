package pt.ist.socialsoftware.edition.domain;

public class ManuscriptSource extends ManuscriptSource_Base {

	public enum Form {
		LEAF
	};

	public enum Material {
		PAPER
	};

	public enum Medium {
		PEN("pen"), PENCIL("pencil"), BLUE_INK("blue-ink"), BLACK_INK("black-ink"), VIOLET_INK("violet-ink"), RED_INK(
				"red-ink"), GREEN_INK("green-ink");

		private final String desc;

		Medium(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public ManuscriptSource() {
		super();
		setType(SourceType.MANUSCRIPT);
		setHasLdoDLabel(false);
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Identificação: " + getIdno() + "<br>";

		String form = getForm() == ManuscriptSource.Form.LEAF ? "Folha" : "";
		result = result + "Formato: " + form + "<br>";

		String material = getMaterial() == ManuscriptSource.Material.PAPER ? "Papel" : "";
		result = result + "Material: " + material + "<br>";

		String columns = getColumns() == 0 ? "" : Integer.toString(getColumns());
		result = result + "Colunas: " + columns + "<br>";

		result = result + "LdoD: " + getHasLdoDLabel() + "<br>";

		for (HandNote handNote : getHandNoteSet()) {
			result = result + "Manuscrito: ";
			if (handNote.getMedium() != null) {
				result = result + "medium(" + handNote.getMedium().getDesc() + "), ";
			}
			result = result + "nota(" + handNote.getNote() + ")";
			if (handNote.getTextPortionSet().size() != 0) {
				result = result + ", número de parágrafos referidos(" + handNote.getTextPortionSet().size() + ")";
			}
			result = result + "<br>";
		}

		for (TypeNote typeNote : getTypeNoteSet()) {
			result = result + "Dactiloscrito: ";
			if (typeNote.getMedium() != null) {
				result = result + "medium(" + typeNote.getMedium().getDesc() + "), ";
			}
			result = result + "nota(" + typeNote.getNote() + "), número de parágrafos referidos("
					+ typeNote.getTextPortionSet().size() + ")<br>";
		}

		if (getNotes() != null) {
			result = result + "Notas: " + getNotes();
			result = result + "<br>";
		}

		Facsimile facs = getFacsimile();
		if (facs != null) {
			result = result + "Facsimiles: ";

			int i = 1;
			for (Surface surf : facs.getSurfaces()) {
				String suffix = facs.getSurfaces().size() == 1 ? "" : "." + Integer.toString(i);
				result = result + "<a href=/facs/" + surf.getGraphic() + " target=" + "\"" + "_blank" + "\"" + ">"
						+ getAltIdentifier() + suffix + "</a> ";
				i++;
			}
		}

		result = result + "<br>";

		if (getLdoDDate() != null) {
			String precision = getLdoDDate().getPrecision() != null
					? " Precisão: " + getLdoDDate().getPrecision().getDesc() : "";

			result = result + "Data: " + getLdoDDate().print() + precision + "<br>";
		}

		return result;
	}

	@Override
	public void remove() {
		if (getDimensions() != null) {
			getDimensions().remove();
		}

		for (HandNote handNote : getHandNoteSet()) {
			handNote.remove();
		}

		for (TypeNote typeNote : getTypeNoteSet()) {
			typeNote.remove();
		}

		super.remove();
	}

}
