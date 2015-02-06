package com.varunrajput.vscraper.output;

import com.varunrajput.vscraper.input.Input;

/**
 * @author varunrajput
 */
public interface Output {
  public void populateFromString(String outputString, Input input);
  
  public String writeFormatted();
}