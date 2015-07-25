package com.varunrajput.vscraper.om;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.output.Output;
import com.varunrajput.vscraper.util.FileUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * @author varunrajput
 */
public class SimpleOutputManager implements OutputManager {
  private List<Output> outputs;
  private String outputFile;
  
  public SimpleOutputManager() {
    outputs = Collections.synchronizedList(new ArrayList<Output>());
    outputFile = PropertiesUtil.get(Property.OutputFile);
  }
  
  @Override
  public void logOutput(Output output) {
    outputs.add(output);
  }
  
  @Override
  public void generateOuputFile() {
    if (outputs == null || outputs.isEmpty()) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    for (Output output : outputs) {
      sb.append(output.writeFormatted()).append("\n");
    }
    FileUtil.writeToFile(sb.toString(), outputFile, false);
  }
}
