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
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.util.HashSet;
import java.util.Set;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaAttribute;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaClass;

/**
 * 
 * Groups all the XML tags into classes that are logically different from the
 * perspective of the schema manager.
 * 
 * @author runway
 * 
 */
public class SMXMLTags implements XMLTags
{

  /**
   * 
   * Tags that are converted to instances of {@link SchemaClass}
   */
  private static final Set<String> classTags                = new HashSet<String>();

  static
  {
    classTags.add(MD_BUSINESS_TAG);
    classTags.add(MD_TERM_TAG);
    classTags.add(MD_UTIL_TAG);
    classTags.add(MD_EXCEPTION_TAG);
    classTags.add(MD_CONTROLLER_TAG);
    classTags.add(MD_VIEW_TAG);
    classTags.add(MD_FACADE_TAG);
    classTags.add(ENUMERATION_MASTER_TAG);
    classTags.add(MD_PROBLEM_TAG);
    classTags.add(MD_STRUCT_TAG);
    classTags.add(MD_INFORMATION_TAG);
    classTags.add(MD_WARNING_TAG);
    classTags.add(MD_WEB_FORM_TAG);
  }

  /**
   * Tags that are converted to instances of {@link SchemaAttribute}
   */
  private static final Set<String> attributeTypeTags        = new HashSet<String>();

  static
  {
    attributeTypeTags.add(CHARACTER_TAG);
    attributeTypeTags.add(TEXT_TAG);
    attributeTypeTags.add(CLOB_TAG);
    attributeTypeTags.add(BOOLEAN_TAG);
    attributeTypeTags.add(INTEGER_TAG);
    attributeTypeTags.add(LONG_TAG);
    attributeTypeTags.add(FLOAT_TAG);
    attributeTypeTags.add(DOUBLE_TAG);
    attributeTypeTags.add(DECIMAL_TAG);
    attributeTypeTags.add(TIME_TAG);
    attributeTypeTags.add(DATETIME_TAG);
    attributeTypeTags.add(DATE_TAG);
    attributeTypeTags.add(ENUMERATION_TAG);
    attributeTypeTags.add(MULTI_REFERENCE_TAG);
    attributeTypeTags.add(REFERENCE_TAG);
    attributeTypeTags.add(STRUCT_TAG);
    attributeTypeTags.add(HASH_TAG);
    attributeTypeTags.add(SYMMETRIC_TAG);
    attributeTypeTags.add(BLOB_TAG);
    attributeTypeTags.add(FILE_TAG);
    attributeTypeTags.add(LOCAL_CHARACTER_TAG);
    attributeTypeTags.add(LOCAL_TEXT_TAG);
    attributeTypeTags.add(VIRTUAL_TAG);

    // These are the RunwayGIS attributes
    attributeTypeTags.add(POLYGON_TAG);
    attributeTypeTags.add(MULTIPOLYGON_TAG);
    attributeTypeTags.add(LINESTRING_TAG);
    attributeTypeTags.add(MULTILINESTRING_TAG);
    attributeTypeTags.add(POINT_TAG);
    attributeTypeTags.add(MULTIPOINT_TAG);
  }

  /**
   * Tags, appearences of which affect the state of the {@link MergeSchema}
   */
  private static final Set<String> stateTags                = new HashSet<String>();

  static
  {
    stateTags.add(CREATE_TAG);
    stateTags.add(UPDATE_TAG);
    stateTags.add(DELETE_TAG);
    // stateTags.add(OBJECT_TAG);
    stateTags.add(RELATIONSHIP_TAG);
    stateTags.add(PERMISSIONS_TAG);
  }

  private static final Set<String> relationshipTags         = new HashSet<String>();

  static
  {
    relationshipTags.add(MD_TREE_TAG);
    relationshipTags.add(MD_GRAPH_TAG);
    relationshipTags.add(MD_RELATIONSHIP_TAG);
    relationshipTags.add(MD_TERM_RELATIONSHIP_TAG);
  }

  private static final Set<String> instanceElementTags      = new HashSet<String>();

  static
  {
    instanceElementTags.add(OBJECT_TAG);
    instanceElementTags.add(RELATIONSHIP_TAG);
  }

  private static final Set<String> enumItemModificationTags = new HashSet<String>();

  static
  {
    enumItemModificationTags.add(ADD_ENUM_ITEM_TAG);
    enumItemModificationTags.add(REMOVE_ENUM_ITEM_TAG);
  }

  private static final Set<String> behavioralElementTags    = new HashSet<String>();

  static
  {
    behavioralElementTags.add(MD_METHOD_TAG);
    behavioralElementTags.add(MD_ACTION_TAG);
  }

