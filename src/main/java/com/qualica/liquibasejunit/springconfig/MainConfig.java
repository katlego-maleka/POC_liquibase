package com.qualica.liquibasejunit.springconfig;

import com.qualica.liquibasejunit.domain.Party;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by katlego on 1/7/14.
 */
@Configuration
@ComponentScan( {"com.qualica.liquibasejunit.springconfig", "com.qualica.liquibasejunit.data"}) //All production contexts and test contexts are found automatically, assuming maven dependencies and package naming is correct.
@EnableTransactionManagement
public class MainConfig {
    private static Logger LOG = LoggerFactory.getLogger(MainConfig.class);

    @Autowired
    private DataSource coreDataSource;

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private Properties additionalJPAProperties;

    @Bean
    @DependsOn(value = "liquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setDataSource(coreDataSource);
        lef.setJpaVendorAdapter(jpaVendorAdapter);
        lef.setPersistenceProviderClass(org.hibernate.ejb.HibernatePersistence.class);
        lef.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        lef.setPersistenceUnitName("main.pu");

        additionalJPAProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        lef.setJpaProperties(additionalJPAProperties);

        lef.setPackagesToScan(Party.class.getPackage().getName());
        return lef;
    }



    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory().getObject() );
        return transactionManager;
    }

}
