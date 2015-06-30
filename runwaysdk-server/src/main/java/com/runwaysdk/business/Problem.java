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
package com.runwaysdk.business;

import com.runwaysdk.ProblemIF;

/**
 * Root of the hierarchy for all generated Problems. Problems interrupt
 * execution, but only at the end of the transaction. This allows multiple
 * problems to be collected before presentation to the end user.
 *
 * @author Eric
 */
public abstract class Problem extends Notification implements ProblemIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 1289660481544498671L;

  public Problem()
  {
    super();
  }

  public Problem(String devMessage)
  {
    super(devMessage);
  }

  /**
   * Finalizes the Problem.  Transaction will obtain a reference
   * to this problem and prevent the transaction from completing.
   */
  public final void throwIt()
  {
    // marker method.
  }
}
