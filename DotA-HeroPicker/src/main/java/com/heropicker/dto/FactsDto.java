package com.heropicker.dto;

import java.util.ArrayList;

import com.heropicker.facts.Fact;
import com.heropicker.facts.heroes.AllyHeroPickedFact;
import com.heropicker.facts.heroes.EnemyHeroPickedFact;
import com.heropicker.facts.heroes.HeroBannedFact;
import com.heropicker.facts.heroes.HeroPreferredFact;
import com.heropicker.facts.heroes.LanePreferredFact;
import com.heropicker.facts.heroes.RolePreferredFact;

public class FactsDto {

	private ArrayList<String> heroPreferences;
	private ArrayList<String> lanePreferences;
	private ArrayList<String> rolePreferences;
	private ArrayList<String> allies;
	private ArrayList<Boolean> enemyLanes;
	private ArrayList<String> enemies;
	private ArrayList<String> banned;

	public FactsDto() {

	}

	public ArrayList<String> getHeroPreferences() {
		return heroPreferences;
	}

	public void setHeroPreferences(ArrayList<String> heroPreferences) {
		this.heroPreferences = heroPreferences;
	}

	public ArrayList<String> getLanePreferences() {
		return lanePreferences;
	}

	public void setLanePreferences(ArrayList<String> lanePreferences) {
		this.lanePreferences = lanePreferences;
	}

	public ArrayList<String> getRolePreferences() {
		return rolePreferences;
	}

	public void setRolePreferences(ArrayList<String> rolePreferences) {
		this.rolePreferences = rolePreferences;
	}

	public ArrayList<String> getAllies() {
		return allies;
	}

	public void setAllies(ArrayList<String> allies) {
		this.allies = allies;
	}

	public ArrayList<String> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<String> enemies) {
		this.enemies = enemies;
	}

	public ArrayList<String> getBanned() {
		return banned;
	}

	public void setBanned(ArrayList<String> banned) {
		this.banned = banned;
	}

	public ArrayList<Boolean> getEnemyLanes() {
		return enemyLanes;
	}

	public void setEnemyLanes(ArrayList<Boolean> enemyLanes) {
		this.enemyLanes = enemyLanes;
	}

	public ArrayList<Fact> toFacts() {
		ArrayList<Fact> retVal = new ArrayList<>();
		for (String allyId : allies) {
			retVal.add(new AllyHeroPickedFact(allyId));
		}
		for (int i = 0; i < enemies.size(); i++) {
			retVal.add(new EnemyHeroPickedFact(enemies.get(i), enemyLanes.get(i)));
		}
		for (String perfId : heroPreferences) {
			retVal.add(new HeroPreferredFact(perfId));
		}
		for (String bannedId : banned) {
			retVal.add(new HeroBannedFact(bannedId));
		}
		for (String role : rolePreferences) {
			retVal.add(new RolePreferredFact(role));
		}
		for (String lane : lanePreferences) {
			retVal.add(new LanePreferredFact(lane));
		}
		return retVal;
	}

}
