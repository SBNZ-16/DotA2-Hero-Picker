package com.heropicker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heropicker.dto.MessageDto;
import com.heropicker.facts.HeroPickedFact;
import com.sun.javafx.collections.MappingChange.Map;

import com.heropicker.model.HeroReccomendationList;
import com.heropicker.model.HeroRecommendation;

import com.sample.DroolsTest;

@CrossOrigin
@RestController
@RequestMapping("/api/pick")
public class PickController {
	
	@GetMapping()
	public ResponseEntity<ArrayList<HeroRecommendation>> test() {
		List<HeroPickedFact> facts = new ArrayList<HeroPickedFact>();
    	facts.add(new HeroPickedFact("alchemist",
    			HeroPickedFact.ALLY_PICK));
    	facts.add(new HeroPickedFact("axe",
    			HeroPickedFact.ENEMY_PICK));
    	ArrayList<HeroRecommendation> retVal = DroolsTest.test(facts).getHeroReccomendations();
        return new ResponseEntity<ArrayList<HeroRecommendation>>(retVal, HttpStatus.OK);
    }
}
