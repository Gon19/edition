package pt.ist.socialsoftware.edition.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

public final class TypescriptSearchOption extends AuthoralSearchOption {
	public static final String TYPESCRIPT = "datil";

	public TypescriptSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD, @JsonProperty("date") DateSearchOption date) {
		super(hasLdoD, date);
	}

	@Override
	protected String getDocumentType() {
		return TYPESCRIPT;
	}
}