package com.varunrajput.vscraper.input;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.util.PropertiesUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * This is a generic implementation for delimited input types. The default delimiter is '\t'
 *
 * @author varunrajput
 */
public class DelimitedInput implements Input {
  protected List<String> inputMetadata;
  protected String query;

  private static final Pattern delimiter = Pattern.compile(PropertiesUtil.get(Property.DelimitedInputDelimiter));

  private static final Integer queryFieldIdx = Integer.parseInt(PropertiesUtil
      .get(Property.DelimitedInputQueryFieldIndex));
  
  @Override
  public void populateQuery(String input) {
    String[] fields = delimiter.split(input);
    inputMetadata = new ArrayList<String>(fields.length - 1);
    for (int i = 0; i < fields.length; i++) {
      if (i == queryFieldIdx) {
        query = fields[i];
      } else {
        inputMetadata.add(fields[i]);
      }
    }
  }
  
  @Override
  public String getQueryString() {
    return query;
  }

  @Override
  public Map<String, String> getDynamicParams() {
    return null;
  }

  public List<String> getMetadata() {
    return Collections.unmodifiableList(inputMetadata);
  }
}