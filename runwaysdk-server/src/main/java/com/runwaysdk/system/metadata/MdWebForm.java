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
package com.runwaysdk.system.metadata;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class MdWebForm extends MdWebFormBase
{
  private static final long serialVersionUID = -563015708;
  
  public MdWebForm()
  {
    super();
  }
  
  public MdField getField(String fieldName)
  {
    QueryFactory f = new QueryFactory();
    MdWebFieldQuery q = new MdWebFieldQuery(f);
    q.WHERE(q.getFieldName().EQ(fieldName));
    q.WHERE(q.getDefiningMdForm().EQ(this));
    
    OIterator<? extends MdWebField> it = q.getIterator();
    try
    {
      if (!it.hasNext())
      {
        String error = "A field named [" + fieldName + "] does not exist on type [" + this.getType() + "]";
        throw new DataNotFoundException(error, MdClass.getByKey(MdWebField.CLASS).getMdTypeDAO());
      }
      
      return it.next();
    }
    finally
    {
      it.close();
    }
  }
  
  private void recurseFields(List<MdField> allFields, List<? extends MdWebField> fields)
  {
    for(MdWebField field : fields)
    {
      allFields.add(field);

      if(field instanceof MdWebGroup)
      {
        MdWebGroup group = (MdWebGroup) field;
        recurseFields(allFields, group.getGroupFields());
      }
    }    
  }
  
  /**
   * Returns all MdFields of this MdForm in the proper field order.
   */
  public MdField[] getOrderedMdFields()
  {
    QueryFactory f = new QueryFactory();
    MdWebFieldQuery q = new MdWebFieldQuery(f);
    MdWebFieldQuery q1 = new MdWebFieldQuery(f);
    WebGroupFieldQuery relQ = new WebGroupFieldQuery(f);
    
    // exclude fields that are directly beneath a group
    relQ.WHERE(relQ.childOid().EQ(q1.getOid()));
    q.AND(q.SUBSELECT_NOT_IN_groupFields(relQ));
    

    q.WHERE(q.getDefiningMdForm().EQ(this));
    q.ORDER_BY_ASC(q.getFieldOrder());
    
    
    OIterator<? extends MdWebField> iterator = q.getIterator();

    try
    {
      List<MdField> allFields = new LinkedList<MdField>();
      List<? extends MdWebField> fields = iterator.getAll();
      
      recurseFields(allFields, fields);

      return allFields.toArray(new MdWebField[allFields.size()]);
    }
    finally
    {
      iterator.close();
    }
  }
}
