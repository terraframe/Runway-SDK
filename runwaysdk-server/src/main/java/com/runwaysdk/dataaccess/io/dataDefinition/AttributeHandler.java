package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.io.ImportManager;

public class AttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public AttributeHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    EntityDAO entity = (EntityDAO) context.getObject(EntityInfo.CLASS);
    String structAttributeName = (String) context.getObject(StructAttributeHandler.STRUCT_ATTRIBUTE_NAME);

    String attributeName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    String value = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_VALUE_ATTRIBUTE);

    if (structAttributeName != null)
    {
      entity.setStructValue(structAttributeName, attributeName, value);
    }
    else
    {
      entity.setValue(attributeName, value);
    }
  }
}
