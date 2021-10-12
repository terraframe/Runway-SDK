/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.localization.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeBooleanUtil;
import com.runwaysdk.constants.MdLocalStructInfo;
import com.runwaysdk.constants.StructInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.localization.LocaleDimension;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.localization.exception.LocalizedRowIgnoredWarning;
import com.runwaysdk.system.metadata.MdAttributeConcrete;
import com.runwaysdk.system.metadata.MdAttributeDimension;
import com.runwaysdk.system.metadata.MdAttributeLocal;
import com.runwaysdk.system.metadata.MdIndex;
import com.runwaysdk.system.metadata.MdMethod;
import com.runwaysdk.system.metadata.MdParameter;
import com.runwaysdk.system.metadata.Metadata;

public class AttributeLocalTabConfiguration extends TabConfiguration
{
  private Logger logger = LoggerFactory.getLogger(AttributeLocalTabConfiguration.class);
  
  protected List<AttributeLocalQueryCriteria> queryCriterias = new ArrayList<AttributeLocalQueryCriteria>();
  
  protected List<? extends MdAttributeLocal> attributeLocals;
  
  /**
   * A tab-wide definition for the attribute name of the AttributeLocal. Completely optional, but if its not provided then the sheet must contain the attribute name.
   */
  protected String tabWideAttributeLocalAttributeName = null;
  
  /**
   * A tab-wide definition for the entity type.
   */
  protected String tabWideEntityType = null;

  protected List<String> valueStoreTagName = new ArrayList<String>();
  
  protected List<LocaleDimension> dimensions;
  
  public AttributeLocalTabConfiguration(String sheetName, List<? extends MdAttributeLocal> attributeLocals, List<LocaleDimension> dimensions, List<ColumnConfiguration> columns)
  {
    super(sheetName, columns);
    
    AttributeLocalQueryCriteria defaultCriteria = new AttributeLocalQueryCriteria();
    defaultCriteria.definingTypeMustNotInclude("com.runwaysdk.system.metadata.MdAction");
    defaultCriteria.definingTypeMustNotInclude(MdParameter.CLASS);
    defaultCriteria.definingTypeMustNotInclude(MdIndex.CLASS);
    defaultCriteria.definingTypeMustNotInclude(MdMethod.CLASS);
    defaultCriteria.definingTypeMustNotInclude(MdAttributeDimension.CLASS);
    this.queryCriterias.add(defaultCriteria);
    
    this.attributeLocals = attributeLocals;
    
    this.dimensions = dimensions;
  }
  
  public void importData()
  {
    Iterator<Row> rowIterator = sheet.rowIterator();
    
    rowIterator.next(); // Skip headers
    
    while (rowIterator.hasNext())
    {
      Row row = rowIterator.next();
      readLocalAttributeRow(row);
    }
  }
  
