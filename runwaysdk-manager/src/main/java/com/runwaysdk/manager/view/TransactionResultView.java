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
package com.runwaysdk.manager.view;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.TransactionRecordInfo;
import com.runwaysdk.manager.controller.IAdminModuleController;
import com.runwaysdk.manager.controller.IViewStrategy;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.EntityObject;
import com.runwaysdk.manager.model.object.PersistanceFacade;
import com.runwaysdk.manager.model.object.SearchObject;
import com.runwaysdk.manager.widgets.PaginatedTableView;
import com.runwaysdk.manager.widgets.TabManager;

public class TransactionResultView extends ViewPart implements IViewPart
{
  private PaginatedTableView     table;

  private IComponentObject          entity;

  private TabManager             manager;

  private SashForm               form;

  private IAdminModuleController controller;

  public TransactionResultView(TabManager manager, IAdminModuleController controller)
  {
    this.entity = SearchObject.newInstance(PersistanceFacade.getMdEntityDAO(TransactionRecordInfo.CLASS));
    this.manager = manager;
    this.table = new PaginatedTableView(entity, 50);
    this.controller = controller;
  }

  public IComponentObject getEntity()
  {
    return entity;
  }

  @Override
  public void createPartControl(Composite parent)
  {
    form = new SashForm(parent, SWT.VERTICAL | SWT.FILL);

    table.createPartControl(form);

    table.addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        TableViewer viewer = table.getTableViewer();

        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();

        if (selection.size() != 1)
        {
          return;
        }

        ComponentIF component = (ComponentIF) selection.getFirstElement();

        final IComponentObject record = EntityObject.get(component.getId());
        final Display display = viewer.getTable().getDisplay();

        manager.openTab(new IViewStrategy()
        {
          public IViewPart getContent()
          {
            return new TransactionRecordView(record, display, manager, controller);
          }

          public String getTitle()
          {
            return record.getMdClassDAO().getDisplayLabel(Localizer.getLocale());
          }

          public String getKey()
          {
            return record.getId();
          }

          @Override
          public boolean isClosable()
          {
            return true;
          }
        });
      }
    });

    table.setInput(entity.getQuery());

    form.setWeights(new int[] { 10, 1 });
  }

  @Override
  public void setFocus()
  {
    table.setFocus();
  }

  @Override
  public Control getWidget()
  {
    return form;
  }
}
