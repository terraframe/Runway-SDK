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
package com.runwaysdk.dataaccess.io;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;

import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF;
import com.runwaysdk.dataaccess.io.dataDefinition.SearchCriteriaIF;
import com.runwaysdk.dataaccess.io.dataDefinition.TagContext;
import com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdFormDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdWebSingleTermGridDAO;

/**
 * Tracks the state of the import and facilitates communication between the
 * different tag handlers. The state includes: a mapping between real ids and
 * xml ids used in the import, a list of the fully qualified types which have
 * been imported, and a list of xml pusedo ids which are currently being search
 * for because of type dependencies.
 * 
 * @author Justin Smethie
 * @author Aritra
 */
public class ImportManager
{
  /**
   * List of States the import manager can be in
   * 
   * @author Justin Smethie
   */
  private enum State {
    /**
     * State for importing keyName to pusedo oid references
     */
    EXISTING,

    /**
     * State for deleting existing objects
     */
    DELETE,

    /**
     * State for importing updates to the a existing objects
     */
    UPDATE,

    /**
     * State for importing new objects
     */
    CREATE,

    /**
     * State which will update an object if it exists, or create a new one if it
     * doesn't
     */
    CREATE_OR_UPDATE,

    /**
     * State for loading permissions
     */
    PERMISSIONS;
  }

  /**
   * A mapping between puesdo and real oid values for instances
   */
  private HashMap<String, String>                    idMapping;

  /**
   * A collection of the names of Object which have already been created
   */
  private Set<String>                                importedTypes;

  /**
   * A collection of the oid which have been imported
   */
  private Set<String>                                importedObjects;

  /**
   * A list of the values currently being searched for
   */
  private Stack<SearchCriteriaIF>                    callStack;

  /**
   * The location of the .xsd schema file used in the .xml file being imported
   */
  private String                                     schemaLocation;

  /**
   * The current state of the import
   */
  private Stack<State>                               state;

  /**
   * Map of handler class names to list of factories to use when dispatching
   * child tags
   */
  private Map<String, Map<String, HandlerFactoryIF>> map;

  /**
   * Root tag handler
   */
  private TagHandlerIF                               root;

  /**
   * Source to parse
   */
  protected StreamSource                             source;

  private Map<String, String>                        keyMap;

  protected ImportManager(String schemaLocation)
  {
    this.schemaLocation = schemaLocation;
    this.importedTypes = new TreeSet<String>();
    this.importedObjects = new TreeSet<String>();
    this.idMapping = new HashMap<String, String>();
    this.callStack = new Stack<SearchCriteriaIF>();
    this.state = new Stack<State>();
    this.map = new HashMap<String, Map<String, HandlerFactoryIF>>();
    this.keyMap = new HashMap<String, String>();
  }

  public ImportManager(StreamSource source, String schemaLocation)
  {
    this(schemaLocation);

    this.source = source;
  }

  public void addKeyMapping(String key, String oid)
  {
    this.keyMap.put(key, oid);
  }

  public String getKeyMapping(String key)
  {
    return this.keyMap.get(key);
  }

  /**
   * Adds a mapping between a type and the database oid to the mapping
   * collection
   * 
   * @param puesdo
   *          The xml oid
   * @param oid
   *          The core oid
   */
  public void addMapping(String type, String oid)
  {
    this.idMapping.put(type, oid);
  }

  /**
   * @param oid
   */
  public void addImportedObject(String oid)
  {
    this.importedObjects.add(oid);
  }

  /**
   * @return the importedObjects
   */
  public Set<String> getImportedObjects()
  {
    return importedObjects;
  }

  public boolean enforceValidation(String cause)
  {
    return !this.idMapping.containsKey(cause);
  }

  public void addImportedType(String type)
  {
    importedTypes.add(type);
  }

  /**
   * @param type
   * 
   * @return true if the type has already been created
   */
  public boolean isCreated(String type)
  {
    return importedTypes.contains(type);
  }

  public void endImport(String keyName)
  {
    if (this.isCreateState() || this.isUpdateState() || this.isCreateOrUpdateState())
    {
      this.addImportedType(keyName);
    }
  }

  /**
   * Adds a puesdo oid to the list of oid currently on which a search is being
   * performed.
   * 
   * @param oid
   *          The oid
   * @param cause
   *          TODO
   */
  public void addSearchId(SearchCriteriaIF criteria, String cause)
  {
    callStack.push(criteria);
  }

  /**
   * Removes the top pusedo oid from the search list
   */
  public void removeSearchId()
  {
    if (callStack.size() > 0)
    {
      callStack.pop();
    }
  }

