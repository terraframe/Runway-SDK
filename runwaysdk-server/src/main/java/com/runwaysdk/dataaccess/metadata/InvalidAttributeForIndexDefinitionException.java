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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;

public class InvalidAttributeForIndexDefinitionException extends InvalidDefinitionException
{ 
  /**
   * 
   */
  private static final long serialVersionUID = -5635510496019237661L;

  /**
   * Metadata defining the index.
   */
  @SuppressWarnings("unused")
  private MdIndexDAOIF     mdIndexIF;
  
  /**
   * Metadata defining the type that the invalid index is on.
   */
  private MdEntityDAOIF     mdEntityIF;
  
  /**
   * Metadata defining the attribute that is not defined by the entity.
   */
  private MdAttributeConcreteDAOIF  mdAttributeIF;
  
  /**
   * Thrown when an attribute is specified to be on an index, yet the attribute is not defined
   * by the type that defines the table the index is on.
   * Constructs a new AttributeDefinitionException with the specified developer
   * message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdIndexIF;
   *          Metadata defining the index.
   * @param mdEntityIF
   *          Metadata defining the type that the invalid index is on.
   * @param mdAttributeIF
   *          Metadata defining the attribute that is not defined by the entity.
   */
  public InvalidAttributeForIndexDefinitionException(String devMessage, 
      MdIndexDAOIF mdIndexIF, MdEntityDAOIF mdEntityIF, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(devMessage);
    this.mdIndexIF     = mdIndexIF;
    this.mdEntityIF    = mdEntityIF;
    this.mdAttributeIF = mdAttributeIF;
  }

  /**
   * Thrown when an attribute is specified to be on an index, yet the attribute is not defined
   * by the type that defines the table the index is on.
   * Constructs a new AttributeDefinitionException with the specified developer
   * message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this AttributeDefinitionException's detail
   * message.
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
   * @param mdIndexIF;
   *          Metadata defining the index.
   * @param mdEntityIF
   *          Metadata defining the type that the invalid index is on.
   * @param mdAttributeIF
   *          Metadata defining the attribute that is not defined by the entity.
   */
  public InvalidAttributeForIndexDefinitionException(String devMessage, Throwable cause, 
      MdIndexDAOIF mdIndexIF, MdEntityDAOIF mdEntityIF, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(devMessage, cause);
    this.mdIndexIF     = mdIndexIF;
    this.mdEntityIF    = mdEntityIF;
    this.mdAttributeIF = mdAttributeIF;
  }

  /**
   * Thrown when an attribute is specified to be on an index, yet the attribute is not defined
   * by the type that defines the table the index is on.
   * Constructs a new AttributeDefinitionException with the specified cause and
   * a developer message taken from the cause. This constructor is useful if the
   * AttributeDefinitionException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param mdIndexIF;
   *          Metadata defining the index.
   * @param mdEntityIF
   *          Metadata defining the type that the invalid index is on.
   * @param mdAttributeIF
   *          Metadata defining the attribute that is not defined by the enti
   */
  public InvalidAttributeForIndexDefinitionException(Throwable cause, 
      MdIndexDAOIF mdIndexIF, MdElementDAOIF mdEntityIF, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    super(cause);
    this.mdIndexIF     = mdIndexIF;
    this.mdEntityIF    = mdEntityIF;
    this.mdAttributeIF = mdAttributeIF;
  }
  
  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {    
    String definesType = this.mdEntityIF.definesType();
    String attributeDisplayLabel = this.mdAttributeIF.getDisplayLabel(this.getLocale());
    
    return ServerExceptionMessageLocalizer.invalidAttributeForIndexDefinitionException(this.getLocale(), definesType, attributeDisplayLabel);
  }
}
