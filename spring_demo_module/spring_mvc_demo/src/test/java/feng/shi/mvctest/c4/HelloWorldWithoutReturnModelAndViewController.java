package feng.shi.mvctest.c4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class HelloWorldWithoutReturnModelAndViewController extends AbstractController {
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		resp.getWriter().write("Hello World!!");		
		//XXX ����ֱ���ڸô�����/������д��Ӧ ����ͨ������null����DispatcherServlet�Լ��Ѿ�д����Ӧ�ˣ�����Ҫ��������ͼ����
		return null;
	}
}