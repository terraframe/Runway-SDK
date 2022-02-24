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
package com.runwaysdk.manager.widgets;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.QueryContentProvider;
import com.runwaysdk.manager.model.QueryLabelProvider;
import com.runwaysdk.manager.view.ColumnRemovableVisitor;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OrderBy.SortOrder;

public class PaginatedTableView extends ViewPart
{
  public static final int      DEFAULT_COLUMN_SIZE = 100;

  private TableViewer          table;

  private IComponentObject        entity;

  private int                  pageSize;

  private QueryContentProvider provider;

  private EntityQuery          query;

  private Group                buttons;

  private Label                maxPage;

  private Text                 currentPage;

  private Button               button;

  private int                  pageCount;

  public PaginatedTableView(IComponentObject entity)
  {
    this(entity, 20);
  }

  public PaginatedTableView(IComponentObject entity, int pageSize)
  {
    this.pageSize = pageSize;
    this.entity = entity;
    this.provider = new QueryContentProvider(pageSize);
  }

  public IComponentObject getEntity()
  {
    return entity;
  }

  @Override
  public void createPartControl(Composite parent)
  {
    this.pageCount = 0;

    List<MdAttributeDAOIF> mdAttributes = this.getEntity().definesMdAttributes();

    // Remove all struct attributes
    Iterator<MdAttributeDAOIF> it = mdAttributes.iterator();

    ColumnRemovableVisitor visitor = new ColumnRemovableVisitor(it);

    while (it.hasNext())
    {
      it.next().accept(visitor);
    }

    table = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
    table.getTable().setLayout(this.createTableLayout(mdAttributes));
    table.getTable().setLinesVisible(true);
    table.getTable().setHeaderVisible(true);
    table.setContentProvider(provider);
    table.setLabelProvider(new QueryLabelProvider(mdAttributes));

    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      final String attributeName = mdAttribute.definesAttribute();

      TableColumn column = new TableColumn(table.getTable(), SWT.CENTER);
      column.setText(mdAttribute.getDisplayLabel(Localizer.getLocale()));
      column.addSelectionListener(new SelectionAdapter()
      {
        private boolean isAscending = true;

        public void widgetSelected(SelectionEvent e)
        {
          if (query != null)
          {
            this.isAscending = !isAscending;

            query.CLEAR_ORDER_BY();

            SortOrder order = isAscending ? SortOrder.ASC : SortOrder.DESC;
            query.ORDER_BY(query.getPrimitive(attributeName), order);
            table.refresh();
          }
        }
      });
    }

    buttons = new Group(parent, SWT.BORDER_DASH | SWT.FILL);
    buttons.setText(Localizer.getMessage("PAGE"));
    buttons.setLayout(new FormLayout());

    FormData currentData = new FormData(50, 10);
    currentPage = new Text(buttons, SWT.BORDER);
    currentPage.setLayoutData(currentData);
    currentPage.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.keyCode == SWT.CR)
        {
          handleSelection();
        }
      }
    });

    FormData maxData = new FormData();
    maxData.left = new FormAttachment(currentPage);

    maxPage = new Label(buttons, SWT.NONE);
    maxPage.setLayoutData(maxData);

    
    FormData buttonData = new FormData();
    buttonData.left = new FormAttachment(maxPage);
    
    button = new Button(buttons, SWT.NONE);
    button.setLayoutData(buttonData);
    button.setText(Localizer.getMessage("GO"));
    button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        handleSelection();
      }
    });
    
    this.setPageCount(0);
  }
    
  private void handleSelection()
  {
    try
    {
      showPage(Integer.parseInt(currentPage.getText()));
    }
    catch (Exception error)
    {
      // Do nothing
    }
  }

  private TableLayout createTableLayout(List<MdAttributeDAOIF> mdAttributes)
  {
    TableLayout layout = new TableLayout();

    int size = mdAttributes.size();

    for (int i = 0; i < size; i++)
    {
      layout.addColumnData(new ColumnPixelData(DEFAULT_COLUMN_SIZE, true));
    }

    return layout;
  }

  @Override
  public void setFocus()
  {
    table.getTable().setFocus();
  }

  public TableViewer getTableViewer()
  {
    return this.table;
  }

  public void addDoubleClickListener(IDoubleClickListener listener)
  {
    this.table.addDoubleClickListener(listener);
  }

  public void setMenu(Menu contextMenu)
  {
    this.table.getTable().setMenu(contextMenu);
  }

  public void setInput(EntityQuery query)
  {
    this.provider.setPageNumber(1);
    this.query = query;

    long count = query.getCount();
    int numberOfPages = (int) Math.ceil(count / (double) pageSize);

    this.table.setInput(query);
    this.setPageNumber(1);
    this.setPageCount(numberOfPages);
  }

  private void showPage(int pageNumber)
  {
    if (query != null && pageNumber <= pageCount)
    {
      this.provider.setPageNumber(pageNumber);
      this.setPageNumber(pageNumber);

      table.refresh();
    }
  }

  private void setPageNumber(int pageNumber)
  {
    String text = new Integer(pageNumber).toString();

    currentPage.setText(text);

    buttons.layout(true);
  }

  private void setPageCount(int pageCount)
  {
    this.pageCount = pageCount;

    String msg = Localizer.getMessage("OF");

    maxPage.setText(" " + msg + " " + pageCount);

    buttons.layout(true);
  }
}
