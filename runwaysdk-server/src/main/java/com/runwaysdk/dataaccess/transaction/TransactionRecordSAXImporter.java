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

import java.io.File;
import java.io.FileNotFoundException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.RunwayException;
import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.io.instance.XMLHandlerWithResolver;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;

public class TransactionRecordSAXImporter extends XMLHandlerWithResolver
{
  private String                     importSite;

  private Long                       exportSeq;

  /**
   * Handler representing the last transaction item handler instantiated.
   */
  private TransactionItemSAXImporter lastTransItemHandler;

  /**
   * Constructor, creates a xerces XMLReader, enables schema validation
   * 
   * @param transactionExportXMLFile
   * @param schemaLocation
   * @param resolver
   *          TODO
   * @param importSite
   * @param exportSeq
   * 
   * @throws SAXException
   * @throws FileNotFoundException
   */
  public TransactionRecordSAXImporter(File transactionExportXMLFile, String schemaLocation, IConflictResolver resolver, String importSite, Long exportSeq) throws SAXException
  {
    super(new FileStreamSource(transactionExportXMLFile), schemaLocation, resolver);

    this.importSite = importSite;
    this.exportSeq = exportSeq;
    this.lastTransItemHandler = null;

    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  /**
   * Handler representing the last transaction item handler instantiated, or
   * null if no handler has been instantiated.
   * 
   * @return handler representing the last transaction item handler
   *         instantiated, or null if no handler has been instantiated.
   */
  protected TransactionItemSAXImporter getLastTransactionItemHandler()
  {
    return this.lastTransItemHandler;
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
    if (localName.equals(XMLTransactionTags.XML_TRANSACTION_ITEM_TAG))
    {
      TransactionItemSAXImporter transItemHandler = new TransactionItemSAXImporter(reader, this, manager, this.getResolver(), attributes);
      this.lastTransItemHandler = transItemHandler;

      reader.setContentHandler(transItemHandler);
      reader.setErrorHandler(transItemHandler);
    }
  }

  /**
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_TRANSACTION_RECORD_TAG))
    {
      ImportLogDAO.setLastExportSeqFromSite(this.importSite, exportSeq);
      reader.setContentHandler(this);
      reader.setErrorHandler(this);
    }
  }

  /**
   * 
   * @param transactionExportXMLFile
   * @param schemaLocation
   * @param resolver
   *          TODO
   * @param importSite
   * @param exportSeq
   */
  @Transaction
  public synchronized static void runImport(File transactionExportXMLFile, String schemaLocation, IConflictResolver resolver, String importSite, Long exportSeq)
  {
    TransactionRecordSAXImporter importer = null;

    try
    {
      importer = new TransactionRecordSAXImporter(transactionExportXMLFile, schemaLocation, resolver, importSite, exportSeq);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
    catch (RunwayException ex)
    {      
      if (importer != null && importer.getLastTransactionItemHandler() != null)
      {
        String componentSite = importer.getLastTransactionItemHandler().getComponentSite();
        String devMsg = "Error occured importing a record from site [" + componentSite + "].  The error is: [" + ex.getLocalizedMessage() + "]";
        throw new TransactionImportInvalidItem(devMsg, componentSite, ex.getLocalizedMessage());
      }
      else
      {
        throw ex;
      }
    }
  }

}
