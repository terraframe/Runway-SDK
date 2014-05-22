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
package com.runwaysdk.dataaccess;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Entity;

public class CannotDeleteReferencedObject extends DataAccessException
{
  /**
   * 
   */
  private static final long serialVersionUID = 5790791103109311135L;

  private EntityDAO entityDAO;
 
  private MdEntityDAOIF refMdEntityIF;
  
  private MdAttributeReferenceDAOIF refMdAttributeReferenceIF;
  
  /**
   * Constructs a new CannotDeleteReferencedObject with the specified
   * developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param entityDAO
   * @param refMdEntityIF
   * @param mdAttributeReferenceIF
   */
  public CannotDeleteReferencedObject(String devMessage, EntityDAO entityDAO, MdEntityDAOIF refMdEntityIF, MdAttributeReferenceDAOIF mdAttributeReferenceIF)
  {
    super(devMessage);
    this.entityDAO = entityDAO;
    this.refMdEntityIF = refMdEntityIF;
    this.refMdAttributeReferenceIF = mdAttributeReferenceIF;
  }

  /**
   * Constructs a new CannotDeleteReferencedObject with the specified
   * developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this CannotBeDeletedException's detail
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
   * @param entityDAO
   * @param refMdEntityIF
   * @param mdAttributeReferenceIF
   */
  public CannotDeleteReferencedObject(String devMessage, Throwable cause, EntityDAO entityDAO, MdEntityDAOIF refMdEntityIF, MdAttributeReferenceDAOIF mdAttributeReferenceIF)
  {
    super(devMessage, cause);
    this.entityDAO = entityDAO;
    this.refMdEntityIF = refMdEntityIF;
    this.refMdAttributeReferenceIF = mdAttributeReferenceIF;
  }

  /**
   * Constructs a new CannotDeleteReferencedObject with the specified cause
   * and a developer message taken from the cause. This constructor is useful if
   * the CannotBeDeletedException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param entityDAO
   * @param refMdEntityIF
   * @param mdAttributeReferenceIF
   */
  public CannotDeleteReferencedObject(Throwable cause, EntityDAO entityDAO, MdEntityDAOIF refMdEntityIF, MdAttributeReferenceDAOIF mdAttributeReferenceIF)
  {
    super(cause);
    this.refMdEntityIF = refMdEntityIF;
    this.refMdAttributeReferenceIF = mdAttributeReferenceIF;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  { 
    Entity entity =  BusinessFacade.get(this.entityDAO);
    return ServerExceptionMessageLocalizer.cannotDeleteReferencedObject(this.getLocale(), entity, this.refMdEntityIF, this.refMdAttributeReferenceIF);
  }
}
