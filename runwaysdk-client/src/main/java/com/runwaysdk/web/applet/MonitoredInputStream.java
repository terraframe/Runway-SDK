/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.web.applet;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MonitoredInputStream extends FilterInputStream
{
  private long count;
  private boolean closed;

  protected MonitoredInputStream(InputStream in)
  {
    super(in);
    count = 0;
    closed = false;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException
  {
    int out = super.read(b, off, len);

    this.incrementCount(out);

    return out;
  }

  @Override
  public int read() throws IOException
  {
    int out = super.read();

    this.incrementCount(1);
    
    return out;
  }

  @Override
  public int read(byte[] b) throws IOException
  {
    int out = super.read(b);
    
    this.incrementCount(out);
    
    return out;
  }
  
  private synchronized void incrementCount(int amount)
  {
    count += amount;
  }
  
  public synchronized long getBytesRead()
  {
    return count;
  }
  
  @Override
  public void close() throws IOException
  {
    closed = true;
    super.close();
  }
    
  public boolean isClosed()
  {
    return closed;
  }  
}
