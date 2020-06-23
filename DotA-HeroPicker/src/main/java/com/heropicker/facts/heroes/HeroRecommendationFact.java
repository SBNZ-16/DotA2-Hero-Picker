package com.heropicker.facts.heroes;

import com.heropicker.facts.Fact;
import com.heropicker.model.heroes.Hero;

public class HeroRecommendationFact extends Fact {

	private Hero hero;
	private double score;

	public HeroRecommendationFact() {

	}

	public HeroRecommendationFact(Hero hero, double score) {
		super();
		this.hero = hero;
		this.score = score;
	}

	public void updateScore(double multiplyScale, double addValue) {
		this.score = this.score * multiplyScale + addValue;
	}

	public Hero getHero() {
		return hero;
	}

	public void setHero(Hero hero) {
		this.hero = hero;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

}
