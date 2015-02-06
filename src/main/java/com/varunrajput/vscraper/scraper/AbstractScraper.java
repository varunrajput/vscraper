package com.varunrajput.vscraper.scraper;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.im.InputManager;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.om.OutputManager;
import com.varunrajput.vscraper.util.PropertiesUtil;
import com.varunrajput.vscraper.util.URLUtil.QueryMethod;

/**
 * @author varunrajput
 */
public abstract class AbstractScraper implements Scraper {
  protected static final Logger log = LoggerFactory
      .getLogger(AbstractScraper.class);
  
  protected InputManager inputManager;
  protected OutputManager outputManager;
  
  protected Map<String,String> params;
  protected static String queryParamKey;
  protected static String baseUrl;
  protected static QueryMethod queryMethod;
  
  protected AbstractScraper() {
    loadParams();
    queryParamKey = PropertiesUtil.get(Property.QueryParamKey);
    baseUrl = PropertiesUtil.get(Property.BaseUrl);
    queryMethod = QueryMethod.valueOf(PropertiesUtil.get(Property.QueryMethod));
  }
  
  public void setInputManager(InputManager iManager) {
    inputManager = iManager;
  }
  
  public void setOutputManager(OutputManager oManager) {
    outputManager = oManager;
  }
  
  public void run() {
    Input input = null;
    while ((input = inputManager.getInput()) != null) {
      outputManager.logOutput(scrape(input));
    }
  }
  
  private void loadParams() {
    if (params == null) {
      params = new HashMap<String,String>();
      String paramKeys = PropertiesUtil.get(Property.ParamsToAppendToQuery, "");
      if (!paramKeys.isEmpty()) {
        for (String paramKey : paramKeys.split(",")) {
          String paramValue = PropertiesUtil.get(paramKey);
          if (paramValue != null && !paramValue.isEmpty()) {
            params.put(paramKey, paramValue);
          }
        }
      }
    }
  }
}