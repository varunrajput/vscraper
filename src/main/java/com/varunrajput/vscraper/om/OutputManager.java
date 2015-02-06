package com.varunrajput.vscraper.om;

import com.varunrajput.vscraper.output.Output;

/**
 * @author varunrajput
 */
public interface OutputManager {
  public void logOutput(Output output);
  
  public void generateOuputFile();
}