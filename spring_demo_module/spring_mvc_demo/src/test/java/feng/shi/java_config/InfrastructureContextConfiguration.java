package feng.shi.java_config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {
		//"feng.shi.repository.**.impl", 
		"feng.shi.repository.impl", 
		"feng.shi.component", 
		"feng.shi.service.impl", 
		"feng.shi.config"
	}
)
@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = {"com.savdev.springmvcexample.repository"})
public class InfrastructureContextConfiguration {

    @Configuration
    @Profile(value = "test")
    @PropertySource("classpath:/db/config/mysql.properties")
    public static class FileBasedConfiguration {

        @Inject
        private Environment environment;

        @Bean
        public DataSource dataSource() {
            BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
            dataSource.setDriverClassName(
            		               environment.getProperty("jdbc.driver"));
            dataSource.setUrl(     environment.getProperty("jdbc.url"));
            dataSource.setUsername(environment.getProperty("jdbc.username"));
            dataSource.setPassword(environment.getProperty("jdbc.password"));
            return dataSource;
        }
    }

 /*   @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:/db/liquibase/changelog/db.changelog-master.xml");
        liquibase.setDropFirst(true);
        return liquibase;
    }*/
}