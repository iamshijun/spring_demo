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
        setCommandClass(DataBinderTestModel.class); //�����������  
        setCommandName("dataBinderTest");//����������������  
    }  
    @Override  
    protected ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {  
        //���command���󿴿��Ƿ����ȷ  
        System.out.println(command);  
        return new ModelAndView("bindAndValidate/success").addObject("dataBinderTest", command);  
    }  
    
/*
 * 1��ʹ��WebDataBinder���п���������ע��PropertyEditor������������
 * �硰������ʾ����������ʹ�õķ�ʽ��ʹ��WebDataBinderע������������PropertyEditor�����ַ�ʽע���PropertyEditorֻ�Ե�ǰ�����������������Ŀ����������Զ�ע�����PropertyEditor�������Ҫ����Ҫ��ע��һ�¡�
 * 
 * 2��ʹ��WebBindingInitializer����ע��PropertyEditor
 * ������ڶ��������ͬʱע������ͬ��PropertyEditorʱ�����Կ���ʹ��WebBindingInitializer��
 * 
 *  3��ȫ�ּ���ע��PropertyEditor��ȫ�ֹ���
 *  ֻ��Ҫ�������Զ����PropertyEditor���ں����ģ����ͬ���¼��ɣ������Editor������������ǡ�ģ������Editor��������Spring���Զ�ʹ�ñ�׼JavaBean�ܹ������Զ�ʶ��
 */
   
    /*@Override  
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {  
        super.initBinder(request, binder);  
        //ע���Զ�������Ա༭��  
        //1������  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
        //��ʾ������������Date���͵����ԣ���ʹ�ø����Ա༭����������ת��  
        binder.registerCustomEditor(Date.class, dateEditor);  
        //�Զ���ĵ绰����༭��  
        binder.registerCustomEditor(PhoneNumberModel.class, new PhoneNumberEditor());  
    }  */
}  