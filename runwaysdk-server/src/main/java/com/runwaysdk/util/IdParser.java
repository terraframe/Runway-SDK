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
package com.runwaysdk.util;

import com.runwaysdk.constants.DatabaseInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.InvalidIdException;

public class IdParser
{

  /**
   * Returns the rootId portion of an id string (rootId-type).
   * 
   * <br/>Precondition:<br/> parameter contains the delimiter character.
   * 
   * @param id
   * @return rootId portion of an id string (rootId-type).
   */
  public static String parseRootFromId(String id)
  {
    try
    {
      return id.substring(0, new Integer(DatabaseInfo.ROOT_ID_SIZE).intValue());
    }
    catch (StringIndexOutOfBoundsException e)
    {
      String error = "RootId could not be parsed from the id.  Most likely an id or a type is being set where a rootId is required ["+id+"].";
      throw new InvalidIdException(error, e, id);
    }
  }
  
  /**
   * Returns the rootid of the defining type from the id string (rootId-typeRootId).
   *
   *
   * @param id
   * @return rootid of the defining type from the id string (rootId-typeRootId).
   */
  public static String parseMdTypeRootIdFromId(String id)
  {
    try
    {
      return id.substring(new Integer(DatabaseInfo.ROOT_ID_SIZE), id.length());
    }
    catch (StringIndexOutOfBoundsException e)
    {
      String error = MdTypeInfo.CLASS+" id could not be parsed from the id ["+id+"].";
      
      throw new InvalidIdException(error, e, id);
    }
  }
  
  /**
   * Returns a id string built from the id root and the id of the defining class.
   * @param rootId of an object.
   * @param mdClassId id of the class that defines this obuect.
   * @return id string built from the id root and the id of the defining class.
   */
  public static String buildId(String rootId, String mdClassId)
  {
    return rootId+parseRootFromId(mdClassId);
  }

  public static boolean validId(String id)
  {
    return (id != null && id.length() == 64);
  }

}
