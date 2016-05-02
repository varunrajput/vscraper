package com.varunrajput.vscraper.util;

import com.varunrajput.vscraper.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by varunrajput on 5/1/16.
 */
public class MailUtil {
  private static final Logger log = LoggerFactory.getLogger(MailUtil.class);
  private static volatile Boolean failed = false;

  public static void send(String to, String subject, String message) {
    if (to == null || to.trim().isEmpty()) {
      return;
    }
    try {
      Process process = Runtime.getRuntime().exec(new String[] {"mail", "-s", subject, to});
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      writer.write(message); writer.flush(); writer.close();
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    }
  }

  public static void sendSuccessIfRequested() {
    if (Boolean.parseBoolean(PropertiesUtil.get(Property.EmailNotificationsEnable))) {
      if (failed) {
        return;
      }
      send(
          PropertiesUtil.get(Property.EmailNotificationsAddress),
          "VScraper Job '" + PropertiesUtil.get(Property.EmailNotificationsJobName)
              + "' completed successfully!", "");
    }
  }

  public static void sendFailedIfRequested(Throwable e) {
    if (Boolean.parseBoolean(PropertiesUtil.get(Property.EmailNotificationsEnable))) {
      if (failed) {
        return;
      }
      synchronized (failed) {
        if (failed) {
          return;
        }
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        send(
            PropertiesUtil.get(Property.EmailNotificationsAddress),
            "VScraper Job '" + PropertiesUtil.get(Property.EmailNotificationsJobName)
                + "' FAILED!", sw.toString());
        failed = true;
      }
    }
  }
}
