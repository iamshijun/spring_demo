package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

import feng.shi.model.UserModel;

@SuppressWarnings("deprecation")
public class MyAbstractCommandController extends AbstractCommandController {
	
	public MyAbstractCommandController() {
		// �����������ʵ����
		setCommandClass(UserModel.class);
	}

	@Override
	protected ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object command, BindException errors) throws Exception {
		// ���������ת��Ϊʵ������
		ModelAndView mv = new ModelAndView();
		
		UserModel user = (UserModel) command;
		
		mv.setViewName("abstractCommand");
		mv.addObject("user", user);
		return mv;
	}
}