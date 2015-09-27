package feng.shi.service;

import java.util.concurrent.Future;

public interface AService {
	public Future<String> doAsync();
}
