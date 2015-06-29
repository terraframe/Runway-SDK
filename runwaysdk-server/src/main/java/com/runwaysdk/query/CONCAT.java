/**
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
 */
package com.runwaysdk.query;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.session.Session;

public class CONCAT extends StringFunction
{
  private static int OPTION_1 = 1;
  private static int OPTION_2 = 2;
  private static int OPTION_3 = 3;
  private static int OPTION_4 = 4;


  private int selectedOption;
  private String stringToAppend;
  private Selectable selectable2;


  /**
   * String concatenate function.  Concatentates the result of the function with the given attribute.
   *
   * @param selectable1
   * @param selectable2
   */
  protected CONCAT(Selectable selectable1, Selectable selectable2)
  {
    this(selectable1, selectable2, null, null);
  }

  /**
   * String concatenate function.  Concatentates the two attribute.
   *
   * @param selectable1 attribute to concatenate.
   * @param selectable2 attribute to concatenate.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected CONCAT(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable1, userDefinedAlias, userDefinedDisplayLabel);

    this.stringToAppend       = null;

    if (selectable1 instanceof SelectablePrimitive &&
        selectable2 instanceof SelectablePrimitive)
    {
      this.selectable2          = selectable2;

      this.selectedOption       = OPTION_2;
    }
    else if (selectable1 instanceof SelectablePrimitive &&
        selectable2 instanceof Function)
    {
      this.selectable2          = selectable1;
      this.selectable           = selectable2;

      this.selectedOption       = OPTION_3;
    }
    else if (selectable1 instanceof Function &&
        selectable2 instanceof SelectablePrimitive )
    {
      this.selectable2          = selectable2;

      this.selectedOption = OPTION_2;
    }
    else if (selectable1 instanceof Function &&
        selectable2 instanceof Function )
    {
      this.selectable2          = selectable2;

      this.selectedOption = OPTION_3;
    }

    boolean selectable1IsAggregate = this.selectable.isAggregateFunction();

    boolean selectable2IsAggregate = this.selectable2.isAggregateFunction();

    if ( (selectable1IsAggregate == true && selectable2IsAggregate == false) ||
        (selectable1IsAggregate == false && selectable2IsAggregate == true))
    {
      String error = "The ["+this.getFunctionName()+"] cannot concatenate an aggregate function with a non aggregate value.";
      throw new QueryException(error);
    }
  }

  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param function tring to append
   * @param stringToAppend tring to append
   */
  protected CONCAT(Selectable selectable, String stringToAppend)
  {
    this(selectable, stringToAppend, null, null);
  }

  /**
   * String concatenate selectable.  Concatentates the result of the function with the given string.
   *
   * @param selectable tring to append
   * @param stringToAppend tring to append
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected CONCAT(Selectable selectable, String stringToAppend, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
    this.stringToAppend  = stringToAppend;
    this.selectable2  = null;

    this.selectedOption = OPTION_1;
  }

  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param stringToAppend tring to append
   * @param selectable tring to append
   */
  protected CONCAT(String stringToAppend, Selectable selectable)
  {
    this(stringToAppend, selectable, null, null);
  }

  /**
   * String concatenate function.  Concatentates the result of the function with the given string.
   *
   * @param selectable tring to append
   * @param stringToAppend tring to append
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected CONCAT(String stringToAppend, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
    this.stringToAppend  = stringToAppend;
    this.selectable2  = null;

    this.selectedOption = OPTION_4;
  }

  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    super.accept(visitor);

    if (this.selectable2  != null)
    {
      this.selectable2.accept(visitor);
    }
  }

  /**
   * Returns all MdAttributes that are involved in building the select clause.
   * @return all MdAttributes that are involved in building the select clause.
   */
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeList = new HashSet<MdAttributeConcreteDAOIF>();
    mdAttributeList.addAll(super.getAllEntityMdAttributes());

    if (this.selectable2 != null )
    {
      mdAttributeList.addAll(this.selectable2.getAllEntityMdAttributes());
    }

    return mdAttributeList;
  }

  /**
   * Returns the a nested aggregate function in this composite function tree, if there is one, or return null;
   * @return nested aggregate function in this composite function tree, if there is one, or return null;
   */
  public SelectableAggregate getAggregateFunction()
  {
    SelectableAggregate selectableAggregate1 = this.selectable.getAggregateFunction();

    if (selectableAggregate1 != null)
    {
      return selectableAggregate1;
    }

    if (this.selectable2 != null)
    {
      SelectableAggregate selectableAggregate2 = this.selectable2.getAggregateFunction();

      if (selectableAggregate2 != null)
      {
        return selectableAggregate2;
      }
    }

    // Base case
    return null;
  }

  /**
   * Calculates a display label for the result set.
   * @return display label for the result set.
   */
  protected String calculateDisplayLabel()
  {
    Locale currentLocale = Session.getCurrentLocale();

    String displayLabel;

    String secondString = "";

    if (stringToAppend != null)
    {
      secondString = "'"+this.stringToAppend+"'";
    }
    else if (this.selectable2 != null)
    {
      secondString = this.selectable2.getMdAttributeIF().getDisplayLabel(currentLocale);
    }

    // Base case
    if (this.selectable instanceof Attribute)
    {
      displayLabel = this.getFunctionName()+"("+this.getMdAttributeIF().getDisplayLabel(currentLocale)+", "+secondString+")";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateDisplayLabel()+", "+secondString+")";
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

    String secondString = "";

    if (stringToAppend != null)
    {
      secondString = this.stringToAppend;
    }
    else if (this.selectable2 != null)
    {
      secondString = this.selectable2._getAttributeName();
    }

    // Base case
    if (this.selectable instanceof Attribute)
    {
      displayLabel = this.getFunctionName()+"("+this.selectable._getAttributeName()+",'"+secondString+"')";
    }
    else
    {
      displayLabel = this.getFunctionName()+"("+((Function)this.selectable).calculateName()+",'"+secondString+"')";
    }

    return displayLabel;
  }

  /**
   *
   */
  public String getSQL()
  {
    if (this.selectedOption == OPTION_1)
    {
      return Database.buildConcatFunctionCall(this.selectable.getSQL(), "'"+this.stringToAppend+"'");
    }
    else if (this.selectedOption == OPTION_4)
    {
      return Database.buildConcatFunctionCall("'"+this.stringToAppend+"'", this.selectable.getSQL());
    }
    else if(this.selectedOption == OPTION_2)
    {
      return Database.buildConcatFunctionCall(this.selectable.getSQL(), this.selectable2.getSQL());
    }
    // this.selectedOption == OPTION_3
    else
    {
      return Database.buildConcatFunctionCall(this.selectable2.getSQL(), this.selectable.getSQL());
    }

  }

  @Override
  protected String getFunctionName()
  {
    return "CONCAT";
  }

  public CONCAT clone() throws CloneNotSupportedException
  {
    return (CONCAT)super.clone();
  }
}
