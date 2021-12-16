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
package com.runwaysdk.dataaccess.metadata;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.IndexAttributeDAO;
import com.runwaysdk.dataaccess.IndexAttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Session;

public class MdIndexDAO extends MetadataDAO implements MdIndexDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 8338430295850378894L;

  /**
   *
   *
   */
  public MdIndexDAO()
  {
    super();
  }

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    String signature = "Name:"+this.getIndexName()+" Attributes[";

    boolean firstIteration = true;
    for (MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF : getIndexedAttributes())
    {
      if (!firstIteration)
      {
        signature +=", ";
      }
      else
      {
        firstIteration = false;
      }
      signature += mdAttributeConcreteDAOIF.definesAttribute();
    }

    signature += "]";

    return signature;
  }

  /**
   *
   * @param attributeMap
   * @param type
   */
  public MdIndexDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdIndexDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdIndexDAO(attributeMap, MdIndexInfo.CLASS);
  }

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ((AttributeLocal)this.getAttributeIF(MdIndexInfo.DISPLAY_LABEL)).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ((AttributeLocalIF)this.getAttributeIF(MdIndexInfo.DISPLAY_LABEL)).getLocalValues();
  }
  
  /**
   * Returns true if the index is unique, false otherwise.
   *
   * @return true if the index is unique, false otherwise.
   */
  public boolean isUnique()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdIndexInfo.UNIQUE) ).getBooleanValue();
  }

  /**
   * Returns true if the index exists in the database, false otherwise.
   *
   * @return true if the index exists in the database, false otherwise.
   */
  public boolean existsInDatabase()
  {
    int attrSize = Database.getGroupIndexAttributes(this.definesIndexForEntity().getTableName(),
        this.getIndexName()).size();
    if (attrSize > 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns <code>MdAttributeBooleanInfo.TRUE</code> if the index is unique,
   * <code>MdAttributeBooleanInfo.FALSE</code> otherwise.
   *
   * @return <code>MdAttributeBooleanInfo.TRUE</code> if the index is unique,
   *         <code>MdAttributeBooleanInfo.FALSE</code> otherwise.
   */
  public String getUniqueValue()
  {
    return this.getAttributeIF(MdIndexInfo.UNIQUE).getValue();
  }

  /**
   * Returns the MdEntityIF that this index dfines an index on.
   */
  public MdEntityDAOIF definesIndexForEntity()
  {
    return (MdEntityDAOIF) ( (AttributeReferenceIF) this.getAttributeIF(MdIndexInfo.MD_ENTITY) )
        .dereference();
  }

  /**
   * Returns the name the index that is defined in the database.
   */
  public String getIndexName()
  {
    setIndexName();

    return this.getAttributeIF(MdIndexInfo.INDEX_NAME).getValue();
  }

  private void setIndexName()
  {
    String value = this.getAttributeIF(MdIndexInfo.INDEX_NAME).getValue();

    if (value == null || value.equals(""))
    {
      this.getAttribute(MdIndexInfo.INDEX_NAME).setValue(ServerIDGenerator.generateUniqueDatabaseIdentifier());
    }
  }

  /**
   * Returns true if this index is applied to the database, false otherwise.
   * This is a boolean flag stored on this object. It does not actually check
   * the database.
   *
   * @return true if this index is applied to the database, false otherwise.
   */
  public boolean isActive()
  {
    return ( (AttributeBooleanIF) this.getAttributeIF(MdIndexInfo.ACTIVE) ).getBooleanValue();
  }

  public void setActive(boolean isActive)
  {
    ( (AttributeBoolean) this.getAttribute(MdIndexInfo.ACTIVE) ).setValue(isActive);
  }

  /**
   * Returns the metadata that defines each attribute in the index.
   *
   * @return metadata that defines each attribute in the index.
   */
  public List<MdAttributeConcreteDAOIF> getIndexedAttributes()
  {
    List<MdAttributeConcreteDAOIF> mdAttributeIFList = new LinkedList<MdAttributeConcreteDAOIF>();

    List<RelationshipDAOIF> relationshipIFList = this.getChildren(IndexAttributeInfo.CLASS);

    Collections.sort(relationshipIFList,
        new Comparator<RelationshipDAOIF>()
        {
          public int compare(RelationshipDAOIF o1, RelationshipDAOIF o2)
          {
            IndexAttributeDAO ia1 = (IndexAttributeDAO)o1;
            IndexAttributeDAO ia2 = (IndexAttributeDAO)o2;

            return ia1.getIndexOrder().compareTo(ia2.getIndexOrder());
          }

        }
    );

    for (RelationshipDAOIF relationshipIF : relationshipIFList)
    {
      IndexAttributeIF indexAttributeRelIF = (IndexAttributeIF) relationshipIF;
      mdAttributeIFList.add(indexAttributeRelIF.getMdAttributeDAO());
    }

    return mdAttributeIFList;
  }

  /**
   * Returns the metadata that defines each attribute in the index.
   *
   * @return metadata that defines each attribute in the index.
   */
  public List<IndexAttributeIF> getIndexedAttributeDAOs()
  {
    List<IndexAttributeIF> indexAttributeDAOIFList = new LinkedList<IndexAttributeIF>();

    List<RelationshipDAOIF> relationshipIFList = this.getChildren(IndexAttributeInfo.CLASS);

    Collections.sort(relationshipIFList,
        new Comparator<RelationshipDAOIF>()
        {
          public int compare(RelationshipDAOIF o1, RelationshipDAOIF o2)
          {
            IndexAttributeDAO ia1 = (IndexAttributeDAO)o1;
            IndexAttributeDAO ia2 = (IndexAttributeDAO)o2;

            return ia1.getIndexOrder().compareTo(ia2.getIndexOrder());
          }

        }
    );

    for (RelationshipDAOIF relationshipIF : relationshipIFList)
    {
      IndexAttributeIF indexAttributeRelIF = (IndexAttributeIF) relationshipIF;
      indexAttributeDAOIFList.add(indexAttributeRelIF);
    }

    return indexAttributeDAOIFList;
  }
  
  public String apply()
  {
    if (this.isNew())
    {
      setIndexName();
    }

    boolean isAlreadyAppliedToDB = this.isAppliedToDB();

    List<String> columnNames = this.getColumnNames();

    String[] attributeNameArray = new String[columnNames.size()];
    columnNames.toArray(attributeNameArray);

    String key = buildKey(this.definesIndexForEntity().definesType(), attributeNameArray);
    this.setKey(key);
    
    this.checkDuplicateKey();
    
    String oid = super.apply();
    
    // This relationship should not be created, as it will be imported
    if (this.isNew() && !isAlreadyAppliedToDB)
    {
      if (!this.isImport())
      {
        MdEntityDAOIF mdEntityIF = this.definesIndexForEntity();
        RelationshipDAO relationshipDAO = this.addParent(mdEntityIF, RelationshipTypes.ENTITY_INDEX.getType());
        relationshipDAO.setKey(this.getKey());
        relationshipDAO.apply();

        this.setActive(false);
      }
    }
    else
    {
      Attribute attributeKey = this.getAttribute(MdIndexInfo.KEY);
      if (attributeKey.isModified())
      {
        List<RelationshipDAOIF> entityIndexRelList = this.getParents(RelationshipTypes.ENTITY_INDEX.getType());
        for (RelationshipDAOIF relationshipDAOIF : entityIndexRelList)
        {
          RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
          relationshipDAO.setKey(attributeKey.getValue());
          relationshipDAO.apply();
        }
        
        List<IndexAttributeIF> indexAttrRelList = getIndexedAttributeDAOs();
        for (IndexAttributeIF indexAttributeDAOIF : indexAttrRelList)
        {
          // This will update the key
          IndexAttributeDAO indexAttributeDAO = (IndexAttributeDAO)indexAttributeDAOIF.getRelationshipDAO();
          indexAttributeDAO.apply();
        }
      }
    }

    // If the active flag has been changed, then add or drop the index in the
    // database
    if (this.getAttributeIF(MdIndexInfo.ACTIVE).isModified())
    {
      boolean indexExistsInDatabase = Database.groupAttributeIndexExists(this.definesIndexForEntity()
          .getTableName(), this.getIndexName());

      if (this.isActive())
      {
        if (!indexExistsInDatabase)
        {
          if (columnNames.size() > 0)
          {
            this.buildDatabaseIndex(columnNames);
          }
          else
          {
            MdEntityDAOIF mdEntityIF = this.definesIndexForEntity();
            String errMsg = "Index [" + this.getIndexName() + "] on type [" + mdEntityIF.definesType()
                + "] could not be applied to the database because no attributes are defined on it.";
            throw new NoAttributeOnIndexException(errMsg, this, this.definesIndexForEntity());
          }
        }
      }
      // Index is not active
      else
      {
        if (indexExistsInDatabase)
        {
          this.dropDatabaseIndex(columnNames, true);
        }
      }
    }

    return oid;
  }


  /**
   * Throws {@link IdenticalIndexException} if an index exists with an identical key name,
   * meaning an index is already defined on the entity with the given column names
   */
  private void checkDuplicateKey()
  {
    QueryFactory queryFactory = new QueryFactory();

    BusinessDAOQuery query = queryFactory.businessDAOQuery(MdIndexInfo.CLASS);

    query.WHERE(query.aCharacter(MdIndexInfo.KEY).EQ(this.getAttributeIF(MdIndexInfo.KEY).getValue()).
        AND(query.oid().NE(this.getOid())));

    OIterator<BusinessDAOIF> i = query.getIterator();

    try
    {
      if (i.hasNext())
      {
        MdEntityDAOIF mdEntityDAOIF = this.definesIndexForEntity();

        List<String> attributeColumnNames = this.getColumnNames();

        String attributeString = "";

        boolean firstIteration = true;
        for (String attributeName : attributeColumnNames)
        {
          if (!firstIteration)
          {
            attributeString += ", ";
          }

          attributeString += attributeName;

          firstIteration = false;
        }

        String errMsg = "Index ["+this.getDisplayLabel(Session.getCurrentLocale())+"] on entity ["+mdEntityDAOIF.definesType()+"] is invalid. "+
        "An index is already defined on entity ["+mdEntityDAOIF.definesType()+"] with attributes ["+attributeString+"].";
        throw new IdenticalIndexException(errMsg, mdEntityDAOIF, this, attributeString);
      }
    }
    finally
    {
      i.close();
    }
  }

  public static String buildKey(String definingType, String[] columnNames)
  {
    String key = definingType+".Index";

    for (String columnName : columnNames)
    {
      key += "."+columnName;
    }

    return key;
  }

  /**
   *
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   *
   */
  public void delete(DeleteContext context)
  {
    // Drop index:
    // Index is actually dropped when the RelationshipTypes.INDEX_ATTRIBUTE
    // attributes are deleted.

    for (IndexAttributeIF indexAttributeIF : this.getIndexedAttributeDAOs())
    {
      indexAttributeIF.getRelationshipDAO().deleteFromMdIndex(this, context);
    }
    
    super.delete(context);
  }

  /**
   * Adds the given attribute to the index.
   *
   * @param mdAttributeIF
   *          some attribute
   */
  public void addAttribute(MdAttributeDAOIF mdAttributeIF, int indexOrder)
  {
    IndexAttributeDAO indexAttributeDAO = (IndexAttributeDAO)this.addChild(mdAttributeIF, IndexAttributeInfo.CLASS);

    indexAttributeDAO.getAttribute(IndexAttributeInfo.INDEX_ORDER).setValue(Integer.toString(indexOrder));

    // This is actually an instance of IndexAttribute. The apply method will
    // call dropIndex and buildIndex.
    indexAttributeDAO.apply();
  }

  /**
   * Removes the given attribute to the index.
   *
   * @param mdAttributeIF
   *          some attribute
   */
  public void removeAttribute(MdAttributeConcreteDAOIF mdAttributeIF)
  {
    List<RelationshipDAOIF> relationshipDAOifList = RelationshipDAO.get(this, mdAttributeIF, IndexAttributeInfo.CLASS);

    if (relationshipDAOifList.size() > 0)
    {
      RelationshipDAO relationshipDAO = relationshipDAOifList.get(0).getRelationshipDAO();
      // This is actually an instance of IndexAttribute. The apply method will
      // call dropIndex and buildIndex.
      relationshipDAO.delete();
    }
  }

  /**
   * Builds the database index.
   *
   */
  @Transaction
  public void buildIndex()
  {
    List<String> columnNames = this.getColumnNames();

    if (columnNames.size() > 0)
    {
      this.buildDatabaseIndex(columnNames);
      this.setActive(true);
      this.save(true);
    }
  }

  protected void buildDatabaseIndex()
  {
    List<String> columnNames = this.getColumnNames();

    if (columnNames.size() > 0)
    {
      this.buildDatabaseIndex(columnNames);
    }
  }

  private void buildDatabaseIndex(List<String> columnNames)
  {
    MdEntityDAOIF mdEntityIF = this.definesIndexForEntity();
    String tableName = mdEntityIF.getTableName();
    String indexName = this.getIndexName();
    boolean isUnique = this.isUnique();

    Database.addGroupAttributeIndex(tableName, indexName, columnNames, isUnique);
  }

  /**
   * Drops the database index.
   *
   * @param delete
   *          true if this index is being deleted in this transaction, false
   *          otherwise. The index may be deleted if an attribute is being added
   *          to it. In that case, the value should be <code>false</code>.
   */
  public void dropIndex(boolean delete)
  {
    List<String> columnNames = this.getColumnNames();

    if (columnNames.size() > 0)
    {
      this.dropDatabaseIndex(columnNames, delete);
      this.setActive(false);
      this.save(true);
    }
  }

  protected void dropDatabaseIndex(boolean delete)
  {
    List<String> columnNames = this.getColumnNames();

    if (columnNames.size() > 0)
    {
      this.dropDatabaseIndex(columnNames, delete);
    }
  }

  private void dropDatabaseIndex(List<String> columnNames, boolean delete)
  {
    MdEntityDAOIF mdEntityIF = this.definesIndexForEntity();
    String tableName = mdEntityIF.getTableName();
    String indexName = this.getIndexName();
    boolean isUnique = this.isUnique();

    Database.dropGroupAttributeIndex(tableName, indexName, columnNames, isUnique, delete);
  }

  /**
   *
   * @return
   */
  private List<String> getColumnNames()
  {
    List<MdAttributeConcreteDAOIF> mdAttributeIFList = this.getIndexedAttributes();

    List<String> attributeColumnNames = new LinkedList<String>();

    for (MdAttributeConcreteDAOIF mdAttributeIF : mdAttributeIFList)
    {
      attributeColumnNames.add(mdAttributeIF.getColumnName());
    }
    return attributeColumnNames;
  }

  /**
   * Returns the MdIndexIF that defines the database index of the given name.
   *
   * <br/>
   * <b>Precondition:</b> indexName != null <br/>
   * <b>Precondition:</b> !indexName.trim().equals("") <br/>
   * <b>Postcondition:</b> MdIndexIF that defines the database index of the
   * given name.
   *
   * @param indexName
   *          indexName
   * @return MdIndexIF that defines the database index of the given name.
   */
  public static MdIndexDAOIF getMdIndexDAO(String indexName)
  {
    MdIndexDAOIF mdIndexIF = ObjectCache.getMdIndexDAO(indexName);

    if (mdIndexIF == null)
    {
      String error = "Metadata not found that defines database index [" + indexName + "]";

      // Feed in the MdEntity for MdEntity. Yes, it's self-describing.
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdIndexInfo.CLASS));
    }

    return mdIndexIF;
  }

  /**
   * Returns an instance of MdIndexIF with the given oid.
   *
   * @param oid
   * @return an instance of MdIndexIF with the given oid.
   */
  public static MdIndexDAOIF get(String oid)
  {
    return (MdIndexDAOIF) BusinessDAO.get(oid);
  }

  /**
   * MdIndex.
   *
   * @return MdIndex.
   */
  public MdIndexDAO getBusinessDAO()
  {
    return (MdIndexDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new instance of MdIndex.
   *
   * @return new instance of MdIndex.
   */
  public static MdIndexDAO newInstance()
  {
    return (MdIndexDAO) BusinessDAO.newInstance(MdIndexInfo.CLASS);
  }
}
