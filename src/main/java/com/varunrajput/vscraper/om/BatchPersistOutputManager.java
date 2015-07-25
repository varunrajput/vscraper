package com.varunrajput.vscraper.om;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.varunrajput.vscraper.Property;
import com.varunrajput.vscraper.output.Output;
import com.varunrajput.vscraper.util.FileUtil;
import com.varunrajput.vscraper.util.PropertiesUtil;

/**
 * @author varunrajput
 */
public class BatchPersistOutputManager implements OutputManager {
  private static final Logger log = LoggerFactory
      .getLogger(BatchPersistOutputManager.class);
  
  private List<Output> outputs;
  private String outputFile;
  
  private BatchWriter batchWriter;
  private Thread batchWriterThread;
  private List<Output> batchOutput;
  private Integer batchSize;
  private String batchFile;
  
  public BatchPersistOutputManager() {
    outputs = Collections.synchronizedList(new ArrayList<Output>());
    outputFile = PropertiesUtil.get(Property.OutputFile);
    batchSize = (Integer.parseInt(PropertiesUtil.get(Property.BatchSize)));
    
    batchFile = outputFile + "_batch_" + System.currentTimeMillis();
    batchWriter = new BatchWriter();
    batchWriterThread = new Thread(batchWriter);
    batchWriterThread.start();
  }
  
  @Override
  public void logOutput(Output output) {
    outputs.add(output);
    
    // Check if batch needs to be written
    if (outputs.size() >= batchSize) {
      synchronized (this) {
        if (outputs.size() >= batchSize) {
          while (batchWriter.isWriting) {
            if (!batchWriter.isWriting) {
              break;
            }
          }
          batchOutput = outputs;
          outputs = Collections.synchronizedList(new ArrayList<Output>());
        }
      }
    }
  }
  
  @Override
  public void generateOuputFile() {
    batchWriter.run = false;
    
    try {
      batchWriterThread.join();
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
    }
    
    writeOutput(outputs, batchFile, true);
    
    File of = new File(batchFile);
    if (of.exists()) {
      of.renameTo(new File(outputFile));
    }
  }
  
  private void writeOutput(List<Output> outputList, String filename,
      Boolean append) {
    
    if (outputList == null || outputList.isEmpty()) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    for (Output output : outputList) {
      sb.append(output.writeFormatted()).append("\n");
    }
    FileUtil.writeToFile(sb.toString(), filename, append);
  }
  
  /**
   * 
   * Batch Writer to run in parallel and persist batch output
   * 
   * @author varunrajput
   * 
   */
  private class BatchWriter implements Runnable {
    volatile boolean run = true;
    volatile boolean isWriting = false;
    
    @Override
    public void run() {
      while (run) {
        if (batchOutput != null && !batchOutput.isEmpty()) {
          synchronized (batchOutput) {
            if (batchOutput != null && !batchOutput.isEmpty()) {
              isWriting = true;
              writeOutput(batchOutput, batchFile, true);
              batchOutput = null;
              isWriting = false;
            }
          }
        }
      }
    }
  }
  
}