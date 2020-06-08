package com.heropicker.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.heropicker.templating.RoleConfiguration;

import aj.org.objectweb.asm.Type;



public class RuleFileManager {
	
	public static InputStream loadTemplate(String fileName) {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/data/rule_configuration/templates/" + fileName;
		try {
			return new FileInputStream(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static ArrayList<RoleConfiguration> loadRoleConfigurations(String fileName) {
//		String filePath = System.getProperty("user.dir") + "/src/main/resources/data/rule_configuration/template_configs/" + fileName;
//		
//		Gson gson = new Gson();
//		
//		ArrayList<RoleConfiguration> roleConfigs = new ArrayList<RoleConfiguration>();
//
//			
//		
//			
//		
//	}
	
	public static void dumpConfigObjects(ArrayList<Object> configObjects, String configFileName) {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/data/rule_configuration/template_configs/" + configFileName;
		
		Gson gson = new Gson();
		String json = gson.toJson(configObjects);
		
		try {
			PrintWriter out = new PrintWriter(filePath);
			out.println(json);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static String loadActiveRules() {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/data/rule_configuration/active_rules.drl";
		String content;
		try {
			content = FileUtils.readFileToString(new File(filePath));
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void exportNewRules(String rules) {
		String filePath = System.getProperty("user.dir") + "/../integration-kjar/src/main/resources/com/sample/Sample.drl";
		try {
			PrintWriter out = new PrintWriter(filePath);
			out.println(rules);
			out.flush();
			out.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
