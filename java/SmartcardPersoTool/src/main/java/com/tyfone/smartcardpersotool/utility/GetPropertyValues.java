/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tyfone.smartcardpersotool.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Sureshbabu
 */
public class GetPropertyValues {
    String result = "";
	InputStream inputStream;
 
	public String getPropValue() throws IOException { 
		try {
			String resourceName = "config.properties"; // could also be a constant
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Properties props = new Properties();
                        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                            props.load(resourceStream);
                        
			// get the property value and print it out
			result = props.getProperty("version");
			
			System.out.println(result + "\nVersion=" + result);
                        }
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
                }
		return result;
	}
        
        public static String getPropValue(String key) throws IOException { 
            String response = "";
		try {
			String resourceName = "config.properties"; // could also be a constant
                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Properties props = new Properties();
                        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
                            props.load(resourceStream);
                        
			// get the property value and print it out
			 response = props.getProperty(key);
			
			System.out.println(response + "\nVersion=" + response);
                        }
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
                }
		return response;
	}
}
