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
package com.runwaysdk.dataaccess.io.instance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeBlobIF;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.AttributeSymmetricIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.util.Base64;

/**
 * This class performs a full export of Runway object instances (and their attributes) using our custom XML syntax.
 */
public abstract class InstanceExporter
{
  /**
   * Writes the XML code
   */
  private MarkupWriter                 writer;

  /**
   * The location of the schema file
   */
  private String                       schemaLocation;

  /**
   * True if only modified attributes should be exported, false otherwise.
   */
  private boolean                      exportOnlyModifiedAttributes;
  
  /**
   * Include system attributes in the export?
   */
  private boolean                      exportSystemAttrs = true;

  /**
   * A list of attribute names that are masked out of the export file
   */
  private static final TreeSet<String> ATTRIBUTE_MASK = InstanceExporter.getAttributeMask();
  
  private static final TreeSet<String> ATTRIBUTE_WHITELIST = new TreeSet<String>();

  /**
   * Constructor Creates an xml file called file_name
   * 
   * @param _writer
   * 
   * @param schemaLocation
   *          The location of the schema file
   * 
   * @param _exportOnlyModifiedAttributes
   *          True if only modified attributes should be exported, false
   *          otherwise.
   */
  protected InstanceExporter(MarkupWriter _writer, String _schemaLocation, boolean _exportOnlyModifiedAttributes)
  {
    this.writer = _writer;
    this.schemaLocation = _schemaLocation;
    this.exportOnlyModifiedAttributes = _exportOnlyModifiedAttributes;
  }
  
  protected InstanceExporter(MarkupWriter _writer, String _schemaLocation, boolean _exportOnlyModifiedAttributes, boolean exportSystemAttributes)
  {
    this.writer = _writer;
    this.schemaLocation = _schemaLocation;
    this.exportOnlyModifiedAttributes = _exportOnlyModifiedAttributes;
    exportSystemAttrs = exportSystemAttributes;
  }

  /**
   * Returns a reference to the markup writer.
   * 
   * @return reference to the markup writer.
   */
  protected MarkupWriter getWriter()
  {
    return this.writer;
  }

  /**
   * Returns the schema location.
   * 
   * @return the schema location.
   */
  protected String getSchemaLocation()
  {
    return this.schemaLocation;
  }

  public boolean isExportOnlyModifiedAttributes()
  {
    return exportOnlyModifiedAttributes;
  }
  
  /**
   * Used to not export certain attributes.
   */
  public void blacklistAttributes(Collection<String> attributeNames) {
    ATTRIBUTE_MASK.addAll(attributeNames);
  }
  
  public void whitelistAttributes(Collection<String> attributeNames) {
    ATTRIBUTE_WHITELIST.addAll(attributeNames);
  }

  public void open()
  {
    HashMap<String, String> attributes = new HashMap<String, String>();

    attributes.put("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE);
    attributes.put("xsi:noNamespaceSchemaLocation", this.getSchemaLocation());

    this.getWriter().openEscapedTag("Instances", attributes);
  }

  public void close()
  {
    this.getWriter().closeTag();
  }

  /**
   * Exports a relationship
   * 
   * @pre relationship != null
   * 
   * @param DAOIF
   */
  protected void exportRelationship(RelationshipDAOIF relationshipDAOIF)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    String type = relationshipDAOIF.getType();
    String oid = relationshipDAOIF.getOid();

    // Add the type, oid, parent_id, and child_id to the attributes of the
    // <relationship> tag
    parameters.put(XMLTags.TYPE_TAG, type);
    parameters.put(XMLTags.ID_TAG, oid);
    parameters.put(XMLTags.PARENT_ID_TAG, relationshipDAOIF.getParentId());
    parameters.put(XMLTags.CHILD_ID_TAG, relationshipDAOIF.getChildId());

    writer.openEscapedTag(XMLTags.RELATIONSHIP_TAG, parameters);

    // Write the attribute tags of the relationship
    exportAttributes(relationshipDAOIF.isNew(), relationshipDAOIF.isAppliedToDB(), relationshipDAOIF.getAttributeArrayIF());

    writer.closeTag();
  }

  /**
   * Exports a BusinessDAO
   * 
   * @pre businessDAOIF != null
   * 
   * @param businessDAOIF
   *          The data object to export
   */
  protected void exportBusinessDAO(BusinessDAOIF businessDAOIF)
  {
    HashMap<String, String> parameters = new HashMap<String, String>();

    String typeName = businessDAOIF.getType();
    String oid = businessDAOIF.getOid();

    // Add the type and oid to the attributes the <instance> tag
    parameters.put(XMLTags.TYPE_TAG, typeName);
    parameters.put(XMLTags.ID_TAG, oid);

    writer.openEscapedTag(XMLTags.OBJECT_TAG, parameters);

    // Write the attribute tags of the BusinessDAO
    exportAttributes(businessDAOIF.isNew(), businessDAOIF.isAppliedToDB(), businessDAOIF.getAttributeArrayIF());

    writer.closeTag();
  }

  /**
   * Exports a list of queries
   * 
   * @param queries
   */
  public void export(List<ComponentQuery> queries)
  {
    for (ComponentQuery query : queries)
    {
      OIterator<? extends ComponentIF> iterator = query.getIterator();

      // Get export all items of a given queries
      while (iterator.hasNext())
      {
        ComponentIF component = iterator.next();
        
        export(component.getOid());
      }

      iterator.close();
    }
  }

