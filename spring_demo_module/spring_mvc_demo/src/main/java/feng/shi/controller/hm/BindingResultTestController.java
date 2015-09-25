package feng.shi.controller.hm;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import feng.shi.entity.Todo;

@Controller
@RequestMapping("/bindTest")
public class BindingResultTestController {

	@RequestMapping(value = "/doBind",params = {"model=todo","!usingModel"})
	public void bindTodo(/*@ModelAttribute("model")*/ Todo todo,BindingResult bindingResult){
		System.out.println(todo);
	}
	
	@RequestMapping(value = "/doBind",params = {"model=todo","usingModel=1"})
	public void bindTodo2(@ModelAttribute("model") Todo todo,BindingResult bindingResult){
		System.out.println(todo);
	}
}
