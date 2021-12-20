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

import com.runwaysdk.dataaccess.attributes.value.MdAttributeDouble_Q;

public class DIV extends Math
{
  /**
   * DIV function. Performs the function on the two selectables.
   *
   * @param selectable1
   * @param selectable2
   */
  protected DIV(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param selectable1
   * @param selectable2
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(Selectable selectable1, Selectable selectable2, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable1, selectable2, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected DIV(int intValue, Selectable selectable)
  {
    super(intValue, selectable);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(int intValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(intValue, selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   */
  protected DIV(Selectable selectable, int intValue)
  {
    super(selectable, intValue);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param intValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(Selectable selectable, int intValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, intValue, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param intValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected DIV(long longValue, Selectable selectable)
  {
    super(longValue, selectable);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param longValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(long longValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(longValue, selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param longValue
   * @param selectable
   */
  protected DIV(Selectable selectable, long longValue)
  {
    super(selectable, longValue);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param longValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(Selectable selectable, long longValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, longValue, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected DIV(float floatValue, Selectable selectable)
  {
    super(floatValue, selectable);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(float floatValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(floatValue, selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param floatValue
   * @param selectable
   */
  protected DIV(Selectable selectable, float floatValue)
  {
    super(selectable, floatValue);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param floatValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(Selectable selectable, float floatValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, floatValue, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   */
  protected DIV(double doubleValue, Selectable selectable)
  {
    super(doubleValue, selectable);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(double doubleValue, Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(doubleValue, selectable, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param doubleValue
   * @param selectable
   */
  protected DIV(Selectable selectable, double doubleValue)
  {
    super(selectable, doubleValue);

    this.init();
  }

  /**
   * DIV function.
   *
   * @param selectable
   * @param doubleValue
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected DIV(Selectable selectable, double doubleValue, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, doubleValue, userDefinedAlias, userDefinedDisplayLabel);

    this.init();
  }

  private void init()
  {
   this.setMdAttributeIF( new MdAttributeDouble_Q(this.selectable.getMdAttributeIF()));
  }

  @Override
  protected String getFunctionName()
  {
    return "DIV";
  }

  @Override
  protected String getOperator()
  {
    return "/";
  }
}
