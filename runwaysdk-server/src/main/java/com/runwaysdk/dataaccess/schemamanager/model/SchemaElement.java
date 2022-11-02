/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.schemamanager.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.xml.XSDDefinitionNotResolvedException;
import com.runwaysdk.dataaccess.schemamanager.xml.XSDElementFinder;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSType;

/**
 * 
 * The most general element in the model of the XML schema
 * 
 * @author Aritra
 * 
 */
public abstract class SchemaElement extends ElementObservable
{
  private Map<String, String>  xmlAttributes;

  /**
   * This list contains the elements that appear inside this element in the XML
   * documents, except for the ones that have separate representations in the
   * model to preserve their semantics for the purpose of visualiztion and
   * merging updates.
   */
  private Set<SchemaElementIF> children;

  private SchemaElementIF      parent;

  private String               tag;

  public SchemaElement(Attributes attributes, String tag)
  {
    this(attributes, tag, null);
  }

  public SchemaElement(Attributes attributes, String tag, SchemaElementIF parent)
  {
    super();

    this.tag = tag;
    this.xmlAttributes = new LinkedHashMap<String, String>();
    this.children = new LinkedHashSet<SchemaElementIF>();
    this.parent = parent;

    if (attributes != null)
    {
      for (int i = 0; i < attributes.getLength(); i++)
      {
        xmlAttributes.put(attributes.getLocalName(i), attributes.getValue(i));
      }
    }
  }

  @Override
  public String toString()
  {
    return tag;
  }

  public void addAttributesWithReplacement(Attributes attributes)
  {
    for (int i = 0; i < attributes.getLength(); i++)
    {
      xmlAttributes.put(attributes.getLocalName(i), attributes.getValue(i));
    }
  }

  public void addAttributesWithReplacement(SchemaElementIF element)
  {
    Map<String, String> attributes = element.getAttributes();

    for (Entry<String, String> entry : attributes.entrySet())
    {
      xmlAttributes.put(entry.getKey(), entry.getValue());
    }
  }

  public Map<String, String> getAttributes()
  {
    return xmlAttributes;
  }

  public String getXMLAttributeValue(String localName)
  {
    if (xmlAttributes.containsKey(localName))
    {
      return xmlAttributes.get(localName);
    }

    return null;
  }

  public String getTag()
  {
    return tag;
  }

  public String getKey()
  {
    return this.accept(new KeyResolver());
  }

  public boolean hasParent()
  {
    return ( this.parent == null );
  }

  public void setParent(SchemaElementIF parent)
  {
    this.parent = parent;
  }

  public void remove()
  {
    List<SchemaElementIF> _children = new LinkedList<SchemaElementIF>(children);

    for (SchemaElementIF child : _children)
    {
      child.remove();
    }

    if (this.getParent() != null)
    {
      this.getParent().removeChild(this);
    }
  }

  public SchemaElementIF getParent()
  {
    return parent;
  }

  public Set<SchemaElementIF> children()
  {
    return children;
  }

  public boolean isMerged()
  {
    return false;
  }

  public boolean addChild(SchemaElementIF child)
  {
    if (!child.isMerged() && !child.equals(this))
    {
      boolean added = children.add(child);

      if (!added)
      {
        if (children.contains(child))
        {
          SchemaElementIF existing = this.getChild(child);

          // existing.addAttributesWithReplacement(child);
          existing.addChildren(child.children());
        }
        else
        {
          throw new RuntimeException("Could not add child:" + child.getTag() + " (" + child.toString() + ")");
        }
      }
      else
      {
        child.setParent(this);
      }

      return added;
    }

    return false;
  }

  public void addChildren(Set<SchemaElementIF> _children)
  {
    for (SchemaElementIF child : _children)
    {
      this.addChild(child);
    }
  }

