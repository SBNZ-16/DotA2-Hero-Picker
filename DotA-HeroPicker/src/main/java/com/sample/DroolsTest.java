package com.sample;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.heropicker.facts.AllyHeroPickedFact;
import com.heropicker.facts.EnemyHeroPickedFact;
import com.heropicker.facts.Fact;
import com.heropicker.facts.HeroBannedFact;
import com.heropicker.facts.HeroPreferredFact;
import com.heropicker.facts.LanePreferredFact;
import com.heropicker.facts.RolePreferredFact;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.HeroDatabase;
import com.heropicker.model.HeroRecommendationList;
import com.heropicker.model.PickedAllyHeroes;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	public static final void main(String[] args) {
		List<Fact> facts = new ArrayList<Fact>();
		facts.add(new AllyHeroPickedFact("tinker"));
		facts.add(new EnemyHeroPickedFact("invoker", true));
		facts.add(new HeroBannedFact("riki"));
		facts.add(new HeroPreferredFact("lycan"));
		facts.add(new RolePreferredFact("Pusher"));
		facts.add(new LanePreferredFact("Jungle"));
		test(facts);
	}

	public static HeroRecommendationList test(List<Fact> facts) {
		try {
			HeroDatabase heroDatabase = HeroLoader.loadHeroDatabase();
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

			heroRecommendationList.displayReccomendations(10);

			return heroRecommendationList;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

}
