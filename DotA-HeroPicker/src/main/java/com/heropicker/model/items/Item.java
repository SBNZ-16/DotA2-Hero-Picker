package com.heropicker.model.items;

import java.util.ArrayList;

public class Item {
	
	private String name;
	private ArrayList<String> components;
	private Integer cost;
	
	public Item() {
		this.components = new ArrayList<String>();
	}

	public Item(String name, ArrayList<String> components, Integer cost) {
		this.components = new ArrayList<String>();
		this.name = name;
		this.components = components;
		this.cost = cost;
	}
	
	@Override
	public String toString() {
		return "Item [name=" + name + ", components=" + components + ", cost=" + cost + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<String> components) {
		this.components = components;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer price) {
		this.cost = price;
	}
	
}
