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
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Set;

import com.runwaysdk.dataaccess.AttributeBlobIF;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public class AttributeBlob extends Attribute implements AttributeBlobIF
{
  /**
   *
   */
  private static final long serialVersionUID = -2570477954748130636L;
  /**
   * Byte array to hold the value of this attribute.
   */
  private byte[]            valueBytes;

  /**
   * Creates an attribute with the given name.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> bytes != null <br>
   * <b>Precondition: </b> definingEntityType != null <br>
   * <b>Precondition: </b> !definingEntityType().equals("") <br>
   * <b>Precondition: </b> definingEntityType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param the value of the attribute
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute.
   */
  protected AttributeBlob(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, "", definingEntityType, mdAttributeIF, entityMdAttributeIFset);
    this.valueBytes = new byte[0];
  }

  /**
   * Returns the MdAttribute that defines the attribute from which the value came.
   *
   * @return MdAttribute that defines the attribute from which the value came.
   */
  public MdAttributeBlobDAOIF getMdAttribute()
  {
    return (MdAttributeBlobDAOIF)this.mdAttributeIF;
  }

  /**
   * Gets the value of this AttributeBlob. Because the data for a blob cannot be
   * accurately represented by a string value, this method will always return
   * just an empty string.
   */
  public String getValue()
  {
    return "";
  }

  /**
   * Sets the value of this blob as the specified bytes.
   *
   * @param bytes
   * @return The number of bytes written.
   */
  public int setBlobAsBytes(byte[] bytes)
  {
    if (bytes == null)
    {
      return 0;
    }

    valueBytes = bytes;
    return bytes.length;
  }

  /**
   * Returns the blob as an array of bytes.
   *
   * @return The byte array value of this blob attribute.
   */
  public byte[] getBlobAsBytes()
  {
    if (valueBytes != null)
    {
      return valueBytes.clone();
    }
    else
    {
      return new byte[0];
    }
  }


  /**
   * Gets the value of this blob as the specified bytes. This method works the
   * same as the Blob.getBytes(long pos, int length) as specified in the JDBC
   * 3.0 API. Because of this, the first element in the bytes to write to is
   * actually element 1 (as opposed to the standard array treatment where the
   * first element is at position 0).
   *
   * @param pos
   *          The starting position. The first element is at position 1.
   * @param length
   *          The length in bytes to grab after (and including) the starting
   *          positon.
   * @return byte[]
   */
  public byte[] getBlobAsBytes(long pos, int length)
  {
    if (valueBytes != null)
    {
      byte[] temp = new byte[length];
      pos = pos - 1;
      for (int i = 0; i < length; i++)
      {
        temp[i] = valueBytes[(int) pos];
        pos++;
      }
      return temp;
    }
    else
    {
      return new byte[0];
    }
  }

  /**
   * Returns the size of the blob.
   *
   * @return The length of this blob (in bytes).
   */
  public long getBlobSize()
  {
    if (valueBytes != null)
    {
      return valueBytes.length;
    }
    else
    {
      return new byte[0].length;
    }
  }



}
