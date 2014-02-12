package pt.ist.socialsoftware.edition.loaders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jvstm.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.utils.Bootstrap;

public class ImportLdoDFromTEITest {

	@Before
	public void setUp() {
		Bootstrap.initDatabase();

		Transaction.begin();

		LoadTEICorpus corpusLoader = new LoadTEICorpus();
		try {
			corpusLoader.loadTEICorpus(new FileInputStream(
					"/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// "/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"

		LoadTEIFragments fragmentsLoader = new LoadTEIFragments();
		try {
			fragmentsLoader.loadFragmentsAtOnce(new FileInputStream(
					"/Users/ars/Desktop/Frg.1_TEI-encoded_testing.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		Transaction.abort();

		// boolean committed = false;
		// try {
		// Transaction.begin();
		// LdoD ldoD = LdoD.getInstance();
		// Set<Edition> editions = ldoD.getEditionsSet();
		// editions.clear();
		// Set<Taxonomy> taxonomies = ldoD.getTaxonomiesSet();
		// taxonomies.clear();
		// Set<Heteronym> heteronyms = ldoD.getHeteronymsSet();
		// heteronyms.clear();
		// Set<Fragment> fragments = ldoD.getFragmentsSet();
		// fragments.clear();
		// Transaction.commit();
		// committed = true;
		// } finally {
		// if (!committed) {
		//
		// Transaction.abort();
		// fail("failed @TearDown.");
		// }
		// }

	}

	@Test
	public void TestSucessfulLoading() {

		LdoD ldoD = LdoD.getInstance();

		checkTitleStmtLoad(ldoD);
		checkListBiblLoad(ldoD);
		checkTaxonomiesLoad(ldoD);
		checkHeteronymsLoad(ldoD);

		Fragment fragment = checkFragmentLoad(ldoD);
		checkLoadSources(fragment);
		checkLoadWitnesses(fragment);

	}

	private void checkLoadWitnesses(Fragment fragment) {
		assertEquals(7, fragment.getFragmentInterSet().size());
		for (FragInter fragmentInter : fragment.getFragmentInterSet()) {
			if (fragmentInter instanceof ExpertEditionInter) {
				assertTrue(((ExpertEditionInter) fragmentInter)
						.getExpertEdition() != null);
			} else if (fragmentInter instanceof SourceInter) {
				assertTrue(((SourceInter) fragmentInter).getSource() != null);
			}
		}

	}

	private void checkLoadSources(Fragment fragment) {
		assertEquals(3, fragment.getSourcesSet().size());
		for (Source source : fragment.getSourcesSet()) {
			if (source instanceof ManuscriptSource) {
				ManuscriptSource manuscript = (ManuscriptSource) source;
				assertEquals("Lisbon", manuscript.getSettlement());
				assertEquals("BN", manuscript.getRepository());
				assertTrue(manuscript.getIdno().equals(
						"bn-acpc-e-e3-4-1-87_0007_4_t24-C-R0150")
						|| manuscript.getIdno().equals(
								"bn-acpc-e-e3-4-1-87_0005_3_t24-C-R0150"));
			} else if (source instanceof PrintedSource) {
				PrintedSource printedSource = (PrintedSource) source;
				assertEquals("Revista Descobrimento", printedSource.getTitle());
				assertEquals("Lisbon", printedSource.getPubPlace());
				assertEquals("3", printedSource.getIssue());
				assertEquals("1931", printedSource.getDate());
			}

		}
	}

	private Fragment checkFragmentLoad(LdoD ldoD) {
		assertEquals(1, ldoD.getFragmentsSet().size());

		Fragment returnFragment = null;
		for (Fragment fragment : ldoD.getFragmentsSet()) {
			assertEquals("Prefiro a prosa ao verso...", fragment.getTitle());

			returnFragment = fragment;
		}

		return returnFragment;

	}

	private void checkHeteronymsLoad(LdoD ldoD) {
		assertEquals(2, ldoD.getHeteronymsSet().size());
		for (Heteronym heteronym : ldoD.getHeteronymsSet()) {
			assertTrue(heteronym.getName().equals("Bernardo Soares")
					|| heteronym.getName().equals("Vicente Guedes"));
		}
	}

	private void checkTaxonomiesLoad(LdoD ldoD) {
		assertEquals(1, ldoD.getTaxonomiesSet().size());
		Taxonomy taxonomy = ldoD.getTaxonomiesSet().iterator().next();
		assertEquals("António Quadros", taxonomy.getName());

		assertEquals(2, taxonomy.getCategoriesSet().size());
		for (Category category : taxonomy.getCategoriesSet()) {
			assertTrue(category.getName().equals("Fase Confessional")
					|| category.getName().equals("Fase Decadentista"));
		}
	}

	private void checkListBiblLoad(LdoD ldoD) {
		assertEquals(4, ldoD.getExpertEditionsSet().size());
		for (ExpertEdition edition : ldoD.getExpertEditionsSet()) {
			assertEquals("Fernando Pessoa", edition.getAuthor());
			assertEquals("O Livro do Desassossego", edition.getTitle());
			assertTrue((edition.getEditor().equals("Jacinto Prado Coelho") && edition
					.getDate().equals("1982"))
					|| (edition.getEditor().equals("Teresa Sobral Cunha") && edition
							.getDate().equals("1997"))
					|| (edition.getEditor().equals("Richard Zenith") && edition
							.getDate().equals("2007"))
					|| (edition.getEditor().equals("Jerónimo Pizarro") && edition
							.getDate().equals("2010")));
		}
	}

	private void checkTitleStmtLoad(LdoD ldoD) {
		assertEquals("O Livro do Desassossego", ldoD.getTitle());
		assertEquals("Fernando Pessoa", ldoD.getAuthor());
		assertEquals("Project: Nenhum Problema tem Solução", ldoD.getEditor());
		assertEquals("", ldoD.getSponsor());
		assertEquals("FCT", ldoD.getFunder());
		assertEquals("Manuel Portela", ldoD.getPrincipal());
	}

}
