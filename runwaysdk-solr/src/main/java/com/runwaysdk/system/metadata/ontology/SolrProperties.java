package com.runwaysdk.system.metadata.ontology;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationReaderIF;
import com.runwaysdk.generation.loader.Reloadable;

public class SolrProperties implements Reloadable
{
  private static class Singleton implements Reloadable
  {
    private static SolrProperties INSTANCE = new SolrProperties();

    private static SolrProperties getInstance()
    {
      // INSTANCE will only ever be null if there is a problem. The if check is
      // to allow for debugging.
      if (INSTANCE == null)
      {
        INSTANCE = new SolrProperties();
      }

      return INSTANCE;
    }

    private static ConfigurationReaderIF getProps()
    {
      return getInstance().props;
    }
  }

  /**
   * The server.properties configuration file
   */
  private ConfigurationReaderIF props;

  private SolrProperties()
  {
    this.props = ConfigurationManager.getReader(ConfigGroup.SERVER, "solr.properties");
  }

  public static String getUrl()
  {
    return Singleton.getProps().getString("solr.url");
  }
}
