package com.varunrajput.vscraper.cmp;

/**
 * 
 * @author varunrajput
 *
 */
public class DefaultComparator implements Comparator {
  public String compare(Object obj1, Object obj2) {
    return obj2.toString();
  }
}
