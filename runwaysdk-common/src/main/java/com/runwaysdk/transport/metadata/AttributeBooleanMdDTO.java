/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.transport.metadata;


/**
 * Describes metadata for an Attribute Boolean.
 */
public class AttributeBooleanMdDTO extends AttributeMdDTO
{
  /**
   *
   */
  private static final long serialVersionUID = -5380283345818203444L;

  /**
   * The positive display label of the attribute.
   */
  private String positiveDisplayLabel;

  /**
   * The negative display label of the attribute.
   */
  private String negativeDisplayLabel;

  /**
   * Default constructor.
   */
  protected AttributeBooleanMdDTO()
  {
    super();
  }


  /**
   * Returns the display label for the positive of the negative option.
   * @param booleanOption
   * @return display label for the positive of the negative option.
   */
  public String getOptionDisplayLabel(boolean booleanOption)
  {
    if (booleanOption == true)
    {
      return this.getPositiveDisplayLabel();
    }
    else
    {
      return this.getNegativeDisplayLabel();
    }
  }

  /**
   * Returns the positive display label.
   *
   * return positive displayLabel
   */
  public String getPositiveDisplayLabel()
  {
    return this.positiveDisplayLabel;
  }

  /**
   * Sets the positive display label.
   *
   * @param positive displayLabel
   */
  protected void setPositiveDisplayLabel(String positiveDisplayLabel)
  {
    this.positiveDisplayLabel = positiveDisplayLabel;
  }

  /**
   * Returns the negative display label.
   *
   * return negative displayLabel
   */
  public String getNegativeDisplayLabel()
  {
    return this.negativeDisplayLabel;
  }

  /**
   * Sets the negative display label.
   *
   * @param negative displayLabel
   */
  protected void setNegativeDisplayLabel(String negativeDisplayLabel)
  {
    this.negativeDisplayLabel = negativeDisplayLabel;
  }

  @Override
  public Class<?> getJavaType()
  {
    return Boolean.class;
  }

}
