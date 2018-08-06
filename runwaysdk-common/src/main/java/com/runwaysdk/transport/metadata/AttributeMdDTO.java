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
package com.runwaysdk.transport.metadata;

import java.io.Serializable;

/**
 * Describes the metadata for an attribute
 */
public abstract class AttributeMdDTO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1774885854894832472L;

  /**
   * The display label of the attribute.
   */
  private String displayLabel;
  
  /**
   * The description of the attribute.
   */
  private String description;
  
  /**
   * Denotes if this attribute is required.
   */
  private boolean required;
  
  /**
   * Denotes if this attribute is immutable.
   */
  private boolean immutable;
  
  /**
   * Denotes if this attribute is system.
   */
  private boolean system;
  
  /**
   * The attribute name.
   */
  private String name;
  
  /**
   * The oid of the metadata that defines this attribute.
   */
  private String oid;
  
  /**
   * Flag denoting if this attribute has generated getters and setters
   */
  private boolean generateAccessor;
  
  /**
   * Default constructor.
   *
   */
  protected AttributeMdDTO()
  {
    required = false;
    immutable = false;
    displayLabel = "";
    description = "";
    oid = "";
    name = "";
    system = false;
    generateAccessor = true;    
  }
  
  /**
   * Returns the type display label.
   * 
   * @return
   */
  public String getDisplayLabel()
  {
    return displayLabel;
  }
  
  /**
   * Returns the attribute name.
   * 
   * @return
   */
  public String getName()
  {
    return name;
  }
  
  /**
   * Returns the type description.
   * 
   * @return
   */
  public String getDescription()
  {
    return description;
  }
  
  /**
   * Returns the oid of the attribute metadata that defines this attribute.
   * 
   * @return
   */
  public String getOid()
  {
    return oid;
  }

  /**
   * @return The flag denoting if this attribute has generated getters and setters
   */
  public boolean getGenerateAccessor()
  {
    return this.generateAccessor;
  }
  
  /**
   * Returns the flag denoting if this attribute is a system attribute.
   * 
   * @return
   */
  public boolean isSystem()
  {
    return system;
  }
  
  /**
   * Sets the flag that denotes if this attribute is a system attribute.
   * 
   */
  protected void setSystem(boolean system)
  {
    this.system = system;
  }
  
  /**
   * Sets the oid of the attribute metadata that defines this attribute. 
   * 
   * @param oid
   */
  protected void setOid(String oid)
  {
    this.oid = oid;
  }
  
  /**
   * Sets the attribute name.
   * 
   * @param name
   */
  protected void setName(String name)
  {
    this.name = name;
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
   * Sets the description.
   *
   * @param description
   */
  protected void setDescription(String description)
  {
    this.description = description;
  }
  
  /**
   * 
   * @return
   */
  public boolean isRequired()
  {
    return required;
  }
  
  /**
   * 
   * @return
   */
  public boolean isImmutable()
  {
    return immutable;
  }
  
  /**
   * Sets the required value.
   * 
   * @param required
   */
  protected void setRequired(boolean required)
  {
    this.required = required;
  }
  
  /**
   * Sets the immutable value.
   * 
   * @param immutable
   */
  protected void setImmutable(boolean immutable)
  {
    this.immutable = immutable;
  }
  
  /**
   * @return 
   */
  public abstract Class<?> getJavaType();
  
  protected void setGenerateAccessor(boolean generateAccessor)
  {
    this.generateAccessor = generateAccessor;
  }
}
