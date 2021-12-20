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
import java.util.HashMap;
import java.util.Set;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.TransactionRecordDAOIF;
import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.IdParser;

public class TransactionExportXML
{
  /**
   * Writes the XML code
   */
  private MarkupWriter writer;

  /**
   * The location of the schema file
   */
  private String       schemaLocation;

  /**
   * Location of the transactions xml
   */
  private String       transactionsDirLocation;

  /**
   * Constructor Creates an xml file for a set of transactions
   * 
   * @param _transactionsDirLocation
   * 
   * @param _writer
   * 
   * @param schemaLocation
   *          The location of the schema file
   * 
   * @param _exportOnlyModifiedAttributes
   *          True if only modified attributes should be exported, false
   *          otherwise.
   */
  public TransactionExportXML(String _transactionsDirLocation, MarkupWriter _writer, String _schemaLocation)
  {
    this.transactionsDirLocation = _transactionsDirLocation;
    this.writer = _writer;
    this.schemaLocation = _schemaLocation;
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
   * 
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
  public void open(Long firstExportSequence, Long transactionCount)
  {
    HashMap<String, String> attributes = new HashMap<String, String>();

    attributes.put("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE);
    attributes.put(XMLTransactionTags.XML_TRANSACTIONS_SITE_ATTR, CommonProperties.getDomain());
    attributes.put(XMLTransactionTags.XML_TRANSACTIONS_START_EXPORT_SEQ_ATTR, firstExportSequence.toString());
    attributes.put(XMLTransactionTags.XML_TRANSACTIONS_NUM_OF_TRANS_ATTR, transactionCount.toString());

    this.getWriter().openEscapedTag(XMLTransactionTags.XML_TRANSACTIONS_TAG, attributes);
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
  protected void exportTransaction(TransactionRecordDAOIF transactionRecordDAOIF)
  {
    String filePathLocation = transactionRecordDAOIF.getFilePath();

    String fileLocation = filePathLocation + IdParser.parseRootFromId(transactionRecordDAOIF.getOid()) + ".xml";

    HashMap<String, String> parameters = new HashMap<String, String>();

    parameters.put(XMLTransactionTags.XML_EXPORT_SEQ_ATTR, Long.toString(transactionRecordDAOIF.getExportSequence()));
    parameters.put(XMLTransactionTags.XML_TRANSACTION_FILE_ATTR, fileLocation);

    this.getWriter().writeEmptyEscapedTag(XMLTransactionTags.XML_TRANSACTION_RECORD_TAG, parameters);

    String transactionRecordXMLFileLocation = this.transactionsDirLocation + fileLocation;

    new File(this.transactionsDirLocation + filePathLocation).mkdirs();

    TransactionRecordXML transactionRecordXML = new TransactionRecordXML(new FileMarkupWriter(transactionRecordXMLFileLocation), this.getSchemaLocation(), transactionRecordDAOIF);
    transactionRecordXML.exportTransaction();
  }

  public void writeProperties(HashMap<String, String> properties)
  {
    Set<String> names = properties.keySet();

    for (String name : names)
    {
      String value = properties.get(name);

      HashMap<String, String> attributes = new HashMap<String, String>();

      attributes.put(XMLTransactionTags.XML_TRANSACTIONS_NAME, name);
      attributes.put(XMLTransactionTags.XML_TRANSACTIONS_VALUE, value);

      this.getWriter().writeEmptyEscapedTag(XMLTransactionTags.XML_TRANSACTIONS_PROPERTY, attributes);
    }
  }

  public void writeImportLog()
  {
    this.getWriter().openEscapedTag(XMLTransactionTags.XML_IMPORT_LOG, new HashMap<String, String>());

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(ImportLogInfo.CLASS);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        BusinessDAOIF log = iterator.next();

        String sourceSite = log.getValue(ImportLogInfo.SOURCE_SITE);
        String lastExportSeq = log.getValue(ImportLogInfo.LAST_EXPORT_SEQUENCE);

        HashMap<String, String> attributes = new HashMap<String, String>();

        attributes.put(XMLTransactionTags.XML_LOG_SOURCE_SITE, sourceSite);
        attributes.put(XMLTransactionTags.XML_LOG_LAST_EXPORT_SEQ, lastExportSeq);

        this.getWriter().writeEmptyEscapedTag(XMLTransactionTags.XML_LOG_SITE, attributes);
      }
    }
    finally
    {
      iterator.close();
    }

    this.getWriter().closeTag();
  }

}
