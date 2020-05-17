package com.heropicker.facts;

public class EnemyHeroPickedFact extends Fact {
	private boolean goingToFaceInLane = false;
	public String heroId;

	public EnemyHeroPickedFact() {
		super();
	}

	public EnemyHeroPickedFact(String heroId, boolean goingToFaceInLane) {
		this.heroId = heroId;
		this.goingToFaceInLane = goingToFaceInLane;
	}

	public boolean isGoingToFaceInLane() {
		return goingToFaceInLane;
	}

	public void setGoingToFaceInLane(boolean goingToFaceInLane) {
		this.goingToFaceInLane = goingToFaceInLane;
	}

	public String getHeroId() {
		return heroId;
	}

	public void setHeroId(String heroId) {
		this.heroId = heroId;
	}

}
