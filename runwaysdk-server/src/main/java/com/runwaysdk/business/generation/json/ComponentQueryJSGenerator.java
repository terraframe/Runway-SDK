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
package com.runwaysdk.business.generation.json;

import java.util.List;

import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdTypeDAOIF;

public abstract class ComponentQueryJSGenerator extends JSGenerator
{
  public ComponentQueryJSGenerator(String sessionId, MdTypeDAOIF mdTypeIF)
  {
    super(sessionId, mdTypeIF);
  }
  
  @Override
  protected String getClassName()
  {
    return getMdTypeIF().definesType()+TypeGeneratorInfo.QUERY_DTO_SUFFIX;
  }
  
  @Override
  protected List<Declaration> getConstants()
  {
    return null;
  }

  @Override
  protected List<Declaration> getInstanceMethods()
  {
    return null;
  }

  @Override
  protected List<Declaration> getStaticMethods()
  {
    return null;
  }
  
  @Override
  protected boolean isAbstract()
  {
    // Although the underlying type might be abstract, we want the query objects
    // to be instantiated to query on an abstract type and its subclasses.
    return false;
  }
}
