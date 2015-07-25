package com.varunrajput.vscraper.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;

/**
 * @author varunrajput
 */
public class URLUtil {
  private static final Logger log = LoggerFactory.getLogger(URLUtil.class);
  private static Integer numRetries = Integer.parseInt(PropertiesUtil
      .get(Property.NumRetries));
  
  private static HttpClient client = null;
  
  static {
    int numScrapers = Integer.parseInt(PropertiesUtil.get(Property.NumScrapers, "100"));
    MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
    HttpConnectionManagerParams params = new HttpConnectionManagerParams();
    params.setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION, numScrapers);
    params.setDefaultMaxConnectionsPerHost(numScrapers);
    params.setMaxTotalConnections(numScrapers);
    multiThreadedHttpConnectionManager.setParams(params);
    client = new HttpClient(multiThreadedHttpConnectionManager);
  }
  
  public static String getOutputString(String baseUrl,
      Map<String,String> params, QueryMethod queryMethod) {
    
    String outputString = null;
    HttpMethod method = generateMethod(baseUrl, params, queryMethod);
    
    try {
      int statusCode = client.executeMethod(method);
      if (statusCode == HttpStatus.SC_OK) {
        InputStream is = method.getResponseBodyAsStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
          builder.append(line);
        }
        outputString = builder.toString();
        rd.close();
      } else {
        log.error("NON OK Status Code Received: " + statusCode + ". Reason: "
            + method.getStatusText());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (method != null) {
        method.releaseConnection();
      }
    }
    
    return outputString;
  }
  
  private static HttpMethod generateMethod(String baseUrl,
      Map<String,String> params, QueryMethod queryMethod) {
    
    HttpMethod method = null;
    switch (queryMethod) {
      case GET:
        method = new GetMethod(baseUrl);
        break;
      case POST:
        method = new PostMethod(baseUrl);
        break;
      default:
        method = new GetMethod(baseUrl);
        break;
    }
    
    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    for (String param : params.keySet()) {
      nameValuePairs.add(new NameValuePair(param, params.get(param)));
    }
    
    method.setQueryString(nameValuePairs
        .toArray(new NameValuePair[nameValuePairs.size()]));
    
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        new DefaultHttpMethodRetryHandler(numRetries, false));
    
    method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
        Integer.parseInt(PropertiesUtil.get(Property.SocketTimeout)));
    
    return method;
  }
  
  public static enum QueryMethod {
    GET, POST
  }
}