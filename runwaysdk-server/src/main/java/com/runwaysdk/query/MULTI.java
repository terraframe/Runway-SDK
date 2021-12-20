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
package com.runwaysdk.query;


public class MULTI extends MixedMathType
{
  /**
   * MULTI function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  protected MULTI(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);
  }

  /**
   * MULTI function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   */
  protected MULTI(int intValue, Selectable selectable)
  {
    super(intValue, selectable);
  }



  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   */
  protected MULTI(Selectable selectable, int intValue)
  {
    super(selectable, intValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param intValue
   */
  protected MULTI(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected MULTI(long longValue, Selectable selectable)
  {
    super(longValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param longValue
   * @param selectable
   */
  protected MULTI(Selectable selectable, long longValue)
  {
    super(selectable, longValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected MULTI(float floatValue, Selectable selectable)
  {
    super(floatValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param floatValue
   * @param selectable
   */
  protected MULTI(Selectable selectable, float floatValue)
  {
    super(selectable, floatValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   */
  protected MULTI(double doubleValue, Selectable selectable)
  {
    super(doubleValue, selectable);
  }

  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * MULTI function.
   *
   * @param doubleValue
   * @param selectable
   */
  protected MULTI(Selectable selectable, double doubleValue)
  {
    super(selectable, doubleValue);
  }

  /**
   * MULTI function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected MULTI(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);
  }


  @Override
  protected String getOperator()
  {
    return "*";
  }

  @Override
  protected String getFunctionName()
  {
    return "MULTI";
  }

}
