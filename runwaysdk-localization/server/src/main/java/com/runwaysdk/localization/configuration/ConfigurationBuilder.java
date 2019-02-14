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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.LocalizationFacade;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.localization.LocaleDimension;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.system.metadata.MdAttributeLocal;
import com.runwaysdk.system.metadata.MdEntity;

public class ConfigurationBuilder
{
  SpreadsheetConfiguration config;
  
  List<LocaleDimension> dimensions;
  
  public ConfigurationBuilder()
  {
    config = new SpreadsheetConfiguration();
    
    this.dimensions = new ArrayList<LocaleDimension>();
    
    addInstalledLocales();
  }
  
  private void addInstalledLocales()
  {
    this.addLocaleDimensions(MdAttributeLocalInfo.DEFAULT_LOCALE);
    
    List<Locale> locales = LocalizationFacade.getInstalledLocales();
    
    for (Locale locale : locales)
    {
      this.addLocaleDimensions(locale.toLanguageTag().replace("-", "_"));
    }
  }
  
  private void addLocaleDimensions(String localeString)
  {
    dimensions.add(new LocaleDimension(localeString));
    for (MdDimensionDAOIF dim : MdDimensionDAO.getAllMdDimensions())
    {
      dimensions.add(new LocaleDimension(localeString, dim));
    }
  }
  
  /**
   * Creates a tab in the spreadsheet where the first column is 'key' and the rest of the columns are populated from
   * the installed locales. The 'key' column maps directly to LocalizedValueStore.STOREKEY. Only localization saved
   * with the provided tags will be included in this tab.
   * 
   * 
   * @param tabName
   * @param tags
   * @return
   */
  public AttributeLocalTabConfiguration addLocalizedValueStoreTab(String tabName, List<String> tags)
  {
    List<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
    columns.add(new EntityColumnConfiguration("Key", MdEntity.KEYNAME));
    columns.add(new LocaleMultiColumnConfiguration(dimensions));
    
    List<MdAttributeLocal> valueStore = new LinkedList<MdAttributeLocal>();
    MdAttributeLocal storeValueLocal = (MdAttributeLocal) BusinessFacade.get(LocalizedValueStore.getStoreValueMd());
    valueStore.add(storeValueLocal);
    
    AttributeLocalTabConfiguration valueStoreTab = new AttributeLocalTabConfiguration(tabName, valueStore, dimensions, columns);
    valueStoreTab.setTabWideLocalAttributeName(LocalizedValueStore.getStoreValueMd().definesAttribute());
    valueStoreTab.setTabWideEntityType(LocalizedValueStore.CLASS);
    for (String tag : tags)
    {
      valueStoreTab.addValueStoreTagName(tag);
    }
    config.addTab(valueStoreTab);
    
    return valueStoreTab;
  }
  
  /**
   * Creates a tab in the spreadsheet of name `tabName` from the given list of MdAttributeLocal. The attribute name for all MdAttriuteLocal is assumed
   * to be `attributeName`. Since the attributeName is fixed, there will not be a 'attributeName' column in this tab.
   * 
   * @param tabName
   * @param attributeName
   * @param mdAttrLocal
   * @return
   */
  public AttributeLocalTabConfiguration addAttributeLocalTab(String tabName, String attributeName, List<MdAttributeLocal> mdAttrLocals)
  {
    List<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
    columns.add(new EntityColumnConfiguration("Key", MdEntity.KEYNAME));
    columns.add(new EntityColumnConfiguration("Type", MdEntity.TYPE));
    columns.add(new LocaleMultiColumnConfiguration(dimensions));
    
    List<MdAttributeLocal> listMdAttrLocal = new LinkedList<MdAttributeLocal>();
    listMdAttrLocal.addAll(mdAttrLocals);
    
    AttributeLocalTabConfiguration attrLocalTab = new AttributeLocalTabConfiguration(tabName, listMdAttrLocal, dimensions, columns);
    attrLocalTab.setTabWideLocalAttributeName(attributeName);
    config.addTab(attrLocalTab);
    
    return attrLocalTab;
  }
  
  /**
   * Creates a tab in the spreadsheet of name `tabName` from the given list of MdAttributeLocal. The attribute name is written into the
   * spreadsheet as a column and is thus dynamic.
   * 
   * @param tabName
   * @param attributeName
   * @param mdAttrLocal
   * @return
   */
  public AttributeLocalTabConfiguration addDynamicAttributeLocalTab(String tabName, List<MdAttributeLocal> mdAttrLocals)
  {
    List<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
    columns.add(new EntityColumnConfiguration("Key", MdEntity.KEYNAME));
    columns.add(new EntityColumnConfiguration("Type", MdEntity.TYPE));
    columns.add(new AttributeLocalAttributeColumnConfiguration("Attribute Name"));
    columns.add(new LocaleMultiColumnConfiguration(dimensions));
    
    List<MdAttributeLocal> listMdAttrLocal = new LinkedList<MdAttributeLocal>();
    listMdAttrLocal.addAll(mdAttrLocals);
    
    AttributeLocalTabConfiguration attrLocalTab = new AttributeLocalTabConfiguration(tabName, listMdAttrLocal, dimensions, columns);
    config.addTab(attrLocalTab);
    
    return attrLocalTab;
  }

  public SpreadsheetConfiguration build()
  {
    return config;
  }
}
