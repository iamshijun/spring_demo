package feng.shi.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet(name="forwarding",urlPatterns="/forward")
//有点需要注意的是 似乎urlPattern不给定 就找不到这个名称的servlet了 ,但是在web.xml中不给定servletmapping是可以的!!! <_<
public class ForwardingServlet extends HttpServlet {
	
	private static final long serialVersionUID = 8956856948397864543L;

	public ForwardingServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().write("Controller forward to Servlet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
