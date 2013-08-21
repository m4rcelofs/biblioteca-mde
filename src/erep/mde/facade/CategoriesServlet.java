package erep.mde.facade;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import erep.mde.dao.CategoryDAO;
import erep.mde.data.Category;

public class CategoriesServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(CategoriesServlet.class
															.getName());

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		CategoryDAO catDao = new CategoryDAO();

		try {
			Collection<Category> categories = catDao.getCategories();

			JSONArray array = new JSONArray();
			for (Category c : categories)
				array.add(c.toJSON());

			resp.getWriter().write(array.toJSONString());

		} finally {
			catDao.close();
		}

	}

}
