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
package com.runwaysdk.dataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Entity;

/**
 * Thrown when chnages are made to a stale (out of date) entity.
 * 
 * @author Eric Grunzke
 */
public class StaleEntityException extends DataAccessException
{
  public static final Logger logger = LoggerFactory.getLogger(StaleEntityException.class);
  
  /**
   * Generated by Eclipse
   */
  private static final long serialVersionUID = 6490314123466904003L;

  /**
   * Entity object that is stale.
   */
  private ComponentDAO      entityDAO;

  /**
   * Constructs a new StaleEntityException with the specified developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param entityDAO
   *          Entity object that is stale.
   */
  public StaleEntityException(String devMessage, ComponentDAO entityDAO)
  {
    super(devMessage);
    this.entityDAO = entityDAO;
  }

  /**
   * Constructs a new StaleEntityException with the specified developer message
   * and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this StaleEntityException's detail
   * message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   * @param entityDAO
   *          Entity object that is stale.
   */
  public StaleEntityException(String devMessage, Throwable cause, ComponentDAO entityDAO)
  {
    super(devMessage, cause);
    this.entityDAO = entityDAO;
  }

  /**
   * Constructs a new StaleEntityException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * StaleEntityException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   */
  public StaleEntityException(Throwable cause, ComponentDAO entityDAO)
  {
    super(cause);
    this.entityDAO = entityDAO;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   * @param entity
   *          The stale entity
   */
  public String getLocalizedMessage()
  {
    try
    {
      if (entityDAO instanceof EntityDAOIF)
      {
        Entity entity = BusinessFacade.get((EntityDAOIF) this.entityDAO);
  
        return ServerExceptionMessageLocalizer.staleEntityException(this.getLocale(), entity);
      }
  
      String label = this.entityDAO.getMdClassDAO().getDisplayLabel(this.getLocale());
  
      return ServerExceptionMessageLocalizer.staleEntityException(this.getLocale(), label);
    }
    catch (Throwable t)
    {
      logger.error("Could not get localized message for StaleEntityException. Falling back to non-localized", t);
      
      // We can't do ANYTHING which could hit the database here. So it might be hard to tell the end user what the actual entity is.
      return ServerExceptionMessageLocalizer.staleEntityException(null, this.entityDAO.getClass().getName());
    }
  }
}