  private static final Set<String> permissionHolderTags     = new HashSet<String>();

  static
  {
    permissionHolderTags.add(USER_TAG);
    permissionHolderTags.add(ROLE_TAG);
    permissionHolderTags.add(METHOD_TAG);
  }

  private static final Set<String> permissionActionTags     = new HashSet<String>();

  static
  {
    permissionActionTags.add(GRANT_TAG);
    permissionActionTags.add(REVOKE_TAG);
  }

  private static final Set<String> permissionTypeTags       = new HashSet<String>();

  static
  {
    permissionTypeTags.add(MD_FACADE_PERMISSION_TAG);
    permissionTypeTags.add(MD_BUSINESS_PERMISSION_TAG);
    permissionTypeTags.add(MD_STRUCT_PERMISSION_TAG);
    permissionTypeTags.add(MD_VIEW_PERMISSION_TAG);
    permissionTypeTags.add(MD_UTIL_PERMISSION_TAG);
    permissionTypeTags.add(MD_RELATIONSHIP_PERMISSION_TAG);
  }

  private static final Set<String> unkeyedTags              = new HashSet<String>();

  static
  {
    unkeyedTags.add(XMLTags.UPDATE_TAG);
    unkeyedTags.add(XMLTags.CREATE_TAG);
    unkeyedTags.add(XMLTags.DELETE_TAG);
    unkeyedTags.add(XMLTags.PERMISSIONS_TAG);
    unkeyedTags.add(XMLTags.MD_INDEX_TAG);
    unkeyedTags.add(XMLTags.ATTRIBUTE_TAG);
    unkeyedTags.add(XMLTags.ATTRIBUTES_TAG);
    unkeyedTags.add(XMLTags.FIELDS_TAG);
    unkeyedTags.add(XMLTags.SUPER_ROLE_TAG);
    unkeyedTags.add(XMLTags.STUB_SOURCE_TAG);
    unkeyedTags.add(XMLTags.DTO_STUB_SOURCE_TAG);
    unkeyedTags.add(XMLTags.INCLUDEALL_TAG);
    unkeyedTags.add(XMLTags.ADD_ENUM_ITEM_TAG);
    unkeyedTags.add(XMLTags.REMOVE_ENUM_ITEM_TAG);
    unkeyedTags.add(XMLTags.QUERY_STUB_SOURCE_TAG);
    unkeyedTags.add(XMLTags.PARENT_TAG);
    unkeyedTags.add(XMLTags.CHILD_TAG);
    unkeyedTags.add(XMLTags.STATES_TAG);
    unkeyedTags.add(XMLTags.TRANSITIONS_TAG);
    unkeyedTags.add(XMLTags.ASSIGNED_ROLE_TAG);
    unkeyedTags.add(XMLTags.GRANT_TAG);
    unkeyedTags.add(XMLTags.REVOKE_TAG);
    unkeyedTags.add(XMLTags.OPERATION_TAG);
    unkeyedTags.add(XMLTags.ATTRIBUTE_PERMISSION_TAG);
    unkeyedTags.add(XMLTags.MD_METHOD_PERMISSION_TAG);
    unkeyedTags.add(XMLTags.STATE_PERMISSION_TAG);
    unkeyedTags.add(XMLTags.PARENT_STATE_PERMISSION_TAG);
    unkeyedTags.add(XMLTags.CHILD_STATE_PERMISSION_TAG);
  }

  public static boolean isUnkeyedTag(String tag)
  {
    return unkeyedTags.contains(tag);
  }

  public static boolean isClassTag(String tag)
  {
    return classTags.contains(tag);
  }

  public static boolean isBehaviorTag(String tag)
  {
    return behavioralElementTags.contains(tag);
  }

  public static boolean isAttributeTypeTag(String tag)
  {
    return attributeTypeTags.contains(tag);
  }

  public static boolean isStateTag(String tag)
  {
    return stateTags.contains(tag);
  }

  public static boolean isInstanceElementTag(String tag)
  {
    return instanceElementTags.contains(tag);
  }

  public static boolean isRelationshipTag(String localName)
  {
    return relationshipTags.contains(localName);
  }

  public static boolean isEnumItemModificationTag(String localName)
  {
    return enumItemModificationTags.contains(localName);
  }

  public static boolean isPermissionHolderTag(String localName)
  {
    return permissionHolderTags.contains(localName);
  }

  public static boolean isPermissionActionTag(String tag)
  {
    return permissionActionTags.contains(tag);
  }

  public static boolean isPermissionTypeTag(String tag)
  {
    return permissionTypeTags.contains(tag);
  }

  public static String[] classTags()
  {
    return classTags.toArray(new String[] {});
  }
}
