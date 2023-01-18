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
package com.runwaysdk.dataaccess.metadata.graph;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.graph.MdEdgeInfo;
import com.runwaysdk.constants.graph.MdVertexInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeReference;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphDDLCommand;
import com.runwaysdk.dataaccess.graph.GraphDDLCommandAction;
import com.runwaysdk.dataaccess.graph.GraphRequest;

public class MdEdgeDAO extends MdGraphClassDAO implements MdEdgeDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 6394194987307320439L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdEdgeDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdEdgeDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdEdgeDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdEdgeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdEdgeDAO(attributeMap, MdEdgeInfo.CLASS);
  }

  /**
   * Returns a new {@link MdEdgeDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return instance of {@link MdVertexDAO}.
   */
  public static MdEdgeDAO newInstance()
  {
    return (MdEdgeDAO) BusinessDAO.newInstance(MdEdgeInfo.CLASS);
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
    String superMdViewId = this.getAttributeIF(MdEdgeInfo.SUPER_MD_RELATIONSHIP).getValue();
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
   * Returns the {@link MdEdgeDAOIF} that is the root of the hierarchy that this
   * type belongs to. returns a reference to itself if it is the root.
   * 
   * @return {@link MdEdgeDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  @Override
  public MdEdgeDAOIF getRootMdClassDAO()
  {
    return (MdEdgeDAOIF) super.getRootMdClassDAO();
  }

  /**
   * @see MdEdgeDAOIF#getSubClasses()
   */
  @Override
  public List<MdEdgeDAOIF> getSubClasses()
  {
    List<RelationshipDAOIF> subClassRelationshipArray = this.getChildren(RelationshipTypes.EDGE_INHERITANCE.getType());

    List<MdEdgeDAOIF> mdViewList = new LinkedList<MdEdgeDAOIF>();
    for (RelationshipDAOIF relationship : subClassRelationshipArray)
    {
      mdViewList.add((MdEdgeDAOIF) relationship.getChild());
    }

    return mdViewList;
  }

  /**
   * @see MdEdgeDAOIF#getAllConcreteSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdEdgeDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdEdgeDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * @see MdEdgeDAOIF#getAllSubClasses().
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdEdgeDAOIF> getAllSubClasses()
  {
    return (List<? extends MdEdgeDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdEdgeDAOIF#getSuperClass()
   */
  public MdEdgeDAOIF getSuperClass()
  {
    if (!this.isRootOfHierarchy())
    {
      String superMdVertexId = this.getAttributeIF(MdEdgeInfo.SUPER_MD_RELATIONSHIP).getValue();
      return MdEdgeDAO.get(superMdVertexId);
    }
    else
    {
      return null;
    }
  }

  /**
   * @see MdEdgeDAOIF#getSuperClasses()
   */
  @SuppressWarnings("unchecked")
  public List<MdEdgeDAOIF> getSuperClasses()
  {
    return (List<MdEdgeDAOIF>) super.getSuperClasses();
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

  // Nothing special happening here yet.
  // /**
  // *
  // */
  // public String save(boolean validateRequired)
  // {
  // boolean applied = this.isAppliedToDB();
  //
  // String oid = super.save(validateRequired);
  //
  //
  // return oid;
  // }

  @Override
  protected void createClassInDB()
  {
    String edgeClass = this.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();
    
    String superMdEdgeOid = this.getAttributeIF(MdEdgeInfo.SUPER_MD_RELATIONSHIP).getValue();
    String superClassName = null;

    if (superMdEdgeOid != null && superMdEdgeOid.length() > 0)
    {
      MdEdgeDAOIF mdEdge = MdEdgeDAO.get(superMdEdgeOid);

      superClassName = mdEdge.getDBClassName();
    }

    MdVertexDAOIF parentMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX).getValue());
    String parentVertexClass = parentMdVertex.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();

    MdVertexDAOIF childMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.CHILD_MD_VERTEX).getValue());
    String childVertexClass = childMdVertex.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().createEdgeClass(graphRequest, graphDDLRequest, edgeClass, superClassName, parentVertexClass, childVertexClass);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().deleteEdgeClass(graphRequest, graphDDLRequest, edgeClass);

    GraphDDLCommand command = new GraphDDLCommand(doItAction, undoItAction, false);
    command.doIt();
  }

  @Override
  protected void deleteClassInDB()
  {
    String edgeClass = this.getAttributeIF(MdEdgeInfo.DB_CLASS_NAME).getValue();
    
    String superMdEdgeOid = this.getAttributeIF(MdEdgeInfo.SUPER_MD_RELATIONSHIP).getValue();
    String superClassName = null;

    if (superMdEdgeOid != null && superMdEdgeOid.length() > 0)
    {
      MdEdgeDAOIF mdEdge = MdEdgeDAO.get(superMdEdgeOid);

      superClassName = mdEdge.getDBClassName();
    }

    MdVertexDAOIF paretMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX).getValue());
    String parentVertexClass = paretMdVertex.getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();

    MdVertexDAOIF childMdVertex = MdVertexDAO.get(this.getAttributeIF(MdEdgeInfo.CHILD_MD_VERTEX).getValue());
    String childVertexClass = childMdVertex.getAttributeIF(MdVertexInfo.DB_CLASS_NAME).getValue();

    GraphRequest graphRequest = GraphDBService.getInstance().getGraphDBRequest();
    GraphRequest graphDDLRequest = GraphDBService.getInstance().getDDLGraphDBRequest();

    GraphDDLCommandAction doItAction = GraphDBService.getInstance().deleteEdgeClass(graphRequest, graphDDLRequest, edgeClass);
    GraphDDLCommandAction undoItAction = GraphDBService.getInstance().createEdgeClass(graphRequest, graphDDLRequest, edgeClass, superClassName, parentVertexClass, childVertexClass);

    GraphDDLCommand command = new GraphDDLCommand(doItAction, undoItAction, true);
    command.doIt();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdEdgeDAOIF get(String oid)
  {
    return (MdEdgeDAOIF) BusinessDAO.get(oid);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdEdgeDAO getBusinessDAO()
  {
    return (MdEdgeDAO) super.getBusinessDAO();
  }

  /**
   * Returns an {@link MdEdgeDAOIF} instance of the metadata for the given type.
   * 
   * <br/>
   * <b>Precondition:</b> edgeType != null <br/>
   * <b>Precondition:</b> !edgeType.trim().equals("") <br/>
   * <b>Precondition:</b> edgeType is a valid class defined in the database
   * <br/>
   * <b>Postcondition:</b> Returns a {@link MdEdgeDAOIF} instance of the
   * metadata for the given class
   * ({@link MdEdgeDAOIF}().definesType().equals(edgeType)
   * 
   * @param edgeType
   * @return {@link MdEdgeDAOIF} instance of the metadata for the given type.
   */
  public static MdEdgeDAOIF getMdEdgeDAO(String edgeType)
  {
    return ObjectCache.getMdEdgeDAO(edgeType);
  }

  public String toString()
  {
    return '[' + this.definesType() + " definition]";
  }

  /**
   * Returns a list of all generators used to generate source for this
   * {@link MdEdgeDAOIF}.
   * 
   * @return list of all generators used to generate source for this
   *         {@link MdEdgeDAOIF}.
   */
  @Override
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Don't generate reserved types
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this))
    {
      return list;
    }

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
  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    Command command = null;

    // if (this.isNew())
    // {
    // command = new JavaArtifactMdViewCommand(this,
    // JavaArtifactMdTypeCommand.Operation.CREATE, conn);
    //
    // }
    // else
    // {
    // command = new JavaArtifactMdViewCommand(this,
    // JavaArtifactMdTypeCommand.Operation.UPDATE, conn);
    // }

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
    // return new JavaArtifactMdViewCommand(this,
    // JavaArtifactMdTypeCommand.Operation.DELETE, conn);
    return null;
  }

  /**
   * Returns a command object that cleans Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that cleans Java artifacts for this type.
   */
  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    // return new JavaArtifactMdViewCommand(this,
    // JavaArtifactMdTypeCommand.Operation.CLEAN, conn);

    return null;
  }

  /**
   * Returns the metadata object that defines the type of objects that are
   * parents in this relationship.
   * 
   * @return the metadata object that defines the type of objects that are
   *         parents in this relationship.
   */
  public MdVertexDAOIF getParentMdVertex()
  {
    AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdEdgeInfo.PARENT_MD_VERTEX);
    return (MdVertexDAOIF) attributeReference.dereference();
  }

  public MdVertexDAOIF getChildMdVertex()
  {
    AttributeReference attributeReference = (AttributeReference) this.getAttributeIF(MdEdgeInfo.CHILD_MD_VERTEX);
    return (MdVertexDAOIF) attributeReference.dereference();
  }

  @Override
  public String getParentDisplayLabel(Locale locale)
  {
    return this.getParentMdVertex().getDisplayLabel(locale);
  }

  @Override
  public Map<String, String> getParentDisplayLabes()
  {
    return this.getParentMdVertex().getDisplayLabels();
  }

  @Override
  public String getChildDisplayLabel(Locale locale)
  {
    return this.getChildMdVertex().getDisplayLabel(locale);
  }

  @Override
  public Map<String, String> getChildDisplayLabes()
  {
    return this.getChildMdVertex().getDisplayLabels();
  }

}
