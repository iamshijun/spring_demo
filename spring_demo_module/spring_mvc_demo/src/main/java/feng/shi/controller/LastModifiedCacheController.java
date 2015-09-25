package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.LastModified;

public class LastModifiedCacheController extends AbstractController implements LastModified {
	
	private long lastModified;

	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// ������ٴ�����ǰҳ��
		resp.getWriter().write("<a href=''>this</a>");
		return null;
	}

	public long getLastModified(HttpServletRequest request) {
		if (lastModified == 0L) {
			// TODO �˴����µ���������������и��£�Ӧ�����·������������޸ĵ�ʱ���
			lastModified = System.currentTimeMillis();
		}
		return lastModified;
	}
}