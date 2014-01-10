package com.qualica.liquibasejunit.springconfig;

import com.qualica.liquibasejunit.springconfig.profiles.ApplicationProfiles;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;

import java.util.Properties;

/**
 * Created by katlego on 1/9/14.
 */
@Configuration
@Profile(ApplicationProfiles.DEFAULT)
public class DatabaseConfig {

    @Bean(destroyMethod = "close")
    public DataSource mainDataSource() {
        //BoneCP configuration
        BoneCPDataSource mainDataSource = new BoneCPDataSource();
        mainDataSource.setDriverClass("com.mysql.jdbc.Driver");
        mainDataSource.setJdbcUrl("jdbc:mysql://localhost/liquibasejunit?characterEncoding=utf8");
        mainDataSource.setUsername("flexifin");
        mainDataSource.setPassword("flexifin");
        mainDataSource.setMaxConnectionsPerPartition(60);
        mainDataSource.setMinConnectionsPerPartition(20);
        mainDataSource.setPartitionCount(3);
        mainDataSource.setAcquireIncrement(10);
        mainDataSource.setStatementsCacheSize(50);
        return mainDataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public Properties additionalJPAProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", MySQL5Dialect.class.getCanonicalName());
        return jpaProperties;
    }

    @Bean( )
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();

        liquibase.setDataSource(mainDataSource());
        liquibase.setChangeLog("/database/db.changelog-master.xml");
        return liquibase;
    }
}
