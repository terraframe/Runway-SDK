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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.SmartExceptionBaseGenerator;
import com.runwaysdk.business.generation.SmartExceptionStubGenerator;
import com.runwaysdk.business.generation.dto.SmartExceptionDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.SmartExceptionDTOStubGenerator;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdExceptionDAO extends MdLocalizableDAO implements MdExceptionDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 5560007441971218212L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdExceptionDAO()
  {
    super();
  }

  /**
   * Constructs an MdException from the given hashtable of Attributes.
   * 
   * <br/><b>Precondition:</b> attributeMap != null 
   * <br/><b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdExceptionDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdExceptionDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdExceptionDAO(attributeMap, MdExceptionInfo.CLASS);
  }
  
  /**
   * Returns a new MdView. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdView.
   */
  public static MdExceptionDAO newInstance()
  {
    return (MdExceptionDAO) BusinessDAO.newInstance(MdExceptionInfo.CLASS);
  }
  
  /**
   * Returns true if this class is the root class of a hierarchy, false otherwise.
   * @return true if this class is the root class of a hierarchy, false otherwise.
   */
  public boolean isRootOfHierarchy()
  {
    String superMdExceptionId = this.getAttributeIF(MdExceptionInfo.SUPER_MD_EXCEPTION).getValue();
    if (superMdExceptionId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   *Returns the MdExceptionIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdExceptionIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdExceptionDAOIF getRootMdClassDAO()
  {
    return (MdExceptionDAOIF)super.getRootMdClassDAO();
  }
  
  /**
   * Returns an array of MdExceptionIF that defines immediate subclasses of this class.
   * @return an array of MdExceptionIF that defines immediate subclasses of this class.
   */
  public List<MdExceptionDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.EXCEPTION_INHERITANCE.getType());

    List<MdExceptionDAOIF> mdExceptionList = new LinkedList<MdExceptionDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdExceptionList.add((MdExceptionDAOIF) relationship.getChild());
    }

    return mdExceptionList;
  }
  
  /**
   * Returns a list of MdExceptionIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdExceptionIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<MdExceptionDAOIF> getAllConcreteSubClasses()
  {
    return (List<MdExceptionDAOIF>)super.getAllConcreteSubClasses();
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
  public List<MdExceptionDAOIF> getAllSubClasses()
  {
    return (List<MdExceptionDAOIF>) super.getAllSubClasses();
  }
  
  /**
   * Returns an MdExceptionIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdExceptionIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdExceptionDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdExceptionId = this.getAttributeIF(MdExceptionInfo.SUPER_MD_EXCEPTION).getValue();
      return MdExceptionDAO.get(superMdExceptionId);
    }
    else
    {
      return null;
    }
  }
  
  /**
   * Returns a list of MdExceptionIF instances representing every
   * parent of this MdExceptionIF partaking in an inheritance relationship.
   * 
   * @return a list of MdExceptionIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<MdExceptionDAOIF> getSuperClasses()
  {
    return (List<MdExceptionDAOIF>) super.getSuperClasses();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdExceptionDAOIF get(String oid)
  {
    return (MdExceptionDAOIF) BusinessDAO.get(oid);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdExceptionDAO getBusinessDAO()
  {
    return (MdExceptionDAO) super.getBusinessDAO();
  }

  /**
   * Creates the relationship such that this class becomes superclass of the
   * given class.
   * 
   * @param childMdTransientIF
   *          to become a subclass of this class.
   */
  protected void addSubMdTransient(MdTransientDAOIF childMdTransientIF)
  {
    RelationshipDAO newChildRelDAO = this.addChild(childMdTransientIF, RelationshipTypes.EXCEPTION_INHERITANCE.getType());
    newChildRelDAO.setKey(childMdTransientIF.getKey());
    newChildRelDAO.save(true);
  }
  
  /**
   * Updates the key on the inheritance relationship.
   * 
   * <br/>
   * <b>Precondition:</b>the key has been modified
   */
  protected void updateInheritanceRelationshipKey()
  {
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.EXCEPTION_INHERITANCE.getType());
    
    for (RelationshipDAOIF parentInheritanceDAOIF : parentInheritances)
    {
      RelationshipDAO parentInheritanceDAO = parentInheritanceDAOIF.getRelationshipDAO();
      parentInheritanceDAO.setKey(this.getKey());
      parentInheritanceDAO.save(true);
    }
  }
  
  /**
   * Returns an MdExceptionIF instance of the metadata for the given type. 
   * 
   * <br/><b>Precondition:</b>  exceptionType != null
   * <br/><b>Precondition:</b>  !exceptionType.trim().equals("")
   * <br/><b>Precondition:</b>  exceptionType is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdExceptionIF instance of the metadata for the 
   *                            given class
   *                            (MdExceptionIF().definesType().equals(exceptionType)
   * 
   * @param  exceptionType 
   * @return MdExceptionIF instance of the metadata for the given type.
   */
  public static MdExceptionDAOIF getMdException(String exceptionType)
  {
    return ObjectCache.getMdExceptionDAO(exceptionType);
  }
  
  public String toString()
  {
    return '[' + this.definesType() + " definition]";
  }

  /**
   * Returns a list of all generators used to generate source
   * for this MdType.
   * 
   * @return
   */
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();
    
    //Dont generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }
    
    list.add(new SmartExceptionBaseGenerator(this));
    list.add(new SmartExceptionStubGenerator(this));
    list.add(new SmartExceptionDTOBaseGenerator(this));      
    list.add(new SmartExceptionDTOStubGenerator(this));   
    
    return list;
  }
}
