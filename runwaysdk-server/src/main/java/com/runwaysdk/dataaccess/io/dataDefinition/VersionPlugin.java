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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.VersionHandler.Action;

public class VersionPlugin implements ImportPluginIF
{
  private class NullHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public NullHandler(ImportManager manager)
    {
      super(manager);
    }
  }

  private class VersionRootHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public VersionRootHandler(ImportManager manager, Action action)
    {
      super(manager);

      if (action.equals(Action.DO_IT))
      {
        this.addHandler(XMLTags.DO_IT_TAG, new RootHandler(manager));
        this.addHandler(XMLTags.UNDO_IT_TAG, new NullHandler(manager));
      }
      else if (action.equals(Action.UNDO_IT))
      {
        this.addHandler(XMLTags.DO_IT_TAG, new NullHandler(manager));
        this.addHandler(XMLTags.UNDO_IT_TAG, new RootHandler(manager));
      }
    }
  }

  private Action action;

  /**
   * 
   */
  public VersionPlugin(Action action)
  {
    this.action = action;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.ImportPluginIF#register(com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void register(ImportManager manager)
  {
    manager.setRoot(new VersionRootHandler(manager, this.action));
  }

}
