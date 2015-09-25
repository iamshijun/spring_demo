package feng.shi.controller.hm;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/feature")
public class MvcFeatureTestController {

	@RequestMapping("/testModleAttribute")
	public String test1(ModelMap model){
		System.out.println("today : " + model.get("today"));
		System.out.println("strs  : " + model.get("strs"));
		return "test";
	}
	
	@ModelAttribute("today") 
	public Date today() {
		return new Date();
	}
	
	@ModelAttribute("strs") 
	public List<String> randomStr() {
		return Arrays.asList("1","2","3");
	}

	@RequestMapping(value = "/owners/{ownerId}/pets/{petId}", method = RequestMethod.GET)    //    /feature/owners/44/pets/55;q=22,33;s=23
    public void findPet(
    		@MatrixVariable Map<String, String> matrixVars,
    		@MatrixVariable(pathVar = "petId") Map<String, String> petMatrixVars) {    
        System.out.println(matrixVars);    
        System.out.println(petMatrixVars);    
    } 
	
	/*
	value的uri值为以下三类：
		A） 可以指定为普通的具体值；
		B) 可以指定为含有某变量的一类值(URI Template Patterns with Path Variables)；
		C) 可以指定为含正则表达式的一类值( URI Template Patterns with Regular Expressions);
	*/
	
	//try  /feature/login  ,  /feature/login;jsessionid=  ,....
	@RequestMapping(value = {"/{login:login;?.*}"}, method = RequestMethod.GET)  
	public String loginForm() {  
	    return "index";
	}  
	
	//用 ":"分隔开 占位符名称 和正则表达式
	@RequestMapping("/spring-web/{symbolicName:[a-z-]+}-{version:\\d\\.\\d\\.\\d}.{extension:[a-z]*}") //try :   /feature/spring-web/xwork-3.2.8.jar  
    public void handle(@PathVariable String symbolicName ,@PathVariable String version, @PathVariable String extension) {
		System.out.println("symblicName :" + symbolicName);
		System.out.println("version :" + version);
		System.out.println("extention:" + extension);
	}  
	
	@RequestMapping("/paramWithSpEL")
	public void paramWithSpEL(@RequestParam(required=false,defaultValue="T(Integer)") Class<?> clazz){
		System.out.println(clazz);
	}
}
