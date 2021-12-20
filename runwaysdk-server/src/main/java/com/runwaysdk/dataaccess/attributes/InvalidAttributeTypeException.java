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
package com.runwaysdk.dataaccess.attributes;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.session.Session;

public class InvalidAttributeTypeException extends AttributeException
{

  /**
   *
   */
  private static final long serialVersionUID = -6938271554042498350L;

  /**
   * Metadata describing the attribute that is not a reference.
   */
  private MdAttributeDAOIF mdAttribute;

  /**
   * Metadata that defines the expected attribute type.
   */
  private MdBusinessDAOIF expectedAttributeTypeDefinition;

  /**
   * Metadata that defines the given attribute type.
   */
  private MdBusinessDAOIF givenAttributeTypeDefinition;

  /**
   * Constructs a new InvalidReferenceAttributeException with the specified developer
   * message.
   *
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdAttribute
   *          Metadata describing the attribute that is not a reference.
   * @param expectedAttributeTypeDefinition
   *          Metadata that defines the expected attribute type.
   * @param givenAttributeTypeDefinition
   *          Metadata that defines the given attribute type.
   */
  public InvalidAttributeTypeException(String devMessage, MdAttributeDAOIF mdAttribute,
      MdBusinessDAOIF expectedAttributeTypeDefinition, MdBusinessDAOIF givenAttributeTypeDefinition)
  {
    super(devMessage);
    this.mdAttribute = mdAttribute;
    this.expectedAttributeTypeDefinition = expectedAttributeTypeDefinition;
    this.givenAttributeTypeDefinition = givenAttributeTypeDefinition;
  }

  /**
   * Constructs a new InvalidReferenceAttributeException with the specified developer message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this InvalidReferenceException's detail message.
   *
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param mdAttribute
   *          Metadata describing the attribute that is not a reference.
   * @param expectedAttributeTypeDefinition
   *          Metadata that defines the expected attribute type.
   * @param givenAttributeTypeDefinition
   *          Metadata that defines the given attribute type.
   */
  public InvalidAttributeTypeException(String devMessage, Throwable cause, MdAttributeDAOIF mdAttribute,
      MdBusinessDAOIF expectedAttributeTypeDefinition, MdBusinessDAOIF givenAttributeTypeDefinition)
  {
    super(devMessage, cause);
    this.mdAttribute = mdAttribute;
    this.expectedAttributeTypeDefinition = expectedAttributeTypeDefinition;
    this.givenAttributeTypeDefinition = givenAttributeTypeDefinition;
  }

  /**
   * Constructs a new InvalidReferenceAttributeException with the specified cause and a developer
   * message taken from the cause. This constructor is useful if the
   * InvalidReferenceException is a wrapper for another throwable.
   *
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param mdAttribute
   *          Metadata describing the attribute that is not a reference.
   * @param expectedAttributeTypeDefinition
   *          Metadata that defines the expected attribute type.
   * @param givenAttributeTypeDefinition
   *          Metadata that defines the given attribute type.
   */
  public InvalidAttributeTypeException(Throwable cause, MdAttributeDAOIF mdAttribute,
      MdBusinessDAOIF expectedAttributeTypeDefinition, MdBusinessDAOIF givenAttributeTypeDefinition)
  {
    super(cause);
    this.mdAttribute = mdAttribute;
    this.expectedAttributeTypeDefinition = expectedAttributeTypeDefinition;
    this.givenAttributeTypeDefinition = givenAttributeTypeDefinition;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   *
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.invalidAttributeTypeException(this.getLocale(),
        this.mdAttribute.getDisplayLabel(Session.getCurrentLocale()),
        this.mdAttribute.definedByClass().getDisplayLabel(Session.getCurrentLocale()),
        this.expectedAttributeTypeDefinition.getDisplayLabel(Session.getCurrentLocale()),
        this.givenAttributeTypeDefinition.getDisplayLabel(Session.getCurrentLocale()));
  }

}
