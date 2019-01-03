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
package com.runwaysdk.controller.tag.develop;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.generation.CommonGenerationUtil;

public class TLDGenerator
{
  /**
   * Writer used to generate the xml
   */
  private FileMarkupWriter writer;

  /**
   * List of classes to examine when generating the TLD file.
   */
  private Class<?>[]   classes;

  private String       shortname;

  public TLDGenerator(File dest, Class<?>[] classes, String shortname) throws IOException
  {
    this.writer = new FileMarkupWriter(dest.getAbsolutePath());
    this.classes = classes;
    this.shortname = shortname;
  }

  public void generate()
  {    
    HashMap<String, String> tagLib = new HashMap<String, String>();
    tagLib.put("xmlns", "http://java.sun.com/xml/ns/javaee");
    tagLib.put("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    tagLib.put("xsi:schemaLocation", "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd");
    tagLib.put("version", "2.1");
    
    writer.writeValue("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
    writer.openTag("taglib", tagLib);

    this.writePreface();

    // Write tags definitions
    for (Class<?> c : classes)
    {
      TagAnnotation tag = c.getAnnotation(TagAnnotation.class);

      if (tag != null)
      {
        writer.openTag("tag");

        writerTag(c, tag);

        for (Method method : c.getMethods())
        {
          AttributeAnnotation attribute = method.getAnnotation(AttributeAnnotation.class);

          if (attribute != null)
          {
            writer.openTag("attribute");

            writeAttribute(method, attribute);

            writer.closeTag();
          }
        }

        writer.closeTag();
      }
    }

    // Write function definitions
    for (Class<?> c : classes)
    {
      for (Method method : c.getMethods())
      {
        FunctionAnnotation function = method.getAnnotation(FunctionAnnotation.class);

        if (function != null)
        {
          writer.openTag("function");

          writeFunction(c, method, function);

          writer.closeTag();
        }
      }
    }

    writer.closeTag();
  }

  private void writeFunction(Class<?> c, Method method, FunctionAnnotation function)
  {
    StringBuffer signature = new StringBuffer();

    signature.append(method.getReturnType().getName());
    signature.append(" ");
    signature.append(method.getName());
    signature.append("(");

    for(Class<?> parameter : method.getParameterTypes())
    {
      signature.append(", " + parameter.getName());
    }

    signature.append(")");
    
    writer.writeTagSingleValue("name", method.getName());
    writer.writeTagSingleValue("function-class", c.getName());
    writer.writeTagSingleValue("function-signature", signature.toString().replaceFirst(", ", ""));
  }

  private void writeAttribute(Method method, AttributeAnnotation attribute)
  {
    String name = method.getName().replace(CommonGenerationUtil.GET, "");
    name = name.substring(0, 1).toLowerCase() + name.substring(1);
    
    writer.writeTagSingleValue("description", attribute.description());
    writer.writeTagSingleValue("name", name);
    writer.writeTagSingleValue("required", new Boolean(attribute.required()).toString());
    writer.writeTagSingleValue("rtexprvalue", new Boolean(attribute.rtexprvalue()).toString());    
  }

  private void writerTag(Class<?> c, TagAnnotation jspTag)
  {
    writer.writeTagSingleValue("description", jspTag.description());
    writer.writeTagSingleValue("name", jspTag.name());
    writer.writeTagSingleValue("tag-class", c.getName());
    writer.writeTagSingleValue("body-content", jspTag.bodyContent());
  }

  private void writePreface()
  {
    writer.writeTagSingleValue("tlib-version", "1.0");
    writer.writeTagSingleValue("short-name", shortname);
  }

  public static void main(String[] args)
  {
//    try
//    {
//      // Generate runway form tags
//      new TLDGenerator(new File(args[0]), new Class<?>[] { SelectTagSupport.class,
//          CommandTagSupport.class, ComponentTagSupport.class, FormTagSupport.class,
//          InputTagSupport.class, TextAreaTagSupport.class, BooleanTagSupport.class, OptionTagSupport.class,
//          StandardTagSupport.class, GroupTagSupport.class, GroupOptionTagSupport.class, MessagesTagSupport.class,
//          MessageTagSupport.class, ComponentsTagSupport.class, StructTagSupport.class,
//          PropertyTagSupport.class, CommandLinkTagSupport.class, FunctionSupport.class,
//          TableTagSupport.class, ColumnsTagSupport.class, AttributeColumnTagSupport.class,
//          FreeColumnTagSupport.class, StructColumnTagSupport.class, HeaderTagSupport.class, RowTagSupport.class,
//          FooterTagSupport.class, ContextTagSupport.class, DTTagSupport.class,
//          PaginationTagSupport.class, PageTagSupport.class, FieldPropertyTagSupport.class}, "Runway").generate();
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }
}
