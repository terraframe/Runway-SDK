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
package com.runwaysdk.constants;

public interface MdAttributeClassificationInfo extends MdAttributeConcreteInfo
{
  /**
   * Class.
   */
  public static final String CLASS                       = Constants.METADATA_PACKAGE + ".MdAttributeClassification";

  /**
   * Name of the attribute that references the name of the {@link MdClassDAOIF}
   * used to define the attributes that make up this struct attribute.
   */
  public static final String REFERENCE_MD_CLASSIFICATION = "referenceMdClassification";

  public static final String ROOT                        = "root";

  /**
   * OID.
   */
  // public static final String ID_VALUE =
  // "b0ac09d5-4507-34b5-9794-0d03a400003a";
}
