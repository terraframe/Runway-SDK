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
package com.runwaysdk.transport.metadata;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.session.Session;

public class ServerAttributeReferenceMdBuilder extends ServerAttributeMdBuilder
{

  /**
   * Constructor to set the source MdAttributeIF and the destination AttributeMdDTO
   * @param source
   * @param dest
   */
  protected ServerAttributeReferenceMdBuilder(MdAttributeDAOIF source, AttributeReferenceMdDTO dest)
  {
    super(source, dest);
  }
  
  /**
   * Sets the MdBusiness type that the attribute referenc points to.
   */
  protected void build()
  {
    super.build();
    
    MdAttributeReferenceDAOIF sourceSafe = (MdAttributeReferenceDAOIF) source.getMdAttributeConcrete();
    AttributeReferenceMdDTO destSafe = (AttributeReferenceMdDTO) dest;
    
    MdBusinessDAOIF referenced = sourceSafe.getReferenceMdBusinessDAO();
    if(referenced != null)
    {
      destSafe.setReferencedMdBusiness(referenced.definesType());
      destSafe.setReferencedDisplayLabel(referenced.getDisplayLabel(Session.getCurrentLocale()));
    }
  }

}
