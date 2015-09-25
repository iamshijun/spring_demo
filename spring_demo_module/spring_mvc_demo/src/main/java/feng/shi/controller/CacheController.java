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
	 * cacheSeconds = -1：缓存过期时间，正数表示需要缓存，负数表示不做任何事情（也就是说保留上次的缓存设置），
      1、cacheSeconds = 0时，则将设置如下响应头数据：
        Pragma：no-cache             // HTTP 1.0的不缓存响应头
        Expires：1L                  // useExpiresHeader=true时，HTTP 1.0
        Cache-Control ：no-cache      // useCacheControlHeader=true时，HTTP 1.1
        Cache-Control ：no-store       // useCacheControlNoStore=true时，该设置是防止Firefox缓存
 
      2、cacheSeconds>0时，则将设置如下响应头数据：
        Expires：System.currentTimeMillis() + cacheSeconds * 1000L    // useExpiresHeader=true时，HTTP 1.0
        Cache-Control ：max-age=cacheSeconds                    // useCacheControlHeader=true时，HTTP 1.1
 
      3、cacheSeconds<0时，则什么都不设置，即保留上次的缓存设置。
 
		 
	此处简单说一下以上响应头的作用，缓存控制已超出本书内容：
	HTTP1.0缓存控制响应头
	  Pragma：no-cache：表示防止客户端缓存，需要强制从服务器获取最新的数据；
	  Expires：HTTP1.0响应头，本地副本缓存过期时间，如果客户端发现缓存文件没有过期则不发送请求，HTTP的日期时间必须是格林威治时间（GMT）， 如“Expires:Wed, 14 Mar 2012 09:38:32 GMT”；
	 
	HTTP1.1缓存控制响应头
	  Cache-Control : no-cache       强制客户端每次请求获取服务器的最新版本，不经过本地缓存的副本验证；
	  Cache-Control : no-store       强制客户端不保存请求的副本，该设置是防止Firefox缓存
	  Cache-Control : max-age=[秒]    客户端副本缓存的最长时间，类似于HTTP1.0的Expires，只是此处是基于请求的相对时间间隔来计算，而非绝对时间。
	 */
	

}
