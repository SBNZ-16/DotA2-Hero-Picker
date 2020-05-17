package com.heropicker.facts;

public class HeroPickedFact {
	
	public static int ALLY_PICK = 0;
	public static int ENEMY_PICK = 1;
	
	private String heroId;
	private int team;
	
	public HeroPickedFact() {
		
	}
	
	public HeroPickedFact(String hero_id, int team) {
		this.heroId = hero_id;
		this.team = team;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

}
