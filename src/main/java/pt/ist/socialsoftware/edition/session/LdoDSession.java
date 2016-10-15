package pt.ist.socialsoftware.edition.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.recommendation.ReadingRecommendation;

public class LdoDSession implements Serializable {
	private static final long serialVersionUID = 3742738985902099143L;

	private final List<String> selectedVEAcr = new ArrayList<String>();

	private final ReadingRecommendation recommendation = new ReadingRecommendation();

	@Atomic(mode = TxMode.WRITE)
	public void updateSession(LdoDUser user) {
		user.getSelectedVirtualEditionsSet().stream().forEach(ve -> addSelectedVE(ve));

		user.getSelectedVirtualEditionsSet().addAll(materializeVirtualEditions());
	}

	public boolean hasSelectedVE(String acronym) {
		return selectedVEAcr.contains(acronym);
	}

	public void removeSelectedVE(String acronym) {
		selectedVEAcr.remove(acronym);
	}

	public void addSelectedVE(VirtualEdition virtualEdition) {
		String toAddAcr = new String(virtualEdition.getAcronym());

		// do not add the archive virtual edition because it is already
		// hardcoded in the menu
		if (!selectedVEAcr.contains(toAddAcr) && !toAddAcr.equals(Edition.ARCHIVE_EDITION_ACRONYM)) {
			selectedVEAcr.add(toAddAcr);
			Collections.sort(selectedVEAcr);
		}
	}

	public List<VirtualEdition> materializeVirtualEditions() {
		LdoD ldod = LdoD.getInstance();

		return selectedVEAcr.stream().map(acr -> ldod.getEdition(acr)).filter(e -> e != null)
				.map(VirtualEdition.class::cast).collect(Collectors.toList());

	}

	public List<String> getSelectedVEAcr() {
		return selectedVEAcr;
	}

	@Atomic(mode = TxMode.WRITE)
	public void toggleSelectedVirtualEdition(LdoDUser user, VirtualEdition virtualEdition) {
		if (hasSelectedVE(virtualEdition.getAcronym())) {
			removeSelectedVE(virtualEdition.getAcronym());
			if (user != null)
				user.removeSelectedVirtualEditions(virtualEdition);
		} else {
			addSelectedVE(virtualEdition);
			if (user != null)
				user.addSelectedVirtualEditions(virtualEdition);
		}
	}

	public void synchronizeSession(LdoDUser user) {
		List<VirtualEdition> selected = materializeVirtualEditions();

		clearSession();
		if (user != null) {
			user.getSelectedVirtualEditionsSet().stream().forEach(ve -> addSelectedVE(ve));
		} else {
			selected.stream().filter(ve -> ve.getPub()).forEach(ve -> addSelectedVE(ve));
		}

	}

	private void clearSession() {
		selectedVEAcr.clear();
	}

	public ReadingRecommendation getRecommendation() {
		return recommendation;
	}

}
