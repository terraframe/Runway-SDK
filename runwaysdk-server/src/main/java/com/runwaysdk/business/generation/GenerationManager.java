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
package com.runwaysdk.business.generation;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ClassSignature;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.generation.CommonMarker;
import com.runwaysdk.generation.loader.GeneratedLoader;

public class GenerationManager
{
  /**
   * Generates the source code for a given mdType.
   *
   * @param mdType
   *          The mdType
   */
  public static void generate(MdTypeDAOIF mdType)
  {
    for (GeneratorIF generator : mdType.getGenerators())
    {
      generator.go(false);
    }
  }

  /**
   * Generates the source code for a given mdType.
   *
   * @param mdType
   *          The mdType
   */
  public static void forceRegenerate(MdTypeDAOIF mdType)
  {
    for (GeneratorIF generator : mdType.getGenerators())
    {
      generator.go(true);
    }
  }

  /**
   * Returns a list of all generated .java source files that are exist in the
   * server directory for a given MdType.
   *
   * @param mdType
   * @return
   */
  public static List<String> getServerFiles(MdTypeDAOIF mdType)
  {
    List<String> list = new LinkedList<String>();

    for (GeneratorIF generator : mdType.getGenerators())
    {
      if (generator instanceof ServerMarker)
      {
        String sourceFiles = generator.getPath();
        if (!sourceFiles.trim().equals(""))
        {
          try
          {
            Class<?> aClass = GeneratedLoader.isolatedClassLoader().loadClass(generator.getJavaType());
            Annotation annotation = aClass.getAnnotation(ClassSignature.class);
            ClassSignature classSignature = (ClassSignature) annotation;

            long hash = generator.getSerialVersionUID();

            if (classSignature == null || hash != classSignature.hash())
            {
              list.add(sourceFiles);
              // System.out.println("COMPILE: "+generator.getJavaType());
            }
            // else
            // {
            // System.out.println("SKIPPED COMPILE: "+generator.getJavaType());
            // }

          }
          catch (RuntimeException | ClassNotFoundException | MalformedURLException ex)
          {
            // if (ex instanceof LoaderDecoratorExceptionIF)
            // {
            list.add(sourceFiles);
            // }
            // else
            // {
            // throw ex;
            // }
          }
        }
      }
    }

    return list;
  }

  /**
   * Returns a list of all generated .java source files that are exist in the
   * common directory for a given MdType.
   *
   * @param mdType
   * @return
   */
  public static List<String> getCommonFiles(MdTypeDAOIF mdType)
  {
    List<String> list = new LinkedList<String>();

    for (GeneratorIF generator : mdType.getGenerators())
    {
      if (generator instanceof CommonMarker)
      {
        String sourceFiles = generator.getPath();
        if (!sourceFiles.trim().equals(""))
        {
          try
          {
            Class<?> aClass = GeneratedLoader.isolatedClassLoader().loadClass(generator.getJavaType());
            Annotation annotation = aClass.getAnnotation(ClassSignature.class);
            ClassSignature classSignature = (ClassSignature) annotation;

            long hash = generator.getSerialVersionUID();

            if (classSignature == null || hash != classSignature.hash())
            {
              list.add(sourceFiles);
            }
          }
          catch (RuntimeException | ClassNotFoundException | MalformedURLException ex)
          {
            // if (ex instanceof LoaderDecoratorExceptionIF)
            // {
            list.add(sourceFiles);
            // }
            // else
            // {
            // throw ex;
            // }
          }
        }
      }
    }

    return list;
  }

  /**
   * Returns a list of all generated .java source files that are exist in the
   * client directory for a given MdType.
   *
   * @param mdType
   * @return
   */
  public static List<String> getClientFiles(MdTypeDAOIF mdType)
  {
    List<String> list = new LinkedList<String>();

    for (GeneratorIF generator : mdType.getGenerators())
    {
      if (generator instanceof ClientMarker)
      {
        String sourceFiles = generator.getPath();
        if (!sourceFiles.trim().equals(""))
        {
          try
          {
            Class<?> aClass = GeneratedLoader.isolatedClassLoader().loadClass(generator.getJavaType());
            Annotation annotation = aClass.getAnnotation(ClassSignature.class);
            ClassSignature classSignature = (ClassSignature) annotation;

            long hash = generator.getSerialVersionUID();

            if (classSignature == null || hash != classSignature.hash())
            {
              list.add(sourceFiles);
            }
          }
          catch (RuntimeException | ClassNotFoundException | MalformedURLException ex)
          {
            // if (ex instanceof LoaderDecoratorExceptionIF)
            // {
            list.add(sourceFiles);
            // }
            // else
            // {
            // throw ex;
            // }
          }
        }
      }
    }

    return list;
  }
}
