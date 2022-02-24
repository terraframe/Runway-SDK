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
package com.runwaysdk.gis.dataaccess.attributes.entity;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.gis.AttributeMultiLineStringParseException;
import com.runwaysdk.gis.dataaccess.AttributeMultiLineStringIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class AttributeMultiLineString extends AttributeGeometry implements AttributeMultiLineStringIF
{

  /**
   *
   */
  private static final long serialVersionUID = -2892462255610472618L;

  private MultiLineString   multiLineString  = null;

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
  protected AttributeMultiLineString(String name, String mdAttributeKey, String definingEntityType)
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
  protected AttributeMultiLineString(String name, String mdAttributeKey, String definingEntityType, MultiLineString multiLineString)
  {
    super(name, mdAttributeKey, definingEntityType);

    this.multiLineString = multiLineString;
  }

  /**
   * Returns the Geometry object.
   * 
   * @return Geometry object.
   */
  @Override
  public Geometry getGeometry()
  {
    return this.getMultiLineString();
  }

  /**
   * Returns the MultiLineString object.
   * 
   * @return MultiLineString object.
   */
  public MultiLineString getMultiLineString()
  {
    return this.multiLineString;
  }

  /**
   * Sets the multiLineString.
   * 
   * @param multiLineString
   */
  public void setMultiLineString(MultiLineString _multiLineString)
  {
    if (_multiLineString == null)
    {
      if (this.multiLineString != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.multiLineString == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.multiLineString.equalsExact(_multiLineString))
        {
          this.setModified(true);
        }
      }
    }

    this.multiLineString = _multiLineString;

    if (this.multiLineString != null)
    {
      super.setValue(this.multiLineString.toText());
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
    if (this.multiLineString != null)
    {
      this.multiLineString.setSRID(this.getMdAttribute().getSRID());
    }

    return this.multiLineString;
  }

  /**
   * Returns a string representation of the multiLineString.
   * 
   * @return string representation of the multiLineString.
   */
  public String getValue()
  {
    if (this.multiLineString == null)
    {
      return "";
    }
    else
    {
      return this.multiLineString.toText();
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
    return this.getMultiLineString();
  }

  /**
   * Assumes WKT format for a multiLineString
   * 
   * @param WKT
   */
  public void setValue(String value)
  {
    if (value == null || value.trim().equals(""))
    {
      if (this.multiLineString != null)
      {
        this.multiLineString = null;
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

        if (newGeometry instanceof MultiLineString)
        {
          MultiLineString newMultiLineString = (MultiLineString) newGeometry;

          if (this.multiLineString == null)
          {
            this.setModified(true);
          }
          else if (!this.multiLineString.equalsExact(newMultiLineString))
          {
            this.setModified(true);
          }

          this.multiLineString = newMultiLineString;
        }
        else
        {
          String errMsg = "Object is of type [" + newGeometry.getClass().getName() + "] instead of type [" + MultiLineString.class.getName() + "].";
          throw new AttributeMultiLineStringParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
        }
      }
      catch (ParseException pe)
      {
        String errMsg = "Value [" + value + "] is not a valid [" + MultiLineString.class.getName() + "].";
        throw new AttributeMultiLineStringParseException(errMsg, this.getDisplayLabel(CommonProperties.getDefaultLocale()), value);
      }
    }

    if (this.multiLineString != null)
    {
      super.setValue(this.multiLineString.toText());
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
    this.setMultiLineString((MultiLineString) object);
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return <code>MdAttributeMultiLineStringDAOIF</code> that defines the this
   *         attribute
   */
  public MdAttributeMultiLineStringDAOIF getMdAttribute()
  {
    return (MdAttributeMultiLineStringDAOIF) super.getMdAttribute();
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
  public AttributeMultiLineString attributeClone()
  {
    MultiLineString clonedGeom = null;
    if (this.multiLineString != null)
    {
      clonedGeom = (MultiLineString) this.multiLineString.clone();
    }
    return new AttributeMultiLineString(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), clonedGeom);
  }

}
