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
package com.runwaysdk.constants;

public interface IndicatorCompositeInfo extends IndicatorElementInfo
{
  /**
   * Interface {@link IndicatorCompositeInfo}.
   */
  public static final String CLASS_NAME    = "IndicatorComposite";

  public static final String CLASS         = Constants.METADATA_PACKAGE + "." + CLASS_NAME;

  /**
   * OID.
   */
  public static final String ID_VALUE      = "ijx2qpvkmeubmt67o8t8ac0a5nd4x8pc00000000000000000000000000000001";

  /**
   * The left operand that references a {@link IndicatorElementInfo}
   */
  public static final String LEFT_OPERAND  = "leftOperand";

  /**
   * The operator {@link Operators} used in the {@link IndicatorCompositeInfo}
   */
  public static final String OPERATOR      = "operator";

  /**
   * The right operand that references a {@link IndicatorElementInfo}
   */
  public static final String RIGHT_OPERAND = "rightOperand";

  public static final String PERCENTAGE    = "percentage";
}
