package com.heropicker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoleStatsDTO {
	@JsonProperty("HeroesPerRole")
	private double heroesPerRole;
	@JsonProperty("ScoreLossPercentage")
	private double scoreLossPercentage;

	public RoleStatsDTO() {
		super();
	}

	public double getHeroesPerRole() {
		return heroesPerRole;
	}

	public void setHeroesPerRole(double heroesPerRole) {
		this.heroesPerRole = heroesPerRole;
	}

	public double getScoreLossPercentage() {
		return scoreLossPercentage;
	}

	public void setScoreLossPercentage(double scoreLossPercentage) {
		this.scoreLossPercentage = scoreLossPercentage;
	}

}
