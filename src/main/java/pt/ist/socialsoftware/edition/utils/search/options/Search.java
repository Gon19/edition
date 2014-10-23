package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.utils.search.options.SearchOption.Mode;

// @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class Search {
	private static final String OPTIONS = "options";
	private static final String MODE = "mode";

	private final Mode mode;
	private final SearchOption[] searchOptions;

	public Search(@JsonProperty(Search.MODE) String mode, @JsonProperty(Search.OPTIONS) SearchOption[] searchOptions) {
		this.mode = mode.equals(Mode.AND.getMode()) ? Mode.AND : Mode.OR;
		this.searchOptions = searchOptions;
	}

	public Mode getMode() {
		return mode;
	}

	public SearchOption[] getSearchOptions() {
		return searchOptions;
	}
}
