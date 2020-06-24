package com.heropicker.model.heroes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.heropicker.facts.heroes.HeroRecommendationFact;

public class HeroDatabase {

	private ArrayList<Hero> heroes;

	public HeroDatabase() {
		this.heroes = new ArrayList<Hero>();
	}

	public ArrayList<HeroRecommendationFact> generateHeroRecommendationFacts() {
		ArrayList<HeroRecommendationFact> retval = new ArrayList<HeroRecommendationFact>();
		for (Hero hero : heroes) {
			retval.add(new HeroRecommendationFact(hero, hero.getOverallWinrate()));
		}
		return retval;
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}

	public Hero getByName(String heroName) {
		for (Hero hero : this.heroes) {
			if (hero.getHeroName().equals(heroName)) {
				return hero;
			}
		}
		return null; // wont happen
	}
}
