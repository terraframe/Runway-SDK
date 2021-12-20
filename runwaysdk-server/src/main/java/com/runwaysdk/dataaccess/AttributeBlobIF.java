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
package com.runwaysdk.dataaccess;

public interface AttributeBlobIF extends AttributeIF
{
  
  /**
   * Returns the blob as an array of bytes.
   * 
   * @return The byte array value of this blob attribute.
   */
  public byte[] getBlobAsBytes();
  
  /**
   * Gets the value of this blob as the specified bytes. This method works the
   * same as the Blob.getBytes(long pos, int length) as specified
   * in the JDBC 3.0 API. Because of this, the first element in the bytes to write to
   * is actually element 1 (as opposed to the standard array treatment where the
   * first element is at position 0).
   * 
   * @param pos The starting position. The first element is at position 1.
   * @param length The length in bytes to grab after (and including) the starting positon.
   * @return byte[]
   */
  public byte[] getBlobAsBytes(long pos, int length);
  
  /**
   * Returns the size of the blob.
   * 
   * @return The length of this blob (in bytes).
   */
  public long getBlobSize();
  
}
