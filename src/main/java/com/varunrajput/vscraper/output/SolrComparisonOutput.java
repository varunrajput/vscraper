package com.varunrajput.vscraper.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.DelimitedInput;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.util.PropertiesUtil;

import java.util.*;

/**
 * 
 * @author varunrajput
 *
 */
public class SolrComparisonOutput extends ComparisonOutput {
  
  public static enum FormatMode {
    rpc, rpl;
  }
  
  protected static final Gson gson = new GsonBuilder().create();
  
  protected static final FormatMode formatMode = FormatMode.valueOf(
      PropertiesUtil.get(Property.SolrComparisonOutputFormatMode));
  
  protected static final String lineSeparator = "\n";
  
  protected static final String fieldSeparator = PropertiesUtil.get(
      Property.SolrComparisonOutputFieldSeparator, "\t");
  
  protected static final Set<String> fieldsToCompare = new LinkedHashSet<String>(
      Arrays.asList(PropertiesUtil.get(Property.SolrComparisonOutputFieldsToCompare, "*")
          .split(",")));
  
  protected static final Integer numResultsToCompare = Integer.parseInt(
      PropertiesUtil.get(Property.SolrComparisonOutputNumResultsToCompare));
  
  protected static final List<String> comparisonPrefixes = new ArrayList<String>(
      new LinkedHashSet<String>(
          Arrays.asList(
              PropertiesUtil.get(Property.ComparisonScrapingPrefixes).split(","))));
  
  protected Map<String, SolrOutput> solrOutputMap;
  protected DelimitedInput input;
  
  public SolrComparisonOutput() {
    solrOutputMap = new HashMap<String, SolrOutput>();
  }
  
  @Override
  public void populateFromString(String comparisonPrefix, String outputString,
      Input input) {
    
    this.input = (DelimitedInput) input;
    
    if (outputString != null && !outputString.isEmpty()) {
      SolrOutput parsedOutput = gson.fromJson(outputString, SolrOutput.class);
      solrOutputMap.put(comparisonPrefix, parsedOutput);
    }
  }
  
  @Override
  public String writeFormatted() {
    switch (formatMode) {
      case rpc:
        return writeRankPerColumn();
      case rpl:
        return writeRankPerLine();
      default:
        return writeRankPerLine();
    }
  }
  
  public String writeRankPerColumn() {
    StringBuilder sb = new StringBuilder();
    
    for (String fieldToCompare : fieldsToCompare) {
      
      for (int i = 0; i < comparisonPrefixes.size(); i++) {
        
        sb.append(input.getQueryString()).append(fieldSeparator);
        sb.append(fieldToCompare).append(fieldSeparator);
        sb.append(comparisonPrefixes.get(i)).append(fieldSeparator);
        
        SolrOutput solrOutput = solrOutputMap.get(comparisonPrefixes.get(i));
        if (solrOutput == null) {
          sb.append("NO_SOLR_OUTPUT").append(lineSeparator);
          continue;
        }
        
        List<Map<String, Object>> solrDocuments = solrOutput.response.getDocs();
        
        Integer ranksToCheck = numResultsToCompare < solrDocuments.size() ? numResultsToCompare : solrDocuments.size();
        
        for (int rank = 0; rank < ranksToCheck; rank++) {
          
          Map<String, Object> solrDocument = solrDocuments.get(rank);
          
          if (solrDocument.containsKey(fieldToCompare)) {
            
            if (i == 0) {
              sb.append(solrDocument.get(fieldToCompare)).append(fieldSeparator);
            } else {
              sb.append(getComparisonResult(solrDocument.get(fieldToCompare), fieldToCompare)).append(fieldSeparator);
            }
            
          } else {
            sb.append(fieldToCompare + "_NOT_PRESENT").append(fieldSeparator);
          }
        }
        sb.append(lineSeparator);
      }
    }
    return sb.toString().trim();
  }
  
  public String writeRankPerLine() {
    StringBuilder sb = new StringBuilder();
    
    for (int rank = 0; rank < numResultsToCompare; rank++) {
      
      for (String fieldToCompare : fieldsToCompare) {
        
        sb.append(input.getQueryString()).append(fieldSeparator);
        sb.append(fieldToCompare).append(fieldSeparator);
        sb.append(rank+1).append(fieldSeparator);
        
        for (int i = 0; i < comparisonPrefixes.size(); i++) {
          
          SolrOutput solrOutput = solrOutputMap.get(comparisonPrefixes.get(i));
          if (solrOutput == null) {
            sb.append("NO_SOLR_OUTPUT").append(lineSeparator);
            continue;
          }
          
          List<Map<String, Object>> solrDocuments = solrOutput.response.getDocs();
          if (solrDocuments.size() < rank) {
            continue;
          }
          
          Map<String, Object> solrDocument = solrDocuments.get(rank);
          
          if (solrDocument.containsKey(fieldToCompare)) {
            
            if (i == 0) {
              sb.append(solrDocument.get(fieldToCompare)).append(fieldSeparator);
            } else {
              sb.append(getComparisonResult(solrDocument.get(fieldToCompare), fieldToCompare)).append(fieldSeparator);
            }
            
          } else {
            sb.append(fieldToCompare + "_NOT_PRESENT").append(fieldSeparator);
          }
        }
        
        sb.append(lineSeparator);
      }
    }
    return sb.toString().trim();
  }
  
  public String getComparisonResult(Object objectToBeCompared, String fieldName) {
    return objectToBeCompared.toString();
  }
}