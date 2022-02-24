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
package com.runwaysdk.business;

import java.io.Serializable;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;

public class MethodMetaData implements Serializable
{
  /**
   * Auto generated serializable oid
   */
  private static final long serialVersionUID = -2380180278873914600L;

  /**
   * The fully qualified name of the class on which the method resides
   */
  private final String      className;

  /**
   * The name of the method to invoke
   */
  private final String      methodName;

  /**
   * The declared parameter types of the method
   */
  private final String[]    declaredTypes;

  /**
   * The actual parameter types of the parameters
   */
  private String[]          actualTypes;

  /**
   * Stores metadata for the method invocation.
   * 
   * @param className
   *          The fully qualified name of the class on which the method is
   *          defined
   * @param methodName
   *          The name of the method to invoke
   * @param declaredTypes
   *          The declared parameter types of the method in the order on which
   *          they appear in the method signature
   */
  public MethodMetaData(String className, String methodName, String[] declaredTypes)
  {
    this.className = className;
    this.methodName = methodName;
    this.declaredTypes = declaredTypes;
    this.actualTypes = new String[0];

    if (declaredTypes == null)
    {
      String msg = "Attribute [declaredTypes] cannot be null on class [MethodMetaData]";
      throw new NullPointerException(msg);
    }
  }

  /**
   * Returns the fully qualified class name
   * 
   * @return
   */
  public String getClassName()
  {
    return className;
  }

  /**
   * Returns the method name
   * 
   * @return
   */
  public String getMethodName()
  {
    return methodName;
  }

  /**
   * Returns the declared parameters types in the order they appear in the
   * method signature
   * 
   * @return
   */
  public String[] getDeclaredTypes()
  {
    return declaredTypes;
  }

  /**
   * Sets the actual parameter types of the method in the order they appear in
   * the method signature
   * 
   * @param actualTypes
   */
  public final void setActualTypes(String[] actualTypes)
  {
    if (actualTypes.length != declaredTypes.length)
    {
      String msg = "The actual types must be the same size as the delcared types";
      CommonExceptionProcessor.processException(ExceptionConstants.ProgrammingErrorException.getExceptionClass(), msg);
    }

    this.actualTypes = actualTypes;
  }

  /**
   * Returns the actual parameter types in the order they appear in the method
   * signature.
   * 
   * @return
   */
  public String[] getActualTypes()
  {
    return actualTypes;
  }

  @Override
  public String toString()
  {
    return "[MethodMetaData][" + this.className + "." + this.methodName + "]";
  }
}
