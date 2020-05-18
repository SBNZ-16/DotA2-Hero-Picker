package com.heropicker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.heropicker.facts.AllyHeroPickedFact;
import com.heropicker.facts.EnemyHeroPickedFact;
import com.heropicker.facts.Fact;
import com.sun.javafx.collections.MappingChange.Map;

import com.heropicker.model.HeroRecommendationList;
import com.heropicker.model.HeroRecommendation;

import com.sample.DroolsTest;

@CrossOrigin
@RestController
@RequestMapping("/api/pick")
public class PickController {
	
	@PostMapping()
	public ResponseEntity<ArrayList<HeroRecommendation>> recommend(@RequestBody FactsDto info) {
		List<Fact> facts = info.toFacts();
    	ArrayList<HeroRecommendation> retVal = DroolsTest.test(facts).getHeroRecommendations();
        return new ResponseEntity<ArrayList<HeroRecommendation>>(retVal, HttpStatus.OK);
	}
}
