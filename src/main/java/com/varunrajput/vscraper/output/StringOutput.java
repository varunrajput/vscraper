package com.varunrajput.vscraper.output;

import com.varunrajput.vscraper.input.Input;

public class StringOutput implements Output {
  
  public static final String TAB_DELIMITER = "\t";
  private String response;
  
  @Override
  public void populateFromString(String outputString, Input input) {
    response = input.getQueryString() + TAB_DELIMITER + outputString;
  }
  
  @Override
  public String writeFormatted() {
    return response;
  }
}