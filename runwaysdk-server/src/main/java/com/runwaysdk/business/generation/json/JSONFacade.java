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
package com.runwaysdk.business.generation.json;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.ontology.ToJSONIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdInformationDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdUtilDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.MdWarningDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.facade.FacadeException;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.system.metadata.MdMethodQuery;
import com.runwaysdk.transport.conversion.ConversionException;
import com.runwaysdk.transport.conversion.json.ComponentDTOIFToJSON;
import com.runwaysdk.transport.conversion.json.ComponentQueryDTOToJSON;
import com.runwaysdk.transport.conversion.json.EnumDTOToJSON;
import com.runwaysdk.transport.conversion.json.JSONToComponentQueryDTO;
import com.runwaysdk.transport.conversion.json.JSONToEnumDTO;
import com.runwaysdk.transport.conversion.json.JSONUtil;
import com.runwaysdk.util.DTOConversionUtilInfo;
import com.runwaysdk.util.DateUtilities;

public class JSONFacade
{

  /**
   * Generates the dependency tree for a list of types
   */
  private static List<MdTypeDAOIF> getTypeTree(String sessionId, String[] types)
  {
    // remove redundant entries, such as parent classes when a subclass is
    // requested
    // (the parent classes will automatically be included)
    TypeTree tree = new TypeTree();
    for (String type : types)
    {
      MdTypeDAOIF mdTypeIF = MdEntityDAO.getMdTypeDAO(type);
      // check for unpublished classes
      if (mdTypeIF instanceof MdClassDAOIF && ! ( (MdClassDAOIF) mdTypeIF ).isPublished())
      {
        String error = "Cannot generate Javascript for type [" + mdTypeIF.definesType() + "] because it is not published.";
        throw new FacadeException(error);
      }
      else
      {
        tree.insert(mdTypeIF);
      }
    }

    return tree.getOrderedTypes();
  }

  /**
   * Gets the newest last updated date of a type in a list of types
   */
  public static String getNewestUpdateDate(String sessionId, String[] types)
  {

    List<MdTypeDAOIF> mdTypes = JSONFacade.getTypeTree(sessionId, types);

    // set the newest type to January 1st 1970
    Date newestType = new Date(1);
    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    // get each type's MdEntity
    for (MdTypeDAOIF mdTypeIF : mdTypes)
    {
      String toParse = mdTypeIF.getValue(MdTypeInfo.LAST_UPDATE_DATE);
      Date lastUpdate = dateFormat.parse(toParse, new java.text.ParsePosition(0));
      if (lastUpdate.after(newestType))
      {
        newestType = lastUpdate;
      }

      if (mdTypeIF instanceof MdEntityDAOIF)
      {
        MdEntityDAOIF mdEntity = (MdEntityDAOIF) mdTypeIF;
        for (MdAttributeConcreteDAOIF mdAttribute : mdEntity.getAllDefinedMdAttributes())
        {
          String mDtoParse = mdAttribute.getValue(MdTypeInfo.LAST_UPDATE_DATE);
          Date lastUpdateMd = dateFormat.parse(mDtoParse, new java.text.ParsePosition(0));
          if (lastUpdateMd.after(newestType))
          {
            newestType = lastUpdateMd;
          }

        }
      }
    }
    return dateFormat.format(newestType);
  }
  
