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
/*
 * Created on Aug 8, 2005
 *
 */
package com.runwaysdk.dataaccess;

/**
 * @author nathan
 *
 * TODO To change the template for this generated comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AttributeReferenceIF extends AttributeIF
{
  /**
   * 
   * @return
   */
  public BusinessDAOIF dereference();
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeReferenceDAOIF that defines the this attribute
   */
  public MdAttributeReferenceDAOIF getMdAttributeConcrete();
  
}
