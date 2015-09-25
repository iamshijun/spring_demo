package feng.shi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class CacheController extends AbstractController {
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.getWriter().write("<a href=''>this</a>");
		return null;
	}
	
	
	/*
	 * setCacheSeconds(int) 
	 * 
	 * cacheSeconds = -1���������ʱ�䣬������ʾ��Ҫ���棬������ʾ�����κ����飨Ҳ����˵�����ϴεĻ������ã���
      1��cacheSeconds = 0ʱ��������������Ӧͷ���ݣ�
        Pragma��no-cache             // HTTP 1.0�Ĳ�������Ӧͷ
        Expires��1L                  // useExpiresHeader=trueʱ��HTTP 1.0
        Cache-Control ��no-cache      // useCacheControlHeader=trueʱ��HTTP 1.1
        Cache-Control ��no-store       // useCacheControlNoStore=trueʱ���������Ƿ�ֹFirefox����
 
      2��cacheSeconds>0ʱ��������������Ӧͷ���ݣ�
        Expires��System.currentTimeMillis() + cacheSeconds * 1000L    // useExpiresHeader=trueʱ��HTTP 1.0
        Cache-Control ��max-age=cacheSeconds                    // useCacheControlHeader=trueʱ��HTTP 1.1
 
      3��cacheSeconds<0ʱ����ʲô�������ã��������ϴεĻ������á�
 
		 
	�˴���˵һ��������Ӧͷ�����ã���������ѳ����������ݣ�
	HTTP1.0���������Ӧͷ
	  Pragma��no-cache����ʾ��ֹ�ͻ��˻��棬��Ҫǿ�ƴӷ�������ȡ���µ����ݣ�
	  Expires��HTTP1.0��Ӧͷ�����ظ����������ʱ�䣬����ͻ��˷��ֻ����ļ�û�й����򲻷�������HTTP������ʱ������Ǹ�������ʱ�䣨GMT���� �硰Expires:Wed, 14 Mar 2012 09:38:32 GMT����
	 
	HTTP1.1���������Ӧͷ
	  Cache-Control : no-cache       ǿ�ƿͻ���ÿ�������ȡ�����������°汾�����������ػ���ĸ�����֤��
	  Cache-Control : no-store       ǿ�ƿͻ��˲���������ĸ������������Ƿ�ֹFirefox����
	  Cache-Control : max-age=[��]    �ͻ��˸���������ʱ�䣬������HTTP1.0��Expires��ֻ�Ǵ˴��ǻ�����������ʱ���������㣬���Ǿ���ʱ�䡣
	 */
	

}
