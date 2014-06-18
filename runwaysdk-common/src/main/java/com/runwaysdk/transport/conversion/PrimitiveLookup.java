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
package com.runwaysdk.transport.conversion;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;

/**
 *
 *
 */
public class PrimitiveLookup
{
  /**
   * A map where the key is a java type and the value is a primitive type.
   */
  private static Map<String, String> primitiveTypeFromJavaTypeMap = null;

  /**
   * A map where the key is a primitive type and the value is a java type.
   */
  private static Map<String, String> javaTypeFromPrimitiveTypeMap = null;

  /**
   * Load the two maps that will be used for lookup.
   */
  private static void loadMaps()
  {
    primitiveTypeFromJavaTypeMap = new HashMap<String, String>();
    javaTypeFromPrimitiveTypeMap = new HashMap<String, String>();

    for(PrimitiveTypes type : PrimitiveTypes.values())
    {
      // mapping for java type -> primitive type
      primitiveTypeFromJavaTypeMap.put(type.getJavaType(), type.getPrimitiveType());

      // mapping from primitive type -> java type
      javaTypeFromPrimitiveTypeMap.put(type.getPrimitiveType(), type.getJavaType());
    }
  }

  /**
   * An enum of PrimitiveType mappings.
   */
  public enum PrimitiveTypes
  {
    BOOLEAN(Boolean.class.getName(), Boolean.class.getSimpleName()),

    CHARACTER(Character.class.getName(), Character.class.getSimpleName()),

    STRING(String.class.getName(), String.class.getSimpleName()),

    INTEGER(Integer.class.getName(), Integer.class.getSimpleName()),

    SHORT(Short.class.getName(), Short.class.getSimpleName()),

    DOUBLE(Double.class.getName(), Double.class.getSimpleName()),

    FLOAT(Float.class.getName(), Float.class.getSimpleName()),

    LONG(Long.class.getName(), Long.class.getSimpleName()),

    BYTE(Byte.class.getName(), Byte.class.getSimpleName());

    private String javaType;

    private String primitiveType;

    /**
     * Enum constructor
     *
     * @param javaType
     * @param primitiveType
     */
    private PrimitiveTypes(String javaType, String primitiveType)
    {
      this.javaType = javaType;
      this.primitiveType = primitiveType;
    }

    /**
     * @return A String representing the java type name.
     */
    private String getJavaType()
    {
      return javaType;
    }

    /**
     * @return A String representing the primitive type name.
     */
    private String getPrimitiveType()
    {
      return primitiveType;
    }
  }

  /**
   * Returns the java type associated with a specific primitive type.
   *
   * @param primitiveType
   * @return
   */
  public static synchronized String getJavaTypeFromPrimitiveType(String primitiveType)
  {
    // initialize the map if needed
    if(javaTypeFromPrimitiveTypeMap == null) loadMaps();

    String type = javaTypeFromPrimitiveTypeMap.get(primitiveType);

    // make sure a match was found
    if(type == null)
    {
      String error = "The primitive type ["+primitiveType+"] is not supported for conversion";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
    }

    return type;
  }

  /**
   * Returns the primitive type associated with a specific java type.
   *
   * @param javaType
   * @return A String representing a primitive type.
   */
  public static synchronized String getPrimitiveTypeFromJavaType(String javaType)
  {
    // initialize the map if needed
    if(primitiveTypeFromJavaTypeMap == null) loadMaps();

    String type = primitiveTypeFromJavaTypeMap.get(javaType);

    // make sure a match was found
    if(type == null)
    {
      String error = "The java type ["+javaType+"] is not supported for conversion";
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
    }

    return type;
  }
}
