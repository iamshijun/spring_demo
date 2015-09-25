package feng.shi.support;

import java.util.HashMap;
import java.util.Map;

public class ContextVariablesHolder {

	private static ThreadLocal<Map<String,Object>> contextHolder = new ThreadLocal<Map<String,Object>>(){
		protected java.util.Map<String,Object> initialValue() {
			return new HashMap<String, Object>();
		};
	};
	
	public static Object set(String key,Object value){
		return contextHolder.get().put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValOfType(String key,Class<T> clazz){
		Object o =  contextHolder.get().get(key);
		if(o == null) return null;
		if(!clazz.isAssignableFrom(o.getClass()))
			throw new ClassCastException(o.getClass() + " cannot be cast to "+clazz.toString());
		return ((T) o);
	}
	
	public static Object get(String key){
		return contextHolder.get().get(key);
	}
	
}
