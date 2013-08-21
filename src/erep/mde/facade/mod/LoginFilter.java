package erep.mde.facade.mod;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import erep.mde.dao.ModerationDAO;
import erep.mde.data.Moderator;

public class LoginFilter implements Filter {

	private static Logger	log	= Logger.getLogger(LoginFilter.class.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {
		ModerationDAO dao = new ModerationDAO();
		try {
			UserService userService = UserServiceFactory.getUserService();

			if (!userService.isUserLoggedIn()) {
				((HttpServletResponse) resp).sendError(401);
				log.log(Level.SEVERE, "User is not logged-in in filter");
				return;
			}

			User gUser = userService.getCurrentUser();
			Moderator mod = dao.getModerator(gUser);

			if (mod == null) {
				log.log(Level.WARNING, "New mod user: " + gUser.getUserId()
						+ ", " + gUser.getEmail());
				mod = new Moderator(gUser.getUserId());
				mod.setgEmail(gUser.getEmail());
				mod.setIsAuthorized(false);
				dao.save(mod);
				dao.removeFromCache(gUser);
				((HttpServletResponse) resp).sendError(401);
				return;
			}

			if (!mod.getIsAuthorized()) {
				log.log(Level.WARNING, "Unauthorized mod: " + mod.getgId()
						+ ", " + mod.getgEmail());
				((HttpServletResponse) resp).sendError(403);
				return;
			}

			filterChain.doFilter(req, resp);

		} finally {
			dao.close();
		}

	}
}
