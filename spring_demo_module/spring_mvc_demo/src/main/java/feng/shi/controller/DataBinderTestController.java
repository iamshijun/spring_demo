package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

import feng.shi.model.DataBinderTestModel;

@SuppressWarnings("deprecation")
public class DataBinderTestController extends AbstractCommandController {
	
    public DataBinderTestController() {  
        setCommandClass(DataBinderTestModel.class); //设置命令对象  
        setCommandName("dataBinderTest");//设置命令对象的名字  
    }  
    @Override  
    protected ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {  
        //输出command对象看看是否绑定正确  
        System.out.println(command);  
        return new ModelAndView("bindAndValidate/success").addObject("dataBinderTest", command);  
    }  
    
/*
 * 1、使用WebDataBinder进行控制器级别注册PropertyEditor（控制器独享）
 * 如“【三、示例】”中所使用的方式，使用WebDataBinder注册控制器级别的PropertyEditor，这种方式注册的PropertyEditor只对当前控制器独享，即其他的控制器不会自动注册这个PropertyEditor，如果需要还需要再注册一下。
 * 
 * 2、使用WebBindingInitializer批量注册PropertyEditor
 * 如果想在多个控制器同时注册多个相同的PropertyEditor时，可以考虑使用WebBindingInitializer。
 * 
 *  3、全局级别注册PropertyEditor（全局共享）
 *  只需要将我们自定义的PropertyEditor放在和你的模型类同包下即可，且你的Editor命名规则必须是“模型类名Editor”，这样Spring会自动使用标准JavaBean架构进行自动识别
 */
   
    /*@Override  
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {  
        super.initBinder(request, binder);  
        //注册自定义的属性编辑器  
        //1、日期  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
        //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
        binder.registerCustomEditor(Date.class, dateEditor);  
        //自定义的电话号码编辑器  
        binder.registerCustomEditor(PhoneNumberModel.class, new PhoneNumberEditor());  
    }  */
}  