package feng.shi.controller.temp;

import java.io.Writer;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.util.UriComponentsBuilder;

import feng.shi.model.UserModel;

@Controller
public class TempController {

	@RequestMapping("/req")
	public void testParamGet(
			@RequestParam(required=false) long[] ids,//如果传入的是 如 1,2,3 这样的字符串的话 spring会默认的以 ","号作为分隔符   
			HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(request.getParameter("name"));
		System.out.println(Arrays.toString(ids));
		System.out.println(request.getParameter("ids")); //そのまま　
		
	}
	
	@RequestMapping("/reqArg")
	public void testArgumentResovle(String name,@Value("pwd") String password,
			@ModelAttribute UserModel userModel,
			Errors errors, //An Errors/BindingResult argument is expected to be declared immediately after the model attribute, the @RequestBody or the @RequestPart arguments
			//BindingResult bindingResult,
			UriComponentsBuilder uriComponentsBuilder,
			SessionStatus sessionStatus,
			@RequestHeader("user-agent") String userAgent,
			@CookieValue(required = false) String as/*,Writer writer*/
	){
		
	}
}
