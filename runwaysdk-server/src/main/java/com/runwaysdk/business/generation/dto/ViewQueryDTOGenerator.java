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
package com.runwaysdk.business.generation.dto;

import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;


public class ViewQueryDTOGenerator extends ComponentQueryDTOGenerator
{
  /**
   * 
   * @param mdViewIF
   */
  public ViewQueryDTOGenerator(MdViewDAOIF mdViewIF)
  {   
    super(mdViewIF);
  }

  /**
   * Returns the reference to the MdViewIF object that defines the entity type
   * for which this object generates a query API object for.
   * @return reference to the MdViewIF object that defines the entity type
   * for which this object generates a query API object for.
   */
  protected MdViewDAOIF getMdClassIF()
  {
    return (MdViewDAOIF)super.getMdClassIF();
  }

  @Override
  protected void addExtends(MdClassDAOIF parentMdClassIF)
  {
    if (parentMdClassIF == null)
    {
      writeLine(srcBuffer, ViewQueryDTO.class.getName());
    }
    else
    {
      writeLine(srcBuffer, ComponentQueryDTOGenerator.getQueryClass(parentMdClassIF));
    }
  }
}
