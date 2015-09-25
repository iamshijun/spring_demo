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
public class AServiceImpl implements AService, BeanSelfAware {// �˴�ʡ��Aservice�ӿڶ���
	@Autowired
	private BService bService; // �� ͨ��@Autowired��ʽע��BService
	private AService self; // �� ע���Լ���AOP�������

	public void setSelf(Object proxyBean) {
		this.self = (AService) proxyBean; // ��
											// ͨ��InjectBeanSelfProcessorע���Լ���Ŀ����󣩵�AOP�������
		System.out.println("AService==" + AopUtils.isAopProxy(this.self)); // ������true��ʶAOP�������ע��ɹ�
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void a() {
		self.b();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void b() {
	}
}
