/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk;

public class StopTransactionException extends RuntimeException
{
  private static final long serialVersionUID = 158466258123L;

  private boolean doNotLog = false;
  
  public StopTransactionException()
  {
    super();
  }
  
  public StopTransactionException(boolean doNotLog)
  {
    super();
    
    this.doNotLog = doNotLog;
  }
  
  public StopTransactionException(Throwable cause)
  {
    super(cause);
  }
  
  public StopTransactionException(Throwable cause, boolean doNotLog)
  {
    super(cause);
    
    this.doNotLog = doNotLog;
  }
  
  public boolean shouldLog()
  {
    return !this.doNotLog;
  }
}
