package cjava.walker.support;

import java.lang.reflect.Method;

import org.springframework.context.ApplicationEvent;

public class JobFinishEvent extends ApplicationEvent {

	private static final long serialVersionUID = 5356254644221286406L;
	private String description;
	
	public JobFinishEvent(Object source) {
		super(source);
	}
	
	public JobFinishEvent(Object source,Class<?> clazz , Method method,Object result){
		super(source);
		//....
	}
	
	public void setJobDescription(String description){
 		 this.description = description;
	}
	
	public String getJobDescription(){
		return description;
	}

}
