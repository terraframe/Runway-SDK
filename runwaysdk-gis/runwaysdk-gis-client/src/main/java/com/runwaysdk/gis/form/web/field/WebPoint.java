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
package com.runwaysdk.gis.form.web.field;

import com.runwaysdk.form.web.WebFormVisitor;
import com.runwaysdk.form.web.field.WebAttribute;
import com.runwaysdk.gis.AttributePointParseExceptionDTO;
import com.runwaysdk.gis.form.web.metadata.WebPointMd;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class WebPoint extends WebAttribute
{
  private Point point;

  public WebPoint(WebPointMd fMd)
  {
    super(fMd);
    this.point = null;
  }

  @Override
  public void accept(WebFormVisitor visitor)
  {
    super.accept(visitor);
    visitor.visit(this);
  }

  @Override
  public WebPointMd getFieldMd()
  {
    return (WebPointMd) super.getFieldMd();
  }

  @Override
  public void setValue(Object object)
  {
    if (object instanceof Point)
    {
      this.setPoint(point);
    }
    else
    {
      super.setValue(object);
    }
  }

  /**
   * Converts the value of this WebPoint to a Point object. If a point object is
   * already set then that is returned. Otherwise, if only a string value exists
   * it is parsed into a Point and returned. This call does not affect the
   * underlying data.
   * 
   * If no values are available then null is returned.
   * 
   * @return
   * @throws AttributePointParseException
   *           If the string value is an invalid point.
   */
  public Point convertToPoint()
  {
    if (this.point != null)
    {
      return this.point;
    }

    String value = super.getValue();
    if (value != null)
    {
      try
      {
        return (Point) new WKTReader().read(value);
      }
      catch (ParseException e)
      {
        throw new AttributePointParseExceptionDTO(AttributePointParseExceptionDTO.class.getName(), this
            .getFieldMd().getDisplayLabel(), value);
      }
    }

    return null;
  }

  @Override
  public String getValue()
  {
    return this.point != null ? this.point.toText() : super.getValue();
  }

  /**
   * Sets the x (longitude) and y (latitude) of the point.
   * 
   * @param x
   *          String
   * @param y
   *          String
   */
  public void setValue(String x, String y)
  {
    this.setValue(Double.parseDouble(x), Double.parseDouble(y));
  }

  /**
   * Sets the x (longitude) and y (latitude) of the point.
   * 
   * @param x
   *          double
   * @param y
   *          double
   */
  public void setValue(double x, double y)
  {
    Point point = new GeometryFactory().createPoint(new Coordinate(x, y));
    this.setPoint(point);
  }

  public void setPoint(Point point)
  {
    this.point = point;
    this.setModified(true);
  }

  public Point getPoint()
  {
    return point;
  }

  @Override
  public Point getObjectValue()
  {
    return this.point;
  }
}
