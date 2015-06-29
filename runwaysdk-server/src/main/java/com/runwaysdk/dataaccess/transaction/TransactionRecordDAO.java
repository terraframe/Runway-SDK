/**
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
 */
package com.runwaysdk.dataaccess.transaction;

import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.constants.TransactionRecordInfo;
import com.runwaysdk.dataaccess.AttributeLongIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.TransactionItemDAOIF;
import com.runwaysdk.dataaccess.TransactionRecordDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.IdParser;

public class TransactionRecordDAO extends BusinessDAO implements TransactionRecordDAOIF, SpecializedDAOImplementationIF
{
  /**
   *
   */
  private static final long serialVersionUID = -9165829329169211965L;

  /**
   * The depth of the direcory containing the xml file with the transaction items.
   */
  private static final int  DEPTH            = 6;
  
  /**
   * The default constructor, does not set any attributes
   */
  public TransactionRecordDAO()
  {
    super();
  }

  /**
   * Constructs a <code>TransactionRecordDAO</code> from the given map of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public TransactionRecordDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.TransactionRecordDAO#create(java.util.Hashtable, java.util.String, ComponentDTOIF, Map)
   */
  public TransactionRecordDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new TransactionRecordDAO(attributeMap, TransactionRecordInfo.CLASS);
  }

  /**
   * Returns the export sequence of the transaction.
   * @return export sequence of the transaction.
   */
  public Long getExportSequence()
  {
    return ((AttributeLongIF)this.getAttributeIF(TransactionRecordInfo.EXPORT_SEQUENCE)).getLongValue();
  }

  /**
   * Sets the export sequence.  There can absolutely be no gaps in this
   * sequence.
   * @param exportSequence
   */
  public void setExportSequence(Long exportSequence)
  {
    if (exportSequence == null)
    {
      this.getAttribute(TransactionRecordInfo.EXPORT_SEQUENCE).setValue("");
    }
    else
    {
      this.getAttribute(TransactionRecordInfo.EXPORT_SEQUENCE).setValue(exportSequence.toString());
    }
  }

  /**
   * Returns transaction items.  Client must close the iterator when finished.
   * 
   * @return transaction items.
   */
  public OIterator<BusinessDAOIF> getTransactionItems()
  {
    QueryFactory qf = new QueryFactory();

    BusinessDAOQuery transItemQ = qf.businessDAOQuery(TransactionItemInfo.CLASS);
    transItemQ.WHERE(transItemQ.aReference(TransactionItemInfo.TRANSACTION_RECORD).EQ(this));
    transItemQ.ORDER_BY_ASC(transItemQ.getPrimitive(TransactionItemInfo.SEQUENCE));
    
    return transItemQ.getIterator();
  }
  
  /**
   * Returns the transaction item with the given componentid and sequence number, or null
   * if there is none.
   * 
   * @param componentId
   * @param componentSequence
   * 
   * @return transaction items.
   */
  public TransactionItemDAOIF getTransactionItem(String componentId, long componentSequence)
  {
    QueryFactory qf = new QueryFactory();

    BusinessDAOQuery transItemQ = qf.businessDAOQuery(TransactionItemInfo.CLASS);
    transItemQ.WHERE(transItemQ.aReference(TransactionItemInfo.TRANSACTION_RECORD).EQ(this).
               AND(transItemQ.aCharacter(TransactionItemInfo.COMPONENT_ID).EQ(componentId)).
               AND(transItemQ.aLong(TransactionItemInfo.COMPONENT_SEQ).EQ(componentSequence)));
    transItemQ.ORDER_BY_ASC(transItemQ.getPrimitive(TransactionItemInfo.SEQUENCE));
    
    OIterator<BusinessDAOIF> i = transItemQ.getIterator();

    try
    {
      for (BusinessDAOIF businessDAOIF : i)
      {
        return (TransactionItemDAOIF)businessDAOIF;
      }
    }
    finally
    {
      i.close();
    }
    
    return null;
  }
  
  /**
   * Returns a new <code>TransactionRecordDAO</code>.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return instance of <code>TransactionRecordDAO</code>.
   */
  public static TransactionRecordDAO newInstance()
  {
    return (TransactionRecordDAO) BusinessDAO.newInstance(TransactionRecordInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.TransactionRecordDAO#getBusinessDAO()
   */
  public TransactionRecordDAO getBusinessDAO()
  {
    return (TransactionRecordDAO) super.getBusinessDAO();
  }

  /**
   * Returns the <code>TransactionRecordDAO</code> of the current transaction if this method is called
   * within the control flow of a transaction and transaction logging is enabled.  Returns false otherwise.
   *
   * @return <code>TransactionRecordDAO</code> of the current transaction if this method is called
   * within the control flow of a transaction and transaction logging is enabled.  Returns false otherwise.
   */
  public static TransactionRecordDAO getCurrentTransactionRecord()
  {
    return null;
  }

  /**
   * Returns the file path for the xml file that contains the transaction items.
   *
   * @return file path for the xml file that contains the transaction items.
   */
  public String getFilePath()
  {
    // Load the new path
    StringBuffer newPath = new StringBuffer();

    String rootId = IdParser.parseRootFromId(this.getId());
    
    int length = rootId.length();

    for (int i = 0; i < DEPTH; i++)
    {
      int index = length - i - 1;
      newPath.append(rootId.charAt(index) + "/");
    }

    return newPath.toString();
  }
 
  /**
   * Returns true if the entity should be logged, but it does not check to see
   * if the entity's type should be logged, as that should be checked by the client.
   * Rather, certain types of entities are never logged under any condition.
   * 
   * @param entityDAOIF
   *
   * @return true if the entity should be logged, false otherwise.
   */
  public static boolean shouldLogEntity(EntityDAOIF entityDAOIF)
  {
    // AttributeStructs will include the struct information
    if ((entityDAOIF instanceof StructDAO) ||
        (entityDAOIF instanceof LocalTransactionArtifact)
       )
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  
  /**
   * Sets the export sequence attribute of the <code>TransactionRecordDAO</code>.  There may not
   * be any gaps in the export sequence numbers of the transaction records.
   *
   * @param transactionRecordDAO
   */
  @Transaction
  protected static void incrementTransactionRecordSequence(TransactionRecordDAO transactionRecordDAO)
  {
    String nextTransactionSeq = Database.getNextTransactionSequence();
    transactionRecordDAO.getAttribute(TransactionRecordInfo.EXPORT_SEQUENCE).setValue(nextTransactionSeq);
    transactionRecordDAO.apply();
  }

  /**
   * Sets the export sequence attribute of the <code>TransactionRecordDAO</code>s that do 
   * not yet have value.  There may not be any gaps in the export sequence numbers of the 
   * transaction records.
   */
  @Transaction
  public synchronized static void incrementTransactionRecordSequences()
  {
	QueryFactory qf = new QueryFactory();
    BusinessDAOQuery transRecQ = qf.businessDAOQuery(TransactionRecordInfo.CLASS);
    transRecQ.WHERE(transRecQ.aLong(TransactionRecordInfo.EXPORT_SEQUENCE).EQ((Long)null));
    transRecQ.ORDER_BY_ASC(transRecQ.aLong(TransactionRecordInfo.SEQUENCE));
    
    OIterator<BusinessDAOIF> i = transRecQ.getIterator();

    try
    {
      for (BusinessDAOIF businessDAOIF : i)
      {
        BusinessDAO businessDAO = businessDAOIF.getBusinessDAO();
        incrementTransactionRecordSequence((TransactionRecordDAO)businessDAO);
      }
    }
    finally
    {
      i.close();
    }
  }
  
  
}
