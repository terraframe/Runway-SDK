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
package com.runwaysdk.dataaccess.transaction;

import java.util.HashMap;

import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.TransactionItemDAOIF;
import com.runwaysdk.dataaccess.TransactionRecordDAOIF;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.query.OIterator;

public class TransactionRecordXML
{
  /**
   * Writes the XML code
   */
  private MarkupWriter             writer;

  /**
   * The location of the schema file
   */
  private String                   schemaLocation; 

  /**
   * Transaction to export
   */
  private TransactionRecordDAOIF   transactionRecordDAOIF;
  
  /**
   * Constructor Creates an xml file for a set of transactions
   *
   * @param _writer
   *
   * @param schemaLocation
   *            The location of the schema file
   *
   * @param _exportOnlyModifiedAttributes
   *             True if only modified attributes should be exported, false otherwise.
   *
   * @param transactionRecordDAOIF transaction export
   */
  public TransactionRecordXML(MarkupWriter _writer, String _schemaLocation, TransactionRecordDAOIF _transactionRecordDAOIF)
  {
    this.writer = _writer;
    this.schemaLocation = _schemaLocation;
    this.transactionRecordDAOIF = _transactionRecordDAOIF;
  }

  /**
   * Returns a reference to the markup writer.
   *
   * @return reference to the markup writer.
   */
  protected MarkupWriter getWriter()
  {
    return this.writer;
  }

  /**
   * Returns the schema location.
   * @return the schema location.
   */
  protected String getSchemaLocation()
  {
    return this.schemaLocation;
  }

  /**
   * 
   * @param firstExportSequence
   */
  public void open()
  {
    HashMap<String, String> attributes = new HashMap<String, String>();

    attributes.put("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE);
    attributes.put(XMLTransactionTags.XML_EXPORT_SEQ_ATTR, this.transactionRecordDAOIF.getExportSequence().toString());
    
    this.getWriter().openEscapedTag(XMLTransactionTags.XML_TRANSACTION_RECORD_TAG, attributes);
  }

  public void close()
  {
    this.getWriter().closeTag();
  }
  
  /**
   * Exports a <code>TransactionRecordDAOIF</code>.
   *
   * @pre transactionRecordDAO != null
   * @pre transactionRecordDAO.getExportSequence() != null
   * @pre !transactionRecordDAO.getExportSequence().trim().equals("");
   *
   * @param transactionRecordDAOIF
   */
  protected void exportTransaction()
  {
    this.open();
       
    OIterator<BusinessDAOIF> i = transactionRecordDAOIF.getTransactionItems();
    
    try
    {
       for(BusinessDAOIF businessDAOIF : i)
       {
         TransactionItemDAOIF transactionItemDAOIF = (TransactionItemDAOIF)businessDAOIF;
         this.exportTransactionItem(transactionItemDAOIF);
       }
    }
    finally
    {
      i.close();
    }
    
    this.close();
  }

  /**
   * Exports a <code>TransactionItemDAOIF</code>.
   *
   * @pre transactionItemDAOIF != null
   *
   * @param transactionItemDAOIF
   */
  protected void exportTransactionItem(TransactionItemDAOIF transactionItemDAOIF)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put(XMLTransactionTags.XML_TRANSACTION_ITEM_ID_ATTR, transactionItemDAOIF.getComponentId());
    parameters.put(XMLTransactionTags.XML_TRANSACTION_ITEM_SEQ_ATTR, Long.toString(transactionItemDAOIF.getComponentSeq()));
    parameters.put(XMLTransactionTags.XML_TRANSACTION_ITEM_SITE_ATTR, transactionItemDAOIF.getComponentSiteMaster());
    
    if(transactionItemDAOIF.getIgnoreSequenceNumber() != null)
    {
      parameters.put(XMLTransactionTags.XML_TRANSACTION_ITEM_IGNORE_SEQ_ATTR, transactionItemDAOIF.getIgnoreSequenceNumber().toString());      
    }

    this.getWriter().openEscapedTag(XMLTransactionTags.XML_TRANSACTION_ITEM_TAG, parameters);

    this.getWriter().writeTagSingleValue(XMLTransactionTags.XML_TRANSACTION_ITEM_ACTION_TAG, transactionItemDAOIF.getItemAction().name());
    
    this.getWriter().openEscapedTag(XMLTransactionTags.XML_TRANSACTION_ITEM_XMLRECORD_TAG, new HashMap<String, String>());
    this.getWriter().writeValue(transactionItemDAOIF.getXMLRecord());
    this.getWriter().closeTag();
    
    this.getWriter().closeTag(); 
  }  
}
