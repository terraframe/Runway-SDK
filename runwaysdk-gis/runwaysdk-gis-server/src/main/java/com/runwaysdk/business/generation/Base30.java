/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.business.generation;

public class Base30
{
  private static short  BASE_30_RADIX = 30;

  private static String BASE_30_ZERO  = "0";

  private static String SYMBOLS       = "0123456789BCDFGHJKLMNPQRSTVWXYZ";

  private static String MAX_VALUE     = "KBMTTCD1HD207";

  public static long fromBase30(char[] cs)
  {
    if (cs.length > MAX_VALUE.length())
    {
      throw new IllegalArgumentException("Value is larger than a Long");
    }

    return fromBase(cs, BASE_30_RADIX);
  }

  public static char[] toBase30CharArray(long i)
  {
    return toBase(i, BASE_30_RADIX, BASE_30_ZERO);
  }

  public static long fromBase30(String cs)
  {
    return fromBase30(cs.toCharArray());
  }

  public static String toBase30String(long i)
  {
    return new String(toBase30CharArray(i));
  }

  public static String toBase30String(long i, int minLength)
  {
    return toBase30String(i,minLength,BASE_30_ZERO);
  }

  public static String toBase30String(long i, int minLength, String padding)
  {
    String s = toBase30String(i);

    while(s.length() < minLength)
    {
      s = padding + s;
    }

    return s;
  }

  private static char[] toBase(long i, short radix, String nullCharacter)
  {
    long value = i;

    if (value < 0)
    {
      throw new IllegalArgumentException("Negative Numbers Not Allowed");
    }

    if (value == 0)
    {
      return nullCharacter.toCharArray();
    }

    String result = "";
    while (value > 0)
    {
      long mod = value % radix;
      value -= mod;
      if (value > 0)
        value /= radix;
      result = SYMBOLS.charAt((int) mod) + result;
    }

    return result.toCharArray();
  }

  private static long fromBase(char[] cs, short radix)
  {
    long value = 0;
    for (int i = cs.length - 1; i >= 0; i--)
    {
      if (!SYMBOLS.contains(new String(cs, i, 1)))
      {
        throw new IllegalArgumentException("Invalid Base" + radix + " character: " + cs[i]);
      }
      int digit = ( SYMBOLS.indexOf(cs[i]) );

      long digitBase = (long) Math.pow(radix, ( cs.length - 1 ) - i);

      value = ( digit * digitBase ) + value;

      if (value < 0)
      {
        throw new IllegalArgumentException("Value is larger than a Long, Overflow");
      }
    }

    return value;
  }

}
