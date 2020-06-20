package ruleTests;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
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

	// Mjollnir has Maelstrom as a component
	@Test
	public void mjollnirWithMaelstromTest() {

		String wantedItem = "Mjollnir";
		ArrayList<String> boughtItems = new ArrayList<String>();
		boughtItems.add("Maelstrom");

		Integer balance = getResults(boughtItems, wantedItem);
		Integer expected = itemDatabase.getItemPrice("Mjollnir") - itemDatabase.getItemPrice("Maelstrom");

		Assertions.assertEquals(expected, balance);
	}

	// Vanguard has Vitality Booster and Ring of Health as its components
	@Test
	public void vanguardWithAllComponentsTest() {

		String wantedItem = "Vanguard";
		ArrayList<String> boughtItems = new ArrayList<String>();
		boughtItems.add("Vitality Booster");
		boughtItems.add("Ring of Health");

		Integer balance = getResults(boughtItems, wantedItem);
		Integer expected = 0;

		Assertions.assertEquals(expected, balance);
	}

	// Orchid Malevolence has two Oblivion Staffs as its components
	@Test
	public void orchidWithOblivionTest() {

		String wantedItem = "Orchid Malevolence";
		ArrayList<String> boughtItems = new ArrayList<String>();
		boughtItems.add("Oblivion Staff");

		Integer balance = getResults(boughtItems, wantedItem);
		Integer expected = itemDatabase.getItemPrice("Orchid Malevolence")
				- itemDatabase.getItemPrice("Oblivion Staff");

		Assertions.assertEquals(expected, balance);
	}

	// Orchid Malevolence has two Oblivion Staffs as its components
	@Test
	public void orchidWithTwoOblivionsTest() {

		String wantedItem = "Orchid Malevolence";
		ArrayList<String> boughtItems = new ArrayList<String>();
		boughtItems.add("Oblivion Staff");
		boughtItems.add("Oblivion Staff");

		Integer balance = getResults(boughtItems, wantedItem);
		Integer expected = itemDatabase.getItemPrice("Orchid Malevolence")
				- 2 * itemDatabase.getItemPrice("Oblivion Staff");

		Assertions.assertEquals(expected, balance);
	}

	// Linken's Sphere has Perseverance which has Ring of Health as its component 
	// (having both bought will reduce the price only by the price of Perseverance)
	@Test
	public void linkenWithPerseveranceAndRingOfHealthTest() {

		String wantedItem = "Linken's Sphere";
		ArrayList<String> boughtItems = new ArrayList<String>();
		boughtItems.add("Perseverance");
		boughtItems.add("Ring of Health");

		Integer balance = getResults(boughtItems, wantedItem);
		Integer expected = itemDatabase.getItemPrice("Linken's Sphere")
				- itemDatabase.getItemPrice("Perseverance");

		Assertions.assertEquals(expected, balance);
	}
	
	public Integer getResults(ArrayList<String> boughtItems, String wantedItem) {

		ArrayList<ItemHierarchyFact> hierarchyFacts = itemDatabase.createItemHierarchyFacts(wantedItem);
		WantedItemFact wantedItemFact = itemDatabase.createWantedItemFact(wantedItem);
		BoughtItemCollectionFact boughtItemCollectionFact = itemDatabase.prepareBoughtItemFacts(boughtItems);

		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("item-test-ksession");

		for (ItemHierarchyFact fact : hierarchyFacts) {
			kSession.insert(fact);
		}
		kSession.insert(wantedItemFact);
		kSession.insert(boughtItemCollectionFact);

		kSession.fireAllRules();

		Integer balance = (Integer) kSession.getGlobal("balance");
		return balance;
	}

}
