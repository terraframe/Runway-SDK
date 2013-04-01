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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;

public class NoAttributeOnIndexException extends InvalidDefinitionException
{
  /**
   * 
   */
  private static final long serialVersionUID = 1743297765514134932L;

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
   * Thrown when an attempt is made to apply an index to the database, yet no attributes are defined on the index.
   * Constructs a new NoAttributeOnIndexException with the specified developer
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
   */
  public NoAttributeOnIndexException(String devMessage, 
      MdIndexDAOIF mdIndexIF, MdEntityDAOIF mdEntityIF)
  {
    super(devMessage);
    this.mdIndexIF     = mdIndexIF;
    this.mdEntityIF    = mdEntityIF;
  }

  /**
   * Thrown when an attempt is made to apply an index to the database, yet no attributes are defined on the index.
   * Constructs a new NoAttributeOnIndexException with the specified developer
   * message.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this NoAttributeOnIndexException's detail
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
   */
  public NoAttributeOnIndexException(String devMessage, Throwable cause, 
      MdIndexDAOIF mdIndexIF, MdEntityDAOIF mdEntityIF)
  {
    super(devMessage, cause);
    this.mdIndexIF     = mdIndexIF;
    this.mdEntityIF    = mdEntityIF;
  }

  /**
   * Thrown when an attempt is made to apply an index to the database, yet no attributes are defined on the index.
   * Constructs a new NoAttributeOnIndexException with the specified developer
   * message.
   * Constructs a new NoAttributeOnIndexException with the specified cause and
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
  public NoAttributeOnIndexException(Throwable cause, 
      MdIndexDAOIF mdIndexIF, MdElementDAOIF mdEntityIF)
  {
    super(cause);
    this.mdIndexIF     = mdIndexIF;
    this.mdEntityIF    = mdEntityIF;
  }
  
  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {    
    String definesType = this.mdEntityIF.definesType();
    
    return ServerExceptionMessageLocalizer.noAttributeOnIndexException(this.getLocale(), definesType);
  }
}
