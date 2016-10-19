package pt.ist.socialsoftware.edition.recommendation;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.recommendation.properties.DateProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.HeteronymProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.Property.PropertyCache;
import pt.ist.socialsoftware.edition.recommendation.properties.TaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;

public class ReadingRecommendationPerformanceTest {

	// Assuming that the 4 expert editions, the archive edition and user ars are
	// in the database
	@BeforeClass
	public static void setUp() throws IOException, WriteOnReadError, NotSupportedException, SystemException {
		FenixFramework.getTransactionManager().begin(false);

		LdoD ldoD = LdoD.getInstance();

		VirtualEdition archiveEdition = ldoD.getArchiveEdition();
		List<VirtualEditionInter> archiveVirtualEditionInters = archiveEdition.getIntersSet().stream()
				.map(VirtualEditionInter.class::cast).collect(Collectors.toList());

		VSMRecommender<VirtualEditionInter> recommender = new VSMVirtualEditionInterRecommender();

		List<Property> properties = new ArrayList<Property>();
		properties.add(new HeteronymProperty(1.0));
		properties.add(new DateProperty(1.0));
		properties.add(new TaxonomyProperty(1.0, archiveEdition.getTaxonomy(), PropertyCache.ON));
		properties.add(new TextProperty(1.0));

		// warm the system in order to create all the caches
		recommender.getMostSimilarItemsAsList(archiveVirtualEditionInters.get(42),
				new HashSet<VirtualEditionInter>(archiveVirtualEditionInters), properties);

	}

	@AfterClass
	public static void tearDown() throws IllegalStateException, SecurityException, SystemException {
		FenixFramework.getTransactionManager().rollback();
	}

	@Test
	public void testSeveralRecommendations() throws IOException, ParseException {
		ReadingRecommendation recommender = new ReadingRecommendation();

		recommender.setDateWeight(1.0);
		recommender.setHeteronymWeight(1.0);
		recommender.setTextWeight(1.0);
		recommender.setTaxonomyWeight(1.0);

		LdoD ldoD = LdoD.getInstance();
		ExpertEdition pizarroEdition = (ExpertEdition) ldoD.getEdition(Edition.PIZARRO_EDITION_ACRONYM);
		ExpertEditionInter inter = pizarroEdition.getExpertEditionIntersSet().stream().collect(Collectors.toList())
				.get(0);

		for (int i = 0; i < 100; i++) {
			Set<ExpertEditionInter> nextInters = recommender.getNextRecommendations(inter.getExternalId());
			assertFalse(nextInters.isEmpty());
		}

	}

}