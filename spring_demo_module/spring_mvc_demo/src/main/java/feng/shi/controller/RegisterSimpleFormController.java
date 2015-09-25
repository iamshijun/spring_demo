package feng.shi.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.SimpleFormController;

import feng.shi.model.UserModel;

@SuppressWarnings("deprecation")
public class RegisterSimpleFormController extends SimpleFormController {
	
	public RegisterSimpleFormController() {
		
		setCommandClass(UserModel.class); // �����������ʵ����
		setCommandName("user");// ����������������
		
	}

	// form object �������ṩչʾ��ʱ�ı����ݣ�ʹ��commandName��������
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UserModel user = new UserModel();
		user.setUsername("�������û���");
		return user;
	}

	// �ṩչʾ��ʱ��Ҫ��һЩ��������
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		map.put("cityList", Arrays.asList("ɽ��", "����", "�Ϻ�"));
		return map;
	}

	protected void doSubmitAction(Object command) throws Exception {
		UserModel user = (UserModel) command;
		// TODO ����ҵ�������
		System.out.println(user);
	}
}