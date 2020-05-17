package com.sample;



import java.util.ArrayList;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.heropicker.dto.MessageDto;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.HeroDatabase;
import com.heropicker.model.HeroReccomendationList;
import com.heropicker.facts.HeroPickedFact;

import java.util.List;



/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
    	List<HeroPickedFact> facts = new ArrayList<HeroPickedFact>();
    	facts.add(new HeroPickedFact("alchemist",
    			HeroPickedFact.ALLY_PICK));
    	facts.add(new HeroPickedFact("axe",
    			HeroPickedFact.ENEMY_PICK));
        test(facts);
    }
    
    public static HeroReccomendationList test(List<HeroPickedFact> facts) {
		try {
        	HeroDatabase heroDatabase = HeroLoader.loadHeroDatabase();
        	HeroReccomendationList heroReccomendationList = new HeroReccomendationList(heroDatabase);
        	
        	
        	
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");
        	
        	kSession.setGlobal("heroDatabase", heroDatabase);
        	kSession.setGlobal("heroReccomendationList", heroReccomendationList);
        	
        	for (HeroPickedFact heroPickedFact: facts) {
        		kSession.insert(heroPickedFact);
        	}
        	
            
            kSession.fireAllRules();
            
            heroReccomendationList.sortRecommendations();
            heroReccomendationList.displayReccomendations(5);
            
            return heroReccomendationList;
        } catch (Throwable t) {
            t.printStackTrace();
        }
		return null;
    }

    public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

}
