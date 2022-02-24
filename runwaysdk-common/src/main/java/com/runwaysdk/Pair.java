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
package com.runwaysdk;

/**
 * Provides a generic Pair composite mechanism for collecting 2 similar things into one. If used in HashMaps or something similar make sure not to change
 * the hashcode of the first/second items otherwise the hash of the pair will change and the HashMap will produce undefined behavior.
 * 
 * Special thanks to StackOverflow:
 * http://stackoverflow.com/questions/156275/what-is-the-equivalent-of-the-c-pairl-r-in-java
 */
public class Pair<A, B>
{
  protected final A first;

  protected final B second;

  public Pair(A first, B second)
  {
    super();
    this.first = first;
    this.second = second;
  }

  public int hashCode()
  {
    int hashFirst = first != null ? first.hashCode() : 0;
    int hashSecond = second != null ? second.hashCode() : 0;

    return ( hashFirst + hashSecond ) * hashSecond + hashFirst;
  }

  public boolean equals(Object other)
  {
    if (other instanceof Pair)
    {
      Pair<?, ?> otherPair = (Pair<?, ?>) other;
      return ( ( this.first == otherPair.first || ( this.first != null && otherPair.first != null && this.first.equals(otherPair.first) ) ) && ( this.second == otherPair.second || ( this.second != null && otherPair.second != null && this.second.equals(otherPair.second) ) ) );
    }

    return false;
  }

  public static <A, B> Pair<A, B> of(A first, B second)
  {
    return new Pair<A, B>(first, second);
  }

  public String toString()
  {
    return "(" + first + ", " + second + ")";
  }

  public A getFirst()
  {
    return first;
  }

  public B getSecond()
  {
    return second;
  }
}
