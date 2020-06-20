package com.heropicker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.heropicker.interceptor.DdosInterceptor;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private DdosInterceptor ddosInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ddosInterceptor);
	}

}
