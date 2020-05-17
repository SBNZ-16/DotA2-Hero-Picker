package com.heropicker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Hero {
	
	
	private String heroId;
	private String heroName;
	private String attackType;
	private ArrayList<String> roles;
	private double overallWinrate;
	private String primaryAttribute;	
	private Map<String, Double> winPercentages;
	
	public Hero() {
		this.winPercentages = new HashMap<String, Double>();
		this.roles = new ArrayList<String>();
	}
	
	public Hero(String heroId, String heroName) {
		this.winPercentages = new HashMap<String, Double>();
		this.roles = new ArrayList<String>();
		this.heroId = heroId;
		this.heroName = heroName;
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

	public Map<String, Double> getWinPercentages() {
		return winPercentages;
	}

	public String getAttackType() {
		return attackType;
	}

	public void setAttackType(String attackType) {
		this.attackType = attackType;
	}

	public double getOverallWinrate() {
		return overallWinrate;
	}

	public void setOverallWinrate(double overallWinrate) {
		this.overallWinrate = overallWinrate;
	}

	public String getPrimaryAttribute() {
		return primaryAttribute;
	}

	public void setPrimaryAttribute(String primaryAttribute) {
		this.primaryAttribute = primaryAttribute;
	}

	public ArrayList<String> getRoles() {
		return roles;
	}
	
	
}
