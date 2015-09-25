package cjava.walker.common.controller;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/message")
public class MessageSourceTestController {

	@Resource
	private MessageSourceAccessor messageSourceAccessor;

	@ResponseBody
	@RequestMapping("/sayHello/{name}")
	public String sayHello(@PathVariable("name") String username){
		return messageSourceAccessor.getMessage("welcome.sayHello",new Object[]{username},Locale.CHINA);
	}
}
