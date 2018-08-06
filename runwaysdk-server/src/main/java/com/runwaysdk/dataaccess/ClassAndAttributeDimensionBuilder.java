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
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Locale;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.SupportedLocaleDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.session.Request;

public class ClassAndAttributeDimensionBuilder
{
  private String  siteMaster;

  private boolean logTransactions;

  private boolean originalLogTransactions;

  public ClassAndAttributeDimensionBuilder()
  {
    this(CommonProperties.getDomain(), false);
  }

  public ClassAndAttributeDimensionBuilder(String siteMaster, boolean logTransactions)
  {
    this.siteMaster = siteMaster;
    this.logTransactions = logTransactions;
    this.originalLogTransactions = ServerProperties.logTransactions();
  }

  @Transaction
  public void build()
  {
    List<MdDimensionDAOIF> mdDimensions = MdDimensionDAO.getAllMdDimensions();

    if (mdDimensions.size() > 0)
    {
      ServerProperties.setLogTransactions(logTransactions);

      try
      {
        this.buildMdClassDimension(mdDimensions);
        this.buildMdAttributeDimension(mdDimensions);
        this.buildDimensionLocaleAttributes(mdDimensions);
      }
      finally
      {
        ServerProperties.setLogTransactions(originalLogTransactions);
      }
    }
  }

  private void buildMdClassDimension(List<MdDimensionDAOIF> mdDimensions)
  {
    OIterator<BusinessDAOIF> it = this.getMdClasses();

    try
    {
      while (it.hasNext())
      {
        MdClassDAOIF mdClass = (MdClassDAOIF) it.next();

        for (MdDimensionDAOIF mdDimension : mdDimensions)
        {
          MdClassDimensionDAO mdClassDimensionDAO = MdClassDimensionDAO.newInstance();
          mdClassDimensionDAO.setDefiningMdDimension(mdDimension);
          mdClassDimensionDAO.setDefiningMdClass(mdClass);
          mdClassDimensionDAO.getAttribute(ElementInfo.SITE_MASTER).setValue(siteMaster);
          mdClassDimensionDAO.getAttribute(ElementInfo.SEQUENCE).setValue("0");
          mdClassDimensionDAO.apply();
        }
      }
    }
    finally
    {
      it.close();
    }
  }

  public OIterator<BusinessDAOIF> getMdClasses()
  {
    QueryFactory factory = new QueryFactory();

    RelationshipDAOQuery classHasDimensionQuery = factory.relationshipDAOQuery(RelationshipTypes.CLASS_HAS_DIMENSION.getType());
    BusinessDAOQuery query = factory.businessDAOQuery(MdClassInfo.CLASS);
    query.WHERE(query.get(MdClassInfo.OID).SUBSELECT_NOT_IN(classHasDimensionQuery.parentId()));

    return query.getIterator();
  }

  private void buildMdAttributeDimension(List<MdDimensionDAOIF> mdDimensions)
  {
    OIterator<BusinessDAOIF> it = this.getMdAttributes();

    try
    {
      while (it.hasNext())
      {
        MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) it.next();

        for (MdDimensionDAOIF mdDimension : mdDimensions)
        {
          MdAttributeDimensionDAO mdAttributeDimensionDAO = MdAttributeDimensionDAO.newInstance();
          mdAttributeDimensionDAO.setDefiningMdDimension(mdDimension);
          mdAttributeDimensionDAO.setDefiningMdAttribute(mdAttribute);
          mdAttributeDimensionDAO.getAttribute(ElementInfo.SITE_MASTER).setValue(siteMaster);
          mdAttributeDimensionDAO.getAttribute(ElementInfo.SEQUENCE).setValue("0");
          mdAttributeDimensionDAO.apply();
        }
      }
    }
    finally
    {
      it.close();
    }
  }

  /**
   * @param mdDimensions
   */
  private void buildDimensionLocaleAttributes(List<MdDimensionDAOIF> mdDimensions)
  {
    OIterator<BusinessDAOIF> it = this.getAllMdAttributeLocals();

    List<Locale> locales = SupportedLocaleDAO.getSupportedLocales();

    try
    {
      while (it.hasNext())
      {
        MdAttributeLocalDAO mdAttributeLocal = ( (MdAttributeLocalDAOIF) it.next() ).getBusinessDAO();

        for (MdDimensionDAOIF mdDimension : mdDimensions)
        {
          if (!mdAttributeLocal.definesDefaultLocale(mdDimension))
          {
            mdAttributeLocal.addDefaultLocale(mdDimension.getBusinessDAO());
          }
        }

        for (Locale locale : locales)
        {
          if (!mdAttributeLocal.definesLocale(locale))
          {
            mdAttributeLocal.addLocale(locale);
          }

          for (MdDimensionDAOIF mdDimension : mdDimensions)
          {
            if (!mdAttributeLocal.definesLocale(mdDimension, locale))
            {
              mdAttributeLocal.addLocale(mdDimension.getBusinessDAO(), locale);
            }
          }
        }
      }
    }
    finally
    {
      it.close();
    }
  }

  /**
   * @return
   */
  private OIterator<BusinessDAOIF> getAllMdAttributeLocals()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(MdAttributeLocalInfo.CLASS);
    OIterator<BusinessDAOIF> it = query.getIterator();

    return it;
  }

  public OIterator<BusinessDAOIF> getMdAttributes()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery mdAttrDimQuery = factory.businessDAOQuery(MdAttributeDimensionInfo.CLASS);
    
    BusinessDAOQuery query = factory.businessDAOQuery(MdAttributeInfo.CLASS);
    query.WHERE(query.get(MdAttributeInfo.OID).SUBSELECT_NOT_IN(mdAttrDimQuery.get(MdAttributeDimensionInfo.DEFINING_MD_ATTRIBUTE)));
    
    return query.getIterator();
  }

  public static void main(String[] args)
  {
    try
    {
      ClassAndAttributeDimensionBuilder.start(args);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  @Request
  private static void start(String[] args)
  {
    if (args.length > 0)
    {
      new ClassAndAttributeDimensionBuilder(args[0], false).build();
    }
    else
    {
      new ClassAndAttributeDimensionBuilder().build();
    }
    
    ObjectCache.refreshCache();
  }
}
