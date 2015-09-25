package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Component("/crud") //Stero type : @Component , @ManageBean @Named ��Щע�ⶼֻ�� ����һ��value��ΪbeanName 
//��������ö��name�Ļ�  ����ʹ�� java-config�ķ�ʽ  @Bean(name={}) name������������ʽ�� 
public class MultiACUsingParameterMethodNameResolver extends MultiActionController {
	
	@Autowired(required=false)
	public void setMyMethodNameResolver(MethodNameResolver methodNameResolver) {
		super.setMethodNameResolver(methodNameResolver);
	}

	public String save(HttpServletRequest request,HttpServletResponse resp){
		return "index";
	}
	
	public String edit(HttpServletRequest request,HttpServletResponse resp){
		return "index";
	}
	
	public String delete(HttpServletRequest request,HttpServletResponse resp){
		return "index";
	}
}
