package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

public interface TagHandlerIF
{

  /**
   * @param localName
   * @param attributes
   * @param context
   *          TODO
   */
  public void onStartElement(String localName, Attributes attributes, TagContext context);

  public void onEndElement(String uri, String localName, String name, TagContext context);

  public void characters(char[] ch, int start, int length, TagContext context);

  public String getKey();
}
