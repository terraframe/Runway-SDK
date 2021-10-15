/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.dataaccess.metadata.graph;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.graph.generation.JavaArtifactMdGraphClassCommand;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.constants.GeoEntityInfo;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.gis.dataaccess.MdGeoVertexDAOIF;

public class MdGeoVertexDAO extends MdVertexDAO implements MdGeoVertexDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5015373068741693970L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdGeoVertexDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdGeoVertexDAO} from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdGeoVertexDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdGeoVertexDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdGeoVertexDAO(attributeMap, MdGeoVertexInfo.CLASS);
  }

  /**
   * Returns a new {@link MdGeoVertexDAO}. Some attributes will contain default
   * values, as defined in the attribute metadata. Otherwise, the attributes
   * will be blank.
   * 
   * @return instance of {@link MdGeoVertexDAO}.
   */
  public static MdGeoVertexDAO newInstance()
  {
    return (MdGeoVertexDAO) BusinessDAO.newInstance(MdGeoVertexInfo.CLASS);
  }

  /**
   * Returns the {@link MdVertexDAOIF} that is the root of the hierarchy that
   * this type belongs to. returns a reference to itself if it is the root.
   * 
   * @return {@link MdVertexDAOIF} that is the root of the hierarchy that this
   *         type belongs to. returns a reference to itself if it is the root.
   */
  @Override
  public MdGeoVertexDAOIF getRootMdClassDAO()
  {
    return (MdGeoVertexDAOIF) super.getRootMdClassDAO();
  }

  /**
   * @see MdVGeoertexDAOIF#getSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGeoVertexDAOIF> getSubClasses()
  {
    return (List<? extends MdGeoVertexDAOIF>) super.getSubClasses();
  }

  /**
   * @see MdVGeoertexDAOIF#getAllConcreteSubClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGeoVertexDAOIF> getAllConcreteSubClasses()
  {
    return (List<? extends MdGeoVertexDAOIF>) super.getAllConcreteSubClasses();
  }

  /**
   * @see MdVGeoertexDAOIF#getAllSubClasses().
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGeoVertexDAOIF> getAllSubClasses()
  {
    return (List<? extends MdGeoVertexDAOIF>) super.getAllSubClasses();
  }

  /**
   * @see MdVGeoertexDAOIF#getSuperClass()
   */
  @Override
  public MdGeoVertexDAOIF getSuperClass()
  {
    return (MdGeoVertexDAOIF) super.getSuperClass();
  }

  /**
   * @see MdVGeoertexDAOIF#getSuperClasses()
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends MdGeoVertexDAOIF> getSuperClasses()
  {
    return (List<? extends MdGeoVertexDAOIF>) super.getSuperClasses();
  }

  protected void createDefaultAttributes()
  {
    super.createDefaultAttributes();
    
    // Copy attributes defined on GeoEntity
    MdBusinessDAOIF mdBusGeoEntity = MdBusinessDAO.getMdBusinessDAO(GeoEntityInfo.CLASS);
    
    Stream<String> defaultAttrs = Arrays.stream(GeoEntityInfo.DEFAULT_ATTRIBUTES);
    
    defaultAttrs.forEach(attrName -> {
      if(!attrName.equals("geoId")) {
        
      this.copyAttribute(mdBusGeoEntity.definesAttribute(attrName));
      }
    }
    );

// Heads up: clean up   
//    MdAttributePointDAO mdAttributePointDAO = MdAttributePointDAO.newInstance();
//    mdAttributePointDAO.setValue(MdAttributePointInfo.NAME, "testPoint");
//    mdAttributePointDAO.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
//    mdAttributePointDAO.setStructValue(MdAttributePointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
//    mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
//    // mdAttributePointDAO.setValue(MdAttributePointInfo.DIMENSION, "2");
//    mdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
//    mdAttributePointDAO.apply();
//
//    mdAttributeLineStringDAO = MdAttributeLineStringDAO.newInstance();
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.NAME, "testLineString");
//    mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
//    mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.SRID, "4326");
//    // mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DIMENSION,
//    // "2");
//    mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
//    mdAttributeLineStringDAO.apply();
//
//    mdAttributePolygonDAO = MdAttributePolygonDAO.newInstance();
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.NAME, "testPolygon");
//    mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
//    mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.SRID, "4326");
//    // mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DIMENSION, "2");
//    mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
//    mdAttributePolygonDAO.apply();
//
//    mdAttributeMultiPointDAO = MdAttributeMultiPointDAO.newInstance();
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.NAME, "testMultiPoint");
//    mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
//    mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.SRID, "4326");
//    // mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DIMENSION,
//    // "2");
//    mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
//    mdAttributeMultiPointDAO.apply();
//
//    mdAttributeMultiLineStringDAO = MdAttributeMultiLineStringDAO.newInstance();
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.NAME, "testMultiLineString");
//    mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
//    mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.SRID, "4326");
//    // mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DIMENSION,
//    // "2");
//    mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
//    mdAttributeMultiLineStringDAO.apply();
//
//    mdAttributeMultiPolygonDAO = MdAttributeMultiPolygonDAO.newInstance();
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.NAME, "testMultiPolygon");
//    mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
//    mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
//    // mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DIMENSION,
//    // "2");
//    mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
//    mdAttributeMultiPolygonDAO.apply();
  }

  // Heads up: Clean up
  // /**
  // * Adds the geometry attributes.
  // */
  // @Override
  // protected void createClassInDB()
  // {
  // super.createClassInDB();
  // }
  //
  // /**
  // *
  // */
  // @Override
  // protected void deleteClassInDB()
  // {
  // super.deleteClassInDB();
  // }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdGeoVertexDAOIF get(String oid)
  {
    return (MdGeoVertexDAOIF) BusinessDAO.get(oid);
  }

  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdGeoVertexDAO getBusinessDAO()
  {
    return (MdGeoVertexDAO) super.getBusinessDAO();
  }

  /**
   * Returns an {@link MdGeoVertexDAOIF} instance of the metadata for the given
   * type.
   * 
   * <br/>
   * <b>Precondition:</b> vertexType != null <br/>
   * <b>Precondition:</b> !vertexType.trim().equals("") <br/>
   * <b>Precondition:</b> vertexType is a valid class defined in the database
   * <br/>
   * <b>Postcondition:</b> Returns a {@link MdGeoVertexDAOIF} instance of the
   * metadata for the given class
   * ({@link MdGeoVertexDAOIF}().definesType().equals(vertexType)
   * 
   * @param vertexType
   * @return {@link MdGeoVertexDAOIF} instance of the metadata for the given
   *         type.
   */
  public static MdGeoVertexDAOIF getMdGeoVertexDAO(String vertexType)
  {
    return (MdGeoVertexDAOIF) ObjectCache.getMdVertexDAO(vertexType);
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
