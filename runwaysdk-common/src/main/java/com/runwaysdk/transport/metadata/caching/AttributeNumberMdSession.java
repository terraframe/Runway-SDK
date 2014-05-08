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
package com.runwaysdk.transport.metadata.caching;

public class AttributeNumberMdSession extends AttributeMdSession {
  
  /**
   * Denotes if this number rejects zero.
   */
  private boolean rejectZero;
  
  /**
   * Denotes if this number rejects negative values.
   */
  private boolean rejectNegative;
  
  /**
   * Denotes if this number rejects positive values.
   */
  private boolean rejectPositive;
  
  public AttributeNumberMdSession(boolean rejectZero, boolean rejectNegative, boolean rejectPositive) {
    this.rejectZero = rejectZero;
    this.rejectNegative = rejectNegative;
    this.rejectPositive = rejectPositive;
  }

  public boolean isRejectZero() {
    return rejectZero;
  }

  public boolean isRejectNegative() {
    return rejectNegative;
  }

  public boolean isRejectPositive() {
    return rejectPositive;
  }
  
}
