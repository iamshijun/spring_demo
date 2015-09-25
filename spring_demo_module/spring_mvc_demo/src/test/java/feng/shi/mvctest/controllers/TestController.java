package feng.shi.mvctest.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import feng.shi.support.ContextVariablesHolder;

@Controller
public class TestController {

	@RequestMapping("/testModelAttribute_inm")
	public String testModelAttributeInMethod(HttpServletRequest request,Model model){
		return "test";
	}
	
	@RequestMapping("/testModelAttribute_inmp")
	public String testModelAttributeInMethodParam(HttpServletRequest request,@ModelAttribute("message") String message){
		return "test";
	}
	
	@RequestMapping("/test_get_ser_wac")
	public void testGetWACOfServletLevel(HttpServletRequest request,HttpServletResponse response){
		WebApplicationContext webApplicationContext = (WebApplicationContext) request.getAttribute(
				DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		System.out.println(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE + " found : " + (webApplicationContext!= null) + "("+ObjectUtils.getIdentityHexString(webApplicationContext)+")");
		
		ContextVariablesHolder.set(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, webApplicationContext);
		//System.identityHashCode(obj);
	}
	
	@ModelAttribute("now") //默认的解析方式会得到类型的名称 date 
	public Date now() {
		Date now =  new Date();
		ContextVariablesHolder.set("now", now);
		return now;
	}

}
