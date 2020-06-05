package com.heropicker.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.heropicker.io.HeroLoader;
import com.heropicker.model.HeroDatabase;

@Configuration
public class Config {
	
	
	@Bean
	public KieContainer kieContainer() {		
		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks
				.newKieContainer(ks.newReleaseId("dota.heropicker", "integration-kjar", "0.0.1-SNAPSHOT"));
		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10_000);
		return kContainer;
	}
	
	@Bean
	public HeroDatabase heroDatabase() {
		HeroDatabase heroDatabase = null;
		try {
			heroDatabase = HeroLoader.loadHeroDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return heroDatabase;
	}
	

}
