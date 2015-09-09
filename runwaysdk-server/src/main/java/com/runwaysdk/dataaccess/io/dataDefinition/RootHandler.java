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
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;

public class RootHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{

  /**
   * @param manager
   *          TODO
   */
  public RootHandler(ImportManager manager)
  {
    super(manager);

    // Setup default dispatching
    this.addHandler(XMLTags.DELETE_TAG, new DeleteHandler(manager));
    this.addHandler(XMLTags.CREATE_TAG, new CreateDecorator(new CreateHandler(manager)));
    this.addHandler(XMLTags.UPDATE_TAG, new UpdateHandler(manager));
    this.addHandler(XMLTags.CREATE_OR_UPDATE_TAG, new CreateOrUpdateHandler(manager));
    this.addHandler(XMLTags.PERMISSIONS_TAG, new PermissionsHandler(manager));
  }
}
