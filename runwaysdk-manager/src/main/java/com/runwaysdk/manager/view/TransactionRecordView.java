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
package com.runwaysdk.manager.view;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.manager.controller.IAdminModuleController;
import com.runwaysdk.manager.controller.IViewStrategy;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.EntityObject;
import com.runwaysdk.manager.model.object.SearchObject;
import com.runwaysdk.manager.widgets.PaginatedTableView;
import com.runwaysdk.manager.widgets.TabManager;
import com.runwaysdk.query.EntityQuery;

public class TransactionRecordView extends DetailView implements IViewPart
{
  private PaginatedTableView     table;

  private SashForm               form;

  private TabManager             manager;

  public TransactionRecordView(IComponentObject entity, Display display, TabManager manager, IAdminModuleController controller)
  {
    super(entity);

    this.manager = manager;
  }

  public void createPartControl(Composite parent)
  {
    form = new SashForm(parent, SWT.VERTICAL | SWT.FILL);

    super.createPartControl(form);

    IComponentObject item = SearchObject.newInstance(TransactionItemInfo.CLASS);

    this.table = new PaginatedTableView(item);

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

        final IComponentObject item = EntityObject.get(component.getOid());

        manager.openTab(new IViewStrategy()
        {
          public IViewPart getContent()
          {
            return new DetailView(item);
          }

          public String getTitle()
          {
            return item.getMdClassDAO().getDisplayLabel(Localizer.getLocale());
          }

          public String getKey()
          {
            return item.getOid();
          }

          @Override
          public boolean isClosable()
          {
            return true;
          }
        });
      }
    });

    EntityQuery query = item.getQuery();
    query.WHERE(query.aReference(TransactionItemInfo.TRANSACTION_RECORD).EQ(getEntity().getOid()));

    table.setInput(query);

    form.setWeights(new int[] { 5, 5, 1 });
  }

  @Override
  public void setFocus()
  {
    this.getWidget().setFocus();
  }

  @Override
  public Composite getWidget()
  {
    return form;
  }

}
