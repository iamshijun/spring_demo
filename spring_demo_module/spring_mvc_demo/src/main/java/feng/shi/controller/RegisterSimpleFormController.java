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
		
		setCommandClass(UserModel.class); // 设置命令对象实现类
		setCommandName("user");// 设置命令对象的名字
		
	}

	// form object 表单对象，提供展示表单时的表单数据（使用commandName放入请求）
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UserModel user = new UserModel();
		user.setUsername("请输入用户名");
		return user;
	}

	// 提供展示表单时需要的一些其他数据
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		map.put("cityList", Arrays.asList("山东", "北京", "上海"));
		return map;
	}

	protected void doSubmitAction(Object command) throws Exception {
		UserModel user = (UserModel) command;
		// TODO 调用业务对象处理
		System.out.println(user);
	}
}