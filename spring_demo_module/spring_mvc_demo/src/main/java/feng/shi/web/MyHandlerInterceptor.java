package feng.shi.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.util.UrlPathHelper;

import feng.shi.support.AccessAuth;

public class MyHandlerInterceptor extends HandlerInterceptorAdapter implements BeanPostProcessor{
	
	private Logger logger = LoggerFactory.getLogger("Ablesky_Auth_Access");
	
	private static final String AC_HANDLER_METHOD_NAME = "handleRequestInternal";
	
	private static final String MAC_HANDLER_MAP_NAME = "handlerMethodMap";
	
	private static final String CON_HANDLER_METHOD_NAME = "handleRequest";
	
	private final Map<Class<?>,Map<String, Method>> handlerMethodCacheMap = new HashMap<Class<?>,Map<String, Method>>();
	
	private String[] requireParameters;
	
	public void setRequireParameters(String[] requireParameters) {
		this.requireParameters = requireParameters;
	}
	
	@Autowired
	private ViewResolver viewResolver;
	
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		
		Object targetHandler = handler;
		
		if(handler instanceof SpringProxy){ // 有Spring创建的代理对象
			
			Advised advisedSupport = (Advised) handler;
			targetHandler = advisedSupport.getTargetSource().getTarget();
			//handlerClass = advisedSupport.getTargetSource().getTargetClass();
			
		}else if(Proxy.isProxyClass(targetHandler.getClass()) || ClassUtils.isCglibProxy(targetHandler)){//如果是非 spring创建的代理 ,暂不支持
			
			String mapRequest = urlPathHelper.getLookupPathForRequest(request);
			logger.warn("[Request : \"{}\"] 's Handler {} is not a SpringProxy,it cannot be handled by Class AuthHandlerInterceptor,"
					+ "Please make sure if you really need to create the handler proxy",
					mapRequest,targetHandler);
			throw new IllegalArgumentException("Cannot found the target object for proxy handler : " + handler);
		}
		
		Method handlerMethod = null;
		
		Class<?> handlerClass = targetHandler.getClass();
		
		handlerMethod = resolveHandlerMethod(request, targetHandler, handlerClass);
		
