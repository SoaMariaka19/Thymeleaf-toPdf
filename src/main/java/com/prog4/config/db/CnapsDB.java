package com.prog4.config.db;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "cnapsEntityManagerFactory",
        basePackages = {
                "com.prog4.cnaps_db.repositories"
        },
        transactionManagerRef= "cnapsTransactionManager"
)
public class CnapsDB {

    @Bean
    @ConfigurationProperties("spring.cnaps.datasource")
    public DataSourceProperties cnapsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.cnaps.datasource.configuration")
    public DataSource cnapsDataSource() {
        return cnapsDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "cnapsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean cnapsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(cnapsDataSource())
                .packages("com.prog4.cnaps_db.entity")
                .build();
    }

    @Bean
    public PlatformTransactionManager cnapsTransactionManager(
            final @Qualifier("cnapsEntityManagerFactory") LocalContainerEntityManagerFactoryBean cnapsEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(cnapsEntityManagerFactory.getObject()));
    }
}
