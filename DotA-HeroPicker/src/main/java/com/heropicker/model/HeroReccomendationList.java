package com.heropicker.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class HeroReccomendationList {
	
	private ArrayList<HeroRecommendation> heroReccomendations;
	private Map<String, HeroRecommendation> heroRecommendationsMap;
	
	private HeroDatabase heroDatabase;
	
	public HeroReccomendationList(HeroDatabase heroDatabase) {
		this.heroDatabase = heroDatabase;
		this.heroReccomendations = new ArrayList<HeroRecommendation>();
		this.heroRecommendationsMap = new HashMap<String, HeroRecommendation>();
		for (Hero hero: heroDatabase.getHeroes()) {
			HeroRecommendation heroRecommendation = new HeroRecommendation(hero.getHeroId(), hero.getHeroName(), 100.0);
			this.heroReccomendations.add(heroRecommendation);
			this.heroRecommendationsMap.put(hero.getHeroId(), heroRecommendation);
		}
	}
	
	
	public HeroRecommendation findHeroRecommendationByHeroId(String heroId) {
		return heroRecommendationsMap.get(heroId);
	}
	
	public void markHeroAsTaken(String heroId) {
		HeroRecommendation heroRecommendation = findHeroRecommendationByHeroId(heroId);
		heroRecommendation.setScore(0);
	}
	
	public void changeScoresOnEnemyPicked(String enemyId) {
		Hero enemy = this.heroDatabase.getById(enemyId);
		for (Map.Entry<String, Double> entry : enemy.getWinPercentages().entrySet()) {
			HeroRecommendation heroRecommendation = this.findHeroRecommendationByHeroId(entry.getKey());
			if (heroRecommendation.getScore() != 0) {
				heroRecommendation.setScore(heroRecommendation.getScore() - (entry.getValue() - 50));
			}
		}
	}
	
	
	public void sortRecommendations() {
		this.heroReccomendations.sort(new Comparator<HeroRecommendation>() {
			@Override
			public int compare(HeroRecommendation o1, HeroRecommendation o2) {
				return - Double.compare(o1.getScore(), o2.getScore());
			}
		});
	}
	
	public void displayReccomendations(int countToDisplay) {
		for (int i = 0; i < countToDisplay; i++) {
			HeroRecommendation heroRecommendation = this.heroReccomendations.get(i);
			System.out.println(i + ". " + heroRecommendation.getHeroName() + ", score: " + heroRecommendation.getScore());
		}
	}


	public ArrayList<HeroRecommendation> getHeroReccomendations() {
		return heroReccomendations;
	}
	
	
	
}
