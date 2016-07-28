package pt.ist.socialsoftware.edition.domain;

public class PrintedSource extends PrintedSource_Base {

	public PrintedSource() {
		super();
		setType(SourceType.PRINTED);
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";

		result = result + "Título: " + getTitle() + "<br>";

		result = result + "Revista: " + getJournal() + "<br>";

		result = result + "Número: " + getIssue() + "<br>";

		result = result + "Páginas: " + getStartPage() + "-" + getEndPage() + "<br>";

		result = result + "Local de Publicação: " + getPubPlace() + "<br>";

		if (getLdoDDate() != null) {
			String precision = getLdoDDate().getPrecision() != null
					? " Precisão: " + getLdoDDate().getPrecision().getDesc() : "";

			result = result + "Data: " + getLdoDDate().print() + precision + "<br>";
		}

		Facsimile facs = getFacsimile();
		if (facs != null) {
			result = result + "Facsimiles: ";

			for (Surface surf : facs.getSurfaces()) {
				result = result + "<a href=/facs/" + surf.getGraphic() + " target=" + "\"" + "_blank" + "\"" + ">"
						+ surf.getGraphic() + "</a> ";
			}
		}

		result = result + "<br>";

		return result;
	}

}
