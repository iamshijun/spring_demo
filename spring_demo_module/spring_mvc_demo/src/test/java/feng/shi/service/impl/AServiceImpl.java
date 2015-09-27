package feng.shi.service.impl;

import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import feng.shi.service.AService;
import feng.shi.service.BService;

@Service("aService")
public class AServiceImpl implements AService {

	@Autowired
	private BService bService;
	
	@PostConstruct
	public void init(){
		System.out.println("AServiceImpl method with annotation:@PostConstruct invoke");
	}
	
	public AServiceImpl(){
		System.out.println("AServiceImpl constructor invoked");
	}

	@Override
//	@Async
	public Future<String> doAsync() { // dummy
		return null;
	}
	
	
}
