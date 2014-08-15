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
package com.runwaysdk.dataaccess.transaction;

import com.runwaysdk.dataaccess.EntityDAO;

public aspect ImportProperty percflow(importingRecords())
{
  protected pointcut importingRecords()
   : execution (* com.runwaysdk.dataaccess.transaction.TransactionRecordSAXImporter+.runImport(..));
  
  protected pointcut isImport(EntityDAO entityDAO)
    : call (* com.runwaysdk.dataaccess.EntityDAO.isImport()) && target(entityDAO);
 
  /**
   * Return true if this is being called within the context of an import
   *
   * @return true if this is being called within the context of an import
   */
  boolean around(EntityDAO entityDAO) : isImport(entityDAO)
  {
    if (entityDAO instanceof LocalTransactionArtifact)
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  
}
