/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.controller;

import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.io.instance.ImportAction;

public class ViewConfig
{
  private boolean     closable;

  private boolean     closeOnSucess;

  private String      message;

  private Set<ImportAction> ImportActions;

  public ViewConfig()
  {
    this(true, false, null);
  }

  public ViewConfig(boolean closable, boolean closeOnSucess, String message)
  {
    this.closable = closable;
    this.closeOnSucess = closeOnSucess;
    this.message = message;
    this.ImportActions = new TreeSet<ImportAction>();
    this.ImportActions.add(ImportAction.APPLY);
    this.ImportActions.add(ImportAction.DELETE);
  }

  public boolean isClosable()
  {
    return closable;
  }

  public boolean isCloseOnSucess()
  {
    return closeOnSucess;
  }

  public String getMessage()
  {
    return message;
  }

  public void removeImportAction(ImportAction ImportAction)
  {
    this.ImportActions.remove(ImportAction);
  }

  public boolean contains(ImportAction ImportAction)
  {
    return this.ImportActions.contains(ImportAction);
  }
}
