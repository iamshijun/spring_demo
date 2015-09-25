package feng.shi.web;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
	
	
	/* from acegi-security class : UrlUtils */
	public static String getFullRequestURL(HttpServletRequest request){
		final HttpServletRequest r = request;
		return buildFullRequestUrl(r.getScheme(), r.getServerName(), r.getServerPort(), r.getContextPath(),
	            r.getRequestURL().toString(), r.getServletPath(), r.getRequestURI(), r.getPathInfo(), r.getQueryString());
	}

	private static String buildFullRequestUrl(String scheme, String serverName, int serverPort, String contextPath, String requestUrl, String servletPath, String requestURI, String pathInfo, String queryString) {

		boolean includePort = true;

		if ("http".equals(scheme.toLowerCase()) && (serverPort == 80)) {
			includePort = false;
		}

		if ("https".equals(scheme.toLowerCase()) && (serverPort == 443)) {
			includePort = false;
		}

		return scheme + "://" + serverName + ((includePort) ? (":" + serverPort) : "") + contextPath + buildRequestUrl(servletPath, requestURI, contextPath, pathInfo, queryString);
	}

	private static String buildRequestUrl(String servletPath, String requestURI, String contextPath, String pathInfo, String queryString) {

		String uri = servletPath;

		if (uri == null) {
			uri = requestURI;
			uri = uri.substring(contextPath.length());
		}
		return uri + ((pathInfo == null) ? "" : pathInfo) + ((queryString == null) ? "" : ("?" + queryString));
	}
}
