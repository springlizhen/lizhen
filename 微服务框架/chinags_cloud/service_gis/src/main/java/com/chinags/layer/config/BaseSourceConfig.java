package com.chinags.layer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryBase",
        transactionManagerRef = "transactionManagerBase",
        basePackages = {"com.chinags.layer.dao.base"}
)
public class BaseSourceConfig {

    @Autowired
    @Qualifier("baseDataSource")
    private DataSource baseDataSource;

    @Bean(name = "entityManagerBase")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryBase(builder).getObject().createEntityManager();
    }

    @Resource
    private Properties jpaProperties;

    @Bean(name = "entityManagerFactoryBase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBase(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder
                .dataSource(baseDataSource)
                .packages("com.chinags.layer.entity.base") //设置实体类所在位置
                .persistenceUnit("basePersistenceUnit")
                .build();
        entityManagerFactory.setJpaProperties(jpaProperties);
        return entityManagerFactory;
    }

    @Bean(name = "transactionManagerBase")
    public PlatformTransactionManager transactionManagerBase(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryBase(builder).getObject());
    }
}
