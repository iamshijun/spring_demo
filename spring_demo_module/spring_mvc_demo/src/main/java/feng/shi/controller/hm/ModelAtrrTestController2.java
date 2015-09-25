package feng.shi.controller.hm;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import feng.shi.model.UserModel;

@Controller
@RequestMapping("/user")
public class ModelAtrrTestController2 {
	
	@ModelAttribute("user") // ①
	public UserModel getUser(@RequestParam(value = "username", defaultValue = "") String username) {
		// todo 去数据库根据用户名查找用户对象
		UserModel user = new UserModel();
		user.setRealname("zhang");
		System.out.println("getUser :" + System.identityHashCode(user));
		return user;
	}
	
	@ModelAttribute("cityList")
	public List<String> cityList() {
	    return Arrays.asList("北京", "山东","上海");
	}
	
	@RequestMapping(value="/model1") //②
	public String test1(@ModelAttribute("user") UserModel user, Model model){
		System.out.println("test1 : " + System.identityHashCode(user));
		return "user/userInfo";
	}
	
	
	@RequestMapping(value="/model3") //②   //此时Spring Web MVC会根据RequestToViewNameTranslator进行逻辑视图名的翻译  这里因为请求是 /user/model3  所以viewName=user/model3 -> WEB-INF/pages/jsp/user/model3.jsp
	public @ModelAttribute("user2") UserModel test3(
			@ModelAttribute("user2") UserModel user) {
		return user;
	}
	
	
	/*
	 * 此处我们看到①和②有同名的命令对象，那Spring Web MVC内部如何处理的呢：
	 *   (1、首先执行@ModelAttribute注解的方法，准备视图展示时所需要的模型数据；@ModelAttribute注解方法形式参数规则和@RequestMapping规则一样，如可以有@RequestParam等；)
	 *   (2、执行@RequestMapping注解方法，进行模型绑定时首先查找模型数据中是否含有同名对象，如果有直接使用，如果没有通过反射创建一个，因此②处的user将使用①处返回的命令对象。即②处的user等于①处的user。
	 *      另外 @RequestMapping 中还是有机会修改 model的值的    根据参数对user的属性值进行设置   /model1中如果给定了realname那么在 getUser()中设置的realname被覆盖! 
	 *   )
	 */
	
	
	public String test4(@ModelAttribute UserModel user, Model model) {
		return "user/userInfo";
	}
	//or  或
	public String test5(UserModel user, Model model) {
		return "user/userInfo";
	}
	/*
	 *  此时我们没有为命令对象提供暴露到模型数据中的名字，此时的名字是什么呢？Spring WebMVC自动将简单类名（首字母小写）作为名字暴露，
	 *  如“cn.javass.chapter6.model.UserModel”暴露的名字为“userModel”。
	 */

	
	public @ModelAttribute List<String> test6(){return Arrays.asList("w","s","o");}
	//or 或
	public @ModelAttribute List<UserModel> test7(){ return Arrays.asList(new UserModel());}
	/*  对于集合类型（Collection接口的实现者们，包括数组），生成的模型对象属性名为“简单类名（首字母小写）”+“List”，如List<String>生成的模型对象属性名为“stringList”，
	 *   List<UserModel>生成的模型对象属性名为“userModelList”。
	 * 	 其他情况一律都是使用简单类名（首字母小写）作为模型对象属性名，如Map<String, UserModel>类型的模型对象属性名为“map”。
	 */
}
