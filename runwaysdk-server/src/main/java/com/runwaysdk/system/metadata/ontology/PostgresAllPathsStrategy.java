package com.runwaysdk.system.metadata.ontology;

import java.util.List;

import com.runwaysdk.business.ontology.Term;

public class PostgresAllPathsStrategy extends PostgresAllPathsStrategyBase
{
  private static final long serialVersionUID = -672024276;
  
  public PostgresAllPathsStrategy()
  {
    super();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#initialize()
   */
  @Override
  public void initialize()
  {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#shutdown()
   */
  @Override
  public void shutdown()
  {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#copyTerm(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public void copyTerm(Term parent, Term child, String relationshipType)
  {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#isLeaf(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public boolean isLeaf(Term term, String relationshipType)
  {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllAncestors(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getAllAncestors(Term term, String relationshipType)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getAllDescendants(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getAllDescendants(Term term, String relationshipType)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectAncestors(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getDirectAncestors(Term term, String relationshipType)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.system.metadata.ontology.OntologyStrategy#getDirectDescendants(com.runwaysdk.business.ontology.Term, java.lang.String)
   */
  @Override
  public List<Term> getDirectDescendants(Term term, String relationshipType)
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
