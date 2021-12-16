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
package com.runwaysdk.dataaccess.metadata;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeLocalEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.SupportedLocaleInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.SupportedLocaleDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.transport.conversion.ConversionFacade;

public class SupportedLocaleDAO extends EnumerationItemDAO implements SupportedLocaleDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -7400328204766563017L;

  public SupportedLocaleDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  public static SupportedLocaleDAO newInstance()
  {
    return (SupportedLocaleDAO) BusinessDAO.newInstance(SupportedLocaleInfo.CLASS);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static SupportedLocaleDAOIF get(String oid)
  {
    return (SupportedLocaleDAOIF) BusinessDAO.get(oid);
  }

  @Override
  public String save(boolean businessContext)
  {
    if (this.isNew() && !this.isAppliedToDB())
    {
      String enumName = this.getName();
      Locale locale = ConversionFacade.getLocale(enumName);

      this.addColumnToMdAttributeLocal(locale);

      this.addColumnToMdAttributeLocalEmbedded(locale);
    }

    return super.save(businessContext);
  }

  protected void addColumnToMdAttributeLocal(Locale locale)
  {
    List<String> ids = getMdAttributeLocalIds();
    List<MdDimensionDAOIF> dimensionList = MdDimensionDAO.getAllMdDimensions();

    for (String oid : ids)
    {
      MdAttributeLocalDAO mdAttributeLocal = MdAttributeLocalDAO.get(oid).getBusinessDAO();
      mdAttributeLocal.addLocale(locale);

      for (MdDimensionDAOIF mdDimensionDAOIF : dimensionList)
      {
        mdAttributeLocal.addLocale(mdDimensionDAOIF.getBusinessDAO(), locale);
      }
    }
  }

  protected void addColumnToMdAttributeLocalEmbedded(Locale locale)
  {
    List<String> ids = getMdAttributeLocalEmbeddedIds();

    for (String oid : ids)
    {
      MdAttributeLocalEmbeddedDAO mdAttributeLocal = (MdAttributeLocalEmbeddedDAO) MdAttributeLocalEmbeddedDAO.get(oid).getBusinessDAO();
      mdAttributeLocal.addLocale(locale);
    }
  }

  public static List<String> getMdAttributeLocalIds()
  {
    return EntityDAO.getEntityIdsDB(MdAttributeLocalInfo.CLASS);
  }

  public static List<String> getMdAttributeLocalEmbeddedIds()
  {
    return EntityDAO.getEntityIdsDB(MdAttributeLocalEmbeddedInfo.CLASS);
  }

  @Override
  public void delete(boolean businessContext, DeleteContext context)
  {
    String enumName = this.getAttribute(EnumerationMasterInfo.NAME).getValue();

    List<String> ids = getMdAttributeLocalIds();

    Set<String> deletedObjectIds = new HashSet<String>();

    for (String oid : ids)
    {
      MdAttributeLocalDAOIF mdAttributeLocalDAOIF = MdAttributeLocalDAO.get(oid);

      MdStructDAOIF mdStructDAOIF = mdAttributeLocalDAOIF.getMdStructDAOIF();

      MdAttributeDAOIF localeMdAttributeDAOIF = mdStructDAOIF.definesAttribute(enumName);
      // Localized attributes can used the same struct. The attribute may have
      // been deleted.
      if (localeMdAttributeDAOIF != null && !deletedObjectIds.contains(localeMdAttributeDAOIF.getOid()))
      {
        MdAttributeDAO localeMdAttributeDAO = (MdAttributeDAO) localeMdAttributeDAOIF.getBusinessDAO();
        localeMdAttributeDAO.delete(context);
        deletedObjectIds.add(localeMdAttributeDAO.getOid());
      }

      // Delete all dimension locale attributes
      List<MdDimensionDAOIF> dimensionList = MdDimensionDAO.getAllMdDimensions();
      for (MdDimensionDAOIF mdDimensionDAOIF : dimensionList)
      {
        String dimensionLocaleName = mdDimensionDAOIF.getLocaleAttributeName(enumName);
        MdAttributeDAOIF dimensionLocaleMdAttributeDAOIF = mdStructDAOIF.definesAttribute(dimensionLocaleName);
        // Localized attributes can used the same struct. The attribute may have
        // been deleted.
        if (dimensionLocaleMdAttributeDAOIF != null && !deletedObjectIds.contains(dimensionLocaleMdAttributeDAOIF.getOid()))
        {
          MdAttributeDAO dimensionLocaleMdAttributeDAO = (MdAttributeDAO) dimensionLocaleMdAttributeDAOIF.getBusinessDAO();
          dimensionLocaleMdAttributeDAO.delete(context);
          deletedObjectIds.add(dimensionLocaleMdAttributeDAO.getOid());
        }
      }
    }

    super.delete(context);
  }

  /**
   * Returns all locals supported.
   * 
   * @return all locals supported.
   */
  public static List<Locale> getSupportedLocales()
  {
    List<Locale> localeList = new LinkedList<Locale>();

    QueryFactory qf = new QueryFactory();
    BusinessDAOQuery q = qf.businessDAOQuery(SupportedLocaleInfo.CLASS);

    OIterator<BusinessDAOIF> i = q.getIterator();

    try
    {
      for (BusinessDAOIF businessDAOIF : i)
      {
        SupportedLocaleDAOIF supportedLocaleDAOIF = (SupportedLocaleDAOIF) businessDAOIF;
        String enumName = supportedLocaleDAOIF.getName();
        Locale locale = ConversionFacade.getLocale(enumName);
        localeList.add(locale);
      }
    }
    finally
    {
      i.close();
    }

    return localeList;
  }
}
