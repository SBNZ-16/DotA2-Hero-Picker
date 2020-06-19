package com.heropicker.dto;

import java.util.ArrayList;

public class ItemDto {
	
	private String wanted;
	private ArrayList<String> boughtItems;
	
	public ItemDto() {
		
	}

	public ItemDto(String wanted, ArrayList<String> boughtItems) {
		super();
		this.wanted = wanted;
		this.boughtItems = boughtItems;
	}

	public String getWanted() {
		return wanted;
	}

	public void setWanted(String wanted) {
		this.wanted = wanted;
	}

	public ArrayList<String> getBoughtItems() {
		return boughtItems;
	}

	public void setBoughtItems(ArrayList<String> boughtItems) {
		this.boughtItems = boughtItems;
	}
	
}
