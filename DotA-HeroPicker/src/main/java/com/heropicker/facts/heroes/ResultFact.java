package com.heropicker.facts.heroes;

public class ResultFact {
	
	private double score;
	private String heroId;
	private String heroName;
	
	public ResultFact(double score, String heroId, String heroName) {
		super();
		this.score = score;
		this.heroId = heroId;
		this.heroName = heroName;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
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
	
}
