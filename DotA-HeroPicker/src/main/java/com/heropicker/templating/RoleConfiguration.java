package com.heropicker.templating;

public class RoleConfiguration {
	
	private String role;
	private Double penalty_base;
	private Double penalty_multiplier;
	
	public RoleConfiguration() {
		
	}

	public RoleConfiguration(String role, Double penalty_base, Double penalty_multiplier) {
		super();
		this.role = role;
		this.penalty_base = penalty_base;
		this.penalty_multiplier = penalty_multiplier;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Double getPenalty_base() {
		return penalty_base;
	}

	public void setPenalty_base(Double penalty_base) {
		this.penalty_base = penalty_base;
	}

	public Double getPenalty_multiplier() {
		return penalty_multiplier;
	}

	public void setPenalty_multiplier(Double penalty_multiplier) {
		this.penalty_multiplier = penalty_multiplier;
	}
	

}
