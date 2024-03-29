package com.heropicker.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heropicker.dto.SettingsStatsDTO;
import com.heropicker.io.RuleFileManager;

@Service
public class SettingsStatsService {
	
	@Autowired
	private RuleManagerService ruleManagerService;

	private String path = System.getProperty("user.dir") + "/src/main/resources/rules/integration/";

	public SettingsStatsDTO getSettingsStats() {
		SettingsStatsDTO retval = new SettingsStatsDTO();
		try {
			String userAddedRules = RuleFileManager.loadRuleFile("UserAddedRules.drl");
			String settingsStats = new Scanner(new File(path + "template_params/settingsStats.json")).useDelimiter("\\Z").next();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			retval = gson.fromJson(settingsStats, SettingsStatsDTO.class);
			retval.setUserAddedRules(userAddedRules);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public SettingsStatsDTO getDefaultSettingsStats() {
		SettingsStatsDTO retval = new SettingsStatsDTO();
		try {
			String settingsStats = new Scanner(new File(path + "template_params/defaultSettingsStats.json")).useDelimiter("\\Z").next();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			retval = gson.fromJson(settingsStats, SettingsStatsDTO.class);
			retval.setUserAddedRules(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public String postSettingsStats(SettingsStatsDTO settingsStats) {
		try {
			Files.write(Paths.get(path + "rules/UserAddedRules.drl"), settingsStats.getUserAddedRules().getBytes());
			GsonBuilder builder = (new GsonBuilder()).setPrettyPrinting();
			Gson gson = builder.create();
			Files.write(Paths.get(path + "template_params/settingsStats.json"), gson.toJson(settingsStats).getBytes());
			
			ruleManagerService.exportChangedRules(settingsStats);
			ruleManagerService.triggerMvnInstall();
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Server error";
		}
		return "Saved successfully";
	}


}
