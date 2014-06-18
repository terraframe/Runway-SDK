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

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.session.Session;

public class LENGTH extends IntegerFunction
{
  /**
   * String LENGTH function.  Returns the length of the given column in the database.  For binary columns,
   * it returns the number of bits.  Otherwise, it returns the length of the number of characters in the column.
   *
   * @param function that returns a character.
   */
  protected LENGTH(Selectable selectable)
  {
    this(selectable, null, null);
  }

  /**
   * String LENGTH function.  Returns the length of the given column in the database.  For binary columns,
   * it returns the number of bits.  Otherwise, it returns the length of the number of characters in the column.
   *
   * @param function that returns a character.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected LENGTH(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Calculates a display label for the result set.
   * @return display label for the result set.
   */
  protected String calculateDisplayLabel()
  {
    String displayLabel;

    // Base case
    if (this.selectable instanceof Attribute)
    {
      displayLabel = this.getFunctionName()+"("+this.getMdAttributeIF().getDisplayLabel(Session.getCurrentLocale())+")";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateDisplayLabel()+")";
    }

    return displayLabel;
  }

  /**
   * Calculates a display label for the result set.
   * @return display label for the result set.
   */
  protected String calculateName()
  {
    String displayLabel;

    // Base case
    if (this.selectable instanceof Attribute)
    {
      displayLabel = this.getFunctionName()+"("+this.selectable._getAttributeName()+")";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateName()+")";
    }

    return displayLabel;
  }

  /**
   *
   */
  @Override
  public String getSQL()
  {
    return Database.buildLenthFunctionCall(this.selectable.getSQL());
  }

  @Override
  protected String getFunctionName()
  {
    return "LENGTH";
  }

  public LENGTH clone() throws CloneNotSupportedException
  {
    return (LENGTH)super.clone();
  }
}
