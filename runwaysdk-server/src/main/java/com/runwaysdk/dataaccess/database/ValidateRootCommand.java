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
package com.runwaysdk.dataaccess.database;

import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeClassificationDAO;

public class ValidateRootCommand implements Command
{
  private MdAttributeClassificationDAO mdAttribute;

  public ValidateRootCommand(MdAttributeClassificationDAO mdAttribute)
  {
    this.mdAttribute = mdAttribute;
  }

  @Override
  public void doFinally()
  {
  }

  @Override
  public void doIt()
  {
    MdClassificationDAOIF mdClassification = this.mdAttribute.getMdClassificationDAOIF();

    if (mdClassification != null)
    {
      VertexObjectDAOIF attributeRoot = this.mdAttribute.getRoot();
      VertexObjectDAOIF root = mdClassification.getRoot();

      if (root != null && attributeRoot != null && !VertexObjectDAO.isChild(root, attributeRoot, mdClassification.getReferenceMdEdgeDAO()))
      {
//        System.err.print("Child not found!!!!!!!!!!!!!!!!!!!!!!!!!");
         throw new ProgrammingErrorException("Attribute root must be a child of the classification root");
      }
    }
  }

  @Override
  public String doItString()
  {
    return "Validating classification attribute root is a child of the classification root";
  }

  @Override
  public boolean isUndoable()
  {
    return false;
  }

  @Override
  public void undoIt()
  {
  }

  @Override
  public String undoItString()
  {
    return "";
  }
}
