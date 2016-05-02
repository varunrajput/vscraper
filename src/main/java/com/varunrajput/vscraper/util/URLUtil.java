package com.varunrajput.vscraper.util;

import com.varunrajput.vscraper.Property;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author varunrajput
 */
public class URLUtil {
  private static final Logger log = LoggerFactory.getLogger(URLUtil.class);
  private static Integer numRetries = Integer.parseInt(PropertiesUtil
      .get(Property.NumRetries));

  private static HttpClient httpClient;

  static {
    int numScrapers = Integer.parseInt(PropertiesUtil.get(Property.NumScrapers));
    int numRoutes = PropertiesUtil.get(Property.BaseUrl).split(",").length;

    PoolingHttpClientConnectionManager cnMgr = new PoolingHttpClientConnectionManager();
    cnMgr.setMaxTotal(numScrapers);
    cnMgr.setDefaultMaxPerRoute(numScrapers/numRoutes);

    httpClient = HttpClientBuilder.create()
        .setRetryHandler(new DefaultHttpRequestRetryHandler(numRetries, false))
        .setMaxConnTotal(numScrapers)
        .setMaxConnPerRoute(numScrapers/numRoutes)
        .setConnectionManager(cnMgr)
        .build();
  }

  public static String getOutputString(String baseUrl,
                                       Map<String,String> params, QueryMethod queryMethod) {

    String outputString = null;

    try {
      HttpResponse httpResponse = httpClient.execute(generateMethod(baseUrl, params, queryMethod));

      if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
        outputString = sb.toString();
        reader.close();

      } else {
        log.error("NON OK Status Code Received: " + httpResponse.getStatusLine().getStatusCode()
            + ". Reason: " + httpResponse.getStatusLine().getReasonPhrase());
      }

    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }

    return outputString;
  }

  private static HttpUriRequest generateMethod(String baseUrl,
                                               Map<String,String> params, QueryMethod queryMethod) {

    URI uri;
    try {
      URIBuilder uriBuilder = new URIBuilder(baseUrl);
      for (Map.Entry<String, String> entry : params.entrySet()) {
        uriBuilder.addParameter(entry.getKey(), entry.getValue());
      }
      uri = uriBuilder.build();
    }catch (URISyntaxException e) {
      log.error("Invalid base URL '" + baseUrl + "' " + e.getMessage(), e);
      return null;
    }

    HttpUriRequest request;
    switch (queryMethod) {
      case GET:
        request = new HttpGet(uri);
        break;
      case POST:
        request = new HttpPost(uri);
        break;
      default:
        request = new HttpGet(uri);
        break;
    }

    return request;
  }

  public enum QueryMethod {
    GET, POST
  }
}