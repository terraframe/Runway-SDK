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

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.localization.LocaleDimension;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.system.metadata.MdAttributeLocal;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.metadata.MdLocalizable;

public class ConfigurationBuilder
{
  SpreadsheetConfiguration config;
  
  List<LocaleDimension> dimensions;
  
  public ConfigurationBuilder()
  {
    config = new SpreadsheetConfiguration();
    
    this.dimensions = new ArrayList<LocaleDimension>();
    this.addLocaleDimensions(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }
  
  private void addLocaleDimensions(String localeString)
  {
    dimensions.add(new LocaleDimension(localeString));
    for (MdDimensionDAOIF dim : MdDimensionDAO.getAllMdDimensions())
    {
      dimensions.add(new LocaleDimension(localeString, dim));
    }
  }
  
  public AttributeLocalTabConfiguration addLocalizedValueStoreTab(String tabName, String tagName)
  {
    List<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
    columns.add(new EntityColumnConfiguration("Key", MdEntity.KEYNAME));
    columns.add(new LocaleMultiColumnConfiguration(LocalizedValueStore.getStoreValueMd().definesAttribute(), dimensions));
    
    List<MdAttributeLocal> valueStore = new LinkedList<MdAttributeLocal>();
    MdAttributeLocal storeValueLocal = (MdAttributeLocal) BusinessFacade.get(LocalizedValueStore.getStoreValueMd());
    valueStore.add(storeValueLocal);
    
    AttributeLocalTabConfiguration valueStoreTab = new AttributeLocalTabConfiguration(tabName, valueStore, dimensions, columns);
    valueStoreTab.setTabWideLocalAttributeName(LocalizedValueStore.getStoreValueMd().definesAttribute());
    valueStoreTab.setTabWideEntityType(LocalizedValueStore.CLASS);
    valueStoreTab.setValueStoreTagName(tagName);
    config.addTab(valueStoreTab);
    
    return valueStoreTab;
  }
  
  public AttributeLocalTabConfiguration addAttributeLocalTab(String tabName, MdAttributeLocal mdAttrLocal)
  {
    List<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
    columns.add(new EntityColumnConfiguration("Key", MdEntity.KEYNAME));
    columns.add(new EntityColumnConfiguration("Type", MdEntity.TYPE));
//    columns.add(new ArritubuteLocalColumnConfiguration("Attribute Name", MdAttributeLocal.ATTRIBUTENAME));
    columns.add(new LocaleMultiColumnConfiguration(mdAttrLocal.getAttributeName(), dimensions));
    
    List<MdAttributeLocal> listMdAttrLocal = new LinkedList<MdAttributeLocal>();
    listMdAttrLocal.add(mdAttrLocal);
    
    AttributeLocalTabConfiguration attrLocalTab = new AttributeLocalTabConfiguration(tabName, listMdAttrLocal, dimensions, columns);
    attrLocalTab.setTabWideLocalAttributeName(MdLocalizable.getMessageMd().definesAttribute());
    config.addTab(attrLocalTab);
    
    return attrLocalTab;
  }

  public SpreadsheetConfiguration build()
  {
    return config;
  }
}
