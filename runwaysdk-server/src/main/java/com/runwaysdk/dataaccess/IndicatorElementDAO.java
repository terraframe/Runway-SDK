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
package com.runwaysdk.dataaccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public abstract class IndicatorElementDAO extends BusinessDAO implements IndicatorElementDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 1842135005287070391L;

  public IndicatorElementDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  public IndicatorElementDAO()
  {
    super();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public IndicatorElementDAO getBusinessDAO()
  {
    return (IndicatorElementDAO) super.getBusinessDAO();
  }
  
  public static class IndicatorVisitor
  {
    /**
     * Key: The key of the {@link MdAttributePrimitiveDAOIF}
     * Object: {@link MdAttributePrimitiveDAOIF}
     */
    private Map<String, MdAttributePrimitiveDAOIF> attrPrimitiveMap;
  
    public IndicatorVisitor()
    {
      this.attrPrimitiveMap = new HashMap<String, MdAttributePrimitiveDAOIF>();
    }
  
    public void addMdAttributePrimitive(MdAttributePrimitiveDAOIF mdAttributePrimitive)
    {
      this.attrPrimitiveMap.put(mdAttributePrimitive.getKey(), mdAttributePrimitive);
    }
  
    public List<MdAttributePrimitiveDAOIF> getMdAttributePrimitives()
    {
      return this.attrPrimitiveMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
  
    public void visit(IndicatorCompositeDAOIF indicatorComposite)
    {
      // Nothing to collect for now.
    }
    
    public void visit(IndicatorPrimitiveDAOIF indicatorPrimitive)
    {
      MdAttributePrimitiveDAOIF mdAttrPrimitive = indicatorPrimitive.getMdAttributePrimitive();
      this.addMdAttributePrimitive(mdAttrPrimitive);
    }
  }

}
