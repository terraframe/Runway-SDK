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

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeInteger_Q;
import com.runwaysdk.session.Session;

public class EXTRACT extends IntegerFunction
{
  public enum Unit
  {
    YEAR("YEAR"),
    MONTH("MONTH"),
    DAY("DAY"),
    HOUR("HOUR"),
    MINUTE("MINUTE"),
    SECOND("SECOND");

    private String sqlString;

    private Unit(String sqlString)
    {
      this.sqlString = sqlString;
    }

    public String getSQL()
    {
      return this.sqlString;
    }
  }

  private Unit unit;

  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   */
  protected EXTRACT(Unit unit, Selectable selectable)
  {
    this(unit, selectable, null, null);
  }

  /**
   *
   * @param selectable nested selectable.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected EXTRACT(Unit unit, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);

    MdAttributeConcreteDAOIF selectableMdAttribute = selectable.getMdAttributeIF();

    if (!(selectableMdAttribute instanceof MdAttributeMomentDAOIF))
    {
      String errMsg = "Selectable ["+selectableMdAttribute.getDisplayLabel(Session.getCurrentLocale())+"] is not a date or time and was passed into a date or time function.";
      throw new InvalidMomentSelectableException(errMsg, selectable);
    }

    this.unit = unit;
    this.setMdAttributeIF(new MdAttributeInteger_Q(this.selectable.getMdAttributeIF()));
  }


  public String getSQL()
  {
    return this.getFunctionName()+"(" + unit.getSQL()+" FROM "+this.selectable.getSQL() + ")";
  }

  @Override
  protected String getFunctionName()
  {
    return "EXTRACT";
  }

}
