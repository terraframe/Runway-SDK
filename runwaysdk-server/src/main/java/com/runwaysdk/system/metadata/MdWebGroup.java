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
package com.runwaysdk.system.metadata;

import java.util.List;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class MdWebGroup extends MdWebGroupBase
{
  private static final long serialVersionUID = -235542688;
  
  public MdWebGroup()
  {
    super();
  }
  
  /**
   * Returns all fields in order for the MdWebGroup.
   * 
   * @param groupId
   * @return
   */
  public List<? extends MdWebField> getGroupFields()
  {
    QueryFactory f = new QueryFactory();
    MdWebFieldQuery q = new MdWebFieldQuery(f);
    WebGroupFieldQuery relQ = new WebGroupFieldQuery(f);
    
    relQ.WHERE(relQ.parentId().EQ(this.getId()));
    q.WHERE(q.groupFields(relQ));
    
    q.ORDER_BY_ASC(q.getFieldOrder());
    
    OIterator<? extends MdWebField> iterator = q.getIterator();

    try
    {
      return iterator.getAll();
    }
    finally
    {
      iterator.close();
    }
  }
}
