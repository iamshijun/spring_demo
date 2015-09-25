package feng.shi.controller.hm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import feng.shi.model.DataBinderTestModel;
import feng.shi.model.UserModel;

@Controller
@RequestMapping("/user")
public class ModelAtrrTestController {
	
	@RequestMapping("/test1") //request : ?username=zhang&password=123&workInfo.city=bj
	public String test1(@ModelAttribute("user") UserModel user,Model model){
		return "user/userInfo";//we can see the model has "user" attribute when we retrieve that in userInfo.jsp
	}
	
	@RequestMapping("/test2") 
	public String test2(@RequestParam UserModel user,Model model){ //bad request:400  对于对象类型上的 @RequestParam,是不能够被找到或者说是不会转换的,要不就不要加@RequestParam,要不就加上@ModelAttribute
		return "user/userInfo";
	}


	//URI模板变量也能自动绑定到命令对象中  !!!!!!!!!当URI模板变量和请求参数同名时，请求参数具有高优先权。试在下列的请求参数中添加  &username=washioreina
	@RequestMapping(value="/model2/{username}")
	//request : /model2/shijun?bool=yes&schooInfo.specialty=computer&hobbyList[0]=program&hobbyList[1]=music&map[key1]=value1&map[key2]=value2&state=blocked    
	public String test2(@ModelAttribute("model") DataBinderTestModel model) { 
		return "bindAndValidate/success";
	}
}
