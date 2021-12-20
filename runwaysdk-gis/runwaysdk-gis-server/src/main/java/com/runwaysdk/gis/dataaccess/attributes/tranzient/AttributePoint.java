/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.gis.dataaccess.attributes.tranzient;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.gis.AttributePointParseException;
import com.runwaysdk.gis.dataaccess.AttributePointIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AttributePoint extends AttributeGeometry implements AttributePointIF
{

  /**
   *
   */
  private static final long serialVersionUID = -8800989376918257692L;

  private Point point = null;

  /**
   * Creates an attribute with the given name and initializes the value to blank.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType().equals("") <br>
   * <b>Precondition: </b> definingTransientType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   */
  protected AttributePoint(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Creates an attribute with the given name and initializes the value to the given
   * value.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType represents a class that defines an attribute with
   * this name <br>
   * <b>Precondition: </b> value != null
   *
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   * @param point initial point of the attribute
   */
  protected AttributePoint(String name, String mdAttributeKey, String definingTransientType, Point point)
  {
    super(name, mdAttributeKey, definingTransientType);

    this.point = point;
  }

  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  @Override
  public Geometry getGeometry()
  {
    return this.getPoint();
  }

  /**
   * Returns the Point object.
   * @return Point object.
   */
  public Point getPoint()
  {
    return this.point;
  }

  /**
   * Sets the point.
   * @param point
   */
  public void setPoint(Point _point)
  {
    if (_point == null)
    {
      if (this.point != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.point == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.point.equalsExact(_point) )
        {
          this.setModified(true);
        }
      }
    }

    this.point = _point;

    if (this.point != null)
    {
      super.setValue(this.point.toText());
    }
    else
    {
      super.setValue("");
    }
  }

  /**
   * Returns the value of the attribute as stored in the database.
   * Not all objects are represented as Strings in the <code>Attribute</code>
   * hierarchy.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return value of the attribute
   */
  public Object getRawValueObject()
  {
    return this.point;
  }

  /**
   * Returns a string representation of the point.
   * @return string representation of the point.
   */
  public String getValue()
  {
    if (this.point == null)
    {
      return "";
    }
    else
    {
      return this.point.toText();
    }
  }


  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getPoint();
  }

  /**
   * Sets the point with the following coordinates.
   * @param x
   * @param y
   */
  public void setValue(double x, double y)
  {
    GeometryFactory geomertrFactory = new GeometryFactory();
    Point newPoint = geomertrFactory.createPoint(new Coordinate(x, y));

    if (this.point == null)
    {
      this.setModified(true);
    }
    else
    {
      if (!this.point.equalsExact(newPoint))
      {
        this.setModified(true);
      }
    }

    this.point = newPoint;
  }

  /**
   * Sets the point with the following coordinates.
   * @param x
   * @param y
   * @param z
   */
  public void setValue(double x, double y, double z)
  {
    GeometryFactory geomertrFactory = new GeometryFactory();
    Point newPoint = geomertrFactory.createPoint(new Coordinate(x, y, z));

    if (this.point == null)
    {
      this.setModified(true);
    }
    else
    {
      if (!this.point.equalsExact(newPoint))
      {
        this.setModified(true);
      }
    }

    this.point = newPoint;
  }

  /**
   * Assumes WKT format for a point
   *
   * @param WKT
   */
  public void setValue(String value)
  {
    if (value == null || value.trim().equals(""))
    {
      if (this.point != null)
      {
        this.point = null;
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

        if (newGeometry instanceof Point)
        {
          Point newPoint = (Point) newGeometry;

          if (this.point == null)
          {
            this.setModified(true);
          }
          else if (!this.point.equalsExact(newPoint))
          {
            this.setModified(true);
          }

          this.point = newPoint;
        }
        else
        {
          String errMsg = "Object is of type [" + newGeometry.getClass().getName() + "] instead of type ["
              + Point.class.getName() + "].";
          throw new AttributePointParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
        }
      }
      catch (ParseException pe)
      {
        String errMsg = "Value [" + value + "] is not a valid ["
              + Point.class.getName() + "].";
        throw new AttributePointParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
      }
    }

    if (this.point != null)
    {
      super.setValue(this.point.toText());
    }
    else
    {
      super.setValue("");
    }
  }

  /**
   * Most, but not all, attributes are represented as strings.  For some, they
   * are represented as objects. Object will be cast into the appropriate type.
   *  Precondition object is of expected type.
   */
  public void setValue(Object object)
  {
    this.setPoint((Point)object);
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributePointDAOIF that defines the this attribute
   */
  public MdAttributePointDAOIF getMdAttributeConcrete()
  {
    return (MdAttributePointDAOIF)super.getMdAttributeConcrete();
  }

}
