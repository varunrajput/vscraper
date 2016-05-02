package com.varunrajput.vscraper.sm;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.im.InputManager;
import com.varunrajput.vscraper.om.OutputManager;
import com.varunrajput.vscraper.scraper.Scraper;
import com.varunrajput.vscraper.stats.ProgressTrackingStatsManager;
import com.varunrajput.vscraper.util.ClassUtil;
import com.varunrajput.vscraper.util.MailUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author varunrajput
 */
public class SimpleScraperManager implements ScraperManager {
  private static final Logger log = LoggerFactory
      .getLogger(SimpleScraperManager.class);

  private InputManager queryManager;
  private OutputManager outputManager;
  private ProgressTrackingStatsManager progressTrackingStatsManager;

  private Integer nScrapers = Integer.parseInt(PropertiesUtil
      .get(Property.NumScrapers));

  private Thread[] scrapers;

  private final Thread.UncaughtExceptionHandler threadExceptionHandler = new Thread.UncaughtExceptionHandler() {
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
      log.error(e.getMessage(), e);
      MailUtil.sendFailedIfRequested(e); }
  };

  public SimpleScraperManager() {
    queryManager = ClassUtil.getNewInstance(InputManager.class,
        Property.InputManagerClass);
    outputManager = ClassUtil.getNewInstance(OutputManager.class,
        Property.OutputManagerClass);

    progressTrackingStatsManager = new ProgressTrackingStatsManager(queryManager);
    progressTrackingStatsManager.start();

    scrapers = new Thread[nScrapers];
    for (int i = 0; i < nScrapers; i++) {

      Scraper scraper = ClassUtil.getNewInstance(Scraper.class,
          Property.ScraperClass);
      scraper.setInputManager(queryManager);
      scraper.setOutputManager(outputManager);

      scrapers[i] = new Thread(scraper);
      scrapers[i].setUncaughtExceptionHandler(threadExceptionHandler);
    }
  }

  @Override
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
    progressTrackingStatsManager.stop();
    outputManager.generateOuputFile();
  }
}