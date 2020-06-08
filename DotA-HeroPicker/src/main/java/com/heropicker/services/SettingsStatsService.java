package com.heropicker.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heropicker.dto.SettingsStatsDTO;

@Service
public class SettingsStatsService {

	private String path = System.getProperty("user.dir") + "/src/main/resources/rules/";

	public SettingsStatsDTO getSettingsStats() {
		SettingsStatsDTO retval = new SettingsStatsDTO();
		try {
			String rulesTemplate = new Scanner(new File(path + "rules.drt")).useDelimiter("\\Z").next();
			String vanillaRulesTemplate = new Scanner(new File(path + "vanilla.drt")).useDelimiter("\\Z").next();
			String settingsStats = new Scanner(new File(path + "settingsStats.json")).useDelimiter("\\Z").next();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			retval = gson.fromJson(settingsStats, SettingsStatsDTO.class);
			retval.setRulesTemplate(rulesTemplate);
			retval.setVanillaRulesTemplate(vanillaRulesTemplate);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return retval;
	}

	public String postSettingsStats(SettingsStatsDTO settingsStats) {
		try {
			Files.write(Paths.get(path + "rules.drt"), settingsStats.getRulesTemplate().getBytes());
			GsonBuilder builder = (new GsonBuilder()).setPrettyPrinting();
			Gson gson = builder.create();
			Files.write(Paths.get(path + "settingsStats.json"), gson.toJson(settingsStats).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return "Server error";
		}
		return "Saved successfully";
	}
}