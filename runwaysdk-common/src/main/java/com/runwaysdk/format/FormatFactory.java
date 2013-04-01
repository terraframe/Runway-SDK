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
package com.runwaysdk.format;

import com.google.inject.ImplementedBy;

/**
 * A FormatFactory that allows access to custom Format classes. There
 * are also shortcut methods to format and parse values.
 */
@ImplementedBy(StandardFormat.class)
public interface FormatFactory
{

  /**
   * Returns the specialized Format class that can format/parse instances
   * of the given class.
   * 
   * @param clazz
   * @return
   */
  public <T> Format<T> getFormat(Class<T> clazz);
}
