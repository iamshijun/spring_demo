package cjava.walker.web.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import cjava.walker.common.repository.GroupRepository;
import cjava.walker.entity.User;

@Controller
public class TestController {
	
	private final static Logger logger = LoggerFactory.getLogger(TestController.class);

	@Resource
	private GroupRepository groupRepository;
	
	@ResponseBody
	@RequestMapping("/testJackson")
	public Object testJackon(HttpServletRequest request,Integer id,String name) {
		//设置了 HandlerApater的messageConverters 当前使用Jackon来为我们返回
		User user = new User(id, name);
		return user;
	}

	@RequestMapping("/forward")
	public String testForward(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//		return "redirect:pages/jsp/index.jsp";

		response.addHeader("Contennt-Disposition", "attachement;filename=11111");
		response.addHeader("Last-modified-time", System.currentTimeMillis()+"");
		
		response.setContentType("text/json");
		//response.getOutputStream().write("<div>11111111</div>".getBytes("utf-8"));
		return "jsp/index";
	}
	
	
	@RequestMapping("/test")
	public void test(HttpServletResponse response) throws Exception {
		Object entity = groupRepository.get(1L);
		response.getWriter().write(entity.toString());
	}
	
	private ExecutorService exec = Executors.newFixedThreadPool(
				Runtime.getRuntime().availableProcessors() * 2);
	
	@RequestMapping("/resolve")
	@ResponseBody
	public DeferredResult<String> resolve(){
		final DeferredResult<String> result = new DeferredResult<String>(5L,TimeUnit.SECONDS);
		
		exec.execute(new Runnable(){
			public void run(){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					result.setErrorResult("Erro msg : " + e.getMessage());
					return;
				}
				//simulate
				result.setResult("Hello world");
			}
		});
		result.onCompletion(new Runnable(){
			@Override
			public void run() {
				logger.info("comple !");
			}
		});
		result.onTimeout(new Runnable(){
			@Override
			public void run() {
				logger.warn("get result timeout !");
			}
			
		});
		return result;
	}
}
