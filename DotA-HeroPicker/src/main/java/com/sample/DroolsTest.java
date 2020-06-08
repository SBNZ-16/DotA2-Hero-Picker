package com.sample;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import org.drools.template.ObjectDataCompiler;

import com.heropicker.facts.AllyHeroPickedFact;
import com.heropicker.facts.EnemyHeroPickedFact;
import com.heropicker.facts.Fact;
import com.heropicker.facts.HeroBannedFact;
import com.heropicker.facts.HeroPreferredFact;
import com.heropicker.facts.LanePreferredFact;
import com.heropicker.facts.RolePreferredFact;
import com.heropicker.io.HeroLoader;
import com.heropicker.io.RuleFileManager;
import com.heropicker.model.HeroDatabase;
import com.heropicker.model.HeroRecommendationList;
import com.heropicker.model.PickedAllyHeroes;
import com.heropicker.templating.RoleConfiguration;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

	public static final void main(String[] args) {
		
		
        ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
		

        
        ArrayList<RoleConfiguration> coll = new ArrayList<RoleConfiguration>();
        coll.add(new RoleConfiguration("Jungler", 0.5, 0.025));
        
//        RuleFileManager.dumpConfigObjects();
        
        InputStream ejo = RuleFileManager.loadTemplate("roles.drt");
        
        String ultimate = objectDataCompiler.compile(coll, ejo);
        
        
        
        RuleFileManager.exportNewRules(RuleFileManager.loadActiveRules());
        
        
        
		
		
		List<Fact> facts = new ArrayList<Fact>();
		facts.add(new AllyHeroPickedFact("tinker"));
		facts.add(new AllyHeroPickedFact("ursa"));
		facts.add(new AllyHeroPickedFact("venomancer"));
		facts.add(new AllyHeroPickedFact("lion"));
		//facts.add(new AllyHeroPickedFact("lina"));
		
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

			heroRecommendationList.scaleScore(0,100);
			heroRecommendationList.displayReccomendations(10);

			return heroRecommendationList;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

}
