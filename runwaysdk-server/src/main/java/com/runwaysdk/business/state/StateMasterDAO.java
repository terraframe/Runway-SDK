/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.business.state;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdTreeDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.dataaccess.metadata.NameConventionException;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.dataaccess.metadata.ReservedWords;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;

public class StateMasterDAO extends MetadataDAO implements StateMasterDAOIF
{
  /**
   * An auto-generated eclipse ID
   */
  private static final long     serialVersionUID = 1121222844076161428L;

  /**
   * The suffix automatically added to the concrete Status relationship of each
   * state
   */
  protected static final String STATE_SUFFIX     = "_SS";

  public StateMasterDAO()
  {
    super();
  }

  public StateMasterDAO(Map<String, Attribute> attributeMap, String classType)
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
    String signature = "StateMachine: " + ( (MdStateMachineDAOIF) this.getMdBusinessDAO() ).definesType() + " StateName: " + this.getName() + " ";

    return signature;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.EntityDAO#setValue(java.lang.String,
   * java.lang.String)
   */
  public void setValue(String name, String value)
  {
    if (name.equals(StateMasterDAOIF.STATE_NAME))
    {
      // Ensure that the state name is not a reserved word
      if (ReservedWords.javaContains(value))
      {
        String error = "[" + value + "] is a reserved word and invalid State name.";
        throw new ReservedWordException(error, value, ReservedWordException.Origin.STATE);
      }

      // Ensure state name only contains alpha-numeric characters or '_'
      for (int i = 0; i < value.length(); i++)
      {
        char c = value.charAt(i);
        if (!Character.isLetterOrDigit(c) && c != '_')
        {
          String error = "[" + value + "] is an invalid State name.";
          throw new NameConventionException(error, value);
        }
      }

      // Validate that the state name does not already exist in this state
      // machine
      MdStateMachineDAOIF stateMachine = (MdStateMachineDAOIF) this.getMdBusinessDAO();
      List<StateMasterDAOIF> states = stateMachine.definesStateMasters();

      for (StateMasterDAOIF state : states)
      {
        if (name.equals(state.getName()))
        {
          String error = "A state named [" + name + "] already exists in the [" + stateMachine.definesType() + "] state machine";

          List<AttributeIF> attributeList = new LinkedList<AttributeIF>();
          attributeList.add(this.getAttributeIF(StateMasterDAOIF.STATE_NAME));

          List<String> valueList = new LinkedList<String>();
          valueList.add(value);

          throw new DuplicateStateDefinitionException(error, name, stateMachine.definesType());
        }
      }

    }

    if (name.equals(StateMasterDAOIF.ENTRY_STATE))
    {
      // Validate that a default entry state has not already been defined
      if (value.equals(StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId()))
      {
        // If the default state has already been defined then throw an exception
        try
        {
          MdStateMachineDAOIF stateMachine = (MdStateMachineDAOIF) this.getMdBusinessDAO();
          StateMasterDAOIF defaultState = stateMachine.getDefaultState();

          // If we don't error out, there is already a default state
          if (!defaultState.equals(this))
          {
            String error = "A default state has already been defined for State Machine [" + stateMachine.definesType() + "]";
            throw new DefaultStateExistsException(error, stateMachine.definesType());
          }
        }
        catch (DataNotFoundException e)
        {
          // This means that there is no default state, so it's safe to set
        }
      }
    }

    super.setValue(name, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.state.StateIF#getName()
   */
  public String getName()
  {
    return this.getValue(StateMasterDAOIF.STATE_NAME);
  }

  /**
   * Returns the fully qualified name of the state, including the type and the
   * name of the state.
   * 
   * @return fully qualified name of the state, including the type and the name
   *         of the state.
   */
  public String getQualifiedName()
  {
    return this.getType() + "." + this.getName();
  }

  /**
   * Returns a description of this metadata;
   * 
   * @param SupportedLocale
   *          locale
   * 
   * @return a description of this metadata;
   */
  public String getDescription(Locale locale)
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MetadataInfo.DESCRIPTION) ).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDescriptions()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(MetadataInfo.DESCRIPTION) ).getLocalValues();
  }

  public String getDisplayLabel(Locale locale)
  {
    return ( (AttributeLocalIF) this.getAttributeIF(StateMasterDAOIF.DISPLAY_LABEL) ).getValue(locale);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   * 
   * @return map where the key is the locale and the value is the localized
   *         String value.
   */
  public Map<String, String> getDisplayLabels()
  {
    return ( (AttributeLocalIF) this.getAttributeIF(StateMasterDAOIF.DISPLAY_LABEL) ).getLocalValues();
  }

  public boolean isRemovable()
  {
    return Boolean.parseBoolean(this.getValue(MetadataInfo.REMOVE));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.state.StateIF#getNextState(java.lang.String)
   */
  public StateMasterDAOIF getNextState(String transitionName)
  {
    List<RelationshipDAOIF> list = this.getChildren(RelationshipTypes.TRANSITION_RELATIONSHIP.getType());

    RelationshipDAOIF transition = StateMasterDAO.findTransition(transitionName);

    // Ensure that the transition is a valid transition from this state
    if (list.contains(transition))
    {
      return (StateMasterDAOIF) transition.getChild();
    }

    String error = "A transition named [" + transitionName + "] does not exist for state [" + getName() + "] on machine [" + getMdBusinessDAO().definesType() + "]";
    throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MetadataInfo.CLASS));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.state.StateIF#isEntryState()
   */
  public boolean isEntryState()
  {
    // Ensure that the symmetric encryption method is set
    AttributeEnumerationIF entry = (AttributeEnumerationIF) this.getAttributeIF(StateMasterDAOIF.ENTRY_STATE);
    String entryId = entry.dereference()[0].getId();

    return ( entryId.equals(StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId()) || entryId.equals(StateMasterDAOIF.Entry.ENTRY_STATE.getId()) );
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.state.StateIF#isDefaultState()
   */
  public boolean isDefaultState()
  {
    AttributeEnumerationIF entry = (AttributeEnumerationIF) this.getAttributeIF(StateMasterDAOIF.ENTRY_STATE);
    String entryId = entry.dereference()[0].getId();

    return entryId.equals(StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public StateMasterDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new StateMasterDAO(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public StateMasterDAO getBusinessDAO()
  {
    return (StateMasterDAO) super.getBusinessDAO();
  }

  /**
   * IMPORTANT: Do not delete this method. It is a hook for apsects.
   */
  public String save(boolean validateRequired)
  {
    return super.save(validateRequired);
  }

  @Override
  public String apply()
  {
    String key = StateMasterDAO.buildKey(this.getMdBusinessDAO().definesType(), this.getName());
    this.setKey(key);

    this.setStructValue(StateMasterDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State " + this.getName());
    this.setStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, this.getName() + " state of the " + this.getMdBusinessDAO().getTypeName() + " state machine");
    this.setValue(MetadataInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    String id = super.apply();

    // If this is the first time the state has been applied
    // then create the mdStatus relationship
    if (this.isNew())
    {
      MdStateMachineDAO mdState = (MdStateMachineDAO) this.getMdBusinessDAO();

      String name = MdStateMachineDAO.formatName(this.getName() + StateMasterDAO.STATE_SUFFIX);
      String pack = MdStateMachineDAO.STATE_PACKAGE + "." + mdState.getPackage() + "." + mdState.getTypeName();

      String statusName = mdState.getDisplayLabel(CommonProperties.getDefaultLocale()) + " " + this.getName() + " MdStatus";

      // Get the owning MdBusiness
      MdBusinessDAOIF owner = mdState.getStateMachineOwner();

      // Get the abstract parent of the new relationship
      MdTreeDAOIF superStatus = mdState.getMdStatus();

      // Create the currentState transition for the book state
      MdTreeDAO newStatus = MdTreeDAO.newInstance();

      newStatus.setValue(MdTypeInfo.NAME, name);
      newStatus.setValue(MdTypeInfo.PACKAGE, pack);
      newStatus.setStructValue(MdTypeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, statusName);
      newStatus.setValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP, superStatus.getId());
      newStatus.setValue(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());

      newStatus.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
      newStatus.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Owning object of the state");
      newStatus.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, owner.getId());
      newStatus.setValue(MdRelationshipInfo.PARENT_METHOD, "DefiningMdBusiness");

      newStatus.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "1");
      newStatus.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdState.getTypeName() + " State");
      newStatus.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdState.getId());
      newStatus.setValue(MdRelationshipInfo.CHILD_METHOD, "StateMachine");
      newStatus.apply();
    }

    return id;
  }

  /**
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  @Override
  public void delete(boolean businessContext)
  {
    dropTuples();
    dropStatusRelationship();

    super.delete(businessContext);
  }

  /**
   * Deletes the 'status' MdRelationship that was defined for this StateMaster
   */
  private void dropStatusRelationship()
  {
    MdStateMachineDAO mdStateMachine = (MdStateMachineDAO) this.getMdBusinessDAO();

    String name = MdStateMachineDAO.formatName(this.getName() + StateMasterDAO.STATE_SUFFIX);
    String pack = MdStateMachineDAO.STATE_PACKAGE + "." + mdStateMachine.getPackage() + "." + mdStateMachine.getTypeName();

    MdTreeDAO mdStatus = (MdTreeDAO) MdTreeDAO.getMdRelationshipDAO(pack + "." + name).getBusinessDAO();
    mdStatus.delete();
  }

  /**
   * Deletes any TypeTuples in which this MdAttribute is part of.
   */
  private void dropTuples()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(TypeTupleDAOIF.CLASS);
    query.WHERE(query.aReference(TypeTupleDAOIF.STATE_MASTER).EQ(this.getId()));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while (iterator.hasNext())
    {
      BusinessDAOIF businessDAOIF = iterator.next();
      businessDAOIF.getBusinessDAO().delete(false);
    }
  }

  /**
   *
   */
  public int hashCode()
  {
    return this.getQualifiedName().hashCode();
  }

  /**
   * Find the transition which has the given transitionName
   * 
   * @param transitionName
   *          The name of the transition
   * @return
   */
  private static RelationshipDAOIF findTransition(String transitionName)
  {
    QueryFactory qFactory = new QueryFactory();
    RelationshipDAOQuery transRelQ = qFactory.relationshipDAOQuery(RelationshipTypes.TRANSITION_RELATIONSHIP.getType());
    transRelQ.WHERE(transRelQ.aCharacter(StateMasterDAOIF.TRANSITION_NAME).EQ(transitionName));
    OIterator<RelationshipDAOIF> transRelIterator = transRelQ.getIterator();

    if (transRelIterator.hasNext())
    {
      RelationshipDAOIF returnRel = (RelationshipDAOIF) transRelIterator.next();
      transRelIterator.close();
      return returnRel;
    }

    String error = "A transition named [" + transitionName + "] does not exist for any state machine.";
    throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(RelationshipTypes.TRANSITION_RELATIONSHIP.getType()));
  }

  /**
   * Returns a new BusinessDAO. Some attributes will contain default values, as
   * defined in the attribute metadata. Otherwise, the attributes will be blank.
   * 
   * <br/>
   * <b>Precondition:</b> classType parameter represents a valid classType in
   * the database. <br/>
   * <b>Precondition:</b> classType must not be abstract, otherwise a DataAccess
   * exception will be thrown <br/>
   * <b>Postcondition:</b> BusinessDAO returned is an instance of the given
   * classType. The BusinessDAO contains all attributes defined for that
   * classType.
   * 
   * @param classType
   *          Valid classType. Examples: Constants.USER, FOLDER
   * @return BusinessDAO instance of the given class
   * @throws com.runwaysdk.dataaccess.DataAccessException
   *           if the class is not valid or is abstract
   */
  public static StateMasterDAO newInstance(String classType)
  {
    return (StateMasterDAO) BusinessDAO.newInstance(classType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static StateMasterDAOIF get(String stateId)
  {
    EntityDAOIF entityDAOIF = EntityDAO.get(stateId);
    StateMasterDAOIF state = (StateMasterDAOIF) entityDAOIF;

    return state;
  }

  public static String buildKey(String stateMachineType, String stateName)
  {
    return stateMachineType + "." + stateName;
  }
}
