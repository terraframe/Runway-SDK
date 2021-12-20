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
package com.runwaysdk.transport.conversion;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExcelErrors implements Serializable, Iterable<ExcelMessage>
{
  private static final long serialVersionUID = 2706091384937354708L;

  private byte[] errorXls;
  
  private List<ExcelMessage> messages;
  
  public ExcelErrors()
  {
    messages = new LinkedList<ExcelMessage>();
  }
  
  public byte[] getErrorXLS()
  {
    return errorXls;
  }
  
  public void setErrorXLS(byte[] errorXLS)
  {
    this.errorXls = errorXLS;
  }

  public boolean add(ExcelMessage message)
  {
    return messages.add(message);
  }

  public Iterator<ExcelMessage> iterator()
  {
    return messages.iterator();
  }
  
  public List<ExcelMessage> getMessages()
  {
    return messages;
  }
  
  public int size()
  {
    return messages.size();
  }
}
