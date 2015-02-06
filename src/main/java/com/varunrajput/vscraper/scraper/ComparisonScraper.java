package com.varunrajput.vscraper.scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.im.InputManager;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.om.OutputManager;
import com.varunrajput.vscraper.output.ComparisonOutput;
import com.varunrajput.vscraper.output.Output;
import com.varunrajput.vscraper.util.ClassUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;
import com.varunrajput.vscraper.util.URLUtil;
import com.varunrajput.vscraper.util.URLUtil.QueryMethod;

/**
 * 
 * @author varunrajput
 *
 */
public class ComparisonScraper implements Scraper {
  
  protected InputManager inputManager;
  protected OutputManager outputManager;
  
  protected List<String> comparisonPrefixes;
  
  protected Map<String, String> queryParamKeyMap;
  protected Map<String, String> baseUrlMap;
  protected Map<String, QueryMethod> queryMethodMap;
  protected Map<String, Map<String,String>> paramsMap;
  
  public ComparisonScraper() {
    comparisonPrefixes = new ArrayList<String>(Arrays.asList(PropertiesUtil.get(Property.ComparisonScrapingPrefixes).split(",")));
    
    queryParamKeyMap = new HashMap<String, String>();
    baseUrlMap = new HashMap<String, String>();
    queryMethodMap = new HashMap<String, QueryMethod>();
    paramsMap = new HashMap<String, Map<String, String>>();
    
    loadPrefixedParams();
  }
  
  public Output scrape(Input input) {
    ComparisonOutput comparisonOutput = ClassUtil.getNewInstance(ComparisonOutput.class, Property.OutputClass);
    
    for (String comparisonPrefix : comparisonPrefixes) {
      
      // Add query string to params map
      paramsMap.get(comparisonPrefix).put(queryParamKeyMap.get(comparisonPrefix), input.getQueryString());
      
      // Get response from end point
      String outputString = URLUtil.getOutputString(
          baseUrlMap.get(comparisonPrefix), paramsMap.get(comparisonPrefix), queryMethodMap.get(comparisonPrefix));
      
      // Store response in output object
      if (outputString != null && !outputString.isEmpty()) {
        comparisonOutput.populateFromString(comparisonPrefix, outputString, input);
      }
    }
    return comparisonOutput;
  }
  
  public void run() {
    Input input = null;
    while ((input = inputManager.getInput()) != null) {
      outputManager.logOutput(scrape(input));
    }
  }
  
  protected void loadPrefixedParams() {
    // For each comparison prefix
    for (String comparisonPrefix : comparisonPrefixes) {
      
      // Get query param key
      queryParamKeyMap.put(comparisonPrefix, PropertiesUtil.get(comparisonPrefix + Property.QueryParamKey,
          PropertiesUtil.get(Property.QueryParamKey)));
      
      // Get base url
      baseUrlMap.put(comparisonPrefix, PropertiesUtil.get(comparisonPrefix + Property.BaseUrl));
      
      // Get query method
      queryMethodMap.put(comparisonPrefix, QueryMethod.valueOf(PropertiesUtil.get(comparisonPrefix + Property.QueryMethod,
          PropertiesUtil.get(Property.QueryMethod))));
      
      // Get params to be appended to the query
      Map<String, String> params = new HashMap<String, String>();
      for (String paramKey : PropertiesUtil.get(comparisonPrefix + Property.ParamsToAppendToQuery).split(",")) {
        params.put(paramKey, PropertiesUtil.get(comparisonPrefix + paramKey));
      }
      paramsMap.put(comparisonPrefix, params);
    }
  }
  
  public void setInputManager(InputManager iManager) {
    inputManager = iManager;
  }
  
  public void setOutputManager(OutputManager oManager) {
    outputManager = oManager;
  }
}
