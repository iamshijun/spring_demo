package feng.shi.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import feng.shi.model.DataBinderTestModel;
import feng.shi.model.PhoneNumberModel;
import feng.shi.model.PhoneNumberModelEditor;

@SuppressWarnings("deprecation")
public class DataBinderErrorTestController extends SimpleFormController {
       public DataBinderErrorTestController() {
              setCommandClass(DataBinderTestModel.class);
              setCommandName("dataBinderTest");
       }
       @Override
       protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
              //������ύ���κδ��󶼻��ٻص���չʾҳ��
              System.out.println(errors);
              return super.showForm(request, response, errors);
       }
       @Override
       protected void doSubmitAction(Object command) throws Exception {
              System.out.println(command); //���ύ�ɹ������ݰ󶨳ɹ������й��ܴ���
    }
       @Override
       protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
              super.initBinder(request, binder);
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