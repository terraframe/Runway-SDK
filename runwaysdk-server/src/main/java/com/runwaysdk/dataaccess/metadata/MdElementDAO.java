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
package com.runwaysdk.dataaccess.metadata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;


public abstract class MdElementDAO extends MdEntityDAO implements MdElementDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -907839241360316932L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdElementDAO()
  {
    super();
  }

  /**
   * @see com.runwaysdk.dataaccess.metadata.MdTypeDAO
   */
  public MdElementDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns an array of MdElementIF that defines immediate sub-entites of this
   * entity.
   *
   * @return an array of MdElementIF that defines immediate sub-entites of this
   *         entity.
   */
  public abstract List<? extends MdElementDAOIF> getSubClasses();

  /**
   *Returns a list of MdClassIF objects that represent classes
   * that are subclasses of the given class, including this class,
   * including all recursive entities.
   *
   * @return list of MdClassIF objects that represent classes
   * that are subclasses of the given class, including this class,
   * including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdElementDAOIF> getAllSubClasses()
  {
    return (List<? extends MdElementDAOIF>)super.getAllSubClasses();
  }

  /**
   * Returns a list of MdElementIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   *
   * @return list of MdElementIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdElementDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdElementDAOIF>)super.getAllConcreteSubClasses();
  }

  public abstract MdElementDAOIF getSuperClass();

  /**
   * Returns a list of MdElementIF object representing every parent of this
   * MdElementIF partaking in an inheritance relationship, including this type.
   *
   * @return a list of parent MdElementIF objects
   */
  public List<? extends MdElementDAOIF> getSuperClasses()
  {
    List<MdElementDAOIF> superMdElementIFList = new LinkedList<MdElementDAOIF>();

    List<String> superTypeNameList = this.getSuperTypes();

    for (String superType : superTypeNameList)
    {
      superMdElementIFList.add(MdElementDAO.getMdElementDAO(superType));
    }

    return superMdElementIFList;
  }

  /**
   * Returns true if the type is abstract, false otherwise.
   *
   * @return true if the type is abstract, false otherwise.
   */
  public boolean isAbstract()
  {
    AttributeBooleanIF attributeBoolean = (AttributeBooleanIF) this.getAttributeIF(MdElementInfo.ABSTRACT);
    return AttributeBoolean.getBooleanValue(attributeBoolean);
  }

  /**
   * Returns true if the type can be extended, false otherwise.
   *
   * @return true if the type can be extended, false otherwise.
   */
  public boolean isExtendable()
  {
    AttributeBooleanIF attributeBoolean = (AttributeBooleanIF) this
        .getAttributeIF(MdElementInfo.EXTENDABLE);
    return AttributeBoolean.getBooleanValue(attributeBoolean);
  }

  /**
   * Applies the state of this BusinessDAO to the database. If this is a new
   * BusinessDAO, then records are created in the database and an ID is created.
   * If this is not a new BusinessDAO, then records are modified in the database.
   *
   * <br/><b>Precondition:</b> Attributes must have correct values as defined
   * in their meta data. <br/><b>Postcondition:</b> state of the BusinessDAO is
   * preserved in the database. <br/><b>Postcondition:</b> return value is not
   * null
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
    boolean applied = this.isAppliedToDB();
    String id = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      // Inheritance relationship will be imported if this is imported.
      if (this.createInheritanceRelationship()  && !this.isImport())
      {
        this.createInheritanceNew();
      }
    }
    // !this.isNew() || applied
    else if (this.getAttribute(EntityInfo.KEY).isModified())
    {
      this.updateInheritanceRelationshipKey();
    }

    return id;
  }

  /**
   * Creates a new inheritance relationship
   */
  protected abstract void createInheritanceNew();
  
  /**
   * Updates the key on the inheritance relationship
   */
  protected abstract void updateInheritanceRelationshipKey();

  /**
   * Copies default attributes from the Component class.
   *
   */
  protected void copyDefaultAttributes()
  {
    super.copyDefaultAttributes();

    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);

    List<? extends MdAttributeConcreteDAOIF> mdAttributeIFList = mdBusinessIF.definesAttributes();

    for (MdAttributeConcreteDAOIF mdAttributeIF : mdAttributeIFList)
    {
      this.copyAttribute(mdAttributeIF);
    }
  }

  /**
   * Validates a metadata object if it is new.
   */
  protected void validateNew()
  {
    super.validateNew();

    // check for reserved words
    if (ReservedWords.javaContains(this.getTypeName()))
    {
      throw new ReservedWordException("The type name [" + this.getTypeName() + "] is reserved.", this.getTypeName(), ReservedWordException.Origin.TYPE);
    }
    if (ReservedWords.sqlContains(this.getTableName()))
    {
      throw new ReservedWordException("The name [" + this.getTableName() + "] is reserved.", this.getTableName(), ReservedWordException.Origin.TABLE);
    }
  }


  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdElementDAO getBusinessDAO()
  {
    return (MdElementDAO) super.getBusinessDAO();
  }

  /**
   * Returns true if the given parent type name is a subclass of the given child
   * type, false otherwise. Returns true if the two types are the same.
   *
   * <br/><b>Precondition:</b> childEntityType != null
   * <br/><b>Precondition:</b> !childEntityType.trim().equals("")
   * <br/><b>Precondition:</b> childEntityType is a valid type defined in the database
   * <br/><b>Precondition:</b> parentEntityType != null
   * <br/><b>Precondition:</b> !parentEntityType.trim().equals("")
   * <br/><b>Precondition:</b> parentEntityType is a valid type defined in the database
   * <br/><b>Postcondition:</b> Returns true if the given parent type is a sub type of the given child type
   * name, false otherwise
   *
   * @param childEntityType
   *          child type.
   * @param parentEntityType
   *          parent type.
   * @return true if the given parent type is a sub type of the given child type
   *         name, false otherwise
   */
  public static boolean isSuperEntity(String parentType, String childType)
  {
    return isSubEntity(childType, parentType);
  }

  /**
   * Returns true if the given child type is a sub type of the given parent
   * type, false otherwise. Returns true if the two types names are the same.
   *
   * <br/><b>Precondition:</b> childType != null
   * <br/><b>Precondition:</b> !childyType.trim().equals("")
   * <br/><b>Precondition:</b> childType is a valid type name defined in the database
   * <br/><b>Precondition:</b> parentType != null
   * <br/><b>Precondition:</b> !parentType.trim().equals("")
   * <br/><b>Postcondition:</b>
   * Returns true if the given child type is a sub type of the given parent
   * type, false otherwise. Returns true if the two types names are the same.
   *
   * @param childType
   *          child type
   * @param parentType
   *          parent type
   * @return true if the given child type is a sub type of the given parent
   *         type, false otherwise. Returns true if the two types names are the
   *         same.
   */
  public static boolean isSubEntity(String childType, String parentType)
  {
    MdElementDAOIF mdElementIF = MdElementDAO.getMdElementDAO(childType);

    List<String> superTypeList = mdElementIF.getSuperTypes();

    for (String superType : superTypeList)
    {
      if (parentType.equals(superType))
      {
        return true;
      }
    }
    return false;
  }


  /**
   * Returns the MdEntityIF that is the root of the hierarchy that this type
   * belongs to. returns a reference to inself if it is the root.
   *
   * @return MdEntityIF that is the root of the hierarchy that this type belongs
   *         to. returns a reference to inself if it is the root.
   */
  public MdElementDAOIF getRootMdClassDAO()
  {
    return (MdElementDAOIF) super.getRootMdClassDAO();
  }

  /**
   * Returns a list of MdElementIF objects that are the root of hierarchies.
   *
   * @return list of MdElementIF objects that are the root of hierarchies.
   */
  public static List<MdElementDAOIF> getRootMdElements()
  {
    List<MdElementDAOIF> returnList = new LinkedList<MdElementDAOIF>();

    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery mdEntityQ = qFactory.businessDAOQuery(MdBusinessInfo.CLASS);
    mdEntityQ.WHERE(mdEntityQ.aReference(MdBusinessInfo.SUPER_MD_BUSINESS).EQ(""));

    OIterator<BusinessDAOIF> mdEntityIterator = mdEntityQ.getIterator();
    while (mdEntityIterator.hasNext())
    {
      MdElementDAOIF mdEntity = (MdElementDAOIF)mdEntityIterator.next();
      returnList.add(mdEntity);
    }

    qFactory = new QueryFactory();
    mdEntityQ = qFactory.businessDAOQuery(MdRelationshipInfo.CLASS);
    mdEntityQ.WHERE(mdEntityQ.aReference(MdRelationshipInfo.SUPER_MD_RELATIONSHIP).EQ(""));

    mdEntityIterator = mdEntityQ.getIterator();
    while (mdEntityIterator.hasNext())
    {
      MdElementDAOIF mdEntity = (MdElementDAOIF)mdEntityIterator.next();
      returnList.add(mdEntity);
    }

    return returnList;
  }

  /**
   *
   * @param entityType
   * @return
   */
  public static MdElementDAOIF getMdElementDAO(String entityType)
  {
    MdEntityDAOIF mdEntityIF = ObjectCache.getMdEntityDAO(entityType);

    if (mdEntityIF==null)
    {
      String error = "Metadata not found for entity [" + entityType + "]";

      // Feed in the MdEntity for MdEntity.  Yes, it's self-describing.
      throw new DataNotFoundException(error, getMdElementDAO(MdElementInfo.CLASS));
    }

    return (MdElementDAOIF)mdEntityIF;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdElementDAOIF get(String id)
  {
    return (MdElementDAOIF) BusinessDAO.get(id);
  }

  public final String getTableOfStubAttributes()
  {
    return MdElementDAOIF.TABLE;
  }
}
