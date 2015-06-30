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
package com.runwaysdk.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.session.Session;

public class AttributeLocal extends AttributeStruct implements AttributeLocalIF
{
  protected AttributeLocal(MdAttributeStructDAOIF mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias,
      MdLocalStructDAOIF mdStructIF, String structTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias,
        mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  public MdLocalStructDAOIF getMdStructDAOIF()
  {
    return (MdLocalStructDAOIF)super.getMdStructDAOIF();
  }

  /**
   * Returns a query Attribute object with the value from the current locale.
   *
   * @return query Attribute object with the value from the current locale.
   */
  public AttributePrimitive getSessionLocale()
  {
    return this.getSessionLocale(null, null);
  }

  /**
   * Returns a query Attribute object with the value from the current locale.
   *
   * @param attributeAlias user defined alias.
   *
   * @return query Attribute object with the value from the current locale.
   */
  public AttributePrimitive getSessionLocale(String attributeAlias)
  {
    return this.getSessionLocale(attributeAlias, null);
  }

  /**
   * Builds a <code>Coalesce</code> object with the order of localization for the user's locale.
   * This effectively performs a localized query.
   *
   * @return <code>Coalesce</code> object with the order of localization for the user's locale.
   */
  public Coalesce localize()
  {
    return localize(null, null);
  }

  /**
   * Builds a <code>Coalesce</code> object with the order of localization for the user's locale.
   * This effectively performs a localized query.
   *
   * @return <code>Coalesce</code> object with the order of localization for the user's locale.
   */
  public Coalesce localize(String attributeAlias)
  {
    return localize(attributeAlias, null);
  }

  /**
   * Returns a query Attribute object with the closest match with the current locale.
   *
   * @param attributeAlias user defined alias.
   * @param displayLabel user defined display label
   *
   * @return query Attribute object with the closest match with the current locale.
   */
  public AttributePrimitive getSessionLocale(String attributeAlias, String displayLabel)
  {
    MdLocalStructDAOIF mdLocalStruct = getMdStructDAOIF();
    Locale locale = Session.getCurrentLocale();

    String[] localeStringArray;
    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();
    if (mdDimensionDAOIF != null)
    {
      localeStringArray = new String[2];
      localeStringArray[0] = mdDimensionDAOIF.getLocaleAttributeName(locale);
      localeStringArray[1] = locale.toString();
    }
    else
    {
      localeStringArray = new String[1];
      localeStringArray[0] = locale.toString();
    }

    boolean firstIterationComplete = false;
    for (String localeString : localeStringArray)
    {
      for (int i=localeString.length(); i>0; i = localeString.lastIndexOf('_', i-1))
      {
        String subLocale = localeString.substring(0, i);
        if (mdLocalStruct.definesAttribute(subLocale)!=null)
        {
          return (AttributePrimitive)this.get(subLocale, attributeAlias, displayLabel);
        }
      }

      // Check the default for the dimension
      if (mdDimensionDAOIF != null && !firstIterationComplete)
      {
        String dimensionDefaultAttr = mdDimensionDAOIF.getDefaultLocaleAttributeName();
        MdAttributeDAOIF definesDimensionDefault = mdLocalStruct.definesAttribute(dimensionDefaultAttr);
        if (definesDimensionDefault != null)
        {
          return (AttributePrimitive)this.get(dimensionDefaultAttr, attributeAlias, displayLabel);
        }
      }
      firstIterationComplete = true;
    }

    return (AttributePrimitive)this.get(MdAttributeLocalInfo.DEFAULT_LOCALE, attributeAlias, displayLabel);
  }

  /**
   * Builds a <code>Coalesce</code> object with the order of localization for the user's locale.
   * This effectively performs a localized query.
   *
   * @return <code>Coalesce</code> object with the order of localization for the user's locale.
   */
  public Coalesce localize(String attributeAlias, String displayLabel)
  {
    List<SelectableSingle> selectableList = new ArrayList<SelectableSingle>();

    MdLocalStructDAOIF mdLocalStruct = getMdStructDAOIF();
    Locale locale = Session.getCurrentLocale();

    String[] localeStringArray;
    MdDimensionDAOIF mdDimensionDAOIF = Session.getCurrentDimension();
    if (mdDimensionDAOIF != null)
    {
      localeStringArray = new String[2];
      localeStringArray[0] = mdDimensionDAOIF.getLocaleAttributeName(locale);
      localeStringArray[1] = locale.toString();
    }
    else
    {
      localeStringArray = new String[1];
      localeStringArray[0] = locale.toString();
    }

    boolean firstIterationComplete = false;
    for (String localeString : localeStringArray)
    {
      for (int i=localeString.length(); i>0; i = localeString.lastIndexOf('_', i-1))
      {
        String subLocale = localeString.substring(0, i);
        for (MdAttributeConcreteDAOIF a : mdLocalStruct.definesAttributes())
        {
          if (a.definesAttribute().equalsIgnoreCase(subLocale))
          {
            selectableList.add(this.get(subLocale));
          }
        }
      }

      // Check the default for the dimension
      if (mdDimensionDAOIF != null && !firstIterationComplete)
      {
        String dimensionDefaultAttr = mdDimensionDAOIF.getDefaultLocaleAttributeName();
        MdAttributeDAOIF definesDimensionDefault = mdLocalStruct.definesAttribute(dimensionDefaultAttr);
        if (definesDimensionDefault != null)
        {
          selectableList.add(this.get(dimensionDefaultAttr));
        }
      }

      firstIterationComplete = true;
    }
    // And finally, add the default at the very end
    selectableList.add(this.get(MdAttributeLocalInfo.DEFAULT_LOCALE));

    SelectableSingle firstSelectable = selectableList.remove(0);
    SelectableSingle[] optionalSelectableArray = new SelectableSingle[selectableList.size()];

    return F.COALESCE(attributeAlias, displayLabel, firstSelectable, selectableList.toArray(optionalSelectableArray));
  }

}
