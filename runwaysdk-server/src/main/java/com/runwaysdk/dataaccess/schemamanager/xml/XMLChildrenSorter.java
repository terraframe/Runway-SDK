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
package com.runwaysdk.dataaccess.schemamanager.xml;

import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;

public class XMLChildrenSorter extends XMLElementSorter<SchemaElementIF>
{

  public XMLChildrenSorter(ContentPriorityIF priorities)
  {
    super(priorities);
  }

  public int compare(SchemaElementIF firstElement, SchemaElementIF secondElement)
  {
    int difference =  priorities.childElementPriority(firstElement.getTag())
        - priorities.childElementPriority(secondElement.getTag());
    //If two element have the same priority the order does not matter.
   
    return difference;
  }

}