  /**
   * Exports an entity who's oid is in the result set.
   * 
   * @param resultSet
   *          contains three columns: EntityInfo.OID - A string and primary key.
   * 
   */
  public void export(ResultSet resultSet)
  {
    try
    {
      while (resultSet.next())
      {
        String oid = resultSet.getString(EntityInfo.OID);

        export(oid);
      }
    }
    catch (SQLException e)
    {
      Database.throwDatabaseException(e);
    }
  }

  /**
   * Exports an Entity.
   * 
   * @param oid
   *          The oid of the Entity.
   */
  public void export(String oid)
  {
    // Get the entityDAO
    EntityDAOIF entityDAO = EntityDAO.get(oid);

    this.export(entityDAO);
  }

  /**
   * Exports an Entity.
   * 
   * @param entityDAOIF
   *          Entity to export.
   */
  public void export(EntityDAOIF entityDAOIF)
  {
    if (entityDAOIF instanceof BusinessDAOIF)
    {
      BusinessDAOIF businessDAO = (BusinessDAOIF) entityDAOIF;

      exportBusinessDAO(businessDAO);
    }
    else if (entityDAOIF instanceof RelationshipDAOIF)
    {
      RelationshipDAOIF relationship = (RelationshipDAOIF) entityDAOIF;

      exportRelationship(relationship);
    }
  }

  /**
   * Exports attribute-value mappings to the xml document. Does not export
   * attributes defined in the ATTRIBUTE_MASK
   * 
   * @param appliedToDB
   *          TODO
   * @param attributes
   *          An array of attributes to export
   * 
   * @param true if the attributes come from a new component, false otherwise.
   */
  private void exportAttributes(boolean isNewComponent, boolean appliedToDB, AttributeIF[] attributes)
  {
    // Get all attributes of the entity
    // exportOnlyModifiedAttributes
    for (AttributeIF attributeIF : attributes)
    {
      if (! ( ( isNewComponent && !appliedToDB ) || attributeIF.isModified() || !this.isExportOnlyModifiedAttributes() ))
      {
        continue;
      }
      
      String attributeName = attributeIF.getName();
      String attributeValue = attributeIF.getValue();
      
      // Do not export system attributes
      if (!ATTRIBUTE_MASK.contains(attributeName) && !(attributeIF.getMdAttribute().isSystem() && !(exportSystemAttrs) && !(ATTRIBUTE_WHITELIST.contains(attributeName))))
      {
        if (attributeIF instanceof AttributeStructIF)
        {
          HashMap<String, String> parameters = new HashMap<String, String>();
          
          AttributeStructIF attributeStructIF = (AttributeStructIF) attributeIF;
          
          parameters.put(XMLTags.ATTRIBUTE_TAG, attributeName);
          parameters.put(XMLTags.ID_TAG, attributeStructIF.getStructDAO().getOid());
          
          // write the selection tag
          writer.openEscapedTag(XMLTags.STRUCT_REF_TAG, parameters);
          
          boolean isNew = attributeStructIF.getStructDAO().isNew();
          AttributeIF[] attributeArrayIF = attributeStructIF.getAttributeArrayIF();
          
          exportAttributes(isNew, appliedToDB, attributeArrayIF);
          
          // Close the selection tag
          writer.closeTag();
        }
        // The attribute is a reference to multiple instances
        else if (attributeIF instanceof AttributeEnumerationIF)
        {
          HashMap<String, String> parameters = new HashMap<String, String>();
          
          parameters.put(XMLTags.ATTRIBUTE_TAG, attributeName);

          // write the selection tag
          writer.openEscapedTag(XMLTags.ENUMERATION_TAG, parameters);

          // Get all of the enumeration IDs the attribute references
          AttributeEnumerationIF enumeration = (AttributeEnumerationIF) attributeIF;

          Set<String> enumIds = enumeration.getEnumItemIdList();

          for (String enumId : enumIds)
          {
            HashMap<String, String> enumParam = new HashMap<String, String>();

            enumParam.put(XMLTags.ATTRIBUTE_VALUE_TAG, enumId);

            // write instance_ref tag
            writer.writeEmptyEscapedTag(XMLTags.VALUE_TAG, enumParam);
          }

          // Close the selection tag
          writer.closeTag();
        }
        else if (attributeIF instanceof AttributeBlobIF)
        {
          HashMap<String, String> parameters = new HashMap<String, String>();

          AttributeBlobIF blob = (AttributeBlobIF) attributeIF;

          String hex = Base64.encodeToString(blob.getBlobAsBytes(), false);
          parameters.put(XMLTags.ATTRIBUTE_TAG, attributeName);

          writer.openEscapedTag(XMLTags.VALUE_TAG, parameters);
          writer.writeCData(hex);
          writer.closeTag();
        }
        else if (attributeIF instanceof AttributeSymmetricIF)
        {
          HashMap<String, String> parameters = new HashMap<String, String>();

          String hex = Base64.encodeToString(attributeIF.getRawValue().getBytes(), false);

          parameters.put(XMLTags.ATTRIBUTE_TAG, attributeName);

          writer.openEscapedTag(XMLTags.VALUE_TAG, parameters);
          writer.writeCData(hex);
          writer.closeTag();
        }
        else
        {
          HashMap<String, String> parameters = new HashMap<String, String>();

          // load parameters
          parameters.put(XMLTags.ATTRIBUTE_TAG, attributeName);

          writer.openEscapedTag(XMLTags.VALUE_TAG, parameters);
          writer.writeCData(attributeValue);
          writer.closeTag();
        }
      }
    }
  }

  /**
   * Returns the list of attributes which need to be masked
   * 
   * @return
   */
  private static TreeSet<String> getAttributeMask()
  {
    TreeSet<String> tree = new TreeSet<String>();

    tree.add(EntityInfo.OID);
    tree.add(EntityInfo.TYPE);

    return tree;
  }
}
