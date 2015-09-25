package feng.shi.controller.hm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import feng.shi.model.UserModel;

@Controller
@RequestMapping("/user")
//1���ڿ�������ͷ�����@SessionAttributesע��
@SessionAttributes({"user"})  //��
public class SessionAtrrTestController {
	//2��@ModelAttributeע��ķ������б����ö���Ĵ���
	@ModelAttribute("user")    //��
	public UserModel initUser(){
		return new UserModel();
	} 

	//3��@RequestMappingע�ⷽ����@ModelAttributeע��Ĳ��������������İ�
	@RequestMapping("/session1")   //��
	public String session1(@ModelAttribute("user") UserModel user){
		return "user/userInfo";
	}

	//4��ͨ��SessionStatus��setComplete()�������@SessionAttributesָ���ĻỰ����
	@RequestMapping("/session2")   //��
	public String session(@ModelAttribute("user") UserModel user, SessionStatus status) {
	    if(true) { //��
	        status.setComplete();
	    }
	    return "success";
	} 
}
