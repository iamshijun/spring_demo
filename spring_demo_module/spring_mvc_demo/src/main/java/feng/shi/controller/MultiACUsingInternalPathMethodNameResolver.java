package feng.shi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Component("/client/download/*")
public class MultiACUsingInternalPathMethodNameResolver extends MultiActionController{

/*	private static final String HEADER_USERAGENT = "User-Agent";
	
	protected boolean isBlockedBrowser(String userAgent){
		return userAgent.contains("MicroMessenger");//Œ¢–≈ƒ⁄÷√‰Ø¿¿∆˜ua
	}
*/	
	public String req1(HttpServletRequest request, HttpServletResponse response) throws IOException{
		return "downloadApp";
	}
	
	public String req2(HttpServletRequest request, HttpServletResponse response) throws IOException{
		return "";
	}
}
