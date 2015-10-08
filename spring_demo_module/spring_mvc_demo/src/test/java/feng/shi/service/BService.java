package feng.shi.service;

import java.util.concurrent.Future;

public interface BService {
	public Future<String> doAsync();
	public Future<String> doWithOtherBeanWithAsync();
}
