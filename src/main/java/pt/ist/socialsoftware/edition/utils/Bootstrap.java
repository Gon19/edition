package pt.ist.socialsoftware.edition.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Member.MemberRole;
import pt.ist.socialsoftware.edition.domain.Role;
import pt.ist.socialsoftware.edition.domain.Role.RoleType;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.recommendation.VSMFragmentRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.TextProperty;
import pt.ist.socialsoftware.edition.search.Indexer;
import pt.ist.socialsoftware.edition.topicmodeling.TopicModeler;

/**
 * @author ars
 * 
 */
public class Bootstrap implements WebApplicationInitializer {
	private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	/**
	 * It is invoked from spring mvc user interface
	 * 
	 */
	@Override
	public void onStartup(ServletContext arg0) throws ServletException {
		initializeSystem();
		loadRecommendationCache();
	}

	@Atomic(mode = TxMode.WRITE)
	public static void initializeSystem() {
		if (LdoD.getInstance() == null) {
			new LdoD();
			populateDatabaseUsersAndRoles();
			cleanLucene();
			cleanTopicModeler();
		} else {
			// loadRecommendationCache();
		}
	}

	private static void cleanTopicModeler() {
		TopicModeler topicModeler = new TopicModeler();
		topicModeler.cleanDirectory();
	}

	private static void cleanLucene() {
		Indexer indexer = Indexer.getIndexer();
		indexer.cleanLucene();
	}

	@Atomic(mode = TxMode.WRITE)
	public static void loadRecommendationCache() {
		Set<Fragment> fragments = LdoD.getInstance().getFragmentsSet();

		if (fragments.size() > 500) {
			List<Property> properties = new ArrayList<Property>();
			properties.add(new TextProperty(1.0));

			VSMFragmentRecommender recommender = new VSMFragmentRecommender();
			for (Fragment fragment : fragments) {
				recommender.getMostSimilarItem(fragment, fragments, properties);
			}

			Indexer indexer = Indexer.getIndexer();
			indexer.cleanTermsTFIDFCache();
		}
	}

