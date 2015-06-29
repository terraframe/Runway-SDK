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
package com.runwaysdk.dataaccess.transaction;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ImportLogDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;

public class ImportLogDAO extends BusinessDAO implements ImportLogDAOIF, SpecializedDAOImplementationIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -2071466071757782021L;

  /**
   * The default constructor, does not set any attributes
   */
  public ImportLogDAO()
  {
    super();
  }

  /**
   * Constructs a <code>ImportLogDAO</code> from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public ImportLogDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public ImportLogDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new ImportLogDAO(attributeMap, ImportLogInfo.CLASS);
  }

  /**
   * Returns a new <code>ImportLogDAO</code>. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of <code>ImportLogDAO</code>.
   */
  public static ImportLogDAO newInstance()
  {
    return (ImportLogDAO) BusinessDAO.newInstance(ImportLogInfo.CLASS);
  }

  @Override
  public String apply()
  {
    this.setKey(this.getSourceSite());

    return super.apply();
  }

  /**
   * 
   */
  @Override
  public String save(boolean businessContext)
  {
    return super.save(businessContext);
  }

  /**
   * Source site.
   */
  public String getSourceSite()
  {
    return this.getAttributeIF(ImportLogInfo.SOURCE_SITE).getValue();
  }

  /**
   * Source site.
   */
  public void setSourceSite(String sourceSite)
  {
    this.getAttribute(ImportLogInfo.SOURCE_SITE).setValue(sourceSite);
  }

  /**
   * Export sequence.
   */
  public Long getLastExportSequence()
  {
    try
    {
      return Long.parseLong(this.getAttributeIF(ImportLogInfo.LAST_EXPORT_SEQUENCE).getValue());
    }
    catch (NumberFormatException e)
    {
      return 0L;
    }
  }

  /**
   * Export sequence.
   */
  public void setLastExportSequence(Long exportSequence)
  {
    this.getAttribute(ImportLogInfo.LAST_EXPORT_SEQUENCE).setValue(Long.toString(exportSequence));
  }

  /**
   * Returns the last imported sequence from the given site, but if the site
   * does not exist, then create it with a starting sequence of 0. This method
   * both queries and sometimes also creates!
   * 
   * @param importSite
   * @return last imported sequence from the given site.
   */
  public static Long getLastExportSeqFromSite(String importSite)
  {
    Long lastExportSeq = 0L;

    try
    {
      ImportLogDAOIF importLogDAOIF = (ImportLogDAOIF) ImportLogDAO.get(ImportLogInfo.CLASS, importSite);
      lastExportSeq = importLogDAOIF.getLastExportSequence();
    }
    catch (DataNotFoundException e)
    {
      ImportLogDAO importLogDAO = ImportLogDAO.newInstance();
      importLogDAO.setSourceSite(importSite);
      importLogDAO.setLastExportSequence(lastExportSeq);
      importLogDAO.apply();
    }

    return lastExportSeq;
  }

  /**
   * 
   * @param importSite
   * @return
   */
  public static void setLastExportSeqFromSite(String importSite, Long lastExportSeq)
  {
    ImportLogDAO importLogDAO;

    try
    {
      importLogDAO = ( (ImportLogDAOIF) ImportLogDAO.get(ImportLogInfo.CLASS, importSite) ).getBusinessDAO();
    }
    catch (DataNotFoundException e)
    {
      importLogDAO = ImportLogDAO.newInstance();
      importLogDAO.setSourceSite(importSite);
    }

    importLogDAO.setLastExportSequence(lastExportSeq);
    importLogDAO.apply();
  }

  /**
   * @see com.runwaysdk.dataaccess.TransactionItemDAO#getBusinessDAO()
   */
  public ImportLogDAO getBusinessDAO()
  {
    return (ImportLogDAO) super.getBusinessDAO();
  }
}
