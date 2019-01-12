package com.runwaysdk.localization.configuration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

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
  protected List<String> typeExemptions;
  
  protected List<? extends MdAttributeLocal> attributeLocals;
  
  /**
   * A tab-wide definition for the attribute name of the AttributeLocal. Completely optional, but if its not provided then the sheet must contain the attribute name.
   */
  protected String tabWideAttributeLocalAttributeName = null;
  
  /**
   * A tab-wide definition for the entity type.
   */
  protected String tabWideEntityType = null;

  protected String valueStoreTagName;
  
  protected List<LocaleDimension> dimensions;
  
  public AttributeLocalTabConfiguration(String sheetName, List<? extends MdAttributeLocal> attributeLocals, List<LocaleDimension> dimensions, List<ColumnConfiguration> columns)
  {
    super(sheetName, columns);
    
    this.typeExemptions = new LinkedList<String>();
    this.typeExemptions.add("com.runwaysdk.system.metadata.MdAction");
    this.typeExemptions.add(MdParameter.CLASS);
    this.typeExemptions.add(MdIndex.CLASS);
    this.typeExemptions.add(MdMethod.CLASS);
    this.typeExemptions.add(MdAttributeDimension.CLASS);
    
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
      key = keyCell.getStringCellValue();
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
      
      type = row.getCell(column.getIndex()).getStringCellValue();
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
      
      attributeName = row.getCell(column.getIndex()).getStringCellValue();
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
    ColumnConfiguration columnConfig = this.getColumnByAttribute(attributeName);
    int index = columnConfig.getIndex();
    
    for (LocaleDimension ld : this.dimensions)
    {
      String value = row.getCell(index).getStringCellValue();
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
    for (MdAttributeLocal local : all)
    {
      MdTypeDAOIF mdType = MdTypeDAO.get(local.getValue(MdAttributeLocal.DEFININGMDCLASS));
      MdLocalStructDAOIF mdLocalStruct = (MdLocalStructDAOIF) BusinessFacade.getEntityDAO(local.getMdStruct());
      Boolean enforceSiteMaster = MdAttributeBooleanUtil.getTypeSafeValue(mdLocalStruct.getValue(MdLocalStructInfo.ENFORCE_SITE_MASTER));
      String definingType = mdType.definesType();
      String attributeName = local.getAttributeName();

      if (typeExemptions.contains(definingType))
      {
        continue;
      }

      for (String id : EntityDAO.getEntityIdsDB(definingType))
      {
        EntityDAOIF entity = EntityDAO.get(id);
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
          if (valueStoreTagName != null && entity.getType().equals(LocalizedValueStore.CLASS))
          {
            if (entity.getValue(LocalizedValueStore.STORETAG).equals(valueStoreTagName))
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
          
          this.columns.get(i).exportData(workbook, sheet, row, entity, mdLocalStruct, struct);
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

  public void setValueStoreTagName(String tagName)
  {
    this.valueStoreTagName = tagName;
  }
}
