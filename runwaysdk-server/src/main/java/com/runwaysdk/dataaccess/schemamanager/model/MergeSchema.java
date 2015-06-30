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
package com.runwaysdk.dataaccess.schemamanager.model;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.xml.sax.Attributes;

import com.runwaysdk.business.generation.view.AbstractViewGenerator;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.TimestampFile;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.xml.CreateElement;
import com.runwaysdk.dataaccess.schemamanager.xml.UnKeyedElement;

/**
 * 
 * A model of the xml schema produced after merging
 * 
 * @author Aritra
 * 
 */
public class MergeSchema extends ImportManager implements SchemaVisitable, ElementListener
{
  /**
   * Cache of element nodes
   */
  private ElementCache                  elementCache;

  private Map<String, PermissionHolder> permissionCache;

  /**
   * List of timestamp files which were used to produce the merged model
   */
  private List<Long>                    timestamps;

  /**
   * The create element in the merged schema that merges all the updates and
   * creates in the individual files, whenever possible
   */
  private CreateElement                 createElement;

  /**
   * The update element in the merged schema that merges all the updates
   */
  private UnKeyedElement                updateElement;

  /**
   * The top level permission element
   */
  private UnKeyedElement                permissionElement;

  /**
   * Groups of delete/relationship elements enclosed within update and create
   * elements. Their semantic impact on the model being built is not considered.
   * They are just listed in the merged file in the order they were encountered
   */
  private LinkedList<UnKeyedElement>    roots;

  /**
   * A set of observers that observes any changes in the model being built
   */
  private Set<SchemaObserver>           observers;

  /**
   * List of SchemaClass keys which were deleted
   */
  private Set<String>                   deletedTypes;

  public MergeSchema(StreamSource source, String schemaLocation)
  {
    super(source, schemaLocation);

    this.initialize();
  }

  public MergeSchema(String schemaLocation)
  {
    super(schemaLocation);

    this.initialize();
  }

  private final void initialize()
  {
    this.elementCache = new ElementCache();
    this.roots = new LinkedList<UnKeyedElement>();
    this.permissionCache = new HashMap<String, PermissionHolder>();
    this.observers = new HashSet<SchemaObserver>();
    this.timestamps = new LinkedList<Long>();
    this.deletedTypes = new TreeSet<String>();

    this.createElement = new CreateElement();
    this.updateElement = new UnKeyedElement(null, XMLTags.UPDATE_TAG, null);
    this.permissionElement = new UnKeyedElement(null, XMLTags.PERMISSIONS_TAG, null);

    this.roots.add(createElement);
    this.roots.add(updateElement);
  }

  public SchemaElementIF getElement(String key)
  {
    if (elementCache.containsKey(key))
    {
      return elementCache.get(key);
    }
    return null;
  }

  public boolean containsElement(String key)
  {
    return elementCache.containsKey(key);
  }

  @Override
  public void addImportedType(String type)
  {
    throw new UnsupportedOperationException("Not a valid operation for Schema");
  }

  public List<UnKeyedElement> getRootElements()
  {
    List<UnKeyedElement> list = new LinkedList<UnKeyedElement>(roots);
    list.add(permissionElement);

    return list;
  }

  public KeyedElement createParameter(Attributes attributes, String tag, SchemaElementIF mdMethod)
  {
    KeyedElement element = new KeyedElement(attributes, tag, mdMethod);

    if (elementCache.containsKey(element.getKey()))
    {
      KeyedElement existing = (KeyedElement) elementCache.get(element.getKey());
      existing.addAttributesWithReplacement(attributes);

      return existing;
    }

    this.addKeyedElement(element);

    return element;
  }

  public SchemaClass createSchemaClass(Attributes attributes, String tag)
  {
    String classKey = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    SchemaClass schemaClass;

    if (elementCache.containsKey(classKey))
    {
      schemaClass = (SchemaClass) elementCache.get(classKey);
      schemaClass.addAttributesWithReplacement(attributes);
    }
    else
    {
      schemaClass = new SchemaClass(attributes, tag);
      schemaClass.setParent(createElement);
      schemaClass.registerListener(this);

      this.addKeyedElement(schemaClass);
    }

    return schemaClass;
  }