  /**
   * Validates that an pusedo xml oid is valid for searching. Will throw an
   * exception if the oid is already in the list of ids under search.
   * 
   * @param criteria
   *          A pusedo xml oid
   */
  public void validateSearch(SearchCriteriaIF criteria)
  {
    if (callStack.contains(criteria))
    {
      throw new XMLParseException("Circular dependency on [" + criteria.criteria() + "]");
    }
  }

  /**
   * Returns the fully qualified location of the .xsd schema file used for the
   * .xml import.
   */
  public String getSchemaLocation()
  {
    return schemaLocation;
  }

  /**
   * @return the importedTypes
   */
  public Set<String> getImportedTypes()
  {
    return importedTypes;
  }

  /**
   * Changes the current state to {@link State#EXISTING}
   */
  public void enterExistingState()
  {
    state.push(State.EXISTING);
  }

  /**
   * Changes the current state to {@link State#DELETE}
   */
  public void enterDeleteState()
  {
    state.push(State.DELETE);
  }

  /**
   * Changes the current state to {@link State#UPDATE}
   */
  public void enterUpdateState()
  {
    state.push(State.UPDATE);
  }

  /**
   * Changes the current state to {@link State#CREATE}
   */
  public void enterCreateState()
  {
    state.push(State.CREATE);
  }

  /**
   * Changes the current state to {@link State#CREATE_OR_UPDATE}
   */
  public void enterCreateOrUpdateState()
  {
    state.push(State.CREATE_OR_UPDATE);
  }

  /**
   * Pushes the state {@link State#PERMISSIONS} to the front of the state
   * history stack
   */
  public void enterPermissionsState()
  {
    state.push(State.PERMISSIONS);
  }

  /**
   * Pops the current state from the state history
   */
  public void leavingCurrentState()
  {
    state.pop();
  }

  /**
   * Returns a BusinessDAO. Depending on the state of the manager a different
   * BusinessDAO is returned. If the manager is in CREATE state then a new
   * BusinessDAO of the given type is returned. If the manager is in UPDATE
   * state then an existing BusinessDAO of the given type and key is returned.
   * 
   * @param type
   *          Fully qualified type of the BusinessDAO
   * @param key
   *          key of the BusinessDAO
   * @return
   */
  public EntityDAOIF getEntityDAO(String type, String key)
  {
    MdTypeDAOIF mdType = MdTypeDAO.getMdTypeDAO(type);

    if (this.isCreateOrUpdateState())
    {
      try
      {
        return this.getEntityDAO(type, key, mdType);
      }
      catch (DataNotFoundException e)
      {
        return this.createEntityDAO(type, mdType);
      }
    }
    else if (this.isCreateState())
    {
      return this.createEntityDAO(type, mdType);
    }
    else
    {
      return this.getEntityDAO(type, key, mdType);
    }
  }

  /**
   * @param type
   * @param key
   * @param mdType
   * @return
   */
  private EntityDAOIF getEntityDAO(String type, String key, MdTypeDAOIF mdType)
  {
    if (mdType instanceof MdBusinessDAOIF)
    {
      return BusinessDAO.get(type, key);
    }

    return StructDAO.get(type, key);
  }

  /**
   * @param type
   * @param mdType
   * @return
   */
  private EntityDAOIF createEntityDAO(String type, MdTypeDAOIF mdType)
  {
    if (mdType instanceof MdBusinessDAOIF)
    {
      return BusinessDAO.newInstance(type);
    }

    return StructDAO.newInstance(type);
  }

  public boolean isPermissionState()
  {
    return state.peek().equals(State.PERMISSIONS);
  }

  /**
   * Depending on the {@link State} of the manager it returns the MdAttribute
   * defined by the given MdEntity. If the manager is in the CREATE state a new
   * MdAttribute of the given type is returned. If the manager is in the UPDATE
   * state an existing attribute of the given type and name is returned.
   * 
   * @param mdClass
   *          {@link MdClassDAO} that defines the MdAttribute
   * @param name
   *          Name of the MdAttribute
   * @param type
   *          Type of the MdAttribute
   * @throws DataNotFoundException
   *           if an update operation is requested on an attribute that does not
   *           exist, which often occurs because the user forgot to wrap the
   *           attribute definition in a create tag.
   * 
   * @return
   */
  public MdAttributeDAO getMdAttribute(MdClassDAO mdClass, String name, String type)
  {
    if (this.isCreateOrUpdateState())
    {
      MdAttributeDAOIF mdAttr = mdClass.definesAttribute(name);
      if (mdAttr != null)
      {
        return (MdAttributeDAO) mdAttr.getBusinessDAO();
      }
    }
    else if (this.isUpdateState())
    {
      MdAttributeDAOIF mdAttr = mdClass.definesAttribute(name);
      if (mdAttr != null)
      {
        return (MdAttributeDAO) mdAttr.getBusinessDAO();
      }
      else
      {
        String error = "The [" + State.UPDATE.name() + "] operation failed on the attribute [" + name + "] defined" + " by type [" + mdClass.definesType() + "] because the attribute could not be found.";
        throw new DataNotFoundException(error, MdEntityDAO.getMdEntityDAO(MdAttributeInfo.CLASS));
      }
    }

    return (MdAttributeDAO) MdAttributeConcreteDAO.newInstance(type);
  }

