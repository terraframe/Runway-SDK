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
package com.runwaysdk.business.ontology;

import java.lang.reflect.Method;
import java.util.Iterator;

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
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.ontology.OntologyStrategy;
import com.runwaysdk.system.metadata.ontology.StrategyState;
import com.runwaysdk.system.ontology.ImmutableRootException;
import com.runwaysdk.system.ontology.TermUtil;

abstract public class Term extends Business
{
  public static final String CLASS            = "com.runwaysdk.business.ontology.Term";

  private static final long  serialVersionUID = -2009350279143212154L;
  
  public static final String ROOT_KEY             = "ROOT";

  public Term()
  {
    super();
  }

  public static Term get(String id)
  {
    return (Term) Business.get(id);
  }

  public static Term get(String definesType, String key)
  {
    return (Term) Business.get(definesType, key);
  }
  
  @Override
  public String toString()
  {
    String template = "%s [%s : %s]";
    Object[] args = new Object[] { this.getDisplayLabel().getValue(), this.getClassDisplayLabel(), this.buildKey() };
    return String.format(template, args);
  }
  
  @Override
  @Transaction
  public void apply()
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    super.apply();
  }
  
  @Override
  public void delete() {
    delete(true);
  }
  @Transaction
  public void delete(boolean deleteChildren)
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot delete the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    String[] rels = TermUtil.getAllChildRelationships(this.getId());
    for (int i = 0; i < rels.length; ++i) {
      // 1. Delete this entity from the all paths strategy
      this.removeTerm(rels[i]);
      
      // 2. Recursively delete all children.
      if (deleteChildren && !this.isLeaf(rels[i]))
      {
        Iterator<Term> children = this.getDirectDescendants(rels[i]).iterator();

        while (children.hasNext())
        {
          Term child = children.next();

          boolean hasSingleParent = false;
          OIterator<Term> it = child.getDirectAncestors(rels[i]);
          if (it.hasNext()) {
            it.next();
            hasSingleParent = !it.hasNext();
          }
          
          if (hasSingleParent)
          {
            children.remove();
            child.delete();
          }
        }
      }
    }
    
    // 3. Delete this.
    super.delete();
  }
  
  /**
   * getRoot method to be used in Term.java, which allows for overriding in subtypes. We can't do this inside the regular getRoot method
   * because it would cause an infinite loop when the subtypes "super" up to our getRoot method.  
   * 
   * @param termType
   * @return
   */
//  private static Term getRootDelegate(String termType) {
//    Class<?> clazz = LoaderDecorator.load(termType);
//
//    try
//    {
//      Method m = clazz.getMethod("getRoot", new Class<?>[] {});
//      return (Term) m.invoke(null, new Object[] {});
//    }
//    catch (NoSuchMethodException e)
//    {
//      return Term.getRoot(termType);
//    }
//    catch (Exception e)
//    {
//      throw new CoreException(e);
//    }
//  }
  
  /**
   * Returns the root term of the ontology tree defined by this Term. This root term must be created manually, and with the key "ROOT".
   * Alternatively, this static method can also be overridden by the concrete Term subtype and a different root term can be provided.
   * 
   * @param termType The CLASS of the concrete term subtype.
   * @return The root term of the ontology tree defined by this Term.
   */
  public static Term getRoot(String termType)
  {
    return (Term) Business.get(termType, ROOT_KEY);
  }

  /**
   * Returns the ontology strategy associated with this Term type. This static method can be overridden by the concrete Term subtype to
   * return a different ontology strategy. If not overridden, this Term will use the DefaultStrategy.
   * 
   * @return The ontology strategy associated with this Term type.
   */
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

    // 1) Get an instance of the Strategy.
    if (strategyId == null || strategyId.equals(""))
    {
      // Instantiate an instance of the Strategy
      strategy = Term.callCreateStrategy(termType);

      // Ensure correct values of attributes, if its a stateful strategy
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

    // 2) Configure the strategy
    Term.callConfigureStrategy(termType, strategy);
    
    // 3) Create and apply a root node if it doesn't exist already
//    try {
//      getRoot(termType);
//    }
//    catch (DataNotFoundException e) {
//      BusinessDAO dao = BusinessDAO.newInstance(termType);
//      Term root = (Term) Business.get(dao.getId());
//      root.getDisplayLabel().setValue("ROOT");
//      root.setKeyName(ROOT_KEY);
//      root.apply();
//    }

    return strategy;
  }

  /**
   * Terms may override this static method "configureStrategy" to pass in additional (typically transient) instantiation parameters to the strategy.
   * If Term subclasses do not implement this method then the strategy's configure method is invoked with the Term's CLASS as the parameter.
   * The configureStrategy method is invoked with the OntologyStrategyIF instance as the one and only parameter, and it is invoked immediately after
   * the strategy is instantiated (but not initialized).
   * 
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
      strategy.configure(termType);
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

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getDirectAncestors(String relationshipType)
  {
    return getStrategyWithInstance().getDirectAncestors(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getDirectDescendants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getDirectDescendants(String relationshipType)
  {
    return getStrategyWithInstance().getDirectDescendants(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllAncestors(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getAllAncestors(String relationshipType)
  {
    return getStrategyWithInstance().getAllAncestors(this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#getAllDescendants(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public OIterator<Term> getAllDescendants(String relationshipType)
  {
    return getStrategyWithInstance().getAllDescendants(this, relationshipType);
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
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#addLink(com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.Term,
   *      com.runwaysdk.business.ontology.TermRelationship)
   */
  public Relationship addLink(Term parent, String relationshipType)
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    return getStrategyWithInstance().addLink(parent, this, relationshipType);
  }

  /**
   * @see com.runwaysdk.business.ontology.OntologyStrategyIF#removeTerm(com.runwaysdk.business.ontology.Term,
   *      java.lang.String)
   */
  public void removeTerm(String relationshipType)
  {
    getStrategyWithInstance().removeTerm(this, relationshipType);
  }

  /**
   * {@link com.runwaysdk.business.ontology.OntologyStrategyIF#removeLink(com.runwaysdk.business.ontology.Term, com.runwaysdk.business.ontology.Term, java.lang.String)
   * See OntologyStrategyIF}
   */
  public void removeLink(Term parent, String relationshipType)
  {
    if (this.getKey().equals(ROOT_KEY))
    {
      ImmutableRootException exception = new ImmutableRootException("Cannot modify the root Term.");
      exception.setRootName(this.getDisplayLabel().getValue());
      exception.apply();
      
      throw exception;
    }
    
    getStrategyWithInstance().removeLink(parent, this, relationshipType);
  }

  // public Relationship addAndRemoveLink(Term oldParent, String oldRel, Term
  // newParent, String newRel)
  // {
  // OntologyStrategyIF strat = getStrategyWithInstance();
  // strat.removeLink(oldParent, this, oldRel);
  // return strat.addLink(newParent, this, newRel);
  // }

  /**
   * A DisplayLabel attribute is automatically generated for every term subtype. This method will be overridden by your Term subtype.
   * 
   * @return The display label of the term.
   */
  abstract public LocalStruct getDisplayLabel();

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
