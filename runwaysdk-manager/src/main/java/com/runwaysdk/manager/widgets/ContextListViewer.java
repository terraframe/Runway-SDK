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
package com.runwaysdk.manager.widgets;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;

public class ContextListViewer extends ListViewer
{
  public ContextListViewer(Composite parent, int style)
  {
    super(parent, style);
  }

  public ContextListViewer(Composite parent)
  {
    super(parent);
  }

  public ContextListViewer(List list)
  {
    super(list);
  }

  public void setMenu(Menu menu)
  {
    this.getControl().setMenu(menu);
  }

  public void setLayoutData(Object data)
  {
    this.getControl().setLayoutData(data);
  }
}
