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
		 * requestBodyIn����ȡ������������ֽ������ȼ���request.getInputStream();
		 * responseBodyOut����ȡ��Ӧ���������ֽ������ȼ���response.getOutputStream();
		 */
		responseBodyOut.write("success".getBytes());
	}
	
	@RequestMapping("/readerOrWriteBody")
	public void readerOrWriteBody(Reader reader, Writer writer)
	        throws IOException {
		/*
		 *  reader����ȡ������������ַ������ȼ���request.getReader();
		 *  writer����ȡ��Ӧ���������ַ������ȼ���response.getWriter()��
		 */
	    writer.write("hello");
	}
	
	@RequestMapping("/webRequest")
	public String webRequest(WebRequest webRequest, NativeWebRequest nativeWebRequest) {
		//just like servlet api request.getParameter!
		System.out.println(webRequest.getParameter("test"));// �ٵõ��������test��ֵ
		System.out.println(webRequest.getHeader("Host"));// �ٵõ��������test��ֵ
		
		webRequest.setAttribute("name", "value", WebRequest.SCOPE_REQUEST);// ��
		System.out.println(webRequest.getAttribute("name",WebRequest.SCOPE_REQUEST));
		
		HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);// ��
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		
		Assert.state(request!=null);
		Assert.state(response!=null);
		/*
		 * �� webRequest.getParameter��������������������ݣ�����ͨ��getHeader()��������ͷ���ݣ�
		 * 
		 * �� webRequest.setAttribute/getAttribute����ָ�������÷�Χ��ȡ/���������ݣ�Servlet������������÷�Χ�ֱ�ʹ�����³�������
		 * 
	     *     SCOPE_REQUEST �������������÷�Χ��
	     *     SCOPE_SESSION ������Ự���÷�Χ��
	     *     SCOPE_GLOBAL_SESSION ������ȫ�ֻỰ���÷�Χ����ServletContext���������÷�Χ��
	     *     
	     * �� nativeWebRequest.getNativeRequest/nativeWebRequest.getNativeResponse���õ����ص�Servlet API��
		 */
		
		return "success";
	}
	
	@RequestMapping("/session")
	public String session(HttpSession session) {
	    System.out.println(session);
	    /*
	     *ע�⣺session���ʲ����̰߳�ȫ�ģ������Ҫ�̰߳�ȫ����Ҫ����AnnotationMethodHandlerAdapter
	     *��RequestMappingHandlerAdapter ��synchronizeOnSession����Ϊtrue�������̰߳�ȫ�ķ���session�� 
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
	public String createUser(Model model, Map model2, ModelMap modelMap) { //ModelMap -> LinkedHashMap, Modelʵ�ֳ�ʹ��  ExtendedModelMap -> ModelMap , Model
		/*
		 * AnnotationMethodHandlerAdapter��RequestMappingHandlerAdapter��ʹ��BindingAwareModelMap��Ϊģ�Ͷ����ʵ�֣�
		 * ���˴����ǵ��βΣ�Model model, Map model2, ModelMap model3������ͬһ��BindingAwareModelMapʵ��
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
		
		model.addAttribute("a", "a");// �����ģ������
		ModelAndView mv = new ModelAndView("success");
		
		mv.addObject("a", "update");// ������ͼ��Ⱦ֮ǰ���¢۴�ͬ��ģ������
		model.addAttribute("a", "new");// ���޸Ģٴ�ͬ��ģ������
		
		// ��ͼҳ���a����ʾΪ"update" ������"new"
		
		/*
		 *  �Ӵ��������ǿ����ܽ�����ܴ������ķ���ֵ�е�ģ�����ݣ���ModelAndView���� �ϲ� ���ܴ�������ʽ�����е�ģ�����ݣ���Model��.
		 *  ���������֮����ͬ���ģ�����ֵ�е�ģ�����ݻḲ����ʽ�����е�ģ�����ݡ�
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

	//Spring3.1֮ǰ��ʹ��AnnotationMethodHandlerAdapter������������������������/������֮�����¶����Ǵ���ģ�
	@RequestMapping(value = "/error4")
	public String error4(UserModel user, Model model, Errors errors){
		return "error";
    }
	//���ϴ����Spring3.1��ʼ��ʹ��RequestMappingHandlerAdapter�����������������������Ƽ����������������������/������֮�󡱣�����������һʧ�ġ�

	@RequestMapping("/requestparam1")
	public String requestparam1(@RequestParam String username){
		return "index";
	}
	
	@RequestMapping(value="/users/{userId}/topics/{topicId}")
	public String test(
		   @PathVariable(value="userId") int userId, 
	       @PathVariable(value="topicId") int topicId){
		/*
		 *  �������URLΪ��������URL/users/123/topics/456�������Զ���URL��ģ�����{userId}��{topicId}�󶨵�ͨ��@PathVariableע���ͬ�������ϣ�
		 *  ����κ�userId=123��topicId=456��������PathVariableTypeController�С�
		 *  @PathVariable��value��ָ���Ļ� spring������ļ���ͨ���ֽ����ȡ������ͬ���Ĳ��� -- ע����Ҫ�����ʱ��ȷ�����ֲ���������Ϣ�ӵ����ļ���  ������@RequestParamͬ��
		 */
		return "index";
	}
	
	@RequestMapping("/getSessionId")
	public String test(@CookieValue(value="JSESSIONID", defaultValue="") String sessionId) {
		System.out.println(sessionId);
		return "index";
	}
	
}
