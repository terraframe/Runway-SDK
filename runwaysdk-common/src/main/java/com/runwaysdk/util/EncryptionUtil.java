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
package com.runwaysdk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.security.provider.Sun;

/**
 * Computes a message digest for a value.
 */
@SuppressWarnings("restriction")
public class EncryptionUtil
{

  /**
   * Digests a value and returns the hash value.
   * 
   * @param message
   * @param digestType
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String digestMethod(String message, String digestType) throws NoSuchAlgorithmException
  {
    String hash = null;

    if (message != null)
    {
      MessageDigest digest = MessageDigest.getInstance(digestType, new Sun());

      digest.update(message.getBytes());
      
      hash = Base64.encodeToString(digest.digest(), false);
    }
    
    return hash;
  }
}
