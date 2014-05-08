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
package com.runwaysdk.query;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDoubleDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFloatDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIntegerDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLongDAOIF;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeBoolean_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeDouble_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeFloat_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeInteger_Q;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeLong_Q;



public abstract class SimpleFunction extends Function implements Statement, SelectableSingle
{
  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   */
  protected SimpleFunction(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  public SimpleFunction clone() throws CloneNotSupportedException
  {
    return (SimpleFunction)super.clone();
  }

  /**
   * Sets the {@link MdAttributeDAOIF} to the appropriate numerical type.
   *
   * @param selectable
   */
  protected void setNumericMdAttribute(Selectable selectable1, Selectable selectable2)
  {
    if (selectable1.getMdAttributeIF() instanceof MdAttributeDoubleDAOIF ||
        selectable2.getMdAttributeIF() instanceof MdAttributeDoubleDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
    else if (selectable1.getMdAttributeIF() instanceof MdAttributeFloatDAOIF ||
             selectable2.getMdAttributeIF() instanceof MdAttributeFloatDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeFloat_Q(this.selectable.getMdAttributeIF()));
    }
    else if (selectable1.getMdAttributeIF() instanceof MdAttributeLongDAOIF ||
             selectable2.getMdAttributeIF() instanceof MdAttributeLongDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeLong_Q(this.selectable.getMdAttributeIF()));
    }
    else if (selectable1.getMdAttributeIF() instanceof MdAttributeIntegerDAOIF ||
             selectable2.getMdAttributeIF() instanceof MdAttributeIntegerDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeInteger_Q(this.selectable.getMdAttributeIF()));
    }
    else if (selectable1.getMdAttributeIF() instanceof MdAttributeBooleanDAOIF ||
             selectable2.getMdAttributeIF() instanceof MdAttributeBooleanDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeBoolean_Q(this.selectable.getMdAttributeIF()));
    }
    // Should never get here
    else
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
  }

  /**
   * Sets the {@link MdAttributeDAOIF} to the appropriate numerical type.
   *
   * @param _selectable
   */
  protected void setIntegerMdAttribute(Selectable _selectable)
  {
    if (_selectable.getMdAttributeIF() instanceof MdAttributeDoubleDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeFloatDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeFloat_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeLongDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeLong_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeIntegerDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeInteger_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeBooleanDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeBoolean_Q(this.selectable.getMdAttributeIF()));
    }
    // Should never get here
    else
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
  }

  /**
   * Sets the {@link MdAttributeDAOIF} to the appropriate numerical type.
   *
   * @param _selectable
   */
  protected void setLongMdAttribute(Selectable _selectable)
  {
    if (_selectable.getMdAttributeIF() instanceof MdAttributeDoubleDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeFloatDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeFloat_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeLongDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeLong_Q(this.selectable.getMdAttributeIF()));
    }
    else
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
  }

  /**
   * Sets the {@link MdAttributeDAOIF} to the appropriate numerical type.
   *
   * @param _selectable
   */
  protected void setFloatMdAttribute(Selectable _selectable)
  {
    if (_selectable.getMdAttributeIF() instanceof MdAttributeDoubleDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
    else if (_selectable.getMdAttributeIF() instanceof MdAttributeFloatDAOIF)
    {
      this.setMdAttributeIF(new MdAttributeFloat_Q(this.selectable.getMdAttributeIF()));
    }
    else
    {
      this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
    }
  }

  /**
   * Sets the {@link MdAttributeDAOIF} to the appropriate numerical type.
   *
   * @param _selectable
   */
  protected void setDoubleMdAttribute(Selectable _selectable)
  {
    this.setMdAttributeIF(new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
  }
}
