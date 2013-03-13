package pt.ist.socialsoftware.edition.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.pstm.Transaction;
import pt.ist.socialsoftware.edition.domain.DatabaseBootstrap;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;

public class PlainTransByInterTest {

	@Before
	public void setUp() {
		DatabaseBootstrap.initDatabase();

		LoadLdoDFromTEIService service = new LoadLdoDFromTEIService();
		service.atomicExecution();
	}

	@Test
	public void test() {
		String fragInterExternalID = null;

		Transaction.begin();
		LdoD ldoD = LdoD.getInstance();

		for (Fragment frag : ldoD.getFragments()) {
			for (FragInter fragInter : frag.getFragmentInter()) {
				if (fragInter.getName().equals("Teresa Sobral Cunha")) {
					fragInterExternalID = fragInter.getExternalId();
				}
			}
		}
		Transaction.commit();

		PlainTransByInter service = new PlainTransByInter();
		service.setFragInterExternalID(fragInterExternalID);
		service.atomicExecution();

		assertEquals(
				"Teresa Sobral Cunha: 18-10-1931 Prefiro a prosa ao verso, como modo de arte, por duas razões,",
				service.getTranscription().substring(0, 93));

		assertEquals(
				"Teresa Sobral Cunha: 18-10-1931 Prefiro a prosa ao verso, como modo de arte, por duas razões, das quais a primeira, que é minha, é que não tenho também pode ser minha escolha, pois incapaz de escrever em verso. Mas a segunda, porém, é de todos e não é - creio bem – uma sombra ou dis farce da primeira. Vale pois a pena que eu a esfie, porque toca no sentido íntimo de toda a valia da arte.[SPACE] Considero o verso como uma coisa intermédia, uma pas sa gem da música para a prosa. Como a música, o verso é limi tado por leis rítmicas, que ainda que não sejam as leis rígidas do verso regular, existem todavia como resguardos, coacções, dispositivos automáticos de opressão e castigo. Na prosa falamos livres. Podemos incluir ritmos musicais, e contudo pensar. Podemos incluir ritmos poéticos, e contudo estar fora deles. Um ritmo ocasional de verso não estorva a prosa; um ritmo ocasional de prosa faz tropeçar o verso.[SPACE]",
				service.getTranscription().trim());
	}
}
