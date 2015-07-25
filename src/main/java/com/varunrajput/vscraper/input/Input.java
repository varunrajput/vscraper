package com.varunrajput.vscraper.input;

import java.util.Map;

/**
 * @author varunrajput
 */
public interface Input {
  public void populateQuery(String input);
  
  public String getQueryString();

  /**
   * This method supports addition of parameters to the
   * HTTP call query at run-time, i.e. decided based on
   * input record provided. Default returns: null
   * @return  map of params to add, decided at run-time
   */
  public Map<String, String> getDynamicParams();
}
