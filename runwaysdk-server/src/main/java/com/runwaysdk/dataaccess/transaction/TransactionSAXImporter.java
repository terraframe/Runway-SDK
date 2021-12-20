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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.io.instance.XMLHandlerWithResolver;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;

public class TransactionSAXImporter extends XMLHandlerWithResolver
{
  private List<ITaskListener>     taskListeners;

  private List<IPropertyListener> propertyListeners;

  private String                  transactionsDirLocation;

  private String                  importSite;

  private Long                    numberOfTransactions;

  private Long                    transactionCount = 0L;

  private boolean                 isFirstTransaction;

  /**
   * @param exportXMLFile
   *          Location of the file to import
   * @param schemaLocation
   *          Location of the xsd schema defining the import
   * @param resolver
   *          Resolver to use when conflicts arise during the import
   * @throws SAXException
   * @throws FileNotFoundException 
   */
  public TransactionSAXImporter(File exportXMLFile, String schemaLocation, IConflictResolver resolver) throws SAXException
  {
    super(new FileStreamSource(exportXMLFile), schemaLocation, resolver);

    this.taskListeners = new LinkedList<ITaskListener>();
    this.propertyListeners = new LinkedList<IPropertyListener>();
    this.transactionsDirLocation = exportXMLFile.getParent() + File.separator + TransactionExportManager.TRANSACTIONS_DIR_NAME + File.separator;
    this.isFirstTransaction = true;

    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  public void addTaskListener(ITaskListener listener)
  {
    this.taskListeners.add(listener);
  }

  public void removeTaskListener(ITaskListener listener)
  {
    this.taskListeners.remove(listener);
  }

  public void addPropertyListener(IPropertyListener listener)
  {
    this.propertyListeners.add(listener);
  }

  public void removePropertyListener(IPropertyListener listener)
  {
    this.propertyListeners.remove(listener);
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
    if (localName.equals(XMLTransactionTags.XML_TRANSACTIONS_TAG))
    {
      this.importSite = attributes.getValue(XMLTransactionTags.XML_TRANSACTIONS_SITE_ATTR);
      this.numberOfTransactions = Long.parseLong(attributes.getValue(XMLTransactionTags.XML_TRANSACTIONS_NUM_OF_TRANS_ATTR));
      Long startingExportSequence = Long.parseLong(attributes.getValue(XMLTransactionTags.XML_TRANSACTIONS_START_EXPORT_SEQ_ATTR));

      Long lastSequenceImported = ImportLogDAO.getLastExportSeqFromSite(this.importSite);

      // There cannot be any gap between the imported sequence number and what
      // has already been imported
      // for this site.
      if (startingExportSequence > lastSequenceImported + 1)
      {
        Long neededImportSequence;

        if (lastSequenceImported == 0)
        {
          neededImportSequence = 0L;
        }
        else
        {
          neededImportSequence = lastSequenceImported + 1;
        }

        String errMsg = "There cannot be a gap in sequence numbers when importing from another site.  " + "The last imported sequence number from site [" + this.importSite + "] was [" + lastSequenceImported + "], " + "but the current import starts with sequence number [" + startingExportSequence + "].  " + "Please import from site [" + this.importSite + "] starting with sequence number [" + neededImportSequence + "].";
        throw new SynchronizationSequenceGapException(errMsg, this.importSite, lastSequenceImported, startingExportSequence, neededImportSequence);
      }
    }
    else if (localName.equals(XMLTransactionTags.XML_TRANSACTIONS_PROPERTY))
    {
      String name = attributes.getValue(XMLTransactionTags.XML_TRANSACTIONS_NAME);
      String value = attributes.getValue(XMLTransactionTags.XML_TRANSACTIONS_VALUE);

      this.firePropertyEventEvent(name, value);
    }
    else if (localName.equals(XMLTransactionTags.XML_IMPORT_LOG))
    {
      ImportLogHandler handler = new ImportLogHandler(attributes, this.reader, this, this.manager);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    else if (localName.equals(XMLTransactionTags.XML_TRANSACTION_RECORD_TAG))
    {
      if (this.isFirstTransaction)
      {
        this.firePropertiesFinishedEvent();

        this.isFirstTransaction = false;
      }

      Long exportSeq = Long.parseLong(attributes.getValue(XMLTransactionTags.XML_EXPORT_SEQ_ATTR));

      Long lastSequenceImported = ImportLogDAO.getLastExportSeqFromSite(this.importSite);

      this.transactionCount += 1;

      // skip if this number is less than what has already been imported from
      // the site
      if (exportSeq > lastSequenceImported)
      {
        String transactionRecordfileLocation = this.transactionsDirLocation + attributes.getValue(XMLTransactionTags.XML_TRANSACTION_FILE_ATTR);

        TransactionRecordSAXImporter.runImport(new File(transactionRecordfileLocation), CommonProperties.getTransactionRecordXMLschemaLocation(), this.getResolver(), this.importSite, exportSeq);
      }

      Integer currentPercentage = (int) ( ( (double) this.transactionCount / (double) this.numberOfTransactions ) * 100 );

      this.fireProgressEvent(currentPercentage);
    }
  }

  private void firePropertyEventEvent(String name, String value)
  {
    for (IPropertyListener listener : propertyListeners)
    {
      listener.handleProperty(name, value);
    }
  }

  private void firePropertiesFinishedEvent()
  {
    for (IPropertyListener listener : propertyListeners)
    {
      listener.handlePropertiesFinished();
    }
  }

  private void fireProgressEvent(int percent)
  {
    for (ITaskListener listener : taskListeners)
    {
      listener.taskProgress(percent);
    }
  }

  /**
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_TRANSACTIONS_TAG) || localName.equals(XMLTransactionTags.XML_TRANSACTION_RECORD_TAG))
    {
      reader.setContentHandler(this);
      reader.setErrorHandler(this);
    }
  }

  @Override
  public void endDocument() throws SAXException
  {
  }

  /**
   * 
   * @param exportXMLFile
   * @param schemaLocation
   * @param resolver
   *          TODO
   * @param taskListeners
   * @param propertyListeners
   *          TODO
   */
  public synchronized static void runImport(File exportXMLFile, String schemaLocation, IConflictResolver resolver, List<ITaskListener> taskListeners, List<IPropertyListener> propertyListeners)
  {
    try
    {
      TransactionSAXImporter importer = new TransactionSAXImporter(exportXMLFile, schemaLocation, resolver);

      for (ITaskListener listener : taskListeners)
      {
        importer.addTaskListener(listener);
      }

      for (IPropertyListener listener : propertyListeners)
      {
        importer.addPropertyListener(listener);
      }

      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

}
