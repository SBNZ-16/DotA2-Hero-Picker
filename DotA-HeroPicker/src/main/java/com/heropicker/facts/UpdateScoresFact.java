package com.heropicker.facts;

public class UpdateScoresFact extends Fact {
	public String heroId;
	private double multiplyScale = 1;
	private double addValue = 0;
	private String role;
	private String lane;

	public UpdateScoresFact() {
		super();
	}

	public UpdateScoresFact(double multiplyScale, double addValue, String heroId, String role, String lane) {
		this.heroId = heroId;
		this.multiplyScale = multiplyScale;
		this.addValue = addValue;
		this.role = role;
		this.lane = lane;
	}

	public double getMultiplyScale() {
		return multiplyScale;
	}

	public void setMultiplyScale(double multiplyScale) {
		this.multiplyScale = multiplyScale;
	}

	public double getAddValue() {
		return addValue;
	}

	public void setAddValue(double addValue) {
		this.addValue = addValue;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getLane() {
		return lane;
	}

	public void setLane(String lane) {
		this.lane = lane;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

}
