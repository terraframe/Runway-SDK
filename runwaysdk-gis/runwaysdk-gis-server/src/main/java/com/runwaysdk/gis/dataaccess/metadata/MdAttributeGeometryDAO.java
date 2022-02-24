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
package com.runwaysdk.gis.dataaccess.metadata;

import java.util.Map;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_S;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcrete_T;
import com.runwaysdk.gis.constants.MdAttributeGeometryInfo;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.gis.dataaccess.metadata.graph.MdAttributeGeometry_G;
import com.runwaysdk.system.gis.metadata.InvalidDimensionException;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;

public abstract class MdAttributeGeometryDAO extends MdAttributeConcreteDAO implements MdAttributeGeometryDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 1887995108615716088L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdAttributeGeometryDAO()
  {
    super();
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   *
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null
   *
   * @param attributeMap
   * @param classType
   */
  public MdAttributeGeometryDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   *
   * @param validateRequired
   */
  public String save(boolean validateRequired)
  {
    int dimension = this.getDimension();

    if (dimension < 0 || dimension > 4)
    {
      MdAttributeDAOIF dimensionMdAttribute = this.getAttributeIF(MdAttributeGeometryInfo.DIMENSION).getMdAttribute();
      String errMsg = "Dimension value [" + dimension + "] on attribute [" + dimensionMdAttribute.definesAttribute() + "] is invalid.  Please specify a value between 0 and 4.";
      throw new InvalidDimensionException(errMsg, dimension, dimensionMdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()));
    }

    return super.save(validateRequired);
  }

  /**
   * Initializes the strategy object.
   */
  protected void initializeStrategyObject()
  {
    if (this.definedByClass() instanceof MdEntityDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeGeometry_E(this));
    }
    else if (this.definedByClass() instanceof MdGraphClassDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeGeometry_G(this));
    }    
    else if (this.definedByClass() instanceof MdTransientDAOIF)
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_S(this));
    }
    else
    {
      this.getObjectState().setMdAttributeStrategy(new MdAttributeConcrete_T(this));
    }
  }
  

  /**
   * Returns the projection OID used for this geometry attribute;
   * 
   * @return projection OID used for this geometry attribute;
   */
  public int getSRID()
  {
    // if this is empty, set the value to 0
    String srid = this.getAttributeIF(MdAttributeGeometryInfo.SRID).getValue();
    if (srid.equals(""))
    {
      return 0;
    }
    else
    {
      return Integer.parseInt(srid);
    }
  }

  /**
   * Returns the dimension used for this attribute;
   * 
   * @return dimension used for this attribute;
   */
  public int getDimension()
  {
    // if this is empty, set the value to 0
    String srid = this.getAttributeIF(MdAttributeGeometryInfo.DIMENSION).getValue();
    if (srid.equals(""))
    {
      return 0;
    }
    else
    {
      return Integer.parseInt(srid);
    }
  }

  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    throw new UnsupportedOperationException("getAttributeMdSession() not supported for [" + this.getClass() + "].");
  }

  public String queryAttributeClass()
  {
    return com.runwaysdk.query.SelectableGeometry.class.getName();
  }
}
