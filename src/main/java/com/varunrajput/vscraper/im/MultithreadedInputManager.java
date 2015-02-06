package com.varunrajput.vscraper.im;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * @author varunrajput
 */
public class MultithreadedInputManager extends AbstractInputManager {
  private static final Logger log = LoggerFactory.getLogger(MultithreadedInputManager.class);
  
  private ConcurrentLinkedQueue<Input> queries = null;
  private Integer totalQueries = 0;
  private Boolean QPSisToBeRegulated = false;
  private Integer sleepInterval;
  
  public MultithreadedInputManager() {
    queries = new ConcurrentLinkedQueue<Input>(
        loadInput(PropertiesUtil.get(Property.InputQueriesFile)));
    totalQueries = queries.size();
    
    Integer QPS = Integer.parseInt(PropertiesUtil.get(Property.QPS));
    QPSisToBeRegulated = QPS > 0;
    sleepInterval = QPSisToBeRegulated ? (int) ((1.0 / QPS) * 1000) : 0;
  }
  
  public Integer totalInputs() {
    return totalQueries;
  }
  
  public Integer inputsRemaining() {
    return queries.size();
  }
  
  public boolean hasMoreInputs() {
    return !queries.isEmpty();
  }
  
  public Input getInput() {
    Input query = null;
    try {
      query = queries.remove();
    } catch (NoSuchElementException e) {
      
    }
    if (QPSisToBeRegulated) {
      synchronized(this) {
        try {
          Thread.sleep(sleepInterval);
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return query;
  }
}
