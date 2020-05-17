package com.heropicker.model;

public class HeroRecommendation {
	
	private String heroId;
	private String heroName;
	private double score;
	
	public HeroRecommendation() {
		
	}
	
	public HeroRecommendation(String heroId, String heroName, double score) {
		this.heroId = heroId;
		this.heroName = heroName;
		this.score = score;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

	public String getHeroName() {
		return heroName;
	}

	public void setHeroName(String heroName) {
		this.heroName = heroName;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	
	


}
