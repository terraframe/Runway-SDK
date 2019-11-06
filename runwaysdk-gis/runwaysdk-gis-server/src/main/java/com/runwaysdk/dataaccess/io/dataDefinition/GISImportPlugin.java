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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.AttributeConcreteHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.MdVertexHandler;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.gis.constants.GISConstants;
import com.runwaysdk.gis.constants.MdAttributeGeometryInfo;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeGeometryDAO;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.system.gis.geo.GeoNode;
import com.runwaysdk.system.gis.geo.GeoNodeEntity;
import com.runwaysdk.system.gis.geo.GeoNodeEntityQuery;
import com.runwaysdk.system.gis.geo.GeoNodeGeometry;
import com.runwaysdk.system.gis.geo.GeoNodeGeometryQuery;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;


public class GISImportPlugin implements ImportPluginIF
{
  protected abstract static class GeoNodeHandler extends TagHandler implements HandlerFactoryIF
  {
    protected static final String TYPE      = "type";

    protected static final String ATTRIBUTE = "geoEntityAttribute";

    public GeoNodeHandler(ImportManager manager)
    {
      super(manager);
    }

    protected void setAttributeReference(GeoNode node, String attributeName, String referenceType, String referenceAttribute)
    {
      if (referenceType != null && referenceAttribute != null)
      {
        String key = referenceType + "." + referenceAttribute;

        if (!MdTypeDAO.isDefined(referenceType))
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, referenceType, key);
        }

        MdClassDAOIF concreteClass = MdClassDAO.getMdClassDAO(referenceType);
        MdAttributeDAOIF concreteAttribute = concreteClass.definesAttribute(referenceAttribute);

        // IMPORTANT: It is possible that the concrete type is defined before
        // this
        // schema was imported. However, the definition of the concrete
        // attribute
        // may not be defined until an update statement in the current xml file.
        // As such it is possible to have the type defined but not have the
        // concrete attribute defined. Therefore, if the attribute is not
        // defined
        // then we need to search and import the definition of the class which
        // exists in the current xml file.
        if (concreteAttribute == null)
        {
          String[] search_tags = XMLTags.TYPE_TAGS;
          SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, referenceType, concreteClass.definesType());

          concreteAttribute = concreteClass.definesAttribute(referenceAttribute);
        }

