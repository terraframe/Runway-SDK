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
package com.runwaysdk.gis.dataaccess.attributes.tranzient;

import com.runwaysdk.gis.AttributeMultiPointParseException;
import com.runwaysdk.gis.dataaccess.AttributeMultiPointIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AttributeMultiPoint extends AttributeGeometry implements AttributeMultiPointIF
{

 /**
   *
   */
  private static final long serialVersionUID = 5096011875313570310L;

  private MultiPoint multiPoint = null;

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
  protected AttributeMultiPoint(String name, String mdAttributeKey, String definingTransientType)
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
   * @param multiPoint initial multiPoint of the attribute
   */
  protected AttributeMultiPoint(String name, String mdAttributeKey, String definingTransientType, MultiPoint multiPoint)
  {
    super(name, mdAttributeKey, definingTransientType);

    this.multiPoint = multiPoint;
  }

  @Override
  public Geometry getGeometry()
  {
    return this.getMultiPoint();
  }

  /**
   * Returns the MultiPoint object.
   * @return MultiPoint object.
   */
  public MultiPoint getMultiPoint()
  {
    return this.multiPoint;
  }

  /**
   * Sets the MultiPoint.
   * @param MultiPoint
   */
  public void setMultiPoint(MultiPoint _multiPoint)
  {
    if (_multiPoint == null)
    {
      if (this.multiPoint != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.multiPoint == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.multiPoint.equalsExact(_multiPoint) )
        {
          this.setModified(true);
        }
      }
    }

    this.multiPoint = _multiPoint;

    if (this.multiPoint != null)
    {
      super.setValue(this.multiPoint.toText());
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
    return this.multiPoint;
  }

  /**
   * Returns a string representation of the line.
   * @return string representation of the line.
   */
  public String getValue()
  {
    if (this.multiPoint == null)
    {
      return "";
    }
    else
    {
      return this.multiPoint.toText();
    }
  }

  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getMultiPoint();
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
      if (this.multiPoint != null)
      {
        this.multiPoint = null;
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

        if (newGeometry instanceof MultiPoint)
        {
          MultiPoint newMultiPoint = (MultiPoint) newGeometry;

          if (this.multiPoint == null)
          {
            this.setModified(true);
          }
          else if (!this.multiPoint.equalsExact(newMultiPoint))
          {
            this.setModified(true);
          }

          this.multiPoint = newMultiPoint;
        }
        else
        {
          String errMsg = "Object is of type [" + newGeometry.getClass().getName() + "] instead of type ["
              + MultiPoint.class.getName() + "].";
          throw new AttributeMultiPointParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
        }
      }
      catch (ParseException pe)
      {
        String errMsg = "Value [" + value + "] is not a valid ["
              + MultiPoint.class.getName() + "].";
        throw new AttributeMultiPointParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
      }
    }

    if (this.multiPoint != null)
    {
      super.setValue(this.multiPoint.toText());
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
    this.setMultiPoint((MultiPoint)object);
  }


  /**
   * Returns the BusinessDAO that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributeMultiPointDAOIF that defines the this attribute
   */
  public MdAttributeMultiPointDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeMultiPointDAOIF)super.getMdAttributeConcrete();
  }
}
