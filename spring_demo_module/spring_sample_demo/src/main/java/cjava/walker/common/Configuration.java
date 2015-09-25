package cjava.walker.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration implements InitializingBean{
	//spel add in spring 3.0

	//默认值用“:”隔开 
	@Value("${service.broke:tcp://192.168.1.188}") //default value specified by delimiter ":"!
	private String serviceBroke;

	//spel add in spring 3.0
	@Value("#{systemProperties['java.tmp.io']}")
	private String tmp;

	@Value("#{systemProperties['user.locale']}")
	private String defaultLocal;

	@Value("#{T(java.lang.Math).random() * 100}")
	private Number randonNum;
	
	volatile boolean isInitInvoked = false;
	
	@PostConstruct //需要注意的是 这里除了注解之外 还使用到了 InitializingBean 来设置init方法
	//但是这里只会被执行一次 
	public void afterPropertiesSet(){
		if(isInitInvoked)
			throw new IllegalStateException();
		else
			isInitInvoked = true;
		System.out.println("Config.afterPropertiesSet()");
	}

	public String getDefaultLocal() {
		return defaultLocal;
	}

	public void setDefaultLocal(String defaultLocal) {
		this.defaultLocal = defaultLocal;
	}

	public Number getRandonNum() {
		return randonNum;
	}

	public void setRandonNum(Number randonNum) {
		this.randonNum = randonNum;
	}

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

}
