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
package com.runwaysdk.transport.metadata.caching;


/**
 *
 * This class is used to transport metadata from server to client. This
 * allows the client to cache the metadata. A DTO is not used in order to
 * reduce network traffic. This metadata is specific to a particular session.
 *
 * @author Richard Rowlands
 *
 */
public abstract class AttributeMdSession {
  private String displayLabel;
  private String description;
  private boolean required;
  private boolean immutable;
  private boolean system;
  private String attributeName;
  private String defaultValue;
  
  private boolean readable;
  private boolean writable;

  public AttributeMdSession() {
    // Value population of this object is done in MdAttributeDAO::populateAttributeMdSession
  }
  
  public boolean isSystem() {
    return system;
  }
  
  public void setSystem(boolean system) {
    this.system = system;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getDisplayLabel() {
    return displayLabel;
  }

  public void setDisplayLabel(String displayLabel) {
    this.displayLabel = displayLabel;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public boolean isImmutable() {
    return immutable;
  }

  public void setImmutable(boolean immutable) {
    this.immutable = immutable;
  }

  public String getAttributeName() {
    return attributeName;
  }

  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  public boolean isReadable()
  {
    return readable;
  }

  public void setReadable(boolean readable)
  {
    this.readable = readable;
  }

  public boolean isWritable()
  {
    return writable;
  }

  public void setWritable(boolean writable)
  {
    this.writable = writable;
  }
}
