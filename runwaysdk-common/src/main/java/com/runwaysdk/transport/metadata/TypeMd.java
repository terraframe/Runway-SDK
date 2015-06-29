/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.transport.metadata;

import java.io.Serializable;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;

/**
 * Represents the read-only metadata for a type.
 */
public class TypeMd implements Serializable, Cloneable
{

  /**
   *
   */
  private static final long serialVersionUID = 1890119098038816299L;


  /**
   * The type display label.
   */
  private String displayLabel;

  /**
   * The type description.
   */
  private String description;

  /**
   * The id of the metadata defining the type.
   */
  private String id;

  /**
   * Default constructor.
   */
  public TypeMd()
  {
    displayLabel = "";
    description = "";
    id = "";
  }

  /**
   *
   * @param displayLabel
   * @param description
   */
  public TypeMd(String displayLabel, String description, String id)
  {
    this.displayLabel = displayLabel;
    this.description  = description;
    this.id           = id;
  }

  /**
   * Gets the display label.
   *
   * @return
   */
  public String getDisplayLabel()
  {
    return displayLabel;
  }

  /**
   * Sets the display label.
   *
   * @param displayLabel
   */
  protected void setDisplayLabel(String displayLabel)
  {
    this.displayLabel = displayLabel;
  }

  /**
   * Gets the description.
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Sets the description.
   *
   * @param description
   */
  protected void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Returns the id of the metadata that defines this type.
   *
   * @return
   */
  public String getId()
  {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id
   */
  protected void setId(String id)
  {
    this.id = id;
  }

  /**
   * Deep clones this TypeMd
   */
  public TypeMd clone()
  {
    try
    {
      TypeMd typeMd = (TypeMd) super.clone();
      typeMd.setDescription(description);
      typeMd.setDisplayLabel(displayLabel);
      typeMd.setId(id);

      return typeMd;
    }
    catch (CloneNotSupportedException e)
    {
      CommonExceptionProcessor.processException(
          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), e.getMessage(), e);
    }

    return null;
  }
}
