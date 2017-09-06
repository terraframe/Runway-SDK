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
package com.runwaysdk.dataaccess.attributes.entity;

import java.util.List;

import com.runwaysdk.dataaccess.AttributeIndicatorIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.IndicatorElementDAO.IndicatorVisitor;
import com.runwaysdk.dataaccess.IndicatorElementDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;

public class AttributeIndicator extends Attribute implements AttributeIndicatorIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 2846107532196065437L;

  public AttributeIndicator(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata.
   * @param definingEntityType
   * @param value
   */
  public AttributeIndicator(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }
  
  /**
   * @see AttributeIndicatorIF#getMdAttribute
   */
  public MdAttributeIndicatorDAOIF getMdAttribute()
  {
    return (MdAttributeIndicatorDAOIF)MdAttributeConcreteDAO.getByKey(this.mdAttributeKey);
  }
  
  /**
   * @see AttributeIndicatorIF#getIndicatorMD
   */
  public IndicatorElementDAOIF getIndicatorMD()
  {
    return getMdAttribute().getIndicator();
  }
  
  /**
   * Returns a List<{@link MdAttributePrimitiveDAOIF}> of attributes referenced by the indicator.
   * 
   * @return List<{@link MdAttributePrimitiveDAOIF}> of attributes referenced by the indicator.
   */
  public List<MdAttributePrimitiveDAOIF> getMdAttributePrimitives()
  {
    IndicatorVisitor visitor = new IndicatorVisitor();
    
    IndicatorElementDAOIF indicatorElement = this.getIndicatorMD();
    
    indicatorElement.accept(visitor);
    
    return visitor.getMdAttributePrimitives();
  }
  
  /**
   * @see com.runwaysdk.dataaccess.AttributeIF#getValue
   */
  public String getValue()
  {
    Object nonAggregatedExpressionValue = this.evalNonAggregateValue();
    
    if (nonAggregatedExpressionValue == null)
    {
      return "";
    }
    else
    {
      return this.evalNonAggregateValue().toString();
    }
  }
  
  /**
   * Returns the unaggregated value of the indicator for this single object.
   * 
   * @return the unaggregated value of the indicator for this single object.
   */
  public Object evalNonAggregateValue()
  {
    EntityDAO entityDAO = this.getContainingComponent();
    
    MdAttributeIndicatorDAOIF mdAttributeIndicator = this.getMdAttribute();
    
    IndicatorElementDAOIF indicatorElement = mdAttributeIndicator.getIndicator();
    
    return indicatorElement.evalNonAggregateValue(mdAttributeIndicator, entityDAO);
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a deep clone of this Attribute
   */
  @Override
  public Attribute attributeClone()
  {
    return new AttributeIndicator(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), new String(this.getRawValue()));
  }

}
