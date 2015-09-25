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
	
	@ModelAttribute("user") // ��
	public UserModel getUser(@RequestParam(value = "username", defaultValue = "") String username) {
		// todo ȥ���ݿ�����û��������û�����
		UserModel user = new UserModel();
		user.setRealname("zhang");
		System.out.println("getUser :" + System.identityHashCode(user));
		return user;
	}
	
	@ModelAttribute("cityList")
	public List<String> cityList() {
	    return Arrays.asList("����", "ɽ��","�Ϻ�");
	}
	
	@RequestMapping(value="/model1") //��
	public String test1(@ModelAttribute("user") UserModel user, Model model){
		System.out.println("test1 : " + System.identityHashCode(user));
		return "user/userInfo";
	}
	
	
	@RequestMapping(value="/model3") //��   //��ʱSpring Web MVC�����RequestToViewNameTranslator�����߼���ͼ���ķ���  ������Ϊ������ /user/model3  ����viewName=user/model3 -> WEB-INF/pages/jsp/user/model3.jsp
	public @ModelAttribute("user2") UserModel test3(
			@ModelAttribute("user2") UserModel user) {
		return user;
	}
	
	
	/*
	 * �˴����ǿ����ٺ͢���ͬ�������������Spring Web MVC�ڲ���δ�����أ�
	 *   (1������ִ��@ModelAttributeע��ķ�����׼����ͼչʾʱ����Ҫ��ģ�����ݣ�@ModelAttributeע�ⷽ����ʽ���������@RequestMapping����һ�����������@RequestParam�ȣ�)
	 *   (2��ִ��@RequestMappingע�ⷽ��������ģ�Ͱ�ʱ���Ȳ���ģ���������Ƿ���ͬ�����������ֱ��ʹ�ã����û��ͨ�����䴴��һ������ˢڴ���user��ʹ�âٴ����ص�������󡣼��ڴ���user���ڢٴ���user��
	 *      ���� @RequestMapping �л����л����޸� model��ֵ��    ���ݲ�����user������ֵ��������   /model1�����������realname��ô�� getUser()�����õ�realname������! 
	 *   )
	 */
	
	
	public String test4(@ModelAttribute UserModel user, Model model) {
		return "user/userInfo";
	}
	//or  ��
	public String test5(UserModel user, Model model) {
		return "user/userInfo";
	}
	/*
	 *  ��ʱ����û��Ϊ��������ṩ��¶��ģ�������е����֣���ʱ��������ʲô�أ�Spring WebMVC�Զ���������������ĸСд����Ϊ���ֱ�¶��
	 *  �硰cn.javass.chapter6.model.UserModel����¶������Ϊ��userModel����
	 */

	
	public @ModelAttribute List<String> test6(){return Arrays.asList("w","s","o");}
	//or ��
	public @ModelAttribute List<UserModel> test7(){ return Arrays.asList(new UserModel());}
	/*  ���ڼ������ͣ�Collection�ӿڵ�ʵ�����ǣ��������飩�����ɵ�ģ�Ͷ���������Ϊ��������������ĸСд����+��List������List<String>���ɵ�ģ�Ͷ���������Ϊ��stringList����
	 *   List<UserModel>���ɵ�ģ�Ͷ���������Ϊ��userModelList����
	 * 	 �������һ�ɶ���ʹ�ü�����������ĸСд����Ϊģ�Ͷ�������������Map<String, UserModel>���͵�ģ�Ͷ���������Ϊ��map����
	 */
}
