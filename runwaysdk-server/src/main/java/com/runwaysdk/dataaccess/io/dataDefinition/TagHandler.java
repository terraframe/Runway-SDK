package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public abstract class TagHandler extends HandlerFactory implements TagHandlerIF, HandlerFactoryIF
{
  private ImportManager manager;

  public TagHandler(ImportManager manager)
  {
    this.manager = manager;

    manager.register(this.getKey(), this);
  }

  /**
   * @return the manager
   */
  public ImportManager getManager()
  {
    return manager;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#characters(char[], int, int)
   */
  @Override
  public void characters(char[] ch, int start, int length, TagContext context)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#getKey()
   */
  @Override
  public String getKey()
  {
    return this.getClass().getName();
  }
}
