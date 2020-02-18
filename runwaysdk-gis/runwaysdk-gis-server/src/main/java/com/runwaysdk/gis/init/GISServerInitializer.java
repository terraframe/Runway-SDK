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
package com.runwaysdk.gis.init;

import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractServerGenerator;
import com.runwaysdk.business.generation.GISAbstractClientGenerator;
import com.runwaysdk.business.generation.GISAbstractServerGenerator;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.dataDefinition.ExportVisitor;
import com.runwaysdk.dataaccess.io.dataDefinition.GISExportVisitor;
import com.runwaysdk.dataaccess.io.dataDefinition.GISImportPlugin;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.constants.MdGeoVertexInfo;
import com.runwaysdk.gis.constants.MdWebPointInfo;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdWebPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdGeoVertexDAO;
import com.runwaysdk.gis.transport.conversion.business.GISMutableDTOToMutable;
import com.runwaysdk.query.GISAttributeEnumerationFactory;
import com.runwaysdk.query.GISAttributeReferenceFactory;
import com.runwaysdk.query.GISEntityQueryAttributeFactory;
import com.runwaysdk.query.GISValueQueryAttributeFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.conversion.business.MutableDTOToMutable;
import com.runwaysdk.util.ServerInitializerIF;

public class GISServerInitializer implements ServerInitializerIF
{

  private boolean initialized;

  public GISServerInitializer()
  {
    this.initialized = false;
  }

  public synchronized void init()
  {
    if (initialized)
    {
      return;
    }

    // Register DAO factory
    ConcurrentHashMap<String, BusinessDAO> businessDAOMap = new ConcurrentHashMap<String, BusinessDAO>();
    businessDAOMap.put(MdGeoVertexInfo.CLASS, new MdGeoVertexDAO());
    businessDAOMap.put(MdAttributePointInfo.CLASS, new MdAttributePointDAO());
    businessDAOMap.put(MdAttributeLineStringInfo.CLASS, new MdAttributeLineStringDAO());
    businessDAOMap.put(MdAttributePolygonInfo.CLASS, new MdAttributePolygonDAO());
    businessDAOMap.put(MdAttributeMultiPointInfo.CLASS, new MdAttributeMultiPointDAO());
    businessDAOMap.put(MdAttributeMultiLineStringInfo.CLASS, new MdAttributeMultiLineStringDAO());
    businessDAOMap.put(MdAttributeMultiPolygonInfo.CLASS, new MdAttributeMultiPolygonDAO());
    businessDAOMap.put(MdWebPointInfo.CLASS, new MdWebPointDAO());
    BusinessDAOFactory.registerDAOTypes(businessDAOMap);

    // Register entity attribute factory
    com.runwaysdk.dataaccess.attributes.entity.AttributeFactory.registerPlugin(new com.runwaysdk.gis.dataaccess.attributes.entity.GISAttributeFactory());

    // Register tranzient attribute factory
    com.runwaysdk.dataaccess.attributes.tranzient.AttributeFactory.registerPlugin(new com.runwaysdk.gis.dataaccess.attributes.tranzient.GISAttributeFactory());

    // Register value attribute factory
    com.runwaysdk.dataaccess.attributes.value.AttributeFactory.registerPlugin(new com.runwaysdk.gis.dataaccess.attributes.value.GISAttributeFactory());

    // Register query metadata attribute factory
    com.runwaysdk.query.Attribute.registerPlugin(new com.runwaysdk.query.GISAttributeMdAttributeConcrete_Q_Factory());

    // Register the query attribute factories
    com.runwaysdk.query.EntityQuery.registerPlugin(new GISEntityQueryAttributeFactory());
    com.runwaysdk.query.ValueQuery.registerPlugin(new GISValueQueryAttributeFactory());
    com.runwaysdk.query.AttributeReference.registerPlugin(new GISAttributeReferenceFactory());
    com.runwaysdk.query.AttributeEnumeration.registerPlugin(new GISAttributeEnumerationFactory());

    MutableDTOToMutable.registerPlugin(new GISMutableDTOToMutable());

    SAXSourceParser.registerPlugin(new GISImportPlugin());
    
    // Register XML Schema exporter tags
    ConcurrentHashMap<String, String> exportAttributeTags = new ConcurrentHashMap<String, String>();
    exportAttributeTags.put(MdAttributePointInfo.CLASS, XMLTags.POINT_TAG);
    exportAttributeTags.put(MdAttributeLineStringInfo.CLASS, XMLTags.LINESTRING_TAG);
    exportAttributeTags.put(MdAttributePolygonInfo.CLASS, XMLTags.POLYGON_TAG);
    exportAttributeTags.put(MdAttributeMultiPointInfo.CLASS, XMLTags.MULTIPOINT_TAG);
    exportAttributeTags.put(MdAttributeMultiLineStringInfo.CLASS, XMLTags.MULTILINESTRING_TAG);
    exportAttributeTags.put(MdAttributeMultiPolygonInfo.CLASS, XMLTags.MULTIPOLYGON_TAG);
    ExportVisitor.registerAttributeTags(exportAttributeTags);

    ExportVisitor.registerPlugin(new GISExportVisitor());

    // Register generators
    AbstractServerGenerator.registerPlugin(new GISAbstractServerGenerator());

    // Register generators
    AbstractClientGenerator.registerPlugin(new GISAbstractClientGenerator());

    this.initialized = true;
  }

  @Override
  @Request
  public synchronized void rebuild()
  {
//    StreamSource universalXml;
//    String path = "com/runwaysdk/resources/build/universal.xml";
//    
//    LocalProperties.setSkipCodeGenAndCompile(true);
//
//    if (LocalProperties.isRunwayEnvironment())
//    {
//      // Always read universal from src/main/resources, instead of reading it from a jar.
//      try
//      {
//        String targetFilePath = RunwayGisProperties.getGisServerResourcesDir() + "/" + path;
//        File file = new File(targetFilePath);
//        universalXml = new FileStreamSource(file);
//      }
//      catch (Exception e)
//      {
//        universalXml = new ResourceStreamSource(path);
//      }
//    }
//    else
//    {
//      // Read it from the jar.
//      universalXml = new ResourceStreamSource(path);
//    }
//
//    try
//    {
//      SAXImporter.runImport(universalXml, "classpath:" + ConfigGroup.XSD.getPath() + "datatype_gis.xsd");
//    }
//    catch (Throwable t)
//    {
//      String errorMessage = "Failed to initialize module [" + this.getClass().getName() + "]";
//      throw new ProgrammingErrorException(errorMessage, t);
//    }
  }

}
