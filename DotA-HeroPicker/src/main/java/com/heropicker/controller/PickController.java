package com.heropicker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heropicker.dto.FactsDto;
import com.heropicker.dto.MessageDto;
import com.heropicker.facts.Fact;
import com.heropicker.facts.heroes.AllyHeroPickedFact;
import com.heropicker.facts.heroes.EnemyHeroPickedFact;
import com.heropicker.model.heroes.HeroRecommendation;
import com.heropicker.model.heroes.HeroRecommendationList;
import com.heropicker.service.PickService;


@CrossOrigin
@RestController
@RequestMapping("/api/pick")
public class PickController {
	
	@Autowired
	private PickService pickService;
	
	@PostMapping()
	public ResponseEntity<ArrayList<HeroRecommendation>> recommend(@RequestBody FactsDto info) {
		List<Fact> facts = info.toFacts();
    	ArrayList<HeroRecommendation> retVal = pickService.recommend(facts).getHeroRecommendations();
        return new ResponseEntity<ArrayList<HeroRecommendation>>(retVal, HttpStatus.OK);
	}
}
