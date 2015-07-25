package com.varunrajput.vscraper.scraper;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.output.Output;
import com.varunrajput.vscraper.util.ClassUtil;
import com.varunrajput.vscraper.util.URLUtil;

import java.util.Map;

/**
 * @author varunrajput
 */
public class SimpleScraper extends AbstractScraper {
  
  @Override
  public Output scrape(Input input) {
    // Set the query
    params.put(queryParamKey, input.getQueryString());

    Map<String, String> dynamicParams = input.getDynamicParams();

    if(dynamicParams != null && dynamicParams.size() > 0) {
      params.putAll(dynamicParams);
    }

    Output output = ClassUtil.getNewInstance(Output.class, Property.OutputClass);
    
    output.populateFromString(
        URLUtil.getOutputString(baseUrl, params, queryMethod),
        input);
    
    return output;
  }
}