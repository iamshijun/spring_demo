package feng.shi.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "testServlet", 
		urlPatterns = { "/test", "/testServlet" },
		description = "Just a test servlet",
		asyncSupported = true
)
public class TestServlet extends HttpServlet{

	private static final long serialVersionUID = -5767128783535152486L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		System.out.println("TestServlet.doGet()");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		this.doPost(req, resp);
	}
}
