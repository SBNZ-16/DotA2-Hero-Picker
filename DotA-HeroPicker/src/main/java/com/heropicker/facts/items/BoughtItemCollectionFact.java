package com.heropicker.facts.items;

import java.util.ArrayList;

import com.heropicker.facts.Fact;

public class BoughtItemCollectionFact extends Fact {
	
	private ArrayList<BoughtItemFact> boughtItemFacts;
	
	public BoughtItemCollectionFact() {
		this.boughtItemFacts = new ArrayList<BoughtItemFact>();
	}

	public BoughtItemCollectionFact(ArrayList<BoughtItemFact> boughtItemFacts) {
		super();
		this.boughtItemFacts = boughtItemFacts;
	}

	public ArrayList<BoughtItemFact> getBoughtItemFacts() {
		return boughtItemFacts;
	}
	
}
