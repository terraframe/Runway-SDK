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
import com.runwaysdk.business.generation.WarningBaseGenerator;
import com.runwaysdk.business.generation.WarningStubGenerator;
import com.runwaysdk.business.generation.dto.WarningDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.WarningDTOStubGenerator;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdWarningDAO extends MdMessageDAO implements MdWarningDAOIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 2000690945867902742L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdWarningDAO()
  {
    super();
  }

  /**
   * Constructs a MdWarning from the given hashtable of Attributes.
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
  public MdWarningDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * Returns an MdWarningIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> warningType != null <br/>
   * <b>Precondition:</b> !warningType.trim().equals("") <br/>
   * <b>Precondition:</b> warningType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdWarningIF instance of the metadata for
   * the given class (MdWarningIF().definesType().equals(warningType)
   * 
   * @param warningType
   * @return MdWarningIF instance of the metadata for the given type.
   */
  public static MdWarningDAOIF getMdMessage(String warningType)
  {
    return ObjectCache.getMdWarningDAO(warningType);
  }

  /**
   * Returns a new MdProblem. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdView.
   */
  public static MdWarningDAO newInstance()
  {
    return (MdWarningDAO) BusinessDAO.newInstance(MdWarningInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdWarningDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWarningDAO(attributeMap, MdWarningInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdWarningDAOIF get(String oid)
  {
    return (MdWarningDAOIF) BusinessDAO.get(oid);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdWarningDAO getBusinessDAO()
  {
    return (MdWarningDAO) super.getBusinessDAO();
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
    String superMdWarningId = this.getAttributeIF(MdWarningInfo.SUPER_MD_WARNING).getValue();
    if (superMdWarningId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   *Returns the MdWarningIF that is the root of the hierarchy that this type
   * belongs to. returns a reference to inself if it is the root.
   * 
   * @return MdWarningIF that is the root of the hierarchy that this type
   *         belongs to. returns a reference to inself if it is the root.
   */
  public MdWarningDAOIF getRootMdClassDAO()
  {
    return (MdWarningDAOIF) super.getRootMdClassDAO();
  }

  /**
   * Returns an array of MdWarningIF that defines immediate subclasses of this
   * class.
   * 
   * @return an array of MdWarningIF that defines immediate subclasses of this
   *         class.
   */
  public List<MdWarningDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.WARNING_INHERITANCE.getType());

    List<MdWarningDAOIF> mdWarningList = new LinkedList<MdWarningDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdWarningList.add((MdWarningDAOIF) relationship.getChild());
    }

    return mdWarningList;
  }

  /**
   *Returns a list of MdWarningIF objects that represent entites that are
   * subclasses of the given entity, including all recursive entities.
   * 
   * @return list of MdWarningIF objects that represent entites that are
   *         subclasses of the given entity, including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<MdWarningDAOIF> getAllSubClasses()
  {
    return (List<MdWarningDAOIF>) super.getAllSubClasses();
  }

  /**
   * 
   * 
   * Returns a list of MdWarningIF objects that are subclasses of the given
   * entity. Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdWarningIF objects that are subclasses of the given
   *         entity. Only non abstract entities are returned (i.e. entities that
   *         can be instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<MdWarningDAOIF> getAllConcreteSubClasses()
  {
    return (List<MdWarningDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * Returns an MdWarningIF representing the superclass of this class, or null
   * if this class is basic.
   * 
   * @return an MdWarningIF representing the superclass of this class, or null
   *         if the class is basic.
   */
  public MdWarningDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdWarningId = this.getAttributeIF(MdWarningInfo.SUPER_MD_WARNING).getValue();
      return MdWarningDAO.get(superMdWarningId);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a list of MdWarningIF instances representing every parent of this
   * MdWarningIF partaking in an inheritance relationship.
   * 
   * @return a list of MdWarningIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<MdWarningDAOIF> getSuperClasses()
  {
    return (List<MdWarningDAOIF>) super.getSuperClasses();
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
    RelationshipDAO newChildRelDAO = this.addChild(childMdTransientIF, RelationshipTypes.WARNING_INHERITANCE.getType());
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
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.WARNING_INHERITANCE.getType());
    
    for (RelationshipDAOIF parentInheritanceDAOIF : parentInheritances)
    {
      RelationshipDAO parentInheritanceDAO = parentInheritanceDAOIF.getRelationshipDAO();
      parentInheritanceDAO.setKey(this.getKey());
      parentInheritanceDAO.save(true);
    }
  }

  /**
   * Returns a list of all generators used to generate source for this MdType.
   * 
   * @return
   */
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Dont generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }

    list.add(new WarningBaseGenerator(this));
    list.add(new WarningStubGenerator(this));
    list.add(new WarningDTOBaseGenerator(this));
    list.add(new WarningDTOStubGenerator(this));

    return list;
  }

  public static MdWarningDAOIF getMdWarning(String warningType)
  {
    return (MdWarningDAOIF) MdClassDAO.getMdClassDAO(warningType);
  }
}
