package com.runwaysdk.dataaccess.graph.orientdb;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.spatial4j.shape.Shape;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.OVertexDocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.runwaysdk.business.graph.EdgeObject;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEmbeddedDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdEmbeddedGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAO;
import com.runwaysdk.dataaccess.graph.EdgeObjectDAOIF;
import com.runwaysdk.dataaccess.graph.EmbeddedGraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.graph.ResultSetConverterIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.AttributeEmbedded;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef;
import com.runwaysdk.dataaccess.graph.attributes.AttributeGraphRef.ID;
import com.runwaysdk.dataaccess.metadata.MdAttributeEmbeddedDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;

public class ResultSetConverter implements ResultSetConverterIF
{
  protected Class<?> resultType = null;
  
  public ResultSetConverter()
  {
  }
  
  public ResultSetConverter(Class<?> resultType)
  {
    this.resultType = resultType;
  }
  
  public Object convert(GraphRequest request, Object result)
  {
    final OrientDBRequest orientDBRequest = (OrientDBRequest) request;
    final ODatabaseSession db = orientDBRequest.getODatabaseSession();
    final OResult oresult = (OResult) result;
    
    Object converted = null;
    
    if (oresult.isElement())
    {
      OElement element = oresult.toElement();

      converted = this.buildDAO(element);
    }
    else if (oresult.isRecord())
    {
      ORecord record = oresult.getRecord().get();
      OElement element = db.load(record);

      converted = this.buildDAO(element);
    }
    else
    {
      converted = this.getPropertyValue(oresult, resultType);
    }
    
    if (converted instanceof VertexObjectDAOIF)
    {
      return VertexObject.instantiate((VertexObjectDAO) converted);
    }
    else if (converted instanceof EdgeObjectDAOIF)
    {
      return EdgeObject.instantiate((EdgeObjectDAO) converted);
    }
    else
    {
      return converted;
    }
  }
  
  protected GraphObjectDAO buildDAO(OElement element)
  {
    OClass oClass = element.getSchemaType().get();
    MdGraphClassDAOIF mdGraph = MdGraphClassDAO.getMdGraphClassByTableName(oClass.getName());

    return this.buildDAO(mdGraph, element);
  }

  protected GraphObjectDAO buildDAO(MdGraphClassDAOIF mdGraph, OElement element)
  {
    if (mdGraph instanceof MdVertexDAOIF)
    {
      VertexObjectDAO vertexDAO = VertexObjectDAO.newInstance((MdVertexDAOIF) mdGraph);

      populateDAO(element.asVertex().get(), vertexDAO);

      return vertexDAO;
    }
    else if (mdGraph instanceof MdEmbeddedGraphClassDAOIF)
    {
      EmbeddedGraphObjectDAO embeddedDAO = EmbeddedGraphObjectDAO.newInstance((MdEmbeddedGraphClassDAOIF) mdGraph);

      populateDAO(element, embeddedDAO);

      return embeddedDAO;
    }
    else if (mdGraph instanceof MdEdgeDAOIF)
    {
      MdEdgeDAOIF mdEdge = (MdEdgeDAOIF) mdGraph;
      OEdge edge = element.asEdge().get();

      OVertex parent = edge.getFrom();
      OVertex child = edge.getTo();

      VertexObjectDAO parentDAO = (VertexObjectDAO) this.buildDAO(parent);
      VertexObjectDAO childDAO = (VertexObjectDAO) this.buildDAO(child);

      EdgeObjectDAO edgeDAO = EdgeObjectDAO.newInstance(parentDAO, childDAO, mdEdge);

      populateDAO(edge, edgeDAO);

      return edgeDAO;
    }
    else
    {
      throw new UnsupportedOperationException();
    }
  }
  
