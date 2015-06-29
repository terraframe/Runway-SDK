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
package com.runwaysdk.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.RelationshipDTOInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * Class that parses a Mojax request with complex object from a direct controller invocation. This class will convert JSON into a key/value map that the RequestScraper can understand.
 */
public class MojaxObjectParser
{
  private ActionParameters      annotation;

  private JSONObject            mojaxObject;

  private Map<String, String[]> parameters;

  MojaxObjectParser(ActionParameters annotation, String mojaxObject) throws JSONException
  {
    this.annotation = annotation;
    this.mojaxObject = new JSONObject(mojaxObject);
    this.parameters = new HashMap<String, String[]>();
  }

  /**
   * Returns the Mojax Object as a map that is compatible with RequestScraper.
   *
   * @return
   * @throws JSONException
   */
  Map<String, String[]> getMap() throws JSONException
  {
    StringTokenizer toke = new StringTokenizer(annotation.parameters(), ",");
    while (toke.hasMoreTokens())
    {
      String parameter = toke.nextToken();
      String[] value = parameter.split(":");
      String type = value[0].trim();
      String parameterName = value[1].trim();

      convertToParameter(type, parameterName);
    }

    return parameters;
  }

  /**
   * Converts the parameter with the given name to an acceptable key/value pairing for the RequestScraper to handle.
   *
   * @param type
   * @param baseName
   * @throws JSONException
   */
  private void convertToParameter(String type, String baseName) throws JSONException
  {
    Class<?> c = LoaderDecorator.load(type);

    if (mojaxObject.isNull(baseName))
    {
      parameters.put(baseName, null);
      return;
    }

    if (c.isArray())
    {
      if (DispatchUtil.isPrimitive(c))
      {
        String[] values = convertPrimitiveArray(c, baseName);
        parameters.put(baseName, values);
      }
      else
      {
        JSONArray dtoArray = mojaxObject.getJSONArray(baseName);
        for (int i = 0; i < dtoArray.length(); i++)
        {
          if (!dtoArray.isNull(i))
          {
            JSONObject dto = dtoArray.getJSONObject(i);
            String indexBaseName = baseName + "_" + i;
            convertDTO(dto, indexBaseName);
          }
        }
      }
    }
    else if (DispatchUtil.isPrimitive(c))
    {
      Object object = mojaxObject.get(baseName);
      String value = convertPrimitive(c, object);
      parameters.put(baseName, new String[] { value });
    }
    else
    {
      // convert DTO value
      if (!mojaxObject.isNull(baseName))
      {
        JSONObject dto = mojaxObject.getJSONObject(baseName);
        convertDTO(dto, baseName);
      }
    }
  }

  /**
   * Converts the parameters that represent a dto structure.
   *
   * @param baseName
   * @throws JSONException
   */
  private void convertDTO(JSONObject dto, String baseName) throws JSONException
  {
    // id
    String id = dto.getString(JSON.ENTITY_DTO_ID.getLabel());
    parameters.put(baseName + ".componentId", new String[] { id });

    // isNew
    Boolean isNew = dto.getBoolean(JSON.ENTITY_DTO_NEW_INSTANCE.getLabel());
    parameters.put(baseName + ".isNew", new String[] { isNew.toString() });

    // actual type
    String actualType = dto.getString(JSON.COMPONENT_DTO_TYPE.getLabel());
    parameters.put("#" + baseName + ".actualType", new String[] { actualType });

    convertDTOAttributes(dto, baseName);

    if (dto.getString(JSON.DTO_TYPE.getLabel()).equals(RelationshipDTOInfo.CLASS))
    {
      String parentId = dto.getString(JSON.RELATIONSHIP_DTO_PARENT_ID.getLabel());
      parameters.put("#" + baseName + ".parent.id", new String[] { parentId });

      String childId = dto.getString(JSON.RELATIONSHIP_DTO_CHILD_ID.getLabel());
      parameters.put("#" + baseName + ".child.id", new String[] { childId });
    }
  }

