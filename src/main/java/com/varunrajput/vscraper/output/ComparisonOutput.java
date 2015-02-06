package com.varunrajput.vscraper.output;

import com.varunrajput.vscraper.input.Input;

/**
 * 
 * @author varunrajput
 *
 */
public abstract class ComparisonOutput implements Output{
  public void populateFromString(String outputString, Input input) {
    throw new UnsupportedOperationException(
        "If using comparison output, use the populateFromString(String comparisonPrefix, String outputString, Input input)");
  }
  
  public abstract void populateFromString(String comparisonPrefix, String outputString, Input input);
}