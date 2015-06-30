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

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.widgets.PaginatedTableView;
import com.runwaysdk.manager.widgets.WidgetVisitor;

public class SearchView extends EntityView
{
  public static final String ID = "com.runwaysdk.view.SearchView";

  private SashForm           form;

  private PaginatedTableView results;

  public SearchView(IComponentObject entity, IAdminModule module)
  {
    super(entity, module);

    this.results = new PaginatedTableView(entity);
  }

  public Composite getWidget()
  {
    return form;
  }

  protected TableViewer getResults()
  {
    return results.getTableViewer();
  }

  @Override
  public void createPartControl(Composite parent)
  {
    form = new SashForm(parent, SWT.FILL | SWT.VERTICAL);
    form.setLayout(new FillLayout());

    // Add the Entity View
    super.createPartControl(form);

    results.createPartControl(form);

    this.createTableActions();

    form.setWeights(new int[] { 5, 5, 1 });
  }

  protected void createTableActions()
  {
    final TableViewer viewer = results.getTableViewer();

    results.addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        new EditAction(Localizer.getMessage("EDIT"), viewer, getModule()).run();
      }
    });

    MenuManager manager = new MenuManager();
    results.setMenu(manager.createContextMenu(viewer.getTable()));

    manager.add(new EditAction(Localizer.getMessage("EDIT"), viewer, getModule()));
  }

  @Override
  protected Composite createPartContent(Composite parent)
  {
    Composite content = super.createPartContent(parent);

    Button search = new Button(content, SWT.RIGHT);
    search.setText(Localizer.getMessage("SEARCH"));
    search.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (results != null)
        {
          IComponentObject entity = getEntity();
          EntityQueryDecorator query = new EntityQueryDecorator(entity.getQuery());
          QueryVisitor visitor = new QueryVisitor(entity, query);

          for (MdAttributeDAOIF a : entity.definesMdAttributes())
          {
            a.accept(visitor);
          }

          results.setInput(query.getEntityQuery());
        }
      }
    });

    return content;
  }

  @Override
  protected WidgetVisitor getWidgetVisitor(Composite parent)
  {
    return new WidgetVisitor(parent, this.getModule());
  }
}
