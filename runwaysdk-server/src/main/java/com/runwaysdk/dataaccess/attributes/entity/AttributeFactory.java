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
/*
 * Created on Jun 9, 2005
 */
package com.runwaysdk.dataaccess.attributes.entity;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClassificationInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeGraphReferenceInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeJsonInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeUUIDInfo;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.database.Database;

/**
 * Factory for creating instances of the Attribute hierarchy.
 * 
 * @author nathan
 * 
 * @version $Revision: 1.19 $
 * @since 1.4
 */
public class AttributeFactory
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * Returns and Object with the value from the column and row.
   * 
   * @param row
   * @param columnAlias
   * @param attributeType
   * @param retrieveClobs
   *          true if values from clobs should be retrieved, false otherwise.
   * @return Object with the value from the column and row.
   */
  public static Object getColumnValueFromRow(ResultSet resultSet, String columnAlias, String attributeType, boolean retrieveClobs)
  {
    Object columnValue = null;

    String columnAliasLowerCase = columnAlias.toLowerCase();

    try
    {
      // Don't retrieve the CLOBS directly. CLOBS are fetched lazily.
      if (attributeType.equals(MdAttributeClobInfo.CLASS) && !retrieveClobs)
      {
        columnValue = "";
      }
      // Some text attributes are implemented by CLOBS on some databases
      else if (resultSet.getObject(columnAliasLowerCase) instanceof Clob)
      {
        Clob clob = (Clob) resultSet.getObject(columnAliasLowerCase);
        try
        {
          columnValue = clob.getSubString(1, (int) clob.length());
        }
        catch (SQLException ex)
        {
          Database.throwDatabaseException(ex);
        }
      }
      // Don't retrieve the blob directly. Blobs are fetched lazily.
      else if (resultSet.getObject(columnAliasLowerCase) instanceof byte[])
      {
        columnValue = "";
      }
      else
      {
        for (PluginIF plugin : pluginMap.values())
        {
          columnValue = plugin.getColumnValueFromRow(resultSet, columnAlias, attributeType);

          if (columnValue != null)
          {
            break;
          }
        }

        if (columnValue == null)
        {
          columnValue = "" + resultSet.getString(columnAliasLowerCase);
        }
      }

      if (columnValue instanceof String && columnValue.equals("null"))
      {
        columnValue = "";
      }
    }
    catch (SQLException sqlEx)
    {
      Database.throwDatabaseException(sqlEx);
    }
    return columnValue;
  }

  /**
   * Returns an Attribute object of the appropriate sub class for the given
   * DataAccess attribute class.
   * 
   * <br/>
   * <b>Precondition:</b> attributeType != null <br/>
   * <b>Precondition:</b> !attributeType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeType is a concrete sub class of
   * Constants.MD_ATTRIBUTE <br/>
   * <b>Precondition:</b> attributeName != null <br/>
   * <b>Precondition:</b> !attributeName.trim().equals("") <br/>
   * <b>Precondition:</b> definingType != null <br/>
   * <b>Precondition:</b> !definingType.trim().equals("") <br/>
   * <b>Precondition:</b> attributeValue != null <br/>
   * <b>Postcondition:</b> return value may not be null
   * 
   * @param mdAttribueKey
   * @param attributeType
   * @param attributeName
   * @param definingType
   * @param attributeValue
   * 
   * @return Attribute object of the appropriate sub class for the given
   *         DataAccess attribute type
   */
  public static Attribute createAttribute(String mdAttributeKey, String attributeType, String attributeName, String definingType, Object attributeValue)
  {
    Attribute attribute = null;

    if (attributeType.equals(MdAttributeCharacterInfo.CLASS))
    {
      attribute = new AttributeCharacter(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeIntegerInfo.CLASS))
    {
      attribute = new AttributeInteger(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeLongInfo.CLASS))
    {
      attribute = new AttributeLong(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeFloatInfo.CLASS))
    {
      attribute = new AttributeFloat(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDoubleInfo.CLASS))
    {
      attribute = new AttributeDouble(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDecimalInfo.CLASS))
    {
      attribute = new AttributeDecimal(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeTextInfo.CLASS))
    {
      attribute = new AttributeText(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeJsonInfo.CLASS))
    {
      attribute = new AttributeJson(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeClobInfo.CLASS))
    {
      attribute = new AttributeClob(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeFileInfo.CLASS))
    {
      attribute = new AttributeFile(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeBooleanInfo.CLASS))
    {
      attribute = new AttributeBoolean(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeUUIDInfo.CLASS))
    {
      attribute = new AttributeUUID(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeTimeInfo.CLASS))
    {
      attribute = new AttributeTime(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDateInfo.CLASS))
    {
      attribute = new AttributeDate(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeDateTimeInfo.CLASS))
    {
      attribute = new AttributeDateTime(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeReferenceInfo.CLASS))
    {
      attribute = new AttributeReference(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeTermInfo.CLASS))
    {
      attribute = new AttributeTerm(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeEnumerationInfo.CLASS))
    {
      attribute = new AttributeEnumeration(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeMultiReferenceInfo.CLASS))
    {
      attribute = new AttributeMultiReference(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeMultiTermInfo.CLASS))
    {
      attribute = new AttributeMultiTerm(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeHashInfo.CLASS))
    {
      attribute = new AttributeHash(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeSymmetricInfo.CLASS))
    {
      attribute = new AttributeSymmetric(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeBlobInfo.CLASS))
    {
      attribute = new AttributeBlob(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    // only call for new instances
    else if (attributeType.equals(MdAttributeStructInfo.CLASS))
    {
      attribute = new AttributeStruct(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeLocalCharacterInfo.CLASS))
    {
      attribute = new AttributeLocalCharacter(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeLocalTextInfo.CLASS))
    {
      attribute = new AttributeLocalText(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeIndicatorInfo.CLASS))
    {
      attribute = new AttributeIndicator(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    else if (attributeType.equals(MdAttributeGraphReferenceInfo.CLASS) || attributeType.equals(MdAttributeClassificationInfo.CLASS))
    {
      attribute = new AttributeGraphRef(attributeName, mdAttributeKey, definingType, (String) attributeValue);
    }
    if (attribute == null)
    {
      for (PluginIF plugin : pluginMap.values())
      {
        attribute = plugin.createAttribute(mdAttributeKey, attributeType, attributeName, definingType, attributeValue);

        if (attribute != null)
        {
          break;
        }
      }
    }

    if (attribute == null)
    {
      String error = "[" + attributeType + "] is not a recognized attribute type.";
      throw new AttributeException(error);
    }

    return attribute;
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Object getColumnValueFromRow(ResultSet resultSet, String columnAlias, String attributeType);

    public Attribute createAttribute(String mdAttributeKey, String attributeType, String attributeName, String definingType, Object attributeValue);
  }
}
