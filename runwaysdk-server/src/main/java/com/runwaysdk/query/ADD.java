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

public class ADD extends MixedMathType
{
  /**
   * ADD function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  protected ADD(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);
  }

  /**
   * ADD function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected ADD(int intValue, Selectable selectable)
  {
    super(intValue, selectable);
  }



  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   */
  protected ADD(Selectable selectable, int intValue)
  {
    super(selectable, intValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected ADD(long longValue, Selectable selectable)
  {
    super(longValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param longValue
   * @param selectable
   */
  protected ADD(Selectable selectable, long longValue)
  {
    super(selectable, longValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected ADD(float floatValue, Selectable selectable)
  {
    super(floatValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param floatValue
   * @param selectable
   */
  protected ADD(Selectable selectable, float floatValue)
  {
    super(selectable, floatValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected ADD(double doubleValue, Selectable selectable)
  {
    super(doubleValue, selectable);
  }

  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * ADD function.
   *
   * @param doubleValue
   * @param selectable
   */
  protected ADD(Selectable selectable, double doubleValue)
  {
    super(selectable, doubleValue);
  }

  /**
   * ADD function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected ADD(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }


  @Override
  protected String getOperator()
  {
    return "+";
  }

  @Override
  protected String getFunctionName()
  {
    return "ADD";
  }
}
