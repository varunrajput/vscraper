package com.varunrajput.vscraper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author varunrajput
 */
public class ClassUtil {
  private static final Logger log = LoggerFactory.getLogger(ClassUtil.class);
  
  @SuppressWarnings("unchecked")
  public static <T> T getNewInstance(Class<T> clazz, String classNameProperty) {
    try {
      return ((Class<? extends T>) Class.forName(PropertiesUtil
          .get(classNameProperty))).newInstance();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}