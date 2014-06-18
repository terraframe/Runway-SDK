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
package com.runwaysdk.dataaccess.schemamanager.model;

import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdControllerDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdExceptionDAO;
import com.runwaysdk.dataaccess.metadata.MdInformationDAO;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.MdWarningDAO;
import com.runwaysdk.dataaccess.schemamanager.xml.UnKeyedElement;

public class KeyResolver implements SchemaVisitor<String>
{

  public String visit(SchemaClass mdClass)
  {
    String key = null;
    if (mdClass.getTag().equals(XMLTags.MD_BUSINESS_TAG) || mdClass.getTag().equals(XMLTags.MD_TERM_TAG) || mdClass.getTag().equals(XMLTags.ENUMERATION_MASTER_TAG))
    {
      key = MdBusinessDAO.buildKey(mdClass.getAttributes().get(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_CONTROLLER_TAG))
    {
      key = MdControllerDAO.buildKey(mdClass.getAttributes().get(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_WEB_FORM_TAG))
    {
      key = MdControllerDAO.buildKey(mdClass.getAttributes().get(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_VIEW_TAG))
    {
      key = MdViewDAO.buildKey(mdClass.getAttributes().get(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_EXCEPTION_TAG))
    {
      key = MdExceptionDAO.buildKey(mdClass.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_PROBLEM_TAG))
    {
      key = MdProblemDAO.buildKey(mdClass.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_STRUCT_TAG))
    {
      key = MdStructDAO.buildKey(mdClass.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_UTIL_TAG))
    {
      key = MdUtilDAO.buildKey(mdClass.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_INFORMATION_TAG))
    {
      key = MdInformationDAO.buildKey(mdClass.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
    }
    else if (mdClass.getTag().equals(XMLTags.MD_WARNING_TAG))
    {
      key = MdWarningDAO.buildKey(mdClass.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
    }

    return key;
  }

  public String visit(SchemaAttribute mdAttribute)
  {
    String attributeName = mdAttribute.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE);

    if (attributeName == null || attributeName.equals(""))
    {
      // IMPORTANT: The key on virtual attributes is mdClass + concrete not
      // mdClass + attributeName

      attributeName = mdAttribute.getXMLAttributeValue(XMLTags.CONCRETE_ATTRIBUTE);
    }

    KeyedElementIF ancestor = SchemaElement.getAncestor(mdAttribute, KeyedElementIF.class);

    return MdAttributeDAO.buildKey(ancestor.getKey(), attributeName);
  }

  public String visit(KeyedElement element)
  {
    String key = element.getXMLAttributeValue(XMLTags.KEY_ATTRIBUTE);
    String name = element.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE);
    String concrete = element.getXMLAttributeValue(XMLTags.CONCRETE_ATTRIBUTE);
    String attribute = ( name != null ? name : concrete );

    if (key != null)
    {
      return key;
    }
    else if (attribute != null)
    {
      KeyedElementIF ancestor = SchemaElement.getAncestor(element, KeyedElementIF.class);

      if (ancestor != null)
      {
        String ancestorKey = ancestor.getKey();

        if (ancestorKey != null && !ancestorKey.equals(""))
        {
          return ancestorKey + "." + attribute;
        }
      }

      return attribute;
    }

    String msg = "Key cannot be resolved for tag [" + element.getTag() + "]";

    throw new RuntimeException(msg);
  }

  public String visit(SchemaRelationship relationship)
  {
    return MdRelationshipDAO.buildKey(relationship.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
  }

  public String visit(MergeSchema schema)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public String visit(SchemaObject instanceElement)
  {
    String type = instanceElement.getXMLAttributeValue(XMLTags.TYPE_ATTRIBUTE);
    String key = instanceElement.getXMLAttributeValue(XMLTags.KEY_ATTRIBUTE);

    if (type.contains(Constants.METADATA_PACKAGE))
    {
      return key;
    }

    return type + "." + key;
  }

  public String visit(SchemaRelationshipParticipant relationshipEnd)
  {
    return relationshipEnd.getParent().accept(this) + "." + relationshipEnd.getTag();
  }

  public String visit(SchemaEnumeration schemaEnumeration)
  {
    return MdEnumerationDAO.buildKey(schemaEnumeration.getXMLAttributeValue(XMLTags.NAME_ATTRIBUTE));
  }

  public String visit(EnumItemModification enumItemModification)
  {
    return enumItemModification.getXMLAttributeValue(XMLTags.ENUM_NAME_ATTRIBUTE);
  }

  public String visit(PermissionHolder permissionHolder)
  {
    if (permissionHolder.getTag().equals(XMLTags.USER_TAG))
    {
      return UserDAO.buildKey(permissionHolder.getXMLAttributeValue(XMLTags.USERNAME_ATTRIBUTE));
    }
    else if (permissionHolder.getTag().equals(XMLTags.ROLE_TAG))
    {
      return RoleDAO.buildKey(permissionHolder.getXMLAttributeValue(XMLTags.ROLENAME_ATTRIBUTE));
    }
    else if (permissionHolder.getTag().equals(XMLTags.METHOD_TAG))
    {
      return MethodActorDAO.buildKey(permissionHolder.getXMLAttributeValue(XMLTags.TYPE_ATTRIBUTE), permissionHolder.getXMLAttributeValue(XMLTags.METHOD_NAME_ATTRIBUTE));
    }
    else
    {
      return null;
    }
  }

  public String visit(SchemaIndex element)
  {
    throw new RuntimeException("SchmemaIndex does not have a key");
  }

  public String visit(UnKeyedElement element)
  {
    throw new RuntimeException("UnKeyedElement does not have a key");
  }

  public String visit(PermissionElement element)
  {
    String key = element.getObjectKey();

    if (key != null)
    {
      KeyedElementIF ancestor = SchemaElement.getAncestor(element, KeyedElementIF.class);

      if (ancestor != null)
      {
        String ancestorKey = ancestor.getKey();

        if (!ancestorKey.equals(""))
        {
          return ancestorKey + "." + key;
        }
      }

      return key;
    }

    return "";
  }

  @Override
  public String visit(NullElement element)
  {
    return element.getKey();
  }

}
