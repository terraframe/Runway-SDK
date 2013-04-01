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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.metadata.MdActionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdFacadeDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;

public class ExportMetadata
{
  /**
   * A new {@link MdMethodDAO}, with associated {@link MdParameterDAO}s, which
   * has not been applied to the database but is going to be exported.
   * 
   * @author Justin Smethie
   */
  public class NewParameterMarker
  {
    /**
     * New MdMethod to export
     */
    private ParameterMarker        marker;

    /**
     * New MdParameters on the new MdMethod
     */
    private List<MdParameterDAOIF> mdParameters;

    /**
     * @param marker
     *          New {@link MdMethodDAO} to export
     * @param mdParameters
     *          New {@link MdParameterDAO}s on the new {@link MdMethodDAO}
     */
    public NewParameterMarker(ParameterMarker marker, MdParameterDAO[] mdParameters)
    {
      this.marker = marker;
      this.mdParameters = new LinkedList<MdParameterDAOIF>();

      for (MdParameterDAO mdParameter : mdParameters)
      {
        this.mdParameters.add((MdParameterDAOIF) mdParameter);
      }
    }

    /**
     * @return The new {@link MdMethodDAO}
     */
    public ParameterMarker getMarker()
    {
      return marker;
    }

    /**
     * @return A list of the new {@link MdParameterDAO}s for the new
     *         {@link MdMethodDAO}
     */
    public List<MdParameterDAOIF> getMdParameters()
    {
      return mdParameters;
    }
  }

  /**
   * A new component, which has not been applied to the database, but is going
   * to be exported.
   * 
   * @author Justin Smethie
   */
  class NewComponent
  {
    /**
     * List of new MdAttributes to add to an existing MdEntity
     */
    private List<MdAttributeDAO>     attributes;

    /**
     * List of new MdWebFields to add to an existing MdWebForm
     */
    private List<MdWebFieldDAO>      fields;

    /**
     * A new MdStateMachine to add to an existing MdBusiness
     */
    private MdStateMachineDAO        mdStateMachine;

    /**
     * List of new StateMasters to add to a new or existing MdStateMachine
     */
    private List<StateMasterDAOIF>   states;

    /**
     * List of new TransitionDAO to add to a new or existing MdStateMachine
     */
    private List<TransitionDAOIF>    transitions;

    /**
     * List of new ParameterMarkers, and corresponding MdParamerters, to add to
     * an existing MdEntity, MdFacade, MdController
     */
    private List<NewParameterMarker> markers;

    /**
     * List of new MdParameters to add to an existing ParameterMarker
     */
    private List<MdParameterDAOIF>   mdParameters;

    /**
     * List of enumerated items to remove from an existing MdEnumeration
     */
    private List<BusinessDAOIF>      removeEnumItems;

    /**
     * Contruct a new {@link NewComponent}
     */
    NewComponent()
    {
      attributes = new LinkedList<MdAttributeDAO>();
      mdStateMachine = null;
      states = new LinkedList<StateMasterDAOIF>();
      transitions = new LinkedList<TransitionDAOIF>();
      markers = new LinkedList<NewParameterMarker>();
      mdParameters = new LinkedList<MdParameterDAOIF>();
      removeEnumItems = new LinkedList<BusinessDAOIF>();
      fields = new LinkedList<MdWebFieldDAO>();
    }

    /**
     * Add a new MdMethod component.
     * 
     * @param mdMethod
     *          MdMethod to add
     * @param mdParameters
     *          MdParameters of the new MdMethod
     */
    void addMethod(MdMethodDAO mdMethod, MdParameterDAO... mdParameters)
    {
      markers.add(new NewParameterMarker(mdMethod, mdParameters));
    }

    /**
     * Add a new MdAttribute component.
     * 
     * @param mdAttribute
     *          The MdAttribute to add
     */
    void addMdAttribute(MdAttributeDAO mdAttribute)
    {
      attributes.add(mdAttribute);
    }

    /**
     * Add a new MdAttribute component.
     * 
     * @param mdAttribute
     *          The MdAttribute to add
     */
    void addMdWebField(MdWebFieldDAO mdWebField)
    {
      fields.add(mdWebField);
    }

