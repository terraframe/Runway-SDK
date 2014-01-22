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
package com.runwaysdk.dataaccess.database;

import com.runwaysdk.ConfigurationException;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.util.IDGenerator;


/**
 * IDGenerator creates Universally Unique IDs (UUIDs) for objects created in the
 * runway. The algorithm is an improvement on the Leach-Salz standard <a
 * href="http://www.ietf.org/rfc/rfc4122.txt">(IETF RFC 4122)</a>, specifically
 * the name-based (Type 3) variant.
 *
 * Our algorithm uses a domain URL as the namespace, and a combination of the
 * system time (in milliseconds), a sequence number, and random input from
 * SecureRandom for the name. The namespace and the name are concatenated and
 * fed into the SHA-1 hashing function.
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
public class ServerIDGenerator extends IDGenerator
{
  /**
   * Instantiates the singleton
   */
  protected synchronized static void setUp()
  {
    IDGenerator.setUp();

    space = CommonProperties.getDomain();

    // The user MUST change this property value
    if (space.equalsIgnoreCase("www.Change_me_to_your_unique_domain.com"))
    {
      String error = "[domain] property  in server.properties not been set.";
      throw new ConfigurationException(error);
    }
  }

  /**
   * Generates a unique database identifier.
   *
   * @return unique database identifier.
   */
  public static String generateUniqueDatabaseIdentifier()
  {
    String autoGenId = ServerIDGenerator.nextID();

    int maxBaseHashLength = Database.MAX_DB_IDENTIFIER_SIZE - 1;

    if (autoGenId.length() < maxBaseHashLength)
    {
      maxBaseHashLength = autoGenId.length();
    }

    autoGenId = autoGenId.substring(0, maxBaseHashLength);

    autoGenId = "a" + autoGenId;

    return autoGenId;
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
  public synchronized static String hashedId(String input)
  {
    return "i"+IDGenerator.hash(input);
  }
}
