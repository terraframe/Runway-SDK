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
package com.runwaysdk.business.generation.view;

import java.util.LinkedList;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;

/**
 * A {@link ContentListener} adapter used for generating jsps which generate
 * read/write operations on individual attributes. Provides helper methods and
 * generic routines for dealing with struct attributes.
 * 
 * @author Justin Smethie
 */
public abstract class AttributeAdapter extends ContentAdapter
{
  /**
   * The default component name
   */
  private static final String DEFAULT_NAME = "item";

  /**
   * A stack of the component names which are in scope. The default component
   * name is 'item' and must always be in scope.
   */
  private LinkedList<String>  componentStack;

  protected AttributeAdapter(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    super(mdEntity, fileName, extension);

    componentStack = new LinkedList<String>();
    componentStack.add(DEFAULT_NAME);
  }

  /**
   * @return The concatination of the all the component names in scope seperated
   *         by '.'
   */
  protected String getComponentName()
  {
    StringBuffer buffer = new StringBuffer(componentStack.getFirst());

    for (int i = 1; i < componentStack.size(); i++)
    {
      buffer.append("." + componentStack.get(i));
    }

    return buffer.toString();
  }

  /**
   * Adds a new component name to the current scope
   * 
   * @param component
   *          Name to add to scope
   */
  protected void pushComponent(String component)
  {
    componentStack.add(component);
  }

  /**
   * Removes the last component name from the scope
   */
  protected void popComponent()
  {
    if (componentStack.size() > 1)
    {
      componentStack.removeLast();
    }
  }

  /**
   * Generic routine for dealing with non struct attributes. Uses the Template
   * pattern to specify concrete behavior of specific non struct attribute types
   */
  public void attribute(AttributeEventIF event)
  {
    MdAttributeDAOIF mdAttribute = event.getMdAttribute();
    MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttribute.getMdAttributeConcrete();

    String attributeName = mdAttribute.definesAttribute();

    if (!mdAttribute.isSystem() && !attributeName.equals(ElementInfo.OWNER) && !attributeName.equals(ElementInfo.DOMAIN) && !attributeName.equals(ComponentInfo.KEY))
    {
      if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
      {
        generateReference(mdAttributeConcrete);
      }
      else if (mdAttributeConcrete instanceof MdAttributeMomentDAOIF)
      {
        generateMoment(mdAttributeConcrete);
      }
      else if (mdAttributeConcrete instanceof MdAttributeEnumerationDAOIF)
      {
        generateEnumeration(mdAttributeConcrete);
      }
      else if (mdAttributeConcrete instanceof MdAttributeMultiReferenceDAOIF)
      {
        generateMultiReference(mdAttributeConcrete);
      }
      else if (mdAttributeConcrete instanceof MdAttributeEncryptionDAOIF)
      {
        generateEncryption(mdAttributeConcrete);
      }
      else if (mdAttributeConcrete instanceof MdAttributeBooleanDAOIF)
      {
        generateBoolean(mdAttributeConcrete);
      }
      else
      {
        generateAttribute(mdAttributeConcrete);
      }
    }
  }

  /**
   * @param mdAttributeConcrete
   */
  private void generateMultiReference(MdAttributeConcreteDAOIF mdAttributeConcrete)
  {
    // TODO Auto-generated method stub

  }

  /**
   * Generic routine for opening struct attributes and adding the struct
   * attribute name to the list of component names which are in scope
   */
  public void beforeStructAttribute(AttributeEventIF attributeEvent)
  {
    String attributeName = attributeEvent.getMdAttribute().definesAttribute();

    writeDT(attributeName);
    writeStruct(attributeName);

    this.pushComponent(attributeName);
  }

  /**
   * Generic routine for closing struct attributes and removing the struct
   * attribute name from the list of component names which are in scope
   */
  public void afterStructAttribute(AttributeEventIF attributeEvent)
  {
    this.popComponent();

    // Close struct tag
    getWriter().closeTag();

    // Close dt tag
    getWriter().closeTag();
  }

  /**
   * Generate context specific jsp code for boolean attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateBoolean(MdAttributeDAOIF mdAttribute);

  /**
   * Generate context specific jsp code for moment attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateMoment(MdAttributeDAOIF mdAttribute);

  /**
   * Generate context specific jsp code for generic attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateAttribute(MdAttributeDAOIF mdAttribute);

  /**
   * Generate context specific jsp code for enumeration attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateEnumeration(MdAttributeDAOIF mdAttribute);

  /**
   * Generate context specific jsp code for multi reference attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateMultiReference(MdAttributeDAOIF mdAttribute);

  /**
   * Generate context specific jsp code for enumeration attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateEncryption(MdAttributeDAOIF mdAttribute);

  /**
   * Generate context specific jsp code for reference attributes
   * 
   * @param mdAttribute
   */
  protected abstract void generateReference(MdAttributeDAOIF mdAttribute);
}
