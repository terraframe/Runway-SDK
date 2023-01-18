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
package com.runwaysdk.dataaccess.metadata.graph;

import java.util.Collection;
import java.util.Locale;

import com.runwaysdk.constants.MdAttributeEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalEmbeddedInfo;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalEmbeddedDAO;
import com.runwaysdk.session.LocaleManager;
import com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDAO;

public class MdAttributeLocalEmbedded_G extends MdAttributeEmbedded_G
{

  /**
   * 
   */
  private static final long serialVersionUID = 5275079669003681591L;

  public MdAttributeLocalEmbedded_G(MdAttributeLocalEmbeddedDAO mdAttribute)
  {
    super(mdAttribute);
  }

  @Override
  protected MdAttributeLocalEmbeddedDAO getMdAttribute()
  {
    return (MdAttributeLocalEmbeddedDAO) super.getMdAttribute();
  }

  @Override
  protected void preSaveValidate()
  {
    super.preSaveValidate();

    Attribute embeddedAttribute = getMdAttribute().getAttribute(MdAttributeEmbeddedInfo.EMBEDDED_MD_CLASS);

    if (embeddedAttribute.getValue().trim().equals(""))
    {
      MdEmbeddedGraphClassDAO mdEmbeddedDAO = (MdEmbeddedGraphClassDAO) MdEmbeddedGraphClassDAO.getMdEmbeddedGraphClassDAO(MdAttributeLocalEmbeddedInfo.EMBEDDED_LOCAL_VALUE);

      embeddedAttribute.setValue(mdEmbeddedDAO.getOid());
    }
  }

  /**
   * Contains special logic for saving an attribute.
   */
  @Override
  public void save()
  {
    super.save();

    MdAttributeLocalEmbeddedDAO mdAttributeLocaDAO = this.getMdAttribute();

    // Do not add the attribute on import, as the local embedded will have that
    // attribute already defined.
    if (!mdAttributeLocaDAO.isImport())
    {
      MdAttributeLocalEmbeddedDAO mdAttributeDAO = this.getMdAttribute();

      // if (!mdAttributeDAO.definesDefaultLocale())
      // {
      // mdAttributeDAO.addDefaultLocale();
      // }

      // Add all of the locales defined in SupportedLocale
      Collection<Locale> supportedLocales = LocaleManager.getSupportedLocales();

      for (Locale locale : supportedLocales)
      {
        if (!mdAttributeDAO.definesLocale(locale))
        {
          mdAttributeDAO.addLocale(locale);
        }
      }
    }
  }

  /**
   * Clean up any unused <code>MdAttributeLocalEmbeddedDAO</code>.
   *
   */
  @Override
  public void postDelete()
  {
    super.postDelete();
  }
}
