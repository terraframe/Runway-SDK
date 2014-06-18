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
/**
 * Created on Aug 8, 2005
 *
 */
package com.runwaysdk.dataaccess;

/**
 * @author nathan
 *
 */
public interface AttributeStructIF extends AttributeIF
{

  /**
   * Returns the <code>MdStructDAOIF</code> that defines the type that this attribute references.
   * 
   *  Preconditions:  this.structDAO has been initialized.
   *  
   */
  public MdStructDAOIF getMdStructDAOIF();
  
  /**
   * 
   * precondition: this.structDAO is initialized
   * 
   * @return
   */
  public AttributeIF[] getAttributeArrayIF();
  
  /**
   * 
   * precondition: this.structDAO is initialized
   * 
   * @param attributeName 
   * @return
   */
  public AttributeIF getAttributeIF(String attributeName);
  
  /**
   * 
   * precondition: this.structDAO is initialized
   * 
   * @param attributeName
   * @return
   */
  public String getValue(String attributeName);
  
  /**
   * precondition: this.structDAO is initialized
   * 
   * @param blobName The name of the blob attribute
   * @return
   */
  public byte[] getBlob(String blobName);
  
  /**
   * precondition: this.structDAO is initialized
   * 
   * @return The delegated StructDAO of this AttributeStruct
   */
  public StructDAO getStructDAO();
}
