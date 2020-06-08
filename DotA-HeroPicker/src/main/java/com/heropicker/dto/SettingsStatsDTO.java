package com.heropicker.dto;

public class SettingsStatsDTO {
	private String rulesTemplate;
	private String vanillaRulesTemplate;
	private double enemyHeroDisadvantage;
	private double enemyLaneHeroDisadvantage;
	private double preferredHeroFactor;
	private double preferredLaneFactor;
	private double preferredRoleFactor;
	private RoleStatsDTO nuker;
	private RoleStatsDTO escape;
	private RoleStatsDTO support;
	private RoleStatsDTO initiator;
	private RoleStatsDTO carry;
	private RoleStatsDTO jungler;
	private RoleStatsDTO durable;
	private RoleStatsDTO disabler;
	private RoleStatsDTO pusher;
	
	public SettingsStatsDTO() {
		super();
	}
	
	
	public String getRulesTemplate() {
		return rulesTemplate;
	}
	public void setRulesTemplate(String rulesTemplate) {
		this.rulesTemplate = rulesTemplate;
	}
	public String getVanillaRulesTemplate() {
		return vanillaRulesTemplate;
	}
	public void setVanillaRulesTemplate(String vanillaRulesTemplate) {
		this.vanillaRulesTemplate = vanillaRulesTemplate;
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
