package com.varunrajput.vscraper.scraper;

import com.varunrajput.vscraper.im.InputManager;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.om.OutputManager;
import com.varunrajput.vscraper.output.Output;

/**
 * @author varunrajput
 */
public interface Scraper extends Runnable {
  public void setInputManager(InputManager iManager);
  
  public void setOutputManager(OutputManager oManager);
  
  public Output scrape(Input input);
}