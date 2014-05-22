/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.metadata;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.MobileFormFieldInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdMobileFieldDAOIF;
import com.runwaysdk.dataaccess.MdMobileFormDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public class MdMobileFormDAO extends MdFormDAO implements MdMobileFormDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -5865171940496016535L;

  public MdMobileFormDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  public MdMobileFormDAO()
  {
    super();
  }

  /**
   * Returns the MdFormDAOIF with the given id.
   * 
   * @param id
   * @return
   */
  public static MdMobileFormDAOIF get(String id)
  {
    return (MdMobileFormDAOIF) MdBusinessDAO.get(id);
  }

  public static MdMobileFormDAO newInstance()
  {
    return (MdMobileFormDAO) BusinessDAO.newInstance(MdMobileFormDAOIF.CLASS);
  }

  public MdMobileFormDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdMobileFormDAO(attributeMap, MdMobileFormDAOIF.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdFormDAOIF#getAllMdFieldsForDelete()
   */
  @Override
  public List<? extends MdFieldDAOIF> getAllMdFieldsForDelete()
  {
    // FIXME push logic into MdFormDAO
    List<MdMobileFieldDAOIF> fields = new LinkedList<MdMobileFieldDAOIF>();

    for (RelationshipDAOIF rel : getChildren(MobileFormFieldInfo.CLASS))
    {
      fields.add((MdMobileFieldDAOIF) rel.getChild());
    }

    // Sort the MdParamters into ascending order by the parameter order
    Collections.sort(fields, new Comparator<MdMobileFieldDAOIF>()
    {
      public int compare(MdMobileFieldDAOIF f1, MdMobileFieldDAOIF f2)
      {
        Integer o1 = Integer.parseInt(f1.getFieldOrder());
        Integer o2 = Integer.parseInt(f2.getFieldOrder());

        return o1.compareTo(o2);
      }
    });

    return fields;
  }

  @Override
  public List<MdMobileFieldDAOIF> getAllMdFields()
  {
    // FIXME push logic into MdFormDAO
    List<MdMobileFieldDAOIF> fields = new LinkedList<MdMobileFieldDAOIF>();

    for (RelationshipDAOIF rel : getChildren(MobileFormFieldInfo.CLASS))
    {
      fields.add((MdMobileFieldDAOIF) rel.getChild());
    }

    // Sort the MdParamters into ascending order by the parameter order
    Collections.sort(fields, new Comparator<MdMobileFieldDAOIF>()
    {
      public int compare(MdMobileFieldDAOIF f1, MdMobileFieldDAOIF f2)
      {
        Integer o1 = Integer.parseInt(f1.getFieldOrder());
        Integer o2 = Integer.parseInt(f2.getFieldOrder());

        return o1.compareTo(o2);
      }
    });

    return fields;
  }

  @Override
  public MdMobileFieldDAOIF getMdField(String fieldName)
  {
    // FIXME optimize by performing a query for a single object or by reading
    // the cache
    return this.getMdFieldsByName().get(fieldName);
  }

  @Override
  public Map<String, ? extends MdMobileFieldDAOIF> getMdFieldsByName()
  {
    Map<String, MdMobileFieldDAOIF> fields = new HashMap<String, MdMobileFieldDAOIF>();
    for (MdMobileFieldDAOIF field : this.getAllMdFields())
    {
      fields.put(field.getFieldName(), field);
    }

    return fields;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.MdFormDAOIF#getOrderedMdFields()
   */
  @Override
  public List<? extends MdFieldDAOIF> getOrderedMdFields()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
