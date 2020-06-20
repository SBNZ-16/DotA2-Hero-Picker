package com.heropicker.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.heropicker.facts.ddos.IpAccessFact;

@Component
public class DdosInterceptor extends HandlerInterceptorAdapter {
	
	
	@Autowired
	private KieContainer kieContainer;
	
	private KieSession kieSession;
	
	private void createKieSessionIfNull() {
		if (kieSession == null) {
			kieSession = kieContainer.newKieSession("ddos-ksession");
		}
	}
	
	private String extractIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null) {  
		    ipAddress = request.getRemoteAddr();  
		}
		return ipAddress;
	}
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		createKieSessionIfNull();
		String ipAddress = extractIpAddress(request);
		IpAccessFact fact = new IpAccessFact(ipAddress);
		kieSession.insert(fact);
		kieSession.fireAllRules();
		return true;
    }
	
	
}
