package feng.shi.controller.hm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import feng.shi.model.UserModel;

@Controller
public class MvcDataBindTestontroller {

	@RequestMapping("/reuqestOrResponse")
	public String reuqestOrResponse(ServletRequest servletRequest,HttpServletRequest httpServletRequest,
			 ServletResponse servletResponse,HttpServletResponse httpServletResponse
			){
		return "index";
	}
	
	@RequestMapping("/inputOrOutBody")
	public void inputOrOutBody(InputStream requestBodyIn, OutputStream responseBodyOut) throws IOException {
		/*
		 * requestBodyIn：获取请求的内容区字节流，等价于request.getInputStream();
		 * responseBodyOut：获取相应的内容区字节流，等价于response.getOutputStream();
		 */
		responseBodyOut.write("success".getBytes());
	}
	
	@RequestMapping("/readerOrWriteBody")
	public void readerOrWriteBody(Reader reader, Writer writer)
	        throws IOException {
		/*
		 *  reader：获取请求的内容区字符流，等价于request.getReader();
		 *  writer：获取相应的内容区字符流，等价于response.getWriter()。
		 */
	    writer.write("hello");
	}
	
	@RequestMapping("/webRequest")
	public String webRequest(WebRequest webRequest, NativeWebRequest nativeWebRequest) {
		//just like servlet api request.getParameter!
		System.out.println(webRequest.getParameter("test"));// ①得到请求参数test的值
		System.out.println(webRequest.getHeader("Host"));// ①得到请求参数test的值
		
		webRequest.setAttribute("name", "value", WebRequest.SCOPE_REQUEST);// ②
		System.out.println(webRequest.getAttribute("name",WebRequest.SCOPE_REQUEST));
		
		HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);// ③
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		
		Assert.state(request!=null);
		Assert.state(response!=null);
		/*
		 * ① webRequest.getParameter：访问请求参数区的数据，可以通过getHeader()访问请求头数据；
		 * 
		 * ② webRequest.setAttribute/getAttribute：到指定的作用范围内取/放属性数据，Servlet定义的三个作用范围分别使用如下常量代表：
		 * 
	     *     SCOPE_REQUEST ：代表请求作用范围；
	     *     SCOPE_SESSION ：代表会话作用范围；
	     *     SCOPE_GLOBAL_SESSION ：代表全局会话作用范围，即ServletContext上下文作用范围。
	     *     
	     * ③ nativeWebRequest.getNativeRequest/nativeWebRequest.getNativeResponse：得到本地的Servlet API。
		 */
		
		return "success";
	}
	
	@RequestMapping("/session")
	public String session(HttpSession session) {
	    System.out.println(session);
	    /*
	     *注意：session访问不是线程安全的，如果需要线程安全，需要设置AnnotationMethodHandlerAdapter
	     *或RequestMappingHandlerAdapter 的synchronizeOnSession属性为true，即可线程安全的访问session。 
	     */
	    return "success";
	}

	@RequestMapping(value = "/commandObject", method = RequestMethod.GET)
	public String toCreateUser(HttpServletRequest request, UserModel user) {
	    return "customer/create";
	}

	@RequestMapping(value = "/commandObject", method = RequestMethod.POST)
	public String createUser(HttpServletRequest request, UserModel user) {
	    System.out.println(user);
	    return "success";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/model")
	public String createUser(Model model, Map model2, ModelMap modelMap) { //ModelMap -> LinkedHashMap, Model实现常使用  ExtendedModelMap -> ModelMap , Model
		/*
		 * AnnotationMethodHandlerAdapter和RequestMappingHandlerAdapter将使用BindingAwareModelMap作为模型对象的实现，
		 * 即此处我们的形参（Model model, Map model2, ModelMap model3）都是同一个BindingAwareModelMap实例
		 */
		model.addAttribute("a", "a");
		model2.put("b", "b");
		modelMap.put("c", "c");
		
		System.out.println(model == model2);
		System.out.println(model2 == modelMap);
		return "success";
	}
	
	 

	@RequestMapping(value = "/mergeModel")
	public ModelAndView mergeModel(Model model) {
		
		model.addAttribute("a", "a");// ①添加模型数据
		ModelAndView mv = new ModelAndView("success");
		
		mv.addObject("a", "update");// ②在视图渲染之前更新③处同名模型数据
		model.addAttribute("a", "new");// ③修改①处同名模型数据
		
		// 视图页面的a将显示为"update" 而不是"new"
		
		/*
		 *  从代码中我们可以总结出功能处理方法的返回值中的模型数据（如ModelAndView）会 合并 功能处理方法形式参数中的模型数据（如Model）.
		 *  但如果两者之间有同名的，返回值中的模型数据会覆盖形式参数中的模型数据。
		 */
		return mv;
	}
	
	@RequestMapping(value = "/error1")
	public String error1(UserModel user, BindingResult result) {   //BindingResult
		return "error";
	}

	@RequestMapping(value = "/error2")
	public String error2(UserModel user, BindingResult result, Model model) {
		return "error";
	}

	@RequestMapping(value = "/error3")
	public String error3(UserModel user, Errors errors) { // Errors
		return "error";
	}

	//Spring3.1之前（使用AnnotationMethodHandlerAdapter）错误对象必须紧跟在命令对象/表单对象之后，如下定义是错误的：
	@RequestMapping(value = "/error4")
	public String error4(UserModel user, Model model, Errors errors){
		return "error";
    }
	//如上代码从Spring3.1开始（使用RequestMappingHandlerAdapter）将能正常工作，但还是推荐“错误对象紧跟在命令对象/表单对象之后”，这样是万无一失的。

	@RequestMapping("/requestparam1")
	public String requestparam1(@RequestParam String username){
		return "index";
	}
	
	@RequestMapping(value="/users/{userId}/topics/{topicId}")
	public String test(
		   @PathVariable(value="userId") int userId, 
	       @PathVariable(value="topicId") int topicId){
		/*
		 *  如请求的URL为“控制器URL/users/123/topics/456”，则自动将URL中模板变量{userId}和{topicId}绑定到通过@PathVariable注解的同名参数上，
		 *  即入参后userId=123、topicId=456。代码在PathVariableTypeController中。
		 *  @PathVariable的value不指定的话 spring会从类文件中通过字节码获取方法中同名的参数 -- 注意需要编译的时候确保将局部变量的信息加到类文件中  上述的@RequestParam同样
		 */
		return "index";
	}
	
	@RequestMapping("/getSessionId")
	public String test(@CookieValue(value="JSESSIONID", defaultValue="") String sessionId) {
		System.out.println(sessionId);
		return "index";
	}
	
}