  public KeyedElement createSchemaMethod(Attributes attributes, String tag, SchemaClass mdClass)
  {
    KeyedElement mdMethod = new KeyedElement(attributes, tag, mdClass);

    String key = mdMethod.getKey();

    if (elementCache.containsKey(key))
    {
      mdMethod = (KeyedElement) elementCache.get(key);
      mdMethod.addAttributesWithReplacement(attributes);
    }
    else
    {
      this.addKeyedElement(mdMethod);
    }
    return mdMethod;
  }

  public SchemaRelationship createSchemaRelationship(Attributes attributes, String tag)
  {
    SchemaRelationship relationship = new SchemaRelationship(attributes, tag);

    if (elementCache.containsKey(relationship.getKey()))
    {
      relationship = (SchemaRelationship) elementCache.get(relationship.getKey());
      relationship.addAttributesWithReplacement(attributes);
    }
    else
    {
      relationship.setParent(createElement);
      relationship.registerListener(this);

      this.addKeyedElement(relationship);
    }

    return relationship;
  }

  public KeyedElement createNonRenderableElement(Attributes attributes, String tag)
  {
    KeyedElement nonRenderableElement = new KeyedElement(attributes, tag);
    return nonRenderableElement;
  }

  public SchemaEnumeration createSchemaEnumeration(Attributes attributes, String tag)
  {
    String key = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    SchemaEnumeration enumeration;

    if (elementCache.containsKey(key))
    {
      enumeration = (SchemaEnumeration) elementCache.get(key);
      enumeration.addAttributesWithReplacement(attributes);
    }
    else
    {
      enumeration = new SchemaEnumeration(attributes, tag);
      enumeration.setParent(createElement);

      this.addKeyedElement(enumeration);
      this.registerDependency(enumeration);
    }

    return enumeration;
  }

  /**
   * Creates a partial element. Checks if there is any other partial element
   * that specifies an update to the same target element. If such an element is
   * found the newly created partial element is merged with the exisiting one
   * and the merged element is returned.
   * 
   * @param element
   * @return {@link PartialSchemaElement}
   */
  public SchemaElementIF createPartialSchemaElement(KeyedElementIF element)
  {
    // This is a partial element, as such its parent must be in the update tag
    // instead of the create tag
    element.remove();

    updateElement.addChild(element);

    return this.createKeyedElement(element);
  }

  public PermissionHolder createPermissionHolder(Attributes attributes, String tag)
  {
    PermissionHolder element = new PermissionHolder(attributes, tag);
    String key = element.getKey();

    if (permissionCache.containsKey(key))
    {
      return permissionCache.get(key);
    }

    permissionElement.addChild(element);
    permissionCache.put(key, element);

    return element;
  }

  /**
   * Returns a new schema index as a child of the root create element. Note that
   * it is impossible ot merge SchemaIndex elements because they do not have a
   * key.
   * 
   * @param attributes
   *          Attributes of the SchemaIndex
   * @param localName
   *          Name of the SchemaIndex tag
   * 
   * @return
   */
  public SchemaIndex createSchemaIndex(Attributes attributes, String localName)
  {
    SchemaIndex schemaIndex = new SchemaIndex(attributes, localName, createElement);

    this.registerDependency(schemaIndex);

    return schemaIndex;
  }

  public CreateElement addCreateGroup()
  {
    if (roots.size() > 0 && roots.getLast() instanceof CreateElement)
    {
      return (CreateElement) roots.getLast();
    }

    CreateElement root = new CreateElement();
    roots.add(root);

    return root;
  }

  public UnKeyedElement addDeleteGroup()
  {
    if (roots.size() > 0 && roots.getLast().getTag().equals(XMLTags.DELETE_TAG))
    {
      return (UnKeyedElement) roots.getLast();
    }

    UnKeyedElement root = new UnKeyedElement(null, XMLTags.DELETE_TAG, null);
    roots.add(root);

    return root;
  }

  public UnKeyedElement addUpdateGroup()
  {
    if (roots.size() > 0 && roots.getLast().getTag().equals(XMLTags.UPDATE_TAG))
    {
      return (UnKeyedElement) roots.getLast();
    }

    UnKeyedElement root = new UnKeyedElement(null, XMLTags.UPDATE_TAG, null);
    roots.add(root);

    return root;
  }

