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
package com.runwaysdk.manager.view;

import java.util.Iterator;

import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.manager.controller.ViewConfig;
import com.runwaysdk.manager.model.object.BusinessObject;
import com.runwaysdk.manager.model.object.IEntityObject;

public class EditHandler implements IOpenListener, SelectionListener
{
  private ListViewer   viewer;

  private IAdminModule module;

  public EditHandler(ListViewer viewer, IAdminModule module)
  {
    this.viewer = viewer;
    this.module = module;
  }

  @Override
  public void open(OpenEvent event)
  {
    this.handleSelection();
  }

  @Override
  public void widgetDefaultSelected(SelectionEvent e)
  {
    this.handleSelection();
  }

  @Override
  public void widgetSelected(SelectionEvent e)
  {
    this.handleSelection();
  }

  @SuppressWarnings("unchecked")
  private void handleSelection()
  {
    ISelection selection = viewer.getSelection();

    if (selection instanceof StructuredSelection)
    {
      Iterator<ComponentIF> it = ( (StructuredSelection) selection ).iterator();

      while (it.hasNext())
      {
        try
        {
          ComponentIF component = it.next();

          if (component instanceof MdEntityDAOIF)
          {
            MdEntityDAOIF mdEntity = (MdEntityDAOIF) component;
            IEntityObject entity = BusinessObject.newInstance(mdEntity.definesType());

            module.edit(entity, new ViewConfig());
          }
        }
        catch (Exception e)
        {
          module.error(e);
        }
      }
    }
  }
}
