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
package com.runwaysdk.transport.attributes;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.transport.metadata.AttributeHashMdDTO;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;
import com.runwaysdk.util.EncryptionUtil;

/**
 * DTO to represent hash attributes.
 */
public class AttributeHashDTO extends AttributeEncryptionDTO implements Serializable
{

  /**
   * Auto-generated serial id.
   */
  private static final long serialVersionUID = -1204812329992037944L;

  /**
   * HashDTO Constructor.
   *
   * @param name
   * @param type
   * @param value
   * @param readable
   * @param writable
   * @param modified
   */
  protected AttributeHashDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
  }


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
  public boolean encryptionEquals(String compareValue, boolean alreadyEncrypted)
  {
    // encrypt the value if it not already encrypted
    if (!alreadyEncrypted)
    {
      try
      {
        compareValue = EncryptionUtil.digestMethod(compareValue, getAttributeMdDTO().getEncryptionMethod());
      }
      catch (NoSuchAlgorithmException e)
      {
        CommonExceptionProcessor.processException(
            ExceptionConstants.ProgrammingErrorException.getExceptionClass(), e.getMessage(), e);
      }
    }

    // make the comparison
    if (compareValue.equals(getValue()))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  @Override
  public String getType()
  {
    return MdAttributeHashInfo.CLASS;
  }

  @Override
  public AttributeDTO clone()
  {
    // clone the attribute values
    AttributeHashDTO clone = (AttributeHashDTO) super.clone();
    CommonAttributeFacade.setEncryptionMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());

    return clone;
  }

  @Override
  public AttributeHashMdDTO getAttributeMdDTO()
  {
    return (AttributeHashMdDTO) super.getAttributeMdDTO();
  }

}
