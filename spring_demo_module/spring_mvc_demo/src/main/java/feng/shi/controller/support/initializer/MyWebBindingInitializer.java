package feng.shi.controller.support.initializer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import feng.shi.model.PhoneNumberModel;
import feng.shi.model.PhoneNumberModelEditor;

public class MyWebBindingInitializer implements WebBindingInitializer { 
	
    @Override  
    public void initBinder(WebDataBinder binder, WebRequest request) {  
        //注册自定义的属性编辑器  
        //1、日期  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
        //表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换  
        binder.registerCustomEditor(Date.class, dateEditor);  
        //自定义的电话号码编辑器  
        binder.registerCustomEditor(PhoneNumberModel.class, new PhoneNumberModelEditor());  
    }  
}  