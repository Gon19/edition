package pt.ist.socialsoftware.edition.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class TransactionFilter implements Filter {

	@Override
	@Atomic(mode = TxMode.READ, flattenNested = false)
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		chain.doFilter(request, response);

		// try {
		// Transaction.begin(false);
		// chain.doFilter(request, response);
		// } catch (Exception e) {
		// if (logger.isDebugEnabled()) {
		// logger.error("Exception: {}", e.getMessage(), e);
		// }
		// Transaction.abort();
		// }
		//
		// if (Transaction.isInTransaction()) {
		// try {
		// Transaction.commit();
		// } catch (Exception e) {
		// if (logger.isDebugEnabled()) {
		// logger.error("Exception: {}", e.getMessage(), e);
		// }
		//
		// Transaction.abort();
		// }
		// }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}