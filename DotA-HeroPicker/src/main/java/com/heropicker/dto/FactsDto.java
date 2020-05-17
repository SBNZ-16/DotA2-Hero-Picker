package com.heropicker.dto;

import java.util.ArrayList;

import com.heropicker.facts.AllyHeroPickedFact;
import com.heropicker.facts.EnemyHeroPickedFact;
import com.heropicker.facts.Fact;
import com.heropicker.facts.HeroPreferredFact;

public class FactsDto {

	private ArrayList<String> heroPreferences;
	private ArrayList<String> lanePreferences;
	private ArrayList<String> rolePreferences;
	private ArrayList<String> allies;
	private ArrayList<String> enemies;
	
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
	
	public ArrayList<Fact> toFacts() {
		ArrayList<Fact> retVal = new ArrayList<>();
		for (String allyId: allies) {
			retVal.add(new AllyHeroPickedFact(allyId));
		}
		for (String enemyId: enemies) {
			retVal.add(new EnemyHeroPickedFact(enemyId, false));
		}
		for (String perfId: heroPreferences) {
			retVal.add(new HeroPreferredFact(perfId));
		}
		return retVal;
	}
	
}
