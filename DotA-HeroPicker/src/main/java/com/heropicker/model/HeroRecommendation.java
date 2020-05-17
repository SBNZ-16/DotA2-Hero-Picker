package com.heropicker.model;

public class HeroRecommendation {

	private String heroId;
	private String heroName;
	private double score;
	private boolean pickable;

	public HeroRecommendation() {

	}

	public HeroRecommendation(String heroId, String heroName, double score) {
		super();
		this.heroId = heroId;
		this.heroName = heroName;
		this.score = score;
		this.pickable = true;
	}

	public void updateScore(double multiplyScale, double addValue) {
		this.score=this.score*multiplyScale + addValue;
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

	public boolean isPickable() {
		return pickable;
	}

	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}

}