        if (concreteAttribute != null)
        {
          node.setValue(attributeName, concreteAttribute.getMdAttributeConcrete().getOid());
        }
        else
        {
          throw new RuntimeException("Unable to find the attribute metadata for [" + key + "]");
        }
      }
    }
  }

  protected static class GeoNodeEntityHandler extends GeoNodeHandler implements HandlerFactoryIF
  {
    public GeoNodeEntityHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String type = attributes.getValue(TYPE);
      String attribute = attributes.getValue(ATTRIBUTE);
      String key = type + "." + attribute;

      GeoNodeEntity node = this.getOrCreateNode(key);
      this.setAttributeReference(node, GeoNodeEntity.GEOENTITYATTRIBUTE, type, attribute);
      node.apply();

      System.out.println("Creating node [" + key + "]");

      this.getManager().addImportedObject(node.getOid());
    }

    private GeoNodeEntity getOrCreateNode(String key)
    {
      if (this.getManager().isUpdateState() || this.getManager().isCreateOrUpdateState())
      {
        GeoNodeEntity node = this.getNode(key);

        if (node != null)
        {
          return node;
        }
        else if (this.getManager().isUpdateState())
        {
          String message = "Unable to find a [" + GeoNodeEntity.CLASS + "] with key [" + key + "]";
          MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(GeoNodeEntity.CLASS);

          throw new DataNotFoundException(message, mdClass);
        }
      }

      GeoNodeEntity node = new GeoNodeEntity();
      node.setKeyName(key);
      return node;
    }

    private GeoNodeEntity getNode(String key)
    {
      GeoNodeEntityQuery query = new GeoNodeEntityQuery(new QueryFactory());
      query.WHERE(query.getKeyName().EQ(key));
      OIterator<? extends GeoNodeEntity> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          GeoNodeEntity node = iterator.next();

          return node;
        }

        return null;
      }
      finally
      {
        iterator.close();
      }
    }
  }

  protected static class GeoNodeGeometryHandler extends GeoNodeHandler implements HandlerFactoryIF
  {
    private static final String IDENTIFIER_ATTRIBUTE    = "identifierAttribute";

    private static final String DISPLAY_LABEL_ATTRIBUTE = "displayLabelAttribute";

    private static final String GEOMETRY_ATTRIBUTE      = "geometryAttribute";

    private static final String MULTIPOLYGON_ATTRIBUTE  = "multipolygonAttribute";

    private static final String POINT_ATTRIBUTE         = "pointAttribute";

    public GeoNodeGeometryHandler(ImportManager manager)
    {
      super(manager);
    }

    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String type = attributes.getValue(TYPE);
      String attribute = attributes.getValue(ATTRIBUTE);
      String key = type + "." + attribute;

      GeoNodeGeometry node = this.getOrCreateNode(key);
      this.setAttributeReference(node, GeoNodeGeometry.GEOENTITYATTRIBUTE, type, attribute);
      this.setAttributeReference(node, GeoNodeGeometry.IDENTIFIERATTRIBUTE, type, attributes.getValue(IDENTIFIER_ATTRIBUTE));
      this.setAttributeReference(node, GeoNodeGeometry.DISPLAYLABELATTRIBUTE, type, attributes.getValue(DISPLAY_LABEL_ATTRIBUTE));
      this.setAttributeReference(node, GeoNodeGeometry.GEOMETRYATTRIBUTE, type, attributes.getValue(GEOMETRY_ATTRIBUTE));
      this.setAttributeReference(node, GeoNodeGeometry.MULTIPOLYGONATTRIBUTE, type, attributes.getValue(MULTIPOLYGON_ATTRIBUTE));
      this.setAttributeReference(node, GeoNodeGeometry.POINTATTRIBUTE, type, attributes.getValue(POINT_ATTRIBUTE));
      node.apply();

      System.out.println("Creating node [" + key + "]");

      this.getManager().addImportedObject(node.getOid());
    }

    private GeoNodeGeometry getOrCreateNode(String key)
    {
      if (this.getManager().isUpdateState() || this.getManager().isCreateOrUpdateState())
      {
        GeoNodeGeometry node = this.getNode(key);

        if (node != null)
        {
          node.lock();
          return node;
        }
        else if (this.getManager().isUpdateState())
        {
          String message = "Unable to find a [" + GeoNodeGeometry.CLASS + "] with key [" + key + "]";
          MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(GeoNodeGeometry.CLASS);

          throw new DataNotFoundException(message, mdClass);
        }
      }

      GeoNodeGeometry node = new GeoNodeGeometry();
      node.setKeyName(key);
      return node;
    }

    private GeoNodeGeometry getNode(String key)
    {
      GeoNodeGeometryQuery query = new GeoNodeGeometryQuery(new QueryFactory());
      query.WHERE(query.getKeyName().EQ(key));
      OIterator<? extends GeoNodeGeometry> iterator = query.getIterator();

      try
      {
        if (iterator.hasNext())
        {
          GeoNodeGeometry node = iterator.next();

          return node;
        }

        return null;
      }
      finally
      {
        iterator.close();
      }
    }
  }

  protected static class AttributeGeometryHandler extends AttributeConcreteHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributeGeometryHandler(ImportManager manager, String type)
    {
      super(manager, type);
    }

    protected void populate(MdClassDAO mdClass, MdAttributeGeometryDAO mdAttribute, Attributes attributes)
    {
      super.populate(mdClass, mdAttribute, attributes);

      ImportManager.setValue(mdAttribute, MdAttributeGeometryInfo.SRID, attributes, XMLTags.SRID_ATTRIBUTE);
      ImportManager.setValue(mdAttribute, MdAttributeGeometryInfo.DIMENSION, attributes, XMLTags.DIMENSION_ATTRIBUTE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.MdAttributeHandler.
     * AttributeHandler#configure(com.runwaysdk.dataaccess.metadata.MdClassDAO,
     * com.runwaysdk.dataaccess.metadata.MdAttributeDAO, org.xml.sax.Attributes)
     */
    @Override
    protected void configure(MdClassDAO mdClass, MdAttributeDAO mdAttribute, Attributes attributes)
    {
      this.populate(mdClass, (MdAttributeGeometryDAO) mdAttribute, attributes);
    }
  }

  private class GISAttributeHandlerFactor extends HandlerFactory implements HandlerFactoryIF
  {
    public GISAttributeHandlerFactor(ImportManager manager)
    {
      this.addHandler(XMLTags.POINT_TAG, new AttributeGeometryHandler(manager, MdAttributePointInfo.CLASS));
      this.addHandler(XMLTags.LINESTRING_TAG, new AttributeGeometryHandler(manager, MdAttributeLineStringInfo.CLASS));
      this.addHandler(XMLTags.POLYGON_TAG, new AttributeGeometryHandler(manager, MdAttributePolygonInfo.CLASS));
      this.addHandler(XMLTags.MULTIPOINT_TAG, new AttributeGeometryHandler(manager, MdAttributeMultiPointInfo.CLASS));
      this.addHandler(XMLTags.MULTILINESTRING_TAG, new AttributeGeometryHandler(manager, MdAttributeMultiLineStringInfo.CLASS));
      this.addHandler(XMLTags.MULTIPOLYGON_TAG, new AttributeGeometryHandler(manager, MdAttributeMultiPolygonInfo.CLASS));
    }
  }

  private static class NodeHandlerFactory extends HandlerFactory implements HandlerFactoryIF
  {
    private static final String GEO_NODE_ENTITY_TAG   = "geoNodeEntity";

    private static final String GEO_NODE_GEOMETRY_TAG = "geoNodeGeometry";

    public NodeHandlerFactory(ImportManager manager)
    {
      this.addHandler(GEO_NODE_ENTITY_TAG, new GeoNodeEntityHandler(manager));
      this.addHandler(GEO_NODE_GEOMETRY_TAG, new GeoNodeGeometryHandler(manager));
    }
  }

  private static class MdGeoVertexHandler extends MdVertexHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public static final String TAG = "mdGeoVertex";

    public MdGeoVertexHandler(ImportManager manager)
    {
      super(manager);
    }

    protected final MdGeoVertexDAO createMdVertex(String localName, String name)
    {
      return (MdGeoVertexDAO) this.getManager().getEntityDAO(MdGeoVertexInfo.CLASS, name).getEntityDAO();
    }

    protected String getTag()
    {
      return TAG;
    }
  }

  private static class GeoVertexHandlerFactory extends HandlerFactory implements HandlerFactoryIF
  {

    public GeoVertexHandlerFactory(ImportManager manager)
    {
      this.addHandler(MdGeoVertexHandler.TAG, new MdGeoVertexHandler(manager));
    }
  }

  public String getModuleIdentifier()
  {
    return GISConstants.GIS_SYSTEM_PACKAGE;
  }

  @Override
  public void register(ImportManager manager)
  {
    manager.register(MdAttributeHandler.class.getName(), new GISAttributeHandlerFactor(manager));
    manager.register(CreateHandler.class.getName(), new NodeHandlerFactory(manager));
    manager.register(UpdateHandler.class.getName(), new NodeHandlerFactory(manager));
    manager.register(CreateOrUpdateHandler.class.getName(), new NodeHandlerFactory(manager));
    manager.register(CreateHandler.class.getName(), new GeoVertexHandlerFactory(manager));
    manager.register(UpdateHandler.class.getName(), new GeoVertexHandlerFactory(manager));
    manager.register(CreateOrUpdateHandler.class.getName(), new GeoVertexHandlerFactory(manager));
  }
}
