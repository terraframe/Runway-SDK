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
import com.runwaysdk.business.generation.view.AbstractViewGenerator;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.configuration.LegacyPropertiesSupport;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.GeneratedActions;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdControllerInfo;
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
import com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocal;
import com.runwaysdk.dataaccess.attributes.entity.AttributeLocalCharacter;
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
   * Flag denoting if this MdEntity should generate a controller
   */
  private boolean           generateMdController;

  /**
   * Flag denoting if this MdEntity has generated a controller
   */
  private boolean           hasController;

  /**
   * The default constructor, does not set any attributes
   */
  public MdEntityDAO()
  {
    super();

    if (LegacyPropertiesSupport.isLegacy()) // If DDMS...
    {
      this.generateMdController = true;
    }
    else
    {
      this.generateMdController = false;
    }
    
    this.hasController = false;
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdTypeDAO
   */
  public MdEntityDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);

    if (LegacyPropertiesSupport.isLegacy()) // If DDMS...
    {
      this.generateMdController = true;
    }
    else
    {
      this.generateMdController = false;
    }
    
    this.hasController = false;
  }

  public void setGenerateMdController(boolean generateMdController)
  {
    this.generateMdController = generateMdController;
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
   * Returns true if objects defined by this type are not cached in the global cache, 
   * false otherwise.
   * 
   * @return true if objects defined by this type are not cached in the global cache, 
   * false otherwise.
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
   * @return TRUE if IDs that are generated are deterministic, FALSE 
   * otherwise. Deterministic IDs are generated from a hash of the KeyName value.
   */
  public boolean hasDeterministicIds()
  {
    AttributeBooleanIF attrBool = (AttributeBooleanIF)this.getAttributeIF(MdEntityInfo.HAS_DETERMINISTIC_IDS);
    
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
   * Returns the <code>MdEntityDAOIF</code> instance that defines the given table name.
   * 
   * @param tableName
   * 
   * @return <code>MdEntityDAOIF</code> that defines the table with the given name.
   */
  public static MdEntityDAOIF getMdEntityByTableName(String tableName)
  {
    MdEntityDAOIF mdEntity = ObjectCache.getMdEntityByTableName(tableName);

    if (mdEntity == null)
    {
      String error = "Metadata not found that defines table [" + tableName + "]";

      // Feed in the MdEntityDAO for MdEntityDAO. Yes, it's self-describing.
      throw new DataNotFoundException(error, getMdEntityDAO(MdEntityInfo.CLASS));
    }

    return mdEntity;
  }

  /**
   * Applies the state of this BusinessDAO to the database. If this is a new
   * BusinessDAO, then records are created in the database and an ID is created.
   * If this is not a new BusinessDAO, then records are modified in the
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
   * @return ID of the BusinessDAO.
   * @throws DataAccessException
   *           if an attribute contains a value that is not correct with respect
   *           to the metadata.
   */
  public String save(boolean validateRequired)
  {
    // Validate the name
    validateName();

    String id = new String();
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

    id = super.save(validateRequired);

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

      // Create a MdControllerDAO for this MdEntityDAO
      if (!this.isImport() && this.generateMdController && this.isGenerateSource())
      {
        if (this.isMdControllerQualified())
        {
          this.defineMdController();
        }
      }
    }
    // (!this.isNew() || applied)
    else
    {
      Attribute keyAttribute = this.getAttribute(MdEntityInfo.KEY);
      // This should never actually be modified so for now skip updating the indexes
      if (keyAttribute.isModified())
      {
//        List<RelationshipDAOIF> relList = this.getChildren(RelationshipTypes.ENTITY_INDEX.getType());
//        
//        for (RelationshipDAOIF relationshipDAOIF : relList)
//        {
//          MdIndexDAO mdIndexDAO = (MdIndexDAO)relationshipDAOIF.getChild().getBusinessDAO();
//          mdIndexDAO.apply();
//        }
      }
    }

    this.updateCacheStrategy();

    return id;
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
  public void delete(boolean businessContext)
  {
    this.existenceCheck();

    // Delete the generated MdControllerDAO for this type
    this.deleteMdController();

    this.deleteAllChildClasses(businessContext);

    // 1. Remove any additional dependencies
    this.removeDependencies(businessContext);

    // 2. Delete all instances of this type
    this.deleteInstances(businessContext);

    // 2.5 Delete all MdMethods defined by this type
    this.dropAllMdMethods();

    // 3. Delete all attribute MdAttribue objects that this type defines
    // delete all attribute metadata for this class
    this.dropAllAttributes(businessContext);

    // 3.5 Delete all permission tuples that this class participates in
    this.dropTuples();

    // 4. Delete all MdMethodDAO objects that this type defines
    this.dropMdMethods(businessContext);

    // 5. Delete this BusinessDAO
    super.delete(businessContext);

    ObjectCache.removeCacheStrategy(this.definesType());

    // System.out.println("\n\nUpdating cache for: "+this.definesType());
    ObjectCache.updateParentCacheStrategy(this);
    // Transaction management aspect will update the cache

    this.deleteEntityTable();
  }

  // Removes any additional dependencies. Actually this allows classes to
  // disassociate themselves
  // from any relationship types they participate in.
  protected void removeDependencies(boolean businessContext)
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
  public abstract void deleteInstances(boolean businessContext);

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
  private void dropMdMethods(boolean businessContext)
  {
    for (MdMethodDAOIF mdMethod : this.getMdMethods())
    {
      mdMethod.getBusinessDAO().delete(businessContext);
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
    if (!Database.doesObjectExist(this.getId(), tableName))
    {
      String error = "Object [" + this.getId() + "] does not exist in the database - it may have already been deleted";
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

      if (!mdType.getId().equals(this.getId()))
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

      if (!mdType.getId().equals(this.getId()))
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
   * Structs only get the ID attribute copied from the Entity class.
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
        Database.updateClassAndSource(this.getId(), MdEntityDAOIF.TABLE, queryAPIclassColumnName, queryAPIclassBytes, queryAPIsourceColumnName, queryAPIsource, conn);

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
        Database.updateClassAndSource(this.getId(), MdEntityDAOIF.TABLE, queryDTOclassColumnName, queryDTOclassBytes, queryDTOsourceColumnName, queryDTOsource, conn);

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

  private void defineMdController()
  {
    MdControllerDAO mdController = MdControllerDAO.newInstance();

    AttributeLocalCharacter mdEntityDisplayLabelAttribute = ( (AttributeLocalCharacter) this.getAttribute(MdControllerInfo.DISPLAY_LABEL) );
    StructDAO entityStructDAO = mdEntityDisplayLabelAttribute.getStructDAO();
    List<? extends MdAttributeConcreteDAOIF> mdEntityAttributes = entityStructDAO.getMdAttributeDAOs();

    AttributeLocalCharacter mdControllerDisplayLabelAttribute = ( (AttributeLocalCharacter) mdController.getAttribute(MdControllerInfo.DISPLAY_LABEL) );
    StructDAO controllerStructDAO = mdControllerDisplayLabelAttribute.getStructDAO();

    // Iterate over the localized values and copy them over.
    for (MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF : mdEntityAttributes)
    {
      String attributeName = mdAttributeConcreteDAOIF.definesAttribute();

      if (!mdAttributeConcreteDAOIF.isSystem() && ( mdAttributeConcreteDAOIF instanceof MdAttributeLocalCharacterDAOIF ) && controllerStructDAO.hasAttribute(attributeName))
      {
        controllerStructDAO.getAttribute(attributeName).setValue(entityStructDAO.getAttribute(attributeName).getValue() + " " + AbstractViewGenerator.CONTROLLER_SUFFIX);
      }
    }

    mdController.getAttribute(MdControllerInfo.DESCRIPTION).setValue("Auto-generated Controller for " + this.definesType());
    mdController.getAttribute(MdControllerInfo.NAME).setValue(this.getTypeName() + "Controller");
    mdController.getAttribute(MdControllerInfo.PACKAGE).setValue(this.getPackage());
    mdController.getAttribute(MdControllerInfo.REMOVE).setValue(MdAttributeBooleanInfo.TRUE);
    mdController.setMdEntity(this);
    mdController.apply();

    defineControllerActions(mdController);

    this.hasController = true;
  }

  protected void defineControllerActions(MdControllerDAO mdController)
  {
    // Define viewPage
    defineViewPageAction(mdController);

    // Define viewAll
    defineViewAllAction(mdController);

    defineViewAction(mdController);

    // Define newInstance
    if (!this.isAbstract())
    {
      defineNewInstanceAction(mdController);
    }

    // Define create
    defineCreateAction(mdController);

    // Define edit
    defineEditAction(mdController);

    // Define update
    defineUpdateAction(mdController);

    // Define cancel
    defineCancelAction(mdController);

    // Define delete
    defineDeleteAction(mdController);
  }

  private void defineCancelAction(MdControllerDAO mdController)
  {
    MdActionDAO cancel = MdActionDAO.newInstance();
    cancel.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    cancel.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.CANCEL_ACTION.getName());
    cancel.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    cancel.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.TRUE);
    ( (AttributeLocal) cancel.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Unlocks an instance " + this.definesType());
    ( (AttributeLocal) cancel.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "cancel");
    cancel.apply();

    MdParameterDAO cancelParam = MdParameterDAO.newInstance();
    cancelParam.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(cancel.getId());
    cancelParam.getAttribute(MdParameterInfo.NAME).setValue("dto");
    cancelParam.getAttribute(MdParameterInfo.ORDER).setValue("0");
    cancelParam.getAttribute(MdParameterInfo.TYPE).setValue(this.definesType());
    ( (AttributeLocal) cancelParam.getAttribute(MdParameterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, this.definesType() + " to unlock");
    ( (AttributeLocal) cancelParam.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter: " + this.definesType() + " - dto");
    cancelParam.apply();
  }

  private void defineCreateAction(MdControllerDAO mdController)
  {
    MdActionDAO create = MdActionDAO.newInstance();
    create.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    create.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.CREATE_ACTION.getName());
    create.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    create.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.TRUE);
    ( (AttributeLocal) create.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Applys a new instance of " + this.definesType());
    ( (AttributeLocal) create.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Create");
    create.apply();

    MdParameterDAO createParam = MdParameterDAO.newInstance();
    createParam.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(create.getId());
    createParam.getAttribute(MdParameterInfo.NAME).setValue("dto");
    createParam.getAttribute(MdParameterInfo.ORDER).setValue("0");
    createParam.getAttribute(MdParameterInfo.TYPE).setValue(this.definesType());
    ( (AttributeLocal) createParam.getAttribute(MdParameterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, this.definesType() + " to apply to the database");
    ( (AttributeLocal) createParam.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter: " + this.definesType());
    createParam.apply();
  }

  private void defineDeleteAction(MdControllerDAO mdController)
  {
    MdActionDAO delete = MdActionDAO.newInstance();
    delete.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    delete.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.DELETE_ACTION.getName());
    delete.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    delete.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.TRUE);
    ( (AttributeLocal) delete.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Delete an instance of " + this.definesType());
    ( (AttributeLocal) delete.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "delete");
    delete.apply();

    MdParameterDAO deleteParam = MdParameterDAO.newInstance();
    deleteParam.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(delete.getId());
    deleteParam.getAttribute(MdParameterInfo.NAME).setValue("dto");
    deleteParam.getAttribute(MdParameterInfo.ORDER).setValue("0");
    deleteParam.getAttribute(MdParameterInfo.TYPE).setValue(this.definesType());
    ( (AttributeLocal) deleteParam.getAttribute(MdParameterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, this.definesType() + " to delete");
    ( (AttributeLocal) deleteParam.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter: " + this.definesType() + " - dto");
    deleteParam.apply();
  }

  private void defineEditAction(MdControllerDAO mdController)
  {
    MdActionDAO edit = MdActionDAO.newInstance();
    edit.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    edit.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.EDIT_ACTION.getName());
    edit.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    edit.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.FALSE);
    ( (AttributeLocal) edit.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Applys an existing instance of " + this.definesType());
    ( (AttributeLocal) edit.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "edit");
    edit.apply();

    MdParameterDAO editParam = MdParameterDAO.newInstance();
    editParam.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(edit.getId());
    editParam.getAttribute(MdParameterInfo.NAME).setValue("id");
    editParam.getAttribute(MdParameterInfo.ORDER).setValue("0");
    editParam.getAttribute(MdParameterInfo.TYPE).setValue(String.class.getName());
    ( (AttributeLocal) editParam.getAttribute(MdParameterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, this.definesType() + " to apply to the database");
    ( (AttributeLocal) editParam.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter: " + this.definesType());
    editParam.apply();
  }

  protected void defineNewInstanceAction(MdControllerDAO mdController)
  {
    MdActionDAO newInstance = MdActionDAO.newInstance();
    newInstance.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    newInstance.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.NEW_INSTANCE_ACTION.getName());
    newInstance.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    newInstance.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.FALSE);
    ( (AttributeLocal) newInstance.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Loads a new instance of " + this.definesType() + " into the request object");
    ( (AttributeLocal) newInstance.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "New Instance");
    newInstance.apply();
  }

  protected void defineUpdateAction(MdControllerDAO mdController)
  {
    MdActionDAO update = MdActionDAO.newInstance();
    update.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    update.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.UPDATE_ACTION.getName());
    update.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    update.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.TRUE);
    ( (AttributeLocal) update.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Applys an existing instance of " + this.definesType());
    ( (AttributeLocal) update.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "update");
    update.apply();

    MdParameterDAO updateParam = MdParameterDAO.newInstance();
    updateParam.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(update.getId());
    updateParam.getAttribute(MdParameterInfo.NAME).setValue("dto");
    updateParam.getAttribute(MdParameterInfo.ORDER).setValue("0");
    updateParam.getAttribute(MdParameterInfo.TYPE).setValue(this.definesType());
    ( (AttributeLocal) updateParam.getAttribute(MdParameterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, this.definesType() + " to apply to the database");
    ( (AttributeLocal) updateParam.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter: " + this.definesType() + " - dto");
    updateParam.apply();
  }

  protected void defineViewAllAction(MdControllerDAO mdController)
  {
    MdActionDAO viewAll = MdActionDAO.newInstance();
    viewAll.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    viewAll.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.VIEW_ALL_ACTION.getName());
    viewAll.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    viewAll.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.FALSE);
    ( (AttributeLocal) viewAll.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Loads a ViewAll query with the default settings");
    ( (AttributeLocal) viewAll.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "View All");
    viewAll.apply();
  }

  protected void defineViewAction(MdControllerDAO mdController)
  {
    MdActionDAO view = MdActionDAO.newInstance();
    view.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    view.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.VIEW_ACTION.getName());
    view.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.FALSE);
    view.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.FALSE);
    ( (AttributeLocal) view.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "View an existing instance of " + this.definesType());
    ( (AttributeLocal) view.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "View");
    view.apply();

    MdParameterDAO viewParam = MdParameterDAO.newInstance();
    viewParam.getAttribute(MdParameterInfo.ENCLOSING_METADATA).setValue(view.getId());
    viewParam.getAttribute(MdParameterInfo.NAME).setValue("id");
    viewParam.getAttribute(MdParameterInfo.ORDER).setValue("0");
    viewParam.getAttribute(MdParameterInfo.TYPE).setValue(String.class.getName());
    ( (AttributeLocal) viewParam.getAttribute(MdParameterInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Id of " + this.definesType() + " to view.");
    ( (AttributeLocal) viewParam.getAttribute(MdParameterInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Parameter: " + this.definesType() + " - id");
    viewParam.apply();
  }

  protected void defineViewPageAction(MdControllerDAO mdController)
  {
    MdActionDAO viewPage = MdActionDAO.newInstance();
    viewPage.getAttribute(MdActionInfo.ENCLOSING_MD_CONTROLLER).setValue(mdController.getId());
    viewPage.getAttribute(MdActionInfo.NAME).setValue(GeneratedActions.VIEW_PAGE_ACTION.getName());
    viewPage.getAttribute(MdActionInfo.IS_QUERY).setValue(MdAttributeBooleanInfo.TRUE);
    viewPage.getAttribute(MdActionInfo.IS_POST).setValue(MdAttributeBooleanInfo.FALSE);
    ( (AttributeLocal) viewPage.getAttribute(MdActionInfo.DESCRIPTION) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "Loads a ViewAll query for a specific page into the request object");
    ( (AttributeLocal) viewPage.getAttribute(MdActionInfo.DISPLAY_LABEL) ).setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, "View Page");
    viewPage.apply();
  }

  private void deleteMdController()
  {
    MdControllerDAOIF mdControllerDAOIF = this.getMdController();

    if(mdControllerDAOIF != null)
    {
      // Delete only if it has not already been deleted this transaction
      if (!((MdControllerDAO)mdControllerDAOIF).isDeleted())
      {
        mdControllerDAOIF.getBusinessDAO().delete();
      }
    }
  }

  protected boolean isMdControllerQualified()
  {
    // Flag denoting if this MdEntityDAO was defined as part of a state machine
    boolean isState = this.getPackage().startsWith(MdStateMachineDAO.STATE_PACKAGE);

    return ! ( isState ) && this.isPublished();
  }

  /**
   * @return The CRUD controller defined for this MdEntity, null if this
   *         MdEntity does not have CRUD controller
   */
  private MdControllerDAOIF getMdController()
  {
    MdControllerDAO mdControllerDAO = null;   

    String controllerType = this.definesType() + AbstractViewGenerator.CONTROLLER_SUFFIX;

    try
    {
      mdControllerDAO = (MdControllerDAO)MdControllerDAO.getMdControllerDAO(controllerType);
    }
    catch(DataNotFoundException e) 
    {
      return null;
    }
 
    return mdControllerDAO;
  }

  public boolean hasMdController()
  {
    return this.hasController || ( this.getMdController() != null );
  }

  public boolean getEnforceSiteMaster()
  {
    return Boolean.parseBoolean(this.getAttributeIF(MdEntityInfo.ENFORCE_SITE_MASTER).getValue());
  }

  public static MdEntityDAOIF get(String id)
  {
    return (MdEntityDAOIF) BusinessDAO.get(id);
  }
}
