package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.LastModified;

public class LastModifiedCacheController extends AbstractController implements LastModified {
	
	private long lastModified;

	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 点击后再次请求当前页面
		resp.getWriter().write("<a href=''>this</a>");
		return null;
	}

	public long getLastModified(HttpServletRequest request) {
		if (lastModified == 0L) {
			// TODO 此处更新的条件：如果内容有更新，应该重新返回内容最新修改的时间戳
			lastModified = System.currentTimeMillis();
		}
		return lastModified;
	}
}