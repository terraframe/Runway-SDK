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
package com.runwaysdk.gis.dataaccess.attributes.entity;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.gis.AttributePolygonParseException;
import com.runwaysdk.gis.dataaccess.AttributePolygonIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AttributePolygon extends AttributeGeometry implements AttributePolygonIF
{

  /**
   *
   */
  private static final long serialVersionUID = -4331978938543329341L;

  private Polygon           polygon          = null;

  /**
   * Inherited constrcutor, sets <code>name</code> and
   * <code>definingEntityType</code>.
   * 
   * @param name
   *          The name of this character attribute.
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntityType
   *          The class that defines this attribute.
   */
  protected AttributePolygon(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>,
   * <code>definingEntityType</code>, and <code>value</code>.
   * 
   * @param name
   *          The name of this character attribute.
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntityType
   *          The type that defines this attribute.
   * @param value
   *          The value of this character. "<code>true</code>" or "
   *          <code>false</code>"
   */
  protected AttributePolygon(String name, String mdAttributeKey, String definingEntityType, Polygon polygon)
  {
    super(name, mdAttributeKey, definingEntityType);

    this.polygon = polygon;
  }

  @Override
  public Geometry getGeometry()
  {
    return this.getPolygon();
  }

  /**
   * Returns the Polygon object.
   * 
   * @return Polygon object.
   */
  public Polygon getPolygon()
  {
    return this.polygon;
  }

  /**
   * Sets the Polygon.
   * 
   * @param Polygon
   */
  public void setPolygon(Polygon _polygon)
  {
    if (_polygon == null)
    {
      if (this.polygon != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.polygon == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.polygon.equalsExact(_polygon))
        {
          this.setModified(true);
        }
      }
    }

    this.polygon = _polygon;

    if (this.polygon != null)
    {
      super.setValue(this.polygon.toText());
    }
    else
    {
      super.setValue("");
    }
  }

  /**
   * Returns the value of the attribute as stored in the database. Not all
   * objects are represented as Strings in the <code>Attribute</code> hierarchy.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   * 
   * @return value of the attribute
   */
  public Object getRawValueObject()
  {
    if (this.polygon != null)
    {
      this.polygon.setSRID(this.getMdAttribute().getSRID());
    }

    return this.polygon;
  }

  /**
   * Returns a string representation of the line.
   * 
   * @return string representation of the line.
   */
  public String getValue()
  {
    if (this.polygon == null)
    {
      return "";
    }
    else
    {
      return this.polygon.toText();
    }
  }

  /**
   * Some attributes store objects instead of strings.
   * 
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getPolygon();
  }

  /**
   * Assumes WKT format for a line
   * 
   * @param WKT
   */
  public void setValue(String value)
  {
    if (value == null || value.trim().equals(""))
    {
      if (this.polygon != null)
      {
        this.polygon = null;
        this.setModified(true);
      }
    }
    else
    {
      GeometryFactory geometryFactory = new GeometryFactory();
      WKTReader geometryReader = new WKTReader(geometryFactory);

      try
      {
        Geometry newGeometry = geometryReader.read(value);

        if (newGeometry instanceof Polygon)
        {
          Polygon newPolyGon = (Polygon) newGeometry;

          if (this.polygon == null)
          {
            this.setModified(true);
          }
          else if (!this.polygon.equalsExact(newPolyGon))
          {
            this.setModified(true);
          }

          this.polygon = newPolyGon;
        }
        else
        {
          String errMsg = "Object is of type [" + newGeometry.getClass().getName() + "] instead of type [" + Polygon.class.getName() + "].";
          throw new AttributePolygonParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
        }
      }
      catch (ParseException pe)
      {
        String errMsg = "Value [" + value + "] is not a valid [" + LineString.class.getName() + "].";
        throw new AttributePolygonParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
      }
    }

    if (this.polygon != null)
    {
      super.setValue(this.polygon.toText());
    }
    else
    {
      super.setValue("");
    }
  }

  /**
   * Most, but not all, attributes are represented as strings. For some, they
   * are represented as objects. Object will be cast into the appropriate type.
   * Precondition object is of expected type.
   */
  public void setValue(Object object)
  {
    this.setPolygon((Polygon) object);
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return MdAttributeLineStringDAOIF that defines the this attribute
   */
  public MdAttributePolygonDAOIF getMdAttribute()
  {
    return (MdAttributePolygonDAOIF) super.getMdAttribute();
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition: </b> true <br/>
   * <b>Postcondition: </b> true
   * 
   * @return a deep clone of this Attribute
   */
  @Override
  public AttributePolygon attributeClone()
  {
    Polygon clonedGeom = null;
    if (this.polygon != null)
    {
      clonedGeom = (Polygon) this.polygon.clone();
    }
    return new AttributePolygon(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), clonedGeom);
  }

}
