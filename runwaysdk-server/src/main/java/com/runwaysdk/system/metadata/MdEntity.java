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
package com.runwaysdk.system.metadata;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.DeleteTypePermissionException;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;

public abstract class MdEntity extends MdEntityBase
{
  private static final long serialVersionUID = 1229405886704L;

  public MdEntity()
  {
    super();
  }

  public static MdEntity getMdEntity(String type)
  {
    return (MdEntity) BusinessFacade.get(MdEntityDAO.getMdEntityDAO(type));
  }

  protected MdEntityDAOIF getMdTypeDAO()
  {
    return (MdEntityDAOIF)BusinessFacade.getEntityDAO(this);
  }

  @Transaction
  public void deleteAllTableRecords()
  {
    SessionIF currentSession = Session.getCurrentSession();

    if (currentSession != null)
    {
      MdEntityDAOIF mdEntityDAOIF = this.getMdTypeDAO();

      boolean access = PermissionFacade.checkTypeReadAccess(currentSession.getId(), mdEntityDAOIF);

      if (!access)
      {
        String errorMsg = "Insufficient permissions to delete instances of type [" + mdEntityDAOIF.definesType() + "]";
        throw new DeleteTypePermissionException(errorMsg, mdEntityDAOIF);
      }
    }

    Database.deleteAllTableRecords(this.getTableName());
  }

}
