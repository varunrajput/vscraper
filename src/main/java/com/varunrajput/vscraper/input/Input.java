package com.varunrajput.vscraper.input;

/**
 * @author varunrajput
 */
public interface Input {
  public void populateQuery(String input);
  
  public String getQueryString();
}
