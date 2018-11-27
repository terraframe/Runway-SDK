/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.controller;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.format.ParseException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.mvc.ParseType;
import com.runwaysdk.mvc.conversion.BasicJSONToComponentDTO;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.transport.conversion.json.JSONToComponentDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeLocalMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;

/**
 * Scraps values from a {@link HttpServletRequest} and returns a corresponding
 * java object.
 * 
 * @author Justin Smethie
 */
public class RequestScraper
{
  /**
   * Information about the parameter
   */
  private ParameterIF                 parameter;

  /**
   * Parameters from the request.
   */
  private Map<String, ParameterValue> values;

  /**
   * Manager of the request
   */
  private RequestManager              manager;

  public RequestScraper(ParameterIF parameter, RequestManager manager, Map<String, ParameterValue> values)
  {
    this.parameter = parameter;
    this.manager = manager;
    this.values = values;
  }

  /**
   * @return Type-safe java object representing the parameter
   */
  public Object convert()
  {
    Class<?> c = LoaderDecorator.load(this.parameter.getType());

    if (c.isArray() && DispatchUtil.isPrimitive(c))
    {
      return convertPrimitiveArray(c);
    }
    else if (c.isArray())
    {
      return convertDTOArray(c);
    }
    else if (MultipartFileParameter.class.isAssignableFrom(c))
    {
      return (MultipartFileParameter) this.getParameter(this.parameter.getName());
    }
    else
    {
      String value = getValue(this.parameter.getName());

      return convertValue(c, value);
    }
  }

  /**
   * Scraps the request for an array of DTOs
   * 
   * @param c
   *          The DTO .class object
   * 
   * @return A scraped array of objects corresponding to the given class.
   */
  private Object[] convertDTOArray(Class<?> c)
  {
    // List of the scraped objects
    List<Object> list = new LinkedList<Object>();

    // Enumeration of all the parameters defined in the request
    Set<String> parameterNames = values.keySet();

    // Index counter
    int i = 0;

    // Base class of the array
    Class<?> baseComponent = c.getComponentType();

    // Loop while the request contains parameters for the object of the next
    // index in the array
    while (this.containsBaseParameter(parameterNames, i))
    {
      // Recursively scrape the parameter for the individual object
      String baseName = baseComponent.getName();
      String baseIndex = this.parameter.getName() + "_" + i;

      Object object = new RequestScraper(new ParameterWrapper(baseName, baseIndex), manager, values).convert();
      list.add(object);
      i++;
    }

    // Convert the list to an array of the given type
    return list.toArray((Object[]) Array.newInstance(baseComponent, list.size()));
  }

