package feng.shi.persistence.config;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class PersistenceJPAConfig{
 
   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(dataSource());
      em.setPackagesToScan(new String[] { "feng.shi.persistence.model" });
 
      em.setJpaVendorAdapter(jpaVendorApdater());
      em.setJpaProperties(additionalProperties());
      return em;
   }
	
	@Bean
	public JpaVendorAdapter jpaVendorApdater() {
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		//HibernateJpaVendorAdapter hibernateVendorAdapter = new HibernateJpaVendorAdapter();
        //hibernateVendorAdapter.setShowSql(true);
		return vendorAdapter;
	}
	
   @Bean
   public DataSource dataSource(){
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
      dataSource.setUrl("jdbc:mysql:///db_meicloud_cas?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&serverTimezone=UTC");
      dataSource.setUsername( "root" );
      dataSource.setPassword( "19940120" );
      return dataSource;
   }

   @Bean
   public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){ //<- from LocalContainerEntityManagerFactoryBean
      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(entityManagerFactory);
 
      return transactionManager;
   }
 
   @Bean
   public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
      return new PersistenceExceptionTranslationPostProcessor();
   }
 
   Properties additionalProperties() {
      Properties properties = new Properties();
      properties.setProperty(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
      //properties.setProperty(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "validate");
      properties.setProperty(org.hibernate.cfg.Environment.SHOW_SQL, "true");
      properties.setProperty(org.hibernate.cfg.Environment.FORMAT_SQL, "true");
      return properties;
   }
}