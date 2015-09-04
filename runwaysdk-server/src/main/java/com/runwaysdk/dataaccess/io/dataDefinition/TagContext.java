package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public class TagContext
{
  private String              localName;

  private Attributes          attributes;

  private Map<String, Object> objects;

  private TagContext          parent;

  private TagHandlerIF        handler;

  private boolean             parse;

  /**
   * @param localName
   * @param attributes
   * @param current
   */
  public TagContext(String localName, Attributes attributes, TagContext parent, TagHandlerIF handler)
  {
    this.localName = localName;
    this.attributes = attributes;
    this.parent = parent;
    this.handler = handler;
    this.objects = new HashMap<String, Object>();
    this.parse = true;
  }

  /**
   * @return the localName
   */
  public String getLocalName()
  {
    return localName;
  }

  /**
   * @return the attributes
   */
  public Attributes getAttributes()
  {
    return attributes;
  }

  /**
   * @return the parent
   */
  public TagContext getParent()
  {
    return parent;
  }

  /**
   * @return the handler
   */
  public TagHandlerIF getHandler()
  {
    return handler;
  }

  public void setObject(String key, Object object)
  {
    this.objects.put(key, object);
  }

  public Object getObject(String key)
  {
    Object object = this.objects.get(key);

    if (object != null)
    {
      return object;
    }
    else if (this.parent != null)
    {
      return this.parent.getObject(key);
    }

    return null;
  }
  
  /**
   * @return the parse
   */
  public boolean isParse()
  {
    return parse;
  }
  
  /**
   * @param parse the parse to set
   */
  public void setParse(boolean parse)
  {
    this.parse = parse;
  }
}
