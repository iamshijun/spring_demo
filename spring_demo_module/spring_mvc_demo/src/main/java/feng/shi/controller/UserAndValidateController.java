package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import feng.shi.model.UserModel;
import feng.shi.service.UserService;

//省略import
@Component
public class UserAndValidateController extends MultiActionController {
    //用户服务类
	private UserService userService;
	//逻辑视图名 通过依赖注入方式注入，可配置
	private String createView;
	private String updateView;
	private String deleteView;
	private String listView;
	private String redirectToListView;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getCreateView() {
		return createView;
	}
	public void setCreateView(String createView) {
		this.createView = createView;
	}
	public String getUpdateView() {
		return updateView;
	}
	public void setUpdateView(String updateView) {
		this.updateView = updateView;
	}
	public String getDeleteView() {
		return deleteView;
	}
	public void setDeleteView(String deleteView) {
		this.deleteView = deleteView;
	}
	public String getListView() {
		return listView;
	}
	public void setListView(String listView) {
		this.listView = listView;
	}
	public String getRedirectToListView() {
		return redirectToListView;
	}
	public void setRedirectToListView(String redirectToListView) {
		this.redirectToListView = redirectToListView;
	}
	
	
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response, UserModel user) {
		if("GET".equals(request.getMethod())) {
			//如果是get请求 我们转向 新增页面
			return new ModelAndView(getCreateView());
		}
		//validate
		BindException errors = new BindException(user, getCommandName(user));  
		//如果用户名为空  
		if(!StringUtils.hasLength(user.getUsername())) {  
		    errors.rejectValue("username", "username.not.empty");  
		}  
		if(errors.hasErrors()) {  
		    return new ModelAndView(getCreateView()).addAllObjects(errors.getModel());  
		} 
		
		userService.create(user);
		//直接重定向到列表页面
		return new ModelAndView(getRedirectToListView());
	}
	
	public ModelAndView update(HttpServletRequest request, HttpServletResponse response, UserModel user) {
		if("GET".equals(request.getMethod())) {
			//如果是get请求 我们转向更新页面
			ModelAndView mv = new ModelAndView();
			//查询要更新的数据
			mv.addObject("command", userService.get(user.getUsername()));
			mv.setViewName(getUpdateView());
			return mv;
		}
		userService.update(user);
		//直接重定向到列表页面
		return new ModelAndView(getRedirectToListView());
	}

	
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response, UserModel user) {
		if("GET".equals(request.getMethod())) {
			//如果是get请求 我们转向删除页面
			ModelAndView mv = new ModelAndView();
			//查询要删除的数据
			mv.addObject("command", userService.get(user.getUsername()));
			mv.setViewName(getDeleteView());
			return mv;
		}
		userService.delete(user);
		//直接重定向到列表页面
		return new ModelAndView(getRedirectToListView());
	}	
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("userList", userService.list());
		mv.setViewName(getListView());
		return mv;
	}
	
	//如果使用委托方式，命令对象名称只能是command
	protected String getCommandName(Object command) {
		//命令对象的名字 默认command
		return "command";
	}
}
