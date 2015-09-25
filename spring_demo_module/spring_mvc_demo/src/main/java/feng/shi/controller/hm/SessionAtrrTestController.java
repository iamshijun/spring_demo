package feng.shi.controller.hm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import feng.shi.model.UserModel;

@Controller
@RequestMapping("/user")
//1、在控制器类头上添加@SessionAttributes注解
@SessionAttributes({"user"})  //①
public class SessionAtrrTestController {
	//2、@ModelAttribute注解的方法进行表单引用对象的创建
	@ModelAttribute("user")    //②
	public UserModel initUser(){
		return new UserModel();
	} 

	//3、@RequestMapping注解方法的@ModelAttribute注解的参数进行命令对象的绑定
	@RequestMapping("/session1")   //③
	public String session1(@ModelAttribute("user") UserModel user){
		return "user/userInfo";
	}

	//4、通过SessionStatus的setComplete()方法清除@SessionAttributes指定的会话数据
	@RequestMapping("/session2")   //③
	public String session(@ModelAttribute("user") UserModel user, SessionStatus status) {
	    if(true) { //④
	        status.setComplete();
	    }
	    return "success";
	} 
}