  protected void populateDAO(OElement vertex, GraphObjectDAO vertexDAO)
  {
    vertexDAO.setIsNew(false);
    vertexDAO.setAppliedToDB(true);

    Attribute[] attributes = vertexDAO.getAttributeArray();
    MdGraphClassDAOIF mdClass = vertexDAO.getMdGraphClassDAO();

    for (Attribute attribute : attributes)
    {
      MdAttributeConcreteDAOIF mdAttribute = attribute.getMdAttribute();
      String columnName = mdAttribute.getColumnName();
      Object value = vertex.getProperty(columnName);

      if (value != null)
      {
        if (mdAttribute instanceof MdAttributeGeometryDAOIF)
        {
          ODocument doc = (ODocument) value;

          Shape shape = OShapeFactory.INSTANCE.fromDoc(doc);
          Geometry geometry = OShapeFactory.INSTANCE.toGeometry(shape);

          attribute.setValueInternal(geometry);
        }
        else if (mdAttribute instanceof MdAttributeGraphRefDAOIF)
        {
          if (value instanceof OVertex)
          {
            OVertex ref = (OVertex) value;
            String oid = (String) ref.getProperty("oid");

            attribute.setValueInternal(oid);

            ( (AttributeGraphRef) attribute ).setId(new ID(oid, ref.getIdentity()));
          }
        }
        else if (mdAttribute instanceof MdAttributeEnumerationDAO)
        {
          attribute.setValueInternal(value);
        }
        else if (mdAttribute instanceof MdAttributeEmbeddedDAO)
        {
          AttributeEmbedded attributeEmbedded = (AttributeEmbedded) attribute;
          GraphObjectDAO embedGraphObjectDAO = (GraphObjectDAO) attributeEmbedded.getObjectValue();

          if (value instanceof OElement)
          {
            OElement embedOElement = (OElement) value;

            this.populateDAO(embedOElement, embedGraphObjectDAO); 
          }

          if (mdClass.isEnableChangeOverTime())
          {
            MdEmbeddedGraphClassDAOIF embeddedObject = (MdEmbeddedGraphClassDAOIF) embedGraphObjectDAO.getMdGraphClassDAO();

            List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);

            attribute.clearValuesOverTime();

            for (OElement element : elements)
            {
              Date startDate = element.getProperty(OrientDBConstant.START_DATE);
              Date endDate = element.getProperty(OrientDBConstant.END_DATE);
              OElement vElement = element.getProperty(OrientDBConstant.VALUE);
              String oid = element.getProperty(OrientDBConstant.OID);

              EmbeddedGraphObjectDAO votDAO = EmbeddedGraphObjectDAO.newInstance(embeddedObject);
              this.populateDAO(vElement, votDAO);

              attribute.setValueInternal(oid, votDAO, startDate, endDate);
            }
          }
        }
        else
        {
          attribute.setValueInternal(value);
        }
      }

      if (mdClass.isEnableChangeOverTime())
      {
        populateDAOChangeOverTime(vertex, attribute, mdAttribute);
      }
    }

