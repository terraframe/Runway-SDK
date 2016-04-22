/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.generation.GeneratorIF;


public interface MdTypeDAOIF extends MetadataDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_type";

  /**
   * Column Name of the attribute that stores the name of the relationship that is defined.
   */
  public static final String TYPE_NAME_COLUMN       = "type_name";
  
  /**
   * Column Name of the attribute that stores the name of the package of the relationship that is defined.
   */
  public static final String PACKAGE_NAME_COLUMN    = "package_name";
  
  /**
   * Column name root of the id.  Denormalized for metadata loading performance.
   */
  public static final String ROOT_ID_COLUMN         = "root_id";
  
  /**
   * Column name of the attribute that stores the base .class blob.
   */
  public static final String BASE_CLASS_COLUMN      = "base_class";

  /**
   * Column name of the attribute that stores the base .java text.
   */
  public static final String BASE_SOURCE_COLUMN     = "base_source";

  /**
   * Column name of the attribute that stores the dto .class blob 
   */
  public static final String DTO_BASE_CLASS_COLUMN  = "dto_class";
  
  /**
   * Column name of the attribute that stores the dto .java text
   */
  public static final String DTO_BASE_SOURCE_COLUMN = "dto_source";

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
   */
  public String getDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels();

  /**
   *Returns the name of the type that this MdType definess.
   * @return the name of the type that this MdType definess.
   */
  public String getTypeName();

  /**
   * Returns the name of the package of the class that this object defines.
   * @return name of the package of the class that this object defines.
   */
  public String getPackage();

  /**
   * Returns true if this object defines a type in the system package, false otherwise.
   *
   * @return true if this object defines a type in the system package, false otherwise.
   */
  public boolean isSystemPackage();

  /**
   * Returns true if the object is exported, false otherwise.
   * 
   * @return true if the object is exported, false otherwise.
   */
  public boolean isExported();
  
  /**
   * Returns the type that this object defines.
   * The type consits of the package plus the type name.
   * @return the type that this object define.
   */
  public String definesType();

  /**
   * Returns true if this <code>MdTypeDAOIF</code> object is defined in the database, false otherwise.
   * @return true if this <code>MdTypeDAOIF</code> object is defined in the database, false otherwise.
   */
  public boolean isDefined();

  /**
   * Returns <code>MdMethodDAOIF</code> object of the method with the given name defined by this <code>MdTypeDAOIF</code>.
   * @return <code>MdMethodDAOIF</code> object of the method with the given name defined by this <code>MdTypeDAOIF</code>.
   */
  public MdMethodDAOIF getMdMethod(String methodName);

  /**
   * Returns an List of <code>MdMethodDAOIF</code> objects that this <code>MdTypeDAOIF</code>. defines.
   * @return an List of <code>MdMethodDAOIF</code> objects that this <code>MdTypeDAOIF</code>. defines.
   */
  public List<MdMethodDAOIF> getMdMethods();

  /**
   * Returns a list of the <code>MdMethodDAOIF</code> defined by this <code>MdTypeDAOIF</code>. ordered
   * alphabetically by their name.
   * @return
   */
  public List<MdMethodDAOIF> getMdMethodsOrdered();

  /**
   * Returns a list of all generators used to generate source
   * for this MdType.
   *
   * @return
   */
  public List<GeneratorIF> getGenerators();


  /**
   * Returns true if an attribute that stores source or class has been modified.
   *
   * @return true if an attribute that stores source or class has been modified.
   */
  public abstract boolean javaArtifactsModifiedOnObject();
  
  /**
   * Returns true if source should be generated
   * 
   * @return
   */
  public Boolean isGenerateSource();
}
