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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.ProblemBaseGenerator;
import com.runwaysdk.business.generation.ProblemStubGenerator;
import com.runwaysdk.business.generation.dto.ProblemDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.ProblemDTOStubGenerator;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdProblemDAO extends MdNotificationDAO implements MdProblemDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -3601214394997161558L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdProblemDAO()
  {
    super();
  }

  /**
   * Constructs a MdProblem from the given hashtable of Attributes.
   * 
   * <br/><b>Precondition:</b> attributeMap != null 
   * <br/><b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdProblemDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdProblemDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdProblemDAO(attributeMap, MdProblemInfo.CLASS);
  }
  
  /**
   * Returns a new MdProblem. 
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdView.
   */
  public static MdProblemDAO newInstance()
  {
    return (MdProblemDAO) BusinessDAO.newInstance(MdProblemInfo.CLASS);
  }

  /**
   * Returns true if this class is the root class of a hierarchy, false otherwise.
   * @return true if this class is the root class of a hierarchy, false otherwise.
   */
  public boolean isRootOfHierarchy()
  {
    String superMdProblemId = this.getAttributeIF(MdProblemInfo.SUPER_MD_PROBLEM).getValue();
    if (superMdProblemId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /**
   *Returns the MdProblemIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   *
   * @return MdProblemIF that is the root of the hierarchy that this type belongs to.
   * returns a reference to inself if it is the root.
   */
  public MdProblemDAOIF getRootMdClassDAO()
  {
    return (MdProblemDAOIF)super.getRootMdClassDAO();
  }

  /**
   * Returns an array of MdProblemIF that defines immediate subclasses of this class.
   * @return an array of MdProblemIF that defines immediate subclasses of this class.
   */
  public List<MdProblemDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.PROBLEM_INHERITANCE.getType());

    List<MdProblemDAOIF> mdProblemList = new LinkedList<MdProblemDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdProblemList.add((MdProblemDAOIF) relationship.getChild());
    }

    return mdProblemList;
  }
  
  /**
   * Returns a list of MdProblemIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdProblemIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<MdProblemDAOIF> getAllConcreteSubClasses()
  {
    return (List<MdProblemDAOIF>)super.getAllConcreteSubClasses();
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
  public List<MdProblemDAOIF> getAllSubClasses()
  {
    return (List<MdProblemDAOIF>)super.getAllSubClasses();
  }

  /**
   * Returns an MdProblemIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdProblemIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdProblemDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdProblemId = this.getAttributeIF(MdProblemInfo.SUPER_MD_PROBLEM).getValue();
      return MdProblemDAO.get(superMdProblemId);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a list of MdProblemIF instances representing every
   * parent of this MdProblemIF partaking in an inheritance relationship.
   * 
   * @return a list of MdProblemIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<MdProblemDAOIF> getSuperClasses()
  {
    return (List<MdProblemDAOIF>) super.getSuperClasses();
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
    RelationshipDAO newChildRelDAO = this.addChild(childMdTransientIF, RelationshipTypes.PROBLEM_INHERITANCE.getType());
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
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.PROBLEM_INHERITANCE.getType());
    
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
  public static MdProblemDAOIF get(String id)
  {
    return (MdProblemDAOIF) BusinessDAO.get(id);
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdProblemDAO getBusinessDAO()
  {
    return (MdProblemDAO) super.getBusinessDAO();
  }
  
  /**
   * Returns an MdProblemIF instance of the metadata for the given type. 
   * 
   * <br/><b>Precondition:</b>  problemType != null
   * <br/><b>Precondition:</b>  !problemType.trim().equals("")
   * <br/><b>Precondition:</b>  problemType is a valid class defined in the database
   * <br/><b>Postcondition:</b> Returns a MdProblemIF instance of the metadata for the 
   *                            given class
   *                            (MdProblemIF().definesType().equals(problemType)
   * 
   * @param  problemType 
   * @return MdProblemIF instance of the metadata for the given type.
   */
  public static MdProblemDAOIF getMdProblem(String problemType)
  {
    return ObjectCache.getMdProblemDAO(problemType);
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
    
    list.add(new ProblemBaseGenerator(this));
    list.add(new ProblemStubGenerator(this));
    list.add(new ProblemDTOBaseGenerator(this));
    list.add(new ProblemDTOStubGenerator(this));
    
    return list;
  }
  
  public String toString()
  {
    return '[' + this.definesType() + " definition]";
  }
  
}
