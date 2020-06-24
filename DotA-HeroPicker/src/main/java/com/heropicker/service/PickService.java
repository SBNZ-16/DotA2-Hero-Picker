package com.heropicker.service;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heropicker.facts.Fact;
import com.heropicker.facts.heroes.HeroRecommendationFact;
import com.heropicker.io.HeroLoader;
import com.heropicker.model.heroes.HeroDatabase;
import com.heropicker.model.heroes.PickedAllyHeroes;

@Service
public class PickService {

	@Autowired
	private KieContainer kieContainer;

	@Autowired
	private HeroDatabase heroDatabase;

	public ArrayList<HeroRecommendationFact> recommend(List<Fact> facts) {
		ArrayList<HeroRecommendationFact> sortedRecommendations = new ArrayList<HeroRecommendationFact>();

		KieSession kSession = kieContainer.newKieSession("hero-ksession");

		kSession.setGlobal("sortedRecommendations", sortedRecommendations);
		kSession.setGlobal("heroDatabase", heroDatabase);


		for (Fact heroPickedFact : facts) {
			kSession.insert(heroPickedFact);
		}

		for (Fact heroRecommendationFact : heroDatabase.generateHeroRecommendationFacts()) {
			kSession.insert(heroRecommendationFact);
		}

		kSession.getAgenda().getAgendaGroup("hero-statistics").setFocus();
		kSession.fireAllRules();

		kSession.getAgenda().getAgendaGroup("team-composition").setFocus();
		kSession.fireAllRules();

		kSession.getAgenda().getAgendaGroup("preferences").setFocus();
		kSession.fireAllRules();

		kSession.getAgenda().getAgendaGroup("sort").setFocus();
		kSession.fireAllRules();

		kSession.getAgenda().getAgendaGroup("scale").setFocus();
		kSession.fireAllRules();
		
		System.out.println("ejo");
		
		for (int i = 0; i < 10; i++) {
			HeroRecommendationFact recommendation = sortedRecommendations.get(i);
			
			System.out.println(i + ". " + recommendation.getHero().getHeroName() + " " + recommendation.getScore());
		}

		return sortedRecommendations;
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