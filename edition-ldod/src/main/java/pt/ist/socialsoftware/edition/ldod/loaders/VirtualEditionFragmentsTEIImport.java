package pt.ist.socialsoftware.edition.ldod.loaders;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameRound;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.InfoRange;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Tag;
import pt.ist.socialsoftware.edition.ldod.domain.Tweet;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.utils.RangeJson;

public class VirtualEditionFragmentsTEIImport {
	private static Logger logger = LoggerFactory.getLogger(VirtualEditionFragmentsTEIImport.class);

	LdoD ldoD = null;
	Namespace namespace = null;

	public String importFragmentFromTEI(InputStream inputStream) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		Document doc;
		try {
			doc = builder.build(inputStream);
		} catch (FileNotFoundException e) {
			throw new LdoDLoadException("Ficheiro não encontrado");
		} catch (JDOMException e) {
			throw new LdoDLoadException("Ficheiro com problemas de codificação TEI");
		} catch (IOException e) {
			throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato");
		}

		if (doc == null) {
			LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		return processImport(doc);
	}

	public void importFragmentFromTEI(String fragmentTEI) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		InputStream stream = new ByteArrayInputStream(fragmentTEI.getBytes());

		importFragmentFromTEI(stream);
	}

	@Atomic(mode = TxMode.WRITE)
	public String processImport(Document doc) {
		this.ldoD = LdoD.getInstance();
		this.namespace = doc.getRootElement().getNamespace();

		Fragment fragment = getFragment(doc);

		// inclui a criação de citações, o setTwitterCitation para cada tweet
		// e a criação dos info ranges
		// TODO: importFragmentCitations - done
		importFragmentCitations(doc, fragment);

		importWitnesses(doc, fragment);

		importTextClasses(doc, fragment);

		return fragment.getXmlId();
	}

	private void importWitnesses(Document doc, Fragment fragment) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:witness", Filters.element(), null,
				Namespace.getNamespace("def", this.namespace.getURI()));
		List<Element> wits = new ArrayList<>(xp.evaluate(doc));

		while (!wits.isEmpty()) {
			Element wit = wits.remove(0);
			if (fragment.getFragInterByXmlId(wit.getAttributeValue("source").substring(1)) == null) {
				wits.add(wit);
			} else {
				String interXmlId = wit.getAttributeValue("id", Namespace.XML_NAMESPACE);
				String editionAcronym = interXmlId.substring(interXmlId.lastIndexOf("VIRT.") + "VIRT.".length(),
						interXmlId.lastIndexOf('.'));
				VirtualEdition virtualEdition = this.ldoD.getVirtualEdition(editionAcronym);

				logger.debug("importWitnesses id: {}, source: {}", interXmlId, wit.getAttributeValue("source"));
				virtualEdition.createVirtualEditionInter(
						fragment.getFragInterByXmlId(wit.getAttributeValue("source").substring(1)),
						Integer.parseInt(wit.getChild("num", this.namespace).getAttributeValue("value")));
			}
		}
	}

	private void importTextClasses(Document doc, Fragment fragment) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:textClass", Filters.element(), null,
				Namespace.getNamespace("def", this.namespace.getURI()));

		for (Element textClass : xp.evaluate(doc)) {
			VirtualEditionInter inter = (VirtualEditionInter) fragment
					.getFragInterByXmlId(textClass.getAttributeValue("source").substring(1));

			for (Element catRef : textClass.getChildren("catRef", this.namespace)) {
				importTag(catRef, inter);
			}

			for (Element note : textClass.getChildren("note", this.namespace)) {
				importAnnotation(note, inter);
			}

			importClassificationGames(textClass, inter);
		}
	}

	// TODO - done
	private void importFragmentCitations(Document doc, Fragment fragment) {
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:citation", Filters.element(), null,
				Namespace.getNamespace("def", this.namespace.getURI()));
		for (Element citation : xp.evaluate(doc)) {
			if (citation.getAttributeValue("type").equals("twitter")) {
				String sourceLink = citation.getAttributeValue("sourceLink");
				String date = citation.getAttributeValue("date");

				Element fragTextElement = citation.getChild("fragText", this.namespace);
				String fragText = fragTextElement.getText(); // trim() ?

				Element tweetTextElement = citation.getChild("tweetText", this.namespace);
				String tweetText = tweetTextElement.getText(); // trim() ?

				long tweetID = Long.parseLong(citation.getAttributeValue("tweetId"));
				String location = citation.getAttributeValue("location");
				String country = citation.getAttributeValue("country");
				String username = citation.getAttributeValue("username");
				String userProfileURL = citation.getAttributeValue("userProfileURL");
				String userImageURL = citation.getAttributeValue("userImageURL");

				TwitterCitation twitterCitation = new TwitterCitation(fragment, sourceLink, date, fragText, tweetText,
						tweetID, location, country, username, userProfileURL, userImageURL);

				// TODO: ciclo for que percorre todos os tweets da nova tag criada na citation e
				// faz tweet.setTwitterCitation()
				setTwitterCitation(citation, twitterCitation);

				// TODO suggestion: fazer aqui o import dos info ranges visto que são elementos
				// criados dentro de cada citation element
				importInfoRanges(fragment, citation, twitterCitation);
			}

		}

	}

	private void importInfoRanges(Fragment fragment, Element citation, TwitterCitation twitterCitation) {
		for (Element infoRangeElement : citation.getChild("infoRangesList", this.namespace).getChildren()) {
			String start = infoRangeElement.getAttributeValue("start");
			int startOffset = Integer.parseInt(infoRangeElement.getAttributeValue("startOffset"));
			String end = infoRangeElement.getAttributeValue("end");
			int endOffset = Integer.parseInt(infoRangeElement.getAttributeValue("endOffset"));

			Element quoteElement = infoRangeElement.getChild("quote", this.namespace);
			String quote = quoteElement.getText(); // trim() ?

			Element textElement = infoRangeElement.getChild("text", this.namespace);
			String text = textElement.getText(); // trim() ?

			FragInter fragInter = fragment.getFragInterByXmlId(infoRangeElement.getAttributeValue("fragInterXmlId"));

			new InfoRange(twitterCitation, fragInter, start, startOffset, end, endOffset, quote, text);
		}
	}

	private void setTwitterCitation(Element citation, TwitterCitation twitterCitation) {
		for (Element tweetElement : citation.getChild("tweets", this.namespace).getChildren()) {
			Tweet tweet = LdoD.getInstance()
					.getTweetByTweetID(Long.parseLong(tweetElement.getAttributeValue("tweetId")));
			tweet.setCitation(twitterCitation);
		}
	}

	private void importTag(Element catRef, VirtualEditionInter inter) {
		String username = catRef.getAttributeValue("resp").substring(1);
		String tag = catRef.getAttributeValue("target").substring(1);

		inter.getVirtualEdition().getTaxonomy().createTag(inter, tag, null, this.ldoD.getUser(username));
	}

	// TODO: else if aware - done
	// novo import annotation
	private void importAnnotation(Element note, VirtualEditionInter inter) {
		String text = StringEscapeUtils.escapeHtml(note.getText().trim());
		Element quoteElement = note.getChild("quote", this.namespace);
		String from = quoteElement.getAttributeValue("from");
		String to = quoteElement.getAttributeValue("to");
		String fromOffset = quoteElement.getAttributeValue("fromOffset");
		String toOffset = quoteElement.getAttributeValue("toOffset");
		String quote = quoteElement.getText().trim();

		RangeJson range = new RangeJson();
		range.setStart(from);
		range.setStartOffset(Integer.parseInt(fromOffset));
		range.setEnd(to);
		range.setEndOffset(Integer.parseInt(toOffset));
		List<RangeJson> rangeList = new ArrayList<>();
		rangeList.add(range);

		// TODO
		// este if apenas funciona para o novo export
		// não é retrocompatível para xml que não tenha
		// o atributo "type"
		if (note.getAttribute("type") == null || note.getAttributeValue("type").equals("human")) {
			String username = note.getAttributeValue("resp").substring(1);
			List<String> tagList = new ArrayList<>();
			for (Element catRef : note.getChildren("catRef", this.namespace)) {
				String tag = catRef.getAttributeValue("target").substring(1);
				tagList.add(tag);
			}
			inter.createHumanAnnotation(quote, text, this.ldoD.getUser(username), rangeList, tagList);
		}

		else if (note.getAttributeValue("type").equals("aware")) {
			long tweetID = Long.parseLong(note.getAttributeValue("citationId"));
			Citation citation = inter.getFragment().getCitationById(tweetID);
			inter.createAwareAnnotation(quote, text, citation, rangeList);
		}
	}

	// original code
	// private void importAnnotation(Element note, VirtualEditionInter inter) {
	// String username = note.getAttributeValue("resp").substring(1);
	// String text = StringEscapeUtils.escapeHtml(note.getText().trim());
	// Element quoteElement = note.getChild("quote", this.namespace);
	// String from = quoteElement.getAttributeValue("from");
	// String to = quoteElement.getAttributeValue("to");
	// String fromOffset = quoteElement.getAttributeValue("fromOffset");
	// String toOffset = quoteElement.getAttributeValue("toOffset");
	// String quote = quoteElement.getText().trim();
	//
	// RangeJson range = new RangeJson();
	// range.setStart(from);
	// range.setStartOffset(Integer.parseInt(fromOffset));
	// range.setEnd(to);
	// range.setEndOffset(Integer.parseInt(toOffset));
	//
	// List<String> tagList = new ArrayList<>();
	// for (Element catRef : note.getChildren("catRef", this.namespace)) {
	// String tag = catRef.getAttributeValue("target").substring(1);
	// tagList.add(tag);
	// }
	// List<RangeJson> rangeList = new ArrayList<>();
	// rangeList.add(range);
	// inter.createAnnotation(quote, text, this.ldoD.getUser(username), rangeList,
	// tagList);
	// }

	private Fragment getFragment(Document doc) {
		LdoD ldoD = LdoD.getInstance();

		Namespace namespace = doc.getRootElement().getNamespace();
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:TEI", Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));
		String fragXmlId = xp.evaluate(doc).get(0).getAttributeValue("id", Namespace.XML_NAMESPACE);

		return ldoD.getFragmentByXmlId(fragXmlId);
	}

	private void importClassificationGames(Element textClass, VirtualEditionInter inter) {
		if (textClass.getChild("classificationGameList", this.namespace) == null) {
			return;
		}

		for (Element gameElement : textClass.getChild("classificationGameList", this.namespace).getChildren()) {
			ClassificationGame.ClassificationGameState state = ClassificationGame.ClassificationGameState
					.valueOf(gameElement.getAttributeValue("state"));
			String description = gameElement.getAttributeValue("description");
			DateTime dateTime = new DateTime(DateTime.parse(gameElement.getAttributeValue("dateTime")));
			boolean sync = Boolean.parseBoolean(gameElement.getAttributeValue("sync"));
			LdoDUser responsible = LdoD.getInstance().getUser(gameElement.getAttributeValue("responsible"));
			LdoDUser winner = LdoD.getInstance().getUser(gameElement.getAttributeValue("winningUser"));

			ClassificationGame game = new ClassificationGame(inter.getVirtualEdition(), description, dateTime, inter,
					responsible);

			game.setState(state);
			game.setSync(sync);

			if (winner != null) {
				Tag tag = inter.getTagSet().stream()
						.filter(t -> t.getCategory().getName().equals(gameElement.getAttributeValue("tag"))
								&& t.getContributor() == winner)
						.findFirst().get();
				game.setTag(tag);
			}
			importClassificationGameParticipants(gameElement, game);
			importClassificationGameRounds(gameElement, game);
		}

	}

	private void importClassificationGameRounds(Element gameElement, ClassificationGame game) {
		for (Element roundElement : gameElement.getChild("classificationGameRoundList", this.namespace).getChildren()) {
			String username = roundElement.getAttributeValue("username");

			int paragraphNumber = Integer.parseInt(roundElement.getAttributeValue("paragraphNumber"));
			int roundNumber = Integer.parseInt(roundElement.getAttributeValue("roundNumber"));
			String tag = roundElement.getAttributeValue("tag");
			double vote = Double.parseDouble(roundElement.getAttributeValue("vote"));
			DateTime dateTime = new DateTime(DateTime.parse(roundElement.getAttributeValue("dateTime")));

			ClassificationGameRound gameRound = new ClassificationGameRound();
			gameRound.setNumber(paragraphNumber);
			gameRound.setRound(roundNumber);
			gameRound.setTag(tag);
			gameRound.setVote(vote);
			gameRound.setTime(dateTime);

			// TODO FIXME
			LdoDUser user = LdoD.getInstance().getUser(username);
			ClassificationGameParticipant participant = game.getClassificationGameParticipantSet().stream()
					.filter(p -> p.getPlayer().getUser() == user).findFirst().get();
			gameRound.setClassificationGameParticipant(participant);
		}

	}

	private void importClassificationGameParticipants(Element element, ClassificationGame game) {
		for (Element participantElement : element.getChild("classificationGameParticipantList", this.namespace)
				.getChildren()) {
			String username = participantElement.getAttributeValue("username");
			boolean winner = Boolean.parseBoolean(participantElement.getAttributeValue("winner"));
			double score = Double.parseDouble(participantElement.getAttributeValue("score"));
			LdoDUser user = LdoD.getInstance().getUser(username);

			ClassificationGameParticipant participant = new ClassificationGameParticipant(game, user);
			participant.setWinner(winner);
			participant.setScore(score);
		}
	}
}
