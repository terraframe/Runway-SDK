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
package com.runwaysdk.dataaccess.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.JavaArtifactMdEntityCommand;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.CacheNoneStrategy;
import com.runwaysdk.dataaccess.cache.CacheStrategy;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.FileIO;

public abstract class MdEntityDAO extends MdClassDAO implements MdEntityDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -7249167599355315984L;

  private static final int  NAME_LIMIT       = 96;

  private static final int  FULL_LIMIT       = 200;

  /**
   * The default constructor, does not set any attributes
   */
  public MdEntityDAO()
  {
    super();
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdTypeDAO
   */
  public MdEntityDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the name of the table used to store instances of the class that
   * this object defines.
   * 
   * @return name of the table used to store instances of the class that this
   *         object defines.
   */
  public String getTableName()
  {
    return this.getAttributeIF(MdEntityInfo.TABLE_NAME).getValue();
  }

  /**
   * Sets the name of the table.
   * 
   */
  public void setTableName(String tableName)
  {
    this.getAttribute(MdEntityInfo.TABLE_NAME).setValue(tableName);
  }

  /**
   * Returns the DataAccessIF object that specifies the cache algorithm used by
   * this type.
   * 
   * @return DataAccessIF object that specifies the cache algorithm used by this
   *         type.
   */
  public abstract BusinessDAOIF getCacheAlgorithm();

  /**
   * Returns true if objects defined by this type are not cached in the global
   * cache, false otherwise.
   * 
   * @return true if objects defined by this type are not cached in the global
   *         cache, false otherwise.
   */
  public boolean isNotCached()
  {
    CacheStrategy cacheStrategy = ObjectCache.getTypeCollection(this.definesType());

    if (cacheStrategy instanceof CacheNoneStrategy)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns the integer value of cachSize, which is the optional independent
   * size of type. Because cacheSize is optional, if this method is called
   * without having a valid integer cache size specified, then 0 is returned.
   * 
   * @return the integer value of cacheSize
   */
  public int getCacheSize()
  {
    // if this is empty, set the value to 0
    String cacheSize = this.getAttributeIF(MdBusinessInfo.CACHE_SIZE).getValue();
    if (cacheSize.equals(""))
    {
      return 0;
    }
    else
    {
      return Integer.parseInt(cacheSize);
    }
  }

  /**
   * Returns all unique indexes on this type.
   * 
   * @return all unique indexes on this type.
   */
  public List<MdIndexDAOIF> getUniqueIndexes()
  {
    List<MdIndexDAOIF> mdIndexIFList = new LinkedList<MdIndexDAOIF>();

    List<RelationshipDAOIF> relationshipIFList = this.getChildren(RelationshipTypes.ENTITY_INDEX.getType());

    for (RelationshipDAOIF relationshipIF : relationshipIFList)
    {
      MdIndexDAOIF mdIndexIF = (MdIndexDAOIF) relationshipIF.getChild();
      if (mdIndexIF.isUnique())
      {
        mdIndexIFList.add((MdIndexDAOIF) relationshipIF.getChild());
      }
    }

    return mdIndexIFList;
  }

  /**
   * Returns all indexes on this type.
   * 
   * @return all indexes on this type.
   */
  public List<MdIndexDAOIF> getIndexes()
  {
    List<MdIndexDAOIF> mdIndexIFList = new LinkedList<MdIndexDAOIF>();

    List<RelationshipDAOIF> relationshipIFList = this.getChildren(RelationshipTypes.ENTITY_INDEX.getType());

    for (RelationshipDAOIF relationshipDAOIF : relationshipIFList)
    {
      mdIndexIFList.add((MdIndexDAOIF) relationshipDAOIF.getChild());
    }

    return mdIndexIFList;
  }

  /**
   * @return TRUE if IDs that are generated are deterministic, FALSE otherwise.
   *         Deterministic IDs are generated from a hash of the KeyName value.
   */
  public boolean hasDeterministicIds()
  {
    AttributeBooleanIF attrBool = (AttributeBooleanIF) this.getAttributeIF(MdEntityInfo.HAS_DETERMINISTIC_IDS);

    if (attrBool.getBooleanValue())
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns an array of MdEntityDAOIF that defines immediate subentites of this
   * entity.
   * 
   * @return an array of MdEntityDAOIF that defines immediate subentites of this
   *         entity.
   */
  public abstract List<? extends MdEntityDAOIF> getSubClasses();

  /**
   * Returns a list of MdClassIF objects that represent classes that are
   * subclasses of the given class, including this class, including all
   * recursive entities.
   * 
   * @return list of MdClassIF objects that represent classes that are
   *         subclasses of the given class, including this class, including all
   *         recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdEntityDAOIF> getAllSubClasses()
  {
    return (List<? extends MdEntityDAOIF>) super.getAllSubClasses();
  }

  /**
   * Returns an MdEntityDAOIF representing the superclass of this class, or null
   * if this class is basic.
   * 
   * @return an MdEntityDAOIF representing the superclass of this class, or null
   *         if the class is basic.
   */
  public abstract MdEntityDAOIF getSuperClass();

  /**
   * Returns a list of MdEntityDAOIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdEntityDAOIF objects that are subclasses of the given
   *         entity. Only non abstract entities are returned (i.e. entities that
   *         can be instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdEntityDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdEntityDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * Returns a list of MdEntityDAOIF instances representing every parent of this
   * MdEntityDAO partaking in an inheritance relationship, including this class.
   * 
   * @return a list of MdEntityDAOIF instances that are parents of this class,
   *         including this class.
   */
  public abstract List<? extends MdEntityDAOIF> getSuperClasses();

  /**
   * Returns the MdEntityDAOIF that is the root of the hierarchy that this type
   * belongs to. returns a reference to inself if it is the root.
   * 
   * @return MdEntityDAOIF that is the root of the hierarchy that this type
   *         belongs to. returns a reference to inself if it is the root.
   */
  public MdEntityDAOIF getRootMdClassDAO()
  {
    return (MdEntityDAOIF) super.getRootMdClassDAO();
  }

  /**
   * Returns a sorted list of <code>MdAttributeConcreteDAOIF</code> objects that
   * this <code>MdEntityDAOIF</code> defines.
   * 
   * @return an List of <code>MdAttributeConcreteDAOIF</code> objects that this
   *         <code>MdEntityDAOIF</code> defines.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdAttributeConcreteDAOIF> definesAttributes()
  {
    return (List<? extends MdAttributeConcreteDAOIF>) super.definesAttributes();
  }

  /**
   * Returns a sorted list of <code>MdAttributeConcreteDAOIF</code> objects that
   * this <code>MdEntityDAOIF</code> defines. The list is sorted by the
   * alphabetical order of the attribute names
   * 
   * @return an List of <code>MdAttributeConcreteDAOIF</code> objects that this
   *         <code>MdEntityDAOIF</code> defines.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdAttributeConcreteDAOIF> definesAttributesOrdered()
  {
    return (List<? extends MdAttributeConcreteDAOIF>) super.definesAttributesOrdered();
  }

  /**
   * Returns the MdAttribute that defines the given attribute for the this
   * entity. This method only works if the attribute is explicitly defined by
   * the this. In other words, it will return null if the attribute exits for
   * the given entity, but is inherited from a super entity.
   * 
   * <br/>
   * <b>Precondition:</b> attributeName != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("")
   */
  public MdAttributeConcreteDAOIF definesAttribute(String attributeName)
  {
    return (MdAttributeConcreteDAOIF) super.definesAttribute(attributeName);
  }

  /**
   * Returns a complete list of MdAttributeDAOIF objects defined for this
   * instance of Entity. This list includes attributes inherited from
   * supertypes.
   * 
   * @return a list of MdAttributeDAOIF objects
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributes()
  {
    return (List<? extends MdAttributeConcreteDAOIF>) super.getAllDefinedMdAttributes();
  }

  /**
   * Returns a map of MdAttributeDAOIF objects defined by this entity type plus
   * all attributes defined by parent entities.
   * <p/>
   * <br/>
   * Map Key: mdAttributeID <br/>
   * Map Value: MdAttributeDAOIF
   * <p/>
   * 
   * @return map of MdAttributeDAOIF objects defined by this entity type plus
   *         all attributes defined by parent entities.
   */
  @SuppressWarnings("unchecked")
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeIDMap()
  {
    return (Map<String, ? extends MdAttributeConcreteDAOIF>) super.getAllDefinedMdAttributeIDMap();
  }

  /**
   * Returns a map of MdAttributeDAOIF objects defined by this entity type plus
   * all attributes defined by parent entities.
   * <p/>
   * <br/>
   * Map Key: attribute name in lower case <br/>
   * Map Value: MdAttributeDAOIF
   * <p/>
   * 
   * @return map of MdAttributeDAOIF objects defined by this entity type plus
   *         all attributes defined by parent entities.
   */
  @SuppressWarnings("unchecked")
  public Map<String, ? extends MdAttributeConcreteDAOIF> getAllDefinedMdAttributeMap()
  {
    return (Map<String, ? extends MdAttributeConcreteDAOIF>) super.getAllDefinedMdAttributeMap();
  }

  /**
   * Returns a map of MdAttributeDAOIF objects defined by this entity. Key:
   * attribute name in lower case Value: MdAttributeDAOIF
   * 
   * @return map of MdAttributeDAOIF objects defined by this entity.
   */
  @SuppressWarnings("unchecked")
  public Map<String, ? extends MdAttributeConcreteDAOIF> getDefinedMdAttributeMap()
  {
    return (Map<String, ? extends MdAttributeConcreteDAOIF>) super.getDefinedMdAttributeMap();
  }

  /**
   * Returns a list of MdEntityDAO objects that are the root of hierarchies.
   * 
   * @return list of MdEntityDAO objects that are the root of hierarchies.
   */
  public static List<MdEntityDAOIF> getRootEntities()
  {
    List<MdEntityDAOIF> returnList = new LinkedList<MdEntityDAOIF>();

    returnList.addAll(MdElementDAO.getRootMdElements());

    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery mdStructQ = qFactory.businessDAOQuery(MdStructInfo.CLASS);

    OIterator<BusinessDAOIF> mdEntityIterator = mdStructQ.getIterator();
    while (mdEntityIterator.hasNext())
    {
      MdEntityDAOIF mdEntity = (MdEntityDAOIF) mdEntityIterator.next();
      returnList.add(mdEntity);
    }

    return returnList;
  }

  /**
   * Returns a EntityQuery object for instances of the given type.
   * 
   * @return EntityQuery object for instances of the given type.
   */
  public abstract EntityQuery getEntityQuery();

  /**
   * Returns a <code>MdEntityDAOIF</code> instance that defines the given type.
   * 
   * @param entityType
   * @return <code>MdEntityDAOIF</code> that defines the given type.
   */
  public static MdEntityDAOIF getMdEntityDAO(String entityType)
  {
    MdEntityDAOIF mdEntity = ObjectCache.getMdEntityDAO(entityType);

    if (mdEntity == null)
    {
      String error = "Metadata not found for entity [" + entityType + "]";

      // Feed in the MdEntityDAO for MdEntityDAO. Yes, it's self-describing.
      throw new DataNotFoundException(error, getMdEntityDAO(MdEntityInfo.CLASS));
    }

    return mdEntity;
  }

  /**
   * Returns the <code>MdEntityDAOIF</code> instance that defines the given
   * table name.
   * 
   * @param tableName
   * 
   * @return <code>MdEntityDAOIF</code> that defines the table with the given
   *         name.
   */
  public static MdEntityDAOIF getMdEntityByTableName(String tableName)
  {
    MdEntityDAOIF mdEntity = (MdEntityDAOIF) ObjectCache.getMdClassByTableName(tableName);

    if (mdEntity == null)
    {
      String error = "Metadata not found that defines table [" + tableName + "]";

      // Feed in the MdEntityDAO for MdEntityDAO. Yes, it's self-describing.
      throw new DataNotFoundException(error, getMdEntityDAO(MdEntityInfo.CLASS));
    }

    return mdEntity;
  }

  @Override
  public String apply()
  {
    boolean applied = this.isAppliedToDB();

    if (this.isNew() && !applied)
    {
      // Supply a table name if one was not provided
      Attribute tableNameAttribute = this.getAttribute(MdEntityInfo.TABLE_NAME);
      if (!tableNameAttribute.isModified() || tableNameAttribute.getValue().trim().length() == 0)
      {
        // Create a table name
        String tableName = MdTypeDAO.createTableName(MetadataDAO.convertCamelCaseToUnderscore(this.getTypeName()));
        tableNameAttribute.setValue(tableName);
      }
      else
      {
        tableNameAttribute.setValue(tableNameAttribute.getValue().toLowerCase());
      }
    }

    return super.apply();
  }

  /**
   * Applies the state of this BusinessDAO to the database. If this is a new
   * BusinessDAO, then records are created in the database and an OID is
   * created. If this is not a new BusinessDAO, then records are modified in the
   * database.
   * 
   * <br/>
   * <b>Precondition:</b> Attribues must have correct values as defined in their
   * meta data. <br/>
   * <b>Postcondition:</b> state of the BusinessDAO is preserved in the
   * database. <br/>
   * <b>Postcondition:</b> return value is not null
   * 
   * @param validateRequired
   *          true if attributes should be checked for required values, false
   *          otherwise. StructDAOs used by struct attributes may or may not
   *          need required attributes validated.
   * @return OID of the BusinessDAO.
   * @throws DataAccessException
   *           if an attribute contains a value that is not correct with respect
   *           to the metadata.
   */
  public String save(boolean validateRequired)
  {
    // Validate the name
    validateName();

    String oid = "";
    boolean applied = this.isAppliedToDB();

    if (this.isNew() && !applied)
    {
      // Supply a table name if one was not provided
      Attribute tableNameAttribute = this.getAttribute(MdEntityInfo.TABLE_NAME);
      if (!tableNameAttribute.isModified() || tableNameAttribute.getValue().trim().length() == 0)
      {
        // Create a table name
        String tableName = MdTypeDAO.createTableName(MetadataDAO.convertCamelCaseToUnderscore(this.getTypeName()));
        tableNameAttribute.setValue(tableName);
      }
      else
      {
        tableNameAttribute.setValue(tableNameAttribute.getValue().toLowerCase());
      }
    }

    oid = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      this.createEntityTable();

      /**
       * If this class is the root of a hierarchy, then copy all default
       * attributes. However, do not copy the default attributes during an
       * import because the attributes are assumed to be defined in the import
       * file.
       */
      if (this.isRootOfHierarchy() && !this.isImport())
      {
        this.copyDefaultAttributes();
      }
    }
    // (!this.isNew() || applied)
    else
    {
      Attribute keyAttribute = this.getAttribute(MdEntityInfo.KEY);
      // This should never actually be modified so for now skip updating the
      // indexes
      if (keyAttribute.isModified())
      {
        // List<RelationshipDAOIF> relList =
        // this.getChildren(RelationshipTypes.ENTITY_INDEX.getType());
        //
        // for (RelationshipDAOIF relationshipDAOIF : relList)
        // {
        // MdIndexDAO mdIndexDAO =
        // (MdIndexDAO)relationshipDAOIF.getChild().getBusinessDAO();
        // mdIndexDAO.apply();
        // }
      }
    }

    this.updateCacheStrategy();

    return oid;
  }

  /**
   * Runtime validation to ensure the name of MdEntityDAO is less than
   * NAME_LIMIT characters
   */
  private void validateName()
  {
    Attribute name = this.getAttribute(MdEntityInfo.NAME);
    Attribute pack = this.getAttribute(MdEntityInfo.PACKAGE);
    Attribute type = this.getAttribute(MdEntityInfo.TYPE);

    if (name.getValue().length() > NAME_LIMIT)
    {
      String err = "The (unqualified) type name name [" + name.getValue() + "] may not exceed " + NAME_LIMIT + " characters.";
      throw new AttributeLengthCharacterException(err, type, NAME_LIMIT);
    }

    if (name.getValue().length() + pack.getValue().length() > FULL_LIMIT)
    {
      String err = "The fully qualified type name name [" + pack.getValue() + "." + name.getValue() + "] may not exceed " + FULL_LIMIT + " characters.";
      throw new AttributeLengthCharacterException(err, type, FULL_LIMIT);
    }
  }

  /**
   * Deletes all records for the given class WITHOUT checking any integrity
   * constraints or executing any custom business logic. Use this method with
   * extreme caution.
   * 
   */
  public void deleteAllRecords()
  {
    Database.deleteAllTableRecords(this.getTableName());

    // delete all childclasses
    for (MdEntityDAOIF subMdEntityIF : getSubClasses())
    {
      if (!subMdEntityIF.definesType().equals(this.definesType()))
      {
        MdEntityDAO subMdEntity = subMdEntityIF.getBusinessDAO();
        subMdEntity.deleteAllRecords();
      }
    }
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
  @Override
  public void delete(DeleteContext context)
  {
    this.existenceCheck();

    this.deleteAllChildClasses(context);

    // 1. Remove any additional dependencies
    this.removeDependencies(context);

    // 2. Delete all instances of this type
    this.deleteInstances(context);

    // 2.5 Delete all MdMethods defined by this type
    this.dropAllMdMethods();

    // 3. Delete all attribute MdAttribue objects that this type defines
    // delete all attribute metadata for this class
    this.dropAllAttributes(context);

    // 3.5 Delete all permission tuples that this class participates in
    this.dropTuples();

    // 4. Delete all MdMethodDAO objects that this type defines
    this.dropMdMethods(context);

    // 5. Delete this BusinessDAO
    super.delete(context);

    ObjectCache.removeCacheStrategy(this.definesType());

    // System.out.println("\n\nUpdating cache for: "+this.definesType());
    ObjectCache.updateParentCacheStrategy(this);
    // Transaction management aspect will update the cache

    this.deleteEntityTable();
  }

  // Removes any additional dependencies. Actually this allows classes to
  // disassociate themselves
  // from any relationship types they participate in.
  protected void removeDependencies(DeleteContext context)
  {
  }

  /**
   * Deletes all EntityDAO instances of this type.
   * 
   * <br/>
   * <b>Postcondition:</b> All instances of this class are deleted.
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  public abstract void deleteInstances(DeleteContext context);

  /**
   * Removes any MdMdethod that are defined by this MdEntityDAO.
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  private void dropMdMethods(DeleteContext context)
  {
    for (MdMethodDAOIF mdMethod : this.getMdMethods())
    {
      mdMethod.getBusinessDAO().delete(context);
    }
  }

  /**
   * Makes sure than a given object exists in the database before deletion.
   * 
   * @throws ProgrammingErrorException
   *           if the object does not exist.
   */
  protected void existenceCheck()
  {
    MdEntityDAOIF mdEntityIF = this.getMdClassDAO();
    String tableName = mdEntityIF.getTableName();

    // check to make sure object hasn't already been deleted and does exist.
    if (!Database.doesObjectExist(this.getOid(), tableName))
    {
      String error = "Object [" + this.getOid() + "] does not exist in the database - it may have already been deleted";
      throw new StaleEntityException(error, this);
    }
  }

  /**
   *
   */
  @Override
  public void setValue(String name, String value)
  {
    if (!this.isNew())
    {
      boolean checkMethodParams = false;

      if (name.equals(MdElementInfo.PACKAGE) && !value.equals(this.getAttribute(MdElementInfo.PACKAGE).getValue()))
      {
        checkMethodParams = true;
      }
      else if (name.equals(MdElementInfo.NAME) && !value.equals(this.getAttribute(MdElementInfo.NAME).getValue()))
      {
        checkMethodParams = true;
      }

      // Validate that the MdEntityDAO is not referenced as a type
      // in a MdMethodDAO or MdParameterDAO
      if (checkMethodParams)
      {
        validateMdMethods();
        validateMdParameters();
      }
    }

    super.setValue(name, value);
  }

  /**
   *
   */
  private void validateMdMethods()
  {
    List<ParameterMarker> errors = new LinkedList<ParameterMarker>();

    String type = this.definesType();

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(MdMethodInfo.CLASS);
    query.WHERE(query.aCharacter(MdMethodInfo.RETURN_TYPE).LIKE(type + "%"));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while (iterator.hasNext())
    {
      MdMethodDAOIF mdMethod = (MdMethodDAOIF) iterator.next();

      MdTypeDAOIF mdType = mdMethod.getEnclosingMdTypeDAO();

      if (!mdType.getOid().equals(this.getOid()))
      {
        errors.add(mdMethod);
      }
    }

    if (errors.size() > 0)
    {
      String msg = "The type [" + this.definesType() + "] is referenced in MdMethods";
      throw new ExistingMdMethodReferenceException(msg, this, errors);
    }
  }

  /**
   *
   */
  private void validateMdParameters()
  {
    List<ParameterMarker> errors = new LinkedList<ParameterMarker>();
    String type = this.definesType();

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(MdParameterInfo.CLASS);
    query.WHERE(query.aCharacter(MdParameterInfo.TYPE).LIKE(type + "%"));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while (iterator.hasNext())
    {
      MdParameterDAOIF mdParameter = (MdParameterDAOIF) iterator.next();

      ParameterMarker parameterMarker = mdParameter.getEnclosingMetadata();

      MdTypeDAOIF mdType = parameterMarker.getEnclosingMdTypeDAO();

      if (!mdType.getOid().equals(this.getOid()))
      {
        errors.add(parameterMarker);
      }
    }

    if (errors.size() > 0)
    {
      String msg = "The MdEntityDAO [" + type + "] used in the definition of MdMethods";

      throw new ExistingMdMethodReferenceException(msg, this, errors);
    }
  }

  /**
   * Updates the cache colleciton (if any) containing instances of this entity
   * type.
   */
  protected void updateCacheStrategy()
  {
    // Update the cache collection if needed

    if (this.getAttributeIF(MdElementInfo.CACHE_ALGORITHM).isModified())
    {
      ObjectCache.updateCacheStrategy(this);
    }
  }

  /**
   * Structs only get the OID attribute copied from the Entity class.
   */
  protected void copyDefaultAttributes()
  {
    super.copyDefaultAttributes();

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityInfo.CLASS);

    List<? extends MdAttributeDAOIF> mdAttributeIFList = mdBusinessIF.definesAttributes();

    for (MdAttributeDAOIF mdAttributeIF : mdAttributeIFList)
    {
      this.copyAttribute(mdAttributeIF);
    }
  }

  /**
   * Creates a table for this entity. <br/>
   * <b>Precondition</b>: table for this entity does not already exist.
   */
  protected abstract void createEntityTable();

  /**
   * Deletes the table for this entity. <br/>
   * <b>Precondition</b>: table for this entity must already exist.
   */
  protected abstract void deleteEntityTable();

  /**
   * Returns a command object that either creates or updates Java artifacts for
   * this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that either creates or updates Java artifacts for
   *         this type.
   */
  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    Command command;

    if (this.isNew())
    {
      command = new JavaArtifactMdEntityCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE, conn);

    }
    else
    {
      command = new JavaArtifactMdEntityCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE, conn);
    }

    return command;
  }

  /**
   * Returns a command object that deletes Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that deletes Java artifacts for this type.
   */
  @Override
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdEntityCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE, conn);
  }

  /**
   * Returns a command object that cleans Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that cleans Java artifacts for this type.
   */
  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdEntityCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN, conn);
  }

  /**
   * Copies all Java source and class files from this object into files on the
   * file system.
   */
  public void writeJavaToFile()
  {
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return;
    }

    super.writeJavaToFile();

    // Write the query and base .class files to the filesystem
    byte[] queryAPIclasses = this.getBlob(MdEntityInfo.QUERY_CLASS);
    String queryAPIsource = this.getAttribute(MdEntityInfo.QUERY_SOURCE).getValue();

    byte[] queryDTOclasses = this.getBlob(MdEntityInfo.QUERY_DTO_CLASS);
    String queryDTOsource = this.getAttribute(MdEntityInfo.QUERY_DTO_SOURCE).getValue();

    try
    {
      ClassManager.writeClasses(TypeGenerator.getQueryAPIclassDirectory(this), queryAPIclasses);
      FileIO.write(TypeGenerator.getQueryAPIsourceFilePath(this), queryAPIsource);

      ClassManager.writeClasses(TypeGenerator.getQueryDTOclassDirectory(this), queryDTOclasses);
      FileIO.write(TypeGenerator.getQueryDTOsourceFilePath(this), queryDTOsource);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Copies all Java source and class files from the file system and stores them
   * in the database.
   * 
   * @param conn
   *          database connection object. This method is used during the a
   *          transaction. Consequently, the transaction must be managed
   *          manually.
   */
  public void writeFileArtifactsToDatabaseAndObjects(Connection conn)
  {
    if (!this.isSystemPackage())
    {
      super.writeFileArtifactsToDatabaseAndObjects(conn);

      String queryAPIsource = GenerationFacade.getQueryAPIsource(this);
      byte[] queryAPIclassBytes = GenerationFacade.getQueryAPIclasses(this);

      String queryAPIsourceColumnName = MdEntityDAOIF.QUERY_SOURCE_COLUMN;
      String queryAPIclassColumnName = MdEntityDAOIF.QUERY_CLASS_COLUMN;

      if (queryAPIsource != null && queryAPIclassBytes != null)
      {
        Database.updateClassAndSource(this.getOid(), MdEntityDAOIF.TABLE, queryAPIclassColumnName, queryAPIclassBytes, queryAPIsourceColumnName, queryAPIsource, conn);

        // Only update the source. The blob attributes just point to the
        // database anyway.
        this.getAttribute(MdEntityInfo.QUERY_SOURCE).setValue(queryAPIsource);
        this.getAttribute(MdEntityInfo.QUERY_CLASS).setModified(true);
      }

      byte[] queryDTOclassBytes = new byte[0];
      String queryDTOsource = "";

      // Only update DTO Java artifacts for classes that are published.
      if (this.isPublished())
      {
        queryDTOsource = GenerationFacade.getQueryDTOsource(this);
        queryDTOclassBytes = GenerationFacade.getQueryDTOclasses(this);
      }

      String queryDTOsourceColumnName = MdEntityDAOIF.QUERY_DTO_SOURCE_COLUMN;
      String queryDTOclassColumnName = MdEntityDAOIF.QUERY_DTO_CLASS_COLUMN;

      if (queryDTOsource != null && queryDTOclassBytes != null)
      {
        Database.updateClassAndSource(this.getOid(), MdEntityDAOIF.TABLE, queryDTOclassColumnName, queryDTOclassBytes, queryDTOsourceColumnName, queryDTOsource, conn);

        // Mark the class artifacts as modified, so that their values will be
        // logged (if enabled)
        this.getAttribute(MdEntityInfo.QUERY_DTO_CLASS).setModified(true);
        this.getAttribute(MdEntityInfo.QUERY_DTO_SOURCE).setValue(queryDTOsource);
      }
    }
  }

  /**
   * Returns true if an attribute that stores source or class has been modified.
   * 
   * @return true if an attribute that stores source or class has been modified.
   */
  @Override
  public boolean javaArtifactsModifiedOnObject()
  {
    if (super.javaArtifactsModifiedOnObject())
    {
      return true;
    }

    if (!this.isSystemPackage())
    {
      if (this.getAttribute(MdEntityInfo.QUERY_SOURCE).isModified() || this.getAttribute(MdEntityInfo.QUERY_DTO_SOURCE).isModified() || this.getAttribute(MdEntityInfo.QUERY_CLASS).isModified() || this.getAttribute(MdEntityInfo.QUERY_DTO_CLASS).isModified())
      {
        return true;
      }
    }

    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdEntityDAO getBusinessDAO()
  {
    return (MdEntityDAO) super.getBusinessDAO();
  }

  public boolean getEnforceSiteMaster()
  {
    return Boolean.parseBoolean(this.getAttributeIF(MdEntityInfo.ENFORCE_SITE_MASTER).getValue());
  }

  public static MdEntityDAOIF get(String oid)
  {
    return (MdEntityDAOIF) BusinessDAO.get(oid);
  }
}