  /**
   * Generates the javascript definitions for each specified type. These
   * definitions are used by the dynamically generated JSON objects.
   */
  public static String importTypes(String sessionId, String[] types)
  {
    String definitions = "";

    List<MdTypeDAOIF> mdTypes = JSONFacade.getTypeTree(sessionId, types);

    // get each type's MdEntity
    for (MdTypeDAOIF mdTypeIF : mdTypes)
    {
      // generate a definition
      if (mdTypeIF instanceof MdTermDAOIF) {
        TypeJSGenerator generator = new TermJSGenerator(sessionId, (MdBusinessDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        // TODO : Term Query ?
        ComponentQueryJSGenerator queryGenerator = new BusinessQueryJSGenerator(sessionId, (MdBusinessDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdTermRelationshipDAOIF) {
        TypeJSGenerator generator = new TermRelationshipJSGenerator(sessionId, (MdRelationshipDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        ComponentQueryJSGenerator queryGenerator = new RelationshipQueryJSGenerator(sessionId, (MdRelationshipDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdEnumerationDAOIF)
      {
        TypeJSGenerator generator = new EnumerationJSGenerator(sessionId, (MdEnumerationDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdBusinessDAOIF)
      {
        TypeJSGenerator generator = new BusinessJSGenerator(sessionId, (MdBusinessDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        ComponentQueryJSGenerator queryGenerator = new BusinessQueryJSGenerator(sessionId, (MdBusinessDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdRelationshipDAOIF)
      {
        TypeJSGenerator generator = new RelationshipJSGenerator(sessionId, (MdRelationshipDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        ComponentQueryJSGenerator queryGenerator = new RelationshipQueryJSGenerator(sessionId, (MdRelationshipDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdLocalStructDAOIF)
      {
        TypeJSGenerator generator = new LocalStructJSGenerator(sessionId, (MdLocalStructDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        ComponentQueryJSGenerator queryGenerator = new LocalStructQueryJSGenerator(sessionId, (MdLocalStructDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdStructDAOIF)
      {
        TypeJSGenerator generator = new StructJSGenerator(sessionId, (MdStructDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        ComponentQueryJSGenerator queryGenerator = new StructQueryJSGenerator(sessionId, (MdStructDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdViewDAOIF)
      {
        TypeJSGenerator generator = new ViewJSGenerator(sessionId, (MdViewDAOIF) mdTypeIF);
        definitions += generator.getDefinition();

        ComponentQueryJSGenerator queryGenerator = new ViewQueryJSGenerator(sessionId, (MdViewDAOIF) mdTypeIF);
        definitions += queryGenerator.getDefinition();
      }
      else if (mdTypeIF instanceof MdUtilDAOIF)
      {
        TypeJSGenerator generator = new UtilJSGenerator(sessionId, (MdUtilDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdExceptionDAOIF)
      {
        ExceptionJSGenerator generator = new ExceptionJSGenerator(sessionId, (MdExceptionDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdProblemDAOIF)
      {
        ProblemJSGenerator generator = new ProblemJSGenerator(sessionId, (MdProblemDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdWarningDAOIF)
      {
        WarningJSGenerator generator = new WarningJSGenerator(sessionId, (MdWarningDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdInformationDAOIF)
      {
        InformationJSGenerator generator = new InformationJSGenerator(sessionId, (MdInformationDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdFacadeDAOIF)
      {
        JavascriptClientRequestGenerator generator = new JavascriptClientRequestGenerator(sessionId, (MdFacadeDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else if (mdTypeIF instanceof MdControllerDAOIF)
      {
        ControllerJSGenerator generator = new ControllerJSGenerator(sessionId, (MdControllerDAOIF) mdTypeIF);
        definitions += generator.getDefinition();
      }
      else
      {
        String error = "The JSON for type [" + mdTypeIF.definesType() + "] cannot be generated.";
        throw new ProgrammingErrorException(error);
      }
    }

    return definitions;
  }

  /**
   * Converts an MutableDTO into a JSON string.
   * 
   * @param componentDTO
   * 
   * @return a JSON string representing an ComponentDTO
   */
  public static JSONObject getJSONFromComponentDTO(ComponentDTO componentDTO)
  {
    try
    {
      ComponentDTOIFToJSON componentDTOToJSON = ComponentDTOIFToJSON.getConverter(componentDTO);
      return componentDTOToJSON.populate();
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  public static String getJSONFromMethodMetadata(MethodMetaData metadata)
  {
    try
    {
      String className = metadata.getClassName();
      String methodName = metadata.getMethodName();
      String[] declaredTypes = metadata.getDeclaredTypes();

      JSONObject json = new JSONObject();
      json.put(JSON.METHOD_METADATA_CLASSNAME.getLabel(), className);
      json.put(JSON.METHOD_METADATA_METHODNAME.getLabel(), methodName);

      json.put(JSON.METHOD_METADATA_DECLARED_TYPES.getLabel(), Arrays.asList(declaredTypes));

      return json.toString();
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  public static MethodMetaData getMethodMetaDataFromJSON(String json)
  {
    try
    {
      JSONObject jsonObject = new JSONObject(json);

      String className = jsonObject.getString(JSON.METHOD_METADATA_CLASSNAME.getLabel());
      String methodName = jsonObject.getString(JSON.METHOD_METADATA_METHODNAME.getLabel());

      String[] declaredTypes;
      if (!jsonObject.isNull(JSON.METHOD_METADATA_DECLARED_TYPES.getLabel()))
      {
        JSONArray array = jsonObject.getJSONArray(JSON.METHOD_METADATA_DECLARED_TYPES.getLabel());
        declaredTypes = new String[array.length()];
        for (int i = 0; i < array.length(); i++)
        {
          declaredTypes[i] = array.getString(i);
        }
      }
      else
      {
        // Lazy loading is in use, so query the metadata for a match on class
        // name + method
        MdEntityDAOIF md = MdEntityDAO.getMdEntityDAO(className);
        List<? extends MdEntityDAOIF> hierarchy = md.getSuperClasses();

        String[] ids = new String[hierarchy.size()];
        for (int i = 0; i < hierarchy.size(); i++)
        {
          ids[i] = hierarchy.get(i).getId();
        }

        // FIXME optimize this
        QueryFactory f = new QueryFactory();
        MdMethodQuery q = new MdMethodQuery(f);
        ValueQuery v = new ValueQuery(f);

        v.SELECT(q.getId(MdMethodInfo.ID));
        v.WHERE(q.getMethodName().EQ(methodName));
        v.WHERE(q.getMdType().IN(ids)); // FIXME write tests for IN/NI on
                                        // AttributeReference

        OIterator<? extends ValueObject> iter = v.getIterator();

        try
        {
          if (iter.hasNext())
          {
            declaredTypes = GenerationUtil.getDeclaredTypes(iter.next().getValue(MdMethodInfo.ID));
          }
          else
          {
            // FIXME throw ex;
            throw new RuntimeException();
          }
        }
        finally
        {
          iter.close();
        }
      }

      return new MethodMetaData(className, methodName, declaredTypes);
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  public static JSONArray getJSONArrayFromObjects(List<? extends Object> objectl) {
    try
    {
      JSONArray jsonArray = new JSONArray();
      for (Object obj : objectl)
      {
        if (obj instanceof ToJSONIF) {
          jsonArray.put(( (ToJSONIF) obj ).toJSON());
        }
        else if (obj instanceof ComponentDTOIF) {
          ComponentDTOIFToJSON converter = ComponentDTOIFToJSON.getConverter((ComponentDTOIF) obj);
          jsonArray.put(converter.populate());
        }
        else {
          throw new ConversionException("Invalid object in array.");
        }
      }

      return jsonArray;
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }
  
  public static JSONArray getJSONArrayFromComponentDTOs(List<? extends ComponentDTO> componentDTOs)
  {
    try
    {
      JSONArray jsonArray = new JSONArray();
      for (ComponentDTO componentDTO : componentDTOs)
      {
        ComponentDTOIFToJSON converter = ComponentDTOIFToJSON.getConverter(componentDTO);
        jsonArray.put(converter.populate());
      }

      return jsonArray;
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  public static JSONObject getJSONFromQueryDTO(ComponentQueryDTO queryDTO, boolean typeSafe)
  {
    try
    {
      ComponentQueryDTOToJSON queryDTOToJSON = ComponentQueryDTOToJSON.getConverter(queryDTO, typeSafe);
      return queryDTOToJSON.populate();
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  /**
   * Returns the proper object to represent the input object as json. The output
   * object might be a JSONObject, JSONArray, JSON.Null, or Java Object that
   * represents a primitive.
   * 
   * @param object
   * @return
   */
  public static Object getJSONFromObject(String sessionId, Object object)
  {
    if (object == null)
    {
      return JSONObject.NULL;
    }

    Class<?> clazz = object.getClass();

    if (clazz.isArray())
    {
      Object[] array = (Object[]) object;

      JSONArray jsonArray = new JSONArray();
      for (int i = 0; i < array.length; i++)
      {
        Object itemObj = getJSONFromObject(sessionId, array[i]);
        jsonArray.put(itemObj);
      }

      return jsonArray;
    }
    else if (object instanceof ComponentQueryDTO)
    {
      return JSONFacade.getJSONFromQueryDTO((ComponentQueryDTO) object, true);
    }
    else if (object instanceof ComponentDTO)
    {
      return JSONFacade.getJSONFromComponentDTO((MutableDTO) object);
    }
    else if (object instanceof Date)
    {
      return DateUtilities.formatISO8601((Date) object);
    }
    else if (object instanceof EnumDTO)
    {
      try
      {
        EnumDTOToJSON enumDTOIFToJSON = new EnumDTOToJSON(sessionId, (EnumDTO) object);
        return enumDTOIFToJSON.populate();
      }
      catch (JSONException e)
      {
        throw new ConversionException(e);
      }
    }
    else
    {
      // The object is a primitive, so there is no conversion needed.
      return object;
    }
  }

  public static Object getObjectFromJSON(String sessionId, Locale locale, String classType, String json)
  {
    Class<?> c = LoaderDecorator.load(classType);

    return JSONFacade.getObjectFromJSON(sessionId, locale, c, json);
  }

  public static Object getObjectFromJSON(String sessionId, String classType, String json)
  {
    Class<?> c = LoaderDecorator.load(classType);

    Session session = SessionFacade.getSessionForRequest(sessionId);
    Locale locale = session.getLocale();

    return JSONFacade.getObjectFromJSON(sessionId, locale, c, json);
  }

  /**
   * Checks if a class represents an Entity (both business and DTO layer)
   * 
   * @return true if the class is an Enum; Otherwise, false
   */
  public static boolean isComponent(Class<?> clazz)
  {
    return ( ComponentDTO.class.isAssignableFrom(clazz) || ComponentIF.class.isAssignableFrom(clazz) );
  }

  /**
   * Checks if a class represents an Enum (both business and DTO layer)
   * 
   * @return true if the class is an Enum; Otherwise, false
   */
  public static boolean isEnum(Class<?> clazz)
  {
    return ( EnumDTO.class.isAssignableFrom(clazz) || BusinessEnumeration.class.isAssignableFrom(clazz) );
  }

  public static Object getObjectFromJSON(String sessionId, Locale locale, Class<?> clazz, String json)
  {
    return getObjectFromJSON(sessionId, locale, clazz, json, false);
  }

  /**
   * 
   * @param methodType
   * @param json
   * @return
   */
  public static Object getObjectFromJSON(String sessionId, Locale locale, Class<?> clazz, String json, boolean keepGeneric)
  {
    try
    {
      if (JSONUtil.isNull(json))
      {
        return null;
      }
      else if (clazz.isArray())
      {
        JSONArray jsonArray = new JSONArray(json);

        Class<?> component = clazz.getComponentType();

        Object[] array = (Object[]) Array.newInstance(keepGeneric ? Object.class : component, jsonArray.length());

        for (int i = 0; i < array.length; i++)
        {
          Object item = getObjectFromJSON(sessionId, locale, component, jsonArray.getString(i), keepGeneric);
          array[i] = item;
        }
        return array;
      }
      else if (isComponent(clazz))
      {
        return JSONUtil.getComponentDTOFromJSON(sessionId, locale, json);
      }
      else if (isEnum(clazz))
      {
        try
        {
          JSONToEnumDTO jsonToEnumDTO = new JSONToEnumDTO(sessionId, json);
          return jsonToEnumDTO.populate();
        }
        catch (JSONException e)
        {
          throw new ConversionException(e);
        }
      }
      else
      {
        return AbstractFormatFactory.getFormatFactory().getFormat(clazz).parse(json, locale);
      }
    }
    catch (IllegalArgumentException e)
    {
      throw new ConversionException(e);
    }
    catch (SecurityException e)
    {
      throw new ConversionException(e);
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  /**
   * Converts a JSON string into a QueryDTO and returns that QueryDTO
   */
  public static ComponentQueryDTO getQueryDTOFromJSON(String sessionId, Locale locale, String json)
  {
    try
    {
      JSONToComponentQueryDTO jsonToQueryDTO = JSONToComponentQueryDTO.getConverter(sessionId, locale, json);
      return jsonToQueryDTO.populate();
    }
    catch (JSONException e)
    {
      throw new ConversionException(e);
    }
  }

  /**
   * 
   * @param sessionId
   * @param entityJSON
   * @param methodName
   * @param methodTypes
   * @param parameters
   * @return
   */
  public static JSONArray jsonInvokeMethod(String sessionId, Locale locale, String metadata, String calledObjectJSON, String parametersJSON)
  {
    MutableDTO mutableDTO = null;
    if (!JSONUtil.isNull(calledObjectJSON))
    {
      mutableDTO = (MutableDTO) JSONUtil.getComponentDTOFromJSON(sessionId, locale, calledObjectJSON);
    }

    MethodMetaData methodMetadata = getMethodMetaDataFromJSON(metadata);

    // convert the json arrays into java arrays
    String[] declaredTypes = methodMetadata.getDeclaredTypes();
    String[] parameters = JSONFacade.getStringArrayFromJSON(parametersJSON);

    String[] actualTypes = new String[declaredTypes.length];
    Object[] convertedParameters = new Object[declaredTypes.length];

    // convert each generic JSON parameter string into the runtime type
    for (int i = 0; i < declaredTypes.length; i++)
    {
      Class<?> clazz = LoaderDecorator.load(declaredTypes[i]);
      convertedParameters[i] = JSONFacade.getObjectFromJSON(sessionId, locale, clazz, parameters[i], true);

      // set the runtime type of the parameter
      // DTOs at this level are type unsafe, so get the type string
      if (convertedParameters[i] instanceof MutableDTO)
        actualTypes[i] = ( (MutableDTO) convertedParameters[i] ).getType();
      else
        actualTypes[i] = clazz.getName();
    }
    methodMetadata.setActualTypes(actualTypes);

    Object[] object = (Object[]) Facade.invokeMethod(sessionId, methodMetadata, mutableDTO, convertedParameters);

    JSONArray jsonArray = getJSONFromInvokedMethod(sessionId, object);

    return jsonArray;
  }

  public static JSONArray getJSONFromInvokedMethod(String sessionId, Object[] object)
  {
    // convert the return object array into a JSONArray
    JSONArray jsonArray = new JSONArray();

    try
    {
      // return object
      Object returnObject = object[DTOConversionUtilInfo.RETURN_OBJECT];
      jsonArray.put(DTOConversionUtilInfo.JSON_RETURN_OBJECT, getJSONFromObject(sessionId, returnObject));

      // called object
      MutableDTO calledObject = (MutableDTO) object[DTOConversionUtilInfo.CALLED_OBJECT];

      // static method check
      if (calledObject == null)
      {
        jsonArray.put(DTOConversionUtilInfo.JSON_CALLED_OBJECT, JSONObject.NULL);
      }
      else
      {
        jsonArray.put(DTOConversionUtilInfo.JSON_CALLED_OBJECT, JSONFacade.getJSONFromComponentDTO(calledObject));
      }
    }
    catch (JSONException e)
    {
      throw new ProgrammingErrorException(e);
    }

    return jsonArray;
  }

  /**
   * Converts a json array of strings into a Java String array.
   * 
   * @param json
   * @return
   */
  public static String[] getStringArrayFromJSON(String json)
  {
    String[] arr = null;
    if (JSONUtil.isArray(json))
    {
      try
      {
        JSONArray jsonArray = new JSONArray(json);
        arr = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++)
        {
          arr[i] = jsonArray.getString(i);
        }
        return arr;
      }
      catch (JSONException e)
      {
        throw new ConversionException(e);
      }
    }
    else
    {
      String error = "Tried to convert a non-JSON array into a String array";
      throw new ConversionException(error);
    }
  }

}