  public SchemaRelationshipParticipant createRelationshipParticipant(Attributes attributes, String tag, String type, SchemaRelationship relationship)
  {
    SchemaRelationshipParticipant participant = new SchemaRelationshipParticipant(attributes, tag, type, relationship);

    String key = participant.getKey();

    if (elementCache.containsKey(key))
    {
      participant = (SchemaRelationshipParticipant) elementCache.get(key);
    }
    else
    {
      elementCache.get(participant.getType()).registerListener(relationship);

      this.addKeyedElement(participant);
    }

    return participant;
  }

  @Override
  public boolean isCreated(String keyname)
  {
    return elementCache.containsKey(keyname);
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);

  }

  public void addObserver(SchemaObserver schemaObserver)
  {
    observers.add(schemaObserver);
  }

  public Set<SchemaObserver> observers()
  {
    return observers;
  }

  public void addToCreateElement(SchemaElement element)
  {
    createElement.addChild(element);
  }

  public void removeElement(PermissionHolder element)
  {
    permissionCache.remove(element.getKey());
    element.remove();
  }

  public Collection<PermissionHolder> permissionHolders()
  {
    return permissionCache.values();
  }

  public void changeFile(StreamSource source)
  {
    this.source = source;
  }

  public boolean containsPermission(String permissionHolderID)
  {
    return permissionCache.containsKey(permissionHolderID);
  }

  public void notifyObserversClassCreated(SchemaClass schemaClass)
  {
    for (SchemaObserver observer : observers)
    {
      if (observer.isAcceptable(schemaClass))
      {
        observer.notifyClassCreated(schemaClass);
      }
    }
  }

  public void notifyObserversRelationshipCreated(SchemaRelationship relationship)
  {
    for (SchemaObserver observer : observers)
    {
      if (observer.isAcceptable(relationship))
      {
        observer.notifyRelationshipCreated(relationship);
      }
    }
  }

  public void addTimestamp(File file)
  {
    this.timestamps.add(new TimestampFile(file).getTimestamp());
  }

  public List<Long> getTimestamps()
  {
    return this.timestamps;
  }

  public void addKeyedElement(KeyedElementIF element)
  {
    this.addElement(element.getKey(), element);
  }

  private void addElement(String key, KeyedElementIF element)
  {
    elementCache.put(key, element);
  }

  public void addSchemaObject(SchemaObject object)
  {
    elementCache.putSchemaObject(object.getKey(), object);
  }

  public boolean containsSchemaObjects(String key)
  {
    return elementCache.containsSchemaObject(key);
  }

  public List<SchemaObject> getSchemaObjects(String key)
  {
    return elementCache.getSchemaObjects(key);
  }

  public SchemaElementIF createKeyedElement(KeyedElementIF element)
  {
    // Attempt to find the element in the list of children of the parent
    // element. The parent element is retrieved from the parent handler.
    String key = element.getKey();

    SchemaElementIF keyed = this.getElement(key);

    if (keyed != null && keyed instanceof KeyedElement)
    {
      KeyedElement child = (KeyedElement) keyed;

      // If the element was found in the list of children of its parent, then do
      // not create a new child element. Instead, just replace the attributes of
      // the existing elements

      child.addAttributesWithReplacement(element);
      child.setMerged(true);

      return child;
    }

    // If the child was not found in the list of children of its parent,
    // initialize a new child.
    this.addElement(key, element);

    this.registerDependency(element);
    element.registerListener(this);

    return element;
  }

  public void registerDependency(SchemaElementIF element)
  {
    String[] keys = element.getElementsToObserve();

    for (String key : keys)
    {
      SchemaElementIF source = this.elementCache.get(key);

      source.registerListener(element);
    }
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      this.deletedTypes.add(event.getSource().getKey());
    }
  }

  public boolean wasDeleted(SchemaObject element)
  {
    String key = element.getKey().replace(AbstractViewGenerator.CONTROLLER_SUFFIX, "");

    return deletedTypes.contains(key);
  }
}
