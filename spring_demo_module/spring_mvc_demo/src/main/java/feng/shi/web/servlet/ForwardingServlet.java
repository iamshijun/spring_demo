package feng.shi.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet(name="forwarding",urlPatterns="/forward")
//�е���Ҫע����� �ƺ�urlPattern������ ���Ҳ���������Ƶ�servlet�� ,������web.xml�в�����servletmapping�ǿ��Ե�!!! <_<
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
