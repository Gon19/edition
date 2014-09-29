package pt.ist.socialsoftware.edition.generators;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.Facsimile;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Surface;
import pt.ist.socialsoftware.edition.generators.visitors.TEITextPortionWriter;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class TEIGenerator {

	Document jdomDoc = null;

	// Element rootElement = null;

	TEITextPortionWriter writer = null;
	List<FragInter> fragInterSelectedSet;

	Namespace xmlns;

	// List<String> fragInterSelectedSet = new ArrayList<String>();

	public TEIGenerator() {
		xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
	}

	public void generate(Map<Fragment, List<FragInter>> fragmentMap) {
		// TODO Auto-generated method stub

		Element rootElement = generateCorpus();
		generateCorpusHeader(rootElement);

		Fragment fragment;

		for (Map.Entry<Fragment, List<FragInter>> entry : fragmentMap
				.entrySet()) {

			fragment = entry.getKey();
			fragInterSelectedSet = entry.getValue();

			generateFragment(rootElement, fragment, fragInterSelectedSet);
		}
	}

	// public void generate(Fragment fragment) {
	// Element rootElement = generateCorpus();
	// generateCorpusHeader(rootElement);
	// generateFragment(rootElement, fragment);
	// }
	//
	// public void generate(List<Fragment> fragments) {
	//
	// Element rootElement = generateCorpus();
	// generateCorpusHeader(rootElement);
	//
	// for (Fragment fragment : fragments) {
	// generateFragment(rootElement, fragment);
	// }
	// }

	private Element generateCorpus() {
		jdomDoc = new Document();
		Element rootElement = new Element("teiCorpus");

		rootElement.setNamespace(xmlns);

		rootElement.addNamespaceDeclaration(Namespace.getNamespace("svg",
				"http://www.w3.org/2000/svg"));

		rootElement.addNamespaceDeclaration(Namespace.getNamespace("xi",
				"http://www.w3.org/2001/XInclude"));

		jdomDoc.setRootElement(rootElement);
		return rootElement;
	}

	private void generateCorpusHeader(Element rootElement) {
		Element newElement = new Element("teiHeader", xmlns);
		Attribute type = new Attribute("type", "corpus");
		newElement.setAttribute(type);
		rootElement.addContent(newElement);
	}

	private void generateFragment(Element rootElement, Fragment fragment,
			List<FragInter> fragInterSelectedSet) {

		// Namespace xmlns = Namespace.XML_NAMESPACE;
		// .getNamespace("http://www.tei-c.org/ns/1.0");

		// Namespace xmlns =
		// Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

		Element fragElement = new Element("TEI", xmlns);

		// fragElement.addNamespaceDeclaration(Namespace
		// .getNamespace("http://www.tei-c.org/ns/1.0"));
		// //
		// fragElement.setNamespace(Namespace
		// .getNamespace("http://www.tei-c.org/ns/1.0"));

		// fragElement.addNamespaceDeclaration(Namespace.getNamespace("",
		// fragment.getXmlId()));

		// Attribute type = new Attribute("xmlns",
		// "http://www.tei-c.org/ns/1.0");
		// fragElement.setAttribute(type);

		Attribute id = new Attribute("id", fragment.getXmlId(),
				Namespace.XML_NAMESPACE);
		fragElement.setAttribute(id);

		generateTextHeader(fragment, fragElement);
		generateFacsimiles(fragment, fragElement);
		generateTranscription(fragElement, fragment, fragInterSelectedSet);

		rootElement.addContent(fragElement);
	}

	private void generateTextHeader(Fragment fragment, Element rootElement) {

		Element headerElement = new Element("teiHeader", xmlns);
		Attribute type = new Attribute("type", "text");
		headerElement.setAttribute(type);
		rootElement.addContent(headerElement);

		Element fileDescElement = new Element("fileDesc", xmlns);
		headerElement.addContent(fileDescElement);

		// titleStmt codification

		Element titleStmtElement = new Element("titleStmt", xmlns);
		fileDescElement.addContent(titleStmtElement);

		Element titleElement = new Element("title", xmlns);
		titleStmtElement.addContent(titleElement);
		titleElement.addContent(fragment.getTitle());

		Element authorElement = new Element("author", xmlns);
		titleStmtElement.addContent(authorElement);
		authorElement.addContent(fragment.getLdoD().getAuthor());

		Element respStmtElement = new Element("respStmt", xmlns);
		titleStmtElement.addContent(respStmtElement);

		Element respElement = new Element("resp", xmlns);
		respStmtElement.addContent(respElement);
		respElement.addContent("encoding");

		// TODO: coder's name
		Element nameElement = new Element("name", xmlns);
		respStmtElement.addContent(nameElement);

		// publicationStmt codification

		Element publicationStmtElement = new Element("publicationStmt", xmlns);
		fileDescElement.addContent(publicationStmtElement);

		Element publisherElement = new Element("publisher", xmlns);
		publicationStmtElement.addContent(publisherElement);
		publisherElement.addContent("University of Coimbra");

		Element pubPlaceElement = new Element("pubPlace", xmlns);
		publicationStmtElement.addContent(pubPlaceElement);
		pubPlaceElement.addContent("Coimbra");

		Element availabilityElement = new Element("availability", xmlns);
		publicationStmtElement.addContent(availabilityElement);
		availabilityElement.setAttribute("status", "restricted");

		Element licenceElement = new Element("licence", xmlns);
		availabilityElement.addContent(licenceElement);
		licenceElement.setAttribute("target",
				"http://creativecommons.org/licenses/by-sa/3.0/");
		// TODO: <p>xpto</p> ?

		Element dateElement = new Element("date", xmlns);
		publicationStmtElement.addContent(dateElement);
		dateElement.setAttribute("when", "2014");

		Element sourceDescElement = new Element("sourceDesc", xmlns);
		fileDescElement.addContent(sourceDescElement);

		generateSources(fragment, sourceDescElement);
		generateWitnesses(fragment, sourceDescElement);
	}

	private void generateSources(Fragment fragment, Element rootElement) {

		Element listBibl = new Element("listBibl", xmlns);
		// listBibl.addNamespaceDeclaration(Namespace.getNamespace("id",
		// fragment.getXmlId() + ".SRC"));

		Attribute id = new Attribute("id", fragment.getXmlId() + ".SRC",
				Namespace.XML_NAMESPACE);
		listBibl.setAttribute(id);

		Element listBibl2 = new Element("listBibl", xmlns);
		// listBibl2.addNamespaceDeclaration(Namespace.getNamespace("id",
		// fragment.getXmlId() + ".SRC.MS"));

		Attribute id2 = new Attribute("id", fragment.getXmlId() + ".SRC.MS",
				Namespace.XML_NAMESPACE);
		listBibl2.setAttribute(id2);

		rootElement.addContent(listBibl);
		listBibl.addContent(listBibl2);

		// generate HeaderSources

		ManuscriptSource manuscript = null;

		Element msDescElement = null;
		Element msIdentifierElement = null;

		Element settlementElement = null;
		Element repositoryElement = null;
		Element idnoElement = null;
		Element altIdentifierElement = null;
		Element idnoAltElement = null;

		Element physDescElement = null;
		Element objectDescElement = null;
		Element supportDescElement = null;
		Element layoutDescElement = null;
		Element layoutElement = null;

		Element handDescElement = null;
		Element additionsElement = null;
		Element bindingDescElement = null;

		Element historyElement = null;
		Element originElement = null;
		Element origdateElement = null;

		for (Source source : fragment.getSourcesSet()) {

			// TODO: outros tipos de fontes
			manuscript = (ManuscriptSource) source;

			msDescElement = new Element("msDesc", xmlns);

			Attribute idms = new Attribute("id", manuscript.getXmlId(),
					Namespace.XML_NAMESPACE);
			msDescElement.setAttribute(idms);

			msIdentifierElement = new Element("msIdentifier", xmlns);
			physDescElement = new Element("physDesc", xmlns);
			historyElement = new Element("history", xmlns);

			msDescElement.addContent(msIdentifierElement);
			msDescElement.addContent(physDescElement);
			msDescElement.addContent(historyElement);

			settlementElement = new Element("settlement", xmlns);
			settlementElement.addContent(manuscript.getSettlement());
			msIdentifierElement.addContent(settlementElement);

			repositoryElement = new Element("repository", xmlns);
			repositoryElement.addContent(manuscript.getRepository());
			msIdentifierElement.addContent(repositoryElement);

			idnoElement = new Element("idno", xmlns);
			idnoElement.addContent(manuscript.getIdno());
			msIdentifierElement.addContent(idnoElement);

			altIdentifierElement = new Element("altIdentifier", xmlns);
			altIdentifierElement.setAttribute("type", "SC");
			msIdentifierElement.addContent(altIdentifierElement);

			idnoAltElement = new Element("idno", xmlns);
			idnoAltElement.addContent(source.getName());
			altIdentifierElement.addContent(idnoAltElement);

			// physDesc // TODO: strings
			objectDescElement = new Element("objectDesc", xmlns);
			objectDescElement.setAttribute("form", manuscript.getForm()
					.toString());
			physDescElement.addContent(objectDescElement);

			supportDescElement = new Element("supportDesc", xmlns);
			supportDescElement.setAttribute("material", manuscript
					.getMaterial().name());
			objectDescElement.addContent(supportDescElement);

			layoutDescElement = new Element("layoutDesc", xmlns);
			objectDescElement.addContent(layoutDescElement);

			layoutElement = new Element("layout", xmlns);
			layoutElement.setAttribute("columns",
					Integer.toString(manuscript.getColumns()));
			layoutDescElement.addContent(layoutElement);

			handDescElement = new Element("handDesc", xmlns);
			handDescElement.addContent(manuscript.getNotes());
			objectDescElement.addContent(handDescElement);

			additionsElement = new Element("additions", xmlns);
			objectDescElement.addContent(additionsElement);

			bindingDescElement = new Element("bindingDesc", xmlns);
			objectDescElement.addContent(bindingDescElement);

			historyElement = new Element("history", xmlns);
			msDescElement.addContent(historyElement);

			originElement = new Element("origin", xmlns);
			historyElement.addContent(originElement);

			// TODO: update date format; field precision
			String date = "";
			String precision = "";

			if (manuscript.getDate() != null)
				date = manuscript.getDate().toString();

			origdateElement = new Element("origdate", xmlns);
			origdateElement.setAttribute("date", date);
			origdateElement.setAttribute("precision", precision);
			origdateElement.addContent(date);
			originElement.addContent(origdateElement);

			listBibl2.addContent(msDescElement);

		}

	}

	private void generateWitnesses(Fragment fragment, Element rootElement) {

		// generate Sources Interp
		Element listWitElement = null;
		Element listWitAuthElement = null;
		Element listWitEdElement = null;
		Element witnessElement = null;
		Element refElement = null;

		// listWitEd
		Element headListWitElement = null;
		Element listWitEdCritElement = null;

		Element biblElement = null;
		Element respStmtElement = null;
		Element respElement = null;
		Element persNameElement = null;

		Element titleElement = null;
		Element biblScopeElement = null;

		Element noteElement = null;
		Element dateElement = null;

		listWitElement = new Element("listWit", xmlns);

		Attribute idlw = new Attribute("id", fragment.getXmlId() + ".WIT",
				Namespace.XML_NAMESPACE);
		listWitElement.setAttribute(idlw);

		// listWitElement.addNamespaceDeclaration(Namespace.getNamespace("id",
		// fragment.getXmlId() + ".WIT"));

		listWitAuthElement = new Element("listWit", xmlns);

		// listWitAuthElement.addNamespaceDeclaration(Namespace.getNamespace("id",
		// fragment.getXmlId() + ".WIT.MS"));

		Attribute idlwa = new Attribute("id", fragment.getXmlId() + ".WIT.MS",
				Namespace.XML_NAMESPACE);
		listWitAuthElement.setAttribute(idlwa);

		// manuscripts
		boolean selected = false;
		for (SourceInter sourceInter : fragment.getSortedSourceInter()) {

			// TODO selecionar as edicoes autorais ?
			if (fragInterSelectedSet.contains(sourceInter)) {

				witnessElement = new Element("witness", xmlns);
				// witnessElement.addNamespaceDeclaration(Namespace.getNamespace(
				// "id", sourceInter.getXmlId()));

				Attribute idw = new Attribute("id", sourceInter.getXmlId(),
						Namespace.XML_NAMESPACE);
				witnessElement.setAttribute(idw);

				refElement = new Element("ref", xmlns);
				refElement.setAttribute("target", "#"
						+ sourceInter.getSource().getXmlId());
				witnessElement.addContent(refElement);

				listWitAuthElement.addContent(witnessElement);
				selected = true;
			}
		}

		if (selected)
			listWitElement.addContent(listWitAuthElement);

		// editorial witness

		listWitEdElement = new Element("listWit", xmlns);
		// listWitEdElement.addNamespaceDeclaration(Namespace.getNamespace("id",
		// fragment.getXmlId() + ".WIT.ED"));

		Attribute idlwe = new Attribute("id", fragment.getXmlId() + ".WIT.ED",
				Namespace.XML_NAMESPACE);
		listWitEdElement.setAttribute(idlwe);

		headListWitElement = new Element("head", xmlns);
		listWitEdElement.addContent(headListWitElement);

		listWitEdCritElement = new Element("listWit", xmlns);

		// listWitEdCritElement.addNamespaceDeclaration(Namespace.getNamespace(
		// "id", fragment.getXmlId() + ".WIT.ED.CRIT"));

		Attribute idlwec = new Attribute("id", fragment.getXmlId()
				+ ".WIT.ED.CRIT", Namespace.XML_NAMESPACE);
		listWitEdCritElement.setAttribute(idlwec);

		listWitEdElement.addContent(listWitEdCritElement);

		ExpertEditionInter expertEditionInter = null;

		for (FragInter fragInter : fragment.getFragmentInterSet()) {

			// TODO: confirm: type EDITORIAL && selected
			if (fragInter.getSourceType() == fragInter.getSourceType().EDITORIAL
					&& fragInterSelectedSet.contains(fragInter)) {

				expertEditionInter = (ExpertEditionInter) fragInter;

				System.out.println("TITLE " + fragInter.getTitle());

				witnessElement = new Element("witness", xmlns);
				// witnessElement.addNamespaceDeclaration(Namespace.getNamespace(
				// "id", expertEditionInter.getXmlId()));

				Attribute idwe = new Attribute("id",
						expertEditionInter.getXmlId(), Namespace.XML_NAMESPACE);
				witnessElement.setAttribute(idwe);

				refElement = new Element("ref", xmlns);
				refElement.setAttribute("target", "#"
						+ expertEditionInter.getEdition().getXmlId());

				witnessElement.addContent(refElement);

				biblElement = new Element("bibl", xmlns);
				witnessElement.addContent(biblElement);

				// TODO heteronimo update? ; corresp
				if (fragInter.getHeteronym() != null) {

					respStmtElement = new Element("respStmt", xmlns);
					biblElement.addContent(respStmtElement);

					respElement = new Element("resp", xmlns);
					respElement.addContent("heterónimo");
					respStmtElement.addContent(respElement);

					String name = fragInter.getHeteronym().getName();
					String corresp = "";

					persNameElement = new Element("persName", xmlns);
					persNameElement.addContent(name);

					if (name.compareTo("Bernardo Soares") == 0)
						corresp = "BS";
					else
						corresp = "VS";

					persNameElement.setAttribute("corresp", "#HT." + corresp);
					respStmtElement.addContent(persNameElement);
				}

				titleElement = new Element("title", xmlns);
				titleElement.setAttribute("level", "a");
				titleElement.addContent(fragInter.getTitle());
				biblElement.addContent(titleElement);

				biblScopeElement = new Element("biblScope", xmlns);
				biblScopeElement.setAttribute("unit", "number");
				biblScopeElement
						.addContent(expertEditionInter.getNumber() + "");
				biblElement.addContent(biblScopeElement);

				if (expertEditionInter.getVolume() != null) {
					biblScopeElement = new Element("biblScope", xmlns);
					biblScopeElement.setAttribute("unit", "vol");
					biblScopeElement.addContent(expertEditionInter.getVolume()
							+ "");
					biblElement.addContent(biblScopeElement);
				}

				biblScopeElement = new Element("biblScope", xmlns);
				biblScopeElement.setAttribute("from",
						expertEditionInter.getStartPage() + "");
				biblScopeElement.setAttribute("to",
						expertEditionInter.getEndPage() + "");

				// TODO confirm
				biblScopeElement.setAttribute("unit", "pp");
				biblElement.addContent(biblScopeElement);

				if (expertEditionInter.getNotes().compareTo("") != 0) {
					noteElement = new Element("note", xmlns);
					noteElement.addContent(expertEditionInter.getNotes());
					biblElement.addContent(noteElement);
				}

				String precision = "";
				if (expertEditionInter.getDate() != null) {
					dateElement = new Element("date", xmlns);
					dateElement.addContent(expertEditionInter.getDate()
							.toString());
					dateElement.setAttribute("date", expertEditionInter
							.getDate().toString());
					dateElement.setAttribute("precision", precision);
					biblElement.addContent(dateElement);
				}

				listWitEdCritElement.addContent(witnessElement);

			}

		}

		listWitElement.addContent(listWitEdElement);
		rootElement.addContent(listWitElement);
	}

	private void generateFacsimiles(Fragment fragment, Element fragElement) {
		// TODO Auto-generated method stub
		for (Source source : fragment.getSourcesSet())
			generateFacsimile(source.getFacsimile(), fragElement);

	}

	private void generateFacsimile(Facsimile facsimile, Element fragElement) {

		Element facElement = new Element("facsimile", xmlns);

		// facElement.addNamespaceDeclaration(Namespace.getNamespace("id",
		// facsimile.getXmlId()));

		Attribute idf = new Attribute("id", facsimile.getXmlId(),
				Namespace.XML_NAMESPACE);
		facElement.setAttribute(idf);

		Attribute corresp = new Attribute("corresp", "#"
				+ facsimile.getSource().getXmlId());
		facElement.setAttribute(corresp);

		for (Surface surface : facsimile.getSurfaces()) {
			Element surfaceElement = new Element("surface", xmlns);
			Element graphElement = new Element("graphic", xmlns);

			Attribute graphAtt = new Attribute("url", surface.getGraphic());
			graphElement.setAttribute(graphAtt);

			surfaceElement.addContent(graphElement);
			facElement.addContent(surfaceElement);
		}

		fragElement.addContent(facElement);
	}

	private void generateTranscription(Fragment fragment, Element parentElement) {

		Element textElement = new Element("text", xmlns);
		parentElement.addContent(textElement);

		writer = new TEITextPortionWriter(textElement);
		writer.visit((AppText) fragment.getTextPortion());
	}

	private void generateTranscription(Element parentElement,
			Fragment fragment, List<FragInter> fragInterSelectedSet) {

		Element textElement = new Element("text", xmlns);
		parentElement.addContent(textElement);

		writer = new TEITextPortionWriter(textElement, fragInterSelectedSet);
		writer.visit((AppText) fragment.getTextPortion());
	}

	// TODO: to remove
	public TEITextPortionWriter getWriter() {
		return writer;
	}

	public String updateTeiHeader(String xml) {
		String header = "";
		String result = "";
		Resource resource = new ClassPathResource("teiCorpusHeader.xml");

		try {
			InputStream resourceInputStream = resource.getInputStream();
			header = IOUtils.toString(resourceInputStream, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result = xml.subSequence(0, xml.indexOf("<teiHeader")) + header + "\n"
				+ xml.substring(xml.indexOf("<TEI"));

		return result;
	}

	public String getXMLResult() {
		XMLOutputter xml = new XMLOutputter();
		// we want to format the xml. This is used only for demonstration.
		// pretty formatting adds extra spaces and is generally not required.
		xml.setFormat(Format.getPrettyFormat());
		return updateTeiHeader(xml.outputString(jdomDoc));
		// return xml.outputString(jdomDoc);
	}

	public String getHTMLXMLResult() {
		XMLOutputter xml = new XMLOutputter();
		// we want to format the xml. This is used only for demonstration.
		// pretty formatting adds extra spaces and is generally not required.
		xml.setFormat(Format.getPrettyFormat());
		String output = xml.outputString(jdomDoc);

		output = updateTeiHeader(output);
		output = output.replace("<", "&lt;");
		output = output.replace(">", "&gt;");

		return output;
	}

	public String getBase64XMLResult() {
		XMLOutputter xml = new XMLOutputter();
		// we want to format the xml. This is used only for demonstration.
		// pretty formatting adds extra spaces and is generally not required.
		xml.setFormat(Format.getPrettyFormat());
		String output = xml.outputString(jdomDoc);
		output = updateTeiHeader(output);
		return Base64.encode(output.getBytes());
	}

}
