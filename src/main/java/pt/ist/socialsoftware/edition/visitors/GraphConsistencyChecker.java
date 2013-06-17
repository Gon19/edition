/**
 * 
 */
package pt.ist.socialsoftware.edition.visitors;

import java.util.HashSet;
import java.util.Set;

import pt.ist.socialsoftware.edition.domain.AddText;
import pt.ist.socialsoftware.edition.domain.DelText;
import pt.ist.socialsoftware.edition.domain.EmptyText;
import pt.ist.socialsoftware.edition.domain.FormatText;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LbText;
import pt.ist.socialsoftware.edition.domain.ParagraphText;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.Reading;
import pt.ist.socialsoftware.edition.domain.SimpleText;
import pt.ist.socialsoftware.edition.domain.SpaceText;
import pt.ist.socialsoftware.edition.domain.SubstText;
import pt.ist.socialsoftware.edition.domain.VariationPoint;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

/**
 * Checks the consistency of the graph: (1) Interpretations for outReadings of a
 * VariationPoint should not overlap (2) given a VariationPoint its inReadings
 * interpretations == outReadings interpretations, where start point inReadings
 * Interpretations == fragment interpretations
 * 
 * @author ars
 * 
 */
public class GraphConsistencyChecker implements GraphVisitor {

	Set<VariationPoint> visited = new HashSet<VariationPoint>();

	/**
	 * @see pt.ist.socialsoftware.edition.visitors.GraphVisitor#visit(pt.ist.socialsoftware.edition.domain.Reading)
	 */
	@Override
	public void visit(VariationPoint point) {
		if (!visited.contains(point)) {

			visited.add(point);

			Set<FragInter> pathFragInters = new HashSet<FragInter>();

			Set<FragInter> allInFragInters = point.getInReadingsInters();
			Set<FragInter> allOutFragInters = point.getOutReadingsInters();

			Boolean inEqualsOut = allInFragInters.equals(allOutFragInters);

			GraphWriter debugTextVisitor = new GraphWriter();
			if (!inEqualsOut) {
				point.accept(debugTextVisitor);
			}

			if (!inEqualsOut) {
				throw new LdoDLoadException(
						"os testemunhos de entrada são diferentes do testemunhos de saída, neste ponto="
								+ debugTextVisitor.getResult());
			}

			assert (inEqualsOut) : "INCONSISTENT GRAPGH: OUT-READINGS <> IN-READINGS"
					+ debugTextVisitor.getResult();

			if (!point.getOutReadingsSet().isEmpty()) {

				for (Reading rdg : point.getOutReadingsSet()) {
					Set<FragInter> thisFragInters = rdg.getFragIntersSet();
					Set<FragInter> intersection = new HashSet<FragInter>(
							pathFragInters);
					intersection.retainAll(thisFragInters);

					GraphWriter debugTextVisitor2 = new GraphWriter();
					if (intersection.size() != 0) {
						point.accept(debugTextVisitor2);
					}

					if (intersection.size() != 0) {
						throw new LdoDLoadException(
								"existem rdgs de um mesmo app com testemunhos sobrepostos "
										+ debugTextVisitor.getResult());
					}

					assert intersection.size() == 0 : "INCONSISTENT GRAPH: AT LEAST TWO OUT-READINGS HAVE INTERPRETATIONS IN COMMON"
							+ debugTextVisitor.getResult();

					pathFragInters.addAll(thisFragInters);
				}

			}

			for (Reading rdg : point.getOutReadingsSet()) {
				rdg.accept(this);
			}

		}

	}

	@Override
	public void visit(Reading reading) {
		reading.getNextVariationPoint().accept(this);
	}

	@Override
	public void visit(SimpleText text) {
		assert false;

	}

	@Override
	public void visit(LbText lbText) {
		assert false;

	}

	@Override
	public void visit(FormatText formatText) {
		assert false;

	}

	@Override
	public void visit(PbText pbText) {
		assert false;

	}

	@Override
	public void visit(SpaceText spaceText) {
		assert false;

	}

	@Override
	public void visit(EmptyText emptyText) {
		assert false;

	}

	@Override
	public void visit(AddText addText) {
		assert false;
	}

	@Override
	public void visit(DelText delText) {
		assert false;

	}

	@Override
	public void visit(SubstText substText) {
		assert false;

	}

	@Override
	public void visit(ParagraphText paragraphText) {
		assert false;

	}
}
