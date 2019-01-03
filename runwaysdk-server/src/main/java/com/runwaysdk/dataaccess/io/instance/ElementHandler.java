/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.io.instance;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.LinkedList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;
import com.runwaysdk.dataaccess.resolver.SynchronusExecutor;
import com.runwaysdk.dataaccess.resolver.UpdateRunnable;
import com.runwaysdk.dataaccess.transaction.ThreadTransactionState;

/**
 * Handles the <relationship_instance> tag in the instance importer. See
 * http:\\www.runwaysdk.com\schema\instance.xsd for the entire importer schema.
 * Imports the type, parentOid, childOid, and Id attributes of a Relationship.
 * After importing the previous attributes this passes control to the appropiate
 * handler for other system and user defined attributes of the Relationship, eg,
 * owner.
 * 
 * @author Justin Smethie
 */
public class ElementHandler extends XMLHandlerWithResolver implements UncaughtExceptionHandler
{
  protected ElementDAO          elementDAO;

  /**
   * The sequence of the Relationship which already exist in the database.
   * Initiallized as Integer.MIN_VALUE to gurantee and import if the Entity does
   * not already exist in the database.
   */
  protected long                sequence;

  /**
   * The value of the siteMaster Attribute of the Relationship which already
   * exist in the database.
   */
  protected String              siteMaster;

  /**
   * If the Entity being imported is new in this database
   */
  protected boolean             isNew;

  /**
   * True when the imported sequence number is less than or equal to the
   * sequence number of the entity that already exists on this node;
   */
  protected boolean             skipProcessing;

  /**
   * Collection of exceptions which were thrown while setting the values of the
   * attributes
   */
  private Collection<Throwable> attributeExceptions;

  private Boolean               ignoreSeq;

  /**
   * @param reader
   *          The XMLReader to parse
   * @param previousHandler
   *          The handler which control passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single impot
   * @param attributes
   *          The attributes defined inside of the <relationship_instance> tag
   * @throws SAXException
   */
  public ElementHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver) throws SAXException
  {
    this(reader, previousHandler, manager, resolver, false);
  }

  public ElementHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Boolean ignoreSeq) throws SAXException
  {
    super(reader, previousHandler, manager, resolver);

    this.sequence = Integer.MIN_VALUE;
    this.attributeExceptions = new LinkedList<Throwable>();
    this.isNew = false;
    this.ignoreSeq = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   * java.lang.String, java.lang.String, org.xml.sax.Attributes)
   * 
   * Passes control to the appropiate handler for INSTANCE_VALUE_TAG,
   * SELECTOR_TAG, and COMPOSITION_REF_TAG tags.
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (!skipProcessing)
    {
      if (localName.equals(XMLTags.VALUE_TAG))
      {
        ValueHandler handler = new EntityValueHandler(reader, this, manager, attributes, elementDAO, this);
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
      }
      else if (localName.equals(XMLTags.ENUMERATION_TAG))
      {
        EnumerationHandler sHandler = new ElementEnumerationHandler(reader, this, manager, elementDAO, attributes);
        reader.setContentHandler(sHandler);
        reader.setErrorHandler(sHandler);
      }
      else if (localName.equals(XMLTags.STRUCT_REF_TAG))
      {
        StructHandler cHandler = new StructHandler(reader, this, manager, elementDAO, attributes, this);
        reader.setContentHandler(cHandler);
        reader.setErrorHandler(cHandler);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String) Imports the relationship, with all
   * attributes defined, and then returns control to the handler which passed
   * control.
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (!skipProcessing)
    {
      if (ignoreSeq || (elementDAO.getSequence() > sequence))
      {
        ThreadTransactionState state = ThreadTransactionState.getCurrentThreadTransactionState();
        UpdateRunnable runnable = new UpdateRunnable(getResolver(), state, elementDAO, attributeExceptions);

        SynchronusExecutor thread = new SynchronusExecutor(runnable);
        thread.start();
      }
    }

    reader.setContentHandler(previousHandler);
    reader.setErrorHandler(previousHandler);
  }

  @Override
  public void uncaughtException(Thread thread, Throwable throwable)
  {
    this.attributeExceptions.add(throwable);
  }
}
