package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.security.LdoDSession;

public class LdoD extends LdoD_Base {

	public static LdoD getInstance() {
		return FenixFramework.getDomainRoot().getLdoD();
	}

	public LdoD() {
		FenixFramework.getDomainRoot().setLdoD(this);
	}

	public List<ExpertEdition> getSortedExpertEdition() {
		List<ExpertEdition> editions = new ArrayList<ExpertEdition>(
				getExpertEditionsSet());
		Collections.sort(editions);
		return editions;
	}

	public Edition getEdition(String acronym) {
		for (Edition edition : getExpertEditionsSet()) {
			if (edition.getAcronym().equals(acronym)) {
				return edition;
			}
		}

		for (Edition edition : getVirtualEditionsSet()) {
			if (edition.getAcronym().equals(acronym)) {
				return edition;
			}
		}

		return null;
	}

	public LdoDUser getUser(String username) {
		for (LdoDUser user : getUsersSet()) {
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

		for (VirtualEdition virtualEdition : getVirtualEditionsSet()) {
			if ((user != null)
					&& (virtualEdition.getSelectedBySet().contains(user))) {
				selectedVE.add(virtualEdition);
			} else if (virtualEdition.getParticipantSet().contains(user)) {
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

	public Tag getTag(String tagName) {
		for (VirtualEdition edition : getVirtualEditionsSet()) {
			for (VirtualEditionInter inter : edition
					.getVirtualEditionIntersSet()) {
				for (Annotation annotation : inter.getAnnotationSet()) {
					for (Tag tag : annotation.getTagSet()) {
						if (tag.getTag().equals(tagName)) {
							return tag;
						}
					}
				}
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEdition createVirtualEdition(LdoDUser user, String acronym,
			String title, String date, boolean pub) {
		return new VirtualEdition(this, user, acronym, title, date, pub);
	}
}
