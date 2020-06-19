package com.heropicker.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heropicker.dto.SettingsStatsDTO;
import com.heropicker.model.items.Item;
import com.heropicker.model.items.ItemDatabase;

public class ItemLoader {
	
	public static ItemDatabase loadItemDatabase() {
		String filePath = System.getProperty("user.dir") + "/src/main/resources/data/item_data/items.json";
		String content = null;
		try {
			content = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		ArrayList<Item> items = gson.fromJson(content, new TypeToken<ArrayList<Item>>() {}.getType());
		return new ItemDatabase(items);
	}

}
