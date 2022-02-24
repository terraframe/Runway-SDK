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
package com.runwaysdk.system.metadata.ontology;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class SolrCommand implements Command
{
  private HttpSolrClient client;

  public SolrCommand(HttpSolrClient client)
  {
    this.client = client;
  }

  public HttpSolrClient getClient()
  {
    return client;
  }

  @Override
  public void doIt()
  {
    try
    {
      this.client.commit();
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void undoIt()
  {
    try
    {
      this.client.rollback();
    }
    catch (SolrServerException | IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public void doFinally()
  {
    try
    {
      this.client.close();
    }
    catch (IOException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  @Override
  public String doItString()
  {
    return "Commits to solr";
  }

  @Override
  public String undoItString()
  {
    return "Rollsback to solr";
  }

  @Override
  public boolean isUndoable()
  {
    return false;
  }

}
