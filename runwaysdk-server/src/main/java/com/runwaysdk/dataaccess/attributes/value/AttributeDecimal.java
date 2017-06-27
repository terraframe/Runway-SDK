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
package com.runwaysdk.dataaccess.attributes.value;

import java.math.BigDecimal;
import java.util.Set;

import com.runwaysdk.dataaccess.AttributeDecimalIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeDecimalCommonIF;

public class AttributeDecimal extends AttributeNumber implements AttributeDecimalIF, AttributeDecimalCommonIF
{

  /**
   *
   */
  private static final long serialVersionUID = 1068706331037740414L;

  /**
   * Display length of the entire number (ie, 3.14 has DisplayLength = 3).
   */
  private int displayLength;

  /**
   * The number of decimal digits to the right of the decimal (ie, 3.14 has DecimalDigits = 2).
   *
   */
  private int decimalDigits;

  /**
   * Creates an attribute with the given name.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> definingEntityType != null <br>
   * <b>Precondition: </b> !definingEntityType().equals("") <br>
   * <b>Precondition: </b> definingEntityType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param the value of the attribute
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute.
   */
  protected AttributeDecimal(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, value, definingEntityType, mdAttributeIF, entityMdAttributeIFset);

    this.value = com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal.setDecimalAndLength(this);
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public BigDecimal getTypeSafeValue()
  {
    if (this.getValue().trim().equals(""))
    {
      return null;
    }
    else
    {
      return new BigDecimal(this.getValue());
    }
  }
  
  /**
   * Returns the DisplayLength
   */
  public int getDisplayLength()
  {
    return this.displayLength;
  }

  /**
   * Returns the DisplayLength
   */
  public void setDisplayLength(int displayLength)
  {
    this.displayLength = displayLength;
  }

  /**
   * Returns the decimalDigits
   */
  public int getDecimalDigits()
  {
    return this.decimalDigits;
  }

  /**
   * Returns the decimalDigits
   */
  public void setDecimalDigits(int decimalDigits)
  {
    this.decimalDigits = decimalDigits;
  }

}
