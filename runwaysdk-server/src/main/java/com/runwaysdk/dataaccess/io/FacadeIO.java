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
/**
 * Created on Aug 7, 2005
 *
 */
package com.runwaysdk.dataaccess.io;

import java.util.List;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;


/**
 * @author nathan
 *
 */
public class FacadeIO
{
  /**
   * 
   * @param exportFile
   */
  public static void exportAll(String schemaFile, String exportFile)
  { 
    XMLExporter xmlExporter = new XMLExporter(schemaFile);

    List<MdEntityDAOIF> rootEntityList = MdEntityDAO.getRootEntities();
    
    for(MdEntityDAOIF mdEntityIF : rootEntityList)
    {      
      QueryFactory queryFactory = new QueryFactory();
      EntityQuery entityQuery = queryFactory.entityQueryDAO(mdEntityIF);

      OIterator<? extends ComponentIF> componentIterator = entityQuery.getIterator();
      while (componentIterator.hasNext())
      {
        xmlExporter.add(componentIterator.next());
      }
    }
  
    xmlExporter.writeToFile(exportFile);
  }
}
