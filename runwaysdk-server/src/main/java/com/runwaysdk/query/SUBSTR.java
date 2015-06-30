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
package com.runwaysdk.query;

import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.session.Session;

public class SUBSTR extends StringFunction
{
  private int startIndex;

  private int numOfCharacters;

  /**
   * Substring selectable
   *
   * @param selectable Selectable that returns a String.
   * @param startIndex Starting string index.  First character is at index 1 (like in SQL).
   * @param numOfCharacters Number of characters starting with the startIndex.
   */
  protected SUBSTR(Selectable selectable,  int startIndex, int numOfCharacters)
  {
    this(selectable, startIndex, numOfCharacters, null, null);
  }

  /**
   * Substring function
   *
   * @param selectable Selectable that returns a String.
   * @param startIndex Starting string index.  First character is at index 1 (like in SQL).
   * @param numOfCharacters Number of characters starting with the startIndex.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected SUBSTR(Selectable selectable,  int startIndex, int numOfCharacters, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
    this.startIndex     = startIndex;
    this.numOfCharacters = numOfCharacters;
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
      displayLabel = this.getFunctionName()+"("+this.getMdAttributeIF().getDisplayLabel(Session.getCurrentLocale())+", "+this.startIndex+", "+this.numOfCharacters+")";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateDisplayLabel()+", "+this.startIndex+", "+this.numOfCharacters+")";
    }

    return displayLabel;
  }


  /**
   * Calculates a name for the result set.
   * @return a name for the result set.
   */
  protected String calculateName()
  {
    String displayLabel;

    // Base case
    if (this.selectable instanceof Attribute)
    {
      displayLabel = this.getFunctionName()+"("+this.selectable._getAttributeName()+","+this.startIndex+","+this.numOfCharacters+")";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateName()+","+this.startIndex+","+this.numOfCharacters+")";
    }

    return displayLabel;
  }

  /**
   *
   */
  public String getSQL()
  {
    return Database.buildSubstringFunctionCall(this.selectable.getSQL(), this.startIndex, this.numOfCharacters);
  }

  @Override
  protected String getFunctionName()
  {
    return "SUBSTR";
  }

  public SUBSTR clone() throws CloneNotSupportedException
  {
    return (SUBSTR)super.clone();
  }
}
