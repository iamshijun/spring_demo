package feng.shi.controller.rest;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class RestController {
	
	private SecureRandom random = new SecureRandom();
	
	@RequestMapping("/hi")
	public String sayHi(String name){
		if(name == null){
			name = "World";
		}
		System.out.println("Hello " + name);
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/course/{cId}",method= RequestMethod.POST)
	public String modifyCourse(@PathVariable Long cId,String title,String content){
		if(random.nextBoolean()){
			return "success";
		}
		return "failed";
	}
	@RequestMapping(value = "/course/{cId}",method= RequestMethod.GET)
	public String courseInfo(@PathVariable Long cId,Integer appendix){//index appendix 不给定会报错, 对于非primitive类型 可以给定""空字符串 e.g /course/1?appendix=  
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/course/{cId}/delete",method= RequestMethod.DELETE) 
	//注意 requestMehthod为delete的时候 不能返回页面资源 ！
	public String deleteCourse(@PathVariable Long cId){
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/owners/{ownerId}/pets/{petId}", method = RequestMethod.GET)  
	public void resovleUriTempateVars(WebRequest webRequest,HttpServletResponse response){
		Map<String, String> uriTemplateVars =
				(Map<String, String>) webRequest.getAttribute(
						HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		
		System.out.println(uriTemplateVars);
	}
	
	@ResponseBody
	@RequestMapping(value = "/q",produces = {"application/json;charset=utf-8"/*,"text/plain;chartset=utf-8","application/*"*/})
	public Map<String,Object> query(@RequestParam("s") String searchWord,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> retMap = new HashMap<String, Object>();
		retMap.put("message", searchWord);
		return retMap;
	}
}
