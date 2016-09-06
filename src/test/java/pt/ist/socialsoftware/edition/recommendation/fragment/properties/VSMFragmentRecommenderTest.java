package pt.ist.socialsoftware.edition.recommendation.fragment.properties;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

@Ignore
public abstract class VSMFragmentRecommenderTest {
	public static final double DELTA = 0.01;
	public static final double MAX = 1.00001;

	protected LdoD ldod;
	protected List<Property> properties;
	protected VSMFragmentRecommender vsmFragmentRecomender;
	protected Fragment frag1;
	protected Fragment frag2;
	protected List<Double> v1;
	protected List<Double> v2;
	protected Property property;
	protected Property propertyWithWeight;
	protected Property propertyWithZeroWeight;

	protected abstract Fragment getFragment1();

	protected abstract Fragment getFragment2();

	protected abstract Property getProperty();

	protected abstract Property getPropertyWithWeight();

	protected abstract Property getPropertyWithZeroWeight();

	protected void prepare() {

	}

	@Before
	public void setUp() {
		Bootstrap.initDatabase();
		try {
			FenixFramework.getTransactionManager().begin(false);
			ldod = LdoD.getInstance();
			properties = new ArrayList<Property>();
			property = getProperty();
			propertyWithWeight = getPropertyWithWeight();
			propertyWithZeroWeight = getPropertyWithZeroWeight();
			properties.add(property);
			vsmFragmentRecomender = new VSMFragmentRecommender();
			frag1 = getFragment1();
			frag2 = getFragment2();
			this.prepare();
			property.prepareToLoadProperty(frag1, frag2);
			v1 = new ArrayList<Double>(property.loadProperty(frag1));
			v2 = new ArrayList<Double>(property.loadProperty(frag2));
		} catch (WriteOnReadError | NotSupportedException | SystemException e1) {
			e1.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		try {
			FenixFramework.getTransactionManager().rollback();
		} catch (IllegalStateException | SecurityException | SystemException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCalculateSimiliraty() {
		double calculateSimiliraty = vsmFragmentRecomender.calculateSimilarity(frag1, frag2, properties);
		Assert.assertTrue(calculateSimiliraty >= 0);
		Assert.assertTrue(calculateSimiliraty <= 1.0000000000000002);
	}

	@Test
	public void testCalculateSimiliraty2() {
		property.prepareToLoadProperty(frag1, frag2);
		propertyWithWeight.prepareToLoadProperty(frag1, frag2);
		ArrayList<Double> v1 = new ArrayList<Double>(property.loadProperty(frag1));
		ArrayList<Double> v3 = new ArrayList<Double>(propertyWithWeight.loadProperty(frag1));
		Assert.assertTrue(v1.size() > 0);
		Assert.assertTrue(v3.size() > 0);
		Assert.assertEquals(v1.size(), v3.size());
		for (int i = 0; i < v1.size(); i++) {
			if (v1.get(i) != 0.0) {
				Assert.assertTrue(v3.get(i) > v1.get(i));
			}
		}
	}

	@Test
	public final void testProperty() {
		Assert.assertTrue(v1.size() > 0);
		Assert.assertTrue(v2.size() > 0);
		Assert.assertEquals(v1.size(), v2.size());
	}
}