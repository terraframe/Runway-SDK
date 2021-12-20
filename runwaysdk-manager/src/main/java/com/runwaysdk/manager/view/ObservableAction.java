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
package com.runwaysdk.manager.view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.manager.model.object.EntityObject;
import com.runwaysdk.manager.model.object.IEntityObject;

public class ObservableAction extends Action implements ISelectionChangedListener
{
  private StructuredViewer viewer;

  private IAdminModule     module;

  private IEventStrategy   strategy;

  public ObservableAction(String text, StructuredViewer viewer, IEventStrategy strategy, IAdminModule module)
  {
    super(text);

    this.viewer = viewer;
    this.module = module;
    this.strategy = strategy;
  }

  public void run()
  {
    IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

    if (selection.size() != 1)
    {
      return;
    }

    Object element = selection.getFirstElement();

    if (element instanceof IEntityObject)
    {
      strategy.execute(module, (IEntityObject) element);
    }
    else if (element instanceof EntityDAOIF)
    {
      strategy.execute(module, EntityObject.get((EntityDAOIF) element));
    }
  }

  public void selectionChanged(SelectionChangedEvent event)
  {
    this.run();
  }
}
