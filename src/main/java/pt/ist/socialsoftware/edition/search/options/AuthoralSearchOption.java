package pt.ist.socialsoftware.edition.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public abstract class AuthoralSearchOption extends SearchOption {

	private final String hasLdoD;
	private final DateSearchOption date;

	public AuthoralSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
			@JsonProperty("date") DateSearchOption date) {
		this.hasLdoD = hasLdoD;
		this.date = date;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(SourceInter.class::isInstance).map(SourceInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	@Override
	public boolean visit(SourceInter inter) {
		return verifiesSearchOption(inter);
	}

	private boolean verifiesSearchOption(SourceInter inter) {
		if (inter.getSource().getType().equals(SourceType.MANUSCRIPT)) {
			ManuscriptSource source = (ManuscriptSource) inter.getSource();
			if (source.getNotes().toLowerCase().contains(getDocumentType()) && inter.accept(date)) {
				if (hasLdoD.equals(SearchOption.ALL) || (hasLdoD.equals("true") && source.getHasLdoDLabel()))
					return true;
				else if (hasLdoD.equals("false") && !source.getHasLdoDLabel())
					return true;
			}
		}
		return false;
	}

	protected abstract String getDocumentType();

	@Override
	public String toString() {
		return getDocumentType() + ": hasLdoDMark:" + hasLdoD + "\n" + date;
	}

	public boolean hasDate() {
		return date == null ? false : date.hasDate();
	}

	public boolean hasLdoDMark() {
		return !hasLdoD.equals(ALL);
	}
}
