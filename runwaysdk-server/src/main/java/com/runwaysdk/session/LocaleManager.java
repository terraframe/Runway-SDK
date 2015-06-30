/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.session;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.SupportedLocaleInfo;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.transport.conversion.ConversionFacade;

/**
 * Class used to determine which {@link Locale} of many is best supported by the
 * system. By default the {@link Locale} with the highest number of matches on
 * language, country, and variant with any of the {@link Locale}s supported by
 * the system is the best fit. In the case of a tie the {@link Locale} with a
 * lower index is choosen. This is due to the assumption that the primary
 * {@link Locale} is first in the array.
 * 
 * @author Justin Smethie
 */
public class LocaleManager
{
  /**
   * List of possible locales to choose
   */
  private LinkedList<IndexedLocale> locales;

  /**
   * Set of locales which are supported by the system
   */
  private LinkedList<Locale>        supportedLocales;

  public LocaleManager(Locale[] locales)
  {
    this(locales, LocaleManager.getSupportedLocales());
  }

  public LocaleManager(Locale[] locales, Collection<Locale> supportedLocales)
  {
    this.supportedLocales = new LinkedList<Locale>(supportedLocales);
    this.locales = new LinkedList<IndexedLocale>();

    for (int i = 0; i < locales.length; i++)
    {
      this.locales.add(new IndexedLocale(locales[i], i));
    }
  }

  /**
   * @return Best {@link Locale} according to the
   *         {@link IndexedLocaleComparator} algorithm
   */
  public Locale getBestFitLocale()
  {
    return this.getBestFitLocale(this.getIndexedComparator());
  }

  /**
   * @param supportedComparator
   *          TODO
   * @param indexedComparator
   *          {@link Comparator<IndexedLocale>} used to determine the best
   *          choice of the possible {@link Locale}s
   * @return Best {@link Locale} according to the given comparator algorithm
   */
  public Locale getBestFitLocale(Comparator<IndexedLocale> indexedComparator)
  {
    Collections.sort(this.locales, indexedComparator);

    return this.locales.getFirst().getLocale();
  }

  /**
   * @return Best {@link Locale} according to the
   *         {@link IndexedLocaleComparator} algorithm
   */
  public Locale getBestSupportedLocale()
  {
    return this.getBestSupportedLocale(this.getSupportedLocaleComparator());
  }

  public Locale getBestSupportedLocale(Comparator<Locale> supportedComparator)
  {
    if (this.supportedLocales.size() > 0)
    {
      Collections.sort(this.supportedLocales, supportedComparator);

      return this.supportedLocales.getFirst();
    }
    
    return this.locales.getFirst().getLocale();
  }

  private Comparator<Locale> getSupportedLocaleComparator()
  {
    return new SupportedLocaleComparator(this.locales);
  }

  /**
   * @return The default comparator.
   */
  private Comparator<IndexedLocale> getIndexedComparator()
  {
    return new IndexedLocaleComparator(this.supportedLocales);
  }

  /**
   * @return A {@link Set} of {@link Locale}s defined by the SupportLocale
   *         enumeration master.
   */
  public static Collection<Locale> getSupportedLocales()
  {
    List<Locale> list = new LinkedList<Locale>();

    List<? extends EntityDAOIF> supportedLocales = ObjectCache.getCachedEntityDAOs(SupportedLocaleInfo.CLASS);

    for (EntityDAOIF entity : supportedLocales)
    {
      if (entity.getType().equals(SupportedLocaleInfo.CLASS))
      {
        String localeString = entity.getValue(EnumerationMasterInfo.NAME);

        try
        {
          Locale locale = ConversionFacade.getLocale(localeString);

          list.add(locale);
        }
        catch (Exception e)
        {
          throw new InvalidLocaleFormatException(e, localeString);
        }
      }
    }

    return list;
  }
}
