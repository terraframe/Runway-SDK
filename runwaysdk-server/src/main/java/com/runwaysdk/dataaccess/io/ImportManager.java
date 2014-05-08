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
package com.runwaysdk.dataaccess.io;

import java.util.HashMap;
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
import com.runwaysdk.dataaccess.io.dataDefinition.SearchCriteriaIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MdFieldDAO;
import com.runwaysdk.dataaccess.metadata.MdFormDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdWebFieldDAO;

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
     * State for importing keyName to pusedo id references
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
     * State for loading permissions
     */
    PERMISSIONS;
  }

  /**
   * A mapping between puesdo and real id values for instances
   */
  private HashMap<String, String> idMapping;

  /**
   * A collection of the names of Object which have already been created
   */
  private Set<String>             importedTypes;

  /**
   * A list of the values currently being searched for
   */
  private Stack<SearchCriteriaIF> callStack;

  protected StreamSource          source;

  /**
   * The location of the .xsd schema file used in the .xml file being imported
   */
  private String                  schemaLocation;

  /**
   * The current state of the import
   */
  private Stack<State>            state;

  protected ImportManager(String schemaLocation)
  {
    this.schemaLocation = schemaLocation;
    this.importedTypes = new TreeSet<String>();
    this.idMapping = new HashMap<String, String>();
    this.callStack = new Stack<SearchCriteriaIF>();
    this.state = new Stack<State>();
  }

  public ImportManager(StreamSource source, String schemaLocation)
  {
    this(schemaLocation);

    this.source = source;
  }

  /**
   * Adds a mapping between a typeand the database id to the mapping collection
   * 
   * @param puesdo
   *          The xml id
   * @param id
   *          The core id
   */
  public void addMapping(String type, String id)
  {
    this.idMapping.put(type, id);
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
    if (state.peek().equals(State.CREATE))
    {
      this.addImportedType(keyName);
    }
    else if (state.peek().equals(State.UPDATE))
    {
      this.addImportedType(keyName);
    }
  }

  /**
   * Adds a puesdo id to the list of id currently on which a search is being
   * performed.
   * 
   * @param id
   *          The id
   * @param cause
   *          TODO
   */
  public void addSearchId(SearchCriteriaIF criteria, String cause)
  {
    callStack.push(criteria);
  }

  /**
   * Removes the top pusedo id from the search list
   */
  public void removeSearchId()
  {
    if (callStack.size() > 0)
    {
      callStack.pop();
    }
  }

  /**
   * Validates that an pusedo xml id is valid for searching. Will throw an
   * exception if the id is already in the list of ids under search.
   * 
   * @param criteria
   *          A pusedo xml id
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

    if (this.inCreateState())
    {
      if (mdType instanceof MdBusinessDAOIF)
      {
        return BusinessDAO.newInstance(type);
      }

      return StructDAO.newInstance(type);
    }
    else
    {
      if (mdType instanceof MdBusinessDAOIF)
      {
        return BusinessDAO.get(type, key);
      }

      return StructDAO.get(type, key);
    }
  }

  /**
   * @return If the import is in the 'CREATE' state
   */
  public boolean inCreateState()
  {
    return State.CREATE.equals(state.peek());
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
    if (State.UPDATE.equals(state.peek()))
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
    if (State.UPDATE.equals(state.peek()))
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
    return isCreateState() && !isCreateWithinUpdate();
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
}
