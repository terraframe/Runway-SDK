/**
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
 */
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.dataaccess.io.TimeFormat;
import com.runwaysdk.dataaccess.io.dataDefinition.TimestampHandler;
import com.runwaysdk.dataaccess.io.dataDefinition.TimestampHandler.Action;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.EnumItemModification;
import com.runwaysdk.dataaccess.schemamanager.model.IndexAttribute;
import com.runwaysdk.dataaccess.schemamanager.model.KeyedElement;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.NullElement;
import com.runwaysdk.dataaccess.schemamanager.model.PermissionElement;
import com.runwaysdk.dataaccess.schemamanager.model.PermissionHolder;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaAttribute;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaClass;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaEnumeration;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaIndex;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaObject;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaRelationship;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaRelationshipParticipant;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaVisitor;

public class SchemaExportVisitor implements SchemaVisitor<Void>
{
  private FileMarkupWriter      xmlWriter;

  private XSDConstraintsManager xsdConstraints;

  private String                xsdLocation;

  private MergeSchema           doItSchema;

  private MergeSchema           undoItSchema;

  public SchemaExportVisitor(String destinationFilePath, String xsdLocation, MergeSchema doIt, MergeSchema undoIt)
  {
    this(destinationFilePath, xsdLocation);

    this.doItSchema = doIt;
    this.undoItSchema = undoIt;
  }

  public SchemaExportVisitor(String destinationFilePath, String xsdLocation)
  {
    this.xmlWriter = new FileMarkupWriter(destinationFilePath);
    this.xsdConstraints = new XSDConstraintsManager(xsdLocation);
    this.xsdLocation = xsdLocation;
  }

  public void export()
  {
    xmlWriter.openTagln(XMLTags.VERSION_TAG, buildVersionAttributes());

    xmlWriter.openTag(XMLTags.DO_IT_TAG);
    doItSchema.accept(this);
    this.writeTimestamps(TimestampHandler.Action.CREATE, doItSchema.getTimestamps());
    xmlWriter.closeTag();

    xmlWriter.openTag(XMLTags.UNDO_IT_TAG);
    undoItSchema.accept(this);
    this.writeTimestamps(TimestampHandler.Action.DELETE, doItSchema.getTimestamps());
    xmlWriter.closeTag();

    xmlWriter.closeTag();
  }

  private void writeTimestamps(Action action, List<Long> timestamps)
  {
    xmlWriter.openTag(action.getTag());

    for (Long timestamp : timestamps)
    {
      if (timestamp != null)
      {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(XMLTags.VERSION_TAG, new TimeFormat(timestamp).format());

        xmlWriter.writeEmptyTag(XMLTags.TIMESTAMP_TAG, map);
      }
    }

    xmlWriter.closeTag();
  }

  public Void visit(MergeSchema schema)
  {
    List<UnKeyedElement> roots = schema.getRootElements();

    for (UnKeyedElement root : roots)
    {
      if (root.hasChildren())
      {
        root.accept(this);
      }
    }

    return null;
  }

  public Void visit(SchemaClass mdClass)
  {
    xmlWriter.openTagln(mdClass.getTag(), (HashMap<String, String>) mdClass.getAttributes());

    if (!mdClass.children().isEmpty())
    {
      visitChildren(mdClass);
    }

    xmlWriter.closeTag();
    return null;

  }

  public Void visit(SchemaAttribute mdAttribute)
  {
    xmlWriter.writeEmptyEscapedTag(mdAttribute.getTag(), (HashMap<String, String>) mdAttribute.getAttributes());
    return null;

  }

  public Void visit(KeyedElement element)
  {
    if (!element.children().isEmpty())
    {
      xmlWriter.openTagln(element.getTag(), (HashMap<String, String>) element.getAttributes());
      visitChildren(element);
      xmlWriter.closeTag();
    }
    else
    {
      xmlWriter.writeEmptyEscapedTag(element.getTag(), (HashMap<String, String>) element.getAttributes());
    }

    return null;
  }

  public Void visit(SchemaRelationship relationship)
  {
    xmlWriter.openTagln(relationship.getTag(), (HashMap<String, String>) relationship.getAttributes());

    visitChildren(relationship);
    relationship.relationshipParent().accept(this);
    relationship.relationshipChild().accept(this);

    xmlWriter.closeTag();
    return null;
  }

