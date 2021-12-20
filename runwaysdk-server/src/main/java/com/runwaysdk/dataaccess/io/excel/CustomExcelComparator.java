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
package com.runwaysdk.dataaccess.io.excel;

import java.util.Comparator;
import java.util.List;

import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class CustomExcelComparator implements Comparator<MdAttributeDAOIF>
{
  private List<String> attributes;
  
  public CustomExcelComparator(List<String> attributes)
  {
    this.attributes = attributes;
  }
  
  public int compare(MdAttributeDAOIF o1, MdAttributeDAOIF o2)
  {
    int i1 = attributes.indexOf(o1.definesAttribute());
    int i2 = attributes.indexOf(o2.definesAttribute());
    
    if (i1==-1)
    {
      i1 = Integer.MAX_VALUE;
    }
    
    if (i2==-1)
    {
      i2 = Integer.MAX_VALUE;
    }
    
    return i1-i2;
  }
}
