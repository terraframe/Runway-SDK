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

import java.util.HashMap;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;

import com.runwaysdk.manager.controller.IViewStrategy;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.view.IViewPart;

public class TabManager
{
  public static final String        EDIT_PREFIX = "Edit ";

  private CTabFolder                tabFolder;

  private HashMap<String, CTabItem> items;

  public TabManager(CTabFolder tabFolder)
  {
    this.tabFolder = tabFolder;
    this.items = new HashMap<String, CTabItem>();
  }

  public void openTab(final IViewStrategy strategy)
  {
    String key = strategy.getKey();

    if (items.containsKey(key))
    {
      tabFolder.setSelection(items.get(key));
    }
    else
    {
      String title = strategy.getTitle();

      CTabItem item = this.getTabItem(title, key, strategy.isClosable());

      IViewPart view = strategy.getContent();
      view.createPartControl(tabFolder);

      item.setControl(view.getWidget());

      tabFolder.setSelection(item);
    }
  }

  private CTabItem getTabItem(final String title, final String key, boolean closable)
  {
    if (!items.containsKey(title))
    {
      final CTabItem item = closable ? new CTabItem(tabFolder, SWT.CLOSE) : new CTabItem(tabFolder, SWT.NONE);
      item.setText(title);
      item.addDisposeListener(new DisposeListener()
      {
        @Override
        public void widgetDisposed(DisposeEvent e)
        {
          items.remove(key);
        }
      });

      items.put(key, item);
    }

    return items.get(key);
  }

  public void closeTab(String key)
  {
    CTabItem tab = items.get(key);

    if (tab != null)
    {
      tab.dispose();
    }
  }

  public void closeOthers(String _key)
  {
    Set<String> keys = items.keySet();
    keys.remove(_key);

    for (String key : keys)
    {
      this.closeTab(key);
    }
  }

  public Display getDisplay()
  {
    return tabFolder.getDisplay();
  }

  public static String getEditKey(IComponentObject entity)
  {
    return EDIT_PREFIX + entity.getId();
  }
}
