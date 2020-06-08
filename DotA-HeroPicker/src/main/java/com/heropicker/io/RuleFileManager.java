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
		String filePath = System.getProperty("user.dir") + "/src/main/resources/rules/" + fileName;
		try {
			return new FileInputStream(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String loadRuleFile(String fileName) {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/rules/" + fileName;
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
