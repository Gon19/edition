package pt.ist.socialsoftware.edition.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import pt.ist.socialsoftware.edition.shared.exception.LdoDCreateVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDEditVirtualEditionException;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

@ControllerAdvice
public class LdoDExceptionHandler {
	private final static Logger logger = LoggerFactory
			.getLogger(LdoDExceptionHandler.class);

	@ExceptionHandler({ LdoDLoadException.class })
	public ModelAndView handleLdoDLoadException(LdoDLoadException ex) {

		if (logger.isDebugEnabled()) {
			logger.debug("LdoDLoadException: {}", ex.getMessage());
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

	@ExceptionHandler({ LdoDEditVirtualEditionException.class })
	public ModelAndView handleLdoDEditVirtualEditionException(
			LdoDEditVirtualEditionException ex) {

		if (logger.isDebugEnabled()) {
			logger.debug("LdoDEditVirtualEditionException: {}", ex.getErrors());
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("errors", ex.getErrors());
		modelAndView.addObject("externalId", ex.getVirtualEdition()
				.getExternalId());
		modelAndView.addObject("acronym", ex.getAcronym());
		modelAndView.addObject("title", ex.getTitle());
		modelAndView.addObject("date", ex.getVirtualEdition().getDate());
		modelAndView.addObject("pub", ex.isPub());
		modelAndView.setViewName("virtual/edit");
		return modelAndView;
	}

	@ExceptionHandler({ LdoDCreateVirtualEditionException.class })
	public ModelAndView handleLdoDCreateVirtualEditionException(
			LdoDCreateVirtualEditionException ex) {

		if (logger.isDebugEnabled()) {
			logger.debug("LdoDCreateVirtualEditionException: {}",
					ex.getErrors());
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("errors", ex.getErrors());
		modelAndView.addObject("acronym", ex.getAcronym());
		modelAndView.addObject("title", ex.getTitle());
		modelAndView.addObject("pub", ex.isPub());
		modelAndView.addObject("virtualEditions", ex.getVirtualEditions());
		modelAndView.addObject("user", ex.getUser());
		modelAndView.setViewName("virtual/list");
		return modelAndView;
	}

	@ExceptionHandler({ AccessDeniedException.class })
	public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {

		if (logger.isDebugEnabled()) {
			logger.debug("AccessDeniedException: {}", ex.getMessage());
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/ldoDExceptionPage");
		modelAndView.addObject("i18n", true);
		modelAndView.addObject("message", "general.access.denied");
		return modelAndView;
	}

	@ExceptionHandler({ Exception.class })
	public ModelAndView handleException(Exception ex) {

		if (logger.isDebugEnabled()) {
			logger.error("Exception: {}", ex.getMessage(), ex);
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("utils/ldoDExceptionPage");
		modelAndView.addObject("message", ex.getMessage());
		return modelAndView;
	}

}
