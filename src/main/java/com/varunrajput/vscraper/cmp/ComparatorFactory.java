package com.varunrajput.vscraper.cmp;

/**
 * 
 * @author varunrajput
 *
 */
public class ComparatorFactory {
  public static final Comparator defaultComparator = new DefaultComparator();
  
  public static Comparator getComparator(String fieldName) {
    return defaultComparator;
  }
}
