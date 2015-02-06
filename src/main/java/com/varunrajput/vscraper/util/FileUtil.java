package com.varunrajput.vscraper.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author varunrajput
 */
public class FileUtil {
  public static final Logger log = LoggerFactory.getLogger(FileUtil.class);
  
  public static void writeToFile(String data, String filename, Boolean append) {
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(filename, append));
      bw.write(data);
      bw.flush();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }
}