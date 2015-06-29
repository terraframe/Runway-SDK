/**
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
 */
package com.runwaysdk.dataaccess.io.excel;

import com.runwaysdk.ProblemIF;
import com.runwaysdk.RunwayException;
import com.runwaysdk.RunwayProblem;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.session.Session;

public class ExcelProblem extends RunwayProblem
{
  private int row;
  private String column;
  private String localizedMessage;
  private MdAttributeDAOIF mdAttributeDAO;
  
  public ExcelProblem(ProblemIF cause)
  {
    super(cause.getDeveloperMessage());
    cause.setLocale(Session.getCurrentSession().getLocale());
    localizedMessage = cause.getLocalizedMessage();
    
    // If there is no message, at least say what type of exception this is
    if (localizedMessage==null)
      localizedMessage=cause.getClass().getName();
  }
  
  public ExcelProblem(Exception cause)
  {
    super(cause.getMessage());
    if (cause instanceof RunwayException)
      ((RunwayException)cause).setLocale(Session.getCurrentSession().getLocale());
    localizedMessage = cause.getLocalizedMessage();
    
    // If there is no message, at least say what type of exception this is
    if (localizedMessage==null)
      localizedMessage=cause.getClass().getName();
  }

  public String getLocalizedMessage()
  { 
    return localizedMessage;
  }
  
  public int getRow()
  {
    return row;
  }

  public void setRow(int row)
  {
    this.row = row;
  }

  public String getColumn()
  {
    return column;
  }

  public void setColumn(String column)
  {
    this.column = column;
  }

  public MdAttributeDAOIF getMdAttributeDAO()
  {
    return mdAttributeDAO;
  }

  public void setMdAttributeDAO(MdAttributeDAOIF mdAttributeDAO)
  {
    this.mdAttributeDAO = mdAttributeDAO;
  }
  
  public String toString()
  {
    return "Row " + getRow() + ", Column " + getColumn() + " - " + getDeveloperMessage();
  }
}
