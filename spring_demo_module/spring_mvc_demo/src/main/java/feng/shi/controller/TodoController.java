package feng.shi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feng.shi.entity.Todo;
import feng.shi.service.TodoService;

@Controller
public class TodoController {

	private final TodoService service;

	private final MessageSource messageSource;

	@Autowired
	public TodoController(MessageSource messageSource, TodoService service) {
		this.messageSource = messageSource;
		this.service = service;
	}

	@RequestMapping(value = "/findAll",
			method = RequestMethod.GET,
			consumes={"text/plain","application/*"},  // request header->  accept:text/plain,application/*
			produces="text/html") // response header Content-Type:text/html;      tryº”…œ±‡¬Îcharset=utf-8
	public String findAll(Model model) {
		
		List<Todo> models = service.findAll();
		model.addAttribute("todos", models);
		
		String message = messageSource.getMessage("msg.sayHello", new Object[]{"shijun"}, LocaleContextHolder.getLocale());
		System.out.println(message);
		
		return "todo/list";
	}
	
	//Other methods are omitted.
}