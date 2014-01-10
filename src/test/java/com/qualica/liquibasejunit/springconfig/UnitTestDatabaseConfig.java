package com.qualica.liquibasejunit.springconfig;

import com.jolbox.bonecp.BoneCPDataSource;
import com.qualica.liquibasejunit.springconfig.profiles.ApplicationProfiles;
import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.logging.LogFactory;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.hibernate.dialect.H2Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by katlego on 1/9/14.
 */
@Configuration
@Profile(ApplicationProfiles.UNIT_TEST)
public class UnitTestDatabaseConfig extends DatabaseConfig {

    //Must try this for a true in-memory H2: http://tillias.wordpress.com/2012/11/10/unit-testing-and-integration-testing-using-junit-liquibase-hsqldb-hibernate-maven-and-spring-framework/
    @Override
    @Bean(destroyMethod = "close")
    public DataSource mainDataSource() {
        BoneCPDataSource inMemoryDataSource = new BoneCPDataSource();
        inMemoryDataSource.setDriverClass("org.h2.Driver");
        inMemoryDataSource.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"); //See http://stackoverflow.com/questions/5763747/h2-in-memory-database-table-not-found
        inMemoryDataSource.setUsername("SA");
        inMemoryDataSource.setPassword("");

        return inMemoryDataSource;

    }

//    @Override
//    @Bean
//    public DataSource mainDataSource() {
//
//        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
//                    .build();
//    }

    @Override
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.H2);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public Properties additionalJPAProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", H2Dialect.class.getCanonicalName());
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return jpaProperties;
    }

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(mainDataSource());
        liquibase.setChangeLog("/testdata/db.changelog-master.xml");
        return liquibase;
    }
}