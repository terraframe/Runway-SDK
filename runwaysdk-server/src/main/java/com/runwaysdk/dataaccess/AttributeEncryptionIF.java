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
package com.runwaysdk.dataaccess;

public interface AttributeEncryptionIF extends AttributeIF
{
  /**
   * Compares the parameter input string (compareValue) to the encrypted value of
   * this attribute. The boolean parameter, alreadyEncrypted, denotes whether or
   * not the string input is already a hash or not. If alreadyEncrypted is set to
   * true, then the method will expect an encrypted string. Otherwise, if
   * alreadyEncrypted is set to false, the input string will automatically be
   * encrypted by the this method so the comparison will be accurate.
   * 
   * @param compareValue
   * @param alreadyEncrypted
   * @return
   */
  public boolean encryptionEquals(String compareValue, boolean alreadyEncrypted);
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeEncryptionDAOIF that defines the this attribute
   */
  public MdAttributeEncryptionDAOIF getMdAttributeConcrete();
}
