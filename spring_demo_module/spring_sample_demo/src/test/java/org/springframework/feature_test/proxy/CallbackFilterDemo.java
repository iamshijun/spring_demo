package org.springframework.feature_test.proxy;

import java.lang.reflect.Method;
 
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
 
 
public class CallbackFilterDemo {
	/*
	 * CallbackFilter可以实现不同的方法使用不同的回调方法
	 * CallbackFilter中的accept方法, 根据不同的method返回不同的值i, 这个值是在callbacks中的顺序, 就是调用了callbacks[i]
	 */
    public static void main(String[] args) {
    	
        Callback[] callbacks = new Callback[] {
            new MethodInterceptorImpl(), NoOp.INSTANCE
        };
         
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyClass.class);
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackFilter(new CallbackFilterImpl());
         
        MyClass myClass = (MyClass) enhancer.create();
         
        myClass.method();
        myClass.method1();
    }
     
    private static class CallbackFilterImpl implements CallbackFilter {
 
        @Override
        public int accept(Method method) {
            if (method.getName().equals("method"))
                return 1;
            else
                return 0;
        }
         
    }
     
    private static class MethodInterceptorImpl implements MethodInterceptor {
 
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        	
            System.err.println("Before invoke " + method);
            //invoke super !!!IMP_
            Object result = proxy.invokeSuper(obj, args);
            
            System.err.println("After invoke" + method);
            
            return result;
        }
         
    }
}
 
class MyClass {
    public void method() {
        System.out.println("MyClass.method()");
    }
     
    public void method1() {
        System.out.println("MyClass.method()1");
    }
}