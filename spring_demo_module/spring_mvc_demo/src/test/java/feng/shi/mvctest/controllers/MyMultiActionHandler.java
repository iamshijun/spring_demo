package feng.shi.mvctest.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Component("mah")
public class MyMultiActionHandler extends MultiActionController{

	public String handle(HttpServletRequest request,HttpServletResponse response){
		System.out.println("=============MutliActionHandler.handle()==============");
		return "hello";
	}
}
