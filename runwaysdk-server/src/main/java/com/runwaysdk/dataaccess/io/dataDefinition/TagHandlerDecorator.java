package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public class TagHandlerDecorator implements TagHandlerIF
{
  private TagHandlerIF handler;

  public TagHandlerDecorator(TagHandlerIF handler)
  {
    this.handler = handler;
  }

  /**
   * @return the handler
   */
  public TagHandlerIF getHandler()
  {
    return handler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#getManager()
   */
  @Override
  public ImportManager getManager()
  {
    return this.handler.getManager();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    this.handler.onStartElement(localName, attributes, context);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    this.handler.onEndElement(uri, localName, name, context);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#characters(char[], int, int, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void characters(char[] ch, int start, int length, TagContext context)
  {
    this.handler.characters(ch, start, length, context);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#getKey()
   */
  @Override
  public String getKey()
  {
    return this.handler.getKey();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#modifiesState(java.lang.String)
   */
  @Override
  public boolean modifiesState(String localName)
  {
    return this.handler.modifiesState(localName);
  }
}
