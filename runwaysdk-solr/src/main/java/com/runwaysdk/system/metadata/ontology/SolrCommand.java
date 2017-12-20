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
