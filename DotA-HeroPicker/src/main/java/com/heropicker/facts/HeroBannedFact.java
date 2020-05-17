package com.heropicker.facts;

public class HeroBannedFact extends Fact {
	public String heroId;

	public HeroBannedFact() {
		super();
	}

	public HeroBannedFact(String heroId) {
		this.heroId = heroId;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

}
