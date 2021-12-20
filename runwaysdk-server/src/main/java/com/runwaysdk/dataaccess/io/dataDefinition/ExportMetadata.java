/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFormDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;

public class ExportMetadata
{
  /**
   * A new {@link MdMethodDAO}, with associated {@link MdParameterDAO}s, which has not been applied to the database but is going to be exported.
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
     * @return A list of the new {@link MdParameterDAO}s for the new {@link MdMethodDAO}
     */
    public List<MdParameterDAOIF> getMdParameters()
    {
      return mdParameters;
    }
  }

  /**
   * A new component, which has not been applied to the database, but is going to be exported.
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
     * List of new ParameterMarkers, and corresponding MdParamerters, to add to an existing MdEntity, MdFacade, MdController
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
   * List of components to export under the {@link XMLTags#CREATE_OR_UPDATE_TAG}
   */
  private List<ComponentIF>         createOrUpdateList;
  
  /**
   * List of components to export under the {@link XMLTags#PERMISSION_TAG}
   */
  private List<ComponentIF>         grantPermissionsList;

  /**
   * List of components to export under the {@link XMLTags#PERMISSION_TAG}
   */
  private List<ComponentIF>         revokePermissionsList;

  /**
   * Mapping between an existing {@link ComponentIF} oid and it's {@link NewComponent}s.
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
    this.createOrUpdateList = new LinkedList<ComponentIF>();
    this.grantPermissionsList = new LinkedList<ComponentIF>();
    this.revokePermissionsList = new LinkedList<ComponentIF>();
    this.newComponentList = new HashMap<String, NewComponent>();
    this.renameAttributes = new HashMap<String, String>();
    this.rekeyEntities = new HashMap<String, String>();
    this.exportSource = exportSource;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export under the {@link XMLTags#DELETE_TAG}
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
   * @return a list of {@link ComponentIF}s to export under the {@link XMLTags#DELETE_TAG}
   */
  public List<ComponentIF> getDeleteList()
  {
    return deleteList;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export under the {@link XMLTags#UPDATE_TAG}
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
   * @return a list of {@link ComponentIF}s to export under the {@link XMLTags#UPDATE_TAG}
   */
  public List<ComponentIF> getUpdateList()
  {
    return updateList;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export under the {@link XMLTags#CREATE_TAG}
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
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export under the {@link XMLTags#CREATE_OR_UPDATE_TAG}
   * 
   * @param components
   *          {@link ComponentIF} to add
   */
  public void addCreateOrUpdate(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      createOrUpdateList.add(component);
    }
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the {@link XMLTags#CREATE_TAG}
   */
  public List<ComponentIF> getCreateList()
  {
    return createList;
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the {@link XMLTags#CREATE_TAG}
   */
  public List<ComponentIF> getCreateOrUpdateList()
  {
    return createOrUpdateList;
  }

  /**
   * Adds a {@link ComponentIF} to the list of {@link ComponentIF}s to export under the {@link XMLTags#PERMISSIONS_TAG}
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

  /**
   * @param role
   * @param exportRole If true then all the permissions on the RoleDAOIF will also be exported.
   * @param oid
   */
  public void grantAllPermissions(RoleDAOIF component, boolean exportRole, MdBusinessDAOIF... mdBusinesses)
  {
    PermissionComponent decorator = new PermissionComponent(component, exportRole);

    for (MdBusinessDAOIF mdBusiness : mdBusinesses)
    {
      decorator.addAllPermissions(mdBusiness);
    }

    grantPermissionsList.add(decorator);
  }
  
  public void grantAllPermissions(RoleDAOIF component, MdBusinessDAOIF... mdBusinesses)
  {
    grantAllPermissions(component, true, mdBusinesses);
  }

  public void addRevokePermissions(ComponentIF... components)
  {
    for (ComponentIF component : components)
    {
      revokePermissionsList.add(component);
    }
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the {@link XMLTags#PERMISSIONS_TAG}
   */
  public List<ComponentIF> getGrantPermissionList()
  {
    return grantPermissionsList;
  }

  /**
   * @return a list of {@link ComponentIF}s to export under the {@link XMLTags#PERMISSIONS_TAG}
   */
  public List<ComponentIF> getRevokePermissionList()
  {
    return revokePermissionsList;
  }

  /**
   * Adds a new {@link MdAttributeConcreteDAO} to an existing {@link MdEntityDAO}
   * 
   * @param mdClass
   *          Existing {@link MdEntityDAO}
   * @param mdAttribute
   *          The new {@link MdAttributeConcreteDAO}
   */
  public void addNewMdAttribute(MdClassDAOIF mdClass, MdAttributeDAO mdAttribute)
  {
    if (!newComponentList.containsKey(mdClass.getOid()))
    {
      newComponentList.put(mdClass.getOid(), new NewComponent());
    }

    newComponentList.get(mdClass.getOid()).addMdAttribute(mdAttribute);
  }

  /**
   * @param mdClass
   *          An existing {@link MdEntityDAO}
   * @return A list of the new MdAttributes defined for a existing {@link MdEntityDAO}
   */
  public List<MdAttributeDAO> getNewMdAttributes(MdClassDAOIF mdClass)
  {
    if (!newComponentList.containsKey(mdClass.getOid()))
    {
      return new LinkedList<MdAttributeDAO>();
    }

    return newComponentList.get(mdClass.getOid()).attributes;
  }

  public List<MdWebFieldDAO> getNewMdWebFields(MdWebFormDAOIF mdWebForm)
  {
    if (!newComponentList.containsKey(mdWebForm.getOid()))
    {
      return new LinkedList<MdWebFieldDAO>();
    }

    return newComponentList.get(mdWebForm.getOid()).fields;
  }

  /**
   * Adds a new {@link MdMethodDAO} and {@link MdParameters} to an existing {@link MdEntityDAO} or {@link MdFacadeDAO}
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
    if (!newComponentList.containsKey(mdType.getOid()))
    {
      newComponentList.put(mdType.getOid(), new NewComponent());
    }

    newComponentList.get(mdType.getOid()).addMethod(mdMethod, mdParameters);
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
    if (!newComponentList.containsKey(marker.getOid()))
    {
      newComponentList.put(marker.getOid(), new NewComponent());
    }

    newComponentList.get(marker.getOid()).addMdParameter(mdParameter);
  }

  /**
   * Adds an enumerated item to be removed from an existing {@link MdEnumerationDAO}
   * 
   * @param mdEnumeration
   *          An existing {@link MdEnumerationDAO}
   * @param businessDAOs
   *          Existing enumerated items
   */
  public void addRemoveEnumItem(MdEnumerationDAOIF mdEnumeration, BusinessDAOIF... businessDAOs)
  {
    if (!newComponentList.containsKey(mdEnumeration.getOid()))
    {
      newComponentList.put(mdEnumeration.getOid(), new NewComponent());
    }

    newComponentList.get(mdEnumeration.getOid()).addRemoveEnumItem(businessDAOs);
  }

  /**
   * @param component
   * @return if new components have been defined for the given {@link ComponentIF}
   */
  public boolean hasNewComponents(ComponentIF component)
  {
    return newComponentList.containsKey(component.getOid());
  }

  /**
   * @param mdType
   *          An existing {@link MdFacadeDAO} or {@link MdEntityDAO}
   * @return List of new {@link MdMethodDAO}s defined for an existing {@link MdFacadeDAO} or {@link MdEntityDAO}
   */
  public List<NewParameterMarker> getNewParameterMarkers(MdTypeDAOIF mdType)
  {
    if (!newComponentList.containsKey(mdType.getOid()))
    {
      return new LinkedList<NewParameterMarker>();
    }

    return newComponentList.get(mdType.getOid()).markers;
  }

  /**
   * @param marker
   *          An existing {@link ParameterMarker}
   * @return List of new {@link MdParameters}s defined for an existing {@link MdMethodDAO}
   */
  public List<MdParameterDAOIF> getNewMdParameters(ParameterMarker marker)
  {
    if (!newComponentList.containsKey(marker.getOid()))
    {
      return new LinkedList<MdParameterDAOIF>();
    }

    return newComponentList.get(marker.getOid()).mdParameters;
  }

  /**
   * @param mdEnumeration
   *          An existing {@link MdEnumerationDAO}
   * @return List of enumerated items to be removed from an existing {@link MdEnumerationDAO}
   */
  public List<BusinessDAOIF> getRemoveEnumItems(MdEnumerationDAOIF mdEnumeration)
  {
    if (!newComponentList.containsKey(mdEnumeration.getOid()))
    {
      return new LinkedList<BusinessDAOIF>();
    }

    return newComponentList.get(mdEnumeration.getOid()).removeEnumItems;
  }

  public boolean isExportSource()
  {
    return exportSource;
  }

  /**
   * Helper method used to indicate a component should be added to the list of createOrUpdate objects in the exported xml
   * 
   * @param components
   * @return
   */
  public static ExportMetadata buildCreateOrUpdate(ComponentIF... components)
  {
    ExportMetadata metadata = new ExportMetadata();

    metadata.addCreateOrUpdate(components);

    return metadata;
  }

  /**
   * Helper method used to indicate a component should be added to the list of created object in the exported xml
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
   * Helper method used to indicate a component should be added to the list of deleted objects in the exported xml
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
   * Helper method used to indicate a component should be added to the list of updated objects in the exported xml
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

  public List<? extends MdAttributeDAOIF> filterAttributes(MdClassDAOIF mdEntity)
  {
    List<? extends MdAttributeDAOIF> list = mdEntity.definesAttributes();
    List<MdAttributeDAO> newAttributes = this.getNewMdAttributes(mdEntity);

    Iterator<? extends MdAttributeDAOIF> it = list.iterator();

    while (it.hasNext())
    {
      MdAttributeDAOIF next = it.next();

      if (newAttributes.contains(next))
      {
        it.remove();
      }
    }

    return list;
  }

  public void renameAttribute(MdAttributeDAOIF mdAttribute, String updatedName)
  {
    this.renameAttributes.put(mdAttribute.getOid(), updatedName);
  }

  public boolean hasRename(MdAttributeDAOIF mdAttribute)
  {
    return this.renameAttributes.containsKey(mdAttribute.getOid());
  }

  public String getRename(MdAttributeDAOIF mdAttribute)
  {
    return this.renameAttributes.get(mdAttribute.getOid());
  }

  public void rekeyEntity(EntityDAOIF entity, String newKey)
  {
    this.rekeyEntities.put(entity.getOid(), newKey);
  }

  public boolean isRekeyed(EntityDAOIF entity)
  {
    return this.rekeyEntities.containsKey(entity.getOid());
  }

  public String getRekey(EntityDAOIF entity)
  {
    return this.rekeyEntities.get(entity.getOid());
  }

  public void addNewMdField(MdWebFormDAO mdWebForm, MdWebFieldDAO mdWebField)
  {
    if (!newComponentList.containsKey(mdWebForm.getOid()))
    {
      newComponentList.put(mdWebForm.getOid(), new NewComponent());
    }

    newComponentList.get(mdWebForm.getOid()).addMdWebField(mdWebField);
  }

}
