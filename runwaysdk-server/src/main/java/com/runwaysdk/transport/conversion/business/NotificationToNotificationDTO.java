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
package com.runwaysdk.transport.conversion.business;

import com.runwaysdk.business.Notification;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public abstract class NotificationToNotificationDTO extends LocalizableToLocalizableDTO
{

  /**
   * Constructor to use when the Session parameter is to be converted into a
   * DTO. 
   * 
   * @param sessionId
   * @param notification
   * @param convertMetaData
   */
  public NotificationToNotificationDTO(String sessionId, Notification notification, boolean convertMetaData)
  {
    super(sessionId, notification, convertMetaData);
  }  

  /**
   * Returns true.
   * @return true.
   */
  protected boolean checkReadAccess()
  {
    return true;
  }  

  /**
   * Returns true.
   * @return true.
   */
  protected boolean checkWriteAccess()
  {
    return true;
  }

  /**
   * Returns true.
   * 
   * @param sessionId
   * @param mdAttribute
   *            The MdAttributeIF object to check for write permission on.
   * @return true.
   */
  protected boolean hasAttributeReadAccess(MdAttributeDAOIF mdAttribute)
  {
    return true;
  }
  
  /**
   * Returns true.
   * 
   * @param mdAttribute
   *            The MdAttributeIF object to check for write permission on.
   * @return true.
   */
  protected boolean hasAttributeWriteAccess(MdAttributeDAOIF mdAttribute)
  {
    return true;
  }
  
}
