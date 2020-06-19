package com.heropicker.service;

import java.util.ArrayList;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heropicker.facts.items.BoughtItemCollectionFact;
import com.heropicker.facts.items.ItemHierarchyFact;
import com.heropicker.facts.items.WantedItemFact;
import com.heropicker.model.items.ItemDatabase;

@Service
public class ItemService {
	
	@Autowired
	private KieContainer kieContainer;
	
	@Autowired
	private ItemDatabase itemDatabase;
	
	
	public Integer calculateMissingGold(ArrayList<String> boughtItems, String wantedItem) {
		
		ArrayList<ItemHierarchyFact> hierarchyFacts = itemDatabase.createItemHierarchyFacts(wantedItem);
		WantedItemFact wantedItemFact = itemDatabase.createWantedItemFact(wantedItem);
		BoughtItemCollectionFact boughtItemCollectionFact = itemDatabase.prepareBoughtItemFacts(boughtItems);
		
		KieSession kSession = kieContainer.newKieSession("item-ksession");
		
		for (ItemHierarchyFact fact: hierarchyFacts) {
			kSession.insert(fact);
		}
		kSession.insert(wantedItemFact);
		kSession.insert(boughtItemCollectionFact);
		
		kSession.fireAllRules();
			
		Integer balance = (Integer)kSession.getGlobal("balance");
		return balance;
	}
	

}
