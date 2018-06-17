package pt.ist.socialsoftware.edition.core.domain;

public abstract class Citation extends Citation_Base {

	public void init(Fragment fragment, String sourceLink, String date, String fragText) {
		setFragment(fragment);
		setSourceLink(sourceLink);
		setDate(date);
		setFragText(fragText);
	}

	public void remove() {
		setFragment(null);

		getAwareAnnotationSet().stream().forEach(aa -> aa.remove());

		deleteDomainObject();
	}

	public abstract long getId();

	// TODO
	public InfoRange getInfoRangeByInter(FragInter inter) {
		return getInfoRangeSet().stream().filter(infoRange -> infoRange.getFragInter() == inter).findFirst()
				.orElse(null);
	}
}
