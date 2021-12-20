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
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.manager.controller.IComponentListener;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.widgets.ReferenceDialog;

public class SelectionView extends SearchView
{
  class SelectionAction extends Action
  {
    private TableViewer          viewer;

    private IComponentListener[] listeners;

    public SelectionAction(TableViewer viewer, IComponentListener... listeners)
    {
      super(Localizer.getMessage("SELECT"));

      this.viewer = viewer;
      this.listeners = listeners;
    }

    public void run()
    {
      IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

      if (selection.size() != 1)
      {
        return;
      }

      ComponentIF component = (ComponentIF) selection.getFirstElement();

      for (IComponentListener listener : listeners)
      {
        listener.componentChanged(component);
      }

      dialog.close();
    }

    public void selectionChanged(SelectionChangedEvent event)
    {
      this.run();
    }
  }

  private ReferenceDialog      dialog;

  private IComponentListener[] listeners;

  public SelectionView(ReferenceDialog dialog, IAdminModule module, IComponentListener[] listeners)
  {
    super(dialog.getEntity(), module);

    this.dialog = dialog;
    this.listeners = listeners;
  }

  @Override
  protected void createTableActions()
  {
    TableViewer results = this.getResults();

    this.getResults().addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        new SelectionAction(getResults(), listeners).run();
      }
    });

    MenuManager manager = new MenuManager();
    results.getTable().setMenu(manager.createContextMenu(results.getTable()));

    manager.add(new SelectionAction(results, listeners));
  }
}
