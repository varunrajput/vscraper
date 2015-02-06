package com.varunrajput.vscraper.input;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * @author varunrajput
 */
public class TabSeparatedInput implements Input {
  private List<String> inputMetadata;
  private String query;
  
  private static final Integer queryFieldIdx = Integer.parseInt(PropertiesUtil
      .get(Property.TabSeparatedInputQueryFieldIndex, "0"));
  
  public void populateQuery(String input) {
    String[] fields = input.split("\t");
    inputMetadata = new ArrayList<String>(fields.length - 1);
    for (int i = 0; i < fields.length; i++) {
      if (i == queryFieldIdx) {
        query = fields[i];
      } else {
        inputMetadata.add(fields[i]);
      }
    }
  }
  
  public String getQueryString() {
    return query;
  }
  
  public List<String> getMetadata() {
    return Collections.unmodifiableList(inputMetadata);
  }
}