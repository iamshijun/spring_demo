package org.hibernate.feature_test;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=PersistenClassMetaDataTest.RootContext.class)
public class PersistenClassMetaDataTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Configuration
	@ImportResource(value="classpath:springTest/*.xml")
	public static class RootContext{
		
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testClassMetadata() {
		Map x = sessionFactory.getAllClassMetadata();
		for (Iterator<?> i = x.values().iterator(); i.hasNext();) {
			SingleTableEntityPersister y = (SingleTableEntityPersister) i
					.next();
			System.out.println("========///////=========");
			
			System.out.println(y.getName() + " -> " + y.getTableName());
			
			
			for (int j = 0; j < y.getPropertyNames().length; j++) {
				System.out.println("\t"+ y.getPropertyNames()[j]	+ " -> "+ (y.getPropertyColumnNames(j).length > 0 ? y.getPropertyColumnNames(j)[0] : ""));
			}
			System.out.println("========///////=========");
		}
	}
}
