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
