package feng.shi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

import feng.shi.model.DataBinderTestModel;

@Component
@SuppressWarnings("deprecation")
public class ErrorController extends AbstractCommandController {  
	public ErrorController() {  
            setCommandClass(DataBinderTestModel.class);  
            setCommandName("command");  
     }  
     @Override  
     protected ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {     
            //表示用户名不为空  
            errors.reject("username.not.empty");  
            //带有默认错误消息  
            errors.reject("username.not.empty1", "用户名不能为空1");  
            //带有参数和默认错误消息          
            errors.reject("username.length.error", new Object[]{5, 10},"用户长度不合法,需在5~10之间");  
             
            //得到错误相关的模型数据  
            Map<String,Object> model = errors.getModel();  
            return new ModelAndView("bindAndValidate/error", model);  
     }  
}  