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
import java.util.Locale;

/**
 * Pairing which stores a {@link Locale} and its original index in an array.
 * 
 * @author Justin Smethie
 */
public class IndexedLocale
{
  private final static int TOTAL_MATCH = 10;

  /**
   * Locale
   */
  private Locale locale;

  /**
   * Index
   */
  private int    index;

  public IndexedLocale(Locale locale, int index)
  {
    this.locale = locale;
    this.index = index;
  }
  
  @Override
  public String toString()
  {
    return this.locale.toString() + " - " + this.index;
  }  

  public Locale getLocale()
  {
    return locale;
  }

  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  public Integer getIndex()
  {
    return new Integer(index);
  }

  public void setIndex(int index)
  {
    this.index = index;
  }

  /**
   * @param supportedLocales
   *          Set of supported locales
   * 
   * @return Maximum number of matches this locale has with any of the supported
   *         locales.
   */
  public Integer getScore(Collection<Locale> supportedLocales)
  {
    int matches = 0;

    for (Locale supportedLocale : supportedLocales)
    {
      Integer current = this.getScore(supportedLocale);
      
      matches = Math.max(matches, current);
    }

    return new Integer(matches);
  }

  /**
   * @param supportedLocales
   *          Set of supported locales
   * 
   * @return Maximum number of matches this locale has with any of the supported
   *         locales.
   */
  public Integer getScore(Locale supportedLocale)
  {
    int maximum = this.getMaxScore(supportedLocale);
    
    int matches = this.match(supportedLocale.getLanguage(), locale.getLanguage());    
    matches += this.match(supportedLocale.getCountry(), locale.getCountry());    
    matches += this.match(supportedLocale.getVariant(), locale.getVariant());
    
    if(matches == maximum)
    {
      return new Integer(TOTAL_MATCH);
    }
    
    return new Integer(matches);
  }
  
  public Integer getMaxScore(Locale supportedLocale)
  {
    int max = 0;
    
    String language = this.locale.getLanguage();
    String country = this.locale.getCountry();
    String variant = this.locale.getVariant();
    
    max = (language != null && !language.equals("")) ? max + 1 : max;
    max = (country != null && !country.equals("")) ? max + 1 : max;
    max = (variant != null && !variant.equals("")) ? max + 1 : max;
    
    return max;
  }

  private int match(String v0, String v1)
  {
    if (v0 != null && v1 != null && !v0.equals(""))
    {
      return ( v0.equalsIgnoreCase(v1) ? 1 : 0 );
    }

    return 0;
  }

}
