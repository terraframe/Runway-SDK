/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.web.applet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;

import com.runwaysdk.web.applet.common.RemoteAppletAdapter;

public class MonitorWorker
{
  /**
   * Display progress of the upload
   */
  private FileProgressBar      progressBar;

  /**
   * Monitored upload stream
   */
  private MonitoredInputStream stream;

  /**
   * Display progress of the global upload
   */
  private UnlimitedProgressBar totalProgressBar;

  /**
   * Total amount of global bytes uploaded before this upload
   */
  private long                 totalAmount;

  /**
   * Formatter for displaying percentages
   */
  private NumberFormat         format;

  /**
   * Amount of time to sleep when updating progress on uploads
   */
  private final int            period = 100;

  /**
   * Amount of time of no change before a timout 
   */
  private int                  timeout;

  /**
   * Remote server object
   */
  private RemoteAppletAdapter  remoteAdapter;

  /**
   * Session id of the uploader
   */
  private String               sessionId;

  public MonitorWorker(FileProgressBar progressBar, UnlimitedProgressBar totalProgressBar, int timeout, RemoteAppletAdapter remoteAdapter, String sessionId) throws FileNotFoundException
  {
    this.progressBar = progressBar;
    this.totalProgressBar = totalProgressBar;
    this.totalAmount = totalProgressBar.getValue();
    this.timeout = timeout;
    this.remoteAdapter = remoteAdapter;
    this.sessionId = sessionId;

    // Monitor the upload stream for progress updates
    this.stream = new MonitoredInputStream(new FileInputStream(progressBar.getFile()));

    format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(2);
    format.setMinimumFractionDigits(0);
  }

  /**
   * Writes the file to the server. Updates the local and global progress bar
   * as the status of the file changes.
   *  
   * @return JSON string representing the object
   * @throws Throwable
   */
  public String write() throws Throwable
  {
    long previous = 0;
    int staleTime = 0;

    // Start sending the file to the server
    SendTask task = new SendTask(remoteAdapter, sessionId, stream, progressBar.getFile().getName());
    task.start();

    do
    {
      long amount = stream.getBytesRead();

      // Update the individual file progress bar
      progressBar.setValue(amount);
      progressBar.setString(progressBar.getFile().getName() + " - " + new FileSize(amount) + " (" + format.format(100 * progressBar.getPercentComplete()) + "%)");

      // Upadte the total progress bar
      totalProgressBar.setValue(totalAmount + amount);
      totalProgressBar.setString(new FileSize(totalAmount + amount).toString() + " (" + format.format(100 * totalProgressBar.getPercentComplete()) + "%)");

      try
      {
        Thread.sleep(period);
      }
      catch (Exception e)
      {
        //Do nothing
      }

      //Check for a change in stream
      if (previous == amount)
      {
        staleTime += period;
      }
      else
      {
        staleTime = 0;
      }

      previous = amount;
    }
    while (task.getValue() == null && !task.hasThrowable() && staleTime < timeout);

    long length = progressBar.getFile().length();

    progressBar.setValue(progressBar.getMaximum());
    progressBar.setString(progressBar.getFile().getName() + " - " + new FileSize(length).toString() + " (100%)");
    
    // Upadte the total progress bar
    long size = totalAmount + progressBar.getFile().length();

    totalProgressBar.setValue(size);
    totalProgressBar.setString(new FileSize(size).toString() + " (" + format.format(100 * totalProgressBar.getPercentComplete()) + "%)");

    //Check if the stream has timed out
    if (!(staleTime < timeout))
    {
      //Close the stream and kill the thread trying to send the file
      try
      {        
        stream.close();
      }
      catch (IOException e)
      {
        //Do nothing: A timeout exception is already going to be thrown
      }
            
      task.interrupt();
      throw new TimeoutException();
    }
    
    //Ensure that an error has not occured when sending the file to the server
    if(task.hasThrowable())
    {
      throw task.getThrowable();
    }

    return ((String) task.getValue());
  }
}
