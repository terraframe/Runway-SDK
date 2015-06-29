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
package com.runwaysdk.dataaccess.metadata;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.JavaArtifactMdTransientCommand;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdTransientInfo;
import com.runwaysdk.dataaccess.AttributeBooleanIF;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeBoolean;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.transaction.LockObject;

public abstract class MdTransientDAO extends MdClassDAO implements MdTransientDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -2072404586031935270L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdTransientDAO()
  {
    super();
  }

  /**
   * Constructs a MdView from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdTransientDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * Returns true if the type is abstract, false otherwise.
   *
   * @return true if the type is abstract, false otherwise.
   */
  public boolean isAbstract()
  {
    AttributeBooleanIF attributeBoolean = (AttributeBooleanIF) this.getAttributeIF(MdTransientInfo.ABSTRACT);
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
        .getAttributeIF(MdTransientInfo.EXTENDABLE);
    return AttributeBoolean.getBooleanValue(attributeBoolean);
  }

  /**
   *Returns the MdTransientIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdTransientIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdTransientDAOIF getRootMdClassDAO()
  {
    return (MdTransientDAOIF)super.getRootMdClassDAO();
  }
  /**
   * Returns an array of MdTransientIF that defines immediate subclasses of this class.
   * @return an array of MdTransientIF that defines immediate subclasses of this class.
   */
  public abstract List<? extends MdTransientDAOIF> getSubClasses();

  /**
   * Returns a list of MdTransientIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   *
   * @return list of MdTransientIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdTransientDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdTransientDAOIF>) super.getAllConcreteSubClasses();
  }


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
  public List<? extends MdTransientDAOIF> getAllSubClasses()
  {
    return (List<? extends MdTransientDAOIF>)super.getAllSubClasses();
  }

  /**
   * Returns an MdTransientIF representing the super class of this class, or null if
   * it does not have one.
   *
   * @return an MdTransientIF representing the super class of this class, or null if
   * it does not have one.
   */
  public abstract MdTransientDAOIF getSuperClass();

  /**
   * Returns a list of MdTransientIF instances representing every
   * parent of this MdTransientIF partaking in an inheritance relationship.
   *
   * @return a list of MdTransientIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdTransientDAOIF> getSuperClasses()
  {
    return (List<? extends MdTransientDAOIF>) super.getSuperClasses();
  }

  /**
   *Returns the MdAttribute that defines the given attribute for the this entity.  This method
   * only works if the attribute is explicitly defined by the this.  In other words, it
   * will return null if the attribute exits for the given entity, but is inherited from a
   * super entity.
   *
   * <br/><b>Precondition:</b>   attributeName != null
   * <br/><b>Precondition:</b>   !attributeName.trim().equals("")
   */
  public MdAttributeDAOIF definesAttribute(String attributeName)
  {
    return (MdAttributeDAOIF)super.definesAttribute(attributeName);
  }

  /**
   * Returns a MdTransientIF instance that defines the class of the given type.
   *
   * <br/><b>Precondition:</b>  type != null
   * <br/><b>Precondition:</b>  !type.trim().equals("")
   * <br/><b>Precondition:</b>  type is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdTransientIF where
   *                            (mdTransient.getType().equals(type)
   *
   * @param  type Name of the class.
   * @return MdTransientIF instance that defines the class of the given type.
   */
  public static MdTransientDAOIF getMdTransientDAO(String transientType)
  {
    return ObjectCache.getMdTransientDAO(transientType);
  }

  public String apply()
  {
    // Set the value of the published attribute to all of the children.
    // This block only needs to happen on MdBusiness.  MdStructs are at the
    // root of their own hierarchy.  MdRelationships only allow concrete clases
    // at the bottom of the hierarchy.
    if (this.getAttributeIF(MdClassInfo.PUBLISH).isModified() && !this.isImport())
    {
      if (!this.isRootOfHierarchy())
      {
        String errMsg = "Class ["+this.definesType()+"] is not the root of a hierarchy.  "+
        "Only hierarchy root classes can set the ["+MdClassInfo.PUBLISH+"] field.";
        throw new ClassPublishException(errMsg, this);
      }

      for (MdTransientDAOIF subMdTransientIF : this.getAllSubClasses())
      {
        if (subMdTransientIF.getId() == this.getId())
        {
          continue;
        }

        LockObject.getLockObject().appLock(subMdTransientIF.getId());

        MdTransientDAO subMdTransient = subMdTransientIF.getBusinessDAO();
        subMdTransient.setIsPublished(this.isPublished());
        subMdTransient.save(true);

        LockObject.getLockObject().releaseAppLock(subMdTransientIF.getId());
      }
    }    
    
    return super.apply();
  }
  
  /**
   * Updates the key on the inheritance relationship.
   * 
   * <br/>
   * <b>Precondition:</b>the key has been modified
   */
  protected abstract void updateInheritanceRelationshipKey();

  /**
   *
   */
  public String save(boolean validateRequired)
  {
    boolean applied = this.isAppliedToDB();

    String id = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      // Inheritance relationship will be imported if this object is imported
      if (this.createInheritanceRelationship()  && !this.isImport())
      {
        // extend the new class with the given super class
        MdTransientDAO superMdTransient = this.getSuperClass().getBusinessDAO();
        superMdTransient.addSubMdTransient(this);
      }

      // Default attributes will also be imported if this object is imported
      if (this.isRootOfHierarchy()  && !this.isImport())
      {
        this.copyDefaultAttributes();
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
   *
   * @param businessContext true if this is being called from a business context, false
   * otherwise. If true then cascading deletes of other Entity objects will happen at the Business
   * layer instead of the data access layer.
   *
   */
  public void delete(boolean businessContext)
  {
    this.deleteAllChildClasses(businessContext);

    this.dropAllMdMethods();

    this.dropAllAttributes(businessContext);

    this.dropTuples();

    super.delete(businessContext);
  }

  /**
   * Creates the relationship such that this class becomes superclass of the
   * given class.
   *
   * @param childMdTransientIF
   *          to become a subclass of this class.
   */
  protected abstract void addSubMdTransient(MdTransientDAOIF childMdTransientIF);

  /*
   * (non-Javadoc)
   *
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdTransientDAO getBusinessDAO()
  {
    return (MdTransientDAO) super.getBusinessDAO();
  }

  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdTransientCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN, conn);
  }

  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    if (this.isNew())
      return new JavaArtifactMdTransientCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE, conn);
    else
      return new JavaArtifactMdTransientCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE, conn);
  }

  @Override
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdTransientCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE, conn);
  }
}
