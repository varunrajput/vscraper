package com.varunrajput.vscraper.input;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author varunrajput
 */
public class HeaderedDelimitedInput implements Input {
  private static final Logger log = LoggerFactory.getLogger(HeaderedDelimitedInput.class);
  private static final Pattern delimiter = Pattern.compile(
    PropertiesUtil.get(Property.HeaderedDelimitedInputDelimiter));

  private static String[] paramsHeader;

  private Map<String, String> paramValueMap;

  static {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(
        PropertiesUtil.get(Property.InputQueriesFile)));
      String line = reader.readLine();
      if (line != null && !line.trim().isEmpty()) {
        paramsHeader = delimiter.split(line);
      }
      reader.close();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }

    //TODO: Ignore first input since it's the header
  }

  @Override
  public void populateQuery(final String input) {
    paramValueMap = new HashMap<String, String>();
    String[] paramValues = delimiter.split(input);

    for (int i = 0; i < paramValues.length; i++) {
      if (i < paramsHeader.length && !paramValues[i].isEmpty()) {
        paramValueMap.put(paramsHeader[i], paramValues[i]);
      }
    }
  }

  @Override
  public String getQueryString() {
    return paramValueMap.toString();
  }

  @Override
  public Map<String, String> getDynamicParams() {
    return Collections.unmodifiableMap(paramValueMap);
  }
}