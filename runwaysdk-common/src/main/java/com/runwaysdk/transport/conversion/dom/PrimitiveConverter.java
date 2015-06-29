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
package com.runwaysdk.transport.conversion.dom;

import java.lang.reflect.Constructor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.transport.conversion.PrimitiveLookup;

public class PrimitiveConverter
{
  public static Object getPrimitiveObject(Node parent)
  {
    Object object = null;

    // get the information from the DOM
    NodeList children = parent.getChildNodes();

    Node typeNode = children.item(0);
    String primitiveType = typeNode.getTextContent();

    Node valueNode = children.item(1);
    String value = valueNode.getTextContent();

    try
    {
      // create a new instance with reflection
      String javaType = PrimitiveLookup.getJavaTypeFromPrimitiveType(primitiveType);
      Class<?> clazz = LoaderDecorator.load(javaType);

      // chars require special handling
      if(clazz.getName().equals(Character.class.getName()))
      {
        Constructor<?> constructor = clazz.getConstructor(char.class);
        char charValue = value.charAt(0);
        object = constructor.newInstance(charValue);
      }
      else
      {
        Constructor<?> constructor = clazz.getConstructor(String.class);
        object = constructor.newInstance(value);
      }
    }
    catch (Throwable e)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), e.getMessage(), e);
    }

    return object;
  }

  protected static Object getPrimitiveObject(Document document)
  {
    Element root = document.getDocumentElement();
    return getPrimitiveObject(root);
  }

  public static Element getPrimitiveDOM(Object object, Document document)
  {
    Element root = document.createElement(Elements.PRIMITIVE.getLabel());

    // add the type
    String type = object.getClass().getName();
    Element element = document.createElement(Elements.PRIMITIVE_TYPE.getLabel());
    String primitiveType = PrimitiveLookup.getPrimitiveTypeFromJavaType(type);
    element.setTextContent(primitiveType);
    root.appendChild(element);

    // add the value
    String value = object.toString();
    element = document.createElement(Elements.PRIMITIVE_VALUE.getLabel());
    element.setTextContent(value);
    root.appendChild(element);

    return root;
  }
}
