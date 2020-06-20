package com.heropicker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heropicker.dto.RoleStatsDTO;
import com.heropicker.dto.SettingsStatsDTO;
import com.heropicker.service.RuleManagerService;

@SpringBootApplication
public class SpringApp implements CommandLineRunner {
	
	@Autowired
	private RuleManagerService ruleManagerService;
	

	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ruleManagerService.exportDdosRules();
		ruleManagerService.triggerMvnInstall();
	}

}