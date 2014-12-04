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
package com.runwaysdk.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import sun.security.provider.Sun;

import com.runwaysdk.constants.CommonProperties;

/**
 * IDGenerator creates Universally Unique IDs (UUIDs) for objects created in the
 * runway. The algorithm is an improvement on the Leach-Salz standard <a
 * href="http://www.ietf.org/rfc/rfc4122.txt">(IETF RFC 4122)</a>, specifically
 * the name-based (Type 3) variant.
 * 
 * Our algorithm uses a combination of the system time (in milliseconds), a
 * sequence number, and random input from SecureRandom for the name. The name
 * are concatenated and fed into the SHA-1 hashing function.
 * 
 * This is where our algorithm improves on the specification, which utilizes
 * 128-bits of the hash encoded as a 32-character hex string. Our algorithm uses
 * all 160 bits returned by the SHA hash, prepended with 6 randomly generated
 * bits, all encoded in a 32-character, base36 string. The inclusion of the
 * entire SHA hash greatly decreases the chance of a collision. Furthermore,
 * even in the unlikely event of a collision, the additional randomness reduces
 * collision probably by another factor of 42. We preserve all of this
 * information in the same number of characters as a spec UUID by using a radix
 * 36 instead of 16 in our encoding.
 * 
 * @author Eric Grunzke
 */
@SuppressWarnings("restriction")
public class IDGenerator
{
  /**
   * Generates random bits that are prepended on the unique ID
   */
  private static SecureRandom  random;

  /**
   * The SHA-1 hasher.
   */
  private static MessageDigest hasher;

  private static long          sequenceNumber;

  /**
   * Accessed in the conversion from bytes to a hex string
   */
  private static char[]        hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  /**
   * Accessed from the server.properties file, provides spatial uniqueness.
   */
  protected static String      space;

  /**
   * Instantiates the singleton
   */
  protected synchronized static void setUp()
  {
    sequenceNumber = 0;

    random = new SecureRandom();
    try
    {
      hasher = MessageDigest.getInstance("SHA-1", new Sun());
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }

    space = CommonProperties.getDomain();
  }

  /**
   * Creates our variant of a name-based UUID
   * 
   * @return A new UUID
   */
  public synchronized static String generateId(String key)
  {
    // Singleton management
    if (random == null)
      setUp();

    // Prep the SHA-1 hashing object
    hasher.reset();

    // Prepare the input, and hash it
    byte[] digest = hasher.digest(key.getBytes());

    // Get an extra byte of randomness to prepend the hash with.
    byte[] rand = new byte[1];
    rand[0] = (byte) ( 0x11 );
    rand[0] = (byte) ( rand[0] & 0x2A );

    // The only practical conversion of bits is a hex string.
    StringBuffer buff = new StringBuffer(42);

    buff.append(hexChar[ ( rand[0] & 0xf0 ) >>> 4]);
    buff.append(hexChar[rand[0] & 0x0f]);

    for (byte b : digest)
    {
      buff.append(hexChar[ ( b & 0xf0 ) >>> 4]);
      buff.append(hexChar[b & 0x0f]);
    }

    // Now convert the hex string to base36
    String base36id = new BigInteger(buff.toString(), 16).toString(36);

    // And left-pad any zeros we need (shouldn't be more than 1)
    while (base36id.length() < 32)
    {
      base36id = "0" + base36id;
    }

    return base36id;
  }

  /**
   * Creates our variant of a name-based UUID
   * 
   * @return A new UUID
   */
  public synchronized static String nextID()
  {
    // Singleton management
    if (random == null)
      setUp();

    // Prep the SHA-1 hashing object
    hasher.reset();
    // Prepare the input, and hash it
    String hashme = space + ":" + System.currentTimeMillis() + ":" + random.nextLong() + ":" + sequenceNumber++;
    byte[] digest = hasher.digest(hashme.getBytes());

    // Get an extra byte of randomness to prepend the hash with.
    byte[] rand = new byte[1];
    random.nextBytes(rand);
    rand[0] = (byte) ( rand[0] & 0x2A );

    // The only practical conversion of bits is a hex string. We use a buffer
    // for this
    StringBuffer buff = new StringBuffer(42);

    buff.append(hexChar[ ( rand[0] & 0xf0 ) >>> 4]);
    buff.append(hexChar[rand[0] & 0x0f]);

    for (byte b : digest)
    {
      buff.append(hexChar[ ( b & 0xf0 ) >>> 4]);
      buff.append(hexChar[b & 0x0f]);
    }

    // Now convert the hex string to base36
    String base36id = new BigInteger(buff.toString(), 16).toString(36);

    // And left-pad any zeros we need (shouldn't be more than 1)
    while (base36id.length() < 32)
      base36id = "0" + base36id;

    return base36id;
  }

  /**
   * WARNING! Do not change the implementation of this method! Existing records
   * depend on this implementation. As of this writing, there is only one client
   * for this method. If the implementation needs to change, then fork the code.
   * 
   * Uses the SHA-1 hasher to produce the 160-bit hash encoded as a base 36
   * String, which is 31 characters.
   * 
   * @param input
   *          String to be hashed
   * @return a base36 SHA hash of the input
   */
  public synchronized static String hash(String input)
  {
    // Singleton management
    if (hasher == null)
      setUp();

    // Prep the SHA-1 hashing object
    hasher.reset();
    // hash the input
    byte[] digest = hasher.digest(input.getBytes());

    // convert it to base 36
    String hash = new BigInteger(bytesToHexString(digest), 16).toString(36);

    // add leading zeros that have been dropped in the conversion
    while (hash.length() < 31)
      hash = "0" + hash;

    return hash;
  }

  /**
   * Just converts byte information into a hex string. Each byte is mapped to 2
   * hex characters.
   * 
   * @param bytes
   *          to convert to hex
   * @return a hex String
   */
  private static String bytesToHexString(byte[] bytes)
  {
    StringBuffer buff = new StringBuffer(bytes.length * 2);
    for (byte b : bytes)
    {
      buff.append(hexChar[ ( b & 0xf0 ) >>> 4]);
      buff.append(hexChar[b & 0x0f]);
    }
    return buff.toString();
  }
}