  private HashMap<String, String> buildVersionAttributes()
  {
    HashMap<String, String> versionAttributes = new HashMap<String, String>();
    versionAttributes.put("xsi:noNamespaceSchemaLocation", xsdLocation);
    versionAttributes.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    return versionAttributes;
  }

  private void visitChildren(SchemaElementIF schemaElement)
  {
    // make a list out of the set of children
    List<SchemaElementIF> childrenAsList = new ArrayList<SchemaElementIF>(schemaElement.children());

    // Sort the children as they appear in the xsd file

    Collections.sort(childrenAsList, xsdConstraints.childComparator(schemaElement));

    for (SchemaElementIF child : childrenAsList)
    {
      child.accept(this);
    }
  }

  private void visitChildrenIgnoreOrder(SchemaElementIF schemaElement)
  {
    for (SchemaElementIF child : schemaElement.children())
    {
      child.accept(this);
    }
  }

  public Void visit(SchemaObject instanceElement)
  {

    if (!instanceElement.children().isEmpty())
    {
      xmlWriter.openEscapedTag(instanceElement.getTag(), (HashMap<String, String>) instanceElement.getAttributes());
      visitChildrenIgnoreOrder(instanceElement);
      xmlWriter.closeTag();
    }
    else
    {
      xmlWriter.writeEmptyEscapedTag(instanceElement.getTag(), (HashMap<String, String>) instanceElement.getAttributes());
    }

    return null;
  }

  public Void visit(SchemaRelationshipParticipant relationshipEnd)
  {
    xmlWriter.writeEmptyEscapedTag(relationshipEnd.getTag(), (HashMap<String, String>) relationshipEnd.getAttributes());
    return null;
  }

  public Void visit(SchemaEnumeration schemaEnumeration)
  {
    xmlWriter.openTagln(schemaEnumeration.getTag(), (HashMap<String, String>) schemaEnumeration.getAttributes());
    if (schemaEnumeration.doesIncludeAll())
    {
      xmlWriter.writeEmptyTag(XMLTags.INCLUDEALL_TAG);
    }

    else
    {
      for (EnumItemModification enumItemModification : schemaEnumeration.enumItemAdditions())
      {
        enumItemModification.accept(this);
      }

      for (EnumItemModification enumItemModification : schemaEnumeration.enumItemDeletions())
      {
        enumItemModification.accept(this);
      }
    }
    xmlWriter.closeTag();
    return null;
  }

  public Void visit(EnumItemModification enumItemModification)
  {
    xmlWriter.writeEmptyEscapedTag(enumItemModification.getTag(), (HashMap<String, String>) enumItemModification.getAttributes());
    return null;
  }

  public Void visit(PermissionHolder permissionHolder)
  {
    if (!permissionHolder.children().isEmpty())
    {
      xmlWriter.openTagln(permissionHolder.getTag(), (HashMap<String, String>) permissionHolder.getAttributes());
      visitChildren(permissionHolder);
      xmlWriter.closeTag();
    }
    else
      xmlWriter.writeEmptyEscapedTag(permissionHolder.getTag(), (HashMap<String, String>) permissionHolder.getAttributes());
    return null;
  }

  public Void visit(SchemaIndex schemaIndex)
  {
    xmlWriter.openTag(schemaIndex.getTag(), (HashMap<String, String>) schemaIndex.getAttributes());
    if (!schemaIndex.indexAttributes().isEmpty())
    {
      for (IndexAttribute indexAttribute : schemaIndex.indexAttributes())
      {
        xmlWriter.writeEmptyEscapedTag(XMLTags.INDEX_ATTRIBUTE_TAG, (HashMap<String, String>) indexAttribute.getAttributes());
      }
    }
    xmlWriter.closeTag();

    return null;
  }

  public Void visit(UnKeyedElement element)
  {
    if (!element.children().isEmpty())
    {
      xmlWriter.openTagln(element.getTag(), (HashMap<String, String>) element.getAttributes());
      visitChildren(element);
      xmlWriter.closeTag();
    }
    else
    {
      xmlWriter.writeEmptyEscapedTag(element.getTag(), (HashMap<String, String>) element.getAttributes());
    }

    return null;
  }

  public Void visit(PermissionElement element)
  {
    return this.visit((KeyedElement) element);
  }

  @Override
  public Void visit(NullElement element)
  {
    // DO NOTHING THIS IS A NULL ELEMENT

    return null;
  }

}
