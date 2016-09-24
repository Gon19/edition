package pt.ist.socialsoftware.edition.recommendation;

import java.util.HashMap;
import java.util.Map;

import pt.ist.socialsoftware.edition.recommendation.properties.StorableProperty;

public class StoredVectors {
	private static StoredVectors instance;

	public static StoredVectors getInstance() {
		if (instance == null) {
			instance = new StoredVectors();
		}
		return instance;
	}

	private final Map<Class<? extends StorableProperty>, Map<String, double[]>> weights;

	private StoredVectors() {
		weights = new HashMap<Class<? extends StorableProperty>, Map<String, double[]>>();
	}

	public boolean contains(StorableProperty property, String id) {
		if (weights.containsKey(property.getClass())) {
			if (weights.get(property.getClass()).containsKey(id)) {
				return true;
			}
		}
		return false;
	}

	public double[] get(StorableProperty property, String id) {
		if (weights.containsKey(property.getClass())) {
			if (weights.get(property.getClass()).containsKey(id)) {
				return weights.get(property.getClass()).get(id);
			}
		}
		return null;
	}

	public void put(StorableProperty property, String id, double[] weightCollection) {
		if (!weights.containsKey(property.getClass())) {
			weights.put(property.getClass(), new HashMap<String, double[]>());
		}
		weights.get(property.getClass()).put(id, weightCollection);
	}

}
