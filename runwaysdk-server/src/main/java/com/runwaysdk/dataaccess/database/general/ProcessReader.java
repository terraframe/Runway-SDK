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
package com.runwaysdk.dataaccess.database.general;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class ProcessReader implements UncaughtExceptionHandler
{
  public static final String OUTPUT_THREAD = "OUTPUT_THREAD";
  
  public static final String ERROR_THREAD = "ERROR_THREAD";
  
  public static final String PROCESS_THREAD = "PROCESS_THREAD";

  private ProcessBuilder builder;

  private Process        process;

  private PrintStream    output;

  private StringBuffer   errorOut;
  
  private static Log log = LogFactory.getLog(ProcessReader.class);
  
  /**
   * A map with the key being the name of the thread, which is defined by
   * the constants on this class, and the value being the Throwable.
   */
  private Map<String, Throwable> threadThrowables;
  
  
  
  /**
   * Creates a new ProcessReader with the given ProcessBuilder and System.out as
   * the default stream to read the output.
   * 
   * @param process
   */
  public ProcessReader(ProcessBuilder builder)
  {
    this(builder, System.out);
  }

  /**
   * Instantiates this ProcessReader with the given ProcessBuilder and
   * PrintStream to read the output.
   * 
   * @param process
   * @param out
   */
  public ProcessReader(ProcessBuilder builder, PrintStream output)
  {
    this.builder = builder;
    this.output = output;
    this.process = null;
    this.errorOut = new StringBuffer();
    this.threadThrowables = new HashMap<String, Throwable>();
  }

  /**
   * Consumes the output of the process in a separate thread.
   * 
   * @param inputStream
   * @param stream
   */
  private void consumeOutput(final InputStream inputStream, final PrintStream stream)
  {
    final BufferedReader out = new BufferedReader(new InputStreamReader(inputStream));
    
    Thread t = new Thread(new Runnable(){
      
      @Override
      public void run()
      {
        try
        {
          String line = new String();

          while ( ( line = out.readLine() ) != null)
          {
            stream.println(line);
          }
          
          stream.close();
        }
        catch (Throwable t)
        {
          String msg = "Error when consuming the process output.";
          throw new ProgrammingErrorException(msg, t);
        }
      }
    }, OUTPUT_THREAD);
    
    t.setUncaughtExceptionHandler(this);
    t.setDaemon(true);
    t.start();
  }
  
  /**
   * Consumes the 
   * @param reader
   * @param buffer
   */
  private void consumeError(final InputStream inputStream, final StringBuffer buffer)
  {
    final BufferedReader out = new BufferedReader(new InputStreamReader(inputStream));
    
    Thread t = new Thread(new Runnable(){
      
      @Override
      public void run()
      {
        try
        {
          String line = new String();

          while ( ( line = out.readLine() ) != null)
          {
            buffer.append(line);
          }
        }
        catch (Throwable t)
        {
          String msg = "Error when consuming the error stream.";
          throw new ProgrammingErrorException(msg, t);
        }
      }
    }, ERROR_THREAD);
    
    t.setUncaughtExceptionHandler(this);
    t.setDaemon(true);
    t.start();
  }
  
  /**
   * Returns the process after it has been created via a call to
   * startSynchronous() or startAsynchronous();
   * 
   * @return
   */
  public Process getProcess()
  {
    return this.process;
  }
  
  private void _start(boolean async)
  {
    Thread t = new Thread(new Runnable(){
      
      @Override
      public void run()
      {
        try
        {
          ProcessReader.this.process = ProcessReader.this.builder.start();
          
          consumeOutput(process.getInputStream(), ProcessReader.this.output);
          consumeError(process.getErrorStream(), ProcessReader.this.errorOut);

          ProcessReader.this.process.waitFor();
        }
        catch (Throwable ex)
        {
          throw new ProgrammingErrorException(ProcessReader.this.toString(), ex);
        }
      }
    }, PROCESS_THREAD);
    
    t.setUncaughtExceptionHandler(this);
    t.start();
    
    if(!async){
      // block this thread until everything is done
      try
      {
        t.join();
      }
      catch (InterruptedException e)
      {
        log.error(e);
      }
    }
  }

  /**
   * Starts the process in a synchronous fashion, which blocks thread execution
   * until the process is done.
   */
  public void start()
  {
    this._start(false);
  }

  /**
   * Starts the process in either a synchronous or asynchronous fashion. An asynchronous
   * call will not block the calling thread.
   * 
   * @param asynchronous
   */
  public void start(boolean asynchronous)
  {
    this._start(asynchronous);
  }
  
  /**
   * Returns any errors gathered from the process's output.
   * 
   * @return
   */
  public StringBuffer getProcessError()
  {
    return this.errorOut;
  }
  
  /**
   * Returns all Throwables registered with the Threads spawned by this class.
   * 
   * @return
   */
  public Map<String, Throwable> getThreadThrowables()
  {
    return this.threadThrowables;
  }
  
  /**
   * Gets the Throwable that occurred in the output thread, or 
   * null if none occurred.
   * 
   * @return
   */
  public Throwable getOutputThrowable()
  {
    return this.threadThrowables.get(OUTPUT_THREAD);
  }
  
  /**
   * Gets the Throwable that occurred in the error thread, or 
   * null if none occurred.
   * 
   * @return
   */
  public Throwable getErrorThrowable()
  {
    return this.threadThrowables.get(ERROR_THREAD);
  }
  
  /**
   * Gets the Throwable that occurred in the process thread, or 
   * null if none occurred.
   * 
   * @return
   */
  public Throwable getProcessThrowable()
  {
    return this.threadThrowables.get(PROCESS_THREAD);
  }

  /**
   * Handles errors from the underlying threads
   * 
   * @param thread
   * @param t
   */
  @Override
  public void uncaughtException(Thread thread, Throwable t)
  {
    log.error(thread, t);
    
    this.threadThrowables.put(thread.getName(), t);
  }
  
  /**
   * Returns the command this ProcessReader is executing.
   */
  @Override
  public String toString()
  {
    return "["+this.getClass().getSimpleName()+"] : " + this.builder.toString();
  }
}
