package com.runwaysdk.localization.configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.system.metadata.MdAttributeLocal;
import com.runwaysdk.system.metadata.MdEntity;
import com.runwaysdk.system.metadata.MdException;
import com.runwaysdk.system.metadata.MdLocalizable;

public class ConfigurationBuilder
{
  SpreadsheetConfiguration config;
  
  public ConfigurationBuilder()
  {
    config = new SpreadsheetConfiguration();
  }
  
  public AttributeLocalTabConfiguration addLocalizedValueStoreTab(String tabName, String tagName)
  {
    List<ColumnConfiguration> columns = new ArrayList<ColumnConfiguration>();
    columns.add(new EntityColumnConfiguration("Key", MdEntity.KEYNAME));
    
    List<MdAttributeLocal> valueStore = new LinkedList<MdAttributeLocal>();
    MdAttributeLocal storeValueLocal = (MdAttributeLocal) BusinessFacade.get(LocalizedValueStore.getStoreValueMd());
    valueStore.add(storeValueLocal);
    
    AttributeLocalTabConfiguration valueStoreTab = new AttributeLocalTabConfiguration(tabName, valueStore, columns);
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
    
    List<MdAttributeLocal> listMdAttrLocal = new LinkedList<MdAttributeLocal>();
    listMdAttrLocal.add(mdAttrLocal);
    
    AttributeLocalTabConfiguration attrLocalTab = new AttributeLocalTabConfiguration(tabName, listMdAttrLocal, columns);
    attrLocalTab.setTabWideLocalAttributeName(MdLocalizable.getMessageMd().definesAttribute());
    config.addTab(attrLocalTab);
    
    return attrLocalTab;
  }

  public SpreadsheetConfiguration build()
  {
    return config;
  }
}
