package com.heropicker.service;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heropicker.facts.Fact;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.heroes.HeroDatabase;
import com.heropicker.model.heroes.HeroRecommendationList;
import com.heropicker.model.heroes.PickedAllyHeroes;

@Service
public class PickService {
	
	@Autowired
	private KieContainer kieContainer;
	
	@Autowired
	private HeroDatabase heroDatabase;
	
	
	public HeroRecommendationList recommend(List<Fact> facts) {
		
		HeroRecommendationList heroRecommendationList = new HeroRecommendationList(heroDatabase);
		PickedAllyHeroes pickedAllyHeroes= new PickedAllyHeroes(heroDatabase);
		
		KieSession kSession = kieContainer.newKieSession("hero-ksession");
				
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

		kSession.getAgenda().getAgendaGroup("sort").setFocus();
		kSession.fireAllRules();
		
//		kSession.getAgenda().getAgendaGroup("scale").setFocus();
//		kSession.fireAllRules();
		
		heroRecommendationList.displayReccomendations(10);

		return heroRecommendationList;
	}


	public KieContainer getKieContainer() {
		return kieContainer;
	}


	public void setKieContainer(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}


	public HeroDatabase getHeroDatabase() {
		return heroDatabase;
	}


	public void setHeroDatabase(HeroDatabase heroDatabase) {
		this.heroDatabase = heroDatabase;
	}
	
}