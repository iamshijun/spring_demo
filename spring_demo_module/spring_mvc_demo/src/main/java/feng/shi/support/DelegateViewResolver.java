package feng.shi.support;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * MultiViewResolver
 */
public class DelegateViewResolver implements ViewResolver {

	private Map<String, ViewResolver> resolvers;

	private ViewResolver defaultViewResolver;

	@Autowired
	public void setResolvers(Map<String, ViewResolver> resolvers) {
		this.resolvers = resolvers;
	}
	
	public Map<String, ViewResolver> getResolvers() {
		return resolvers;
	}

	public void setDefaultViewResolver(ViewResolver defaultViewResolver) {
		this.defaultViewResolver = defaultViewResolver;
	}

	public View resolveViewName(String viewName, Locale locale)
			throws Exception {

		// view "json/xxx" "ftl/yyyy", "jsp/zzz"
		int n = viewName.indexOf("/");

		if (n == -1) {
			return defaultViewResolver.resolveViewName(viewName, locale);
		}

		String key = viewName.substring(0, n);
		String value = viewName.substring(n + 1);

		ViewResolver resolver = resolvers.get(key);
		if (resolver != null) {
			return resolver.resolveViewName(value, locale);
		}
		return defaultViewResolver.resolveViewName(viewName, locale);
	}

}