  /**
   * Depending on the {@link State} of the manager it returns the MdAttribute
   * defined by the given MdEntity. If the manager is in the CREATE state a new
   * MdAttribute of the given type is returned. If the manager is in the UPDATE
   * state an existing attribute of the given type and name is returned.
   * 
   * @param mdClass
   *          {@link MdClassDAO} that defines the MdAttribute
   * @param name
   *          Name of the MdAttribute
   * @param type
   *          Type of the MdAttribute
   * @throws DataNotFoundException
   *           if an update operation is requested on an attribute that does not
   *           exist, which often occurs because the user forgot to wrap the
   *           attribute definition in a create tag.
   * 
   * @return
   */
  public MdFieldDAO getMdField(MdFormDAO mdForm, String name, String type)
  {
    if (this.isCreateOrUpdateState())
    {
      MdFieldDAOIF mdAttr = mdForm.getMdField(name);
      if (mdAttr != null)
      {
        return (MdFieldDAO) mdAttr.getBusinessDAO();
      }
    }
    else if (this.isUpdateState())
    {
      MdFieldDAOIF mdAttr = mdForm.getMdField(name);
      if (mdAttr != null)
      {
        return (MdFieldDAO) mdAttr.getBusinessDAO();
      }
      else
      {
        String error = "The [" + State.UPDATE.name() + "] operation failed on the field [" + name + "] defined" + " by type [" + mdForm.definesType() + "] because the attribute could not be found.";
        throw new DataNotFoundException(error, MdEntityDAO.getMdEntityDAO(MdAttributeInfo.CLASS));
      }
    }

    return (MdFieldDAO) MdWebFieldDAO.newInstance(type);
  }

  /**
   * @param mdWebSingleGrid
   * @param name
   * @param type
   * @return
   */
  public MdFieldDAO getMdField(MdWebSingleTermGridDAO mdWebSingleGrid, String name, String type)
  {
    if (this.isCreateOrUpdateState())
    {
      MdFieldDAOIF mdAttr = mdWebSingleGrid.getMdField(name);

      if (mdAttr != null)
      {
        return (MdFieldDAO) mdAttr.getBusinessDAO();
      }
    }
    else if (this.isUpdateState())
    {
      MdFieldDAOIF mdAttr = mdWebSingleGrid.getMdField(name);

      if (mdAttr != null)
      {
        return (MdFieldDAO) mdAttr.getBusinessDAO();
      }
      else
      {
        String error = "The [" + State.UPDATE.name() + "] operation failed on the field [" + name + "] defined" + " by type [" + mdWebSingleGrid.getKey() + "] because the attribute could not be found.";
        throw new DataNotFoundException(error, MdEntityDAO.getMdEntityDAO(MdAttributeInfo.CLASS));
      }
    }

    return (MdFieldDAO) MdWebFieldDAO.newInstance(type);
  }

  /**
   * @return Returns a stream of the xml source which is being parsed
   */
  public StreamSource getStreamSource()
  {
    return source;
  }

  /**
   * @return Returns a stream of the xml source which is being parsed
   */
  public InputSource getSource()
  {
    return new InputSource(source.getInputStream());
  }

  /**
   * Helper method used for parsing {@link Attributes}. If an attribute of the
   * given name exists in the list of given attributes then it sets the
   * corresponding attribute on the {@link EntityDAO} to the value specified in
   * the xml. Otherwise no change occurs to the {@link EntityDAO}.
   * 
   * @param entityDAO
   *          The EntityDAO to set.
   * @param attributeName
   *          Name of the attribute on the BusinessDAO
   * @param attributes
   *          List of Attributes
   * @param xmlName
   *          Name of the attribute in the list of attributes
   */
  public static void setValue(EntityDAO entityDAO, String attributeName, Attributes attributes, String xmlName)
  {
    String value = attributes.getValue(xmlName);

    if (value != null)
    {
      Attribute attribute = entityDAO.getAttribute(attributeName);
      attribute.setValue(value);
    }
  }

