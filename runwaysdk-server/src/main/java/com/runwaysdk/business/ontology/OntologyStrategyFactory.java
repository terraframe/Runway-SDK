package com.runwaysdk.business.ontology;

import java.util.HashMap;
import java.util.Map;

public class OntologyStrategyFactory
{
  private static Map<String, OntologyStrategyBuilderIF> builders = new HashMap<>();

  public static synchronized OntologyStrategyIF get(String key, OntologyStrategyBuilderIF defaultBuilder)
  {
    if (!builders.containsKey(key))
    {
      builders.put(key, defaultBuilder);
    }

    return builders.get(key).build();
  }

  public static synchronized void set(String key, OntologyStrategyBuilderIF builder)
  {
    builders.put(key, builder);
  }
}
