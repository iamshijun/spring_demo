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
            //��ʾ�û�����Ϊ��  
            errors.reject("username.not.empty");  
            //����Ĭ�ϴ�����Ϣ  
            errors.reject("username.not.empty1", "�û�������Ϊ��1");  
            //���в�����Ĭ�ϴ�����Ϣ          
            errors.reject("username.length.error", new Object[]{5, 10},"�û����Ȳ��Ϸ�,����5~10֮��");  
             
            //�õ�������ص�ģ������  
            Map<String,Object> model = errors.getModel();  
            return new ModelAndView("bindAndValidate/error", model);  
     }  
}  