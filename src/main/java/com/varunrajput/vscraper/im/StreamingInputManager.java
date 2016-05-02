package com.varunrajput.vscraper.im;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.input.Input;
import com.varunrajput.vscraper.util.ClassUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by varunrajput on 5/1/16.
 */
public class StreamingInputManager implements InputManager {

  public static final Logger log = LoggerFactory.getLogger(StreamingInputManager.class);

  private volatile int totalInputs;
  private volatile boolean QPSisToBeRegulated = false;
  private volatile int sleepInterval;
  private volatile int inputsRead;

  private BufferedReader reader;

  public StreamingInputManager() {
    String inputQueriesFile = PropertiesUtil.get(Property.InputQueriesFile);
    Integer QPS = Integer.parseInt(PropertiesUtil.get(Property.QPS));
    QPSisToBeRegulated = QPS > 0;
    sleepInterval = QPSisToBeRegulated ? (int) ((1.0 / QPS) * 1000) : 0;
    try {
      totalInputs = getTotalInputs(inputQueriesFile);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    try {
      reader = new BufferedReader(new FileReader(inputQueriesFile));
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public Integer totalInputs() {
    return totalInputs;
  }

  @Override
  public Integer inputsRemaining() {
    return totalInputs-inputsRead;
  }

  @Override
  public boolean hasMoreInputs() {
    return inputsRead >= totalInputs;
  }

  @Override
  public Input getInput() {
    Input input = null;

    try {
      String line = reader.readLine();
      if (line == null || line.trim().isEmpty()) {
        return input;
      }
      input = ClassUtil.getNewInstance(Input.class, Property.InputClass);
      input.populateQuery(line);
      inputsRead++;
    } catch (IOException e) {
      log.warn(e.getMessage(), e);
    }

    if (QPSisToBeRegulated) {
      synchronized(this) {
        try {
          Thread.sleep(sleepInterval);
        } catch (InterruptedException e) {
          log.error(e.getMessage(), e);
        }
      }
    }

    return input;
  }

  private static Integer getTotalInputs(String filename) throws Exception {
    InputStream is = null;
    try {
      is = new BufferedInputStream(new FileInputStream(filename));
      byte[] c = new byte[1024];
      int count = 0;
      int readChars = 0;
      boolean empty = true;
      while ((readChars = is.read(c)) != -1) {
        empty = false;
        for (int i = 0; i < readChars; ++i) {
          if(c[i] == '\n') {
            ++count;
          }
        }
      }
      return (count == 0 && !empty) ? 1 : count;
    } finally {
      if(is != null) {
        is.close();
      }
    }
  }
}
