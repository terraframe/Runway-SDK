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
package com.runwaysdk.transport.conversion;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.ComponentDTOIFCopier;
import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.business.EnumerationDTOIF;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.business.ontology.TermAndRelDTO;
import com.runwaysdk.business.ontology.TermDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class ConversionFacade
{

  public static final String ENUM_CONVERSION_CLASS             = Constants.ROOT_PACKAGE + ".transport.conversion.business.BusinessToBusinessDTO";

  public static final String CONVERT_GIVEN_ENUMS_TO_DTO_METHOD = "getEnumerations";

  private static XPath       xpath;

  static
  {
    XPathFactory factory = null;

    try
    {
      factory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI);
    }
    catch (XPathFactoryConfigurationException ex)
    {
      String errString = "XPath factory is not propery configured: " + ex.getMessage();
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), errString, ex);
    }
    xpath = factory.newXPath();
  }

  public static XPath getXPath()
  {
    return xpath;
  }

  /**
   * Returns a java.util.Locale object that represents the value of the given
   * locale parameter. The parameter is expected to be in the form of
   * language_country (e.g., en_US) or just language (e.g., en).
   * 
   * @param locale
   * @return java.util.Locale object that represents the value of the given
   *         locale parameter. The parameter is expected to be in the form of
   *         language_country (e.g., en_US) or just language (e.g., en).
   */
  public static Locale getLocale(String locale)
  {
    StringTokenizer tokenizer = new StringTokenizer(locale, "_");

    // language_country_variant
    if (tokenizer.countTokens() > 2)
    {
      String language = tokenizer.nextToken();
      String country = tokenizer.nextToken();
      String variant = tokenizer.nextToken();

      return new Locale(language, country, variant);
    }
    // language_country
    else if (tokenizer.countTokens() == 2)
    {
      String language = tokenizer.nextToken();
      String country = tokenizer.nextToken();
      return new Locale(language, country);
    }
    // language
    else
    {
      String language = tokenizer.nextToken();
      return new Locale(language);
    }
  }

  public static Locale[] convertLocales(String[] stringLocales)
  {
    Locale[] locales = new Locale[stringLocales.length];

    for (int i = 0; i < stringLocales.length; i++)
    {
      if (!stringLocales[i].trim().equals(""))
      {
        try
        {
          locales[i] = ConversionFacade.getLocale(stringLocales[i]);
        }
        catch (Exception e)
        {
          locales[i] = CommonProperties.getDefaultLocale();
        }
      }
    }
    return locales;
  }

  /**
   * Creates a typeSafe DTO with values copied from the given source into
   * typesafe values.
   * 
   * @param source
   * 
   * @return A type safe equivalent of a given DTO
   */
  public static ComponentDTOIF createTypeSafeCopyWithTypeSafeAttributes(ClientRequestIF clientRequest, ComponentDTOIF source)
  {
    if (source == null)
    {
      return null;
    }

    return ComponentDTOIFCopier.create(clientRequest, source, true, true);
  }

  /**
   * Creates generic DTO with type safe attributes with values copied from the
   * given source.
   * 
   * @param source
   * 
   * @return A generic DTO with type safe attributes.
   */
  public static ComponentDTOIF createGenericCopyWithTypeSafeAttributes(ClientRequestIF clientRequest, ComponentDTOIF source)
  {
    if (source == null)
    {
      return null;
    }

    return ComponentDTOIFCopier.create(clientRequest, source, false, true);
  }

  /**
   * Copies the values of a DTO into a new generic DTO for transfer to the
   * Facade.
   * 
   * @param source
   * @return
   */
  public static ComponentDTOIF createGenericCopy(ComponentDTOIF source)
  {
    return ComponentDTOIFCopier.create(null, source, false, false);
  }

  /**
   * Copies the contents of one DTO into another. The ComponentDTOIF dest
   * parameter is assumed to be type-safe.
   * 
   * @param sessionId
   * @param clientRequestIF
   * @param source
   * @param dest
   */
  public static void typeSafeCopy(ClientRequestIF clientRequestIF, ComponentDTOIF source, ComponentDTOIF dest)
  {
    if (source == null)
    {
      return;
    }

    ComponentDTOIFCopier.copy(clientRequestIF, source, dest, true, true);
  }

  /**
   * Converts an array of Objects into its serializable form. If an object is a
   * DTO then it converts the DTO to its generic form. If the object is a Java
   * primitive then it is already serializable. If the object is an array then
   * it recursively converts the object to a serializable form.
   * 
   * @param objects
   *          An array of objects to convert to serializable
   * 
   * @return
   */
  public static Object[] convertGeneric(Object[] objects)
  {
    String type = getGenericDTOArrayType(objects.getClass());
    Class<?> componentType = LoaderDecorator.load(type).getComponentType();

    Object[] converted = (Object[]) Array.newInstance(componentType, objects.length);

    for (int i = 0; i < objects.length; i++)
    {
      converted[i] = convertGeneric(objects[i]);
    }

    return converted;
  }

  /**
   * Converts an Object into its generic, serializable form. This is nessary to
   * transport the Object to and from the server. If an object is a DTO then it
   * is converted. Otherwise, it must be a primitive, the object is unchanged
   * 
   * @param object
   *          The object to convert
   * 
   * @return
   */
  public static Object convertGeneric(Object object)
  {
    if (object == null)
    {
      return null;
    }
    else if (object.getClass().isArray())
    {
      Object convertedArray = convertGeneric((Object[]) object);
      return convertedArray;
    }
    else if (object instanceof EnumerationDTOIF)
    {
      // Convert the type safe enumeration dto to its serializable form
      EnumerationDTOIF enu = (EnumerationDTOIF) object;
      String enumType = object.getClass().getName().replace(TypeGeneratorInfo.DTO_SUFFIX, "");

      return new EnumDTO(enumType, enu.name());
    }
    else if (object instanceof MutableDTO)
    {
      // Convert the type safe dto to its serializable form
      return createGenericCopy((MutableDTO) object);
    }
    else
    {
      // Do nothing if the parameter is not a DTO
      return object;
    }
  }

  public static String getGenericDTOArrayType(Class<?> c)
  {
    Class<?> baseComponent = getBaseComponent(c);

    if (MutableDTO.class.isAssignableFrom(baseComponent))
    {
      String baseName = baseComponent.getName();
      String name = c.getName();

      return name.replace(baseName, MutableDTO.class.getName());
    }
    else if (EnumerationDTOIF.class.isAssignableFrom(baseComponent))
    {
      String baseName = baseComponent.getName();
      String name = c.getName();

      return name.replace(baseName, EnumDTO.class.getName());
    }

    return c.getName();
  }

  public static Class<?> getBaseComponent(Class<?> c)
  {
    while (c.isArray())
    {
      c = c.getComponentType();
    }

    return c;
  }

  /**
   * Converts an Object into its non serializable form, type safe form. If the
   * object is a DTO then it converts the DTO to its type safe form. If the
   * object is a Java primitive then it is already in the type safe form. If the
   * object is an array then it recursively converts the object to a
   * serializable form.
   * 
   * @param type
   *          The type safe type of the Object
   * @param object
   *          The Object to convert (not type safe)
   * @return
   */
  public static Object convertToTypeSafe(ClientRequestIF clientRequest, String type, Object object)
  {
    if (object == null)
    {
      return null;
    }
    Class<?> cls = LoaderDecorator.load(type);

    if (cls.isArray())
    {
      Object[] array = (Object[]) object;
      Class<?> componentType = cls.getComponentType();
      Object[] converted = (Object[]) Array.newInstance(componentType, array.length);

      for (int i = 0; i < array.length; i++)
      {
        Object inner = array[i];

        Object convertedArray = convertToTypeSafe(clientRequest, componentType.getName(), inner);
        converted[i] = convertedArray;
      }

      return converted;
    }
    else if (object instanceof EnumDTO)
    {
      return convertEnumDTO(clientRequest, (EnumDTO) object);
    }
    else if (object instanceof MutableDTO)
    {
      // Convert the DTO to the type safe equivalent
      return createTypeSafeCopyWithTypeSafeAttributes(clientRequest, (MutableDTO) object);
    }
    else if (object instanceof ClassQueryDTO)
    {
      return convertGenericQueryToTypeSafe(clientRequest, (ClassQueryDTO) object);
    }
    else if (object instanceof TermAndRelDTO)
    {
      TermAndRelDTO tnr = (TermAndRelDTO) object;

      TermDTO term = (TermDTO) createTypeSafeCopyWithTypeSafeAttributes(clientRequest, (MutableDTO) tnr.getTerm());

      return new TermAndRelDTO(term, tnr.getRelationshipType(), tnr.getRelationshipId());
    }
    else
    {
      return object;
    }
  }

  public static ClassQueryDTO convertGenericQueryToTypeSafe(ClientRequestIF clientRequestIF, ClassQueryDTO genericQueryDTO)
  {
    if (genericQueryDTO.hasSource())
    {
      ClassQueryDTO typeSafeQueryDTO = ComponentDTOFacade.instantiateTypeSafeQueryDTO(genericQueryDTO.getType());

      typeSafeQueryDTO.copyProperties(genericQueryDTO);

      List<ComponentDTOIF> safes = new LinkedList<ComponentDTOIF>();

      for (ComponentDTOIF generic : genericQueryDTO.getResultSet())
      {
        ComponentDTOIF componentDTOIF = ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF, generic);

        if (componentDTOIF instanceof ComponentDTO)
        {
          ComponentDTO componentDTO = (ComponentDTO) componentDTOIF;
          ComponentDTOFacade.setDefinedAttributeMetadata(componentDTO, typeSafeQueryDTO);
        }

        safes.add(componentDTOIF);
      }

      // reset the result set with type-safe DTOs
      ComponentDTOFacade.addResultSetToQueryDTO(typeSafeQueryDTO, safes);

      typeSafeQueryDTO.copyProperties(genericQueryDTO);
      return typeSafeQueryDTO;
    }

    return genericQueryDTO;
  }

  /**
   * Convert an EnumDTO to its type safe form
   * 
   * @param clientRequest
   *          The clientRequest to convert the underlying BusinessDTO
   * @param enumDTO
   *          The EnumDTO to convert
   * @return
   */
  public static EnumerationDTOIF convertEnumDTO(ClientRequestIF clientRequest, String enumType, String enumName)
  {
    try
    {
      // Get the name of the enumeration to create
      String enumDTOType = enumType + TypeGeneratorInfo.DTO_SUFFIX;

      Class<?> clazz = LoaderDecorator.load(enumDTOType);

      // Get the enumerated item
      EnumerationDTOIF enu = (EnumerationDTOIF) clazz.getMethod("valueOf", String.class).invoke(null, enumName);
      return enu;
    }
    catch (Exception e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }

    return null;
  }

  /**
   * Convert an EnumDTO to its type safe form
   * 
   * @param clientRequest
   *          The clientRequest to convert the underlying BusinessDTO
   * @param enumDTO
   *          The EnumDTO to convert
   * @return
   */
  public static EnumerationDTOIF convertEnumDTO(ClientRequestIF clientRequest, EnumDTO enumDTO)
  {
    try
    {
      // Get the name of the enumeration to create
      String enuType = enumDTO.getEnumType() + TypeGeneratorInfo.DTO_SUFFIX;

      Class<?> clazz = LoaderDecorator.load(enuType);

      // Get the enumerated item
      EnumerationDTOIF enu = (EnumerationDTOIF) clazz.getMethod("valueOf", String.class).invoke(null, enumDTO.getEnumName());
      return enu;
    }
    catch (Exception e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }

    return null;
  }

  /**
   * Returns the String representation of the business Layer Class name
   * equivalent for each objects
   * 
   * @param objects
   * @return
   */
  public static String[] getClassNames(Object[] objects)
  {
    String[] names = new String[objects.length];

    for (int i = 0; i < objects.length; i++)
    {
      Object object = objects[i];
      String name = null;

      if (object != null)
      {
        name = object.getClass().getName().replace(TypeGeneratorInfo.DTO_SUFFIX, "");
      }

      names[i] = name;
    }

    return names;
  }

  /**
   * Returns a list containing the type safe Enumerations of the given
   * enumeration type.
   * 
   * @param clientRequest
   *          The clientRequest to use if needed in conversion
   * @param enumType
   *          The type of the enumeration
   * @param enumNameList
   *          List of enumeration item names
   * @return
   */
  public static List<? extends EnumerationDTOIF> convertEnumDTOsFromEnumNames(ClientRequestIF clientRequest, String enumType, List<String> enumNameList)
  {
    List<EnumerationDTOIF> enums = new LinkedList<EnumerationDTOIF>();

    for (String enumName : enumNameList)
    {
      enums.add(convertEnumDTO(clientRequest, enumType, enumName));
    }

    return enums;
  }

  /**
   * Creates type safe BusinessDTO for the given type. If clientRequest is not
   * null then a trip is made to the server. If a type safe BusinessDTO is not
   * possible then it returns the generic BusinessDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */
  public static BusinessDTO createDynamicBusinessDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    BusinessDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (BusinessDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildBusinessDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link BusinessDTO} for the given type. If a type safe
   * {@link BusinessDTO} is not possible then it returns the generic
   * {@link BusinessDTO}. The values of the given {@link BusinessDTO} are copied
   * to the type safe {@link BusinessDTO}.
   * 
   * @param businessDTO
   *          Generic {@link BusinessDTO} contains the values for the new type
   *          safe {@link BusinessDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe {@link BusinessDTO}
   *          will use to communicate with the server
   * @return A sub-class of {@link BusinessDTO} corresponding to the type of the
   *         given {@link BusinessDTO}.
   */

  public static BusinessDTO createTypeSafeCopy(BusinessDTO businessDTO, ClientRequestIF clientRequest)
  {
    String dtoType = businessDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    BusinessDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(BusinessDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (BusinessDTO) constructor.newInstance(businessDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, businessDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return businessDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe RelationshipDTO for the given type. If clientRequest is
   * not null then a trip is made to the server. If a type safe RelationshipDTO
   * is not possible then it returns the generic RelationshipDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static RelationshipDTO createDynamicRelationshipDTO(ClientRequestIF clientRequest, String type, String parentId, String childId)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    RelationshipDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class, String.class, String.class);

      retDTO = (RelationshipDTO) constructor.newInstance(clientRequest, parentId, childId);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      if (e.getCause() != null)
      {
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getCause().getMessage(), e.getCause());
      }
      else
      {
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
      }
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildRelationshipDTO(clientRequest, type, parentId, childId);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link RelationshipDTO} for the given type. If a type
   * safe {@link RelationshipDTO} is not possible then it returns the generic
   * {@link RelationshipDTO}. The values of the given {@link RelationshipDTO}
   * are copied to the type safe {@link RelationshipDTO}.
   * 
   * @param relationshipDTO
   *          Generic {@link RelationshipDTO} contains the values for the new
   *          type safe {@link RelationshipDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe
   *          {@link RelationshipDTO} will use to communicate with the server
   * 
   * @return A sub-class of {@link RelationshipDTO} corresponding to the type of
   *         the given {@link RelationshipDTO}.
   */

  public static RelationshipDTO createTypeSafeCopy(RelationshipDTO relationshipDTO, ClientRequestIF clientRequest)
  {
    String dtoType = relationshipDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    RelationshipDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(RelationshipDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (RelationshipDTO) constructor.newInstance(relationshipDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, relationshipDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return relationshipDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe StructDTO for the given type. If clientRequest is not
   * null then a trip is made to the server. If a type safe StructDTO is not
   * possible then it returns the generic StructDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static StructDTO createDynamicStructDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    StructDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (StructDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildStructDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link StructDTO} for the given type. If a type safe
   * {@link StructDTO} is not possible then it returns the generic
   * {@link StructDTO}. The values of the given {@link StructDTO} are copied to
   * the type safe {@link StructDTO}.
   * 
   * @param structDTO
   *          Generic {@link StructDTO} contains the values for the new type
   *          safe {@link StructDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe {@link StructDTO}
   *          will use to communicate with the server
   * 
   * @return A sub-class of {@link StructDTO} corresponding to the type of the
   *         given {@link StructDTO}.
   */

  public static StructDTO createTypeSafeCopy(StructDTO structDTO, ClientRequestIF clientRequest)
  {
    String dtoType = structDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    StructDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(StructDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (StructDTO) constructor.newInstance(structDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, structDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return structDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe ViewDTO for the given type. If clientRequest is not null
   * then a trip is made to the server. If a type safe ViewDTO is not possible
   * then it returns the generic ViewDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static ViewDTO createDynamicViewDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    ViewDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (ViewDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildViewDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link ViewDTO} for the given type. If a type safe
   * {@link ViewDTO} is not possible then it returns the generic {@link ViewDTO}
   * . The values of the given {@link ViewDTO} are copied to the type safe
   * {@link ViewDTO}.
   * 
   * @param viewDTO
   *          Generic {@link ViewDTO} contains the values for the new type safe
   *          {@link ViewDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe {@link ViewDTO}
   *          will use to communicate with the server
   * @return A sub-class of {@link ViewDTO} corresponding to the type of the
   *         given {@link ViewDTO}.
   */

  public static ViewDTO createTypeSafeCopy(ViewDTO viewDTO, ClientRequestIF clientRequest)
  {
    String dtoType = viewDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    ViewDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(ViewDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (ViewDTO) constructor.newInstance(viewDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, viewDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return viewDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe UtilDTO for the given type. If clientRequest is not null
   * then a trip is made to the server. If a type safe UtilDTO is not possible
   * then it returns the generic UtilDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static UtilDTO createDynamicUtilDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    UtilDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (UtilDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildUtilDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link UtilDTO} for the given type. If a type safe
   * {@link UtilDTO} is not possible then it returns the generic {@link UtilDTO}
   * . The values of the given {@link UtilDTO} are copied to the type safe
   * {@link UtilDTO}.
   * 
   * @param utilDTO
   *          Generic {@link UtilDTO} contains the values for the new type safe
   *          {@link UtilDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe {@link UtilDTO}
   *          will use to communicate with the server
   * @return A sub-class of {@link UtilDTO} corresponding to the type of the
   *         given {@link UtilDTO}.
   */

  public static UtilDTO createTypeSafeCopy(UtilDTO utilDTO, ClientRequestIF clientRequest)
  {
    String dtoType = utilDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    UtilDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(UtilDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (UtilDTO) constructor.newInstance(utilDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, utilDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return utilDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe {@link SmartExceptionDTO} for the given type. If
   * clientRequest is not null then a trip is made to the server. If a type safe
   * {@link SmartExceptionDTO} is not possible then it returns the generic
   * {@link SmartExceptionDTO}.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static SmartExceptionDTO createDynamicSmartExceptionDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    SmartExceptionDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (SmartExceptionDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildSmartExceptionDTO(clientRequest);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link SmartExceptionDTO} for the given type. If a type
   * safe {@link SmartExceptionDTO} is not possible then it returns the generic
   * {@link SmartExceptionDTO}. The values of the given
   * {@link SmartExceptionDTO} are copied to the type safe
   * {@link SmartExceptionDTO}.
   * 
   * @param smartExceptionDTO
   *          Generic {@link SmartExceptionDTO} contains the values for the new
   *          type safe {@link SmartExceptionDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe
   *          {@link SmartExceptionDTO} will use to communicate with the server
   * @return A sub-class of {@link SmartExceptionDTO} corresponding to the type
   *         of the given {@link SmartExceptionDTO}.
   */

  public static SmartExceptionDTO createTypeSafeCopy(SmartExceptionDTO smartExceptionDTO, ClientRequestIF clientRequest)
  {
    String dtoType = smartExceptionDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    SmartExceptionDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(UtilDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (SmartExceptionDTO) constructor.newInstance(smartExceptionDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, smartExceptionDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return smartExceptionDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe RunwayExceptionDTO for the given type. If clientRequest
   * is not null then a trip is made to the server. If a type safe
   * RunwayExceptionDTO is not possible then it returns the generic ProblemDTO.
   * 
   * @param sessionId
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static RunwayExceptionDTO buildRunwayExceptionDTO(String type, String localizedMessage, String developerMessage)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    RunwayExceptionDTO runwayExceptionDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(String.class, String.class, String.class);

      runwayExceptionDTO = (RunwayExceptionDTO) constructor.newInstance(type, localizedMessage, developerMessage);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return new RunwayExceptionDTO(type, localizedMessage, developerMessage);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return runwayExceptionDTO;
  }

  /**
   * Creates type safe ProblemDTO for the given type. If clientRequest is not
   * null then a trip is made to the server. If a type safe ProblemDTO is not
   * possible then it returns the generic ProblemDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static ProblemDTO createDynamicProblemDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    ProblemDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (ProblemDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildProblemDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe WarningDTO for the given type. If clientRequest is not
   * null then a trip is made to the server. If a type safe WarningDTO is not
   * possible then it returns the generic WarningDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static WarningDTO createDynamicWarningDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    WarningDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (WarningDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildWarningDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates type safe InformationDTO for the given type. If clientRequest is
   * not null then a trip is made to the server. If a type safe InformationDTO
   * is not possible then it returns the generic InformationDTO.
   * 
   * @param clientRequest
   * @param type
   * 
   * @return
   */

  public static InformationDTO createDynamicInformationDTO(ClientRequestIF clientRequest, String type)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    InformationDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getConstructor(ClientRequestIF.class);

      retDTO = (InformationDTO) constructor.newInstance(clientRequest);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        retDTO = ComponentDTOFacade.buildInformationDTO(clientRequest, type);
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link ProblemDTO} for the given type. If a type safe
   * {@link ProblemDTO} is not possible then it returns the generic
   * {@link ProblemDTO}. The values of the given {@link ProblemDTO} are copied
   * to the type safe {@link ProblemDTO}.
   * 
   * @param problemDTO
   *          Generic {@link ProblemDTO} contains the values for the new type
   *          safe {@link ProblemDTO}
   * @param clientRequest
   *          {@link ClientRequestIF} that the new type safe {@link ProblemDTO}
   *          will use to communicate with the server
   * @return A sub-class of {@link ProblemDTO} corresponding to the type of the
   *         given {@link ProblemDTO}.
   */

  public static ProblemDTO createTypeSafeCopy(ProblemDTO problemDTO, ClientRequestIF clientRequest)
  {
    String dtoType = problemDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    ProblemDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(ProblemDTO.class, ClientRequestIF.class);
      constructor.setAccessible(true);

      retDTO = (ProblemDTO) constructor.newInstance(problemDTO, clientRequest);
      ConversionFacade.typeSafeCopy(clientRequest, problemDTO, retDTO);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return problemDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link AttributeProblemDTO} for the given type. If a
   * type safe {@link AttributeProblemDTO} is not possible then it returns the
   * generic {@link AttributeProblemDTO}. The values of the given
   * {@link AttributeProblemDTO} are copied to the type safe
   * {@link AttributeProblemDTO}.
   * 
   * @param attributeProblemDTO
   *          Generic {@link AttributeProblemDTO} contains the values for the
   *          new type safe {@link AttributeProblemDTO}
   * @return A sub-class of {@link AttributeProblemDTO} corresponding to the
   *         type of the given {@link AttributeProblemDTO}.
   */

  public static AttributeProblemDTO createTypeSafeCopy(AttributeProblemDTO attributeProblemDTO)
  {
    String dtoType = attributeProblemDTO.getType() + TypeGeneratorInfo.DTO_SUFFIX;
    AttributeProblemDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
      constructor.setAccessible(true);

      retDTO = (AttributeProblemDTO) constructor.newInstance(attributeProblemDTO.getType(), attributeProblemDTO.getComponentId(), attributeProblemDTO.getDefiningType(), attributeProblemDTO.getDefiningTypeDisplayLabel(), attributeProblemDTO.getAttributeName(), attributeProblemDTO.getAttributeId(), attributeProblemDTO.getAttributeDisplayLabel(), attributeProblemDTO.getMessage());

      retDTO.setDeveloperMessage(attributeProblemDTO.getDeveloperMessage());
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        return attributeProblemDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

  /**
   * Creates a type safe {@link AttributeProblemDTO} for the given type. If a
   * type safe {@link AttributeProblemDTO} is not possible then it returns the
   * generic {@link AttributeProblemDTO}. The values of the given
   * {@link AttributeProblemDTO} are copied to the type safe
   * {@link AttributeProblemDTO}.
   * 
   * @param type
   * @param componentId
   * @param definingType
   * @param definingTypeDisplayLabel
   * @param attributeName
   * @param attributeDisplayLabel
   * @param localizedMessage
   * @param developerMessage
   * @return A sub-class of {@link AttributeProblemDTO} corresponding to the
   *         type of the given {@link AttributeProblemDTO}.
   */

  public static AttributeProblemDTO createDynamicAttributeProblemDTO(String type, String componentId, String definingType, String definingTypeDisplayLabel, String attributeName, String attributeId, String attributeDisplayLabel, String localizedMessage, String developerMessage)
  {
    String dtoType = type + TypeGeneratorInfo.DTO_SUFFIX;
    AttributeProblemDTO retDTO = null;
    Class<?> clazz;
    try
    {
      clazz = LoaderDecorator.load(dtoType);
      Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
      constructor.setAccessible(true);

      retDTO = (AttributeProblemDTO) constructor.newInstance(type, componentId, definingType, definingTypeDisplayLabel, attributeName, attributeId, attributeDisplayLabel, localizedMessage);

      retDTO.setDeveloperMessage(developerMessage);
    }
    catch (SecurityException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalArgumentException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InstantiationException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (IllegalAccessException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (NoSuchMethodException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (InvocationTargetException e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }
    catch (RuntimeException e)
    {
      if (e instanceof LoaderDecoratorExceptionIF)
      {
        AttributeProblemDTO attributeProblemDTO = new AttributeProblemDTO(type, componentId, definingType, definingTypeDisplayLabel, attributeName, attributeId, attributeDisplayLabel, localizedMessage);
        attributeProblemDTO.setDeveloperMessage(developerMessage);
        return attributeProblemDTO;
      }
      else
      {
        throw e;
      }
    }

    // reset the type since this method mucks with the type for the sake of
    // avoiding infinite recursion.
    return retDTO;
  }

}
