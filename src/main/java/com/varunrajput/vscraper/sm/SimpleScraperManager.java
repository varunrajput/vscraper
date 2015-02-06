package com.varunrajput.vscraper.sm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.im.InputManager;
import com.varunrajput.vscraper.om.OutputManager;
import com.varunrajput.vscraper.scraper.Scraper;
import com.varunrajput.vscraper.util.ClassUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * @author varunrajput
 */
public class SimpleScraperManager implements ScraperManager {
  private static final Logger log = LoggerFactory
      .getLogger(SimpleScraperManager.class);
  
  private InputManager queryManager;
  private OutputManager outputManager;
  
  private Integer nScrapers = Integer.parseInt(PropertiesUtil
      .get(Property.NumScrapers));
  
  private Thread[] scrapers;
  
  public SimpleScraperManager() {
    queryManager = ClassUtil.getNewInstance(InputManager.class,
        Property.InputManagerClass);
    outputManager = ClassUtil.getNewInstance(OutputManager.class,
        Property.OutputManagerClass);
    
    scrapers = new Thread[nScrapers];
    for (int i = 0; i < nScrapers; i++) {
      
      Scraper scraper = ClassUtil.getNewInstance(Scraper.class,
          Property.ScraperClass);
      scraper.setInputManager(queryManager);
      scraper.setOutputManager(outputManager);
      
      scrapers[i] = new Thread(scraper);
    }
  }
  
  public void runScrapers() {
    if (nScrapers < 0) {
      log.error("Unable to run. Scrapers have not been initialized");
    }
    
    for (int i = 0; i < nScrapers; i++) {
      scrapers[i].start();
    }
    
    for (int i = 0; i < nScrapers; i++) {
      try {
        scrapers[i].join();
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }
    }
    
    outputManager.generateOuputFile();
  }
}