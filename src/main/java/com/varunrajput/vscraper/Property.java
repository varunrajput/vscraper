package com.varunrajput.vscraper;

/**
 * @author varunrajput
 */
public interface Property {
  public String InputQueriesFile = "INPUT_QUERIES_FILE";
  public String OutputFile = "OUTPUT_FILE";
  
  public String NumScrapers = "NUM_SCRAPERS";
  public String BatchSize = "BATCH_SIZE";
  
  public String BaseUrl = "BASE_URL";
  public String QueryMethod = "QUERY_METHOD";
  public String NumRetries = "NUM_OF_RETRIES";
  public String SocketTimeout = "HTTP_SOCKET_TIMEOUT";
  public String QueryParamKey = "QUERY_PARAM_KEY";
  
  public String QPS = "QPS";
  
  public String ParamsToAppendToQuery = "PARAMS_TO_APPEND_TO_QUERY";
  
  public String InputClass = "INPUT_CLASS";
  public String InputManagerClass = "INPUT_MANAGER_CLASS";
  
  public String ScraperClass = "SCRAPER_CLASS";
  public String ScraperManagerClass = "SCRAPER_MANAGER_CLASS";
  
  public String OutputClass = "OUTPUT_CLASS";
  public String OutputManagerClass = "OUTPUT_MANAGER_CLASS";
  
  public String ProgressTrackingStatsManagerEnable = "PROGRESS_TRACKING_STATS_MANAGER_ENABLE";
  public String ProgressTrackingStatsManagerInterval = "PROGRESS_TRACKING_STATS_MANAGER_INTERVAL";
  public String ProgressTrackingStatsManagerWriteToConsole = "PROGRESS_TRACKING_STATS_MANAGER_WRITE_TO_CONSOLE";
  
  public String TabSeparatedInputQueryFieldIndex = "TAB_SEPARATED_INPUT_QUERY_FIELD_INDEX";
  
  public String SolrOutputFieldSeparator = "SOLR_OUTPUT_FIELD_SEPARATOR";
  public String SolrOutputFieldsToExtract = "SOLR_OUTPUT_FIELDS_TO_EXTRACT";
  
  public String ComparisonScrapingPrefixes = "COMPARISON_SCRAPING_PREFIXES";
  
  public String SolrComparisonOutputFormatMode = "SOLR_COMPARISON_OUTPUT_FORMAT_MODE";
  public String SolrComparisonOutputFieldSeparator = "SOLR_COMPARISON_OUTPUT_FIELD_SEPARATOR";
  public String SolrComparisonOutputFieldsToCompare = "SOLR_COMPARISON_OUTPUT_FIELDS_TO_COMPARE";
  public String SolrComparisonOutputNumResultsToCompare = "SOLR_COMPARISON_OUTPUT_NUM_RESULTS_TO_COMPARE";
}