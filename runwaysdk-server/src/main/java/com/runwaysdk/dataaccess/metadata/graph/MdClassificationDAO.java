/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.metadata.graph;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.graph.generation.JavaArtifactMdGraphClassCommand;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;

public class MdClassificationDAO extends MdVertexDAO implements MdClassificationDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5015373068741693970L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdClassificationDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdClassificationDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdClassificationDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdClassificationDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdClassificationDAO(attributeMap, MdClassificationInfo.CLASS);
  }

  /**
   * Returns a new {@link MdClassificationDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return instance of {@link MdClassificationDAO}.
   */
  public static MdClassificationDAO newInstance()
  {
    return (MdClassificationDAO) BusinessDAO.newInstance(MdClassificationInfo.CLASS);
  }

  /**
   * Returns the {@link MdVertexDAOIF} that is the root of the hierarchy that
   * this type belongs to. returns a reference to itself if it is the root.
   * 
   * @return {@link MdVertexDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  @Override
  public MdClassificationDAOIF getRootMdClassDAO()
  {
    return (MdClassificationDAOIF) super.getRootMdClassDAO();
  }

  /**
   * @see MdVGeoertexDAOIF#getSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdClassificationDAOIF> getSubClasses()
  {
    return (List<? extends MdClassificationDAOIF>) super.getSubClasses();
  }

  /**
   * @see MdVGeoertexDAOIF#getAllConcreteSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdClassificationDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdClassificationDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * @see MdVGeoertexDAOIF#getAllSubClasses().
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdClassificationDAOIF> getAllSubClasses()
  {
    return (List<? extends MdClassificationDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdVGeoertexDAOIF#getSuperClass()
   */
  @Override
  public MdClassificationDAOIF getSuperClass()
  {
    return (MdClassificationDAOIF)super.getSuperClass();
  }

  /**
   * @see MdVGeoertexDAOIF#getSuperClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdClassificationDAOIF> getSuperClasses()
  {
    return (List<? extends MdClassificationDAOIF>) super.getSuperClasses();
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdClassificationDAOIF get(String oid)
  {
    return (MdClassificationDAOIF) BusinessDAO.get(oid);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdClassificationDAO getBusinessDAO()
  {
    return (MdClassificationDAO) super.getBusinessDAO();
  }

  /**
   * Returns an {@link MdClassificationDAOIF} instance of the metadata for the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> vertexType != null <br/>
   * <b>Precondition:</b> !vertexType.trim().equals("") <br/>
   * <b>Precondition:</b> vertexType is a valid class defined in the database
   * <br/>
   * <b>Postcondition:</b> Returns a {@link MdClassificationDAOIF} instance of the
   * metadata for the given class
   * ({@link MdClassificationDAOIF}().definesType().equals(vertexType)
   * 
   * @param vertexType
   * @return {@link MdClassificationDAOIF} instance of the metadata for the given type.
   */
  public static MdClassificationDAOIF getMdClassificationDAO(String vertexType)
  {
    return (MdClassificationDAOIF)ObjectCache.getMdVertexDAO(vertexType);
  }

  /**
   * Returns a list of all generators used to generate source for this
   * {@link MdVertexDAOIF}.
   * 
   * @return list of all generators used to generate source for this
   *         {@link MdVertexDAOIF}.
   */
  @Override
  public List<GeneratorIF> getGenerators()
  {
    // TODO
    return super.getGenerators();
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
    // TODO
    return super.getCreateUpdateJavaArtifactCommand(conn);
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
    // TODO
    return super.getDeleteJavaArtifactCommand(conn);
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
    return new JavaArtifactMdGraphClassCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN);
  }
}