	private static void populateDatabaseUsersAndRoles() {
		// delete directory and all its files if it exists
		String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");
		File directory = new File(corpusFilesPath);
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				// Unable to delete directory
				e.printStackTrace();
			}
		}
		directory.mkdirs();

		LdoD ldod = LdoD.getInstance();

		Role user = Role.getRole(RoleType.ROLE_USER);
		Role admin = Role.getRole(RoleType.ROLE_ADMIN);

		// the bcrypt generator
		// https://www.dailycred.com/blog/12/bcrypt-calculator
		LdoDUser ars = new LdoDUser(ldod, "ars", "$2a$11$Y0PQlyE20CXaI9RGhtjZJeTM/0.RUyp2kO/YAJI2P2FeINDEUxd2m",
				"António", "Rito Silva", "rito.silva@tecnico.ulisboa.pt");
		LdoDUser diego = new LdoDUser(ldod, "diego", "$2a$11$b3rI6cl/GOzVqOKUOWSQQ.nTJFn.s8a/oALV.YOWoUZu6HZGvyCXu",
				"Diego", "Giménez", "dgimenezdm@gmail.com");
		LdoDUser mp = new LdoDUser(ldod, "mp", "$2a$11$Nd6tuFTBZV3ej02xJcJhUOZtHKsc888UOBXFz9jDYDBs/EHQIIP26", "Manuel",
				"Portela", "mportela@fl.uc.pt");
		LdoDUser tiago = new LdoDUser(ldod, "tiago", "$2a$11$GEa2gLrEweOV5b.fzTi5ueg.s9h2wP/SmRUt2mCvU.Ra7BxgkPVci",
				"Tiago", "Santos", "tiago@tiagosantos.me");
		LdoDUser nuno = new LdoDUser(ldod, "nuno", "$2a$11$ICywhcOlcgbkWmi2zxYRi./AjLrz4Vieb25TBUeK3FsMwYmSPTcMu",
				"Nuno", "Pinto", "nuno.mribeiro.pinto@gmail.com");
		LdoDUser luis = new LdoDUser(ldod, "luis", "$2a$11$c0Xrwz/gw0tBoMo3o1AG3.boCszoGOXyDWZ5z2vSY259/RDLK4ZDi",
				"Luís", "Lucas Pereira", "lmlbpereira@gmail.com");
		LdoDUser andre = new LdoDUser(ldod, "afs", "$2a$11$na24dttCBjjT5uVT0mBCb.MlDdCGHwu3w6tRTqf5OD9QAsIPYJzfu",
				"André", "Santos", "andrefilipebrazsantos@gmail.com");
		LdoDUser daniela = new LdoDUser(ldod, "daniela", "$2a$04$QiGbDnmoyrvyFnJdfsHhSeJoWJkjVkegrIpIADcIBVziVYWPHnPpC",
				"Daniela", "Maduro", "cortesmaduro@hotmail.com");
		LdoDUser joana = new LdoDUser(ldod, "joana", "$2a$12$tdXO4XfyDP0BdrvGyScv9uRHjDPitbwKzpU1eepeJxgzZFnXZczLq",
				"Joana", "Malta", "joanavmalta@gmail.com");
		LdoDUser bernardosoares = new LdoDUser(ldod, "bernardosoares",
				"$2a$04$2romaiXNBOFcVpDrcg0Miepy7AeeBGJq4jc4EdRA/EFekYxSFxTsC", "Bernardo", "Soares",
				"bernardosoares@pessoa.pt");
		LdoDUser rita = new LdoDUser(ldod, "rita", "$2a$12$6UbQBZNy0s2LQnQjaPe2au645FF.gEC7/RF5Xv9P8bdAhJo.fugoa",
				"Rita", "Marrone", "bernardosoares@pessoa.pt");

		ars.setEnabled(true);
		ars.addRoles(user);
		ars.addRoles(admin);

		diego.setEnabled(true);
		diego.addRoles(user);
		diego.addRoles(admin);

		mp.setEnabled(true);
		mp.addRoles(user);
		mp.addRoles(admin);

		tiago.setEnabled(true);
		tiago.addRoles(user);
		tiago.addRoles(admin);

		nuno.setEnabled(true);
		nuno.addRoles(user);
		nuno.addRoles(admin);

		luis.setEnabled(true);
		luis.addRoles(user);
		luis.addRoles(admin);

		andre.setEnabled(true);
		andre.addRoles(user);
		andre.addRoles(admin);

		daniela.setEnabled(true);
		daniela.addRoles(user);
		daniela.addRoles(admin);

		joana.setEnabled(true);
		joana.addRoles(user);

		bernardosoares.setEnabled(true);
		bernardosoares.addRoles(user);

		rita.setEnabled(true);
		rita.addRoles(user);
		rita.addRoles(admin);

		VirtualEdition classX = new VirtualEdition(ldod, ars, "ClassX", "LdoD Edition of Class X", new LocalDate(),
				false, null);
		classX.addMember(luis, MemberRole.ADMIN, true);
		classX.addMember(mp, MemberRole.ADMIN, true);
		classX.addMember(diego, MemberRole.ADMIN, true);
		classX.addMember(tiago, MemberRole.ADMIN, true);
		classX.addMember(ars, MemberRole.ADMIN, true);
		classX.addMember(andre, MemberRole.ADMIN, true);
		classX.addMember(daniela, MemberRole.ADMIN, true);
		classX.addMember(joana, MemberRole.ADMIN, true);
		classX.addMember(bernardosoares, MemberRole.ADMIN, true);
		classX.addMember(rita, MemberRole.ADMIN, true);
		luis.addSelectedVirtualEditions(classX);
		mp.addSelectedVirtualEditions(classX);
		ars.addSelectedVirtualEditions(classX);
		diego.addSelectedVirtualEditions(classX);
		tiago.addSelectedVirtualEditions(classX);
		nuno.addSelectedVirtualEditions(classX);
		andre.addSelectedVirtualEditions(classX);
		bernardosoares.addSelectedVirtualEditions(classX);
		rita.addSelectedVirtualEditions(classX);

		VirtualEdition classY = new VirtualEdition(ldod, ars, "ClassY", "LdoD Edition of Class Y", new LocalDate(),
				false, null);
		classY.addMember(luis, MemberRole.ADMIN, true);
		classY.addMember(mp, MemberRole.ADMIN, true);
		classY.addMember(diego, MemberRole.ADMIN, true);
		classY.addMember(tiago, MemberRole.ADMIN, true);
		classY.addMember(ars, MemberRole.ADMIN, true);
		luis.addSelectedVirtualEditions(classY);
		mp.addSelectedVirtualEditions(classY);
		ars.addSelectedVirtualEditions(classY);
		diego.addSelectedVirtualEditions(classY);
		tiago.addSelectedVirtualEditions(classY);
		nuno.addSelectedVirtualEditions(classY);

		VirtualEdition classW = new VirtualEdition(ldod, ars, "ClassW", "LdoD Edition of Class W", new LocalDate(),
				false, null);
		classW.addMember(diego, MemberRole.ADMIN, true);
		classW.addMember(mp, MemberRole.ADMIN, true);
		classW.addMember(luis, MemberRole.ADMIN, true);
		classW.addMember(andre, MemberRole.ADMIN, true);
		classW.addMember(tiago, MemberRole.ADMIN, true);
		classW.addMember(nuno, MemberRole.ADMIN, true);
		mp.addSelectedVirtualEditions(classW);
		ars.addSelectedVirtualEditions(classW);
		diego.addSelectedVirtualEditions(classW);
		tiago.addSelectedVirtualEditions(classW);
		nuno.addSelectedVirtualEditions(classW);
	}

}
