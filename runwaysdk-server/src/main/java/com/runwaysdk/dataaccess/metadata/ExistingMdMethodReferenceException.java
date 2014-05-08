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
package com.runwaysdk.dataaccess.metadata;

import java.util.List;

import com.runwaysdk.ServerExceptionMessageLocalizer;

public class ExistingMdMethodReferenceException extends MetadataException
{
  /**
   * 
   */
  private static final long serialVersionUID = -42532143241342421L;

  private MdEntityDAO mdEntity;
  
  private List<ParameterMarker> markers;
  
  public ExistingMdMethodReferenceException(String devMessage, MdEntityDAO mdEntity, List<ParameterMarker> markers)
  {
    super(devMessage);
    
    this.mdEntity = mdEntity;
    this.markers = markers;
  }
  
  public ExistingMdMethodReferenceException(Throwable cause, MdEntityDAO mdEntity, List<ParameterMarker> markers)
  {
    super(cause);
    
    this.mdEntity = mdEntity;
    this.markers = markers;
  }
  
  public ExistingMdMethodReferenceException(String devMessage, Throwable cause, MdEntityDAO mdEntity, List<ParameterMarker> markers)
  {
    super(devMessage, cause);
    
    this.mdEntity = mdEntity;
    this.markers = markers;    
  }
  
  @Override
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.existingMdMethodReferenceException(this.getLocale(), this.mdEntity, this.markers);
  }

}
