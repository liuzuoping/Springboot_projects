package cn.itxiaoliu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "cn.itxiaoliu.dao1",entityManagerFactoryRef ="localContainerEntityManagerFactoryBean1",
transactionManagerRef = "platformTransactionManager1")
public class JpaConfig1 {
    @Autowired
    @Qualifier("dsOne")
    DataSource dsOne;
    @Autowired
    JpaProperties jpaProperties;
    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean1(EntityManagerFactoryBuilder builder){
        return builder.dataSource(dsOne)
                .properties(jpaProperties.getProperties())
                .persistenceUnit("pu1")
                .packages("cn.itxiaoliu.bean")
                .build();
    }
    @Bean
    PlatformTransactionManager platformTransactionManager1(EntityManagerFactoryBuilder builder){
        return new JpaTransactionManager(localContainerEntityManagerFactoryBean1(builder).getObject());
    }
}