		if (handlerMethod == null) {
			//throw new NoSuchRequestHandlingMethodException(methodName, handlerClass);
			
			//levae it to Handlers;!
		}else{
			checkRequiredParamters(request,handler);
			
			if(handlerMethod.isAnnotationPresent(AccessAuth.class)){
				
				if(logger.isTraceEnabled()){
					logger.trace("Handler Method :  {}",handlerMethod.toString());
				}
				
				checkRequiredParamters(request,handler);
				
				AccessAuth accessAuth = handlerMethod.getAnnotation(AccessAuth.class);
				boolean canAccess = canAccess(accessAuth);
				boolean isAjax = true;
				
				String failMessage = "";
				if(canAccess){
					if(!isAjax){
						//往同步请求中放入管理员的导航树以及权限，同步请求只能有一个页面property
						request.setAttribute("navigationTree", "orgNavigationPagesTree");
					}
					
				}else{ // permission deny
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);//403
					
					if(isAjax){ //FIXME what about jsonp
						//
					}else{
						Map<String,Object> model = new HashMap<String, Object>();
						model.put("message", failMessage);
						
						View resolveViewName = viewResolver.resolveViewName("UNSUCCESSVIEW", null);
						resolveViewName.render(model, request, response);
					}
					return false;
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,String> checkRequiredParamters(HttpServletRequest httpServletRequest,Object handlerMethod){
		
		final String[] requireParameters = this.requireParameters;
		
		if(handlerMethod instanceof HandlerMethod){
			//利用RequestMappingHandlerMapping解析出来的 uri变量 (同样也是使用到了UrlPathHelper)
			Map<String, String> uriTemplateVars =
					(Map<String, String>) httpServletRequest.getAttribute(
							HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			
			String resovleArg = (uriTemplateVars != null) ? uriTemplateVars.get("cId") : null;
			System.out.println(resovleArg);
		}else{
			//using request.getParameter
			String resovleArg = httpServletRequest.getParameter("name");
			System.out.println(resovleArg);
		}
		
		return Collections.emptyMap();
	}

	private boolean canAccess(AccessAuth accessAuth){
		return true;
	}
	
	private Method resolveHandlerMethod(HttpServletRequest request,	Object targetHandler, Class<?> handlerClass) throws NoSuchRequestHandlingMethodException {
		
		Method handlerMethod = null;
		
		if(targetHandler instanceof MultiActionController){
			
			MultiActionController mac = (MultiActionController)targetHandler;
			String methodName = mac.getMethodNameResolver().getHandlerMethodName(request);
			Map<String, Method> handlerMethodMap = this.handlerMethodCacheMap.get(handlerClass);
			if(handlerMethodMap == null){
				//throw new NoSuchRequestHandlingMethodException(methodName, handlerClass);
			}else{
				handlerMethod = handlerMethodMap.get(methodName);
			}
			
		}else if(targetHandler instanceof AbstractController){// 2: AbstractController 
			
			Map<String, Method> handlerMethodMap = this.handlerMethodCacheMap.get(handlerClass);
			if(handlerMethodMap == null){
				//throw new NoSuchRequestHandlingMethodException(methodName, handlerClass);
			}else{
				handlerMethod = handlerMethodMap.get(AC_HANDLER_METHOD_NAME);
			}
		}else if(targetHandler instanceof Controller){
			
			Map<String, Method> handlerMethodMap = this.handlerMethodCacheMap.get(handlerClass);
			if(handlerMethodMap == null){
				//throw new NoSuchRequestHandlingMethodException(methodName, handlerClass);
			}else{
				handlerMethod = handlerMethodMap.get(CON_HANDLER_METHOD_NAME);
			}
			
		}else if(targetHandler instanceof HandlerMethod){ //3 : Annotation  @Controller
			
			HandlerMethod hm = (HandlerMethod)targetHandler;
			handlerMethod = hm.getMethod();
		}
		return handlerMethod;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
	}
	
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)	throws BeansException {
		return bean;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object postProcessAfterInitialization(Object bean, String beanName)	throws BeansException {
		Map<String,Method> handlerMethodMap = null;
		if(bean instanceof MultiActionController){// 1. MultiActionController
			
			Field hmp = ReflectionUtils.findField(MultiActionController.class, MAC_HANDLER_MAP_NAME);
			ReflectionUtils.makeAccessible(hmp);
			
			handlerMethodMap = (Map<String, Method>) ReflectionUtils.getField(hmp, bean);
			
		}else if(bean instanceof AbstractController){// 2: AbstractController
			
			Method handlerRequestMethod = ReflectionUtils.findMethod(bean.getClass(),
																	   AC_HANDLER_METHOD_NAME,
																	    new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			
			if(handlerRequestMethod.isAnnotationPresent(AccessAuth.class)){
				handlerMethodMap = new HashMap<String, Method>(1);
				handlerMethodMap.put(AC_HANDLER_METHOD_NAME, handlerRequestMethod);
			}
		}else if(bean instanceof Controller){ // 3: Controller   -- 鉴于1,2,3具有继承关系 这里顺序不能乱
			
			Method handlerRequestMethod = ReflectionUtils.findMethod(bean.getClass(),
																	CON_HANDLER_METHOD_NAME,
																	new Class[]{HttpServletRequest.class,HttpServletResponse.class});
			
			if(handlerRequestMethod.isAnnotationPresent(AccessAuth.class)){
				handlerMethodMap = new HashMap<String, Method>(1);
				handlerMethodMap.put(CON_HANDLER_METHOD_NAME, handlerRequestMethod);
			}
		}
		
		//4. HandlerMethod not necessary
		
		//5. others : Servlet,HttpRequestHandler  如果有这样的情况加上
		
		//At last we add to handlerMethodCacheMap for quick access
		if(handlerMethodMap!=null){
			
			if(logger.isDebugEnabled())
				logger.debug("Register Handler {}'s handlerMethods",bean.getClass());
			
			handlerMethodCacheMap.put(bean.getClass(),Collections.unmodifiableMap(handlerMethodMap));
		}
			
		return bean;
	}

}
