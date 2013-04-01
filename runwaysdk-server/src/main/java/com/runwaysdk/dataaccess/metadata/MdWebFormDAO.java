/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.WebFormFieldInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;


public class MdWebFormDAO extends MdFormDAO implements MdWebFormDAOIF
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 2756828613984090383L;

  public MdWebFormDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  public MdWebFormDAO()
  {
    super();
  }
  
  
  /**
   * Returns the MdFormDAOIF with the given id.
   * 
   * @param id
   * @return
   */
  public static MdWebFormDAOIF get(String id)
  {
    return (MdWebFormDAOIF) BusinessDAO.get(id);
  }

  public static MdWebFormDAO newInstance()
  {
    return (MdWebFormDAO) BusinessDAO.newInstance(MdWebFormDAOIF.CLASS);
  }
  
  public MdWebFormDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebFormDAO(attributeMap, MdWebFormDAOIF.CLASS);
  }
  
  @Override
  public List<MdWebFieldDAOIF> getAllMdFields()
  {
    // FIXME push logic into MdFormDAO
    List<MdWebFieldDAOIF> fields = new LinkedList<MdWebFieldDAOIF>();
    
    for(RelationshipDAOIF rel : getChildren(WebFormFieldInfo.CLASS))
    {
      fields.add((MdWebFieldDAOIF)rel.getChild());
    }
    
    //Sort the MdParamters into ascending order by the parameter order
    Collections.sort(fields, new Comparator<MdWebFieldDAOIF>()
    {
      public int compare(MdWebFieldDAOIF f1, MdWebFieldDAOIF f2)
      {
        Integer o1 = Integer.parseInt(f1.getFieldOrder());
        Integer o2 = Integer.parseInt(f2.getFieldOrder());

        return o1.compareTo(o2);
      }
    });
    
    return fields;
  }

  @Override
  public MdWebFieldDAOIF getMdField(String fieldName)
  {
    // FIXME optimize by performing a query for a single object or by reading
    // the cache
    return this.getMdFieldsByName().get(fieldName);
  }

  @Override
  public Map<String, ? extends MdWebFieldDAOIF> getMdFieldsByName()
  {
    Map<String, MdWebFieldDAOIF> fields = new HashMap<String, MdWebFieldDAOIF>();
    for(MdWebFieldDAOIF field : this.getAllMdFields())
    {
      fields.put(field.getFieldName(), field);
    }
    
    return fields;
  }
  
}
