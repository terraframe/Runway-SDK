/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class FieldConditionHandler extends XMLHandler implements ConditionListIF
{
  private MdWebFormDAO           mdForm;

  private MdFieldDAO             mdField;

  private Stack<ConditionListIF> stack;

  /**
   * Handler Construction, parses and creates a list new MdAttribute definition
   * 
   * @param attributes
   *          The XML attributes of the tag.
   * @param reader
   *          The XML parsing stream.
   * @param previousHandler
   *          The Handler in which control was passed from.
   * @param manager
   *          ImportManager containing information about the status of the
   *          import.
   * @param mdForm
   *          The MdClass on which the MdAttribute is defined
   */
  public FieldConditionHandler(XMLReader reader, MdWebFieldHandler previousHandler, ImportManager manager, MdWebFormDAO mdForm, MdFieldDAO mdField)
  {
    super(reader, previousHandler, manager);

    this.mdForm = mdForm;
    this.mdField = mdField;

    this.stack = new Stack<ConditionListIF>();
    this.stack.add(this);
  }

  public void addCondition(ConditionAttributeIF condition)
  {
    ( (MdWebFieldHandler) ( this.previousHandler ) ).addCondition(condition);
  }

  /**
   * @return Returns the condition list currently in scope on the stack.
   */
  public ConditionListIF getCurrent()
  {
    return this.stack.peek();
  }

  /**
   * Handles all the attribute tags, see datatype.xsd for a complete list
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    // Delegates elements tag to methods
    if (localName.equals(XMLTags.NONE_TAG))
    {
      this.getCurrent().addCondition(new NoneConditionAttribute(this.mdField));
    }
    else if (localName.equals(XMLTags.DATE_TAG) || localName.equals(XMLTags.CHARACTER_TAG) || localName.equals(XMLTags.DOUBLE_TAG) || localName.equals(XMLTags.LONG_TAG))
    {
      MdFieldDAO mdField = ( this.getCurrent().equals(this) ? this.mdField : null );

      this.getCurrent().addCondition(new ConditionAttribute(localName, attributes, this.mdForm, mdField, this.manager));
    }
    else if (localName.equals(XMLTags.AND_TAG))
    {
      CompositeConditionAttribute condition = new CompositeConditionAttribute(localName, this.mdField, this.manager);

      this.getCurrent().addCondition(condition);
      this.stack.push(condition);
    }
  }

  /**
   * Passes parsing control back to the previous handler on the close of an
   * attributes tag Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.CONDITION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
    else if (localName.equals(XMLTags.AND_TAG))
    {
      this.stack.pop();
    }
  }
}