  /**
   * The default implementation of equals compares two elements by comparing
   * their tags and the attributes as well. Equality of parents is not tested,
   * as an element is always added to a single parent element. The equality of
   * children is ignored as well, as inside the update tags, an element may be
   * specified just by its tag name and attributes.
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof SchemaElement)
    {
      SchemaElement otherElement = (SchemaElement) obj;
      if (this.getTag().equals(otherElement.getTag()))
      {
        if (this.getAttributes().equals(otherElement.getAttributes()))
        {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isEmpty()
  {
    return children.isEmpty();
  }

  @Override
  public int hashCode()
  {
    return getTag().hashCode();
  }

  public SchemaElementIF getChild(SchemaElementIF child)
  {
    Iterator<SchemaElementIF> it = children.iterator();

    while (it.hasNext())
    {
      SchemaElementIF next = it.next();

      if (child.equals(next))
      {
        return next;
      }
    }

    return null;
  }

  /**
   * Returns a child that has all the attributes and the same tag as the input
   * parameters. Assumption is that there will be always a unique child.
   * 
   * @param attributes
   * @param tag
   * @return
   */
  public SchemaElementIF getChild(Attributes attributes, String tag)
  {
    outer: for (SchemaElementIF child : children())
    {
      if (child.getTag().equals(tag))
      {
        for (int i = 0; i < attributes.getLength(); i++)
        {
          String key = attributes.getLocalName(i);
          String value = attributes.getValue(i);

          String childValue = child.getXMLAttributeValue(key);

          if ( ( value != null && childValue == null ) || (value != null && !value.equals(childValue)))
          {
            continue outer;
          }
        }

        return child;
      }
    }

    return null;
  }

  /**
   * Returns a child that has all the attributes and the same tag as the input
   * parameters. Assumption is that there will be always a unique child.
   * 
   * @param tag
   * @param key
   * 
   * @return
   */
  public SchemaElementIF getChild(String tag, String key)
  {
    for (SchemaElementIF child : children())
    {
      String name = child.getAttributes().get(XMLTags.NAME_ATTRIBUTE);
      if (child.getTag().equals(tag) && name.equals(key))
      {
        return child;
      }
    }

    return null;
  }

  /**
   * Returns a child that has the same tag as the input parameters. Assumption
   * is that there will be always a unique child. IMPORTANT: This method
   * disregards the tag attributes when looking for a match and so it should
   * only be used when a tag has no attributes.
   * 
   * @param tag
   * @return
   */
  public SchemaElementIF getChild(String tag)
  {
    for (SchemaElementIF child : children())
    {
      if (child.getTag().equals(tag))
      {
        return child;
      }
    }

    return null;
  }

  public void removeChild(SchemaElementIF child)
  {
    if (children.contains(child))
    {
      children.remove(child);
    }
  }

  public boolean hasChildren()
  {
    return children.size() > 0;
  }

  @Override
  public String[] getElementsToObserve()
  {
    return new String[] {};
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    // DO NOTHING AT THE MOMENT
  }

  public XSType getXSType(XSSchemaSet schemaSet)
  {
    SchemaElementIF parent = this.getParent();

    if (parent != null)
    {
      XSType parentType = parent.getXSType(schemaSet);

      if (parentType.asComplexType() != null)
      {
        XSDElementFinder elementFinder = new XSDElementFinder(this.getTag());
        parentType.asComplexType().visit(elementFinder);
        return elementFinder.getElementDecl().getType();
      }
    }
    else
    {
      XSDElementFinder elementFinder = new XSDElementFinder(this.getTag());

      StringBuilder xsds = new StringBuilder("{");
      Collection<XSSchema> schemas = schemaSet.getSchemas();
      for (XSSchema schema : schemas)
      {
        schema.visit(elementFinder);
        xsds.append("[" + schema.getLocator().getSystemId() + "],");
      }
      xsds.append("}");

      if (elementFinder.getElementDecl() == null)
      {
        throw new XSDDefinitionNotResolvedException(this.getTag(), "Could not find tag [" + this.getTag() + "] in xsdset " + xsds.toString());
      }

      return elementFinder.getElementDecl().getType();
    }

    throw new XSDDefinitionNotResolvedException(this.getTag(), "No xsd type definition was found for " + this.getTag());
  }

  @SuppressWarnings("unchecked")
  public static <T> T getAncestor(SchemaElementIF element, Class<T> clazz)
  {
    SchemaElementIF parent = element.getParent();

    if (parent != null)
    {
      if (clazz.isInstance(parent))
      {
        return (T) parent;
      }

      return SchemaElement.getAncestor(parent, clazz);
    }

    return null;
  }
}
