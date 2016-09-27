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

import com.runwaysdk.gis.AttributeMultiPolygonParseException;
import com.runwaysdk.gis.dataaccess.AttributeMultiPolygonIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AttributeMultiPolygon extends AttributeGeometry implements AttributeMultiPolygonIF
{

  /**
	 * 
	 */
  private static final long serialVersionUID = -300087508323133169L;

  private MultiPolygon multiPolygon = null;

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
  protected AttributeMultiPolygon(String name, String mdAttributeKey, String definingTransientType)
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
   * @param multiPolygon initial multiPolygon of the attribute
   */
  protected AttributeMultiPolygon(String name, String mdAttributeKey, String definingTransientType, MultiPolygon multiPolygon)
  {
    super(name, mdAttributeKey, definingTransientType);

    this.multiPolygon = multiPolygon;
  }


  @Override
  public Geometry getGeometry()
  {
    return this.getMultiPolygon();
  }

  /**
   * Returns the MultiPolygon object.
   * @return MultiPolygon object.
   */
  public MultiPolygon getMultiPolygon()
  {
    return this.multiPolygon;
  }

  /**
   * Sets the MultiPolygon.
   * @param MultiPolygon
   */
  public void setMultiPolygon(MultiPolygon _multiPolygon)
  {
    if (_multiPolygon == null)
    {
      if (this.multiPolygon != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.multiPolygon == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.multiPolygon.equalsExact(_multiPolygon) )
        {
          this.setModified(true);
        }
      }
    }

    this.multiPolygon = _multiPolygon;

    if (this.multiPolygon != null)
    {
      super.setValue(this.multiPolygon.toText());
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
    return this.multiPolygon;
  }

  /**
   * Returns a string representation of the line.
   * @return string representation of the line.
   */
  public String getValue()
  {
    if (this.multiPolygon == null)
    {
      return "";
    }
    else
    {
      return this.multiPolygon.toText();
    }
  }

  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getMultiPolygon();
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
      if (this.multiPolygon != null)
      {
        this.multiPolygon = null;
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

        if (newGeometry instanceof MultiPolygon)
        {
          MultiPolygon newMultiPolygon = (MultiPolygon) newGeometry;

          if (this.multiPolygon == null)
          {
            this.setModified(true);
          }
          else if (!this.multiPolygon.equalsExact(newMultiPolygon))
          {
            this.setModified(true);
          }

          this.multiPolygon = newMultiPolygon;
        }
        else
        {
          String errMsg = "Object is of type [" + newGeometry.getClass().getName() + "] instead of type ["
              + MultiPolygon.class.getName() + "].";
          throw new AttributeMultiPolygonParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
        }
      }
      catch (ParseException pe)
      {
        String errMsg = "Value [" + value + "] is not a valid ["
              + MultiPolygon.class.getName() + "].";
        throw new AttributeMultiPolygonParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
      }
    }

    if (this.multiPolygon != null)
    {
      super.setValue(this.multiPolygon.toText());
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
    this.setMultiPolygon((MultiPolygon)object);
  }


  /**
   * Returns the BusinessDAO that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributeMultiPolygonDAOIF that defines the this attribute
   */
  public MdAttributeMultiPolygonDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeMultiPolygonDAOIF)super.getMdAttributeConcrete();
  }

}
