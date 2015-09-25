package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Component("/crud") //Stero type : @Component , @ManageBean @Named 这些注解都只能 给定一个value作为beanName 
//如果你想用多个name的话  建议使用 java-config的方式  @Bean(name={}) name参数是数组形式的 
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
