package com.varunrajput.vscraper.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.DelimitedInput;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.util.PropertiesUtil;

import java.util.*;

public class SolrComparisonDecisionOutput extends ComparisonOutput {
  protected static final Gson gson = new GsonBuilder().create();
  
  protected static final List<String> comparisonPrefixes = new ArrayList<String>(
      new LinkedHashSet<String>(
          Arrays.asList(
              PropertiesUtil.get(Property.ComparisonScrapingPrefixes).split(","))));
  
  protected Map<String, SolrOutput> solrOutputMap;
  protected DelimitedInput input;
  
  protected List<String> fieldsToCompare = new ArrayList<String>(
      Arrays.asList(PropertiesUtil.get(
          Property.SolrComparisonOutputFieldsToCompare).split(",")));
  
  protected static final String fieldSeparator = "\t";
  protected static final String lineSeparator = "\n";
  
  public SolrComparisonDecisionOutput() {
    solrOutputMap = new HashMap<String, SolrOutput>();
  }
  
  @Override
  public void populateFromString(String comparisonPrefix, String outputString,
      Input input) {
    
    if (this.input == null) {
      this.input = (DelimitedInput) input;
    }
    
    if (outputString != null && !outputString.isEmpty()) {
      SolrOutput parsedOutput = gson.fromJson(outputString, SolrOutput.class);
      solrOutputMap.put(comparisonPrefix, parsedOutput);
    }
  }
  
  @Override
  public String writeFormatted() {
    StringBuilder sb = new StringBuilder();
    
    for (String fieldToCompare : fieldsToCompare) {
      sb.append(input.getQueryString()).append(fieldSeparator);
      sb.append(fieldToCompare).append(fieldSeparator);
      for (int prefIdx = 0; prefIdx < comparisonPrefixes.size(); prefIdx++) {
        if (prefIdx == 0) {
          sb.append(solrOutputMap.get(comparisonPrefixes.get(prefIdx)).response.getDocs());
        } else {
          
        }
      }
    }
    
    return sb.toString();
  }
}
