package com.heropicker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heropicker.dto.SettingsStatsDTO;
import com.heropicker.service.SettingsStatsService;

@CrossOrigin
@RestController
@RequestMapping("/api/settings")
public class SettingsStatsController {
	@Autowired
	private SettingsStatsService settingsStatsService;
	
	@GetMapping()
	public ResponseEntity<SettingsStatsDTO> getSettingsStats() {
        return new ResponseEntity<SettingsStatsDTO>(settingsStatsService.getSettingsStats(), HttpStatus.OK);
	}
	
	@GetMapping("/default")
	public ResponseEntity<SettingsStatsDTO> getDefaultSettingsStats() {
        return new ResponseEntity<SettingsStatsDTO>(settingsStatsService.getDefaultSettingsStats(), HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<String> postSettingsStats(@RequestBody SettingsStatsDTO settingsStats) {
        return new ResponseEntity<String>(settingsStatsService.postSettingsStats(settingsStats), HttpStatus.OK);
	} 
}
