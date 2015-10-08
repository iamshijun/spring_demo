package feng.shi.service.impl;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import feng.shi.service.AService;
import feng.shi.service.BService;

@Service("bService")
public class BServiceImpl implements BService {

	@Autowired
	private AService aService;
	
	@PostConstruct
	public void init(){
		System.out.println("BServiceImpl method with annotation:@PostConstruct invoke");
	}
	
	public BServiceImpl(){
		System.out.println("BServiceImpl constructor invoked");
	}

	@Async
	public Future<String> doAsync(){
		try {
			TimeUnit.SECONDS.sleep((int)(Math.random() * 5));
		} catch (InterruptedException ignore) {
		}
		return new AsyncResult<String>("Here we go in bService");
	}
	
	
	public Future<String> doWithOtherBeanWithAsync(){
		return aService.doAsync();
	}
}
