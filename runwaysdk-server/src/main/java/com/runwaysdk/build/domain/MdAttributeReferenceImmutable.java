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
package com.runwaysdk.build.domain;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;

public class MdAttributeReferenceImmutable
{
  public static void main(String[] args)
  {
    doIt();
  }
  
  public static void doIt()
  {
    ServerProperties.setIgnoreSiteMaster(true);
    
    MdBusinessDAO mdbiz = (MdBusinessDAO) MdBusinessDAO.getMdBusinessDAO(MdAttributeReference.CLASS);
    
    MdAttributeConcreteDAO ref = (MdAttributeConcreteDAO) mdbiz.getDefinedMdAttributeMap().get("mdbusiness");
    
    ref.getAttribute(MdAttributeConcreteInfo.IMMUTABLE).setValue(false);
    
    ref.apply();
    
    ServerProperties.setIgnoreSiteMaster(false);
  }
}