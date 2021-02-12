package com.mongoxmltojson.mongoxmltojson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class MongoXmlToJsonApplication {


	public static void main(String[] args) throws FileNotFoundException {

		Reader reader = new FileReader("/Users/pulkitpushkarna/Downloads/IA_FIRM_SEC_Feed_02_11_2021.xml");
		JSONObject jsonObject = XML.toJSONObject(reader);
		ApplicationContext applicationContext = SpringApplication.run(MongoXmlToJsonApplication.class, args);
		MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
		jsonObject.keySet().forEach(e -> {
					JSONObject json = (JSONObject) jsonObject.get(e);
					String genOn = json.get("GenOn").toString();
					JSONObject jsonObject1 = (JSONObject) json.get("Firms");
					JSONArray jsonArray = (JSONArray) jsonObject1.get("Firm");
				List<JSONObject> jsonObjectList = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						((JSONObject) jsonArray.get(i)).put("GenOn", genOn);
						jsonObjectList.add((JSONObject) jsonArray.get(i));
					}
					jsonObjectList.parallelStream().forEach(e1->{
						mongoTemplate.insert(e1, "demo");
					});
				}
		);
		
	}

}
