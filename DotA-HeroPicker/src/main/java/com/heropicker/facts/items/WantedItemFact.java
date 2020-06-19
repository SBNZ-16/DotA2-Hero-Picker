package com.heropicker.facts.items;

import org.kie.api.definition.type.Position;

import com.heropicker.facts.Fact;

public class WantedItemFact extends Fact {
	
	@Position(0)
	private String name;
	@Position(1)
	private Integer price;
	
	public WantedItemFact() {
		
	}

	public WantedItemFact(String name, Integer price) {
		super();
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
}
