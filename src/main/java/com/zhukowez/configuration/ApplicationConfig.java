package com.zhukowez.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.zhukowez.repository", "com.zhukowez.configuration",
        "com.zhukowez.service", "com.zhukowez.aspect"})
@PropertySource("classpath:database.properties")
public class ApplicationConfig {


}