  private void readLocalAttributeRow(Row row)
  {
    Cell keyCell = row.getCell(this.getColumnByAttribute(MdAttributeLocal.KEYNAME).getIndex());
    
    String key;
    if (keyCell == null)
    {
      LocalizedRowIgnoredWarning warning = new LocalizedRowIgnoredWarning();
      warning.setSheet(this.sheetName);
      warning.setRow(row.getRowNum() + 1);
      warning.throwIt();
      return;
    }
    else
    {
      key = ExcelUtil.getString(keyCell);
    }
    
    String type;
    if (tabWideEntityType != null)
    {
      type = tabWideEntityType;
    }
    else
    {
      ColumnConfiguration column = this.getColumnByAttribute(MdAttributeLocal.TYPE);
      
      if (column == null)
      {
        throw new ProgrammingErrorException("The configuration for this spreadsheet is invalid. If the spreadsheet does not contain the type for this entity then one must be provided for the tab configuration.");
      }
      
      type = ExcelUtil.getString(row.getCell(column.getIndex()));
    }
    
    String attributeName;
    if (tabWideAttributeLocalAttributeName != null)
    {
      attributeName = tabWideAttributeLocalAttributeName;
    }
    else
    {
      ColumnConfiguration column = this.getColumnByAttribute(MdAttributeLocal.ATTRIBUTENAME);
      
      if (column == null)
      {
        throw new ProgrammingErrorException("The configuration for this spreadsheet is invalid. If the spreadsheet does not contain the attribute name for the attribute local then one must be provided for the tab configuration.");
      }
      
      attributeName = ExcelUtil.getString(row.getCell(column.getIndex()));
    }
    
    StructDAO struct;
    EntityDAO entity;
    try
    {
      // Casting to DAO so we can update the cache for this object only, not a
      // complete rebuild
      entity = (EntityDAO) EntityDAO.get(type, key);
      
      // Casting to AttributeStruct so we can get the StructDAO reference
      AttributeStruct attributeStruct = (AttributeStruct) entity.getAttribute(attributeName);
      struct = attributeStruct.getStructDAO();
    }
    catch (DataNotFoundException e)
    {
      LocalizedRowIgnoredWarning warning = new LocalizedRowIgnoredWarning();
      warning.setSheet(this.sheetName);
      warning.setRow(row.getRowNum() + 1);
      warning.throwIt();

      return;
    }
    
    boolean apply = false;
    ColumnConfiguration columnConfig = this.getLocaleColumn();
    int index = columnConfig.getIndex();
    
    for (LocaleDimension ld : this.dimensions)
    {
      Cell cell = row.getCell(index);
      
      String value = new String();
      if (cell != null)
      {
        value = ExcelUtil.getString(row.getCell(index));
      }
      if (value == null)
      {
        value = new String();
      }
      
      String localeAttributeName = ld.getAttributeName();
      String oldValue = struct.getValue(localeAttributeName);
      
      // To speed things up, only set values that have changed
      if (!oldValue.equals(value))
      {
        struct.setValue(localeAttributeName, value);
        apply = true;
      }
      
      index++;
    }
    
    this.importProgressMonitor.onImportRecord();
    
    // To speed things up, only apply if we actually changed a value
    if (apply)
    {
      struct.apply();
      
      ObjectCache.updateCache(entity);
    }
  }
  
  public void exportData()
  {
    addAttributeLocals(this.sheet, this.attributeLocals);
  }
  
