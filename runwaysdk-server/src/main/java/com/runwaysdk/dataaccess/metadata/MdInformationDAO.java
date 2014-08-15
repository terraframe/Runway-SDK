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

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.InformationBaseGenerator;
import com.runwaysdk.business.generation.InformationStubGenerator;
import com.runwaysdk.business.generation.dto.InformationDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.InformationDTOStubGenerator;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdInformationDAO extends MdMessageDAO implements MdInformationDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -6646591440208351927L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdInformationDAO()
  {
    super();
  }

  /**
   * Constructs a MdInformation from the given hashtable of Attributes.
   * 
   * <br/><b>Precondition:</b> attributeMap != null 
   * <br/><b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdInformationDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }


  /**
   * Returns an MdInformationIF instance of the metadata for the given type. 
   * 
   * <br/><b>Precondition:</b>  informationType != null
   * <br/><b>Precondition:</b>  !informationType.trim().equals("")
   * <br/><b>Precondition:</b>  informationType is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdInformationIF instance of the metadata for the 
   *                            given class
   *                            (MdInformationIF().definesType().equals(informationType)
   * 
   * @param  informationType 
   * @return MdInformationIF instance of the metadata for the given type.
   */
  public static MdInformationDAOIF getMdInformation(String informationType)
  {
    return ObjectCache.getMdInformationDAO(informationType);
  }
  
  /**
   * Returns a new MdInformation. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdInformation.
   */
  public static MdInformationDAO newInstance()
  {
    return (MdInformationDAO) BusinessDAO.newInstance(MdInformationInfo.CLASS);
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdInformationDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdInformationDAO(attributeMap, MdInformationInfo.CLASS);
  }

  
  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdInformationDAOIF get(String id)
  {
    return (MdInformationDAOIF) BusinessDAO.get(id);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdInformationDAO getBusinessDAO()
  {
    return (MdInformationDAO) super.getBusinessDAO();
  }
  
  /**
   * Returns true if this class is the root class of a hierarchy, false otherwise.
   * @return true if this class is the root class of a hierarchy, false otherwise.
   */
  public boolean isRootOfHierarchy()
  {
    String superMdInformationId = this.getAttributeIF(MdInformationInfo.SUPER_MD_INFORMATION).getValue();
    if (superMdInformationId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   *Returns the MdInformationIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdInformationIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdInformationDAOIF getRootMdClassDAO()
  {
    return (MdInformationDAOIF)super.getRootMdClassDAO();
  }

  /**
   * Returns an array of MdInformationIF that defines immediate subclasses of this class.
   * @return an array of MdInformationIF that defines immediate subclasses of this class.
   */
  public List<MdInformationDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.INFORMATION_INHERITANCE.getType());

    List<MdInformationDAOIF> mdInformationList = new LinkedList<MdInformationDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdInformationList.add((MdInformationDAOIF) relationship.getChild());
    }

    return mdInformationList;
  }

  /**
   *Returns a list of MdInformationIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   *
   * @return list of MdInformationIF objects that represent entites 
   * that are subclasses of the given entity, including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<MdInformationDAOIF> getAllSubClasses()
  {
    return (List<MdInformationDAOIF>)super.getAllSubClasses();
  }

  
  /**
   * 
   * 
   * Returns a list of MdInformationIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdInformationIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<MdInformationDAOIF> getAllConcreteSubClasses()
  {
    return (List<MdInformationDAOIF>)super.getAllConcreteSubClasses();
  }
  
  /**
   * Returns an MdInformationIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdInformationIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdInformationDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdInformationId = this.getAttributeIF(MdInformationInfo.SUPER_MD_INFORMATION).getValue();
      return MdInformationDAO.get(superMdInformationId);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a list of MdInformationIF instances representing every
   * parent of this MdInformationIF partaking in an inheritance relationship.
   * 
   * @return a list of MdInformationIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<MdInformationDAOIF> getSuperClasses()
  {
    return (List<MdInformationDAOIF>) super.getSuperClasses();
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
    RelationshipDAO newChildRelDAO = this.addChild(childMdTransientIF, RelationshipTypes.INFORMATION_INHERITANCE.getType());
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
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.INFORMATION_INHERITANCE.getType());
    
    for (RelationshipDAOIF parentInheritanceDAOIF : parentInheritances)
    {
      RelationshipDAO parentInheritanceDAO = parentInheritanceDAOIF.getRelationshipDAO();
      parentInheritanceDAO.setKey(this.getKey());
      parentInheritanceDAO.save(true);
    }
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
    if (GenerationUtil.isReservedType(this))
    {
      return list;
    }
    
    list.add(new InformationBaseGenerator(this));
    list.add(new InformationStubGenerator(this));
    list.add(new InformationDTOBaseGenerator(this));
    list.add(new InformationDTOStubGenerator(this));
    
    return list;
  }
}
