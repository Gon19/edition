package pt.ist.socialsoftware.edition.domain;

public class Heteronym extends Heteronym_Base implements Comparable<Heteronym> {

	public Heteronym() {
		super();
	}

	public Heteronym(LdoD ldoD, String name) {
		setLdoD(ldoD);
		setName(name);
	}

	public void remove() {
		setLdoD(null);

		getSourceSet().stream().forEach(s -> removeSource(s));
		getFragInterSet().stream().forEach(i -> removeFragInter(i));

		deleteDomainObject();
	}

	@Override
	public int compareTo(Heteronym o) {
		return this.getXmlId().compareTo(o.getXmlId());
	}

}
