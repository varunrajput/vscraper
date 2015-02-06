package com.varunrajput.vscraper.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author varunrajput
 */
public class PropertiesUtil {
  private static Properties properties;
  private static final Logger log = LoggerFactory
      .getLogger(PropertiesUtil.class);
  
  static {
    initializeDefaults();
  }
  
  private static void initializeDefaults() {
    properties = new Properties();
    try {
      properties.load(PropertiesUtil.class.getClassLoader()
          .getResourceAsStream("vscraper.properties"));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
  
  public static void overrideDefaults(String externalPropertiesFile)
      throws Exception {
    Properties externalProperties = new Properties();
    externalProperties.load(new BufferedReader(new FileReader(
        externalPropertiesFile)));
    properties.putAll(externalProperties);
  }
  
  public static void restoreDefaults() {
    initializeDefaults();
  }
  
  public static void set(String propertyName, String propertyValue) {
    properties.put(propertyName, propertyValue);
  }
  
  public static String get(String property) {
    return properties.getProperty(property);
  }
  
  public static String get(String property, String defaultValue) {
    return properties.getProperty(property, defaultValue);
  }
}