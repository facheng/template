package com.template.framework.filter;

/**
 * Created by feng on 2019/3/6.
 */
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestApiLoggingConfig {

//    @Bean
    RestApiLoggingFilter restApiLoggingFilter(BeanFactory beanFactory) {
        return new RestApiLoggingFilter();
    }
}

