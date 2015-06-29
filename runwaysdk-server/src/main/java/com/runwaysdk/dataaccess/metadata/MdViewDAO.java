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

import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.generation.JavaArtifactMdViewCommand;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.business.generation.ViewBaseGenerator;
import com.runwaysdk.business.generation.ViewQueryBaseAPIGenerator;
import com.runwaysdk.business.generation.ViewQueryStubAPIGenerator;
import com.runwaysdk.business.generation.ViewStubGenerator;
import com.runwaysdk.business.generation.dto.ViewDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.ViewDTOStubGenerator;
import com.runwaysdk.business.generation.dto.ViewQueryDTOGenerator;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdAttributeVirtualDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.util.FileIO;

public class MdViewDAO extends MdSessionDAO implements MdViewDAOIF
{

  /**
   *
   */
  private static final long serialVersionUID = -4400952097016420516L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdViewDAO()
  {
    super();
  }

  /**
   * Constructs a MdView from the given hashtable of Attributes.
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
  public MdViewDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdViewDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdViewDAO(attributeMap, MdViewInfo.CLASS);
  }

  /**
   * Returns a new MdView. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * @return instance of MdView.
   */
  public static MdViewDAO newInstance()
  {
    return (MdViewDAO) BusinessDAO.newInstance(MdViewInfo.CLASS);
  }

  /**
   * Creates the relationship such that this type defines the given attribute.
   * 
   * @param mdAttributeVirtualIF
   *          the attribute to add to this type.
   */
  protected void addAttributeVirtual(MdAttributeVirtualDAOIF mdAttributeVirtualDAOIF)
  {
    RelationshipDAO newChildRelDAO = this.addChild(mdAttributeVirtualDAOIF, RelationshipTypes.CLASS_ATTRIBUTE_VIRTUAL.getType());
    newChildRelDAO.setKey(MdAttributeVirtualDAO.buildClassAttrVirtualKey(mdAttributeVirtualDAOIF));
    newChildRelDAO.save(true);
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
    String superMdViewId = this.getAttributeIF(MdViewInfo.SUPER_MD_VIEW).getValue();
    if (superMdViewId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Returns the MdViewIF that is the root of the hierarchy that this type
   * belongs to. returns a reference to itself if it is the root.
   * 
   * @return MdViewIF that is the root of the hierarchy that this type belongs
   *         to. returns a reference to inself if it is the root.
   */
  public MdViewDAOIF getRootMdClassDAO()
  {
    return (MdViewDAOIF) super.getRootMdClassDAO();
  }

  /**
   * Returns an array of MdViewIF that defines immediate subclasses of this
   * class.
   * 
   * @return an array of MdViewIF that defines immediate subclasses of this
   *         class.
   */
  public List<MdViewDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.VIEW_INHERITANCE.getType());

    List<MdViewDAOIF> mdViewList = new LinkedList<MdViewDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdViewList.add((MdViewDAOIF) relationship.getChild());
    }

