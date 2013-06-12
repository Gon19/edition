package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.security.LdoDSession;

public class LdoD extends LdoD_Base {

	public static LdoD getInstance() {
		return FenixFramework.getRoot();
	}

	public LdoD() {
		super();
	}

	public ExpertEdition getExpertEdition(String acronym) {
		for (ExpertEdition edition : getExpertEditions()) {
			if (edition.getAcronym().equals(acronym)) {
				return edition;
			}
		}
		return null;
	}

	public LdoDUser getUser(String username) {
		for (LdoDUser user : getUsers()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}
		return null;
	}

	public List<VirtualEdition> getVirtualEditions4User(LdoDUser user,
			LdoDSession session) {
		List<VirtualEdition> manageVE = new ArrayList<VirtualEdition>();
		List<VirtualEdition> selectedVE = new ArrayList<VirtualEdition>();
		List<VirtualEdition> mineVE = new ArrayList<VirtualEdition>();
		List<VirtualEdition> publicVE = new ArrayList<VirtualEdition>();

		// synchronize session
		Set<VirtualEdition> sessionVE = new HashSet<VirtualEdition>(
				session.getSelectedVEs());
		for (VirtualEdition edition : sessionVE) {
			if ((user != null)
					&& !user.getSelectedVirtualEditionsSet().contains(edition)) {
				session.removeSelectedVE(edition);
			} else if ((user == null) && (!edition.getPub())) {
				session.removeSelectedVE(edition);
			}
		}

		if (user == null) {
			selectedVE.addAll(session.getSelectedVEs());
		}

		for (VirtualEdition virtualEdition : getVirtualEditions()) {
			if ((user != null)
					&& (virtualEdition.getSelectedBySet().contains(user))) {
				selectedVE.add(virtualEdition);
			} else if (virtualEdition.getParticipant().contains(user)) {
				mineVE.add(virtualEdition);
			} else if (virtualEdition.getPub()
					&& !selectedVE.contains(virtualEdition)) {
				publicVE.add(virtualEdition);
			}
		}

		manageVE.addAll(selectedVE);
		manageVE.addAll(mineVE);
		manageVE.addAll(publicVE);

		return manageVE;
	}
}
