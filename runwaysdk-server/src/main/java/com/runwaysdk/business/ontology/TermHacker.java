package com.runwaysdk.business.ontology;

public class TermHacker
{
  public static OntologyStrategyIF getStrategy(Term term)
  {
    return term.getStrategyWithInstance();
  }
}
