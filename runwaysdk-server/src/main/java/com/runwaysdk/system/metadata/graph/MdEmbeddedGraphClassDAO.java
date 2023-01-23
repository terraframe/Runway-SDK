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
package com.runwaysdk.system.metadata.graph;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.graph.generation.EmbeddedGraphObjectBaseGenerator;
import com.runwaysdk.business.graph.generation.EmbeddedGraphObjectStubGenerator;
import com.runwaysdk.business.graph.generation.JavaArtifactMdGraphClassCommand;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.graph.MdEmbeddedGraphClassInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;

public class MdEmbeddedGraphClassDAO extends MdGraphClassDAO implements MdEmbeddedGraphClassDAOIF
{
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -451456021;
  
  public MdEmbeddedGraphClassDAO()
  {
    super();
  }
  
  /**
   * Constructs a {@link MdGraphClassDAO} from the given hashtable of
   * Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   *
   *
   * @param attributeMap
   * @param type
   */
  public MdEmbeddedGraphClassDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }
  
  /**
   * Returns a new {@link MdEmbeddedGraphClassDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return instance of {@link MdEmbeddedGraphClassDAO}.
   */
  public static MdEmbeddedGraphClassDAO newInstance()
  {
    return (MdEmbeddedGraphClassDAO) BusinessDAO.newInstance(MdEmbeddedGraphClassInfo.CLASS);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  @Override
  public MdEmbeddedGraphClassDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdEmbeddedGraphClassDAO(attributeMap, MdEmbeddedGraphClassInfo.CLASS);
  }

  @Override
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Don't generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }

    list.add(new EmbeddedGraphObjectBaseGenerator(this));
    list.add(new EmbeddedGraphObjectStubGenerator(this));

    return list;
  }

  @Override
  public List<? extends MdEmbeddedGraphClassDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.EMBEDDED_GRAPH_INHERITANCE.getType());

    List<MdEmbeddedGraphClassDAOIF> mdViewList = new LinkedList<MdEmbeddedGraphClassDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdViewList.add((MdEmbeddedGraphClassDAOIF) relationship.getChild());
    }

    return mdViewList;
  }

  @Override
  public MdEmbeddedGraphClassDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdVertexId = this.getAttributeIF(MdEmbeddedGraphClassInfo.SUPER_MD_RELATIONSHIP).getValue();
      return MdEmbeddedGraphClassDAO.get(superMdVertexId);
    }
    else
    {
      return null;
    }
  }
  
  @Override
  public String save(boolean validateRequired)
  {
    boolean applied = this.isAppliedToDB();

    String oid = super.save(validateRequired);

    if (this.isNew() && !applied)
    {
      // Inheritance relationship will be imported if this object is imported
      if (this.createInheritanceRelationship() && !this.isImport())
      {
        // extend the new class with the given super class
        MdEmbeddedGraphClassDAO superMdEmbedded = this.getSuperClass().getBusinessDAO();
        superMdEmbedded.addSubMdEmbeddedGraph(this);
      }

    }
    // !this.isNew() || applied
    else if (this.getAttribute(EntityInfo.KEY).isModified())
    {
      this.updateInheritanceRelationshipKey();
    }

    return oid;
  }
  
  /**
   * Updates the key on the inheritance relationship.
   * 
   * <br/>
   * <b>Precondition:</b>the key has been modified
   */
  protected void updateInheritanceRelationshipKey()
  {
    List<RelationshipDAOIF> parentInheritances = this.getParents(RelationshipTypes.EMBEDDED_GRAPH_INHERITANCE.getType());

    for (RelationshipDAOIF parentInheritanceDAOIF : parentInheritances)
    {
      RelationshipDAO parentInheritanceDAO = parentInheritanceDAOIF.getRelationshipDAO();
      parentInheritanceDAO.setKey(this.getKey());
      parentInheritanceDAO.save(true);
    }
  }
  
  /**
   * Creates the relationship such that this class becomes superclass of the
   * given class.
   * 
   * @param childMdVertexDAOIF
   *          to become a subclass of this class.
   */
  protected void addSubMdEmbeddedGraph(MdEmbeddedGraphClassDAOIF childMdVertexDAOIF)
  {
    RelationshipDAO newChildRelDAO = this.addChild(childMdVertexDAOIF, RelationshipTypes.EMBEDDED_GRAPH_INHERITANCE.getType());
    newChildRelDAO.setKey(childMdVertexDAOIF.getKey());
    newChildRelDAO.save(true);
  }

  @Override
  protected void createClassInDB()
  {
    String superMdEmbeddedOid = this.getAttributeIF(MdEmbeddedGraphClassInfo.SUPER_MD_RELATIONSHIP).getValue();
    String dbClassName = this.getAttributeIF(MdEmbeddedGraphClassInfo.DB_CLASS_NAME).getValue();
    String superClassName = null;

    if (superMdEmbeddedOid != null && superMdEmbeddedOid.length() > 0)
    {
      MdEmbeddedGraphClassDAOIF mdEmbedded = MdEmbeddedGraphClassDAO.get(superMdEmbeddedOid);

      superClassName = mdEmbedded.getDBClassName();
    }

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createEmbeddedClass(graphRequest, graphDDLRequest, dbClassName, superClassName);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().deleteEmbeddedClass(graphRequest, graphDDLRequest, dbClassName);

    GraphDDLCommand command = new GraphDDLCommand(doItAction, undoItAction, false);
    command.doIt();
  }

  @Override
  protected void deleteClassInDB()
  {
    String superMdVertexOid = this.getAttributeIF(MdEmbeddedGraphClassInfo.SUPER_MD_RELATIONSHIP).getValue();
    String dbClassName = this.getAttributeIF(MdEmbeddedGraphClassInfo.DB_CLASS_NAME).getValue();
    String superClassName = null;

    if (superMdVertexOid != null && superMdVertexOid.length() > 0)
    {
      MdVertexDAOIF mdVertex = MdVertexDAO.get(superMdVertexOid);

      superClassName = mdVertex.getDBClassName();
    }

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().deleteVertexClass(graphRequest, graphDDLRequest, dbClassName);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createVertexClass(graphRequest, graphDDLRequest, dbClassName, superClassName);

    GraphDDLCommand command = new GraphDDLCommand(doItAction, undoItAction, true);
    command.doIt();
  }

  @Override
  public boolean isAbstract()
  {
    return false;
  }

  @Override
  public boolean isExtendable()
  {
    return true;
  }

  /**
   * Returns true if this class is the root class of a hierarchy, false
   * otherwise.
   * 
   * @return true if this class is the root class of a hierarchy, false
   *         otherwise.
   */
  @Override
  public boolean isRootOfHierarchy()
  {
    String superMdViewId = this.getAttributeIF(MdEmbeddedGraphClassInfo.SUPER_MD_RELATIONSHIP).getValue();
    if (superMdViewId.trim().equals(""))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    if (this.isNew())
    {
      return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE);
    }

    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE);
  }

  @Override
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE);
  }

  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN);
  }
  
  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdEmbeddedGraphClassDAOIF get(String oid)
  {
    return (MdEmbeddedGraphClassDAOIF) BusinessDAO.get(oid);
  }
  
  /**
   * Returns an {@link MdEmbeddedGraphClassDAOIF} instance of the metadata for the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> classType != null <br/>
   * <b>Precondition:</b> !classType.trim().equals("") <br/>
   * <b>Precondition:</b> classType is a valid class defined in the database
   * <br/>
   * <b>Postcondition:</b> Returns a {@link MdEmbeddedGraphClassDAOIF} instance of the
   * metadata for the given class
   * ({@link MdEmbeddedGraphClassDAOIF}().definesType().equals(classType)
   * 
   * @param classType
   * @return {@link MdEmbeddedGraphClassDAOIF} instance of the metadata for the given type.
   */
  public static MdEmbeddedGraphClassDAOIF getMdEmbeddedGraphClassDAO(String classType)
  {
    return ObjectCache.getMdEmbeddedGraphClassDAO(classType);
  }
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  @Override
  public MdEmbeddedGraphClassDAO getBusinessDAO()
  {
    return (MdEmbeddedGraphClassDAO) super.getBusinessDAO();
  }
  
}
