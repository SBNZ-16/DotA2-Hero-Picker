package com.heropicker.facts;

public abstract class Fact {
	public String heroId;

	public Fact() {
		super();
	}

	public Fact(String heroId) {
		super();
		this.heroId = heroId;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

}
