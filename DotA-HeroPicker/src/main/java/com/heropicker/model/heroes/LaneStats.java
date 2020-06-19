package com.heropicker.model.heroes;

public class LaneStats {
	private double presence;
	private String name;
	private double winrate;

	public LaneStats() {
		super();
	}

	public LaneStats(double presence, String name, double winrate) {
		super();
		this.presence = presence;
		this.name = name;
		this.winrate = winrate;
	}

	public double getPresence() {
		return presence;
	}

	public void setPresence(double presence) {
		this.presence = presence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWinrate() {
		return winrate;
	}

	public void setWinrate(double winrate) {
		this.winrate = winrate;
	}

}
