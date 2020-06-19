package com.heropicker.controller;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.heropicker.dto.ItemDto;
import com.heropicker.service.ItemService;


@CrossOrigin
@RestController
@RequestMapping("/api/itemCalculator")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@PostMapping()
	public ResponseEntity<String> recommend(@RequestBody ItemDto itemDto) {
		ArrayList<String> boughtItems = itemDto.getBoughtItems();
		String wantedItem = itemDto.getWanted();
		Integer missingGold = itemService.calculateMissingGold(boughtItems, wantedItem);
    	return new ResponseEntity<String>(missingGold.toString(), HttpStatus.OK);
	}

}
