/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class MdClassificationHandler extends MdVertexHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
   * @param manager
   *          TODO
   */
  public MdClassificationHandler(ImportManager manager)
  {
    super(manager);
  }

  protected MdVertexDAO createMdVertex(String localName, String name)
  {
    return (MdVertexDAO) this.getManager().getEntityDAO(MdClassificationInfo.CLASS, name).getEntityDAO();
  }

  protected String getTag()
  {
    return XMLTags.MD_CLASSIFICATION_TAG;
  }
}
