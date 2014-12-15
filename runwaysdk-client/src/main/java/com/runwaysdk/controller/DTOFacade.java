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
package com.runwaysdk.controller;

import java.lang.reflect.Method;

import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;

public class DTOFacade
{
  private String     attributeName;

  private String     rootMethod;

  private MutableDTO mutableDTO;

  private Class<?>   c;

  public DTOFacade(String attributeName, MutableDTO mutableDTO)
  {
    this.mutableDTO = mutableDTO;
    this.c = mutableDTO.getClass();

    this.attributeName = attributeName;
    this.rootMethod = attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
  }

  public Boolean isWritable()
  {
    try
    {
      String methodName = "is" + rootMethod + "Writable";
      return (Boolean) c.getMethod(methodName).invoke(mutableDTO);
    }
    catch (Exception e)
    {
      return true;
    }
  }

  public AttributeMdDTO getAttributeMdDTO() throws Exception
  {
    try
    {
      // Root name of all the accessor's getters and setters
      String accessorMd = "get" + rootMethod + "Md";

      return (AttributeMdDTO) c.getMethod(accessorMd).invoke(mutableDTO);
    }
    catch (Exception e)
    {
      if (this.mutableDTO.hasAttribute(this.attributeName))
      {
        return this.mutableDTO.getAttributeMd(this.attributeName);
      }
      else
      {
        throw e;
      }
    }
  }

  public Object getValue() throws Exception
  {
    try
    {
      String methodName = "get" + rootMethod;
      return c.getMethod(methodName).invoke(mutableDTO);
    }
    catch (Exception e)
    {
      if (this.mutableDTO.hasAttribute(this.attributeName))
      {
        return this.mutableDTO.getObjectValue(this.attributeName);
      }
      else
      {
        throw e;
      }
    }
  }

  public void setValue(Object value) throws Exception
  {
    try
    {
      String methodName = "set" + rootMethod;
      Class<?> javaType = this.getAttributeMdDTO().getJavaType();

      c.getMethod(methodName, javaType).invoke(mutableDTO, value);
    }
    catch (Exception e)
    {    
      if (this.mutableDTO.hasAttribute(this.attributeName))
      {
        this.mutableDTO.setValue(this.attributeName, value);
      }
      else
      {
        throw e;
      }
    }
  }

  public void addAttribute(Object value) throws Exception
  {
    // Get the method to add an enumeration
    try
    {
      String methodName = "add" + rootMethod;
      Class<?> javaType = this.getAttributeMdDTO().getJavaType();
      Method method = c.getMethod(methodName, javaType);

      method.invoke(mutableDTO, value);
    }
    catch (Exception e)
    {
      if (this.mutableDTO.hasAttribute(this.attributeName))
      {
        this.mutableDTO.addEnumItem(this.attributeName, value.toString());
      }
      else
      {
        throw e;
      }
    }
  }

  public void clearAttribute() throws Exception
  {
    try
    {
      String methodName = "clear" + rootMethod;

      c.getMethod(methodName).invoke(mutableDTO);
    }
    catch (Exception e)
    {
      if (this.mutableDTO.hasAttribute(this.attributeName))
      {
        this.mutableDTO.clearEnum(this.attributeName);
      }
      else
      {
        throw e;
      }
    }
  }
}