    return mdViewList;
  }

  /**
   * Returns a list of MdViewIF objects that are subclasses of the given entity.
   * Only non abstract entities are returned (i.e. entities that can be
   * instantiated)
   * 
   * @return list of MdViewIF objects that are subclasses of the given entity.
   *         Only non abstract entities are returned (i.e. entities that can be
   *         instantiated)
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdViewDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdViewDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * Returns a list of MdViewIF objects that represent entites that are
   * subclasses of the given entity, including all recursive entities.
   * 
   * @return list of MdViewIF objects that represent entites that are subclasses
   *         of the given entity, including all recursive entities.
   */
  @SuppressWarnings("unchecked")
  public List<? extends MdViewDAOIF> getAllSubClasses()
  {
    return (List<? extends MdViewDAOIF>) super.getAllSubClasses();
  }

  /**
   * Returns an MdViewIF representing the superclass of this class, or null if
   * this class is basic.
   * 
   * @return an MdViewIF representing the superclass of this class, or null if
   *         the class is basic.
   */
  public MdViewDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdViewId = this.getAttributeIF(MdViewInfo.SUPER_MD_VIEW).getValue();
      return MdViewDAO.get(superMdViewId);
    }
    else
    {
      return null;
    }
  }

  /**
   * Returns a list of MdViewIF instances representing every parent of this
   * MdViewIF partaking in an inheritance relationship.
   * 
   * @return a list of MdViewIF instances that are parents of this class.
   */
  @SuppressWarnings("unchecked")
  public List<MdViewDAOIF> getSuperClasses()
  {
    return (List<MdViewDAOIF>) super.getSuperClasses();
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
    RelationshipDAO newChildRelDAO = this.addChild(childMdTransientIF, RelationshipTypes.VIEW_INHERITANCE.getType());
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
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.VIEW_INHERITANCE.getType());
    
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
  public static MdViewDAOIF get(String id)
  {
    return (MdViewDAOIF) BusinessDAO.get(id);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdViewDAO getBusinessDAO()
  {
    return (MdViewDAO) super.getBusinessDAO();
  }

  /**
   * Returns an MdViewIF instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> veiwType != null <br/>
   * <b>Precondition:</b> !veiwType.trim().equals("") <br/>
   * <b>Precondition:</b> veiwType is a valid class defined in the database <br/>
   * <b>Postcondition:</b> Returns a MdViewIF instance of the metadata for the
   * given class (MdViewIF().definesType().equals(veiwType)
   * 
   * @param veiwType
   * @return MdViewIF instance of the metadata for the given type.
   */
  public static MdViewDAOIF getMdViewDAO(String viewType)
  {
    return ObjectCache.getMdViewDAO(viewType);
  }

  public String toString()
  {
    return '[' + this.definesType() + " definition]";
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
    if (GenerationUtil.isReservedType(this))
    {
      return list;
    }

    list.add(new ViewBaseGenerator(this));
    list.add(new ViewStubGenerator(this));
    list.add(new ViewDTOBaseGenerator(this));
    list.add(new ViewDTOStubGenerator(this));
    list.add(new ViewQueryBaseAPIGenerator(this));
    list.add(new ViewQueryStubAPIGenerator(this));
    list.add(new ViewQueryDTOGenerator(this));

    return list;
  }

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
      command = new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE, conn);

    }
    else
    {
      command = new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE, conn);
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
    return new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE, conn);
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
    return new JavaArtifactMdViewCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN, conn);
  }

  /**
   * Copies all Java source and class files from this object into files on the
   * file system.
   */
  public void writeJavaToFile()
  {
    super.writeJavaToFile();

    // Write the query and base .class files to the filesystem
    byte[] queryBaseClasses = this.getBlob(MdViewInfo.QUERY_BASE_CLASS);
    String queryBaseSource = this.getAttribute(MdViewInfo.QUERY_BASE_SOURCE).getValue();

    byte[] queryStubClasses = this.getBlob(MdViewInfo.QUERY_STUB_CLASS);
    String queryStubSource = this.getAttribute(MdViewInfo.QUERY_STUB_SOURCE).getValue();

    byte[] queryDTOclasses = this.getBlob(MdViewInfo.QUERY_DTO_CLASS);
    String queryDTOsource = this.getAttribute(MdViewInfo.QUERY_DTO_SOURCE).getValue();
    try
    {
      ClassManager.writeClasses(TypeGenerator.getQueryAPIclassDirectory(this), queryBaseClasses);
      FileIO.write(TypeGenerator.getBaseQueryAPIsourceFilePath(this), queryBaseSource);

      ClassManager.writeClasses(TypeGenerator.getQueryAPIclassDirectory(this), queryStubClasses);
      FileIO.write(TypeGenerator.getStubQueryAPIsourceFilePath(this), queryStubSource);

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

      String queryBaseSource = GenerationFacade.getQueryAPIbaseSource(this);
      byte[] queryBaseClassBytes = GenerationFacade.getQueryBaseAPIclasses(this);

      String queryBaseSourceColumnName = MdViewDAOIF.QUERY_BASE_SOURCE_COLUMN;
      String queryBaseClassColumnName = MdViewDAOIF.QUERY_BASE_CLASS_COLUMN;

      if (queryBaseSource != null && queryBaseClassBytes != null)
      {
        Database.updateClassAndSource(this.getId(), MdViewDAOIF.TABLE, queryBaseClassColumnName, queryBaseClassBytes, queryBaseSourceColumnName, queryBaseSource, conn);

        this.getAttribute(MdViewInfo.QUERY_BASE_SOURCE).setValue(queryBaseSource);
        this.getAttribute(MdViewInfo.QUERY_BASE_CLASS).setModified(true);
      }

      String queryStubSource = GenerationFacade.getQueryAPIstubSource(this);
      byte[] queryStubClassBytes = GenerationFacade.getQueryStubAPIclasses(this);

      String queryStubSourceColumnName = MdViewDAOIF.QUERY_STUB_SOURCE_COLUMN;
      String queryStubClassColumnName = MdViewDAOIF.QUERY_STUB_CLASS_COLUMN;

      if (queryStubSource != null && queryStubClassBytes != null)
      {
        Database.updateClassAndSource(this.getId(), MdViewDAOIF.TABLE, queryStubClassColumnName, queryStubClassBytes, queryStubSourceColumnName, queryStubSource, conn);

        this.getAttribute(MdViewInfo.QUERY_STUB_SOURCE).setValue(queryStubSource);
        this.getAttribute(MdViewInfo.QUERY_STUB_CLASS).setModified(true);
      }

      byte[] queryDTOclassBytes = new byte[0];
      String queryDTOsource = "";

      // Only update DTO Java artifacts for classes that are published.
      if (this.isPublished())
      {
        queryDTOsource = GenerationFacade.getQueryDTOsource(this);
        queryDTOclassBytes = GenerationFacade.getQueryDTOclasses(this);
      }

      String queryDTOsourceColumnName = MdViewDAOIF.QUERY_DTO_SOURCE_COLUMN;
      String queryDTOclassColumnName = MdViewDAOIF.QUERY_DTO_CLASS_COLUMN;

      if (queryDTOsource != null && queryDTOclassBytes != null)
      {
        Database.updateClassAndSource(this.getId(), MdViewDAOIF.TABLE, queryDTOclassColumnName, queryDTOclassBytes, queryDTOsourceColumnName, queryDTOsource, conn);

        this.getAttribute(MdViewInfo.QUERY_DTO_SOURCE).setValue(queryDTOsource);
        this.getAttribute(MdViewInfo.QUERY_DTO_CLASS).setModified(true);
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
      if (this.getAttribute(MdViewInfo.QUERY_BASE_SOURCE).isModified() || this.getAttribute(MdViewInfo.QUERY_STUB_SOURCE).isModified() || this.getAttribute(MdViewInfo.QUERY_DTO_SOURCE).isModified() ||

      this.getAttribute(MdViewInfo.QUERY_BASE_CLASS).isModified() || this.getAttribute(MdViewInfo.QUERY_STUB_CLASS).isModified() || this.getAttribute(MdViewInfo.QUERY_DTO_CLASS).isModified())
      {
        return true;
      }
    }

    return false;
  }
}
