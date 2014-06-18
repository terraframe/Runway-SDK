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
package com.runwaysdk.dataaccess.transaction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.ImportLogInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class ImportLogHandler extends XMLHandler
{

  public ImportLogHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);
  }

  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_LOG_SITE))
    {
      String sourceSite = attributes.getValue(XMLTransactionTags.XML_LOG_SOURCE_SITE);
      String lastExportSeq = attributes.getValue(XMLTransactionTags.XML_LOG_LAST_EXPORT_SEQ);

      importLog(sourceSite, lastExportSeq);
    }
  }

  private void importLog(String sourceSite, String lastExportSeq)
  {
    BusinessDAOIF importLogIF = getImportLog(sourceSite);

    if (importLogIF == null)
    {
      BusinessDAO importLog = BusinessDAO.newInstance(ImportLogInfo.CLASS);
      importLog.setValue(ImportLogInfo.SOURCE_SITE, sourceSite);
      importLog.setValue(ImportLogInfo.LAST_EXPORT_SEQUENCE, lastExportSeq);
      importLog.apply();
    }
    else
    {
      long exportSeq = Long.parseLong(lastExportSeq);
      long existingExportSequence = Long.parseLong(importLogIF.getValue(ImportLogInfo.LAST_EXPORT_SEQUENCE));

      // Update the existing import log if and only if the incoming last export
      // sequence number is greater than the existing last export sequence
      // number
      if (exportSeq > existingExportSequence)
      {
        BusinessDAO importLog = importLogIF.getBusinessDAO();
        importLog.setValue(ImportLogInfo.LAST_EXPORT_SEQUENCE, lastExportSeq);
        importLog.apply();
      }
    }
  }

  private BusinessDAOIF getImportLog(String sourceSite)
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(ImportLogInfo.CLASS);
    query.WHERE(query.get(ImportLogInfo.SOURCE_SITE).EQ(sourceSite));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
    }
    finally
    {
      iterator.close();
    }

    return null;
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (localName.equals(XMLTransactionTags.XML_IMPORT_LOG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

}
