package com.heropicker.service;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heropicker.facts.Fact;
import com.heropicker.facts.heroes.HeroRecommendationFact;
import com.heropicker.facts.heroes.ResultCollectionFact;
import com.heropicker.facts.heroes.ResultFact;
import com.heropicker.model.heroes.HeroDatabase;

@Service
public class PickService {

	@Autowired
	private KieContainer kieContainer;

	@Autowired
	private HeroDatabase heroDatabase;

	public ArrayList<ResultFact> recommend(List<Fact> facts) {
		KieSession kSession = kieContainer.newKieSession("hero-ksession");

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