    if (vertex.hasProperty("_rid"))
    {
      OVertexDocument doc = (OVertexDocument) vertex.getProperty("_rid");
      vertexDAO.setRID(doc.getIdentity());
    }
    else
    {
      vertexDAO.setRID(vertex.getIdentity());
    }
  }

  protected void populateDAOChangeOverTime(OElement vertex, Attribute attribute, MdAttributeConcreteDAOIF mdAttribute)
  {
    String columnName = mdAttribute.getColumnName();

    if (mdAttribute instanceof MdAttributeEmbeddedDAOIF)
    {
      MdEmbeddedGraphClassDAOIF embeddedClass = (MdEmbeddedGraphClassDAOIF) ( (MdAttributeEmbeddedDAOIF) mdAttribute ).getEmbeddedMdClassDAOIF();

      List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);
      attribute.clearValuesOverTime();

      if (elements != null)
      {

        for (OElement element : elements)
        {
          Date startDate = element.getProperty(OrientDBConstant.START_DATE);
          Date endDate = element.getProperty(OrientDBConstant.END_DATE);
          OElement vElement = element.getProperty(OrientDBConstant.VALUE);
          String oid = element.getProperty(OrientDBConstant.OID);

          EmbeddedGraphObjectDAO votDAO = EmbeddedGraphObjectDAO.newInstance(embeddedClass);
          this.populateDAO(vElement, votDAO);

          attribute.setValueInternal(oid, votDAO, startDate, endDate);
        }
      }
    }
    else if (mdAttribute instanceof MdAttributeGeometryDAOIF)
    {
      List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);
      attribute.clearValuesOverTime();

      if (elements != null)
      {
        for (OElement element : elements)
        {
          Date startDate = element.getProperty(OrientDBConstant.START_DATE);
          Date endDate = element.getProperty(OrientDBConstant.END_DATE);
          OElement vElement = element.getProperty(OrientDBConstant.VALUE);
          String oid = element.getProperty(OrientDBConstant.OID);

          if (vElement != null)
          {
            Shape shape = OShapeFactory.INSTANCE.fromDoc((ODocument) vElement);
            Geometry geometry = OShapeFactory.INSTANCE.toGeometry(shape);

            attribute.setValueInternal(oid, geometry, startDate, endDate);
          }
          else
          {
            attribute.setValueInternal(oid, null, startDate, endDate);
          }
        }
      }
    }
    else if (mdAttribute instanceof MdAttributeGraphRefDAOIF)
    {
      List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);
      attribute.clearValuesOverTime();

      if (elements != null)
      {
        for (OElement element : elements)
        {
          Date startDate = element.getProperty(OrientDBConstant.START_DATE);
          Date endDate = element.getProperty(OrientDBConstant.END_DATE);
          Object value = element.getProperty(OrientDBConstant.VALUE);
          String oid = element.getProperty(OrientDBConstant.OID);

          if (value instanceof OVertex)
          {
            OVertex ref = (OVertex) value;
            String itemOid = (String) ref.getProperty("oid");

            ID id = new ID(itemOid, ref.getIdentity());

            attribute.setValueInternal(oid, id, startDate, endDate);
          }
        }
      }
    }
    else
    {
      List<OElement> elements = vertex.getProperty(columnName + OrientDBConstant.COT_SUFFIX);
      attribute.clearValuesOverTime();

      if (elements != null)
      {
        for (OElement element : elements)
        {
          Date startDate = element.getProperty(OrientDBConstant.START_DATE);
          Date endDate = element.getProperty(OrientDBConstant.END_DATE);
          Object votValue = element.getProperty(OrientDBConstant.VALUE);
          String oid = element.getProperty(OrientDBConstant.OID);

          attribute.setValueInternal(oid, votValue, startDate, endDate);
        }
      }
    }

    attribute.getValuesOverTime().validate();
  }
  
  public Object getPropertyValue(Object value, Class<?> resultType)
  {
    if (value instanceof OResult)
    {
      OResult result = (OResult) value;

      Set<String> names = result.getPropertyNames();

      String className = null;
      if (names.contains("@class")) className = result.getProperty("@class");
      else if (names.contains("_class")) className = result.getProperty("_class");
      
      if (className != null)
      {
        List<String> geometryTypes = Arrays.asList("OPoint", "OLineString", "OShape", "OPolygon", "OMultiPoint", "OMultiLineString", "OMultiPolygon");

        if (geometryTypes.contains(className))
        {
          Shape shape = OShapeFactory.INSTANCE.fromObject(result);
          return OShapeFactory.INSTANCE.toGeometry(shape);
        }
        else if (resultType != null && VertexObjectDAO.class.isAssignableFrom(resultType))
        {
          MdGraphClassDAOIF mdClass = MdGraphClassDAO.getMdGraphClassByTableName(className);

          if (mdClass != null && mdClass instanceof MdVertexDAOIF)
          {
            VertexObjectDAO vObject = VertexObjectDAO.newInstance((MdVertexDAOIF) mdClass);

            this.populateDAO(result.toElement(), vObject);

            return vObject;
          }
        }
      }

      if (names.size() > 1)
      {
        Map<String, Object> row = new HashMap<String, Object>();

        for (String name : names)
        {
          if (!name.equals("@class") && !name.equals("_class"))
          {
            row.put(name, this.getPropertyValue(result.getProperty(name), null));
          }
        }

        return row;
      }
      else
      {
        String name = names.iterator().next();

        return this.getPropertyValue(result.getProperty(name), null);
      }
    }
    else if (value instanceof List)
    {
      return ( (List<?>) value ).stream().map(v -> this.getPropertyValue(v, null)).collect(Collectors.toList());
    }

    return value;
  }
}
