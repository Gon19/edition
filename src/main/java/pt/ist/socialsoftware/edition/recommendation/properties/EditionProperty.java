package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;

public class EditionProperty extends StorableProperty {
	private static Collection<Double> defaultVector = null;

	public EditionProperty() {
		super();
	}

	public EditionProperty(double weight) {
		super(weight);
	}

	public EditionProperty(@JsonProperty("weight") String weight) {
		this(Double.parseDouble(weight));
	}

	@Override
	public Collection<Double> extractVector(ExpertEditionInter inter) {
		List<Double> vector = new ArrayList<Double>();
		for (ExpertEdition expertEdition : LdoD.getInstance().getExpertEditionsSet()) {
			if (inter.getExpertEdition() == expertEdition) {
				vector.add(1.0);
			} else {
				vector.add(0.);
			}
		}
		return vector;
	}

	@Override
	public Collection<Double> extractVector(Fragment fragment) {
		List<Double> vector = new ArrayList<Double>();
		for (ExpertEdition expertEdition : LdoD.getInstance().getExpertEditionsSet()) {
			ExpertEditionInter expertEditionInter = fragment.getExpertEditionInter(expertEdition.getEditor());
			if (expertEditionInter != null) {
				vector.add(1.0);
			} else {
				vector.add(0.);
			}
		}
		return vector;
	}

	@Override
	protected Collection<Double> getDefaultVector() {
		if (defaultVector == null) {
			List<Double> vector = new ArrayList<>();
			int times = LdoD.getInstance().getExpertEditionsSet().size();
			for (int i = 0; i < times; i++) {
				vector.add(0.);
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

	@Override
	protected String getConcreteTitle(FragInter inter) {
		String title = "";
		for (ExpertEdition expertEdition : LdoD.getInstance().getExpertEditionsSet()) {
			if (inter.getFragment().getExpertEditionInter(expertEdition.getEditor()) != null) {
				title += ":" + expertEdition.getAcronym();
			}
		}

		if (title.length() > 0)
			title = title.substring(1);

		return title;
	}

	@Override
	protected Collection<Double> extractVector(Source source) {
		return getDefaultVector();
	}

	@Override
	protected Collection<Double> extractVector(SourceInter sourceInter) {
		return getDefaultVector();
	}

}
