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
package com.runwaysdk.dataaccess.graph.orientdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.sequence.OSequenceLibrary;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class OrientDBDeleteClassAction extends OrientDBDDLAction
{
  private String  className;

  private boolean includeSequence;

  public OrientDBDeleteClassAction(GraphRequest graphRequest, GraphRequest ddlGraphDBRequest, String className, boolean includeSequence)
  {
    super(graphRequest, ddlGraphDBRequest);

    this.className = className;
    this.includeSequence = includeSequence;
  }

  @Override
  protected void executeDDL(ODatabaseSession db)
  {
    db.getMetadata().getSchema().dropClass(className);

    if (includeSequence)
    {
      OSequenceLibrary sequenceLibrary = db.getMetadata().getSequenceLibrary();
      sequenceLibrary.dropSequence(className + "Seq");
    }
  }
}
