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

public class XMLTransactionTags
{
  public static final String XML_IMPORT_LOG                         = "importLog";

  public static final String XML_LOG_SITE                           = "site";

  public static final String XML_LOG_LAST_EXPORT_SEQ                = "lastExportSeq";

  public static final String XML_LOG_SOURCE_SITE                    = "sourceSite";

  public static final String XML_TRANSACTIONS_VALUE                 = "value";

  public static final String XML_TRANSACTIONS_NAME                  = "name";

  public static final String XML_TRANSACTIONS_PROPERTY              = "property";

  public static final String XML_TRANSACTIONS_TAG                   = "Transactions";

  public static final String XML_TRANSACTIONS_SITE_ATTR             = "site";

  public static final String XML_TRANSACTIONS_START_EXPORT_SEQ_ATTR = "startExportSeq";

  public static final String XML_TRANSACTIONS_NUM_OF_TRANS_ATTR     = "numberOfTransactions";

  // Transaction Record Tag
  public static final String XML_TRANSACTION_RECORD_TAG             = "transactionRecord";

  public static final String XML_EXPORT_SEQ_ATTR                    = "exportSeq";

  public static final String XML_TRANSACTION_FILE_ATTR              = "file";

  // Transaction Item Tag
  public static final String XML_TRANSACTION_ITEM_TAG               = "transactionItem";

  public static final String XML_TRANSACTION_ITEM_ID_ATTR           = "id";

  public static final String XML_TRANSACTION_ITEM_SEQ_ATTR          = "seq";

  public static final String XML_TRANSACTION_ITEM_SITE_ATTR         = "site";

  public static final String XML_TRANSACTION_ITEM_ACTION_TAG        = "action";

  public static final String XML_TRANSACTION_ITEM_XMLRECORD_TAG     = "xmlrecord";

  public static final String XML_TRANSACTION_ITEM_IGNORE_SEQ_ATTR   = "ignoreSeq";

}
