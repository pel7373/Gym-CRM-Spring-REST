package org.gym.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class DbConfig {

    @Bean
    public Properties properties(@Value("${hibernate.hbm2ddl.auto}") String hbm2ddlAuto,
                                    @Value("${hibernate.dialect}") String hibernateDialect,
                                    @Value("${hibernate.show_sql}") String showSql,
                                    @Value("${hibernate.format_sql}") String formatSql,
                                    @Value("${hibernate.jdbc.lob.non_contextual_creation}") String lobCreation,
                                    @Value("${jakarta.persistence.sql-load-script-source}") String loadScriptSource,
                                    @Value("${hibernate.hbm2ddl.import_files}") String hibernateHbm2ddlImportFiles
    ) {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.show_sql", showSql);
        properties.setProperty("hibernate.format_sql", formatSql);
        properties.setProperty("hibernate.jdbc.lob.non_contextual_creation", lobCreation);
        properties.setProperty("jakarta.persistence.sql-load-script-source", loadScriptSource);
        properties.setProperty("hibernate.hbm2ddl.import_files", hibernateHbm2ddlImportFiles);
        return properties;
    }

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.url}") String dataSourceUrl,
                                @Value("${spring.datasource.username}") String dataSourceUsername,
                                @Value("${spring.datasource.password}") String dataSourcePassword) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);
        return dataSource;
    }

    @Bean
    public EntityManager entityManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        EntityManagerFactory entityManagerFactory = localContainerEntityManagerFactoryBean.getObject();
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                       @Qualifier("properties") Properties properties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("org.gym.entity");
        em.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        em.setJpaProperties(properties);
        return em;
    }

    @Bean
    public TransactionManager transactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        EntityManagerFactory entityManagerFactory = localContainerEntityManagerFactoryBean.getObject();
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }
        return new JpaTransactionManager(entityManagerFactory);
    }
}
