/* 
 * © 2004-2015, Tyfone, Inc. All rights reserved.  
 * Patented and other Patents pending.  
 * All trademarks are property of their respective owners. 
 * MultiChannel Banking is the trademarks of Tyfone, Inc. 
 * For more information visit: www.tyfone.com 
 */
package com.tyfone.smartcardpersotool.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to map model to json and vice versa.
 * 
 * @author Sagar Wanojwar
 * 
 */
public final class JacksonParser {

	private ObjectMapper mapper;
	private static JacksonParser parser;
	private static final String TAG = JacksonParser.class.getSimpleName();

	private JacksonParser() {
		super();
		mapper = new ObjectMapper();
	}

	public static JacksonParser getInstance() {
		if (null == parser) {
			parser = new JacksonParser();
		}
		return parser;
	}

	public Object mapStringToModel(Class<?> modelClass, String jsonString) {
		Object modelObject = null;

		try {
			modelObject = mapper.readValue(jsonString, modelClass);
		} catch (JsonParseException e) {
			e.printStackTrace();
			System.out.println("Map string to model JsonParseException-" + e.getMessage());
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Map string to model JsonMappingException-" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Map string to model IOException-" + e.getMessage());
		}

		return modelObject;
	}

	public String mapModelToString(Object modelObject) {
		String jsonString = null;

		try {
			jsonString = mapper.writeValueAsString(modelObject);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			System.out.println("Map model to string JsonMappingException-" + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Map model to string IOException-" + e.getMessage());
		}

		return jsonString;
	}
}
