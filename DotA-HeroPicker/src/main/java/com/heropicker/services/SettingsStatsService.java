package com.heropicker.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heropicker.dto.SettingsStatsDTO;

@Service
public class SettingsStatsService {

	public SettingsStatsDTO getSettingsStats() {
		SettingsStatsDTO retval = new SettingsStatsDTO();
		String path = System.getProperty("user.dir") + "/src/main/resources/rules/";
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
}
