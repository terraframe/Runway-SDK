/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

/**
 * Builds the metadata for an attribute.
 */
public class CommonAttributeMdBuilder
{
  /**
   * The destination of the metadata.
   */
  protected AttributeMdDTO dest;

  /**
   * The attribute display label
   */
  private String displayLabel;

  /**
   * The attribute description
   */
  private String description;

  /**
   * Denotes if this attribute is required
   */
  private boolean required;

  /**
   * Denotes if this attribute is immutable
   */
  private boolean immutable;

  /**
   * The oid of the attribute metadata;
   */
  private String oid;

  /**
   * The flag denoting if the attribute is a system attribute.
   */
  private boolean system;

  /**
   * The attribute name
   */
  private String name;

  /**
   * Flag denoting if the attribute has getters and setters
   */
  private boolean generateAccessor;

//  private XPath xpath;

  /**
   *
   * @param source
   * @param dest
   */
  protected CommonAttributeMdBuilder(AttributeMdDTO source, AttributeMdDTO dest)
  {
    this.dest = dest;

    this.displayLabel = source.getDisplayLabel();
    this.description = source.getDescription();
    this.required = source.isRequired();
    this.immutable = source.isImmutable();
    this.oid = source.getOid();
    this.system = source.isSystem();
    this.name = source.getName();
    this.generateAccessor = source.getGenerateAccessor();

  }

  /**
   * Sets the metadata on the provided AttributeMdDTO
   *
   * @return
   */
  protected void build()
  {
    dest.setDisplayLabel(displayLabel);
    dest.setDescription(description);
    dest.setRequired(required);
    dest.setImmutable(immutable);
    dest.setOid(oid);
    dest.setSystem(system);
    dest.setName(name);
    dest.setGenerateAccessor(generateAccessor);
  }

}
