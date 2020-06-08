package ruleTests;



import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.junit.jupiter.api.Assertions;

import com.heropicker.service.PickService;



import com.heropicker.config.Config;
import com.heropicker.facts.AllyHeroPickedFact;
import com.heropicker.facts.EnemyHeroPickedFact;
import com.heropicker.facts.Fact;
import com.heropicker.facts.HeroBannedFact;
import com.heropicker.model.Hero;
import com.heropicker.model.HeroDatabase;
import com.heropicker.model.HeroRecommendation;
import com.heropicker.model.HeroRecommendationList;

public class QuickTest {

	private static PickService pickService;
	
	@BeforeAll
	public static void setupKieSession() {
		Config config = new Config();
		
		KieContainer kieContainer = config.kieContainer();
		HeroDatabase heroDatabase = config.heroDatabase();
		
		pickService = new PickService();
		pickService.setKieContainer(kieContainer);
		pickService.setHeroDatabase(heroDatabase);
	}
	
	
	// since no heroes are picked, the hero with the highest overall win-rate is expected, Wraith king
	@Test
	public void noHeroesTakenTest() {
		
		List<Fact> facts = new ArrayList<Fact>();
		
		ArrayList<HeroRecommendation> heroRecommendationList = pickService.recommend(facts).getHeroRecommendations();
		
		HeroRecommendation firstHero = heroRecommendationList.get(0);
		
		Assertions.assertEquals(100.0, firstHero.getScore());
		Assertions.assertEquals("Wraith King", firstHero.getHero().getHeroName());
	}
	
	// if a hero is picked by an ally, or an enemy or banned, he cannot be recommended
	@Test
	public void cantRecommendTakenHeroTest() {
		
		List<Fact> facts = new ArrayList<Fact>();
		facts.add(new AllyHeroPickedFact("tinker"));
		facts.add(new EnemyHeroPickedFact("ursa", true));
		facts.add(new HeroBannedFact("venomancer"));
		
		HeroRecommendationList heroRecommendationList = pickService.recommend(facts);
		
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("tinker"));
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("ursa"));
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("venomancer"));
	}
	
	
	
}
