/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.gis.dataaccess.attributes.entity;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.gis.dataaccess.AttributeGeometryIF;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.AttributePolygonParseException;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AttributeShape extends AttributeGeometry implements AttributeGeometryIF
{

  /**
   *
   */
  private static final long serialVersionUID = -4331978938543329341L;

  private Geometry          geometry         = null;

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
  protected AttributeShape(String name, String mdAttributeKey, String definingEntityType)
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
  protected AttributeShape(String name, String mdAttributeKey, String definingEntityType, Geometry polygon)
  {
    super(name, mdAttributeKey, definingEntityType);

    this.geometry = polygon;
  }

  @Override
  public Geometry getGeometry()
  {
    return this.getGeometry();
  }

  /**
   * Sets the Geometry.
   * 
   * @param Geometry
   */
  public void setGeometry(Geometry _polygon)
  {
    if (_polygon == null)
    {
      if (this.geometry != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.geometry == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.geometry.equalsExact(_polygon))
        {
          this.setModified(true);
        }
      }
    }

    this.geometry = _polygon;

    if (this.geometry != null)
    {
      super.setValue(this.geometry.toText());
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
    if (this.geometry != null)
    {
      this.geometry.setSRID(this.getMdAttribute().getSRID());
    }

    return this.geometry;
  }

  /**
   * Returns a string representation of the line.
   * 
   * @return string representation of the line.
   */
  public String getValue()
  {
    if (this.geometry == null)
    {
      return "";
    }
    else
    {
      return this.geometry.toText();
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
    return this.getGeometry();
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
      if (this.geometry != null)
      {
        this.geometry = null;
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

        if (this.geometry == null)
        {
          this.setModified(true);
        }
        else if (!this.geometry.equalsExact(newGeometry))
        {
          this.setModified(true);
        }

        this.geometry = newGeometry;
      }
      catch (ParseException pe)
      {
        String errMsg = "Value [" + value + "] is not a valid [" + LineString.class.getName() + "].";
        throw new AttributePolygonParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
      }
    }

    if (this.geometry != null)
    {
      super.setValue(this.geometry.toText());
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
    this.setGeometry((Geometry) object);
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
  public MdAttributeGeometryDAOIF getMdAttribute()
  {
    return (MdAttributeGeometryDAOIF) super.getMdAttribute();
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
  public AttributeShape attributeClone()
  {
    Geometry clonedGeom = null;
    if (this.geometry != null)
    {
      clonedGeom = (Geometry) this.geometry.clone();
    }
    return new AttributeShape(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), clonedGeom);
  }

}
