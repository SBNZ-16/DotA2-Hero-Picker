package com.heropicker.facts;

public class HeroPreferredFact extends Fact {
	public String heroId;

	public HeroPreferredFact() {
		super();
	}

	public HeroPreferredFact(String heroId) {
		this.heroId = heroId;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

}
