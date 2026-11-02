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
package com.runwaysdk.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.business.ExpressionException;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.MdAttributePrimitiveDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;


public abstract class MdAttributePrimitiveDAO extends MdAttributeConcreteDAO implements MdAttributePrimitiveDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -212665433619931419L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributePrimitiveDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null
   * <br/><b>Precondition:</b>ObjectCache.isSubTypeOf(classType, Constants.MD_CLASS)
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributePrimitiveDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the default value for the attribute that this metadata defines. If
   * no default value has been defined, an empty string is returned.
   *
   * @return the default value for the attribute that this metadata defines.
   */
  public String getDefaultValue()
  {
    return getDefaultValue(this.getAttributeIF(MdAttributePrimitiveInfo.DEFAULT_VALUE).getValue());
  }
  
  /**
   * True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   * 
   * @return True if the value of the attribute is calculated as a result of a user defined expression, false otherwise.
   */
  public boolean isExpression()
  {
    return ((AttributeBooleanIF)this.getAttributeIF(MdAttributePrimitiveInfo.IS_EXPRESSION)).getBooleanValue();
  }
  
  /**
   * Returns the user defined expression string. If none is defined then an empty string is returned.
   * 
   * @return user defined expression string. If none is defined then an empty string is returned.
   */
  public String getExpression()
  {
    return this.getAttributeIF(MdAttributePrimitiveInfo.EXPRESSION).getValue();
  }
  
  /**
   * 
   */
  public String save(boolean validateRequired)
  {
    this.validateExpression();
    
    return super.save(validateRequired);
  }
  
  
  /**
   * Throws an <code>ExpressionException</code> if this is set to be an expression attribute.
   * Expression attributes were evaluated with OGNL, which has been removed.
   * 
   */
  private void validateExpression()
  {
    if (this.isExpression())
    {
      String devMessage = "The attribute [" + this.definesAttribute() + "] is defined as an expression attribute, which is no longer supported.";
      throw new ExpressionException(devMessage, this, new UnsupportedOperationException("Expression attributes are no longer supported."));
    }
  }
}
