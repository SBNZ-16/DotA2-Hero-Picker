package ruleTests;


import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.heropicker.facts.items.BoughtItemCollectionFact;
import com.heropicker.facts.items.ItemHierarchyFact;
import com.heropicker.facts.items.WantedItemFact;
import com.heropicker.io.ItemLoader;
import com.heropicker.model.items.ItemDatabase;

public class ItemCalculatorTests {
	
	private static ItemDatabase itemDatabase;
	
	@BeforeAll
	public static void setupHeroDatabase() {
		try {
			itemDatabase = ItemLoader.loadItemDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void mjollnirTest() {
		
		String wantedItem = "Mjollnir";
		ArrayList<String> boughtItems = new ArrayList<String>();
		boughtItems.add("Maelstrom");
		
		Integer balance = getResults(boughtItems, wantedItem);
		
		System.out.println("Kranji balance: " + balance);
	}
	
	public Integer getResults(ArrayList<String> boughtItems, String wantedItem) {
		
		ArrayList<ItemHierarchyFact> hierarchyFacts = itemDatabase.createItemHierarchyFacts(wantedItem);
		WantedItemFact wantedItemFact = itemDatabase.createWantedItemFact(wantedItem);
		BoughtItemCollectionFact boughtItemCollectionFact = itemDatabase.prepareBoughtItemFacts(boughtItems);
		
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("item-test-ksession");
		
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
