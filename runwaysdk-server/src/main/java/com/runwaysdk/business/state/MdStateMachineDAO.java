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
package com.runwaysdk.business.state;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.StateGenerator;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTreeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.NameConventionException;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.dataaccess.metadata.ReservedWords;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;



public class MdStateMachineDAO extends MdBusinessDAO implements MdStateMachineDAOIF
{
  /**
   * The suffix automatically added to all Transition relationships
   */
  public static final String TRANS_SUFFIX = "_T";

  /**
   * The suffix automatically added to the abstract Status relationships
   */
  public static final String STATUS_SUFFIX = "_S";

  /**
   * The name of the state package
   */
  public static final String STATE_PACKAGE = "state";

  /**
   * Eclipse generated serial id
   */
  private static final long serialVersionUID = -5318910317567127145L;

  /**
   * Largest package size
   */
  protected static final int PACKAGE_SIZE = 127;

  /**
   * Maximum class size
   */
  protected static final int MAX_CLASS_NAME = 64;

  /**
   * Amount of characters in prefix "state."
   */
  protected static final int STATE_CHARACTER = 6;

  /**
   * The maximum size of the package attribute for MdStateMachine
   */
  protected static final int MAX_PACKAGE = PACKAGE_SIZE - MAX_CLASS_NAME - STATE_CHARACTER;

  /**
   * The maximum size of the name attribute for MdStateMachine
   */
  protected static final int MAX_NAME = MAX_CLASS_NAME - Math.max(TRANS_SUFFIX.length(), STATUS_SUFFIX.length());

  /**
   * Empty constructor, does nothing
   */
  public MdStateMachineDAO()
  {
    super();
  }

