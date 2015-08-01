package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class EditionProperty extends CompositeProperty {
	private static Collection<Double> defaultVector = null;

	public EditionProperty() {
		super();
	}
	
	public EditionProperty(double weight) {
		super(weight);
	}

	public EditionProperty(@JsonProperty("weight") String weight) {
		super(Double.parseDouble(weight));
	}

	@Override
	public Collection<Double> extractVector(ExpertEditionInter inter) {
		List<Double> vector = new ArrayList<Double>();
		for(ExpertEdition expertEdition : LdoD.getInstance().getExpertEditionsSet()) {
			if(inter.getExpertEdition().getEditor().equals(expertEdition.getEditor())) {
				vector.add(1.0);
				vector.addAll(inter.accept(heteronymProperty));
				vector.addAll(inter.accept(dateProperty));
			} else {
				vector.add(0.);
				vector.addAll(heteronymProperty.getDefaultVector());
				vector.addAll(dateProperty.getDefaultVector());
			}
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>();
		for(ExpertEdition expertEdition : LdoD.getInstance().getExpertEditionsSet()) {
			ExpertEditionInter expertEditionInter = fragment.getExpertEditionInter(expertEdition.getEditor());
			if(expertEditionInter != null) {
				vector.add(1.0);
				vector.addAll(expertEditionInter.accept(heteronymProperty));
				vector.addAll(expertEditionInter.accept(dateProperty));
			} else {
				vector.add(0.);
				vector.addAll(heteronymProperty.getDefaultVector());
				vector.addAll(dateProperty.getDefaultVector());
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> extractVector(VirtualEditionInter virtualEditionInter) {
		return virtualEditionInter.getLastUsed().accept(this);
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		if(defaultVector == null) {
			List<Double> vector = new ArrayList<>();
			int times = LdoD.getInstance().getExpertEditionsSet().size();
			for(int i = 0; i < times; i++) {
				vector.add(0.);
				vector.addAll(heteronymProperty.getDefaultVector());
				vector.addAll(dateProperty.getDefaultVector());
			}
			defaultVector = Collections.unmodifiableCollection(vector);
		}
		return defaultVector;
	}

	@Override
	public void userWeights(RecommendationWeights recommendationWeights) {
		recommendationWeights.setEditionWeight(getWeight());
	}

	@Override
	public String getTitle() {
		return "Edition";
	}

}
