
package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;

import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public abstract class VSMRecommender<T> implements Recommender<T, Property> {
	public static final int NUMBER_OF_RECOMMENDATIONS = 5;

	protected static final Random RANDOM = new Random();

	protected abstract void prepareToLoadProperty(T item1, T item2, Property property);

	protected abstract double[] loadProperty(T item1, Property property);

	@Override
	public double calculateSimilarity(T item1, T item2, Collection<Property> ps) {
		double[] vector1 = null;
		double[] vector2 = null;
		for (Property property : ps) {
			prepareToLoadProperty(item1, item2, property);
			vector1 = ArrayUtils.addAll(vector1, loadProperty(item1, property));
			vector2 = ArrayUtils.addAll(vector2, loadProperty(item2, property));
		}
		return Vectors.calculateSimilarity(vector1, vector2);
	}

	@Override
	public T getMostSimilarItem(T item, Collection<T> items, Collection<Property> properties) {
		List<T> newList = new ArrayList<T>(items);
		newList.remove(item);
		T result = null;
		double max = Double.NEGATIVE_INFINITY;
		double similarity;
		for (T otherItem : newList) {
			similarity = calculateSimilarity(item, otherItem, properties);
			if (similarity > max) {
				result = otherItem;
				max = similarity;
			}
		}
		return result;
	}

	@Override
	public List<Entry<T, Double>> getMostSimilarItems(T item, Collection<T> items, Collection<Property> properties) {
		// itemSet.remove(item);
		double similarity;
		Map<T, Double> map = new HashMap<T, Double>();
		for (T it : items) {
			similarity = calculateSimilarity(item, it, properties);
			if (similarity >= Double.NEGATIVE_INFINITY)
				map.put(it, similarity);
		}

		return map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toList());
	}

	public List<T> getMostSimilarItemsAsList(T item, Collection<T> items, List<Property> properties) {
		List<T> inters = new ArrayList<T>();

		T nextItem = item;
		do {
			nextItem = getMostSimilarItem(nextItem, items, properties);
			inters.add(nextItem);
			items.remove(nextItem);
		} while (!items.isEmpty());

		// for (Entry<T, Double> entry : getMostSimilarItems(item, items,
		// properties)) {
		// inters.add(entry.getKey());
		// }

		return inters;
	}

}
