package com.varunrajput.vscraper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.sm.ScraperManager;
import com.varunrajput.vscraper.util.ClassUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * 
 * This is the main entry point of the VScraper framework. To use this as an
 * external library, make use of the run methods to do the scraping.
 * 
 * @author varunrajput
 */
public class VScraper {
  private static final Logger log = LoggerFactory.getLogger(VScraper.class);
  
  /**
   * 
   * This method will run the scraping with the default properties within the
   * framework. You can set desired properties programmatically through the
   * setter method setProperty(String propertyName, String propertyValue) or
   * pass your own properties file to the run(String propertiesFile) method. The
   * property keys are available in the {@link Property} interface.
   * 
   */
  public static void run() {
    run(null);
  }
  
  /**
   * 
   * This method will run the scraping with external properties file provided in
   * the arguments. Default property values will be used if they are not
   * available in the external properties file.
   * 
   * @param propertiesFile
   */
  public static void run(String propertiesFile) {
    
    checkAndLoadExternalPropertiesFile(propertiesFile);
    
    ScraperManager scraperManager = ClassUtil.getNewInstance(
        ScraperManager.class, Property.ScraperManagerClass);
    
    scraperManager.runScrapers();
    
    PropertiesUtil.restoreDefaults();
  }
  
  public static void setProperty(String propertyName, String propertyValue) {
    PropertiesUtil.set(propertyName, propertyValue);
  }
  
  public static void setPropertyForComparison(String comparisonPrefix, String propertyName, String propertyValue) {
    PropertiesUtil.set(comparisonPrefix + propertyName, propertyValue);
  }
  
  private static void checkAndLoadExternalPropertiesFile(String propertiesFile) {
    if (propertiesFile != null && !propertiesFile.isEmpty()) {
      try {
        PropertiesUtil.overrideDefaults(propertiesFile);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }
  
  public static void main(String[] args) {
    if (args.length > 0) {
      run(args[0]);
    } else {
      run(null);
    }
  }
}