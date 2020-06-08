package com.heropicker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsStatsDTO {
	@JsonProperty("UserAddedRules")
	private String userAddedRules;
	@JsonProperty("EnemyHeroDisadvantage")
	private double enemyHeroDisadvantage;
	@JsonProperty("EnemyLaneHeroDisadvantage")
	private double enemyLaneHeroDisadvantage;
	@JsonProperty("PreferredHeroFactor")
	private double preferredHeroFactor;
	@JsonProperty("PreferredLaneFactor")
	private double preferredLaneFactor;
	@JsonProperty("PreferredRoleFactor")
	private double preferredRoleFactor;
	@JsonProperty("Nuker")
	private RoleStatsDTO nuker;
	@JsonProperty("Escape")
	private RoleStatsDTO escape;
	@JsonProperty("Support")
	private RoleStatsDTO support;
	@JsonProperty("Initiator")
	private RoleStatsDTO initiator;
	@JsonProperty("Carry")
	private RoleStatsDTO carry;
	@JsonProperty("Jungler")
	private RoleStatsDTO jungler;
	@JsonProperty("Durable")
	private RoleStatsDTO durable;
	@JsonProperty("Disabler")
	private RoleStatsDTO disabler;
	@JsonProperty("Pusher")
	private RoleStatsDTO pusher;

	public SettingsStatsDTO() {
		super();
	}

	public String getUserAddedRules() {
		return userAddedRules;
	}

	public void setUserAddedRules(String userAddedRules) {
		this.userAddedRules = userAddedRules;
	}

	public double getEnemyHeroDisadvantage() {
		return enemyHeroDisadvantage;
	}

	public void setEnemyHeroDisadvantage(double enemyHeroDisadvantage) {
		this.enemyHeroDisadvantage = enemyHeroDisadvantage;
	}

	public double getEnemyLaneHeroDisadvantage() {
		return enemyLaneHeroDisadvantage;
	}

	public void setEnemyLaneHeroDisadvantage(double enemyLaneHeroDisadvantage) {
		this.enemyLaneHeroDisadvantage = enemyLaneHeroDisadvantage;
	}

	public double getPreferredHeroFactor() {
		return preferredHeroFactor;
	}

	public void setPreferredHeroFactor(double preferredHeroFactor) {
		this.preferredHeroFactor = preferredHeroFactor;
	}

	public double getPreferredLaneFactor() {
		return preferredLaneFactor;
	}

	public void setPreferredLaneFactor(double preferredLaneFactor) {
		this.preferredLaneFactor = preferredLaneFactor;
	}

	public double getPreferredRoleFactor() {
		return preferredRoleFactor;
	}

	public void setPreferredRoleFactor(double preferredRoleFactor) {
		this.preferredRoleFactor = preferredRoleFactor;
	}

	public RoleStatsDTO getNuker() {
		return nuker;
	}

	public void setNuker(RoleStatsDTO nuker) {
		this.nuker = nuker;
	}

	public RoleStatsDTO getEscape() {
		return escape;
	}

	public void setEscape(RoleStatsDTO escape) {
		this.escape = escape;
	}

	public RoleStatsDTO getSupport() {
		return support;
	}

	public void setSupport(RoleStatsDTO support) {
		this.support = support;
	}

	public RoleStatsDTO getInitiator() {
		return initiator;
	}

	public void setInitiator(RoleStatsDTO initiator) {
		this.initiator = initiator;
	}

	public RoleStatsDTO getCarry() {
		return carry;
	}

	public void setCarry(RoleStatsDTO carry) {
		this.carry = carry;
	}

	public RoleStatsDTO getJungler() {
		return jungler;
	}

	public void setJungler(RoleStatsDTO jungler) {
		this.jungler = jungler;
	}

	public RoleStatsDTO getDurable() {
		return durable;
	}

	public void setDurable(RoleStatsDTO durable) {
		this.durable = durable;
	}

	public RoleStatsDTO getDisabler() {
		return disabler;
	}

	public void setDisabler(RoleStatsDTO disabler) {
		this.disabler = disabler;
	}

	public RoleStatsDTO getPusher() {
		return pusher;
	}

	public void setPusher(RoleStatsDTO pusher) {
		this.pusher = pusher;
	}

}
