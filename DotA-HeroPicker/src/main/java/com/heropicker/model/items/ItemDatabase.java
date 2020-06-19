package com.heropicker.model.items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.heropicker.facts.items.BoughtItemCollectionFact;
import com.heropicker.facts.items.BoughtItemFact;
import com.heropicker.facts.items.ItemHierarchyFact;
import com.heropicker.facts.items.WantedItemFact;

public class ItemDatabase {
	
	private ArrayList<Item> items;
	private Map<String, Item> itemMap;
	
	public ItemDatabase() {
		items = new ArrayList<Item>();
		itemMap = new HashMap<String, Item>();
	}
	
	public ItemDatabase(ArrayList<Item> items) {
		super();
		this.items = items;
		itemMap = new HashMap<String, Item>();
		for (Item item: items) {
			itemMap.put(item.getName(), item);
		}
	}

	public ArrayList<Item> getItems() {
		return items;
	}
	
	public Integer getItemPrice(String name) {
		return itemMap.get(name).getCost();
	}
	
	public ArrayList<ItemHierarchyFact> createItemHierarchyFacts(String name) {
		ArrayList<ItemHierarchyFact> retVal = new ArrayList<ItemHierarchyFact>();
		Item root = itemMap.get(name);
		recursivelyExpandHierarchy(root, retVal);
		return retVal;
	}
	
	public BoughtItemCollectionFact prepareBoughtItemFacts(ArrayList<String> boughtItems) {		
		ArrayList<BoughtItemFact> facts = new ArrayList<BoughtItemFact>();
		for (String name: boughtItems) {
			facts.add(createBoughtItemFact(name));
		}
		
		facts.sort(new Comparator<BoughtItemFact>() {
			@Override
			public int compare(BoughtItemFact arg0, BoughtItemFact arg1) {
				return Integer.compare(arg0.getPrice(), arg1.getPrice());
			}
		});
		return new BoughtItemCollectionFact(facts);
	}
	
	public WantedItemFact createWantedItemFact(String name) {
		Item item = itemMap.get(name);
		return new WantedItemFact(name, item.getCost());
	}
		
	private BoughtItemFact createBoughtItemFact(String name) {
		Item item = itemMap.get(name);
		return new BoughtItemFact(item.getName(), item.getCost());
	}
	
	private void recursivelyExpandHierarchy(Item root, ArrayList<ItemHierarchyFact> facts) {
		for (String componentString: root.getComponents()) {
			Item component = itemMap.get(componentString);
			facts.add(new ItemHierarchyFact(component.getName(), root.getName()));
			recursivelyExpandHierarchy(component, facts);
		}
	}
}
