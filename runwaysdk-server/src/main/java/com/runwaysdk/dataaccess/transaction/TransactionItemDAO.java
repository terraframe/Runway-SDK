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

import java.sql.PreparedStatement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeLongIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.TransactionItemDAOIF;
import com.runwaysdk.dataaccess.TransactionRecordDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;

public class TransactionItemDAO extends BusinessDAO implements TransactionItemDAOIF, SpecializedDAOImplementationIF
{
  /**
   *
   */
  private static final long serialVersionUID = 7162073694233671322L;

  /**
   * The default constructor, does not set any attributes
   */
  public TransactionItemDAO()
  {
    super();
  }

  /**
   * Constructs a <code>TransactionItemDAO</code> from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public TransactionItemDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable,
   *      java.util.String, ComponentDTOIF, Map)
   */
  public TransactionItemDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new TransactionItemDAO(attributeMap, TransactionItemInfo.CLASS);
  }

  /**
   * Returns the <code>TransactionRecordDAOIF</code> that is the transaction
   * that this item belongs to.
   * 
   * @return <code>TransactionRecordDAOIF</code> that is the transaction that
   *         this item belongs to.
   */
  public TransactionRecordDAOIF getTransactionRecordDAOIF()
  {
    return (TransactionRecordDAOIF) ( (AttributeReferenceIF) this.getAttributeIF(TransactionItemInfo.TRANSACTION_RECORD) ).dereference();
  }

  /**
   * Sets the <code>TransactionRecordDAOIF</code> that is the transaction that
   * this item belongs to.
   * 
   * @param transactionRecordDAOIF
   *          <code>TransactionRecordDAOIF</code> that is the transaction that
   *          this item belongs to.
   */
  public void setTransactionRecordDAOIF(TransactionRecordDAOIF transactionRecordDAOIF)
  {
    ( (AttributeReference) this.getAttribute(TransactionItemInfo.TRANSACTION_RECORD) ).setValue(transactionRecordDAOIF.getOid());
  }

  /**
   * Returns the oid of the component.
   * 
   * @return oid of the component.
   */
  public String getComponentId()
  {
    return this.getAttributeIF(TransactionItemInfo.COMPONENT_ID).getValue();
  }

  /**
   * Returns the seq of the component.
   * 
   * @return seq of the component.
   */
  public long getComponentSeq()
  {
    return ( (AttributeLongIF) this.getAttributeIF(TransactionItemInfo.COMPONENT_SEQ) ).getLongValue();
  }

  /**
   * Returns the site master of the component.
   * 
   * @return site master of the component.
   */
  public String getComponentSiteMaster()
  {
    return this.getAttributeIF(TransactionItemInfo.COMPONENT_SITE).getValue();
  }

  /**
   * Returns the XML that is the serialized actions that were performed on this
   * object.
   * 
   * @return XML that is the serialized actions that were performed on this
   *         object.
   */
  public String getXMLRecord()
  {
    return this.getAttributeIF(TransactionItemInfo.XML_RECORD).getValue();
  }

  /**
   * Sets the oid of the component.
   * 
   * @param componentId
   */
  public void setComponentId(String componentId)
  {
    this.getAttribute(TransactionItemInfo.COMPONENT_ID).setValue(componentId);
  }

  /**
   * Sets the seq of the component.
   * 
   * @return seq of the component.
   */
  public void setComponentSeq(Long componentSequence)
  {
    this.getAttribute(TransactionItemInfo.COMPONENT_SEQ).setValue(Long.toString(componentSequence));
  }

  /**
   * Sets the site master of the component.
   * 
   * @param componentSite
   */
  public void setComponentSiteMaster(String componentSite)
  {
    this.getAttribute(TransactionItemInfo.COMPONENT_SITE).setValue(componentSite);
  }

  /**
   * Sets the XML that is the serialized actions that were performed on this
   * object.
   * 
   * @param XML
   *          that is the serialized actions that were performed on this object.
   */
  public void setXMLRecord(String xmlRecord)
  {
    this.getAttribute(TransactionItemInfo.XML_RECORD).setValue(xmlRecord);
  }

  /**
   * Returns the action on of this transaction item.
   * 
   * @return action on of this transaction item.
   */
  public ActionEnumDAO getItemAction()
  {
    EnumerationItemDAOIF[] enumerationItemDAOIFs = ( (AttributeEnumerationIF) this.getAttributeIF(TransactionItemInfo.ITEM_ACTION) ).dereference();

    if (enumerationItemDAOIFs.length == 0)
    {
      return null;
    }
    else
    {
      EnumerationItemDAOIF enumerationItemDAOIF = enumerationItemDAOIFs[0];
      String enumName = enumerationItemDAOIF.getAttributeIF(EnumerationMasterInfo.NAME).getValue();
      return ActionEnumDAO.valueOf(enumName);
    }
  }
  
  /**
   * Sets the action on of this transaction item.
   * 
   * @param actionEnumDAO
   *          action on of this transaction item.
   */
  public void setItemAction(ActionEnumDAO actionEnumDAO)
  {
    ( (AttributeEnumeration) this.getAttribute(TransactionItemInfo.ITEM_ACTION) ).setValue(actionEnumDAO.getOid());
  }
  
  public Boolean getIgnoreSequenceNumber()
  {
    AttributeBoolean attribute = (AttributeBoolean)this.getAttribute(TransactionItemInfo.IGNORE_SEQUENCE_NUMBER);
    String value = attribute.getValue();
    
    if(value != null && value.length() > 0)
    {
      return attribute.getBooleanValue();
    }
    
    return null;
  }



  public void setIgnoreSequenceNumber(Boolean ignoreSequenceNumber)
  {
    if(ignoreSequenceNumber != null)
    {
      this.getAttribute(TransactionItemInfo.IGNORE_SEQUENCE_NUMBER).setValue(ignoreSequenceNumber.toString());
    }
    else
    {
      this.getAttribute(TransactionItemInfo.IGNORE_SEQUENCE_NUMBER).setValue("");
    }
  }

  /**
   * Returns the metadata that defines component in the transaction.
   * 
   * @return metadata that defines component in the transaction.
   */
  public MdClassDAOIF getComponentMdClassDAOIF()
  {
    String componentId = this.getComponentId();

    String mdClassRootId = IdParser.parseMdTypeRootIdFromId(componentId);

    return MdClassDAO.getMdClassByRootId(mdClassRootId);
  }

  /**
   * This updates the database with the XML in the XML Record attribute. Use
   * this to update but without incrementing the sequence number. This is
   * necessary to update metadata types since their code artifacts are not
   * updated
   */
  @Transaction
  public void updateXMLInDatabase()
  {
    List<String> columnNames = new LinkedList<String>();
    List<String> prepStmtVars = new LinkedList<String>();
    List<Object> values = new LinkedList<Object>();
    List<String> attributeTypes = new LinkedList<String>();

    // AttributeName
    Attribute attribute = this.getAttribute(TransactionItemInfo.XML_RECORD);
    MdAttributeConcreteDAOIF xmlMdAttrib = attribute.getMdAttribute();
    columnNames.add(xmlMdAttrib.getColumnName());
    attributeTypes.add(xmlMdAttrib.getType());

    // Prepared Statement
    prepStmtVars.add(attribute.getPreparedStatementVar());
    values.add(attribute.getValue());

    MdEntityDAOIF mdEntityDAOIF = this.getMdClassDAO();

    List<PreparedStatement> preparedStatementList = new LinkedList<PreparedStatement>();
    PreparedStatement preparedStmt = Database.buildPreparedSQLUpdateStatement(mdEntityDAOIF.getTableName(), columnNames, prepStmtVars, values, attributeTypes, this.getOid());
    preparedStatementList.add(preparedStmt);
    Database.executeStatementBatch(preparedStatementList);
  }

  /**
   * Returns a new <code>TransactionItemDAO</code>. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of <code>TransactionItemDAO</code>.
   */
  public static TransactionItemDAO newInstance()
  {
    return (TransactionItemDAO) BusinessDAO.newInstance(TransactionItemInfo.CLASS);
  }

  /**
   * @see com.runwaysdk.dataaccess.TransactionItemDAO#getBusinessDAO()
   */
  public TransactionItemDAO getBusinessDAO()
  {
    return (TransactionItemDAO) super.getBusinessDAO();
  }
}
