package pt.ist.socialsoftware.edition.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;

public final class EditionSearchOption extends SearchOption {

	private final boolean inclusion;
	private final String edition;
	private final HeteronymSearchOption heteronym;
	private final DateSearchOption date;

	@JsonCreator
	public EditionSearchOption(@JsonProperty("inclusion") String inclusion, @JsonProperty("edition") String edition,
			@JsonProperty("heteronym") HeteronymSearchOption heteronym, @JsonProperty("date") DateSearchOption date) {

		if (inclusion.equals("in"))
			this.inclusion = true;
		else
			this.inclusion = false;

		this.edition = edition;
		this.heteronym = heteronym;
		this.date = date;
	}

	@Override
	public String toString() {
		return "edition:" + edition + "\ninclusion:" + inclusion + "\n" + heteronym + "\n" + date;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(ExpertEditionInter.class::isInstance).map(ExpertEditionInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	@Override
	public boolean visit(ExpertEditionInter inter) {
		return verifiesSearchOption(inter);
	}

	private boolean verifiesSearchOption(ExpertEditionInter inter) {
		if (inclusion) {
			if (!(edition.equals(inter.getEdition().getAcronym()) || edition.equals(ALL))) {
				return false;
			}
			if (heteronym != null && !inter.accept(heteronym)) {
				return false;
			}
			if (date != null && !inter.accept(date)) {
				return false;
			}
		} else if ((edition.equals(inter.getEdition().getAcronym()) || edition.equals(ALL)) && heteronym != null
				&& inter.accept(heteronym) && date != null && inter.accept(date))
			return false;

		return true;
	}

	public boolean hasDate() {
		return date == null ? false : date.hasDate();
	}

	public boolean hasHeteronym() {
		return heteronym == null ? false : heteronym.hasHeteronym();
	}

	public String getEdition() {
		return edition;
	}
}
