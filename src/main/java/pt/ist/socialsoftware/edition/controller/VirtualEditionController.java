package pt.ist.socialsoftware.edition.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.MergeCategory;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.mallet.CorpusGenerator;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDEditVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.validator.VirtualEditionValidator;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/virtualeditions")
public class VirtualEditionController {

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		LdoDSession ldoDSession = new LdoDSession();

		System.out.println("VirtualEditionController:getLdoDSession()");

		LdoDUser user = LdoDUser.getUser();
		if (user != null) {
			for (VirtualEdition virtualEdition : user
					.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
		}
		return ldoDSession;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String listVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession) {

		model.addAttribute("expertEditions", LdoD.getInstance()
				.getSortedExpertEdition());
		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());
		return "virtual/editions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/create")
	public String createVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("acronym") String acronym,
			@RequestParam("title") String title,
			@RequestParam("pub") boolean pub,
			@RequestParam("use") String editionID) {

		/**
		 * WORKAROUND: to create a user without regenerating the data base LdoD
		 * ldod = LdoD.getInstance(); LdoDUser tiago = new LdoDUser(ldod,
		 * "tiago",
		 * "de968c78d0e50dbfd5083e1994492548baf4159f7112242acb02f773dc308ac9");
		 * for (Role role : ldod.getRolesSet()) { tiago.addRoles(role); }
		 **/

		Edition usedEdition = null;
		if (!editionID.equals("no")) {
			usedEdition = FenixFramework.getDomainObject(editionID);
		}

		LocalDate date = new LocalDate();

		title = title.trim();
		acronym = acronym.trim();

		VirtualEdition virtualEdition = null;

		VirtualEditionValidator validator = new VirtualEditionValidator(
				virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDCreateVirtualEditionException(errors, acronym, title,
					pub, LdoD.getInstance().getVirtualEditions4User(
							LdoDUser.getUser(), ldoDSession),
					LdoDUser.getUser());
		}

		try {
			virtualEdition = LdoD.getInstance().createVirtualEdition(
					LdoDUser.getUser(), acronym, title, date, pub, usedEdition);

		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDCreateVirtualEditionException(errors, acronym, title,
					pub, LdoD.getInstance().getVirtualEditions4User(
							LdoDUser.getUser(), ldoDSession),
					LdoDUser.getUser());
		}

		model.addAttribute("expertEditions", LdoD.getInstance()
				.getSortedExpertEdition());
		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());
		return "virtual/editions";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String deleteVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {

			String acronym = virtualEdition.getAcronym();

			virtualEdition.remove();

			if (ldoDSession.hasSelectedVE(externalId)) {
				ldoDSession.removeSelectedVE(externalId, acronym);
			}

			model.addAttribute("expertEditions", LdoD.getInstance()
					.getSortedExpertEdition());
			model.addAttribute("virtualEditions", LdoD.getInstance()
					.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
			model.addAttribute("user", LdoDUser.getUser());
			return "virtual/editions";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/editForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showEditVirtualEdition(Model model,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			model.addAttribute("externalId", virtualEdition.getExternalId());
			model.addAttribute("acronym", virtualEdition.getAcronym());
			model.addAttribute("title", virtualEdition.getTitle());
			model.addAttribute("date",
					virtualEdition.getDate().toString("dd-MM-yyyy"));
			model.addAttribute("pub", virtualEdition.getPub());
			return "virtual/edition";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String editVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId,
			@RequestParam("acronym") String acronym,
			@RequestParam("title") String title,
			@RequestParam("pub") boolean pub) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		}

		title = title.trim();
		acronym = acronym.trim();

		VirtualEditionValidator validator = new VirtualEditionValidator(
				virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			throw new LdoDEditVirtualEditionException(errors, virtualEdition,
					acronym, title, pub);
		}

		try {
			virtualEdition.edit(acronym, title, pub);
		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			throw new LdoDEditVirtualEditionException(errors, virtualEdition,
					acronym, title, pub);
		}

		model.addAttribute("expertEditions", LdoD.getInstance()
				.getSortedExpertEdition());
		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", LdoDUser.getUser());

		return "virtual/editions";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/toggleselection")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
	public String toggleSelectedVirtualEdition(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId) {
		final VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);

		if (virtualEdition == null)
			return "utils/pageNotFound";

		LdoDUser user = LdoDUser.getUser();

		ldoDSession.toggleSelectedVirtualEdition(user, virtualEdition);

		model.addAttribute("expertEditions", LdoD.getInstance()
				.getSortedExpertEdition());
		model.addAttribute("virtualEditions", LdoD.getInstance()
				.getVirtualEditions4User(LdoDUser.getUser(), ldoDSession));
		model.addAttribute("user", user);
		return "virtual/editions";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/participantsForm/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String showParticipantsForm(Model model,
			@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addparticipant")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String addParticipant(Model model,
			@RequestParam("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.unknown");
			model.addAttribute("errors", errors);
			model.addAttribute("username", username);
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/participants";
		} else {
			user.addToVirtualEdition(virtualEdition);
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/participants";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/removeparticipant")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String removeParticipant(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("veId") String veId,
			@RequestParam("userId") String userId) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		LdoDUser user = FenixFramework.getDomainObject(userId);

		if ((virtualEdition == null) || (user == null)) {
			return "utils/pageNotFound";
		}

		if (virtualEdition.getParticipantSet().size() == 1) {
			List<String> errors = new ArrayList<String>();
			errors.add("user.one");
			model.addAttribute("errors", errors);
			model.addAttribute("virtualedition", virtualEdition);
			return "virtual/participants";
		} else {
			user.removeVirtualEdition(virtualEdition);

			if (user == LdoDUser.getUser()) {
				model.addAttribute(
						"virtualEditions",
						LdoD.getInstance().getVirtualEditions4User(
								LdoDUser.getUser(), ldoDSession));
				model.addAttribute("user", LdoDUser.getUser());
				return "virtual/editions";
			} else {
				model.addAttribute("virtualedition", virtualEdition);
				return "virtual/participants";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/addinter/{veId}/{interId}")
	@PreAuthorize("hasPermission(#veId, 'virtualedition.participant')")
	public String addInter(Model model, @PathVariable String veId,
			@PathVariable String interId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(veId);
		FragInter inter = FenixFramework.getDomainObject(interId);
		if ((virtualEdition == null) && (inter == null)) {
			return "utils/pageNotFound";
		}

		VirtualEditionInter addInter = virtualEdition
				.createVirtualEditionInter(inter);

		if (addInter != null) {
			List<FragInter> inters = new ArrayList<FragInter>();
			inters.add(addInter);

			HtmlWriter4OneInter writer = new HtmlWriter4OneInter(
					addInter.getLastUsed());
			writer.write(false);

			model.addAttribute("writer", writer);

			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", LdoDUser.getUser());
			model.addAttribute("fragment", inter.getFragment());
			model.addAttribute("inters", inters);
			return "fragment/main";
		} else {
			return "utils/pageNotFound";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.public')")
	public String taxonomies(Model model, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/taxonomies";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/regenerateCorpus")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String regenerateCorpus(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId)
			throws FileNotFoundException, IOException {

		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			CorpusGenerator generator = new CorpusGenerator();
			generator.generate(virtualEdition);
			model.addAttribute("virtualEdition", virtualEdition);
			return "virtual/taxonomies";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/taxonomy/createTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public String topicModelling(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@RequestParam("externalId") String externalId,
			@RequestParam("name") String name,
			@RequestParam("numTopics") int numTopics,
			@RequestParam("numWords") int numWords,
			@RequestParam("thresholdCategories") int thresholdCategories,
			@RequestParam("numIterations") int numIterations)
			throws IOException {
		VirtualEdition virtualEdition = FenixFramework
				.getDomainObject(externalId);
		if (virtualEdition == null) {
			return "utils/pageNotFound";
		} else {
			TopicModeler modeler = new TopicModeler();
			Taxonomy taxonomy = null;
			try {
				taxonomy = modeler.generate(virtualEdition, name, numTopics,
						numWords, thresholdCategories, numIterations);
			} catch (LdoDDuplicateNameException ex) {
				// TODO: userfriendly handle of this exception
				System.out.println("Já existe uma taxonomia com este nome");
				throw new LdoDException("Já existe uma taxonomia com este nome");
			}

			if (taxonomy == null) {
				// TODO: userfriendly handle of this exception
				throw new LdoDException(
						"Não existem fragmentos associados a esta edição ou é necessário gerar o Corpus");
			} else {
				model.addAttribute("edition", virtualEdition);
				model.addAttribute("taxonomy", taxonomy);
				return "virtual/taxonomy";
			}
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/taxonomy/delete")
	@PreAuthorize("hasPermission(#virtualEditionExternalId, 'virtualedition.participant')")
	public String deleteTaxonomy(
			Model model,
			@RequestParam("virtualEditionExternalId") String virtualEditionExternalId,
			@RequestParam("taxonomyExternalId") String taxonomyExternalId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyExternalId);
		if (taxonomy == null) {
			return "utils/pageNotFound";
		} else {
			Edition edition = taxonomy.getEdition();

			taxonomy.remove();

			model.addAttribute("virtualEdition", edition);
			return "virtual/taxonomies";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/taxonomy/{taxonomyId}")
	@PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.public')")
	public String showTaxonomy(Model model, @PathVariable String taxonomyId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		if (taxonomy == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("edition", taxonomy.getEdition());
			model.addAttribute("taxonomy", taxonomy);
			return "virtual/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/category/{categoryId}")
	@PreAuthorize("hasPermission(#categoryId, 'category.public')")
	public String showCategory(Model model, @PathVariable String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("category", category);
			return "virtual/category";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.public')")
	public String showFragmentInterpretation(Model model,
			@PathVariable String fragInterId) {
		FragInter fragInter = FenixFramework.getDomainObject(fragInterId);
		if (fragInter == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("fragInter", fragInter);
			return "virtual/fragInter";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category")
	@PreAuthorize("hasPermission(#categoryId, 'category.public')")
	public String updateCategoryName(Model model,
			@RequestParam("categoryId") String categoryId,
			@RequestParam("name") String name) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		} else {
			try {
				category.setName(name);
			} catch (LdoDDuplicateNameException ex) {
				// TODO: userfriendly handling of this exception
				System.out.println("Já existe uma category com este nome");
				throw new LdoDException("Já existe uma category com este nome");

			}
			model.addAttribute("category", category);
			return "virtual/category";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/merge")
	@PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.participant')")
	public String mergeCategories(
			Model model,
			@RequestParam("taxonomyId") String taxonomyId,
			@RequestParam(value = "categories[]", required = false) String categoriesIds[]) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		if (taxonomy == null) {
			return "utils/pageNotFound";
		}

		if ((categoriesIds != null) && (categoriesIds.length > 1)) {
			List<Category> categories = new ArrayList<Category>();
			for (String categoryId : categoriesIds) {
				Category category = FenixFramework.getDomainObject(categoryId);
				categories.add(category);
			}

			Category category = taxonomy.merge(categories);

			model.addAttribute("category", category);
			return "virtual/category";

		} else {
			model.addAttribute("edition", taxonomy.getEdition());
			model.addAttribute("taxonomy", taxonomy);
			return "virtual/taxonomy";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/merge/undo")
	@PreAuthorize("hasPermission(#categoryId, 'category.participant')")
	public String undoMergeCategories(Model model,
			@RequestParam("categoryId") String categoryId) {
		MergeCategory category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return "utils/pageNotFound";
		}

		Taxonomy taxonomy = category.getTaxonomy();

		category.undo();

		model.addAttribute("edition", taxonomy.getEdition());
		model.addAttribute("taxonomy", taxonomy);
		return "virtual/taxonomy";

	}

}
