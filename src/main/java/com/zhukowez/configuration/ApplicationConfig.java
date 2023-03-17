package com.zhukowez.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhukowez.repository.impl.AthleteRepositoryImpl;
import com.zhukowez.repository.rowmapper.AthleteRowMapper;
import com.zhukowez.util.RandomValuesGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.Random;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.zhukowez.repository", "com.zhukowez.configuration",
        "com.zhukowez.service", "com.zhukowez.aspect"})
@PropertySource("classpath:database.properties")
public class ApplicationConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DatabaseProperties databaseProperties() {
        return new DatabaseProperties();
    }

    @Bean
    public DataSource hikariDatasource(DatabaseProperties properties) {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setUsername(properties.getLogin());
        hikariDataSource.setPassword(properties.getPassword());
        hikariDataSource.setDriverClassName(properties.getDriverName());
        hikariDataSource.setMaximumPoolSize(properties.getPoolSize());
        hikariDataSource.setJdbcUrl(properties.getJdbcUrl());

        hikariDataSource.addDataSourceProperty("cachePrepStmts", "true");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return hikariDataSource;
    }

    @Bean
    public RandomValuesGenerator getRandomGenerator() {
        return new RandomValuesGenerator();
    }

    @Bean
    public AthleteRowMapper athleteRowMapper() {
        return new AthleteRowMapper();
    }

    @Bean
    public AthleteRepositoryImpl athleteRepositoryImpl(JdbcTemplate jdbcTemplate, AthleteRowMapper athleteRowMapper) {
        return new AthleteRepositoryImpl(jdbcTemplate, athleteRowMapper);
    }

}
