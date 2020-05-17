package com.heropicker.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import com.heropicker.model.Hero;
import com.heropicker.model.HeroDatabase;

public class HeroLoader {

	public static HeroDatabase loadHeroDatabase() throws Exception {
		String mappingsPath = System.getProperty("user.dir") + "/src/main/resources/data/mappings.json";
    	JSONParser parser = new JSONParser(new FileReader(new File(mappingsPath)));
    	ArrayList<ArrayList<String>> mappingList = (ArrayList<ArrayList<String>>) parser.parse();
    	
    	Map<String, String> nameToId = new HashMap<String, String>();
		
    	HeroDatabase retVal = new HeroDatabase();
    	
    	for (ArrayList<String> heroJSON: mappingList) {
    		Hero hero = new Hero(heroJSON.get(0), heroJSON.get(1));
    		retVal.getHeroes().add(hero);
    		retVal.getHeroesMap().put(hero.getHeroId(), hero);
    		nameToId.put(hero.getHeroName(), hero.getHeroId());
    	}
    	
    	for (Hero hero: retVal.getHeroes()) {
    		loadHeroInfo(hero, nameToId);
    	}
    	return retVal;
	}
	
	
	private static void loadHeroInfo(Hero hero, Map<String, String> nameToId) throws Exception {
		String heroFilePath = System.getProperty("user.dir") +
				"/src/main/resources/data/hero_specific_data/" + hero.getHeroId() + ".json";
		JSONParser parser = new JSONParser(new FileReader(new File(heroFilePath)));
		Map<String, Object> heroMap = (Map<String, Object>) parser.parse();
		
		hero.setAttackType((String) heroMap.get("attack_type"));
		hero.setOverallWinrate(percentageStringToDouble((String) heroMap.get("overall_winrate")));
		hero.setPrimaryAttribute((String)heroMap.get("primary_attribute"));
		
		for (String role: (ArrayList<String>)heroMap.get("roles")) {
			hero.getRoles().add(role);
		}
		
		Map<String, String> winPercentageMap = (Map<String, String>) heroMap.get("win_rates");
		for (Map.Entry<String, String> entry: winPercentageMap.entrySet()) {
			double winPercentage = percentageStringToDouble(entry.getValue());
			String enemyId = nameToId.get(entry.getKey());
			hero.getWinPercentages().put(enemyId, winPercentage);
		}
		
		Map<String, String> disadvantagesMap = (Map<String, String>) heroMap.get("disadvantages");
		for (Map.Entry<String, String> entry: disadvantagesMap.entrySet()) {
			double disadvantage = percentageStringToDouble(entry.getValue());
			String enemyId = nameToId.get(entry.getKey());
			hero.getDisadvantages().put(enemyId, disadvantage);
		}
	}
	
	private static double percentageStringToDouble(String dirtyString) {
		String cleanString = dirtyString.substring(0, dirtyString.length() - 1);
		return Double.parseDouble(cleanString);
	}
	
	
	
	
}