  private void convertDTOAttributes(JSONObject dto, String baseName) throws JSONException
  {
    JSONObject attributeMap = dto.getJSONObject(JSON.ENTITY_DTO_ATTRIBUTE_MAP.getLabel());

    Iterator<?> iter = attributeMap.keys();

    while (iter.hasNext())
    {
      String name = (String) iter.next();

      // ignore id and type attributes because including them
      // will cause an exception as the RequestScraper will try
      // and set their values on the DTO
      if (name.equals(ComponentInfo.ID) || name.equals(ComponentInfo.TYPE))
      {
        continue;
      }

      String paramName = baseName + "." + name;

      JSONObject attribute = attributeMap.getJSONObject(name);
      String type = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_TYPE.getLabel());

      if (type.equals(MdAttributeStructInfo.CLASS) && attribute.has(JSON.STRUCT_STRUCT_DTO.getLabel()) && !attribute.isNull(JSON.STRUCT_STRUCT_DTO.getLabel()))
      {
        JSONObject struct = attribute.getJSONObject(JSON.STRUCT_STRUCT_DTO.getLabel());
        convertDTOAttributes(struct, paramName);
      }
      else if (type.equals(MdAttributeEnumerationInfo.CLASS))
      {
        JSONObject enumValues = attribute.getJSONObject(JSON.ENUMERATION_ENUM_NAMES.getLabel());

        Iterator<?> enumIter = enumValues.keys();
        String[] values = new String[enumValues.length()];
        int count = 0;
        while (enumIter.hasNext())
        {
          String enumName = (String) enumIter.next();
          values[count] = enumName;
          count++;
        }

        parameters.put(paramName, values);
      }
      else if (type.equals(MdAttributeDateTimeInfo.CLASS))
      {
        if (!attribute.isNull(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel()))
        {
          String dateTime = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
          Date date = this.parseDate(dateTime);

          String value = new SimpleDateFormat(Constants.DATETIME_FORMAT).format(date);
          parameters.put(paramName, new String[] { value });
        }
      }
      else if (type.equals(MdAttributeDateInfo.CLASS))
      {
        if (!attribute.isNull(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel()))
        {
          String dateTime = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
          Date date = this.parseDate(dateTime);

          String value = new SimpleDateFormat(Constants.DATE_FORMAT).format(date);
          parameters.put(paramName, new String[] { value });
        }
      }
      else if (type.equals(MdAttributeTimeInfo.CLASS))
      {
        if (!attribute.isNull(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel()))
        {
          String dateTime = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
          Date date = this.parseDate(dateTime);

          String value = new SimpleDateFormat(Constants.TIME_FORMAT).format(date);
          parameters.put(paramName, new String[] { value });
        }
      }
      else
      {
        if (attribute.isNull(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel()))
        {
          parameters.put(paramName, new String[] { "" });
        }
        else
        {
          String value = attribute.getString(JSON.ENTITY_DTO_ATTRIBUTE_VALUE.getLabel());
          parameters.put(paramName, new String[] { value });
        }
      }
    }
  }

  private Date parseDate(String dateTime)
  {
    try
    {
      long timestamp = Long.parseLong(dateTime);

      return new Date(timestamp);
    }
    catch (NumberFormatException e)
    {
      // Expect date values to come in as timestamps. However, json.js uses
      // the ISO date format to serialized dates, so sometimes the
      // dates will be in that format instead of a timestamp.

      try
      {
        Date date = ISODateTimeFormat.dateTimeParser().parseDateTime(dateTime).toDate();
        
        return date;
      }
      catch (Throwable t)
      {
        // Throw the original exception
        throw e;
      }
    }
  }

  /**
   * Converts a json primitive array into a string array of values.
   *
   * @param c
   * @param baseName
   * @return
   * @throws JSONException
   */
  private String[] convertPrimitiveArray(Class<?> c, String baseName) throws JSONException
  {
    JSONArray array = mojaxObject.getJSONArray(baseName);
    String[] values = new String[array.length()];
    for (int i = 0; i < array.length(); i++)
    {
      Object object = array.get(i);
      String value = convertPrimitive(c.getComponentType(), object);
      values[i] = value;
    }

    return values;
  }

  /**
   * Converts a json primitive into a java string value.
   *
   * @param type
   * @param name
   * @throws JSONException
   */
  private String convertPrimitive(Class<?> c, Object object) throws JSONException
  {
    if (JSONObject.NULL.equals(object))
    {
      // nothing to see here people ... move along
      return null;
    }
    else if (String.class.isAssignableFrom(c))
    {
      return (String) object;
    }
    else if (Date.class.isAssignableFrom(c))
    {
      // convert from timestamp to date
      long timestamp = Long.parseLong(String.valueOf(object));
      Date date = new Date(timestamp);
      return new SimpleDateFormat(Constants.DATETIME_FORMAT).format(date);
    }
    else
    {
      return String.valueOf(object);
    }
  }
}
