package com.bjh.config;

import com.bjh.filter.LoginCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 原生Servlet的配置类:
 */
@Configuration
public class ServletConfig {

    //注入redis模板
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 注册原生Servlet的Filter
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        //创建filterRegistrationBean的bean对象
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //自定义的过滤器
        LoginCheckFilter loginCheckFilter = new LoginCheckFilter();
        //注入redis模板
        loginCheckFilter.setRedisTemplate(redisTemplate);
        //自定义的过滤器注册到FilterRegistrationBean中
        filterRegistrationBean.setFilter(loginCheckFilter);
        //配置loginCheckFilter拦截所有请求
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }
}
