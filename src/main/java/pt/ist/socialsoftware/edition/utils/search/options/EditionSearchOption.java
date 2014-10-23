package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;

public final class EditionSearchOption extends SearchOption {

	private final boolean inclusion;
	private final String edition;
	private final HeteronymSearchOption heteronym;
	private final DateSearchOption date;

	@JsonCreator
	public EditionSearchOption(@JsonProperty("inclusion") String inclusion,
			@JsonProperty("edition") String edition,
			@JsonProperty("heteronym") HeteronymSearchOption heteronym,
			@JsonProperty("date") DateSearchOption date) {

		if(inclusion.equals("in"))
			this.inclusion = true;
		else
			// if(inclusion.equals("out"))
			this.inclusion = false;

		this.edition = edition;
		this.heteronym = heteronym;
		this.date = date;
	}

	@Override
	public String toString() {
		return "Edition " + inclusion + " " + edition;
	}

	@Override
	public boolean visit(ExpertEditionInter inter) {
		if (inclusion) {
			if (!(edition.equals(inter.getEdition().getAcronym()) || 
					edition.equals(ALL))){
				return false;
			}
			if(heteronym != null && !inter.accept(heteronym)){
				return false;
			}
			if(date != null && !inter.accept(date)) {
				return false;
			}
		}else if (edition.equals(inter.getEdition().getAcronym()) || edition.equals(ALL)) {
			return false;
		}
		return true;
	}
}
