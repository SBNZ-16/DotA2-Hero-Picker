package com.heropicker.facts.heroes;

import com.heropicker.facts.Fact;

public class RolePreferredFact extends Fact {
	private String role;

	public RolePreferredFact(String role) {
		super();
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
