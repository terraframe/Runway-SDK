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

/**
*
*/
package com.runwaysdk;

import org.apache.juli.logging.LogFactory;

import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.logging.Log;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.session.Request;





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
public class Sandbox
{
  @Request
  @Transaction
  public static void main(String[] args) throws Exception
  {
//    BasicConfigurator.configure();
//
//    Database.enableLoggingDMLAndDDLstatements(true);
//    
//    // migration script (SQL) to allow MdAttributeReference to reference MdEntity instead of just MdBusiness.
//    MdAttributeReferenceDAO ref = (MdAttributeReferenceDAO) MdAttributeReferenceDAO.getByKey("com.runwaysdk.system.metadata.MdAttributeReference.mdBusiness").getBusinessDAO();
////    ref.setValue(MdAttributeReferenceInfo.IMMUTABLE, "false");
////    ref.apply();
//    
////    ref.setValue(MdAttributeReferenceInfo.KEY, "com.runwaysdk.system.metadata.MdAttributeReference.mdEntity");
////    ref.setColumnName("md_entity");
//    ref.setValue(MdAttributeReferenceInfo.NAME, "mdEntity");
//    ref.setStructValue(MdAttributeReference.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdEntity");
//    ref.apply();
  }
}
