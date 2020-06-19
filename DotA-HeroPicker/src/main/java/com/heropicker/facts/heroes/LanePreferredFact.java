package com.heropicker.facts.heroes;

import com.heropicker.facts.Fact;

public class LanePreferredFact extends Fact {
	private String lane;

	public LanePreferredFact(String lane) {
		super();
		this.lane = lane;
	}

	public String getLane() {
		return lane;
	}

	public void setLane(String lane) {
		this.lane = lane;
	}

}
