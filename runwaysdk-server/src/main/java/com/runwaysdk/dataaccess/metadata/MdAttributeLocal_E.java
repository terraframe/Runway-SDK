/**
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
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocalCharacter;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.session.LocaleManager;

public class MdAttributeLocal_E extends MdAttributeStruct_E
{
  private static final long serialVersionUID = -3492447157803101736L;

  public MdAttributeLocal_E(MdAttributeLocalDAO mdAttribute)
  {
    super(mdAttribute);
  }

  @Override
  protected MdAttributeLocalDAO getMdAttribute()
  {
    return (MdAttributeLocalDAO) super.getMdAttribute();
  }

  @Override
  protected void preSaveValidate()
  {
    super.preSaveValidate();

    Attribute structAttribute = getMdAttribute().getAttribute(MdAttributeStructInfo.MD_STRUCT);
    if (structAttribute.getValue().trim().equals(""))
    {
      // Create the MdStruct that holds the localized values
      MdLocalStructDAO mdLocalStructDAO = MdLocalStructDAO.newInstance();
      mdLocalStructDAO.getAttribute(MdLocalStructInfo.NAME).setValue(definedByClass().getTypeName() + CommonGenerationUtil.upperFirstCharacter(getMdAttribute().definesAttribute()));
      mdLocalStructDAO.getAttribute(MdLocalStructInfo.PACKAGE).setValue(definedByClass().getPackage());

      ( (AttributeLocalCharacter) mdLocalStructDAO.getAttribute(MdLocalStructInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, getMdAttribute().getDisplayLabel(CommonProperties.getDefaultLocale()));

      ( (AttributeLocalCharacter) mdLocalStructDAO.getAttribute(MdLocalStructInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, getMdAttribute().getDescription(CommonProperties.getDefaultLocale()));

      mdLocalStructDAO.getAttribute(MdLocalStructInfo.CACHE_SIZE).setValue("0");
      mdLocalStructDAO.addItem(MdLocalStructInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      mdLocalStructDAO.apply();

      structAttribute.setValue(mdLocalStructDAO.getId());
    }
  }

  /**
   * Contains special logic for saving an attribute.
   */
  @Override
  public void save()
  {
    super.save();

    MdAttributeLocalDAO mdAttributeLocaDAO = this.getMdAttribute();

    // Do not add the attribute on import, as the local struct will have that
    // attribute already defined.
    if (!mdAttributeLocaDAO.isImport())
    {
      MdAttributeLocalDAO mdLocalStructDAO = this.getMdAttribute();

      List<MdDimensionDAOIF> dimensions = MdDimensionDAO.getAllMdDimensions();

      if (!mdLocalStructDAO.definesDefaultLocale())
      {
        mdLocalStructDAO.addDefaultLocale();
      }

      for (MdDimensionDAOIF dimension : dimensions)
      {
        if(!mdLocalStructDAO.definesDefaultLocale(dimension))
        {
          mdLocalStructDAO.addDefaultLocale(dimension.getBusinessDAO());
        }
      }

      // Add all of the locales defined in SupportedLocale
      Collection<Locale> supportedLocales = LocaleManager.getSupportedLocales();

      for (Locale locale : supportedLocales)
      {
        if(!mdLocalStructDAO.definesLocale(locale))
        {
          mdLocalStructDAO.addLocale(locale);
        }

        for (MdDimensionDAOIF dimension : dimensions)
        {
          if(!mdLocalStructDAO.definesLocale(dimension, locale))
          {
            mdLocalStructDAO.addLocale(dimension.getBusinessDAO(), locale);
          }
        }
      }
    }
  }

  /**
   * Clean up any unused <code>MdAttributeLocalDAO</code>.
   *
   */
  @Override
  public void postDelete()
  {
    super.postDelete();

    this.getMdAttribute().getMdStructDAOIF().getBusinessDAO().deleteIfNotReferenced();
  }
}
