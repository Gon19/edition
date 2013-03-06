package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.GraphVisitor;

public class SpaceText extends SpaceText_Base {

	public enum SpaceDim {
		VERTICAL, HORIZONTAL, UNKNOWN;
	};

	public enum SpaceUnit {
		MINIMS, UNKNOWN;
	};

	public SpaceText() {
		super();
	}

	@Override
	public void print() {
		System.out.println("SPACE ");
	}

	@Override
	public void accept(GraphVisitor visitor) {
		visitor.visit(this);
	}

}
