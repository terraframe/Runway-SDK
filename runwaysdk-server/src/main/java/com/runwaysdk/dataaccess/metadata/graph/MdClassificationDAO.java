/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.metadata.graph;

import java.sql.Connection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.graph.generation.JavaArtifactMdGraphClassCommand;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.graph.MdClassificationInfo;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;

public class MdClassificationDAO extends MetadataDAO implements MdClassificationDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5015373068741693970L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdClassificationDAO()
  {
    super();
  }

  /**
   * Constructs a {@link MdClassificationDAO} from the given hashtable of
   * Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * 
   * 
   * @param attributeMap
   * @param type
   */
  public MdClassificationDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /**
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdClassificationDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdClassificationDAO(attributeMap, MdClassificationInfo.CLASS);
  }

  /**
   * Returns a new {@link MdClassificationDAO}. Some attributes will contain
   * default values, as defined in the attribute metadata. Otherwise, the
   * attributes will be blank.
   * 
   * @return instance of {@link MdClassificationDAO}.
   */
  public static MdClassificationDAO newInstance()
  {
    return (MdClassificationDAO) BusinessDAO.newInstance(MdClassificationInfo.CLASS);
  }

  /**
   * Returns the name of the package of the type that this object defines.
   * 
   * @return name of the package of the type that this object defines.
   */
  public String getPackage()
  {
    return this.getAttributeIF(MdClassificationInfo.PACKAGE).getValue();
  }

  /**
   * Returns the name of the type that this MdType definess.
   * 
   * @return the name of the type that this MdType definess.
   */
  public String getTypeName()
  {
    return this.getAttributeIF(MdTypeInfo.NAME).getValue();
  }

  /**
   * Returns the display label of this metadata object
   * 
   * @param locale
   * 
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MdTypeInfo.DISPLAY_LABEL) ).getLocalValues();
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    return "Classification:" + this.definesType();
  }

  /**
   * Returns the type that this object defines. The type consits of the package
   * plus the type name.
   * 
   * @return the type that this object defines.
   */
  public String definesType()
  {
    return EntityDAOFactory.buildType(this.getPackage(), this.getTypeName());
  }

}