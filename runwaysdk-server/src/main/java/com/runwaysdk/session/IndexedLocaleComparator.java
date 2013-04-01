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
package com.runwaysdk.session;

import java.util.Collection;
import java.util.Comparator;
import java.util.Locale;

/**
 * Comparator which sorts the {@link Locale} with the highest number of matches
 * on language, country, and variant with any of the {@link Locale}s supported
 * by the system is the best fit. In the case of a tie the {@link Locale} with a
 * lower index is choosen. This is due to the assumption that the primary
 * {@link Locale} is first in the array. 
 * 
 *  @author Justin Smethie
 */
public class IndexedLocaleComparator implements Comparator<IndexedLocale>
{
  private Collection<Locale> supportedLocales;

  public IndexedLocaleComparator(Collection<Locale> supportedLocales)
  {
    this.supportedLocales = supportedLocales;
  }

  public int compare(IndexedLocale o1, IndexedLocale o2)
  {
    Integer m1 = o1.getScore(this.supportedLocales);
    Integer m2 = o2.getScore(this.supportedLocales);

    if (m1.equals(m2))
    {
      return o1.getIndex().compareTo(o2.getIndex());
    }

    // multiple the matches by -1 such that the IndexedLocale with the
    // highest number of matches is sorted earlier in the list
    return -1 *  m1.compareTo(m2);
  }
}