  /**
   * @param attributeMap
   * @param classType
   */
  public MdStateMachineDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
  }

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    String signature = super.getSignature()+" StateTransitions[";

    boolean firstIteration = true;
    for (TransitionDAOIF transitionDAOIF: definesTransitions())
    {
      if (!firstIteration)
      {
        signature += ", ";
      }
      else
      {
        firstIteration = false;
      }

      signature += transitionDAOIF.getSignature();
    }

    signature += "]";

    return signature;
  }

  @Override
  public void setValue(String name, String value)
  {
    //Validate the package of the state machine
    if(name.equals(MdTypeInfo.PACKAGE))
    {
      if(value.length() > MAX_PACKAGE)
      {
        String error = "The length of the package [" + value + "] is too long: it must be "
            + MAX_PACKAGE + " characters or less.";

        throw new AttributeLengthCharacterException(error, getAttribute(MdBusinessInfo.PACKAGE), MAX_PACKAGE);
      }
    }

    if(name.equals(MdTypeInfo.NAME))
    {
      if(value.length() > MAX_NAME)
      {
        String error = "The length of the state machine name [" + value + "] is too long: it must be "
            + MAX_NAME + " characters or less.";

        throw new AttributeLengthCharacterException(error, getAttribute(MdBusinessInfo.NAME), MAX_NAME);
      }
    }

    super.setValue(name, value);
  }

  /**
   * Creates a state to the MdState, but does not apply the state in case additional
   * attributes must be supplied
   *
   * @param name The name of the state
   * @param entryId The id of the entry enumeration
   * @return The StateMasterDAO according to the name and entryId
   */
  public StateMasterDAO addState(String name, String entryId)
  {
    StateMasterDAO newState = StateMasterDAO.newInstance(this.definesType());
    newState.setValue(StateMasterDAOIF.STATE_NAME, name);
    newState.setValue(StateMasterDAOIF.ENTRY_STATE, entryId);

    return newState;
  }

  /**
   * Creates a new transition relationship but does not apply the relationship in case
   * additional attributes must be supplied.
   *
   * @param name The new name of the transition relationship
   * @param sourceId The id of the state which the transition connects from
   * @param sinkId The id of the state which the transition connects to
   *
   * @return The transition relationship of the MdState
   */
  public TransitionDAO addTransition(String name, String sourceId, String sinkId)
  {
    validateTransition(name);
    MdRelationshipDAOIF transition = this.getMdTransition();

    TransitionDAO newTransition = TransitionDAO.newInstance(sourceId, sinkId, transition.definesType());
    newTransition.setName(name);
    newTransition.setStructValue(TransitionDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, name);

    return newTransition;
  }

  public static void validateTransition(String name)
  {
    //Ensure that the state name is not a reserved word
    if(ReservedWords.javaContains(name))
    {
      String error = "[" + name + "] is a reserved word and invalid Transition name.";
      throw new ReservedWordException(error, name, ReservedWordException.Origin.TRANSITION);
    }

    for(int i = 0; i < name.length(); i++)
    {
      if(!Character.isLetterOrDigit(name.charAt(i)) && name.charAt(i) != '_')
      {
        String error = "Invalid chacater '" + name.charAt(i) + "' in the transition name [" + name + "]";
        throw new NameConventionException(error, name);
      }
    }
  }

  /**
   * @see com.runwaysdk.business.state.MdStateIF#getStates()
   */
  @SuppressWarnings("unchecked")
  public List<StateMasterDAOIF> definesStateMasters()
  {
    return (List<StateMasterDAOIF>)BusinessDAO.getCachedEntityDAOs(this.definesType());
  }

  /**
   * @see com.runwaysdk.business.state.MdStateIF#getTransitions()
   */
  @SuppressWarnings("unchecked")
  public List<TransitionDAOIF> definesTransitions()
  {
    MdRelationshipDAOIF mdTransition = this.getMdTransition();

    return (List<TransitionDAOIF>)RelationshipDAO.getCachedEntityDAOs(mdTransition.definesType());
  }

  /**
   * @see com.runwaysdk.business.state.MdStateIF#getMdTransition()
   */
  public MdRelationshipDAOIF getMdTransition()
  {
    return MdRelationshipDAO.getMdRelationshipDAO(this.getMdTransitionType());
  }

  public String getMdTransitionName()
  {
    return MdStateMachineDAO.formatName(this.getTypeName() + TRANS_SUFFIX);
  }

  public String getMdTransitionPackage()
  {
    return STATE_PACKAGE + "." + this.getPackage() + "." + this.getTypeName();
  }

  public String getMdTransitionType()
  {
    return this.getMdTransitionPackage() + "." + this.getMdTransitionName();
  }

  /**
   *
   */
  public String save(boolean flag)
  {
    //Set the super entity to STATE_MASTER before saving the MdStateMachine for the first time
    if (this.isNew())
    {
      this.getAttribute(MdBusinessInfo.CACHE_ALGORITHM).setValue(EntityCacheMaster.CACHE_EVERYTHING.getId());
      this.getAttribute(MdBusinessInfo.SUPER_MD_BUSINESS).setValue(EntityTypes.STATE_MASTER.getId());
    }

    //Save the MdStateMachine
    String id =  super.save(flag);
    
    String statusName = this.getMdStatusName();
    String statusPack = this.getMdStatusPackage();

    //If this is the first time the MdStateMachine has been save then create the abstract transition
    //and status MdRelationships used by the MdStateMachine
    if (this.isNew())
    {
      String transitionName = this.getMdTransitionName();
      String transitionPack = this.getMdTransitionPackage();

      MdElementDAOIF transitionMaster = MdElementDAO.getMdElementDAO(RelationshipTypes.TRANSITION_RELATIONSHIP.getType());

      // Create the abstract MdTransition relationship for this MdStateMachine
      MdGraphDAO newTransition = MdGraphDAO.newInstance();

      newTransition.setValue(MdTypeInfo.NAME, transitionName);
      newTransition.setValue(MdTypeInfo.PACKAGE, transitionPack);
      newTransition.setStructValue(MdTypeInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, this.getDisplayLabel(CommonProperties.getDefaultLocale()) + " MdTransition");
      newTransition.setValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP, transitionMaster.getId());
      newTransition.setValue(MdTypeInfo.GENERATE_SOURCE, this.isGenerateSource().toString());

      newTransition.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
      newTransition.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,"PRE STATE");
      newTransition.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, this.getId());
      newTransition.setValue(MdRelationshipInfo.PARENT_METHOD, "PreState");

      newTransition.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
      newTransition.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "POST STATE");
      newTransition.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, this.getId());
      newTransition.setValue(MdRelationshipInfo.CHILD_METHOD, "PostState");
      newTransition.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());

      newTransition.apply();

      //Create the abstract MdStatus relationship for this class
      MdTreeDAO newStatus = MdTreeDAO.newInstance();

      String owner = this.getValue(MdStateMachineInfo.STATE_MACHINE_OWNER);

      newStatus.setValue(MdTypeInfo.NAME, statusName);
      newStatus.setValue(MdTypeInfo.PACKAGE, statusPack);
      newStatus.setStructValue(MdTypeInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, this.getDisplayLabel(CommonProperties.getDefaultLocale()) + " MdStatus");
      newStatus.setValue(MdElementInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
      newStatus.setValue(MdElementInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      newStatus.setValue(MdTypeInfo.GENERATE_SOURCE, this.isGenerateSource().toString());

      newStatus.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
      newStatus.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Owning object of the state");
      newStatus.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, owner);

      newStatus.setValue(MdRelationshipInfo.PARENT_METHOD, "DefiningMdBusiness");

      newStatus.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "1");
      newStatus.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, this.getTypeName() + " State");
      newStatus.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, this.getId());
      newStatus.setValue(MdRelationshipInfo.CHILD_METHOD, "StateMachine");
