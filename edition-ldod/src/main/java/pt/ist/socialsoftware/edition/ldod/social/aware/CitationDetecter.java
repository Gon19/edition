package pt.ist.socialsoftware.edition.ldod.social.aware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParserBase;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.InfoRange;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.search.IgnoreDiacriticsAnalyzer;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class CitationDetecter {

	private final Logger logger = LoggerFactory.getLogger(CitationDetecter.class);

	private final String ID = "id";
	private final String TEXT = "text";

	private final Path docDir;
	private final Analyzer analyzer;
	private final QueryParserBase queryParser;

	public CitationDetecter() throws IOException {
		String path = PropertiesManager.getProperties().getProperty("indexer.dir");
		this.docDir = Paths.get(path);
		this.analyzer = new IgnoreDiacriticsAnalyzer();
		this.queryParser = new QueryParser(this.TEXT, this.analyzer);
	}

	public void detect() throws IOException {
		this.logger.debug("STARTING CITATION DETECTER!!");
		// resets last twitter IDs
		resetLastTwitterIds();
		citationDetection();
		this.logger.debug("FINISHED DETECTING CITATIONS!!!");

		// identify ranges
		this.logger.debug("STARTED IDENTIFYING RANGES!!!");
		createInfoRanges();
		this.logger.debug("FINISHED IDENTIFYING RANGES!!!");

		printNumberOfCitationsWithIndoRanges();

		this.logger.debug("STARTED REMOVING TWEETS WITHOUT CITATIONS!!!");
		removeTweetsWithoutCitations();
		this.logger.debug("FINISHED REMOVING TWEETS WITHOUT CITATIONS!!!");
	}

	@Atomic(mode = TxMode.WRITE)
	private void removeTweetsWithoutCitations() {
		LdoD.getInstance().removeTweetsWithoutCitations();
	}

	@Atomic
	private void printNumberOfCitationsWithIndoRanges() {
		this.logger.debug(
				"Number of Citations with Info Ranges: " + LdoD.getInstance().getNumberOfCitationsWithInfoRanges());
	}

	@Atomic(mode = TxMode.WRITE)
	private void resetLastTwitterIds() {
		LdoD.getInstance().getLastTwitterID().resetTwitterIDS();
	}

	private void citationDetection() throws IOException, FileNotFoundException {
		File folder = new File(PropertiesManager.getProperties().getProperty("social.aware.dir"));
		// get just files, not directories
		File[] files = folder.listFiles((FileFilter) FileFileFilter.FILE);
		Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);

		for (File fileEntry : files) {
			fileCitationDetection(fileEntry);

		}
		printLastTwitterIds();

	}

	private void fileCitationDetection(File fileEntry) throws FileNotFoundException, IOException {
		this.logger.debug("JSON file name: " + fileEntry.getName());

		try {
			JSONObject obj = new JSONObject();
			String line = null;

			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));

			// criar um tempMaxID que guarda o valor de
			// LdoD.getInstance().getLastTwitterID()
			// pq é preciso darmos set na base de dados do valor antes do while, pq vem logo
			// na primeira linha
			long tempMaxID = getLastTwitterId(fileEntry);

			int lineNum = 0;
			while ((line = bufferedReader.readLine()) != null) {
				// logger.debug(line);

				obj = (JSONObject) new JSONParser().parse(line);

				if (lineNum == 0) {
					updateLastTwitterId(fileEntry, obj);
				}
				lineNum++;

				if (obj.containsKey("isRetweet") && (boolean) obj.get("isRetweet")) {
					continue;
				}

				if ((long) obj.get("tweetID") > tempMaxID) {
					String tweetTextWithoutHttp = removeHttpFromTweetText(obj);

					if (!tweetTextWithoutHttp.equals("")) {
						searchQueryParserJSON(tweetTextWithoutHttp, obj);
						// searchQueryParser(absoluteSearch(tweetTextWithoutHttp)); //demasiado rígida,
						// nao funciona no nosso caso
					}

				} else {
					break;
				}
			}
			bufferedReader.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}

	public void searchQueryParserJSON(String query, JSONObject obj)
			throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
		Query parsedQuery = this.queryParser.parse(QueryParser.escape(query)); // escape foi a solução porque ele
																				// stressava

		try {
			FenixFramework.getTransactionManager().begin();
		} catch (NotSupportedException | SystemException e1) {
			throw new LdoDException("Fail a transaction begin");
		}

		searchIndexAndDisplayResultsJSON(parsedQuery, obj);

		try {
			FenixFramework.getTransactionManager().commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException e) {
			this.logger.debug("Miss the creation of a citation due to the info it contains");

		}

	}

	// @Atomic(mode = TxMode.WRITE)
	public void searchIndexAndDisplayResultsJSON(Query query, JSONObject obj) {
		try {
			int hitsPerPage = 5;
			Directory directory = new NIOFSDirectory(this.docDir);
			IndexReader idxReader = DirectoryReader.open(directory);
			IndexSearcher idxSearcher = new IndexSearcher(idxReader);

			ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
			if (hits.length > 0) {
				int docId = hits[0].doc;
				float score = hits[0].score;
				if (score > 30) {
					Document d = idxSearcher.doc(docId);

					// necessary because the same tweet was collected using different keywords in
					// FetchCitationsFromTwitter class
					// check if twitter ID already exists in the list of Citations
					// if it does idExists=true, therefore we don't create a citation for it!
					Set<TwitterCitation> allTwitterCitations = LdoD.getInstance().getCitationSet().stream()
							.filter(TwitterCitation.class::isInstance).map(TwitterCitation.class::cast)
							.collect(Collectors.toSet());
					boolean twitterIDExists = false;
					for (TwitterCitation tc : allTwitterCitations) {
						if (tc.getTweetID() == (long) obj.get("tweetID")) {
							twitterIDExists = true;
							break;
						}
					}
					if (!twitterIDExists) {
						// obtain Fragment
						// using external id
						FragInter inter = FenixFramework.getDomainObject(d.get(this.ID));
						Fragment fragment = inter.getFragment();

						String tweetTextWithoutHttp = removeHttpFromTweetText(obj);

						// this.logger.debug("GOING TO CREATE A TWITTER CITATION!!");

						new TwitterCitation(fragment, (String) obj.get("tweetURL"), (String) obj.get("date"),
								d.get(this.TEXT), tweetTextWithoutHttp, (long) obj.get("tweetID"),
								(String) obj.get("location"), (String) obj.get("country"), (String) obj.get("username"),
								(String) obj.get("profURL"), (String) obj.get("profImg"));
						// this.logger.debug("CREATED A TWITTER CITATION!!!");
					}

				}
			}
			idxReader.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Atomic(mode = TxMode.WRITE)
	private void updateLastTwitterId(File fileEntry, JSONObject obj) {
		if ((long) obj.get("tweetID") > LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName())) {
			LdoD.getInstance().getLastTwitterID().updateLastTwitterID(fileEntry.getName(), (long) obj.get("tweetID"));
		}
	}

	private String removeHttpFromTweetText(JSONObject obj) {
		String tweetText = (String) obj.get("text");
		String tweetTextWithoutHttp = tweetText;

		// removing "http" from tweet text
		if (tweetText.contains("http")) {
			int httpIndex = tweetText.indexOf("http");
			tweetTextWithoutHttp = tweetText.substring(0, httpIndex);
		}
		return tweetTextWithoutHttp;
	}

	@Atomic
	private void printLastTwitterIds() {
		this.logger.debug("LdoD BookLastTwitterID:{}", LdoD.getInstance().getLastTwitterID().getBookLastTwitterID());
		this.logger.debug("LdoD BernardoLastTwitterID:{}",
				LdoD.getInstance().getLastTwitterID().getBernardoLastTwitterID());
		this.logger.debug("LdoD VicenteLastTwitterID:{}",
				LdoD.getInstance().getLastTwitterID().getVicenteLastTwitterID());
		this.logger.debug("LdoD PessoaLastTwitterID:{}",
				LdoD.getInstance().getLastTwitterID().getPessoaLastTwitterID());
	}

	@Atomic(mode = TxMode.WRITE)
	private void createInfoRanges() {
		for (Citation citation : LdoD.getInstance().getCitationSet()) {
			if (citation.getInfoRangeSet().isEmpty()) {
				Fragment citationFragment = citation.getFragment();
				Set<FragInter> inters = new HashSet<FragInter>(citationFragment.getFragmentInterSet());
				inters.removeAll(citationFragment.getVirtualEditionInters());

				for (FragInter inter : inters) {
					createInfoRange(inter, citation);
				}
			}
		}
	}

	private void createInfoRange(FragInter inter, Citation citation) {
		String htmlTransc = getHtmlTransc(inter);

		if (citation instanceof TwitterCitation) {
			List<String> result = patternFinding(htmlTransc, ((TwitterCitation) citation).getTweetText());

			String infoQuote = result.get(0);
			int htmlStart = Integer.parseInt(result.get(1));
			int htmlEnd = Integer.parseInt(result.get(2));
			int numOfPStart = Integer.parseInt(result.get(3));
			int numOfPEnd = Integer.parseInt(result.get(4));

			if (htmlStart != -1 && htmlEnd != -1 && infoQuote != ""
					&& !startBiggerThanEnd(htmlStart, htmlEnd, numOfPStart, numOfPEnd)) {

				String infoText = createInfoText(citation);

				this.logger.debug("GOING TO CREATE AN INFO RANGE");

				new InfoRange(citation, inter, "/div[1]/div[1]/p[" + numOfPStart + "]", htmlStart,
						"/div[1]/div[1]/p[" + numOfPEnd + "]", htmlEnd, infoQuote, infoText);
			}
		}
	}

	private String createInfoText(Citation citation) {
		// concatenation of meta information
		String sourceLink = citation.getSourceLink();
		String date = citation.getDate();
		String tweetID = Long.toString(citation.getId());
		String location = ((TwitterCitation) citation).getLocation();
		String country = ((TwitterCitation) citation).getCountry();
		String username = ((TwitterCitation) citation).getUsername();
		String userProfileURL = ((TwitterCitation) citation).getUserProfileURL();

		String infoText;

		// complete info text
		// infoText = "SOURCE LINK: " + sourceLink + "\n" + "DATE: " + date + "\n" +
		// "TWEET ID: " + tweetID + "\n"
		// + "COUNTRY: " + country + "\n" + "LOCATION: " + location + "\n" + "USERNAME:"
		// + username + "\n"
		// + "USER PROFILE: " + userProfileURL;

		// short info text
		infoText = "LINK: " + sourceLink + "\n" + "DATA: " + date + "\n";
		if (!country.equals("unknown")) {
			infoText += "PAÍS: " + country;
		}

		return infoText;
	}

	private String getHtmlTransc(FragInter inter) {
		PlainHtmlWriter4OneInter htmlWriter = new PlainHtmlWriter4OneInter(inter);
		htmlWriter.write(false);
		String htmlTransc = htmlWriter.getTranscription();
		return htmlTransc;
	}

	public boolean startBiggerThanEnd(int htmlStart, int htmlEnd, int numOfPStart, int numOfPEnd) {
		return htmlStart > htmlEnd && numOfPStart == numOfPEnd;
	}

	public String convertFirstCharToUpperCaseInSentence(String str) {
		// Create a char array of given String
		char ch[] = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {

			// If first character of a word is found
			if (i == 0 && ch[i] != ' ' || ch[i] != ' ' && ch[i - 1] == ' ') {

				// If it is in lower-case
				if (ch[i] >= 'a' && ch[i] <= 'z') {

					// Convert into Upper-case
					ch[i] = (char) (ch[i] - 'a' + 'A');
				}
			}

			// If apart from first character
			// Any one is in Upper-case
			else if (ch[i] >= 'A' && ch[i] <= 'Z') {
				// Convert into Lower-Case
				ch[i] = (char) (ch[i] + 'a' - 'A');
			}
		}

		// Convert the char array to equivalent String
		String st = new String(ch);
		return st;
	}

	public String cleanTweetText(String originalTweetText) {
		String result = originalTweetText.toLowerCase().replaceAll("[\"*«»“”()';]", "");
		result = result.replace("\\n", " ");
		result = result.replace("...", " ");

		// não posso por em variáveis pq o tamanho do texto
		// vai sendo encurtado no ciclo for
		// int resultLen = result.length();
		// int lastCharPos = resultLen - 1;

		// apagar apenas os hífenes e pontos que não fizerem parte de palavras
		String charSet = "-.,;?!q"; // 'q' porque muitas pessoas escrevem 'q' em vez de "que"
		for (int i = 0; i < result.length(); i++) {
			char c = result.charAt(i);
			if (charSet.indexOf(c) != -1) {
				result = cleanCharFromString(c, result, i, result.length() - 1);
			}
		}
		return result;
	}

	public String capitalizeFirstWord(String sentence) {
		return sentence.substring(0, 1).toUpperCase() + sentence.substring(1);
	}

	public List<String> patternFinding(String text, String tweet) {
//		logger.debug("------------------------------ PATTERN FINDING ALGORITHM-------------------------");
//		logger.debug("ORIGINAL TWEET TEXT: " + tweet);

		// é chato pôr o text é lowercase pq estamos a adulterar a informação original,
		// experimentar outra distance em vez do Jaro
		text = text.toLowerCase();
		// o "clean" já mete o tweet em lowerCase
		tweet = cleanTweetText(tweet);

//		this.logger.debug("CLEANED TWEET TEXT: " + tweet);

		// variables updated over iteration
		int start = -1; // -1 means that the pattern was not found, either for start and end
		int end = -1;
		int offset = 0;
		String patternFound = "";

		// parameters that can be adjusted
		int window = 10;
		double jaroThreshold = 0.9;
		int startCorrectParam = 3; // parâmetro utilizado na correção da start position

		// algorithm
		int count = 0; // aux counter to check if we reach the minimum value set by "window" variable
		outerloop: for (String initialWord : tweet.split("\\s+")) {
			for (String word : initialWord.split(",")) {
				offset = Math.max(start, end);
				if (offset == -1) {
					offset = 0;
				}

				List<String> info = maxJaroValue(text.substring(offset), word);
				String wordFound = info.get(0);
				double jaroValue = Double.parseDouble(info.get(1));

				// logger.debug("tweet word: " + word);
				// logger.debug("text word: " + wordFound);

				// a palavra tem de existir no texto e estar à frente do offset!
				// primeira palavra encontrada
				if (jaroValue > jaroThreshold && text.indexOf(wordFound, offset) != -1) {
					// logger.debug(" text contains this word");
					// logger.debug(Double.toString(jaroValue));

					// é só updated uma vez e é quando o início começa bem
					if (count == 0) {
						// é só updated uma vez e é quando o início começa bem
						start = text.indexOf(wordFound, offset);
						patternFound += word + " ";
						count = 1;
					}
					// restantes palavras encontradas
					// vai sendo constantemente updated enquanto corre bem
					else {
						// entra neste if para dar o update exato do start
						// pq a primeira palavra do padrão pode ocorrer várias vezes no texto antes de
						// ocorrer no padrão
						// o mais correto seria fazer quando count==1 (pq é quando já recolhemos pelo
						// menos uma palavra)
						// mas como o offset só é updated no início de cada ciclo temos de esperar uma
						// iteração
						if (count == startCorrectParam) {
							// logger.debug(" padrão até agora: " + patternFound);

							// este update ao start dá bug quando as palavras iniciais do padrão aparecem
							// antes do padrão
							String[] splits = patternFound.split(" ");
							String firstWordOfPatternFound = splits[0];
							String lastWordOfPatternFound = splits[splits.length - 1];

							start = text.lastIndexOf(firstWordOfPatternFound, offset - lastWordOfPatternFound.length());
						}
						end = text.indexOf(wordFound, offset) + wordFound.length();
						// logger.debug(" a palavra encontrada no Texto foi: " + wordFound);
						patternFound += word + " ";
						count++;
					}
				}
				// caso em q a palavra não existe no texto
				else {
					// logger.debug(" text DOES NOT contains this word");
					// logger.debug(Double.toString(jaroValue));
					if (count < window) { // significa que não fizémos o número mínimo de palavras seguidas, logo é dar
											// reset!!
						count = 0;
						start = -1;
						end = -1;
						patternFound = "";
						// logger.debug(" dei reset ao count, next word!");
					} else {
						// logger.debug(" vou dar break pq já garanti a window");
						break outerloop;
					}
				}
				// logger.debug(" count: " + count);
			}
		}

		// this is the case where the pattern exists (start !=-1) but the window was not
		// fulfilled
		if (count < window && start != -1) {
			start = -1;
			end = -1;
			patternFound = "";
		}

		int numOfPStart = -1;
		int numOfPEnd = -1;
		int htmlStart = -1;
		int htmlEnd = -1;

		int earlyStart = -1;
		int laterEnd = -1;

		String prefix = "";
		String suffix = "";

		if (start != -1 && end != -1) {
			// HTML treatment
			numOfPStart = 1 + countOccurencesOfSubstring(text, "<p", start); // +1 porque o getTranscription não traz o
																				// primeiro <p
			numOfPEnd = 1 + countOccurencesOfSubstring(text, "<p", end); // +1 porque o getTranscription não traz o
																			// primeiro <p

			htmlStart = start - text.lastIndexOf("\">", start) - 2; // -2, para compensar
			htmlEnd = end - text.lastIndexOf("\">", end) - 2; // -2, para compensar

			// logger.debug("htmlStart: " + htmlStart);
			// logger.debug("htmlEnd: " + htmlEnd);

			// logger.debug("index of >: " + text.lastIndexOf("\">", start));
			// logger.debug("index of <: " + text.indexOf("<", end));
			//
			// logger.debug("\n");
			//
			// logger.debug("start: " + start);
			// logger.debug("end: " + end);
			//
			// logger.debug("\n");
			//
			// logger.debug("last dot: " + text.lastIndexOf(".", start));
			// logger.debug("next dot: " + text.indexOf(".", end));
			//
			// logger.debug("\n");

			// dots solution
			earlyStart = htmlStart;
			laterEnd = htmlEnd;

			if (text.lastIndexOf(".", start) > text.lastIndexOf("\">", start)) {
				// para cobrir a frase até ao ponto final anterior é fazer
				earlyStart = text.lastIndexOf(".", start) - text.lastIndexOf("\">", start) - 2;
				// logger.debug("earlyStart: " + earlyStart);
				// prefix = text.substring(text.lastIndexOf(".", start) + 1, start);
				// logger.debug("prefix: " + prefix);
			}

			if (text.indexOf(".", end) < text.indexOf("<", end)) {
				// para cobrir a frase até ao ponto final seguinte é fazer
				laterEnd = text.indexOf(".", end) - text.lastIndexOf("\">", start) - 2;
				// logger.debug("laterEnd: " + laterEnd);
				// suffix = text.substring(end, text.indexOf(".", end));
				// logger.debug("suffix: " + suffix);
			}

		}

		// logger.debug("earlyStart: " + earlyStart);
		// logger.debug("laterEnd: " + laterEnd);
		//
		// logger.debug("original pattern found: " + patternFound);

		// patternFound = prefix + patternFound + suffix;
		//
		// logger.debug("modified pattern found: " + patternFound);

		patternFound = patternFound.trim();

		// converts the first letter of each sentence to upper case
		String upperPattern = "";
		if (patternFound != "") {
			String[] patternSplit = patternFound.split("\\.\\s+");
			// logger.debug("length do split: " + patternSplit.length);
			for (String s : patternSplit) {
				// logger.debug("string s: " + s);
				upperPattern += this.capitalizeFirstWord(s) + ". ";
			}

		}

		if (upperPattern != "") {
			upperPattern = upperPattern.substring(0, upperPattern.length() - 2);
		}
		// logger.debug("UPPER PATTERN: " + upperPattern);

		List<String> result = new ArrayList<String>();
		result.add(upperPattern);
		result.add(String.valueOf(earlyStart));
		result.add(String.valueOf(laterEnd));
		result.add(String.valueOf(numOfPStart));
		result.add(String.valueOf(numOfPEnd));

		return result;
	}

	public int lastIndexOfCapitalLetter(String str, int auxPos) {
		for (int i = auxPos; i >= 0; i--) {
			if (Character.isUpperCase(str.charAt(i))) {
				return i;
			}
		}
		return -1;
	}

	@Atomic
	private long getLastTwitterId(File fileEntry) {
		long tempMaxID = LdoD.getInstance().getLastTwitterID().getLastTwitterID(fileEntry.getName());
		return tempMaxID;
	}

	// returns max jaro value between a word in the pattern and every word in the
	// text
	public List<String> maxJaroValue(String text, String wordToFind) {
		JaroWinklerDistance jaro = new JaroWinklerDistance();
		double maxJaroValue = 0.0;
		String wordFound = "";
		for (String textWord : text.split("\\s+")) {
			if (textWord.contains("</p>")) {
				textWord = textWord.substring(0, textWord.indexOf("</p>"));
			}
			if (jaro.apply(textWord, wordToFind) > maxJaroValue) {
				maxJaroValue = jaro.apply(textWord, wordToFind);
				wordFound = textWord;
			}
		}

		List<String> info = new ArrayList<String>();
		info.add(wordFound);
		info.add(String.valueOf(maxJaroValue));
		return info;
	}

	public String cleanCharFromString(char charToClean, String s, int position, int lastCharPos) {
		// limpar hífenes que tenham espaços em branco à esquerda ou à direita
		if (charToClean == '-') {
			s = replaceChar(s, position, lastCharPos);
		}
		// limpar pontos que tenham espaços em branco à esquerda e à direita
		else if (charToClean == '.') {
			s = replaceDotChar(s, position, lastCharPos);
		}
		// limpar pontos que tenham ponto é vírgula em branco à esquerda e à direita
		else if (charToClean == ';') {
			s = replaceChar(s, position, lastCharPos);
		}
		// limpar vírgulas que tenham espaços em branco à esquerda e à direita
		else if (charToClean == ',') {
			s = replaceChar(s, position, lastCharPos);
		}
		// limpar pontos de interrogação que tenham espaços em branco à esquerda e à
		// direita
		else if (charToClean == '?') {
			s = replaceChar(s, position, lastCharPos);
		}
		// limpar pontos de exclamação que tenham espaços em branco à esquerda e à
		// direita
		else if (charToClean == '!') {
			s = replaceChar(s, position, lastCharPos);
		}
		// substituir as ocorrências da letra 'q' com espaços à esquerda e à direita por
		// "que"
		else if (charToClean == 'q') {
			if (position != 0) {
				if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
					s = s.substring(0, position) + "que" + s.substring(position + 1);
				}
			}
		}
		return s;
	}

	private String replaceChar(String s, int position, int lastCharPos) {
		if (position != 0) {
			if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
				s = s.substring(0, position) + ' ' + s.substring(position + 1);
			}
		}
		return s;
	}

	// caso específico do ponto final
	private String replaceDotChar(String s, int position, int lastCharPos) {
		if (position != 0) {
			if (s.charAt(position - 1) == ' ' && position != lastCharPos && s.charAt(position + 1) == ' ') {
				s = s.substring(0, position) + ' ' + s.substring(position + 1);
			}
		}
		// caso em q o . vem mesmo no início da frase
		else if (position == 0) {
//			this.logger.debug("ENTREI NO IF EM QUE O . VEM NA POSITION 0");
			if (s.charAt(position + 1) == ' ') {
				s = s.substring(position + 1);
			}
		}
		return s;
	}

	public void searchQueryParser(String query)
			throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
		Query parsedQuery = this.queryParser.parse(QueryParser.escape(query)); // escape foi a solução porque ele
																				// stressava
																				// com o EOF
		searchIndexAndDisplayResults(parsedQuery);
	}

	public void searchIndexAndDisplayResults(Query query) {
		try {
			int hitsPerPage = 5;
			Directory directory = new NIOFSDirectory(this.docDir);
			IndexReader idxReader = DirectoryReader.open(directory);
			IndexSearcher idxSearcher = new IndexSearcher(idxReader);

			ScoreDoc[] hits = idxSearcher.search(query, hitsPerPage).scoreDocs;
			if (hits.length > 0) {
				int docId = hits[0].doc;
				float score = hits[0].score;
				if (score > 30) {
					Document d = idxSearcher.doc(docId);

				}
			}
			directory.close();
			idxReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Fuzzy Query - multiple terms! pesquisa tem de ser exata
	public void searchSpanQuery(String words) {
		String[] split = words.split("\\s+");
		int len = split.length;
		SpanQuery[] clauses = new SpanQuery[len];
		for (int i = 0; i < len; i++) {
			clauses[i] = new SpanMultiTermQueryWrapper(new FuzzyQuery(new Term(this.TEXT, split[i])));
		}
		SpanNearQuery query = new SpanNearQuery(clauses, 0, true);

		searchIndexAndDisplayResults(query);
	}

	// Fuzzy Search - Pesquisa tem de ser exata
	// Search for fragments with a set of words similar to input
	// Fuzzy set for a minimum edition edition of 1
	public void fuzzySearch(String words)
			throws ParseException, org.apache.lucene.queryparser.classic.ParseException, IOException {
		String[] split = words.split("\\s+");
		double fuzzy = 1; // default = 0.5
		String query = "" + split[0] + "~" + fuzzy;
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i] + "~" + fuzzy;
		}
		searchQueryParser(query);
	}

	// Search for fragments with a set of equal to inputs
	private String absoluteSearch(String words) {
		String[] split = words.split("\\s+");
		String query = "" + split[0];
		int len = split.length;

		for (int i = 1; i < len; i++) {
			query += " AND " + split[i];
		}
		return query;
	}

	private void searchSingleTerm(String field, String termText) {
		Term term = new Term(field, termText);
		TermQuery termQuery = new TermQuery(term);

		searchIndexAndDisplayResults(termQuery);
	}

	public int countOccurencesOfSubstring(final String string, final String substring, final int subsStartPos) {
		int count = 0;
		int idx = 0;

		while ((idx = string.indexOf(substring, idx)) != -1 && idx < subsStartPos) {
			idx++;
			count++;
		}

		return count;
	}
}
