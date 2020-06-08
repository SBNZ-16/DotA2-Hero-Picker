package com.heropicker;

import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heropicker.dto.RoleStatsDTO;
import com.heropicker.dto.SettingsStatsDTO;

@SpringBootApplication
public class SpringApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringApp.class, args);
	}

}