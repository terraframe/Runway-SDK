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
package com.runwaysdk.dataaccess.transaction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.ElementDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.io.instance.InstanceHandler;
import com.runwaysdk.dataaccess.io.instance.RelationshipHandler;
import com.runwaysdk.dataaccess.io.instance.XMLHandlerWithResolver;
import com.runwaysdk.dataaccess.io.instance.XMLTags;
import com.runwaysdk.dataaccess.resolver.DeleteRunnable;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;

public class TransactionItemSAXImporter extends XMLHandlerWithResolver
{
  private TransactionItemActionSAXImporter transItemActionHandler;

  private String                           componentId;

  private Long                             componentSeq;

  private String                           componentSite;

  private Boolean                          ignoreSeq;

  /**
   * Imports a transaction.
   * 
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The XMLHandler in which control was passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param resolver
   *          TODO
   * @param _componentId
   * @param _componentSeq
   * @param _componentSite
   * @param attributes
   *          The attributes of the instance tag
   */
  public TransactionItemSAXImporter(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, String _componentId, Long _componentSeq, String _componentSite) throws SAXException
  {
    super(reader, previousHandler, manager, resolver);

    this.componentId = _componentId;
    this.componentSeq = _componentSeq;
    this.componentSite = _componentSite;
  }

  public TransactionItemSAXImporter(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes) throws SAXException
  {
    super(reader, previousHandler, manager, resolver);

    this.componentId = attributes.getValue(XMLTransactionTags.XML_TRANSACTION_ITEM_ID_ATTR);
    this.componentSeq = Long.parseLong(attributes.getValue(XMLTransactionTags.XML_TRANSACTION_ITEM_SEQ_ATTR));
    this.componentSite = attributes.getValue(XMLTransactionTags.XML_TRANSACTION_ITEM_SITE_ATTR);
    this.ignoreSeq = Boolean.parseBoolean(attributes.getValue(XMLTransactionTags.XML_TRANSACTION_ITEM_IGNORE_SEQ_ATTR));
  }

  /**
   * Returns the site of the component item.
   * 
   * @return site of the component item.
   */
  protected String getComponentSite()
  {
    return this.componentSite;
  }

  /**
   * Inherited from ContentHandler Parses the elements of the root tags and
   * delegates specific parsing to other handlers (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_TRANSACTION_ITEM_ACTION_TAG))
    {
      this.transItemActionHandler = new TransactionItemActionSAXImporter(reader, this, manager);
      reader.setContentHandler(transItemActionHandler);
      reader.setErrorHandler(transItemActionHandler);
    }

    // IMPORTANT: We must skip creating or updating imported entities which
    // originated from this domain because this domain is the oracle of its own
    // entities. As such we must ensure that the component site of the incoming
    // entity is not the same as the current site before passing off parsing to
    // the appropriate handler.
    else if (localName.equals(XMLTags.OBJECT_TAG) && !this.componentSite.equals(CommonProperties.getDomain()))
    {
      InstanceHandler iHandler = new InstanceHandler(this.componentSeq, reader, this, manager, this.getResolver(), attributes, this.ignoreSeq);
      reader.setContentHandler(iHandler);
      reader.setErrorHandler(iHandler);
    }
    else if (localName.equals(XMLTags.RELATIONSHIP_TAG) && !this.componentSite.equals(CommonProperties.getDomain()))
    {
      RelationshipHandler handler = new RelationshipHandler(this.componentSeq, reader, this, manager, this.getResolver(), attributes, this.ignoreSeq);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_TRANSACTION_ITEM_TAG))
    {
      ActionEnumDAO actionEnumDAO = this.transItemActionHandler.getActionEnumDAO();

      if (actionEnumDAO == ActionEnumDAO.DELETE)
      {
        try
        {
          ElementDAOIF elementDAOIF = ElementDAO.get(this.componentId);

          // Only peform the action if the imported sequence number is greater.
          if (elementDAOIF.getSequence() <= this.componentSeq)
          {
            EntityDAO entityDAO = elementDAOIF.getEntityDAO();

            ThreadTransactionState state = ThreadTransactionState.getCurrentThreadTransactionState();

            Thread thread = new Thread(new DeleteRunnable(getResolver(), state, entityDAO));
            thread.setDaemon(true);
            thread.start();

            while (thread.isAlive())
            {
              try
              {
                Thread.sleep(100L);
              }
              catch (InterruptedException e)
              {
                // Do nothing: Just retest to ensure that the thread is alive
              }
            }
          }
        }
        catch (DataNotFoundException e)
        {
          // do Nothing
        }
      }

      // The instance handlers will do what is necessary
      else if (actionEnumDAO == ActionEnumDAO.CREATE)
      {
      }
      else if (actionEnumDAO == ActionEnumDAO.UPDATE)
      {
      }

      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }

  }
}

class TransactionItemActionSAXImporter extends XMLHandler
{
  /**
   * Keeps track of the text(value) which is parsed.
   */
  private StringBuffer  buffer;

  private ActionEnumDAO actionEnumDAO;

  /**
   * Imports a transaction.
   * 
   * @param attributes
   *          The attributes of the instance tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The XMLHandler in which control was passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   */
  public TransactionItemActionSAXImporter(XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    this.buffer = new StringBuffer();
  }

  /**
   * 
   * @return
   */
  protected ActionEnumDAO getActionEnumDAO()
  {
    return this.actionEnumDAO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_TRANSACTION_ITEM_ACTION_TAG))
    {
      // Remove all white spaces before and after the text
      String attributeValue = buffer.toString().trim();

      this.actionEnumDAO = ActionEnumDAO.valueOf(attributeValue.toUpperCase());

      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    buffer.append(new String(ch, start, length));
  }
}
