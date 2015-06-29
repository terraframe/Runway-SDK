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

public class MOD extends MixedMathType
{

  /**
   * MOD function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  protected MOD(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);
  }

  /**
   * MOD function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected MOD(int intValue, Selectable selectable)
  {
    super(intValue, selectable);
  }



  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   */
  protected MOD(Selectable selectable, int intValue)
  {
    super(selectable, intValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param intValue
   * @param selectable
   */
  protected MOD(long longValue, Selectable selectable)
  {
    super(longValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param longValue
   * @param selectable
   */
  protected MOD(Selectable selectable, long longValue)
  {
    super(selectable, longValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected MOD(float floatValue, Selectable selectable)
  {
    super(floatValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param floatValue
   * @param selectable
   */
  protected MOD(Selectable selectable, float floatValue)
  {
    super(selectable, floatValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected MOD(double doubleValue, Selectable selectable)
  {
    super(doubleValue, selectable);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MOD function.
   *
   * @param doubleValue
   * @param selectable
   */
  protected MOD(Selectable selectable, double doubleValue)
  {
    super(selectable, doubleValue);
  }

  /**
   * MOD function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MOD(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }


  @Override
  protected String getOperator()
  {
    return "%";
  }

  @Override
  protected String getFunctionName()
  {
    return "MOD";
  }

}
