package com.varunrajput.vscraper.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.im.InputManager;
import com.varunrajput.vscraper.util.PropertiesUtil;

public class ProgressTrackingStatsManager implements StatsManager {
  private static final Logger log = LoggerFactory
      .getLogger(ProgressTrackingStatsManager.class);
  
  private static final Boolean enabled = Boolean.parseBoolean(PropertiesUtil
      .get(Property.ProgressTrackingStatsManagerEnable));
  
  private Thread statsTrackerThread;
  private StatsTracker statsTracker;
  
  public ProgressTrackingStatsManager(InputManager inputManager) {
    if (enabled) {
      statsTracker = new StatsTracker(Long.parseLong(PropertiesUtil
          .get(Property.ProgressTrackingStatsManagerInterval)),
          Boolean.parseBoolean(PropertiesUtil
              .get(Property.ProgressTrackingStatsManagerWriteToConsole)),
              inputManager);
      
      statsTrackerThread = new Thread(statsTracker);
    }
  }
  
  @Override
  public void start() {
    if (enabled) {
      statsTrackerThread.start();
    }
  }
  
  @Override
  public void stop() {
    if (enabled) {
      statsTrackerThread.interrupt();
      statsTracker.run = false;
      try {
        statsTrackerThread.join();
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }
    }
  }
  
  private class StatsTracker implements Runnable {
    volatile boolean run = true;
    
    private Long interval;
    private Boolean writeToConsole;
    private InputManager inputManager;
    
    public StatsTracker(Long interval, Boolean writeToConsole,
        InputManager inputManager) {
      this.interval = interval;
      this.writeToConsole = writeToConsole;
      this.inputManager = inputManager;
    }
    
    @Override
    public void run() {
      while (run) {
        String statsMessage = "Progress: "
            + ((double) inputManager.totalInputs() - inputManager
                .inputsRemaining()) / inputManager.totalInputs() * 100.0
                + "% Checking again in " + interval + " ms";
        
        log.info(statsMessage);
        if (writeToConsole) {
          System.out.println(statsMessage);
        }
        
        try {
          Thread.sleep(interval);
        } catch (InterruptedException e) {}
      }
      
      log.info("Scraping Complete");
      if (writeToConsole) {
        System.out.println("Scraping Complete");
      }
    }
  }
}