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
package com.runwaysdk.business;

import java.util.List;
import java.util.Locale;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;

public class LocalStruct extends Struct
{
  private static final long serialVersionUID = -5905286574707516282L;

  public LocalStruct()
  {
    super();
  }

  protected LocalStruct(MutableWithStructs parent, String structName)
  {
    super(parent, structName);
  }

  /**
   * Primary entry point for other constructors to delegate to. structDAO can be an
   * existing or new instance, so this constructor is used for both cases. Can also be
   * used to create type unsafe Business representations of existing data.
   *
   * @param structDAO
   */
  LocalStruct(StructDAO structDAO)
  {
    super(structDAO);
  }

  @Override
  public MdLocalStructDAOIF getMdClass()
  {
    return (MdLocalStructDAOIF)super.getMdClass();
  }

  /**
   * Copies the localized values from the given localized struct
   * to this one.
   * @param localStruct
   */
  public void copy(LocalStruct localStruct)
  {
    List<? extends MdAttributeConcreteDAOIF> mdAttributes = localStruct.getMdAttributeDAOs();

    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      if (!mdAttribute.isSystem())
      {
        String attributeName = mdAttribute.definesAttribute();

        // type unsafe copy
        if (this.getClass().equals(LocalStruct.class) ||
            localStruct.getClass().equals(LocalStruct.class))
        {
          this.setValue(attributeName, localStruct.getValue(attributeName));
        }
        else
        {
          Class<?> thisClass = this.getClass();
          Class<?> otherClass = localStruct.getClass();

          String accessorName = CommonGenerationUtil.upperFirstCharacter(attributeName);
          Class<?> javaClass = LoaderDecorator.load(mdAttribute.javaType(false));

          Object object;
          try
          {
            object = otherClass.getMethod(CommonGenerationUtil.GET+accessorName).invoke(localStruct);
            thisClass.getMethod(CommonGenerationUtil.SET+accessorName, javaClass).invoke(this, object);
          }
          catch (Exception e)
          {
            String errMsg = "Error trying to copy attributes from one "+LocalStruct.class.getName()+" to another";
            throw new ProgrammingErrorException(errMsg, e);
          }
        }
      }

    }
  }

  public String getDefaultValue()
  {
    return this.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }

  public String getValue()
  {
    return getValue(Session.getCurrentLocale());
  }

  public void setDefaultValue(String value)
  {
    this.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value);
  }


  public void setValue(Locale locale, String value)
  {
    this.setValue(locale.toString(), value);
  }

  public void setValue(String value)
  {
    boolean setFirstValue = false;

    Locale locale = Session.getCurrentLocale();

    MdLocalStructDAOIF mdLocalStruct = getMdClass();

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
          // Set at least one value, even if it is not the exact locale
          if (setFirstValue == false)
          {
            this.setValue(subLocale, value);
            setFirstValue = true;
          }
          else if (getValue(subLocale).trim().equals(""))
          {
            this.setValue(subLocale, value);
          }
        }
      }

      // Check the default for the dimension
      if (mdDimensionDAOIF != null && !firstIterationComplete)
      {
        String dimensionDefaultAttr = mdDimensionDAOIF.getDefaultLocaleAttributeName();
        MdAttributeDAOIF definesDimensionDefault = mdLocalStruct.definesAttribute(dimensionDefaultAttr);
        if (definesDimensionDefault != null &&
            (getValue(dimensionDefaultAttr).trim().equals("") || !setFirstValue))
        {
          this.setValue(dimensionDefaultAttr, value);
          setFirstValue = true;
        }
      }

      firstIterationComplete = true;
    }


    String defaultLocale = this.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (defaultLocale.trim().equals("") || !setFirstValue)
    {
      this.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value);
    }

  }

  /**
   * Returns the localized string that is the best fit for the given locale.
   *
   * Duplicated in <code>AttributeLocal</code>
   */
  public String getValue(Locale locale)
  {
    MdLocalStructDAOIF mdLocalStruct = this.getMdClass();

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
      for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
      {
        String subLocale = localeString.substring(0, i);

        MdAttributeDAOIF definesSublocale = mdLocalStruct.definesAttribute(subLocale);

        if (definesSublocale != null)
        {
          try
          {
            String subLocaleValue = this.getValue(subLocale);

            if (!subLocaleValue.trim().equals(""))
            {
              return subLocaleValue;
            }
          }
          catch (AttributeDoesNotExistException e)
          {
            // METADATA: Inconsistency
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
          String dimensionDefaultValue = this.getValue(dimensionDefaultAttr);
          if (!dimensionDefaultValue.trim().equals(""))
          {
            return dimensionDefaultValue;
          }
        }
      }

      firstIterationComplete = true;
    }

    // No matching locale found. Resort to the default
    return this.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }

  public String toString()
  {
    return getValue();
  }
}
