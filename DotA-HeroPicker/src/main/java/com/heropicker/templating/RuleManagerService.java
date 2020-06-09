package com.heropicker.templating;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.drools.template.ObjectDataCompiler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.heropicker.dto.SettingsStatsDTO;
import com.heropicker.io.RuleFileManager;


@Service
public class RuleManagerService {
	
	@Autowired
	private Environment env;
	
	public void triggerMvnInstall() {
		
		
		boolean autoTrigger = env.getProperty("config.mvn.autoTrigger").equals("true");
		
		if (autoTrigger) {
			String projectPath = System.getProperty("user.dir") + "/../integration-kjar/pom.xml";
			
			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile( new File( projectPath ) );
			request.setGoals( Arrays.asList( "clean", "install" ) );

			Invoker invoker = new DefaultInvoker();
			String var = env.getProperty("config.mvn.path");
			invoker.setMavenHome(new File(var));
			try {
				invoker.execute( request );
			} catch (MavenInvocationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void exportChangedRules(SettingsStatsDTO settings) {
		
		String basicRules = RuleFileManager.loadRuleFile("basicRules.txt");
		String roleRules = applyRoleTemplate(settings);
		String preferenceRules = applyPreferencesTemplate(settings);
		String userRules = settings.getUserAddedRules();
		String wholeFile = basicRules + roleRules + preferenceRules + userRules;
		RuleFileManager.exportNewRules(wholeFile);		
	}
	
	public String applyPreferencesTemplate(SettingsStatsDTO settings) {
		
		HashMap<String, Object> args = new HashMap<String, Object>();
		
		args.put("preferredHeroFactor", settings.getPreferredHeroFactor());
		args.put("preferredLaneFactor", settings.getPreferredLaneFactor());
		args.put("preferredRoleFactor", settings.getPreferredRoleFactor());
		args.put("enemyHeroDisadvantage", new Double(settings.getEnemyHeroDisadvantage()));
		args.put("enemyLaneHeroDisadvantage", new Double(settings.getEnemyLaneHeroDisadvantage()));
		
		ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
		InputStream rolesTemplateStream = RuleFileManager.loadTemplate("preferences.drt");
		
		
		ArrayList<Object> argsList = new ArrayList<Object>();
		argsList.add(args);
		return objectDataCompiler.compile(argsList, rolesTemplateStream);
	}
	
	
	public String applyRoleTemplate(SettingsStatsDTO settings) {
		
		ArrayList<Object> templateArgs = new ArrayList<Object>();
		
		HashMap<String, Object> nukerMap = new HashMap<String, Object>();
		nukerMap.put("role", "Nuker");
		nukerMap.put("heroesPerRole", settings.getNuker().getHeroesPerRole());
		nukerMap.put("scoreLossPercentage", settings.getNuker().getScoreLossPercentage());
		
		HashMap<String, Object> escapeMap = new HashMap<String, Object>();
		escapeMap.put("role", "Escape");
		escapeMap.put("heroesPerRole", settings.getEscape().getHeroesPerRole());
		escapeMap.put("scoreLossPercentage", settings.getEscape().getScoreLossPercentage());
		
		HashMap<String, Object> supportMap = new HashMap<String, Object>();
		supportMap.put("role", "Support");
		supportMap.put("heroesPerRole", settings.getSupport().getHeroesPerRole());
		supportMap.put("scoreLossPercentage", settings.getSupport().getScoreLossPercentage());
		
		HashMap<String, Object> initiatorMap = new HashMap<String, Object>();
		initiatorMap.put("role", "Initiator");
		initiatorMap.put("heroesPerRole", settings.getInitiator().getHeroesPerRole());
		initiatorMap.put("scoreLossPercentage", settings.getInitiator().getScoreLossPercentage());
		
		HashMap<String, Object> carryMap = new HashMap<String, Object>();
		carryMap.put("role", "Carry");
		carryMap.put("heroesPerRole", settings.getCarry().getHeroesPerRole());
		carryMap.put("scoreLossPercentage", settings.getCarry().getScoreLossPercentage());
		
		HashMap<String, Object> junglerMap = new HashMap<String, Object>();
		junglerMap.put("role", "Jungler");
		junglerMap.put("heroesPerRole", settings.getJungler().getHeroesPerRole());
		junglerMap.put("scoreLossPercentage", settings.getJungler().getScoreLossPercentage());
		
		HashMap<String, Object> durableMap = new HashMap<String, Object>();
		durableMap.put("role", "Durable");
		durableMap.put("heroesPerRole", settings.getDurable().getHeroesPerRole());
		durableMap.put("scoreLossPercentage", settings.getDurable().getScoreLossPercentage());
		
		HashMap<String, Object> disablerMap = new HashMap<String, Object>();
		disablerMap.put("role", "Disabler");
		disablerMap.put("heroesPerRole", settings.getDisabler().getHeroesPerRole());
		disablerMap.put("scoreLossPercentage", settings.getDisabler().getScoreLossPercentage());
		
		HashMap<String, Object> pusherMap = new HashMap<String, Object>();
		pusherMap.put("role", "Pusher");
		pusherMap.put("heroesPerRole", settings.getPusher().getHeroesPerRole());
		pusherMap.put("scoreLossPercentage", settings.getPusher().getScoreLossPercentage());
		
		templateArgs.add(nukerMap);
		templateArgs.add(escapeMap);
		templateArgs.add(supportMap);
		templateArgs.add(initiatorMap);
		templateArgs.add(carryMap);
		templateArgs.add(junglerMap);
		templateArgs.add(durableMap);
		templateArgs.add(disablerMap);
		templateArgs.add(pusherMap);
		
		ObjectDataCompiler objectDataCompiler = new ObjectDataCompiler();
		InputStream rolesTemplateStream = RuleFileManager.loadTemplate("roles.drt");
		
		return objectDataCompiler.compile(templateArgs, rolesTemplateStream);
	}
	
	
	
	
	

}