    /**
     * Add a new MdStateMachine component.
     * 
     * @param mdStateMachine
     *          The new MdStateMachine
     * @param states
     *          The new StateMasters corresponding to the new MdStateMachine
     * @param transitions
     *          The new TransitionDAOs corresponding to the new MdStateMachine
     */
    void addMdStateMachine(MdStateMachineDAO mdStateMachine, List<StateMasterDAO> states, List<TransitionDAO> transitions)
    {
      this.mdStateMachine = mdStateMachine;
      this.states.addAll(states);
      this.transitions.addAll(transitions);
    }

    /**
     * Add a new MdParameter component
     * 
     * @param mdParameter
     *          The new MdParameter
     */
    void addMdParameter(MdParameterDAO mdParameter)
    {
      this.mdParameters.add(mdParameter);
    }

    /**
     * Adds new TransitionDAO components
     * 
     * @param transitions
     *          The new TransitionDAOs
     */
    public void addTransitions(TransitionDAO... transitions)
    {
      for (TransitionDAO transition : transitions)
      {
        this.transitions.add(transition);
      }
    }

    /**
     * Adds new StateMasterDAO components
     * 
     * @param states
     *          The new StateMasters
     */
    public void addStates(StateMasterDAO... states)
    {
      for (StateMasterDAO state : states)
      {
        this.states.add(state);
      }
    }

    /**
     * Adds a new remove enumerated item component
     * 
     * @param businessDAOs
     *          List of enumerated items
     */
    public void addRemoveEnumItem(BusinessDAOIF... businessDAOs)
    {
      for (BusinessDAOIF businessDAO : businessDAOs)
      {
        this.removeEnumItems.add(businessDAO);
      }
    }

    public void addAction(MdActionDAO mdAction, MdParameterDAO... mdParameters)
    {
      markers.add(new NewParameterMarker(mdAction, mdParameters));
    }
  }

  /**
   * List of components to export under the {@link XMLTags#DELETE_TAG}
   */
  private List<ComponentIF>         deleteList;

  /**
   * List of components to export under the {@link XMLTags#UPDATE_TAG}
   */
  private List<ComponentIF>         updateList;

  /**
   * List of components to export under the {@link XMLTags#CREATE_TAG}
   */
  private List<ComponentIF>         createList;

  /**
   * List of components to export under the {@link XMLTags#PERMISSION_TAG}
   */
  private List<ComponentIF>         grantPermissionsList;

  /**
   * List of components to export under the {@link XMLTags#PERMISSION_TAG}
   */
  private List<ComponentIF>         revokePermissionsList;

  /**
   * Mapping between an existing {@link ComponentIF} id and it's
   * {@link NewComponent}s.
   */
  private Map<String, NewComponent> newComponentList;

  /**
   * Id-rename pairing for mdAttribute definitions
   */
  private Map<String, String>       renameAttributes;

  private Map<String, String>       rekeyEntities;

  /**
   * Flag denoting if source should be exported
   */
  private boolean                   exportSource;

  public ExportMetadata()
  {
    this(false);
  }