  public static void setValue(EntityDAO entityDAO, String attributeName, Attributes attributes, String xmlName, String defaultValue)
  {
    String value = attributes.getValue(xmlName);

    if (value != null)
    {
      Attribute attribute = entityDAO.getAttribute(attributeName);
      attribute.setValue(value);
    }
    else
    {
      Attribute attribute = entityDAO.getAttribute(attributeName);

      if (attribute.getValue() == null || attribute.getValue().equals(""))
      {
        attribute.setValue(defaultValue);
      }
    }
  }

  public boolean isUpdateState()
  {
    return state.peek().equals(State.UPDATE);
  }

  public boolean isCreateState()
  {
    return state.peek().equals(State.CREATE);
  }

  public boolean isCreateOrUpdateState()
  {
    return state.peek().equals(State.CREATE_OR_UPDATE);
  }

  public boolean isDeleteState()
  {
    return state.peek().equals(State.DELETE);
  }

  public boolean isCreateWithinUpdate()
  {
    boolean result = false;
    if (isCreateState())
    {
      State tempState = state.pop();
      if (!state.isEmpty())
      {
        if (isUpdateState())
        {
          result = true;
        }
      }
      state.push(tempState);
    }
    return result;
  }

  /**
   * Returns true if the parsing is within a create tag that appears immediately
   * below doIt/undoIt If the parsing is within a create tag that is nested
   * within another update, the method returns false.
   * 
   * @return
   */
  public boolean isTopLevelCreate()
  {
    return isCreateOrUpdateState() || ( isCreateState() && !isCreateWithinUpdate() );
  }

  /**
   * Helper method used for parsing {@link Attributes}. If an attribute of the
   * given name exists in the list of given attributes then it sets the
   * corresponding attribute on the {@link EntityDAO} to the value specified in
   * the xml. Otherwise no change occurs to the {@link EntityDAO}.
   * 
   * @param entityDAO
   *          The EntityDAO to set.
   * @param attributeName
   *          Name of the attribute on the BusinessDAO
   * @param attributes
   *          List of Attributes
   * @param xmlName
   *          Name of the attribute in the list of attributes
   */
  public static void setLocalizedValue(EntityDAO entityDAO, String attributeName, Attributes attributes, String xmlName)
  {
    String value = attributes.getValue(xmlName);

    if (value == null)
    {
      return;
    }

    Attribute attribute = entityDAO.getAttribute(attributeName);

    if (attribute instanceof AttributeStruct)
    {
      AttributeStruct attributeStruct = (AttributeStruct) attribute;

      StructDAO structDAO = attributeStruct.getStructDAO();

      // Set the default value to English
      if (value != null)
      {
        structDAO.getAttribute(MdAttributeLocalInfo.DEFAULT_LOCALE).setValue(value);
      }

      // Loop through all of the attributes of the struct and try to determine
      // if the attribute
      // has been defined for a different locale. We are looking for the
      // attribute name plus the
      // underscore plus the locale string
      for (MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF : structDAO.getMdAttributeDAOs())
      {
        String locAttrName = mdAttributeConcreteDAOIF.definesAttribute();

        String xmlLocAttrName = xmlName + "_" + locAttrName;

        String locAttrValue = attributes.getValue(xmlLocAttrName);

        if (!mdAttributeConcreteDAOIF.isSystem() && locAttrValue != null && !locAttrValue.trim().equals(""))
        {
          structDAO.getAttribute(locAttrName).setValue(locAttrValue);
        }
      }
    }
  }

  public HandlerFactoryIF getFactory(TagContext context, String localName)
  {
    TagHandlerIF handler = context.getHandler();
    String key = handler.getKey();

    if (this.map.containsKey(key))
    {
      Collection<HandlerFactoryIF> factories = this.map.get(key).values();

      for (HandlerFactoryIF factory : factories)
      {
        if (factory.supports(context, localName))
        {
          return factory;
        }
      }
    }

    return null;
  }

  /**
   * @param name
   * @param factories
   */
  public void register(String name, HandlerFactoryIF... factories)
  {
    if (factories != null && factories.length > 0 && !this.map.containsKey(name))
    {
      this.map.put(name, new LinkedHashMap<String, HandlerFactoryIF>());
    }

    for (HandlerFactoryIF factory : factories)
    {
      Map<String, HandlerFactoryIF> set = this.map.get(name);
      set.put(factory.getClass().getName(), factory);
    }
  }

  /**
   * @param rootHandler
   */
  public void setRoot(TagHandlerIF root)
  {
    this.root = root;
  }

  /**
   * @return the root
   */
  public TagHandlerIF getRoot()
  {
    return root;
  }
}
