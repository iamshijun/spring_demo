package feng.shi.controller;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RewritingController {

	@RequestMapping("/rewrite.do")
	public void rewrite(HttpServletRequest request, HttpServletResponse response) {
		String uri = request.getRequestURI(); 
		String queryString = request.getQueryString();
		
		System.out.println("uri = " + uri);
		System.out.println("queryString = " + queryString);
		
		
		Enumeration<String> parameterNames = request.getParameterNames();

		List<String> paramNamesList = Collections.list(parameterNames);
		for (String name : paramNamesList) {
			System.out.println(name + " : " + request.getParameter(name));
		}
	}
}
