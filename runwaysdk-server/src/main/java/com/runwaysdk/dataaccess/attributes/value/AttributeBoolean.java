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

import java.util.Set;

import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class AttributeBoolean extends Attribute implements AttributeBooleanIF
{

  /**
   *
   */
  private static final long serialVersionUID = -5456936863157871312L;

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
  protected AttributeBoolean(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, value, definingEntityType, mdAttributeIF, entityMdAttributeIFset);

    if (value.trim().equals(""))
    {
      this.value = "";
    }
    else
    {
      Float floatValue = new Float(value);

      this.value = floatValue.intValue()+"";
    }
  }

  /**
   * Returns the MdAttribute that defines the attribute from which the value came.
   *
   * @return MdAttribute that defines the attribute from which the value came.
   */
  public MdAttributeBooleanDAOIF getMdAttribute()
  {
    return (MdAttributeBooleanDAOIF)this.mdAttributeIF;
  }

  /**
   * Returns the boolean value of this attribute.
   * @return boolean value of this attribute.
   */
  public boolean getBooleanValue()
  {
    if (this.value.equals("1"))
    {
      return true;
    }
    return false;
  }

  /**
   *Returns 1 if the given attributeBoolean is true, 0.
   *
   * @return 1 if the given attributeBoolean is true, 0.
   */
  public int getBooleanValueInt()
  {
    return Integer.parseInt(this.value);
  }

  /**
   * Tests the string value of the bolean, and returns a primitive
   * <code><b>boolean</b></code>.
   *
   * @return <code><b>true</b></code> if the boolean attribute is MdAttributeBooleanIF.TRUE
   */
  public boolean isTrue()
  {
    if (this.value.equals("1"))
    {
      return true;
    }
    return false;
  }

  /**
   * Tests the string value of the bolean, and returns a primitive
   * <code><b>boolean</b></code>.  Equivalent to <code>!value</code>.
   *
   * @return <code><b>true</b></code> if the boolean attribute is MdAttributeBooleanIF.FALSE
   */
  public boolean isFalse()
  {
    if (this.value.equals("0"))
    {
      return false;
    }
    return true;
  }

  /**
   * Returns the formatted value of the attribute.  Some attributes format
   * this value to something other than what is stored in the database.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return value of the attribute.
   */
  public String getValue()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.convertIntToString(this.value);
  }

}
