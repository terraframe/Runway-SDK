/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.runwaysdk.ontology.strategy.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import com.runwaysdk.business.ontology.OntologyStrategyIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.database.general.DatabaseServiceIF;
import com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy;

public class OntologyDatabaseFactory
{

  /**
   * @param database
   * @param strategy
   */
  public OntologyDatabase getInstance(DatabaseServiceIF database, OntologyStrategyIF strategy)
  {
    if (strategy instanceof DatabaseAllPathsStrategy)
    {
      List<OntologyDatabase> configurations = new ArrayList<OntologyDatabase>();

      ServiceLoader<OntologyDatabase> loader = ServiceLoader.load(OntologyDatabase.class, Thread.currentThread().getContextClassLoader());

      try
      {
        Iterator<OntologyDatabase> it = loader.iterator();

        while (it.hasNext())
        {
          configurations.add(it.next());
        }

      }
      catch (ServiceConfigurationError serviceError)
      {
        throw new RuntimeException(serviceError);
      }

      if (configurations.size() == 0)
      {
        throw new ProgrammingErrorException("Database implementation doesn't exist for the database [" + database.getClass().getName() + "] and strategy [" + strategy.getClass().getName() + "]");
      }

      return configurations.get(0);
    }

    throw new ProgrammingErrorException("Database implementation doesn't exist for the database [" + database.getClass().getName() + "] and strategy [" + strategy.getClass().getName() + "]");
  }
}
