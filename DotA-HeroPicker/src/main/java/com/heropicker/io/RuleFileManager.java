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


public class RuleFileManager {
	
	public static InputStream loadTemplate(String fileName) {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/rules/integration/templates/" + fileName;
		try {
			return new FileInputStream(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String loadTemplateAsString(String fileName) {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/rules/integration/templates/" + fileName;
		return loadText(filePath);
	}
	
	
	public static String loadRuleFile(String fileName) {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/rules/integration/rules/" + fileName;
		return loadText(filePath);
	}
	
	public static String loadText(String filePath) {
		String content;
		try {
			content = FileUtils.readFileToString(new File(filePath));
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void exportHeroRules(String rules) {
		String filePath = System.getProperty("user.dir") + "/../integration-kjar/src/main/resources/rules/heroes/Heroes.drl";
		exportRules(rules, filePath);
	}
	
	public static void exportItemRules(String rules) {
		String filePath = System.getProperty("user.dir") + "/../integration-kjar/src/main/resources/rules/items/Items.drl";
		exportRules(rules, filePath);
	}
	
	public static void exportDdosRules(String rules) {
		String filePath = System.getProperty("user.dir") + "/../integration-kjar/src/main/resources/rules/ddos/DDOS.drl";
		exportRules(rules, filePath);
	}
	
	private static void exportRules(String rules, String filePath) {
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
