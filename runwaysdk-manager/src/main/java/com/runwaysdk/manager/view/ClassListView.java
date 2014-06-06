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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.manager.general.Localizer;
import com.runwaysdk.manager.model.ClassLabelProvider;
import com.runwaysdk.manager.model.QueryContentProvider;
import com.runwaysdk.manager.widgets.ContextListViewer;
import com.runwaysdk.query.BasicCondition;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.session.Request;

public class ClassListView extends ViewPart
{
  /**
   * Constant to filter out system data types
   */
  public static final int   NON_SYSTEM = 0;

  /**
   * Constant to not filter by type
   */
  public static final int   ALL        = 1;

  /**
   * Control module
   */
  private IAdminModule      module;

  /**
   * Viewer to display class types
   */
  private ContextListViewer classList;

  /**
   * Input field for restricting by label
   */
  private Text              input;

  /**
   * Outer composite of the view
   */
  private Composite         composite;

  /**
   * Drop down to restrict by types
   */
  private Combo             combo;

  public ClassListView(IAdminModule module)
  {
    this.module = module;
  }

  @Override
  public void createPartControl(Composite parent)
  {
    composite = new Composite(parent, SWT.BORDER);
    composite.setLayout(new FormLayout());

    input = new Text(composite, SWT.BORDER);
    input.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        classList.setInput(constructQuery());
      }
    });

    FormData comboData = new FormData(50, SWT.DEFAULT);
    comboData.left = new FormAttachment(input);

    combo = new Combo(composite, SWT.BORDER);
    combo.add(Localizer.getMessage("NON_SYSTEM"), NON_SYSTEM);
    combo.add(Localizer.getMessage("ALL"), ALL);
    combo.select(NON_SYSTEM);
    combo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        classList.setInput(constructQuery());
      }
    });

    classList = new ContextListViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FILL);

    SearchHandler searchListener = new SearchHandler(classList, module);

    classList.setContentProvider(new QueryContentProvider(-1));
    classList.setLabelProvider(new ClassLabelProvider());
    classList.setInput(this.constructQuery());
    classList.addOpenListener(searchListener);

    Menu contextMenu = new Menu(classList.getControl());

    MenuItem createMenu = new MenuItem(contextMenu, SWT.PUSH);
    createMenu.setText(Localizer.getMessage("CREATE_MENU_ITEM"));
    createMenu.addSelectionListener(new EditHandler(classList, module));

    MenuItem searchMenu = new MenuItem(contextMenu, SWT.PUSH);
    searchMenu.setText(Localizer.getMessage("SEARCH_MENU_ITEM"));
    searchMenu.addSelectionListener(searchListener);

    classList.setMenu(contextMenu);

    // Set the layout
    this.setPartLayout();
  }

  private void setPartLayout()
  {
    // Input field should be attached to the top-left and offset 5 pixels.
    // Additionally, we want 75% of the first row to be occupied by the input
    // field.
    FormData inputData = new FormData();
    inputData.top = new FormAttachment(0, 5);
    inputData.left = new FormAttachment(0, 5);
    inputData.right = new FormAttachment(75, 0);

    // Combo field should be attached to the top-right and offset 5 pixels.
    // Additionally, we want the remaining space of the top row to be occupied
    // by the combo field
    FormData comboData = new FormData();
    comboData.top = new FormAttachment(0, 5);
    comboData.left = new FormAttachment(input, 5);
    comboData.right = new FormAttachment(100, -5);

    // Combo field should be attached to the input field on top and should
    // occupy all the space in the other directions.
    FormData listData = new FormData();
    listData.top = new FormAttachment(input, 5);
    listData.bottom = new FormAttachment(100, -5);
    listData.left = new FormAttachment(0, 5);
    listData.right = new FormAttachment(100, 0);

    input.setLayoutData(inputData);
    combo.setLayoutData(comboData);
    classList.setLayoutData(listData);
  }

  @Override
  public void setFocus()
  {
    input.setFocus();
  }

  public void setLayoutData(GridData data)
  {
    composite.setLayoutData(data);
  }

  @Request
  private EntityQuery constructQuery()
  {
    EntityQuery query = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS).getEntityQuery();

    this.filterByLabel(query);
    this.filterByPackage(query);

    query.ORDER_BY_ASC(query.aLocalCharacter(MdBusinessInfo.DISPLAY_LABEL).localize());

    return query;
  }

  private void filterByLabel(EntityQuery query)
  {
    String text = input.getText();

    if (text != null && text.length() > 0)
    {
      BasicCondition condition = query.aLocalCharacter(MdBusinessInfo.DISPLAY_LABEL).localize().LIKEi(text + "%");

      query.WHERE(condition);
    }
  }

  private void filterByPackage(EntityQuery query)
  {
    int index = combo.getSelectionIndex();

    if (index == 0)
    {
      BasicCondition condition = query.aCharacter(MdBusinessInfo.PACKAGE).NLIKEi(Constants.ROOT_PACKAGE + "%");

      query.AND(condition);
    }
  }
}
