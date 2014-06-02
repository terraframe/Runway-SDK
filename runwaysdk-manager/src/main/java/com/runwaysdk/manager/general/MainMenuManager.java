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
package com.runwaysdk.manager.general;

import java.util.HashMap;

import org.eclipse.jface.action.MenuManager;

public class MainMenuManager implements IMenuManager
{
  private HashMap<String, MenuManager> map;

  private MenuManager                  bar;

  public MainMenuManager(MenuManager bar)
  {
    this.bar = bar;
    this.map = new HashMap<String, MenuManager>();
  }

  @Override
  public void addMenu(MenuManager menu)
  {
    bar.add(menu);
  }

  @Override
  public MenuManager getMenu(String text)
  {
    if (!map.containsKey(text))
    {
      map.put(text, new MenuManager(text));
    }
    
    return map.get(text);
  }

  @Override
  public MenuManager getMenuBar()
  {
    return bar;
  }

}
