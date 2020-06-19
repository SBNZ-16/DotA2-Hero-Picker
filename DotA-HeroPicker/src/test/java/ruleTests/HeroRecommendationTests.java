package ruleTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.junit.jupiter.api.Assertions;

import com.heropicker.service.PickService;

import com.heropicker.config.Config;
import com.heropicker.facts.Fact;
import com.heropicker.facts.heroes.AllyHeroPickedFact;
import com.heropicker.facts.heroes.EnemyHeroPickedFact;
import com.heropicker.facts.heroes.HeroBannedFact;
import com.heropicker.facts.heroes.HeroPreferredFact;
import com.heropicker.facts.heroes.LanePreferredFact;
import com.heropicker.facts.heroes.RolePreferredFact;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.Hero;
import com.heropicker.model.HeroDatabase;
import com.heropicker.model.HeroRecommendation;
import com.heropicker.model.HeroRecommendationList;
import com.heropicker.model.PickedAllyHeroes;

public class HeroRecommendationTests {

	private static HeroDatabase heroDatabase;

	@BeforeAll
	public static void setupHeroDatabase() {
		try {
			heroDatabase = HeroLoader.loadHeroDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HeroRecommendationList getResults(List<Fact> facts) {

		HeroRecommendationList heroRecommendationList = new HeroRecommendationList(heroDatabase);
		PickedAllyHeroes pickedAllyHeroes = new PickedAllyHeroes(heroDatabase);

		// load up the knowledge base
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("hero-ksession");

		kSession.setGlobal("heroDatabase", heroDatabase);
		kSession.setGlobal("heroRecommendationList", heroRecommendationList);
		kSession.setGlobal("pickedAllyHeroes", pickedAllyHeroes);

		for (Fact heroPickedFact : facts) {
			kSession.insert(heroPickedFact);
		}

		kSession.getAgenda().getAgendaGroup("hero-statistics").setFocus();
		kSession.fireAllRules();

		kSession.getAgenda().getAgendaGroup("team-composition").setFocus();
		kSession.fireAllRules();

		kSession.getAgenda().getAgendaGroup("preferences").setFocus();
		kSession.fireAllRules();

		heroRecommendationList.scaleScore(0, 100);
		heroRecommendationList.displayReccomendations(10);
		return heroRecommendationList;
	}

	// since no heroes are picked, the hero with the highest overall win-rate is
	// expected, Wraith king
	@Test
	public void noHeroesTakenTest() {

		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<HeroRecommendation> heroRecommendationList = getResults(facts).getHeroRecommendations();

		HeroRecommendation firstHero = heroRecommendationList.get(0);

		Assertions.assertEquals(100.0, firstHero.getScore());
		Assertions.assertEquals("Wraith King", firstHero.getHero().getHeroName());
	}

	// if a hero is picked by an ally, or an enemy or banned, he cannot be
	// recommended
	@Test
	public void cantRecommendTakenHeroTest() {

		List<Fact> facts = new ArrayList<Fact>();
		facts.add(new AllyHeroPickedFact("tinker"));
		facts.add(new EnemyHeroPickedFact("ursa", true));
		facts.add(new HeroBannedFact("venomancer"));

		HeroRecommendationList heroRecommendationList = getResults(facts);

		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("tinker"));
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("ursa"));
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("venomancer"));
	}


	@Test
	public void heroPreferenceTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(1, heroRecommendationList.getHeroRank("spectre"));

		facts.add(new HeroPreferredFact("spectre"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(0, heroRecommendationList.getHeroRank("spectre"));

	}
	
	@Test
	public void rolePreferenceTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(3, heroRecommendationList.getHeroRank("ogre-magi"));

		facts.add(new RolePreferredFact("Support"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(1, heroRecommendationList.getHeroRank("ogre-magi"));

	}
	
	@Test
	public void lanePreferenceTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(5, heroRecommendationList.getHeroRank("zeus"));

		facts.add(new LanePreferredFact("Mid Lane"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(0, heroRecommendationList.getHeroRank("zeus"));
	}
	
	@Test
	public void nukerRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(5, heroRecommendationList.getHeroRank("zeus"));

		facts.add(new AllyHeroPickedFact("lion"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(92, heroRecommendationList.getHeroRank("zeus"));
	}
	
	@Test
	public void escapeRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(7, heroRecommendationList.getHeroRank("anti-mage"));

		facts.add(new AllyHeroPickedFact("weaver"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(97, heroRecommendationList.getHeroRank("zeus"));
	}
	
	@Test
	public void supportRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(3, heroRecommendationList.getHeroRank("ogre-magi"));

		facts.add(new AllyHeroPickedFact("lion"));
		facts.add(new AllyHeroPickedFact("io"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(40, heroRecommendationList.getHeroRank("ogre-magi"));
	}
	
	@Test
	public void initiatorRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(6, heroRecommendationList.getHeroRank("beastmaster"));

		facts.add(new AllyHeroPickedFact("axe"));
		facts.add(new AllyHeroPickedFact("magnus"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(93, heroRecommendationList.getHeroRank("beastmaster"));
	}
	
	@Test
	public void carryRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(2, heroRecommendationList.getHeroRank("lone-druid"));

		facts.add(new AllyHeroPickedFact("slark"));
		facts.add(new AllyHeroPickedFact("spectre"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(51, heroRecommendationList.getHeroRank("lone-druid"));
	}
	
	@Test
	public void junglerRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(28, heroRecommendationList.getHeroRank("lycan"));

		facts.add(new AllyHeroPickedFact("natures-prophet"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(38, heroRecommendationList.getHeroRank("lycan"));
	}
	
	@Test
	public void durableRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(0, heroRecommendationList.getHeroRank("wraith-king"));

		facts.add(new AllyHeroPickedFact("axe"));
		facts.add(new AllyHeroPickedFact("bristleback"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(4, heroRecommendationList.getHeroRank("wraith-king"));
	}
	
	@Test
	public void disablerRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(3, heroRecommendationList.getHeroRank("ogre-magi"));

		facts.add(new AllyHeroPickedFact("shadow-shaman"));
		facts.add(new AllyHeroPickedFact("shadow-demon"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(53, heroRecommendationList.getHeroRank("ogre-magi"));
	}
	
	@Test
	public void puherRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		HeroRecommendationList heroRecommendationList = getResults(facts);
		Assertions.assertEquals(4, heroRecommendationList.getHeroRank("visage"));

		facts.add(new AllyHeroPickedFact("venomancer"));
		facts.add(new AllyHeroPickedFact("natures-prophet"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(14, heroRecommendationList.getHeroRank("visage"));
	}
}