  /**
   * Performs a linear search of the parameter names defined in the request for
   * a parameter with the given index appended to the end of its name.
   * 
   * @param names
   *          Enumeration of all the parameter names
   * @param index
   *          Index to search for
   * 
   * @return If a parameter with the given index exists
   */
  private boolean containsBaseParameter(Set<String> names, int index)
  {
    Iterator<String> namesIter = names.iterator();
    while (namesIter.hasNext())
    {
      String base = namesIter.next();
      if (base.contains(this.parameter.getName() + "_" + index))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Scraps the request for an array of primitive objects
   * 
   * @param c
   *          The .java equivalent of the primitive .class object
   * 
   * @return A scraped array of objects corresponding to the given class.
   */
  private Object[] convertPrimitiveArray(Class<?> c)
  {
    // Assumption: All arrays are of a single dimension only!
    String[] values = getValues(this.parameter.getName());

    // Assume zero, and only change if he have actual values to set.
    int length = 0;
    if (values != null)
    {
      length = values.length;
    }

    Object[] objects = (Object[]) Array.newInstance(c.getComponentType(), length);
    Class<?> componentType = objects.getClass().getComponentType();

    for (int i = 0; i < length; i++)
    {
      objects[i] = this.convertValue(componentType, values[i]);
    }

    return objects;
  }

  /**
   * Converts a single parameter to its type-safe java representation
   * 
   * @param c
   *          Class of the java representation
   * @param value
   *          Value to convert
   * 
   * @return Type-safe java object of the given class with the given value
   */
  @SuppressWarnings("unchecked")
  private <T> T convertValue(Class<T> c, String value)
  {
    if (DispatchUtil.isPrimitive(c))
    {
      return (T) this.convertPrimitive(c, value);
    }
    else if (ClientRequestIF.class.isAssignableFrom(c))
    {
      return (T) this.manager.getClientRequest();
    }
    else if (ClientSession.class.isAssignableFrom(c))
    {
      return (T) this.manager.getClientSession();
    }
    else if (Map.class.isAssignableFrom(c))
    {
      return (T) this.values;
    }
    else if (ServletRequestIF.class.isAssignableFrom(c))
    {
      return (T) this.manager.getReq();
    }
    else if (this.parameter.getParseType().equals(ParseType.RUNWAY_JSON))
    {
      JSONToComponentDTO converter = JSONToComponentDTO.getConverter(manager.getClientRequest().getSessionId(), manager.getLocale(), value);
      return (T) converter.populate();
    }
    else if (this.parameter.getParseType().equals(ParseType.BASIC_JSON))
    {
      BasicJSONToComponentDTO converter = BasicJSONToComponentDTO.getConverter(manager.getClientRequest().getSessionId(), manager.getLocale(), this.parameter.getType(), value);
      return (T) converter.populate();
    }
    else
    {
      // c is not primitive, must be a DTO defined type
      try
      {
        // Get the oid and isNew status of the DTO
        String name = this.parameter.getName();

        String oid = getValue(name + ".componentId");
        Boolean isNew = Boolean.parseBoolean(getValue(name + ".isNew"));
        String isDisconnected = this.getValue("#" + name + ".disconnected");
        ClientRequestIF clientRequestIF = manager.getClientRequest();
        boolean disconnected = false;

        if (isNew)
        {
          // If the DTO is new then create a new instance from the client
          // request and fill the values of the DTO from the scraped parameters
          Class<?> concreteClass = c;
          String actualType = this.getValue("#" + name + ".actualType");

          if (actualType != null)
          {
            actualType += TypeGeneratorInfo.DTO_SUFFIX;
            concreteClass = LoaderDecorator.load(actualType);
          }

          if (isDisconnected != null)
          {
            disconnected = Boolean.parseBoolean(isDisconnected);
          }

          if (!disconnected)
          {
            MutableDTO mutableDTO = this.getNewInstance(concreteClass, clientRequestIF);
            this.convertDTO(mutableDTO, name);

            return (T) mutableDTO;
          }
          else
          {
            EntityDTO entity = clientRequestIF.newDisconnectedEntity(concreteClass.getName());
            this.convertDTO(entity, name);

            return (T) entity;
          }
        }
        else
        {
          // Ensure that an oid has been supplied, otherwise return null because
          // it is impossible to perform a get without an oid.
          if (oid == null || oid.equals(""))
          {
            return null;
          }

          // If the DTO already exists then retrieve the DTO from the client
          // request. And update the values of the DTO from the scraped
          // parameter
          Method method = c.getMethod(CommonGenerationUtil.GET, ClientRequestIF.class, String.class);
          MutableDTO mutableDTO = (MutableDTO) method.invoke(null, clientRequestIF, oid);
          this.convertDTO(mutableDTO, name);

          return (T) mutableDTO;
        }
      }
      catch (Throwable t)
      {
        if (t instanceof InvocationTargetException)
        {
          t = ( (InvocationTargetException) t ).getTargetException();
        }

        if (t instanceof RuntimeException)
        {
          throw (RuntimeException) t;
        }
        else
        {
          // FIXME Change exception handling mech.
          throw new RuntimeException(t);
        }
      }
    }
  }

  /**
   * Converts the String representation of a primitive object into the primitive
   * object. If an error occurs during conversion then an AttributeNotification
   * is added to the clientRequest and a default value is returned. The default
   * value is discarded in the error handling phase.
   * 
   * @param c
   *          Primitive class
   * @param value
   *          String representation of the primitive value
   * 
   * @return An Object of the given class with the corresponding value
   */
  private Object convertPrimitive(Class<?> c, String value)
  {
    if (value == null || value.equals(""))
    {
      return null;
    }

    Locale locale = manager.getReq().getLocale();

    try
    {
      FormatFactory factory = AbstractFormatFactory.getFormatFactory();
      return factory.getFormat(c).parse(value, locale);
    }
    catch (ParseException e)
    {
      ParameterFactory factory = new ParameterFactory(c, manager.newClientRequest());
      manager.addAttributeNotificationDTO(factory.getException(this.parameter.getName(), locale, value));

      // The provided value does not convert so return the class which is
      // expected
      // The class is used in error pathing
      return c;
    }
  }

  /**
   * Loads all relevant parameter values into the given DTO
   * 
   * @param mutableDTO
   *          {@link MutableDTO} to load
   * @param baseName
   *          Base name of the DTO parameter. E.g: basename.attributename
   * 
   * @throws Exception
   */
  private void convertDTO(MutableDTO mutableDTO, String baseName)
  {
    // DTO value hash: name.accessorName.structAcessorName
    for (String attributeName : mutableDTO.getAttributeNames())
    {
      String parameterName = baseName + "." + attributeName;

      // Root name of all the accessor's getters and setters

      try
      {
        DTOFacade facade = new DTOFacade(attributeName, mutableDTO);

        AttributeMdDTO attributeDTO = facade.getAttributeMdDTO();

        if (attributeDTO instanceof AttributeLocalMdDTO)
        {
          LocalStructDTO struct = (LocalStructDTO) facade.getValue();

          String _value = this.getValue(parameterName);
          struct.setValue(_value);
        }
        else if (attributeDTO instanceof AttributeStructMdDTO)
        {
          // attribute is a struct attribute
          MutableDTO structDTO = (MutableDTO) facade.getValue();

          this.convertDTO(structDTO, parameterName);
        }
        else if (attributeDTO instanceof AttributeEnumerationMdDTO)
        {
          // first try accessing the enum values directly, but if that
          // fails, try using the [] suffix because Ajax may have posted
          // with that syntax (FIXME find common solution)
          String[] enumNames = getValues(parameterName);
          if (enumNames == null)
          {
            enumNames = getValues(parameterName + "[]");
          }

          if (enumNames != null)
          {
            Class<?> javaType = attributeDTO.getJavaType();

            // Clear the existing values of the attribute enumeration
            facade.clearAttribute();

            // Add all of the selected enumerated items into the enumeration
            for (String enumName : enumNames)
            {
              if (enumName != null && enumName.trim().length() > 0)
              {
                Object enumeration = javaType.getMethod("valueOf", String.class).invoke(null, enumName);

                facade.addAttribute(enumeration);
              }
            }
          }
        }
        else if (attributeDTO instanceof AttributeReferenceMdDTO)
        {
          String oid = manager.getReq().getParameter(parameterName);

          if (oid == null || oid.equals(""))
          {
            String[] values = manager.getReq().getParameterValues(parameterName);

            if (values == null)
            {
              values = this.getValues(parameterName + "[]");
            }

            if (values == null)
            {
              values = this.getValues(parameterName);
            }

            if (values != null && values.length > 0)
            {
              oid = values[0];
            }
          }

          if (oid != null && !oid.equals(""))
          {
            Class<?> javaType = attributeDTO.getJavaType();

            // Get the get method for the reference
            Method get = javaType.getMethod(CommonGenerationUtil.GET, ClientRequestIF.class, String.class);
            Object reference = get.invoke(null, manager.getClientRequest(), oid);

            facade.setValue(reference);
          }
          else if (oid != null)
          {
            facade.setValue(null);
          }
        }
        else
        {
          // Get the name of the setter method
          this.setValue(facade, getValue(parameterName));
        }
      }
      catch (Exception e)
      {
        // A parsing error occured while scrapping values. Use the type unsafe
        // setter to set the invalid value onto the DTO to be used for error
        // handling.
        try
        {
          AttributeMdDTO attributeDTO = new DTOFacade(attributeName, mutableDTO).getAttributeMdDTO();

          String value = getValue(parameterName);

          ParameterFactory factory = new ParameterFactory(attributeDTO.getJavaType(), manager.newClientRequest());
          Locale locale = manager.getReq().getLocale();

          ParseProblemDTO exception = factory.getException(mutableDTO, attributeDTO, locale, value);

          manager.addAttributeNotificationDTO(exception);
        }
        catch (Exception e1)
        {
          // TODO Auto-generated catch block
          throw new RuntimeException(e1);
        }
      }
    }
  }

  /**
   * Sets the value of an attribute on the given DTO.
   * 
   * @param mutableDTO
   *          {@link MutableDTO} to set value upon.
   * @param accessorName
   *          Name of the attribute accessor
   * @param genericValue
   *          String representation of the value.
   * 
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalAccessException
   * @throws SecurityException
   * @throws IllegalArgumentException
   */
  private void setValue(DTOFacade facade, String genericValue) throws Exception
  {
    if (genericValue == null)
    {
      return;
    }

    FormatFactory factory = AbstractFormatFactory.getFormatFactory();

    Class<?> javaType = facade.getAttributeMdDTO().getJavaType();

    Object value = null;

    // Convert the request parameter to its corresponding java type
    if (!genericValue.equals(""))
    {
      value = factory.getFormat(javaType).parse(genericValue, manager.getReq().getLocale());
    }

    // Invoke the setter method
    facade.setValue(value);
  }

  /**
   * @param c
   *          Class of the new instance
   * @param clientRequestIF
   *          ClientRequestIF
   * @return A new instance of the given class
   * 
   * @throws IllegalArgumentException
   * @throws SecurityException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  private <T> MutableDTO getNewInstance(Class<T> c, ClientRequestIF clientRequestIF) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
  {
    if (RelationshipDTO.class.isAssignableFrom(c))
    {
      String name = this.parameter.getName();
      String parentOid = getValue("#" + name + ".parent.oid");
      String childOid = getValue("#" + name + ".child.oid");

      return (MutableDTO) c.getConstructor(ClientRequestIF.class, String.class, String.class).newInstance(clientRequestIF, parentOid, childOid);
    }

    return (MutableDTO) c.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
  }

  /**
   * @param methodName
   * @param c
   *          {@link Class} of the desired {@link Method}
   * @return The first {@link Method} with the corresponding method name.
   */
  public static Method getMethod(String methodName, Class<?> c)
  {
    Method[] methods = c.getMethods();

    // Get the method corresponding to the action name.
    // This is ok because method overloading is not allowed
    // in the metadata.
    for (Method m : methods)
    {
      if (m.getName().equals(methodName))
      {
        return m;
      }
    }

    return null;
  }

  /**
   * Returns a single value from the parameters. If the key is not found then
   * null is returned.
   * 
   * @param key
   * @return
   */
  private String getValue(String key)
  {
    ParameterValue parameter = this.getParameter(key);

    if (parameter != null)
    {
      return parameter.getSingleValue();
    }

    return null;
  }

  /**
   * Returns multiple values from the parameters. If the key is not found then
   * null is returned.
   * 
   * @param key
   * @return
   */
  private String[] getValues(String key)
  {
    ParameterValue parameter = this.getParameter(key);

    if (parameter != null)
    {
      return parameter.getValuesAsArray();
    }

    return null;
  }

  /**
   * @param key
   * @return
   */
  private ParameterValue getParameter(String key)
  {
    if (values.containsKey(key))
    {
      return values.get(key);

    }
    // FIXME this is awkward
    // The key name might have a [] suffix if coming from a select list in Ajax
    else if (values.containsKey(key + "[]"))
    {
      return values.get(key + "[]");
    }

    return null;
  }

}