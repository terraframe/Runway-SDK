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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.UtilBaseGenerator;
import com.runwaysdk.business.generation.UtilStubGenerator;
import com.runwaysdk.business.generation.dto.UtilDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.UtilDTOStubGenerator;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdUtilDAO extends MdSessionDAO implements MdUtilDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -1411900830239586379L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdUtilDAO()
  {
    super();
  }

  /**
   * Constructs a MdUtil from the given hashtable of Attributes.
   * 
   * <br/><b>Precondition:</b> attributeMap != null 
   * <br/><b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdUtilDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdUtilDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdUtilDAO(attributeMap, MdUtilInfo.CLASS);
  }
  
  /**
   * Returns a new MdUtil. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdUtil.
   */
  public static MdUtilDAO newInstance()
  {
    return (MdUtilDAO) BusinessDAO.newInstance(MdUtilInfo.CLASS);
  }

  /**
   * Returns true if this class is the root class of a hierarchy, false
   * otherwise.
   * 
   * @return true if this class is the root class of a hierarchy, false
   *         otherwise.
   */
  public boolean isRootOfHierarchy()
  {
    String superMdUtilId = this.getAttributeIF(MdUtilInfo.SUPER_MD_UTIL).getValue();
    if (superMdUtilId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   *Returns the MdUtilIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdUtilIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdUtilDAOIF getRootMdClassDAO()
  {
    return (MdUtilDAOIF)super.getRootMdClassDAO();
  }

  /**
   * Returns an array of MdUtilIF that defines immediate subclasses of this class.
   * @return an array of MdUtilIF that defines immediate subclasses of this class.
   */
  public List<MdUtilDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.UTIL_INHERITANCE.getType());

    List<MdUtilDAOIF> mdUtilList = new LinkedList<MdUtilDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdUtilList.add((MdUtilDAOIF) relationship.getChild());
    }

    return mdUtilList;
  }
  
  /**
   * Returns a list of MdUtilIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdUtilIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdUtilDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdUtilDAOIF>)super.getAllConcreteSubClasses();
  }

  /**
   *Returns a list of MdUtilIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdUtilIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdUtilDAOIF> getAllSubClasses()
  {
    return (List<? extends MdUtilDAOIF>)super.getAllSubClasses();
  }

  /**
   * Returns an MdUtilIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdUtilIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdUtilDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdUtilId = this.getAttributeIF(MdUtilInfo.SUPER_MD_UTIL).getValue();
      return MdUtilDAO.get(superMdUtilId);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a list of MdUtilIF instances representing every
   * parent of this MdUtilIF partaking in an inheritance relationship.
   * 
   * @return a list of MdUtilIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<MdUtilDAOIF> getSuperClasses()
  {
    return (List<MdUtilDAOIF>) super.getSuperClasses();
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
    RelationshipDAO newChildRelDAO = this.addChild(childMdTransientIF, RelationshipTypes.UTIL_INHERITANCE.getType());
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
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.UTIL_INHERITANCE.getType());
    
    for (RelationshipDAOIF parentInheritanceDAOIF : parentInheritances)
    {
      RelationshipDAO parentInheritanceDAO = parentInheritanceDAOIF.getRelationshipDAO();
      parentInheritanceDAO.setKey(this.getKey());
      parentInheritanceDAO.save(true);
    }
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdUtilDAOIF get(String oid)
  {
    return (MdUtilDAOIF) BusinessDAO.get(oid);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdUtilDAO getBusinessDAO()
  {
    return (MdUtilDAO) super.getBusinessDAO();
  }
  
  /**
   * Returns an MdUtilIF instance of the metadata for the given type. 
   * 
   * <br/><b>Precondition:</b>  utilType != null
   * <br/><b>Precondition:</b>  !utilType.trim().equals("")
   * <br/><b>Precondition:</b>  utilType is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdUtilIF instance of the metadata for the 
   *                            given class
   *                            (MdUtilIF().definesType().equals(utilType)
   * 
   * @param  veiwType 
   * @return MdUtilIF instance of the metadata for the given type.
   */
  public static MdUtilDAOIF getMdUtil(String utilType)
  {
    return ObjectCache.getMdUtilDAO(utilType);
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
    
    list.add(new UtilBaseGenerator(this));
    list.add(new UtilStubGenerator(this));
    list.add(new UtilDTOBaseGenerator(this));
    list.add(new UtilDTOStubGenerator(this));
    
    return list;
  }

}
