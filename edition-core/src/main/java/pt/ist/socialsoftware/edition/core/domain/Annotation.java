package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public abstract class Annotation extends Annotation_Base {

	// protected, maybe
	public void init(VirtualEditionInter inter, String quote, String text) {
		setVirtualEditionInter(inter);
		setQuote(quote);
		setText(text);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		for (Range range : getRangeSet()) {
			range.remove();
		}
		setVirtualEditionInter(null);

		deleteDomainObject();
	}
}
