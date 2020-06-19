package com.heropicker.model.heroes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class HeroRecommendationList {

	private Map<String, HeroRecommendation> heroRecommendationsMap;

	private HeroDatabase heroDatabase;

	public HeroRecommendationList(HeroDatabase heroDatabase) {
		this.heroDatabase = heroDatabase;
		this.heroRecommendationsMap = new HashMap<String, HeroRecommendation>();
		for (Hero hero : heroDatabase.getHeroes()) {
			HeroRecommendation heroRecommendation = new HeroRecommendation(hero, hero.getOverallWinrate());
			this.heroRecommendationsMap.put(hero.getHeroId(), heroRecommendation);
		}
	}

	public HeroRecommendation findHeroRecommendationByHeroId(String heroId) {
		return heroRecommendationsMap.get(heroId);
	}

	public int getHeroRank(String heroId) {
		ArrayList<HeroRecommendation> heroRecommendations = new ArrayList<>(this.heroRecommendationsMap.values());
		heroRecommendations.sort(new Comparator<HeroRecommendation>() {
			@Override
			public int compare(HeroRecommendation o1, HeroRecommendation o2) {
				return -Double.compare(o1.getScore(), o2.getScore());
			}
		});
		return heroRecommendations.indexOf(heroRecommendationsMap.get(heroId));
	}

	public HeroRecommendation removeHeroRecommendationByHeroId(String heroId) {
		return heroRecommendationsMap.remove(heroId);
	}

	public void changeScoresByHeroDisadvantage(String enemyId, double scalingFactor) {
		Hero enemy = this.heroDatabase.getById(enemyId);
		for (Map.Entry<String, Double> entry : enemy.getDisadvantages().entrySet()) {
			HeroRecommendation heroRecommendation = this.findHeroRecommendationByHeroId(entry.getKey());
			if (heroRecommendation != null)
				heroRecommendation.setScore(heroRecommendation.getScore() + entry.getValue() * scalingFactor);
		}
	}

	public void displayReccomendations(int countToDisplay) {
		ArrayList<HeroRecommendation> heroRecommendations = new ArrayList<>(this.heroRecommendationsMap.values());
		heroRecommendations.sort(new Comparator<HeroRecommendation>() {
			@Override
			public int compare(HeroRecommendation o1, HeroRecommendation o2) {
				return -Double.compare(o1.getScore(), o2.getScore());
			}
		});
		for (int i = 0; i < countToDisplay; i++) {
			HeroRecommendation heroRecommendation = heroRecommendations.get(i);
			System.out.println(i + 1 + ". " + heroRecommendation.getHero().getHeroName() + ", score: "
					+ heroRecommendation.getScore());
		}
	}

	public ArrayList<HeroRecommendation> getHeroRecommendations() {
		ArrayList<HeroRecommendation> heroReccomendations = new ArrayList<>(this.heroRecommendationsMap.values());
		heroReccomendations.sort(new Comparator<HeroRecommendation>() {
			@Override
			public int compare(HeroRecommendation o1, HeroRecommendation o2) {
				return -Double.compare(o1.getScore(), o2.getScore());
			}
		});
		return heroReccomendations;
	}

	public void scaleScore(double minValue, double maxValue) {
		double minScore = heroRecommendationsMap.values().stream().map(HeroRecommendation::getScore)
				.min(Double::compare).get();
		double maxScore = heroRecommendationsMap.values().stream().map(HeroRecommendation::getScore)
				.max(Double::compare).get();
		double scoreRange = maxScore - minScore;
		double newValueRange = maxValue - minValue;
		heroRecommendationsMap.values().forEach(heroRecommendation -> heroRecommendation
				.setScore((heroRecommendation.getScore() - minScore) / scoreRange * newValueRange + minValue));
	}

}
