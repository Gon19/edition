package pt.ist.socialsoftware.edition.domain;

public abstract class Source extends Source_Base {

	public Source() {
		super();
	}

	public abstract void print();

	public abstract String getName();
}