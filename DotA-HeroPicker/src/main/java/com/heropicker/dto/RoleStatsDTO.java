package com.heropicker.dto;

public class RoleStatsDTO {
	private double heroesPerRole;
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
