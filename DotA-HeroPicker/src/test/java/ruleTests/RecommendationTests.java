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
import com.heropicker.facts.AllyHeroPickedFact;
import com.heropicker.facts.EnemyHeroPickedFact;
import com.heropicker.facts.Fact;
import com.heropicker.facts.HeroBannedFact;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.Hero;
import com.heropicker.model.HeroDatabase;
import com.heropicker.model.HeroRecommendation;
import com.heropicker.model.HeroRecommendationList;
import com.heropicker.model.PickedAllyHeroes;

public class RecommendationTests {


	
	private static HeroDatabase heroDatabase;
	
	@BeforeAll
	public static void setupHeroDatabase() {
		try {
			heroDatabase = HeroLoader.loadHeroDatabase();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// since no heroes are picked, the hero with the highest overall win-rate is expected, Wraith king
	@Test
	public void noHeroesTakenTest() {
		
		List<Fact> facts = new ArrayList<Fact>();
		
		ArrayList<HeroRecommendation> heroRecommendationList = getResults(facts).getHeroRecommendations();
		
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
		
		HeroRecommendationList heroRecommendationList = getResults(facts);
		
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("tinker"));
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("ursa"));
		Assertions.assertNull(heroRecommendationList.findHeroRecommendationByHeroId("venomancer"));
	}
	
	
	public HeroRecommendationList getResults(List<Fact> facts) {
			
		HeroRecommendationList heroRecommendationList = new HeroRecommendationList(heroDatabase);
		PickedAllyHeroes pickedAllyHeroes= new PickedAllyHeroes(heroDatabase);
				
		// load up the knowledge base
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession("ksession-rules");

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

		heroRecommendationList.scaleScore(0,100);
		heroRecommendationList.displayReccomendations(10);
		return heroRecommendationList;
	}
	
	
	
}
