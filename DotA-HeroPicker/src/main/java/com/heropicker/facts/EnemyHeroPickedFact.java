package com.heropicker.facts;

public class EnemyHeroPickedFact extends Fact {
	private boolean goingToFaceInLane = false;

	public EnemyHeroPickedFact() {
		super();
	}

	public EnemyHeroPickedFact(String heroId, boolean goingToFaceInLane) {
		super(heroId);
		this.goingToFaceInLane = goingToFaceInLane;
	}

	public boolean isGoingToFaceInLane() {
		return goingToFaceInLane;
	}

	public void setGoingToFaceInLane(boolean goingToFaceInLane) {
		this.goingToFaceInLane = goingToFaceInLane;
	}

}
