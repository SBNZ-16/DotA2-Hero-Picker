package com.heropicker.facts.heroes;

import com.heropicker.facts.Fact;

public class AllyHeroPickedFact extends Fact {
	public String heroId;

	public AllyHeroPickedFact() {
		super();
	}

	public AllyHeroPickedFact(String heroId) {
		this.heroId = heroId;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

}
