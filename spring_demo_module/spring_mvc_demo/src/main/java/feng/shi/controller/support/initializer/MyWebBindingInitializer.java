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
        //ע���Զ�������Ա༭��  
        //1������  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
        //��ʾ������������Date���͵����ԣ���ʹ�ø����Ա༭����������ת��  
        binder.registerCustomEditor(Date.class, dateEditor);  
        //�Զ���ĵ绰����༭��  
        binder.registerCustomEditor(PhoneNumberModel.class, new PhoneNumberModelEditor());  
    }  
}  