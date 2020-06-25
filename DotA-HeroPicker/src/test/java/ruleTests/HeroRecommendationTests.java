package ruleTests;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;

import com.heropicker.facts.Fact;
import com.heropicker.facts.heroes.AllyHeroPickedFact;
import com.heropicker.facts.heroes.EnemyHeroPickedFact;
import com.heropicker.facts.heroes.HeroBannedFact;
import com.heropicker.facts.heroes.HeroPreferredFact;
import com.heropicker.facts.heroes.LanePreferredFact;
import com.heropicker.facts.heroes.ResultCollectionFact;
import com.heropicker.facts.heroes.ResultFact;
import com.heropicker.facts.heroes.RolePreferredFact;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.heroes.HeroDatabase;

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

	public ArrayList<ResultFact> getResults(List<Fact> facts) {
		// load up the knowledge base
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("hero-test-ksession");

		kSession.setGlobal("heroDatabase", heroDatabase);
		kSession.insert(new ResultCollectionFact());

		for (Fact heroPickedFact : facts) {
			kSession.insert(heroPickedFact);
		}

		for (Fact heroRecommendationFact : heroDatabase.generateHeroRecommendationFacts()) {
			kSession.insert(heroRecommendationFact);
		}

		kSession.getAgenda().getAgendaGroup("sort").setFocus();
		kSession.getAgenda().getAgendaGroup("scale").setFocus();
		kSession.getAgenda().getAgendaGroup("update").setFocus();
		kSession.getAgenda().getAgendaGroup("preferences").setFocus();
		kSession.getAgenda().getAgendaGroup("update").setFocus();
		kSession.getAgenda().getAgendaGroup("team-composition").setFocus();
		kSession.getAgenda().getAgendaGroup("hero-statistics").setFocus();
		kSession.fireAllRules();

		QueryResults results = kSession.getQueryResults("Get result");

		ResultCollectionFact resultCollections = (ResultCollectionFact) results.iterator().next().get("$result");
		ArrayList<ResultFact> sortedRecommendations = resultCollections.getResult();
		for (int i = 0; i < 10; i++) {
			ResultFact result = sortedRecommendations.get(i);
			System.out.println(i + 1 + ". " + result.getHeroId() + " " + result.getScore());
		}
		return sortedRecommendations;
	}

	private int getHeroRank(ArrayList<ResultFact> heroRecommendationList, String heroId) {
		return IntStream.range(0, heroRecommendationList.size())
				.filter(i -> heroId.equals(heroRecommendationList.get(i).getHeroId())).findFirst().getAsInt();
	}

	private boolean isResultFactPresent(ArrayList<ResultFact> heroRecommendationList, String heroId) {
		return heroRecommendationList.parallelStream().filter(x -> heroId.equals(x.getHeroId())).findFirst()
				.isPresent();
	}

	// since no heroes are picked, the hero with the highest overall win-rate is
	// expected, Wraith king
	@Test
	public void noHeroesTakenTest() {

		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);

		ResultFact firstHero = heroRecommendationList.get(0);

		Assertions.assertEquals(100.0, firstHero.getScore());
		Assertions.assertEquals("Wraith King", firstHero.getHeroName());
	}

	// if a hero is picked by an ally, or an enemy or banned, he cannot be
	// recommended
	@Test
	public void cantRecommendTakenHeroTest() {

		List<Fact> facts = new ArrayList<Fact>();
		facts.add(new AllyHeroPickedFact("tinker"));
		facts.add(new EnemyHeroPickedFact("ursa", true));
		facts.add(new HeroBannedFact("venomancer"));

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);

		Assertions.assertFalse(isResultFactPresent(heroRecommendationList, "tinker"));
		Assertions.assertFalse(isResultFactPresent(heroRecommendationList, "ursa"));
		Assertions.assertFalse(isResultFactPresent(heroRecommendationList, "venomancer"));
	}

	@Test
	public void sortResultsTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);

		for (int i = 1; i < heroRecommendationList.size(); i++) {
			Assertions.assertTrue(
					heroRecommendationList.get(i - 1).getScore() >= heroRecommendationList.get(i).getScore());
		}
	}

	@Test
	public void enemyHeroPickedTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(5, getHeroRank(heroRecommendationList, "zeus"));

		facts.add(new EnemyHeroPickedFact("broodmother", false));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(103, getHeroRank(heroRecommendationList, "zeus"));
	}

	@Test
	public void heroPreferenceTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(1, getHeroRank(heroRecommendationList, "spectre"));

		facts.add(new HeroPreferredFact("spectre"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(0, getHeroRank(heroRecommendationList, "spectre"));

	}

	@Test
	public void rolePreferenceTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(3, getHeroRank(heroRecommendationList, "ogre-magi"));

		facts.add(new RolePreferredFact("Support"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(1, getHeroRank(heroRecommendationList, "ogre-magi"));

	}

	@Test
	public void lanePreferenceTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(5, getHeroRank(heroRecommendationList, "zeus"));

		facts.add(new LanePreferredFact("Mid Lane"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(0, getHeroRank(heroRecommendationList, "zeus"));
	}

	@Test
	public void nukerRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(5, getHeroRank(heroRecommendationList, "zeus"));

		facts.add(new AllyHeroPickedFact("lion"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(98, getHeroRank(heroRecommendationList, "zeus"));
	}

	@Test
	public void escapeRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(7, getHeroRank(heroRecommendationList, "anti-mage"));

		facts.add(new AllyHeroPickedFact("weaver"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(97, getHeroRank(heroRecommendationList, "zeus"));
	}

	@Test
	public void supportRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(3, getHeroRank(heroRecommendationList, "ogre-magi"));

		facts.add(new AllyHeroPickedFact("lion"));
		facts.add(new AllyHeroPickedFact("io"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(32, getHeroRank(heroRecommendationList, "ogre-magi"));
	}

	@Test
	public void initiatorRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(6, getHeroRank(heroRecommendationList, "beastmaster"));

		facts.add(new AllyHeroPickedFact("axe"));
		facts.add(new AllyHeroPickedFact("magnus"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(78, getHeroRank(heroRecommendationList, "beastmaster"));
	}

	@Test
	public void carryRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(2, getHeroRank(heroRecommendationList, "lone-druid"));

		facts.add(new AllyHeroPickedFact("slark"));
		facts.add(new AllyHeroPickedFact("spectre"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(15, getHeroRank(heroRecommendationList, "lone-druid"));
	}

	@Test
	public void junglerRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(28, getHeroRank(heroRecommendationList, "lycan"));

		facts.add(new AllyHeroPickedFact("natures-prophet"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(54, getHeroRank(heroRecommendationList, "lycan"));
	}

	@Test
	public void durableRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(0, getHeroRank(heroRecommendationList, "wraith-king"));

		facts.add(new AllyHeroPickedFact("axe"));
		facts.add(new AllyHeroPickedFact("bristleback"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(5, getHeroRank(heroRecommendationList, "wraith-king"));
	}

	@Test
	public void disablerRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(3, getHeroRank(heroRecommendationList, "ogre-magi"));

		facts.add(new AllyHeroPickedFact("shadow-shaman"));
		facts.add(new AllyHeroPickedFact("shadow-demon"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(45, getHeroRank(heroRecommendationList, "ogre-magi"));
	}

	@Test
	public void puherRoleTest() {
		List<Fact> facts = new ArrayList<Fact>();

		ArrayList<ResultFact> heroRecommendationList = getResults(facts);
		Assertions.assertEquals(4, getHeroRank(heroRecommendationList, "visage"));

		facts.add(new AllyHeroPickedFact("venomancer"));
		facts.add(new AllyHeroPickedFact("natures-prophet"));

		heroRecommendationList = getResults(facts);
		Assertions.assertEquals(14, getHeroRank(heroRecommendationList, "visage"));
	}
}
