package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public interface TagHandlerIF
{

  /**
   * Delegation of SAX start element event
   * 
   * @param localName
   * @param attributes
   * @param context
   */
  public void onStartElement(String localName, Attributes attributes, TagContext context);

  /**
   * Delegation of SAX end element event
   * 
   * @param localName
   * @param attributes
   * @param context
   */
  public void onEndElement(String uri, String localName, String name, TagContext context);

  /**
   * Delegation of SAX characters event
   * 
   * @param localName
   * @param attributes
   * @param context
   */
  public void characters(char[] ch, int start, int length, TagContext context);

  /**
   * @return Unique identifier of
   */
  public String getKey();

  /**
   * Indicates if the handler will modify the manager state based upon the localName of the tag
   * 
   * @param localName
   * @return
   */
  public boolean modifiesState(String localName);

  /**
   * @return Manager being used in the import
   */
  public ImportManager getManager();
}
