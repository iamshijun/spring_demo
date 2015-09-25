package cjava.walker.common.service.impl;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cjava.walker.common.service.AService;
import cjava.walker.common.service.BService;
import cjava.walker.support.BeanSelfAware;

@Service
public class BServiceImpl implements BService, BeanSelfAware {// 此处省略Aservice接口定义
	@Autowired
	private AService aService; // ① 通过@Autowired方式注入AService
	private BService self; // ② 注入自己的AOP代理对象

	public void setSelf(Object proxyBean) { // ③
											// 通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象
		this.self = (BService) proxyBean;
		System.out.println("BService=" + AopUtils.isAopProxy(this.self)); // 如果输出true标识AOP代理对象注入成功
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void a() {
		self.b();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void b() {
	}
}
