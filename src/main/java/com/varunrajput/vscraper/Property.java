package com.varunrajput.vscraper;

/**
 * @author varunrajput
 */
public interface Property {
  String InputQueriesFile = "INPUT_QUERIES_FILE";
  String OutputFile = "OUTPUT_FILE";
  
  String NumScrapers = "NUM_SCRAPERS";
  String BatchSize = "BATCH_SIZE";
  
  String BaseUrl = "BASE_URL";
  String QueryMethod = "QUERY_METHOD";
  String NumRetries = "NUM_OF_RETRIES";
  String SocketTimeout = "HTTP_SOCKET_TIMEOUT";
  String QueryParamKey = "QUERY_PARAM_KEY";
  
  String QPS = "QPS";
  
  String ParamsToAppendToQuery = "PARAMS_TO_APPEND_TO_QUERY";
  
  String InputClass = "INPUT_CLASS";
  String InputManagerClass = "INPUT_MANAGER_CLASS";
  
  String ScraperClass = "SCRAPER_CLASS";
  String ScraperManagerClass = "SCRAPER_MANAGER_CLASS";
  
  String OutputClass = "OUTPUT_CLASS";
  String OutputManagerClass = "OUTPUT_MANAGER_CLASS";
  
  String ProgressTrackingStatsManagerEnable = "PROGRESS_TRACKING_STATS_MANAGER_ENABLE";
  String ProgressTrackingStatsManagerInterval = "PROGRESS_TRACKING_STATS_MANAGER_INTERVAL";
  String ProgressTrackingStatsManagerWriteToConsole = "PROGRESS_TRACKING_STATS_MANAGER_WRITE_TO_CONSOLE";

  String DelimitedInputDelimiter = "DELIMITED_INPUT_DELIMITER";
  String DelimitedInputQueryFieldIndex = "DELIMITED_INPUT_QUERY_FIELD_INDEX";

  String HeaderedDelimitedInputDelimiter = "HEADERED_DELIMITED_INPUT_DELIMITER";

  String TabSeparatedInputQueryFieldIndex = "TAB_SEPARATED_INPUT_QUERY_FIELD_INDEX";
  
  String SolrOutputFieldSeparator = "SOLR_OUTPUT_FIELD_SEPARATOR";
  String SolrOutputFieldsToExtract = "SOLR_OUTPUT_FIELDS_TO_EXTRACT";
  
  String ComparisonScrapingPrefixes = "COMPARISON_SCRAPING_PREFIXES";
  
  String SolrComparisonOutputFormatMode = "SOLR_COMPARISON_OUTPUT_FORMAT_MODE";
  String SolrComparisonOutputFieldSeparator = "SOLR_COMPARISON_OUTPUT_FIELD_SEPARATOR";
  String SolrComparisonOutputFieldsToCompare = "SOLR_COMPARISON_OUTPUT_FIELDS_TO_COMPARE";
  String SolrComparisonOutputNumResultsToCompare = "SOLR_COMPARISON_OUTPUT_NUM_RESULTS_TO_COMPARE";
}