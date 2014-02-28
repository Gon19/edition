package pt.ist.socialsoftware.edition.domain;

public class PrintedSource extends PrintedSource_Base {

	public PrintedSource() {
		setType(SourceType.PRINTED);
	}

	@Override
	public String getName() {
		return getTitle() + " " + getPubPlace() + " " + getIssue();
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Título: " + getTitle() + "<br>";

		result = result + "Local de Publicação: " + getPubPlace() + "<br>";

		result = result + "Número: " + getIssue() + "<br>";

		if (getDate() != null) {
			result = result + "Data: " + getDate().toString("dd-MM-yyyy")
					+ "<br>";
		}

		result = result + "Facsimiles: ";

		Facsimile facs = getFacsimile();
		if (facs != null) {
			result = result + "Facsimiles: ";

			for (Surface surf : facs.getSurfaces()) {
				result = result + "<a href=/facs/" + surf.getGraphic()
						+ " target=" + "\"" + "_blank" + "\"" + ">"
						+ surf.getGraphic() + "</a> ";
			}
		}
		return result;
	}

}
