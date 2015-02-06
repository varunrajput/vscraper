package com.varunrajput.vscraper.im;

import com.varunrajput.vscraper.input.Input;

/**
 * @author varunrajput
 */
public interface InputManager {
  public Integer totalInputs();
  
  public Integer inputsRemaining();
  
  public boolean hasMoreInputs();
  
  public Input getInput();
}