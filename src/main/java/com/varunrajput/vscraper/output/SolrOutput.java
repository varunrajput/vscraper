package com.varunrajput.vscraper.output;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.input.TabSeparatedInput;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * @author varunrajput
 */
public class SolrOutput extends AbstractSolrOutput {
  
  protected static final String lineSeparator = "\n";
  
  protected static final String fieldSeparator = PropertiesUtil.get(
      Property.SolrOutputFieldSeparator, "\t");
  
  protected static final Set<String> fieldsToExtract = new LinkedHashSet<String>(
      Arrays.asList(PropertiesUtil.get(Property.SolrOutputFieldsToExtract, "*")
          .split(",")));
  
  protected static final String EMPTY_VALUE = "";
  
  @Override
  public void populateFromString(String outputString, Input input) {
    this.input = (TabSeparatedInput) input;
    
    if (outputString != null && !outputString.isEmpty()) {
      SolrOutput parsedOutput = gson.fromJson(outputString,
          SolrOutput.class);
      this.responseHeader = parsedOutput.responseHeader;
      this.response = parsedOutput.response;
      this.clusters = parsedOutput.clusters;
    }
  }
  
  @Override
  public String writeFormatted() {
    if (response == null) {
      return input.getQueryString() + fieldSeparator + "ERROR";
    }
    
    List<Map<String,Object>> docList = response.getDocs();
    
    // Return if response is empty
    if (response.getNumFound() < 1 || docList.size() < 1) {
      return input.getQueryString() + fieldSeparator + "NO RESULTS";
    }
    
    StringBuilder outputBuilder = new StringBuilder();
    
    // Iterate over documents
    for (int docIdx = 0; docIdx < docList.size(); docIdx++) {
      Map<String,Object> document = docList.get(docIdx);
      
      Set<String> keysToIterate = fieldsToExtract.contains("*") ? document
          .keySet() : fieldsToExtract;
          
          StringBuilder lineBuilder = new StringBuilder();
          
          // Add query
          lineBuilder.append(input.getQueryString()).append(fieldSeparator);
          
          // Add document position
          lineBuilder.append(docIdx + 1).append(fieldSeparator);
          
          // Iterate over fields to be extracted from each document
          for (String key : keysToIterate) {
            if (document.containsKey(key)) {
              lineBuilder.append(document.get(key)).append(fieldSeparator);
            } else {
              lineBuilder.append(EMPTY_VALUE).append(fieldSeparator);
            }
          }
          
          // Append input metadata if any
          lineBuilder.append(Joiner.on(fieldSeparator).join(input.getMetadata()));
          
          // Add line to output
          outputBuilder.append(lineBuilder.toString().trim()).append(lineSeparator);
    }
    
    return outputBuilder.toString().trim();
  }
}
