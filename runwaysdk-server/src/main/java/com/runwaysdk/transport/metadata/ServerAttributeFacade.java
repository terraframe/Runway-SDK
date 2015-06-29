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

import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class ServerAttributeFacade
{
  /**
   * Sets the metadata for an AttributeDTO
   * 
   * @param source
   * @param dest
   */
  public static void setAttributeMetadata(MdAttributeDAOIF source, AttributeMdDTO dest)
  {
    ServerAttributeMdBuilder builder = new ServerAttributeMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeCharacterDTO
   * 
   * @param source
   * @param dest
   */
  public static void setCharacterMetadata(MdAttributeDAOIF source, AttributeCharacterMdDTO dest)
  {
    ServerAttributeCharacterMdBuilder builder = new ServerAttributeCharacterMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeBooleanDTO
   * 
   * @param source
   * @param dest
   */
  public static void setBooleanMetadata(MdAttributeDAOIF source, AttributeBooleanMdDTO dest)
  {
    ServerAttributeBooleanMdBuilder builder = new ServerAttributeBooleanMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeStructDTO
   * 
   * @param source
   * @param dest
   */
  public static void setStructMetadata(MdAttributeDAOIF source, AttributeStructMdDTO dest)
  {
    ServerAttributeStructMdBuilder builder = new ServerAttributeStructMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeFloatDTO
   * 
   * @param source
   * @param dest
   */
  public static void setDecMetadata(MdAttributeDAOIF source, AttributeDecMdDTO dest)
  {
    ServerAttributeDecMdBuilder builder = new ServerAttributeDecMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeReferenceDTO
   * 
   * @param source
   * @param dest
   */
  public static void setReferenceMetadata(MdAttributeDAOIF source, AttributeReferenceMdDTO dest)
  {
    ServerAttributeReferenceMdBuilder builder = new ServerAttributeReferenceMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeTermDTO
   * 
   * @param source
   * @param dest
   */
  public static void setTermMetadata(MdAttributeDAOIF source, AttributeTermMdDTO dest)
  {
    ServerAttributeTermMdBuilder builder = new ServerAttributeTermMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeSymmetricDTO
   * 
   * @param source
   * @param dest
   */
  public static void setEncryptionMetadata(MdAttributeDAOIF source, AttributeEncryptionMdDTO dest)
  {
    ServerAttributeEncryptionMdBuilder builder = new ServerAttributeEncryptionMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeLongDTO
   * 
   * @param source
   * @param dest
   */
  public static void setNumberMetadata(MdAttributeDAOIF source, AttributeNumberMdDTO dest)
  {
    ServerAttributeNumberMdBuilder builder = new ServerAttributeNumberMdBuilder(source, dest);
    builder.build();
  }

  /**
   * Sets the metadata for an AttributeEnumerationDTO
   * 
   * @param source
   * @param dest
   */
  public static void setEnumerationMetadata(MdAttributeDAOIF source, AttributeEnumerationMdDTO dest)
  {
    ServerAttributeEnumerationMdBuilder builder = new ServerAttributeEnumerationMdBuilder(source, dest);
    builder.build();
  }

  /**
   * @param mdAttributeIF
   * @param attributeMdDTO
   */
  public static void setMultiReferenceMetadata(MdAttributeDAOIF source, AttributeMultiReferenceMdDTO dest)
  {
    ServerAttributeMultiReferenceMdBuilder builder = new ServerAttributeMultiReferenceMdBuilder(source, dest);
    builder.build();
  }
}
