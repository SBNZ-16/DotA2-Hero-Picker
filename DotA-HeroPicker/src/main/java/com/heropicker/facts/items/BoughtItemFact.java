package com.heropicker.facts.items;

import org.kie.api.definition.type.Position;

import com.heropicker.facts.Fact;

public class BoughtItemFact extends Fact {
	
	@Position(0)
	private String name;
	@Position(1)
	private Integer price;
	
	private boolean used;
	
	public BoughtItemFact() {
		used = false;
	}

	public BoughtItemFact(String name, Integer price) {
		super();
		used = false;
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
