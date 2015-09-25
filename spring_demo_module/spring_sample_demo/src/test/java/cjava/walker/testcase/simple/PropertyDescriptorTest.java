package cjava.walker.testcase.simple;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;

import org.junit.Test;
import org.springframework.util.ClassUtils;

public class PropertyDescriptorTest {
	@Test
	public void testPropertyDescritor() {
		try {
//			BeanInfo bi = Introspector.getBeanInfo(Person.class);
			BeanInfo bi = Introspector.getBeanInfo(SuperHero.class);
			PropertyDescriptor[] pds = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				boolean isExcluded = isExcludedFromDependencyCheck(pd) ;
				System.out.print(pd.getDisplayName() + 
						"\ttype : " + pd.getPropertyType());
				System.out.print(
						"\t Read : " + (pd.getReadMethod() != null)
						+ "\t Write  : " + (pd.getWriteMethod() != null)
						+ "\t isExclude : " + isExcluded
						
				);
				
				System.out.println();
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	public  boolean isExcludedFromDependencyCheck(PropertyDescriptor pd) {
		Method wm = pd.getWriteMethod();
		if (wm == null) {
			return false;
		}
		if (!wm.getDeclaringClass().getName().contains("$$")) {
			// Not a CGLIB method so it's OK.
			return false;
		}
		// It was declared by CGLIB, but we might still want to autowire it
		// if it was actually declared by the superclass.
		Class superclass = wm.getDeclaringClass().getSuperclass();
		return !ClassUtils.hasMethod(superclass, wm.getName(), wm.getParameterTypes());
	}
}

class SuperHero extends Person {
	private String superSkill;

	public String getSuperSkill() {
		return superSkill;
	}

	public void setSuperSkill(String superSkill) {
		this.superSkill = superSkill;
	}

}

class Person {
	private int id;
	private String name;
	private int age;

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = (int)id;
	}

	public String getName() {
		return name;
	}
	
	public Object getPC(){
		return Math.random();
	}

	public void setBirth(Date birth){
		//...........
		//this.age = 
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}