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

public class SupportedLocaleComparator implements Comparator<Locale>
{
  private Collection<IndexedLocale> locales;

  public SupportedLocaleComparator(Collection<IndexedLocale> locales)
  {
    this.locales = locales;
  }

  public int compare(Locale o1, Locale o2)
  {
    Integer m1 = this.matches(o1);
    Integer m2 = this.matches(o2);

    // multiple the matches by -1 such that the IndexedLocale with the
    // highest number of matches is sorted earlier in the list
    return -1 *  m1.compareTo(m2);
  }
  
  private int matches(Locale o1)
  {
    int matches = 0;
    
    for(IndexedLocale locale : locales)
    {
      matches = Math.max(locale.getScore(o1), matches); 
    }
    
    return matches;
  }
}
