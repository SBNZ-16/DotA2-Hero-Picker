package com.heropicker.model.heroes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HeroDatabase {
	
	private ArrayList<Hero> heroes;
	private Map<String, Hero> heroesMap;
	
	public HeroDatabase() {
		this.heroesMap = new HashMap<String, Hero>();
		this.heroes = new ArrayList<Hero>();
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}
	
	public Hero getById(String heroId) {
		return heroesMap.get(heroId);
	}
	
	public Hero getByName(String heroName) {
		for (Hero hero: this.heroes) {
			if (hero.getHeroName().equals(heroName)) {
				return hero;
			}
		}
		return null; //wont happen 
	}

	public Map<String, Hero> getHeroesMap() {
		return heroesMap;
	}
	
	
}
