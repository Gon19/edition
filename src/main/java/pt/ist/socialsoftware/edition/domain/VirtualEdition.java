package pt.ist.socialsoftware.edition.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

public class VirtualEdition extends VirtualEdition_Base {

	public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym,
			String title, LocalDate date, Boolean pub, Edition usedEdition) {
		setLdoD4Virtual(ldod);
		addParticipant(participant);
		setAcronym(acronym);
		setTitle(title);
		setDate(date);
		setPub(pub);
		setNextInterNumber(1);

		if (usedEdition != null) {
			for (FragInter inter : usedEdition.getIntersSet()) {
				createVirtualEditionInter(inter);
			}
		}
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		// delete directory and all its files if it exists
		String path = PropertiesManager.getProperties().getProperty(
				"corpus.dir");
		File directory = new File(path + getExternalId());
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				// Unable to delete directory
				e.printStackTrace();
			}
		}

		setLdoD4Virtual(null);

		for (LdoDUser user : getParticipantSet()) {
			removeParticipant(user);
		}

		for (LdoDUser user : getSelectedBySet()) {
			removeSelectedBy(user);
		}

		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {
			inter.remove();
		}

		super.remove();
	}

	@Override
	public void setPub(Boolean pub) {
		if (!pub) {
			Set<LdoDUser> participants = getParticipantSet();
			for (LdoDUser user : getSelectedBySet()) {
				if (!participants.contains(user)) {
					this.removeSelectedBy(user);
				}
			}
		}
		super.setPub(pub);
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.VIRTUAL;
	}

	public List<VirtualEditionInter> getSortedInter4Frag(Fragment fragment) {
		List<VirtualEditionInter> interps = new ArrayList<VirtualEditionInter>();

		for (FragInter inter : fragment.getFragmentInterSet()) {
			if ((inter.getSourceType() == EditionType.VIRTUAL)
					&& ((VirtualEditionInter) inter).getVirtualEdition() == this) {
				interps.add((VirtualEditionInter) inter);
			}
		}

		Collections.sort(interps);

		return interps;
	}

	// determines if the fragment can have more interpretations for this virtual
	// edition, deals with the the case of a fragment having two interpretations
	// for the same expert edition
	public Boolean canAddFragInter(FragInter addInter) {
		Fragment fragment = addInter.getFragment();
		FragInter usedAddInter = addInter.getLastUsed();
		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {
			if (inter.getFragment() == fragment) {
				FragInter usedInter = inter.getLastUsed();
				if (usedAddInter == usedInter) {
					return false;
				}
				if ((usedInter instanceof SourceInter)
						|| (usedAddInter instanceof SourceInter)) {
					return false;
				}

				ExpertEdition addExpertEdition = ((ExpertEditionInter) usedAddInter)
						.getExpertEdition();
				ExpertEdition expertEdition = ((ExpertEditionInter) usedInter)
						.getExpertEdition();
				if (addExpertEdition != expertEdition) {
					return false;
				} else {
					int numberOfInter4Expert = fragment
							.getNumberOfInter4Edition(expertEdition);
					int numberOfInter4Virtual = fragment
							.getNumberOfInter4Edition(this);
					return numberOfInter4Expert > numberOfInter4Virtual;
				}
			}
		}
		return true;
	}

	public int generateNextInterNumber() {
		int nextInterNumber = getNextInterNumber();
		setNextInterNumber(getNextInterNumber() + 1);
		return nextInterNumber;
	}

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>(getVirtualEditionIntersSet());
	}

	@Override
	public String getReference() {
		return getAcronym();
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(String acronym, String title, boolean pub) {
		setPub(pub);
		setTitle(title);
		setAcronym(acronym);
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEditionInter createVirtualEditionInter(FragInter inter) {
		VirtualEditionInter virtualInter = null;
		if (canAddFragInter(inter)) {
			virtualInter = new VirtualEditionInter(this, inter);
		}
		return virtualInter;
	}

	public boolean checkAccess(LdoDUser user) {
		if (getPub()) {
			return true;
		} else if (getParticipantSet().contains(user)) {
			return true;
		}

		return false;
	}
}
