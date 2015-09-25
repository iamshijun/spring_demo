package feng.shi.web.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/feature" })
public class ServletFeatureTest extends HttpServlet{

	private static final long serialVersionUID = 6604595484990814730L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String redirectUrl = req.getParameter("redirectUrl");
		if(redirectUrl == null){
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		redirectUrl = URLDecoder.decode(redirectUrl, "utf-8");
		
		resp.sendRedirect(redirectUrl);
	}

}
