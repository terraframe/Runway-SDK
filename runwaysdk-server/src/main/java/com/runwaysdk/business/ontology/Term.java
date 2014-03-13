/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.business.ontology;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.ontology.OntologyStrategy;
import com.runwaysdk.system.metadata.ontology.StrategyState;

abstract public class Term extends Business
{
  private static final long serialVersionUID = -2009350279143212154L;
  
  public Term()
  {
    super();
  }

  public static Term getRootNode(String termType)
  {
    Class<?> clazz = LoaderDecorator.load(termType);

    Term root;

    try
    {
      Method m = clazz.getMethod("getRootNode", new Class<?>[] {});
      root = (Term) m.invoke(null, new Object[] {});
    }
    catch (NoSuchMethodException e)
    {
      throw new UnsupportedOperationException("The concrete Term type [" + termType + "] does not define a getRootNode method.");
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }

    return root;
  }

  protected static OntologyStrategyIF createStrategy()
  {
    return DefaultStrategy.Singleton.INSTANCE;
  }

  /**
   * Assigns a strategy to the MdTerm specified by termType. This method is
   * called by a static initializer block in TermBaseGenerator.
   * 
   * @param termType
   */
  @Request
  protected static OntologyStrategyIF assignStrategy(String termType)
  {
    return assignStrategyTrasaction(termType);
  }

  @Transaction
  private static OntologyStrategyIF assignStrategyTrasaction(String termType)
  {
    MdTermDAOIF mdTermDAOIF = MdTermDAO.getMdTermDAO(termType);

    String strategyId = mdTermDAOIF.getValue(MdTermInfo.STRATEGY);

    OntologyStrategyIF strategy = null;

    if (strategyId == null || strategyId.equals(""))
    {
      strategy = Term.callCreateStrategy(termType);

      if (strategy instanceof OntologyStrategy)
      {
        OntologyStrategy statefulStrat = (OntologyStrategy) strategy;

        if (statefulStrat.isNew())
        {
          statefulStrat.addStrategyState(StrategyState.UNINITIALIZED);
          statefulStrat.apply();
        }

        MdTermDAO mdTermDAO = MdTermDAO.get(mdTermDAOIF.getId()).getBusinessDAO();
        mdTermDAO.setValue(MdTermInfo.STRATEGY, statefulStrat.getId());
        mdTermDAO.apply();
      }
    }
    else
    {
      strategy = OntologyStrategy.get(strategyId);
    }

    Term.callConfigureStrategy(termType, strategy);

    return strategy;
  }

  /**
   * @param strategy
   */
  private static void callConfigureStrategy(String termType, OntologyStrategyIF strategy)
  {
    Class<?> clazz = LoaderDecorator.load(termType);

    try
    {
      Method m = clazz.getMethod("configureStrategy", OntologyStrategyIF.class);
      m.invoke(null, strategy);
    }
    catch (NoSuchMethodException e)
    {
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }
  }

  /**
   * Invokes the static createStrategy method on the generated Term type if it
   * exists, otherwise invokes the default Term.createStrategy. This allows for
   * users to override the strategy with a static method.
   * 
   * @param termType
   * @return
   */
  private static OntologyStrategyIF callCreateStrategy(String termType)
  {
    Class<?> clazz = LoaderDecorator.load(termType);

    OntologyStrategyIF strat;

    try
    {
      Method m = clazz.getMethod("createStrategy", new Class<?>[] {});
      strat = (OntologyStrategyIF) m.invoke(null, new Object[] {});
    }
    catch (NoSuchMethodException e)
    {
      strat = Term.createStrategy();
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }

    return strat;
  }

  /**
   * This method is delegated to by a generated getStrategy() method in
   * TermBaseGenerator.
   * 
   * @param termType
   * @return
   */
  protected static OntologyStrategyIF getStrategy(String termType)
  {
    Class<?> clazz = LoaderDecorator.load(termType);

    OntologyStrategyIF strat;

    try
    {
      Method m = clazz.getMethod("getStrategy", new Class<?>[] {});
      strat = (OntologyStrategyIF) m.invoke(null, new Object[] {});
    }
    catch (Exception e)
    {
      throw new CoreException(e);
    }

    return strat;
  }

  /**
   * This method is a convenience and only used with instance methods in this
   * class.
   * 
   * @return
   */
  protected OntologyStrategyIF getStrategyWithInstance()
  {
    return Term.getStrategy(this.getMdClass().definesType());
  }

  /**
   * Returns a <code>MdTermDAOIF</code> that defines this Component's class.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a <code>MdTermDAOIF</code> that defines this Component's class.
   */
  public MdTermDAOIF getMdTerm()
  {
    return (MdTermDAOIF) MdClassDAO.getMdClassDAO(this.getType());
  }

  /**
   * Returns the unique id of this term.
   * 
   * @return
   */
  public String getId()
  {
    return "";
  }

  // public boolean isDirectAncestorOf(Term child);
  // public boolean isRecursiveAncestorOf(Term child);
  //
  // public boolean isDirectDescendentOf(Term parent);
  // public boolean isRecursiveDescendentOf(Term parent);

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getDirectAncestors(String relationshipType)
  {
    return getStrategyWithInstance().getDirectAncestors(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescendants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getDirectDescendants(String relationshipType)
  {
    return getStrategyWithInstance().getDirectDescendants(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getAllAncestors(String relationshipType)
  {
    return getStrategyWithInstance().getAllAncestors(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public List<Term> getAllDescendants(String relationshipType)
  {
    return getStrategyWithInstance().getAllDescendants(this, relationshipType);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term)
   */
  public List<TermAndRel> getAllDescendants()
  {
    return getStrategyWithInstance().getAllDescendants(this);
  }
  
  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term)
   */
  public List<TermAndRel> getDirectDescendants()
  {
    return getStrategyWithInstance().getDirectDescendants(this);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#isLeaf(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public boolean isLeaf(String relationshipType)
  {
    return getStrategyWithInstance().isLeaf(this, relationshipType);
  }

  /**
   * Performs a deep copy of this term to the specified parent.
   * 
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#copyTerm(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public Relationship copyTerm(Term parent, String relationshipType)
  {
    return getStrategyWithInstance().copyTerm(parent, this, relationshipType);
  }

  public void removeTerm(String relationshipType)
  {
    getStrategyWithInstance().removeTerm(this, relationshipType);
  }

  public void removeLink(Term parent, String relationshipType)
  {
    getStrategyWithInstance().removeLink(parent, this, relationshipType);
  }
  
  /**
   * A convenience method, uses reflection to invoke "getDisplayLabel" on the Term.
   * 
   * @return
   */
  public LocalStruct getDisplayLabel() {
    try
    {
      return (LocalStruct) this.getClass().getMethod("getDisplayLabel", new Class<?>[]{}).invoke(this);
    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
  }
  
//  public void apply() {
//    if(isNewInstance())
//    {
//      getRequest().createBusiness(this);
//    }
//    else
//    {
//      getRequest().update(this);
//    }
//  }

  /**
   * Adds the term to the relationship type strategy.
   * 
   * @param relationshipType
   */
  public void addTerm(String relationshipType)
  {
    getStrategyWithInstance().add(this, relationshipType);
  }

}