  /**
   * Constructs a new {@link ExportMetadata}
   */
  public ExportMetadata(boolean exportSource)
  {
    this.deleteList = new LinkedList<ComponentIF>();
    this.updateList = new LinkedList<ComponentIF>();
    this.createList = new LinkedList<ComponentIF>();
    this.grantPermissionsList = new LinkedList<ComponentIF>();
    this.revokePermissionsList = new LinkedList<ComponentIF>();
    this.newComponentList = new HashMap<String, NewComponent>();
    this.renameAttributes = new HashMap<String, String>();
    this.rekeyEntities = new HashMap<String, String>();
    this.exportSource = exportSource;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export
   * under the {@link XMLTags#DELETE_TAG}
   * 
   * @param components
   *          {@link ComponentIF} to add
   */
  public void addDelete(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      deleteList.add(component);
    }
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the
   *         {@link XMLTags#DELETE_TAG}
   */
  public List<ComponentIF> getDeleteList()
  {
    return deleteList;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export
   * under the {@link XMLTags#UPDATE_TAG}
   * 
   * @param components
   *          {@link ComponentIF} to add
   */
  public void addUpdate(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      updateList.add(component);
    }
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the
   *         {@link XMLTags#UPDATE_TAG}
   */
  public List<ComponentIF> getUpdateList()
  {
    return updateList;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export
   * under the {@link XMLTags#CREATE_TAG}
   * 
   * @param components
   *          {@link ComponentIF} to add
   */
  public void addCreate(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      createList.add(component);
    }
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the
   *         {@link XMLTags#CREATE_TAG}
   */
  public List<ComponentIF> getCreateList()
  {
    return createList;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export
   * under the {@link XMLTags#PERMISSIONS_TAG}
   * 
   * @param components
   *          {@link ComponentIF} to add
   */
  public void addGrantPermissions(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      grantPermissionsList.add(component);
    }
  }

  public void addRevokePermissions(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      revokePermissionsList.add(component);
    }
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the
   *         {@link XMLTags#PERMISSIONS_TAG}
   */
  public List<ComponentIF> getGrantPermissionList()
  {
    return grantPermissionsList;
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the
   *         {@link XMLTags#PERMISSIONS_TAG}
   */
  public List<ComponentIF> getRevokePermissionList()
  {
    return revokePermissionsList;
  }

  /**
   * Adds a new {@link MdAttributeConcreteDAO} to an existing
   * {@link MdEntityDAO}
   * 
   * @param mdClass
   *          Existing {@link MdEntityDAO}
   * @param mdAttribute
   *          The new {@link MdAttributeConcreteDAO}
   */
  public void addNewMdAttribute(MdClassDAOIF mdClass, MdAttributeDAO mdAttribute)
  {
    if (!newComponentList.containsKey(mdClass.getId()))
    {
      newComponentList.put(mdClass.getId(), new NewComponent());
    }

    newComponentList.get(mdClass.getId()).addMdAttribute(mdAttribute);
  }

  /**
   * @param mdClass
   *          An existing {@link MdEntityDAO}
   * @return A list of the new MdAttributes defined for a existing
   *         {@link MdEntityDAO}
   */
  public List<MdAttributeDAO> getNewMdAttributes(MdClassDAOIF mdClass)
  {
    if (!newComponentList.containsKey(mdClass.getId()))
    {
      return new LinkedList<MdAttributeDAO>();
    }

    return newComponentList.get(mdClass.getId()).attributes;
  }

  public List<MdWebFieldDAO> getNewMdWebFields(MdWebFormDAOIF mdWebForm)
  {
    if (!newComponentList.containsKey(mdWebForm.getId()))
    {
      return new LinkedList<MdWebFieldDAO>();
    }

    return newComponentList.get(mdWebForm.getId()).fields;
  }

  /**
   * Adds a new {@link MdStateMachineDAO} to an existing {@link MdBusinessDAO}
   * 
   * @param mdBusiness
   *          Existing {@link MdBusinessDAO}
   * @param mdStateMachine
   *          The new {@link MdStateMachineDAO}
   * @param states
   *          The new {@link StateMasterDAO}s of the new
   *          {@link MdStateMachineDAO}
   * @param transitions
   *          The new {@link TransitionDAO}s of the new
   *          {@link MdStateMachineDAO}
   */
  public void addNewMdStateMachine(MdBusinessDAOIF mdBusiness, MdStateMachineDAO mdStateMachine, List<StateMasterDAO> states, List<TransitionDAO> transitions)
  {
    if (!newComponentList.containsKey(mdBusiness.getId()))
    {
      newComponentList.put(mdBusiness.getId(), new NewComponent());
    }

    newComponentList.get(mdBusiness.getId()).addMdStateMachine(mdStateMachine, states, transitions);
  }

  /**
   * Adds a new {@link StateMasterDAO} to an existing {@link MdStateMachineDAO}
   * 
   * @param mdStateMachine
   *          Existing {@link MdStateMachineDAO}
   * @param states
   *          The new {@link StateMasterDAO}s of the new
   *          {@link MdStateMachineDAO}
   */
  public void addNewStates(MdStateMachineDAOIF mdStateMachine, StateMasterDAO... states)
  {
    if (!newComponentList.containsKey(mdStateMachine.getId()))
    {
      newComponentList.put(mdStateMachine.getId(), new NewComponent());
    }

    newComponentList.get(mdStateMachine.getId()).addStates(states);
  }

  /**
   * Adds a new {@link TransitionDAO} to an existing {@link MdStateMachineDAO}
   * 
   * @param mdStateMachine
   *          Existing {@link MdStateMachineDAO}
   * @param transitions
   *          The new {@link TransitionDAO}s of the new
   *          {@link MdStateMachineDAO}
   */
  public void addNewTransitions(MdStateMachineDAOIF mdStateMachine, TransitionDAO... transitions)
  {
    if (!newComponentList.containsKey(mdStateMachine.getId()))
    {
      newComponentList.put(mdStateMachine.getId(), new NewComponent());
    }

    newComponentList.get(mdStateMachine.getId()).addTransitions(transitions);
  }

  /**
   * Adds a new {@link MdMethodDAO} and {@link MdParameters} to an existing
   * {@link MdEntityDAO} or {@link MdFacadeDAO}
   * 
   * @param mdType
   *          Existing {@link MdTypeDAO}
   * @param mdMethod
   *          The new {@link MdMethodDAO}
   * @param mdParameters
   *          The new {@link MdParameterDAO}s of the new {@link MdMethodDAO}
   */
  public void addNewMdMethod(MdTypeDAOIF mdType, MdMethodDAO mdMethod, MdParameterDAO... mdParameters)
  {
    if (!newComponentList.containsKey(mdType.getId()))
    {
      newComponentList.put(mdType.getId(), new NewComponent());
    }

    newComponentList.get(mdType.getId()).addMethod(mdMethod, mdParameters);
  }

  public void addNewMdAction(MdControllerDAOIF mdController, MdActionDAO mdAction, MdParameterDAO... mdParameters)
  {
    if (!newComponentList.containsKey(mdController.getId()))
    {
      newComponentList.put(mdController.getId(), new NewComponent());
    }

    newComponentList.get(mdController.getId()).addAction(mdAction, mdParameters);
  }

  /**
   * Adds a new {@link MdParameters} to an existing {@link MdMethodDAO}
   * 
   * @param marker
   *          An existing {@link ParameterMarker}
   * @param mdParameter
   *          The new {@link MdParameterDAO}
   */
  public void addNewMdParameter(ParameterMarker marker, MdParameterDAO mdParameter)
  {
    if (!newComponentList.containsKey(marker.getId()))
    {
      newComponentList.put(marker.getId(), new NewComponent());
    }

    newComponentList.get(marker.getId()).addMdParameter(mdParameter);
  }

  /**
   * Adds an enumerated item to be removed from an existing
   * {@link MdEnumerationDAO}
   * 
   * @param mdEnumeration
   *          An existing {@link MdEnumerationDAO}
   * @param businessDAOs
   *          Existing enumerated items
   */
  public void addRemoveEnumItem(MdEnumerationDAOIF mdEnumeration, BusinessDAOIF... businessDAOs)
  {
    if (!newComponentList.containsKey(mdEnumeration.getId()))
    {
      newComponentList.put(mdEnumeration.getId(), new NewComponent());
    }

    newComponentList.get(mdEnumeration.getId()).addRemoveEnumItem(businessDAOs);
  }

  /**
   * @param component
   * @return if new components have been defined for the given
   *         {@link ComponentIF}
   */
  public boolean hasNewComponents(ComponentIF component)
  {
    return newComponentList.containsKey(component.getId());
  }

  /**
   * @param mdBusiness
   *          An existing {@link MdBusinessDAOIF}
   * @return The new MdStateMachine defined for an existing
   *         {@link MdBusinessDAO}
   */
  public MdStateMachineDAO getNewMdStateMachine(MdBusinessDAOIF mdBusiness)
  {
    if (!newComponentList.containsKey(mdBusiness.getId()))
    {
      return null;
    }

    return newComponentList.get(mdBusiness.getId()).mdStateMachine;
  }

  /**
   * @param mdBusiness
   *          An existing {@link MdBusinessDAOIF}
   * @return List of the new {@link StateMasterDAO}s defined for an existing
   *         {@link MdBusinessDAO} or {@link MdStateMachineDAO}
   */
  public List<StateMasterDAOIF> getNewStates(MdBusinessDAOIF mdBusiness)
  {
    if (!newComponentList.containsKey(mdBusiness.getId()))
    {
      return new LinkedList<StateMasterDAOIF>();
    }

    return newComponentList.get(mdBusiness.getId()).states;
  }

  /**
   * @param mdBusiness
   *          An existing {@link MdBusinessDAOIF}
   * @return List of the new {@link TransitionDAO}s defined for an existing
   *         {@link MdBusinessDAO} or {@link MdStateMachineDAO}
   */
  public List<TransitionDAOIF> getNewTransitions(MdBusinessDAOIF mdBusiness)
  {
    if (!newComponentList.containsKey(mdBusiness.getId()))
    {
      return new LinkedList<TransitionDAOIF>();
    }

    return newComponentList.get(mdBusiness.getId()).transitions;
  }

  /**
   * @param mdType
   *          An existing {@link MdFacadeDAO} or {@link MdEntityDAO}
   * @return List of new {@link MdMethodDAO}s defined for an existing
   *         {@link MdFacadeDAO} or {@link MdEntityDAO}
   */
  public List<NewParameterMarker> getNewParameterMarkers(MdTypeDAOIF mdType)
  {
    if (!newComponentList.containsKey(mdType.getId()))
    {
      return new LinkedList<NewParameterMarker>();
    }

    return newComponentList.get(mdType.getId()).markers;
  }

  /**
   * @param marker
   *          An existing {@link ParameterMarker}
   * @return List of new {@link MdParameters}s defined for an existing
   *         {@link MdMethodDAO}
   */
  public List<MdParameterDAOIF> getNewMdParameters(ParameterMarker marker)
  {
    if (!newComponentList.containsKey(marker.getId()))
    {
      return new LinkedList<MdParameterDAOIF>();
    }

    return newComponentList.get(marker.getId()).mdParameters;
  }

  /**
   * @param mdEnumeration
   *          An existing {@link MdEnumerationDAO}
   * @return List of enumerated items to be removed from an existing
   *         {@link MdEnumerationDAO}
   */
  public List<BusinessDAOIF> getRemoveEnumItems(MdEnumerationDAOIF mdEnumeration)
  {
    if (!newComponentList.containsKey(mdEnumeration.getId()))
    {
      return new LinkedList<BusinessDAOIF>();
    }

    return newComponentList.get(mdEnumeration.getId()).removeEnumItems;
  }

  public boolean isExportSource()
  {
    return exportSource;
  }

  /**
   * Helper method used in testing
   * 
   * @param components
   * @return
   */
  public static ExportMetadata buildCreate(ComponentIF... components)
  {
    ExportMetadata metadata = new ExportMetadata();

    metadata.addCreate(components);

    return metadata;
  }

  /**
   * Helper method used in testing
   * 
   * @param components
   * @return
   */
  public static ExportMetadata buildDelete(ComponentIF... components)
  {
    ExportMetadata metadata = new ExportMetadata();

    metadata.addDelete(components);

    return metadata;
  }

  /**
   * Helper method used in testing
   * 
   * @param components
   * @return
   */
  public static ExportMetadata buildUpdate(ComponentIF... components)
  {
    ExportMetadata metadata = new ExportMetadata();

    metadata.addUpdate(components);

    return metadata;
  }

  public List<? extends MdAttributeDAOIF> filterAttributes(MdEntityDAOIF mdEntity)
  {
    List<? extends MdAttributeConcreteDAOIF> list = mdEntity.definesAttributes();
    List<MdAttributeDAO> newAttributes = this.getNewMdAttributes(mdEntity);

    Iterator<? extends MdAttributeConcreteDAOIF> it = list.iterator();

    while (it.hasNext())
    {
      MdAttributeConcreteDAOIF next = it.next();

      if (newAttributes.contains(next))
      {
        it.remove();
      }
    }

    return list;
  }

  public void renameAttribute(MdAttributeDAOIF mdAttribute, String updatedName)
  {
    this.renameAttributes.put(mdAttribute.getId(), updatedName);
  }

  public boolean hasRename(MdAttributeDAOIF mdAttribute)
  {
    return this.renameAttributes.containsKey(mdAttribute.getId());
  }

  public String getRename(MdAttributeDAOIF mdAttribute)
  {
    return this.renameAttributes.get(mdAttribute.getId());
  }

  public void rekeyEntity(EntityDAOIF entity, String newKey)
  {
    this.rekeyEntities.put(entity.getId(), newKey);
  }

  public boolean isRekeyed(EntityDAOIF entity)
  {
    return this.rekeyEntities.containsKey(entity.getId());
  }

  public String getRekey(EntityDAOIF entity)
  {
    return this.rekeyEntities.get(entity.getId());
  }

  public void addNewMdField(MdWebFormDAO mdWebForm, MdWebFieldDAO mdWebField)
  {
    if (!newComponentList.containsKey(mdWebForm.getId()))
    {
      newComponentList.put(mdWebForm.getId(), new NewComponent());
    }

    newComponentList.get(mdWebForm.getId()).addMdWebField(mdWebField);
  }
}
