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
package com.runwaysdk.form;


public abstract class FormMd
{
  /**
   * The type display label.
   */
  private String displayLabel;

  /**
   * The type description.
   */
  private String description;

  /**
   * The oid of the metadata defining the type.
   */
  private String oid;
  
  /**
   * The name of the form.
   */
  private String formName;
  
  /**
   * The oid of the form
   */
  private String formMdClass;
  
  protected FormMd()
  {
    super();
    this.formName = null;
    this.formMdClass = null;
    this.displayLabel = null;
    this.description = null;
    this.oid = null;
  }

  protected void setFormName(String formName)
  {
    this.formName = formName;
  }
  
  protected void setFormMdClass(String formMdClass)
  {
    this.formMdClass = formMdClass;
  }
  
  public String getFormName()
  {
    return formName;
  }
  
  public String getFormMdClass()
  {
    return formMdClass;
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
   * Returns the oid of the metadata that defines this type.
   *
   * @return
   */
  public String getOid()
  {
    return oid;
  }

  /**
   * Sets the oid.
   *
   * @param oid
   */
  protected void setOid(String oid)
  {
    this.oid = oid;
  }

}
