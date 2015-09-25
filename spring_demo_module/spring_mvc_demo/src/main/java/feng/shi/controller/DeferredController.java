
package feng.shi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
public class DeferredController {
	
	
	@ResponseBody
	@RequestMapping("/complexJob")
	public DeferredResult<String> square() {
	    final DeferredResult<String> deferredResult = new DeferredResult<String>();
	    runInOtherThread(deferredResult);
	    return deferredResult;
	}
	 
	private void runInOtherThread(DeferredResult<String> deferredResult) {
	    //seconds later in other thread...
	    deferredResult.setResult("HTTP response is: 42");
	}
	
	
}

