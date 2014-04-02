package pt.ist.socialsoftware.edition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.Heteronym;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.Taxonomy;

@Controller
@RequestMapping("/edition")
public class EditionController {

	@RequestMapping(method = RequestMethod.GET, value = "/acronym/{acronym}")
	public String getEditionTableOfContentsbyAcronym(Model model,
			@PathVariable String acronym) {

		Edition edition = LdoD.getInstance().getEdition(acronym);

		if (edition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("heteronym", null);
			model.addAttribute("edition", edition);

			return "edition/tableOfContents";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/internalid/{id}")
	public String getEditionTableOfContentsbyId(Model model,
			@PathVariable String id) {

		Edition edition = FenixFramework.getDomainObject(id);

		if (edition == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("heteronym", null);
			model.addAttribute("edition", edition);

			return "edition/tableOfContents";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/internalid/heteronym/{id1}/{id2}")
	public String getEditionTableOfContents4Heteronym(Model model,
			@PathVariable String id1, @PathVariable String id2) {

		ExpertEdition edition = FenixFramework.getDomainObject(id1);
		Heteronym heteronym = FenixFramework.getDomainObject(id2);

		if (edition == null) {
			return "utils/pageNotFound";
		} else if (heteronym == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("heteronym", heteronym);
			model.addAttribute("edition", edition);

			return "edition/tableOfContents";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
	public String getUserContributions(Model model,
			@PathVariable String username) {

		LdoDUser user = LdoD.getInstance().getUser(username);

		if (user != null) {
			model.addAttribute("user", user);
			return "edition/userContributions";
		} else {
			return "utils/pageNotFound";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tag/{tagname}")
	public String getTagUsage(Model model, @PathVariable String tagname) {

		Tag tag = LdoD.getInstance().getTag(tagname);

		if (tag != null) {
			model.addAttribute("tag", tag);
			return "edition/tagUsage";
		} else {
			return "utils/pageNotFound";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/category/{categoryId}")
	public String getCategoryTableOfContents(Model model,
			@PathVariable String categoryId) {

		Category category = FenixFramework.getDomainObject(categoryId);

		if (category != null) {
			model.addAttribute("category", category);
			return "edition/categoryTableOfContents";
		} else {
			return "utils/pageNotFound";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/taxonomy/{taxonomyId}")
	public String getTaxonomyTableOfContents(Model model,
			@PathVariable String taxonomyId) {

		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);

		if (taxonomy != null) {
			model.addAttribute("taxonomy", taxonomy);
			return "edition/taxonomyTableOfContents";
		} else {
			return "utils/pageNotFound";
		}
	}

}
