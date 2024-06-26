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
package com.runwaysdk.constants;

/**
 * Used to store information about a class used in JUnit tests.
 * 
 * @author nathan
 *
 */
public class TypeInfo
{
  private String typeName;

  private String packageName;

  private String oid;

  public TypeInfo(String packageName, String typeName)
  {
    this.typeName = typeName;
    this.packageName = packageName;
  }

  /**
   * Returns the name of the class.
   * 
   * @return name of the class.
   */
  public String getTypeName()
  {
    return typeName;
  }

  /**
   * Returns the name of the package.
   * 
   * @return name of the package.
   */
  public String getPackageName()
  {
    return packageName;
  }

  /**
   * Returns the type of the class.
   * 
   * @return type of the class.
   */
  public String getType()
  {
    return this.packageName + "." + this.typeName;
  }

  public String getOid()
  {
    return oid;
  }

  public void setOid(String oid)
  {
    this.oid = oid;
  }

}
