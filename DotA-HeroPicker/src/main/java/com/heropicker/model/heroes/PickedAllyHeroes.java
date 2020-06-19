package com.heropicker.model.heroes;

import java.util.ArrayList;

public class PickedAllyHeroes {
	private ArrayList<Hero> heroes;
	private HeroDatabase heroDatabase;

	public PickedAllyHeroes() {
		super();
		this.heroes = new ArrayList<Hero>();
	}

	public PickedAllyHeroes(HeroDatabase heroDatabase) {
		super();
		this.heroDatabase = heroDatabase;
		this.heroes = new ArrayList<Hero>();
	}

	public void addHero(String heroId) {
		this.heroes.add(this.heroDatabase.getHeroes().stream().filter(hero -> hero.getHeroId().equals(heroId))
				.findFirst().get());
	}

	public ArrayList<Hero> getHeroes() {
		return heroes;
	}

	public void setHeroes(ArrayList<Hero> heroes) {
		this.heroes = heroes;
	}

	public HeroDatabase getHeroDatabase() {
		return heroDatabase;
	}

	public void setHeroDatabase(HeroDatabase heroDatabase) {
		this.heroDatabase = heroDatabase;
	}

}
