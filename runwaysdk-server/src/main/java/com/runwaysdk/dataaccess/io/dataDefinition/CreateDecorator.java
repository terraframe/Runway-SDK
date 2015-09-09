package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

public class CreateDecorator extends TagHandlerDecorator implements TagHandlerIF
{
  public CreateDecorator(TagHandlerIF handler)
  {
    super(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerDecorator#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      this.getManager().enterCreateState();
    }
    else
    {
      super.onStartElement(localName, attributes, context);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      this.getManager().leavingCurrentState();
    }
    else
    {
      super.onEndElement(uri, localName, name, context);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#modifiesState(java.lang.String)
   */
  @Override
  public boolean modifiesState(String localName)
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      return true;
    }
    else
    {
      return super.modifiesState(localName);
    }
  }

}
