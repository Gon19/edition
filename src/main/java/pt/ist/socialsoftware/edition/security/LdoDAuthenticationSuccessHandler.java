package pt.ist.socialsoftware.edition.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.session.LdoDSession;

@Component
public class LdoDAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static Logger log = LoggerFactory.getLogger(LdoDAuthenticationSuccessHandler.class);

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.debug("onAuthenticationSuccess");

		LdoDSession ldoDSession = null;
		if (request.getSession().getAttribute("ldoDSession") == null) {
			ldoDSession = new LdoDSession();
			request.getSession().setAttribute("ldoDSession", ldoDSession);
		} else {
			ldoDSession = (LdoDSession) request.getSession().getAttribute("ldoDSession");
		}
		LdoDUser user = LdoDUser.getAuthenticatedUser();
		user.setLastLogin(LocalDate.now());
		ldoDSession.updateSession(user);

		super.onAuthenticationSuccess(request, response, authentication);
		log.debug("onAuthenticationSuccess");
	}

}