  private void addAttributeLocals(Sheet sheet, List<? extends MdAttributeLocal> all)
  {
    int r = 1;
    
    LocalLoop:
    for (MdAttributeLocal local : all)
    {
      MdTypeDAOIF mdType = MdTypeDAO.get(local.getValue(MdAttributeLocal.DEFININGMDCLASS));
      MdLocalStructDAOIF mdLocalStruct = (MdLocalStructDAOIF) BusinessFacade.getEntityDAO(local.getMdStruct());
      Boolean enforceSiteMaster = MdAttributeBooleanUtil.getTypeSafeValue(mdLocalStruct.getValue(MdLocalStructInfo.ENFORCE_SITE_MASTER));
      String definingType = mdType.definesType();
      String attributeName = local.getAttributeName();

      for (AttributeLocalQueryCriteria criteria : this.queryCriterias)
      {
        if (!criteria.shouldExportDefiningType(definingType))
        {
          continue LocalLoop;
        }
      }

      EntityLoop:
      for (String id : EntityDAO.getEntityIdsDB(definingType))
      {
        try
        {
          EntityDAOIF entity = EntityDAO.get(id);
          
          for (AttributeLocalQueryCriteria criteria : this.queryCriterias)
          {
            if (!criteria.shouldExportEntity(entity))
            {
              continue EntityLoop;
            }
          }
          
          StructDAOIF struct = StructDAO.get(entity.getValue(attributeName));
  
          // Don't export instances mastered at another site
          if (enforceSiteMaster && !struct.getValue(StructInfo.SITE_MASTER).equals(CommonProperties.getDomain()))
          {
            continue;
          }
  
          // Some attributes are re-created at the top of every hierarchy. Ignore
          // them.
          if (entity instanceof MdAttributeDAOIF)
          {
            MdAttributeDAOIF mdAttribute = (MdAttributeDAOIF) entity;
            String definedAttribute = mdAttribute.getValue(MdAttributeConcrete.ATTRIBUTENAME);
            if (definedAttribute.equalsIgnoreCase(Metadata.OID) || definedAttribute.equalsIgnoreCase(Metadata.CREATEDBY) || definedAttribute.equalsIgnoreCase(Metadata.ENTITYDOMAIN) || definedAttribute.equalsIgnoreCase(Metadata.KEYNAME) || definedAttribute.equalsIgnoreCase(Metadata.LASTUPDATEDATE) || definedAttribute.equalsIgnoreCase(Metadata.LASTUPDATEDBY) || definedAttribute.equalsIgnoreCase(Metadata.LOCKEDBY) || definedAttribute.equalsIgnoreCase(Metadata.OWNER) || definedAttribute.equalsIgnoreCase(Metadata.SEQ) || definedAttribute.equalsIgnoreCase(Metadata.TYPE))
            {
              continue;
            }
  
            // Selectively get rid of create dates
            if (definedAttribute.equalsIgnoreCase(Metadata.CREATEDATE))
            {
              if (mdType.definesType().equals("dss.vector.solutions.general.Email"))
              {
                continue;
              }
              if (mdType.definesType().equals("com.runwaysdk.system.transaction.TransactionRecord"))
              {
                continue;
              }
            }
  
            if (definedAttribute.equalsIgnoreCase(Metadata.SITEMASTER))
            {
              if (mdType.definesType().equals("com.runwaysdk.system.transaction.TransactionRecord"))
              {
                continue;
              }
            }
          }
  
          // Ignore attribute dimensions
          if (entity instanceof MdAttributeDimensionDAOIF)
          {
            continue;
          }
  
          // We don't want to export the attribute definitions of the locales on
          // our MdLocalStructs
          if (entity instanceof MdAttributeCharDAOIF)
          {
            MdAttributeCharDAOIF mdAttribute = (MdAttributeCharDAOIF) entity;
            MdClassDAOIF definedByClass = mdAttribute.definedByClass();
            if (definedByClass instanceof MdLocalStructDAOIF)
            {
              continue;
            }
          }
  
          Row row = null;
          for (int i = 0; i < this.columns.size(); ++i)
          {
            if (valueStoreTagName.size() > 0 && entity.getType().equals(LocalizedValueStore.CLASS))
            {
              String tag = entity.getValue(LocalizedValueStore.STORETAG);
              
              if (valueStoreTagName.contains(tag))
              {
                // do default
              }
              else
              {
                continue;
              }
            }
            
            if (row == null)
            {
              row = sheet.createRow(r++);
            }
            
            this.columns.get(i).exportData(workbook, sheet, row, entity, mdLocalStruct, struct, attributeName);
          }
        }
        catch (DataNotFoundException ex)
        {
          logger.error("Error occurred while exporting [" + definingType + "." + attributeName + "].", ex);
        }
      }
    }
  }

  public void setTabWideLocalAttributeName(String tabWideAttributeLocalAttributeName)
  {
    this.tabWideAttributeLocalAttributeName = tabWideAttributeLocalAttributeName;
  }

  public void setTabWideEntityType(String tabWideEntityType)
  {
    this.tabWideEntityType = tabWideEntityType;
  }

  public void addValueStoreTagName(String tagName)
  {
    this.valueStoreTagName.add(tagName);
  }
  
  public void addQueryCriteria(AttributeLocalQueryCriteria queryCriteria)
  {
    this.queryCriterias.add(queryCriteria);
  }
}
