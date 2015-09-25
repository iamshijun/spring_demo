package feng.shi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

// name = /client/download/*
public class ProductRedirectController extends MultiActionController{
	
	public ProductRedirectController(){
		System.out
				.println(">>>>> ProductRedirectController.ProductRedirectController() <<<<<<<");
	}
	
	public String ableCourse(HttpServletRequest request , HttpServletResponse response) throws IOException{
		return "android/ableCourse";
	}

	public String netCourse(HttpServletRequest request , HttpServletResponse response) throws IOException{
		return "android/netCourse";
	}
}