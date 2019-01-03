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

/**
 * Facade used to set attribute metadata on an attribute. This must be used to
 * set metadata on an attribute since metadata information on an attribute is
 * normally read-only.
 */
public class CommonAttributeFacade
{
  /**
   * Sets the metadata for an AttributeDTO
   * 
   * @param source
   * @param dest
   */
  public static void setAttributeMetadata(AttributeMdDTO source, AttributeMdDTO dest)
  {
    CommonAttributeMdBuilder builder = new CommonAttributeMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeCharacterDTO
   * 
   * @param source
   * @param dest
   */
  public static void setCharacterMetadata(AttributeCharacterMdDTO source, AttributeCharacterMdDTO dest)
  {
    CommonAttributeCharacterMdBuilder builder = new CommonAttributeCharacterMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeBooleanDTO
   * 
   * @param source
   * @param dest
   */
  public static void setBooleanMetadata(AttributeBooleanMdDTO source, AttributeBooleanMdDTO dest)
  {
    CommonAttributeBooleanMdBuilder builder = new CommonAttributeBooleanMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeStructDTO
   * 
   * @param source
   * @param dest
   */
  public static void setStructMetadata(AttributeStructMdDTO source, AttributeStructMdDTO dest)
  {
    CommonAttributeStructMdBuilder builder = new CommonAttributeStructMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeFloatDTO
   * 
   * @param source
   * @param dest
   */
  public static void setDecMetadata(AttributeDecMdDTO source, AttributeDecMdDTO dest)
  {
    CommonAttributeDecMdBuilder builder = new CommonAttributeDecMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeSymmetricDTO
   * 
   * @param source
   * @param dest
   */
  public static void setEncryptionMetadata(AttributeEncryptionMdDTO source, AttributeEncryptionMdDTO dest)
  {
    CommonAttributeEncryptionMdBuilder builder = new CommonAttributeEncryptionMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeLongDTO
   * 
   * @param source
   * @param dest
   */
  public static void setNumberMetadata(AttributeNumberMdDTO source, AttributeNumberMdDTO dest)
  {
    CommonAttributeNumberMdBuilder builder = new CommonAttributeNumberMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeEnumerationDTO
   * 
   * @param source
   * @param dest
   */
  public static void setEnumerationMetadata(AttributeEnumerationMdDTO source, AttributeEnumerationMdDTO dest)
  {
    CommonAttributeEnumerationMdBuilder builder = new CommonAttributeEnumerationMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeMultiReferenceDTO
   * 
   * @param source
   * @param dest
   */
  public static void setMultiReferenceMetadata(AttributeMultiReferenceMdDTO source, AttributeMultiReferenceMdDTO dest)
  {
    CommonAttributeMultiReferenceMdBuilder builder = new CommonAttributeMultiReferenceMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeMultiTermDTO
   * 
   * @param source
   * @param dest
   */
  public static void setMultiTermMetadata(AttributeMultiTermMdDTO source, AttributeMultiTermMdDTO dest)
  {
    CommonAttributeMultiReferenceMdBuilder builder = new CommonAttributeMultiReferenceMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeReferenceDTO
   * 
   * @param source
   * @param dest
   */
  public static void setReferenceMetadata(AttributeReferenceMdDTO source, AttributeReferenceMdDTO dest)
  {
    CommonAttributeReferenceMdBuilder builder = new CommonAttributeReferenceMdBuilder(source, dest);
    builder.build();
  }
}
