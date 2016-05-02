package com.varunrajput.vscraper.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.varunrajput.vscraper.input.DelimitedInput;

import java.util.List;
import java.util.Map;

/**
 * @author varunrajput
 */
public abstract class AbstractSolrOutput implements Output {
  protected static final Gson gson = new GsonBuilder().create();
  
  protected DelimitedInput input;
  protected SolrResponseHeader responseHeader;
  protected SolrResponse response;
  protected SolrResponse clusters;
  
  public static class SolrResponseHeader {
    private Integer status;
    private Long QTime;
    private Map<String,String> params;
    
    public Integer getStatus() {
      return status;
    }
    
    public void setStatus(Integer status) {
      this.status = status;
    }
    
    public Long getQTime() {
      return QTime;
    }
    
    public void setQTime(Long qTime) {
      QTime = qTime;
    }
    
    public Map<String,String> getParams() {
      return params;
    }
    
    public void setParams(Map<String,String> params) {
      this.params = params;
    }
  }
  
  public static class SolrResponse {
    private Long numFound;
    private Long start;
    private Float maxScore;
    private List<Map<String,Object>> docs;
    
    public Long getNumFound() {
      return numFound;
    }
    
    public void setNumFound(Long numFound) {
      this.numFound = numFound;
    }
    
    public Long getStart() {
      return start;
    }
    
    public void setStart(Long start) {
      this.start = start;
    }
    
    public Float getMaxScore() {
      return maxScore;
    }
    
    public void setMaxScore(Float maxScore) {
      this.maxScore = maxScore;
    }
    
    public List<Map<String,Object>> getDocs() {
      return docs;
    }
    
    public void setDocs(List<Map<String,Object>> docs) {
      this.docs = docs;
    }
  }
}