//      newStatus.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.EVERYTHING.getId());

      newStatus.apply();

      //Set the defines state machine relationship
      RelationshipDAO definesState = RelationshipDAO.newInstance(owner, this.getId(), MdStateMachineInfo.DEFINES_STATE_MACHINE_RELATIONSHIP);
      definesState.setKey(buildDefinesStateMachineRelationshipKey(statusPack, statusName));
      definesState.apply();
    }
    else
    {
      String owner = this.getValue(MdStateMachineInfo.STATE_MACHINE_OWNER);
     
      // Update the key of the relationship
      List<RelationshipDAOIF> defineStateList = RelationshipDAO.get(owner, this.getId(), MdStateMachineInfo.DEFINES_STATE_MACHINE_RELATIONSHIP);
      
      for (RelationshipDAOIF relationshipDAOIF : defineStateList)
      {
        RelationshipDAO relationshipDAO = relationshipDAOIF.getRelationshipDAO();
        relationshipDAO.setKey(buildDefinesStateMachineRelationshipKey(statusPack, statusName));
        relationshipDAO.apply();
      }
    }

    return id;
  }

  private String buildDefinesStateMachineRelationshipKey(String statusPack, String statusName)
  {
    return statusPack+"."+statusName;
  }
  
  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateIF#generateMdStatus(com.runwaysdk.business.state.StateIF)
   */
  public MdRelationshipDAOIF getMdStatus(StateMasterDAOIF state)
  {
    return MdRelationshipDAO.getMdRelationshipDAO(this.getMdStatusType(state));
  }

  public String getMdStatusName(StateMasterDAOIF state)
  {
    return MdStateMachineDAO.formatName(state.getName() + StateMasterDAO.STATE_SUFFIX);
  }

  public String getMdStatusPackage()
  {
    return STATE_PACKAGE + "." + this.getPackage() + "." +  this.getTypeName();
  }

  public String getMdStatusType(StateMasterDAOIF state)
  {
    return this.getMdStatusPackage() + "." + this.getMdStatusName(state);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateIF#getEntryState()
   */
  public List<StateMasterDAOIF> getEntryState()
  {
    List<StateMasterDAOIF> entryStates = new LinkedList<StateMasterDAOIF>();

    for(StateMasterDAOIF state : this.definesStateMasters())
    {
      if(state.isEntryState())
      {
        entryStates.add(state);
      }
    }

    return entryStates;
  }

  /**
   * Get the default state of the MdState
   *
   * @return The defualt entry state of the MdState
   *
   * @throws DataNotFoundException If this state machine has no default state
   */
  public StateMasterDAOIF getDefaultState()
  {
    for(StateMasterDAOIF state : this.definesStateMasters())
    {
      if(state.isDefaultState())
      {
        return state;
      }
    }

    String error = "A default state has not been defined for the MdStateMachine [" + this.definesType() + "]";
    throw new DataNotFoundException(error, MdBusinessDAO.getMdBusinessDAO(MdStateMachineInfo.CLASS));
  }

  /**
   * Formats a given string to remove whitespaces and capitialize the first character after each whitespace
   *
   * @param s The string to format
   * @return The formatted string
   */
  protected static String formatName(String s)
  {
    StringTokenizer toke = new StringTokenizer(s);

    String ret = toke.nextToken();

    while(toke.hasMoreTokens())
    {
      String part = toke.nextToken();

      //Set the first character after the whitespace to an uppercase
      ret += Character.toUpperCase(part.charAt(0)) + part.substring(1);
    }

    return ret;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateIF#getMdStatus()
   */
  public MdTreeDAOIF getMdStatus()
  {
    return MdTreeDAO.getMdTreeDAO(this.getMdStatusType());
  }

  public String getMdStatusName()
  {
    return MdStateMachineDAO.formatName(this.getTypeName() + STATUS_SUFFIX);
  }

  //The abstract status name is the fully quallifed name of the MdState + STATUS_SUFFIX
  public String getMdStatusType()
  {
    return this.getMdStatusPackage() + "." + this.getMdStatusName();
  }


  @Override
  public MdStateMachineDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new MdStateMachineDAO(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateIF#getDefiningMdBusiness()
   */
  public MdBusinessDAOIF getStateMachineOwner()
  {
    List<RelationshipDAOIF> list = this.getParents(MdStateMachineInfo.DEFINES_STATE_MACHINE_RELATIONSHIP);

    if(list.size() != 0)
    {
      return (MdBusinessDAOIF) list.get(0).getParent();
    }

    return null;
  }

//  /**
//   * Returns the MdBusinessDAO that owns the state machine.
//   * @return MdBusinessDAO that owns the state machine.
//   */
//  public MdBusinessDAOIF getStateMachineOwner()
//  {
//    return (MdBusinessDAOIF)((AttributeReferenceIF)this.getAttributeIF(MdStateMachineInfo.STATE_MACHINE_OWNER)).dereference();
//  }

  /**
   * Initializes the current state of all existing BusinessDAOs of the defining MdBusinessDAO
   */
  public void initializeExistingInstance()
  {
    //Get the MdBusinessDAO which owns the MdState
    MdBusinessDAOIF mdBusiness = this.getStateMachineOwner();

    //Get the default state of this MdState
    StateMasterDAOIF defaultState = this.getDefaultState();

    //Get the abstract status relationship of this MdState
    MdTreeDAOIF status = this.getMdStatus();

    QueryFactory queryFactory = new QueryFactory();
    BusinessDAOQuery q = queryFactory.businessDAOQuery(mdBusiness.definesType());

    OIterator<BusinessDAOIF> iterator = q.getIterator();

    for(BusinessDAOIF businessDAOIF : iterator)
    {
      List<RelationshipDAOIF> list = businessDAOIF.getChildren(status.definesType());

      //If a status relationship does not already exist between the BusinessDAO and this MdStatus
      //then create one with the default state
      if(list.size() == 0)
      {
        BusinessDAO businessDAO = businessDAOIF.getBusinessDAO();
        businessDAO.setEntryState(defaultState);
      }
    }


  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdStateMachineDAO getBusinessDAO()
  {
    return (MdStateMachineDAO) super.getBusinessDAO();
  }

  /**
   * Returns a new BusinessDAO.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return BusinessDAO instance of the given class
   * @throws com.runwaysdk.dataaccess.DataAccessException
   *           if the class is not valid or is abstract
   */
  public static MdStateMachineDAO newInstance()
  {
    return (MdStateMachineDAO) BusinessDAO.newInstance(MdStateMachineInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String, java.lang.String)
   */
  public static MdStateMachineDAOIF get(String id)
  {
    return (MdStateMachineDAOIF) BusinessDAO.get(id);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateMachineDAOIF#getState(java.lang.String)
   */
  public StateMasterDAOIF definesStateMaster(String stateName)
  {
    for(StateMasterDAOIF state : this.definesStateMasters())
    {
      if(stateName.equals(state.getName()))
      {
        return state;
      }
    }

    String msg = "The state [" + stateName + "] does not exist on [" + this.getId() + "]";
    throw new DataNotFoundException(msg, this);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateMachineDAOIF#getTransition(java.lang.String)
   */
  public TransitionDAOIF definesTransition(String transitionName)
  {
    for(TransitionDAOIF transition : this.definesTransitions())
    {
      String name = transition.getName();

      if(transitionName.equals(name))
      {
        return transition;
      }
    }

    String msg = "The transition [" + transitionName + "] does not exist on [" + this.getId() + "]";
    throw new DataNotFoundException(msg, this);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.business.state.MdStateMachineDAOIF#definesTransitions(com.runwaysdk.business.state.StateMasterDAOIF)
   */
  public List<TransitionDAOIF> definesTransitions(StateMasterDAOIF source)
  {
    List<TransitionDAOIF> transitions = new LinkedList<TransitionDAOIF>();

    for(TransitionDAOIF transition : this.definesTransitions())
    {
      StateMasterDAOIF parent = (StateMasterDAOIF) transition.getParent();

      if(parent.getId().equals(source.getId()))
      {
        transitions.add(transition);
      }
    }

    return transitions;
  }

  /**
   * Returns a list of all generators used to generate source
   * for this MdType.
   *
   * @return
   */
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    //Dont generate reserved types
    if (GenerationUtil.isReservedType(this))
    {
      return list;
    }

    list.add(new StateGenerator(this));

    return list;
  }

  @Override
  protected boolean isMdControllerQualified()
  {
    return false;
  }

}
