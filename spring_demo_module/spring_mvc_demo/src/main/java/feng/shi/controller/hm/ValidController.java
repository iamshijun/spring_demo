package feng.shi.controller.hm;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import feng.shi.model.ValidModel;

@Controller
public class ValidController {

	@RequestMapping("/valid")
	public String valid(@ModelAttribute("vm") @Valid ValidModel vm,BindingResult result){
		if(result.hasErrors()){
			return "valid/validateResult";
		}
		return "index";
	}
}
