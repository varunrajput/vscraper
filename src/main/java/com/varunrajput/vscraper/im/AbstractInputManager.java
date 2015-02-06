package com.varunrajput.vscraper.im;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.util.ClassUtil;

/**
 * @author varunrajput
 */
public abstract class AbstractInputManager implements InputManager {
  private static final Logger log = LoggerFactory
      .getLogger(AbstractInputManager.class);
  
  protected String lastLoadedInputFile = null;
  
  protected List<Input> loadInput(String queriesFile) {
    lastLoadedInputFile = queriesFile;
    
    List<Input> inputs = new ArrayList<Input>();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(queriesFile));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (!line.isEmpty()) {
          try {
            Input input = ClassUtil.getNewInstance(Input.class,
                Property.InputClass);
            input.populateQuery(line.trim());
            inputs.add(input);
          } catch (Exception e) {
            
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
    return inputs;
  }
  
  public void reloadInput() {
    if (lastLoadedInputFile == null) {
      log.error("Cannot reload queries. No queries loaded previously");
    }
    loadInput(lastLoadedInputFile);
  }
}