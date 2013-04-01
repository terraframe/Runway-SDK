/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.schemamanager.model;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.xml.SMXMLTags;

/**
 * {@link SchemaElement} which represents all tags under a
 * {@link PermissionHolder}
 * 
 * @author Justin Smethie
 */
public class PermissionElement extends KeyedElement implements PermissionElementIF, ElementListener
{
  private boolean export;

  public PermissionElement(Attributes map, String tag, SchemaElementIF parent)
  {
    super(map, tag, parent);

    this.export = true;
  }

  public PermissionElement(Attributes map, String tag)
  {
    super(map, tag);

    this.export = true;
  }

  @Override
  public String toString()
  {
    return this.getKey();
  }

  public boolean isExport()
  {
    return export;
  }

  public void setExport(boolean export)
  {
    this.export = export;
  }

  @Override
  public boolean addChild(SchemaElementIF child)
  {
    boolean success = super.addChild(child);

    if (child instanceof PermissionElement && success)
    {
      PermissionElement element = (PermissionElement) child;
      PermissionActionElement ancestor = this.getActionElement();

      if (ancestor != null)
      {
        ancestor.addDescendent(element);
      }
    }

    return success;
  }

  public PermissionActionElement getActionElement()
  {
    return SchemaElement.getAncestor(this, PermissionActionElement.class);
  }

  /**
   * Removes this element and all of its children from the model
   */
  public void remove()
  {
    SchemaElementIF _parent = this.getParent();

    _parent.removeChild(this);

    PermissionActionElement element = this.getActionElement();

    if (element != null)
    {
      element.removeDescendent(this.getKey());
    }

    if (_parent instanceof PermissionElement && !_parent.hasChildren())
    {
      ( (PermissionElement) _parent ).remove();
    }
  }
  
  @Override
  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  public String[] getElementsToObserve()
  {
    String key = this.getKeyOfDependencies();

    if (!key.equals(""))
    {
      return new String[] { key };
    }

    return new String[] {};
  }

  @Override
  public String getKeyOfDependencies()
  {
    String key = this.getObjectKey();

    SchemaElementIF parent = this.getParent();

    if (parent != null && parent instanceof PermissionElementIF)
    {
      PermissionElementIF element = (PermissionElementIF) parent;

      key = element.getKeyOfDependencies() + "." + key;
    }

    return key;
  }
  
  @Override
  public String getObjectKey()
  {
    String tagName = this.getTag();

    if (SMXMLTags.isPermissionTypeTag(tagName))
    {
      return this.getXMLAttributeValue(SMXMLTags.TYPE_ATTRIBUTE);
    }
    else if (tagName.equals(SMXMLTags.STATE_PERMISSION_TAG))
    {
      return this.getXMLAttributeValue(SMXMLTags.STATE_NAME_ATTRIBUTE);
    }
    else if (tagName.equals(SMXMLTags.ATTRIBUTE_PERMISSION_TAG))
    {
      String attribute = this.getXMLAttributeValue(SMXMLTags.PERMISSION_ATTRIBUTE_NAME);

      String dimension = this.getXMLAttributeValue(SMXMLTags.DIMENSION_ATTRIBUTE);

      if (dimension != null && dimension.length() > 0)
      {
        return attribute + "#" + dimension;
      }
      else
      {
        return attribute;
      }
    }
    else if (tagName.equals(SMXMLTags.MD_METHOD_PERMISSION_TAG))
    {
      return this.getXMLAttributeValue(SMXMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE);
    }
    else if (tagName.equals(SMXMLTags.OPERATION_TAG))
    {
      return "#" + this.getXMLAttributeValue(SMXMLTags.NAME_ATTRIBUTE);
    }

    return null;
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      this.remove();
    }
    else if (event instanceof ChangeKeyEvent)
    {      
      SchemaElementIF source = event.getSource();
      
      // Attribute has changed its name, this node must correspondling change its name
      if(source instanceof SchemaAttribute)
      {
        String oldKey = this.getKey();
        
        this.getAttributes().put(SMXMLTags.PERMISSION_ATTRIBUTE_NAME, source.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
        
        String newKey = this.getKey();
        
        this.fireEvent(new ChangeKeyEvent(this, oldKey, newKey));
      }
    }
  }
}
