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
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.util.FileIO;

public class GenerationFacade
{
  static volatile HashMap<String, Class<?>> classes             = new HashMap<String, Class<?>>();

  public static String getDTOstubSource(MdTypeDAOIF mdTypeDAOIF)
  {
    String stubSourceDirectory = AbstractClientGenerator.getRootClientOrCommonStubDirectory(mdTypeDAOIF);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdTypeDAOIF.definesType());

    File file = getFile(stubSourceDirectory, fileSystemType, TypeGeneratorInfo.DTO_SUFFIX + ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static byte[] getDTOstubClass(MdTypeDAOIF type)
  {
    return ClassManager.readDTOstubClasses(type);
  }

  public static String getDTObaseSource(MdTypeDAOIF mdTypeDAOIF)
  {
    String baseSourceDirectory = AbstractClientGenerator.getRootClientOrCommonBaseDirectory(mdTypeDAOIF);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdTypeDAOIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, TypeGeneratorInfo.DTO_BASE_SUFFIX + ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static byte[] getDTObaseClass(MdTypeDAOIF type)
  {
    return ClassManager.readDTObaseClasses(type);
  }

  public static String getSource(AbstractGenerator generator)
  {
    File file = new File(generator.getPath());
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static String getStubSource(MdTypeDAOIF type)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(type);
    String stubSourceDirectory = AbstractServerGenerator.getRootServerStubDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(type.definesType());

    File file = getFile(stubSourceDirectory, fileSystemType, ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static byte[] getStubClass(MdTypeDAOIF type)
  {
    return ClassManager.readStubClasses(type);
  }

  public static byte[] getBaseClass(MdTypeDAOIF type)
  {
    return ClassManager.readBaseClasses(type);
  }

  public static byte[] getClassBytes(AbstractGenerator generator)
  {
    return ClassManager.readClasses(generator);
  }

  public static String getQueryAPIsource(MdEntityDAOIF mdEntityIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdEntityIF);
    String baseSourceDirectory = AbstractServerGenerator.getRootServerBaseDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdEntityIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, EntityQueryAPIGenerator.QUERY_API_SUFFIX + ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static String getQueryAPIbaseSource(MdViewDAOIF mdViewIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdViewIF);
    String baseSourceDirectory = AbstractServerGenerator.getRootServerBaseDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdViewIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, ViewQueryBaseAPIGenerator.QUERY_API_BASE_SUFFIX + ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static String getQueryAPIstubSource(MdViewDAOIF mdViewIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdViewIF);
    String stubSourceDirectory = AbstractServerGenerator.getRootServerStubDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdViewIF.definesType());

    File file = getFile(stubSourceDirectory, fileSystemType, ViewQueryStubAPIGenerator.QUERY_API_STUB_SUFFIX + ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  public static String getQueryDTOsource(MdClassDAOIF mdClassDAOIF)
  {
    String baseSourceDirectory = AbstractClientGenerator.getRootClientOrCommonBaseDirectory(mdClassDAOIF);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdClassDAOIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, TypeGeneratorInfo.QUERY_DTO_SUFFIX + ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch (IOException e)
    {
      if (!LocalProperties.isDevelopEnvironment())
      {
        throw new FileReadException(file, e);
      }
    }

    return null;
  }

  private static File getFile(String directory, String type, String suffix)
  {
    return new File(directory + type.replace('.', '/') + suffix);
  }

  public static byte[] getQueryAPIclasses(MdEntityDAOIF mdEntityIF)
  {
    return ClassManager.readQueryAPIclasses(mdEntityIF);
  }

  public static byte[] getQueryBaseAPIclasses(MdViewDAOIF mdViewIF)
  {
    return ClassManager.readQueryBaseAPIclasses(mdViewIF);
  }

  public static byte[] getQueryStubAPIclasses(MdViewDAOIF mdViewIF)
  {
    return ClassManager.readQueryStubAPIclasses(mdViewIF);
  }

  public static byte[] getQueryDTOclasses(MdTypeDAOIF type)
  {
    return ClassManager.readQueryDTOclasses(type);
  }

  public static void generateAndCompile(Collection<MdTypeDAOIF> mdTypes)
  {
    List<MdTypeDAOIF> typesToCompile = new LinkedList<MdTypeDAOIF>();

    for (MdTypeDAOIF typeDAOIF : mdTypes)
    {
      if (! ( GenerationUtil.isSkipCompileAndCodeGeneration(typeDAOIF) ) && GenerationUtil.shouldGenerate(typeDAOIF))
      {
        GenerationManager.generate(typeDAOIF);
        typesToCompile.add(typeDAOIF);
      }
    }
    
    new DelegateCompiler().compile(typesToCompile);
  }

  /**
   * Force regeneration of the system types.
   */
  public static void regenerateSystemBaseClasses()
  {
    Collection<MdTypeDAOIF> mdTypes = new LinkedList<MdTypeDAOIF>();

    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery mdTypeQ = qFactory.businessDAOQuery(MdTypeInfo.CLASS);
    OIterator<BusinessDAOIF> it = mdTypeQ.getIterator();

    while (it.hasNext())
    {
      MdTypeDAOIF mdTypeDAOIF = (MdTypeDAOIF)it.next();
      
      if (mdTypeDAOIF.isSystemPackage())
      {
        mdTypes.add(mdTypeDAOIF);
        
System.out.println("Heads up: will regenerate "+mdTypeDAOIF.getTypeName());
      }
    }
    
    regenerateAndCompile(mdTypes);
  }
  
  public static void regenerateAndCompile(Collection<MdTypeDAOIF> mdTypes)
  {
    for (MdTypeDAOIF type : mdTypes)
    {
      if (!GenerationUtil.isSkipCompileAndCodeGeneration(type))
      {
        GenerationManager.generate(type);
      }
    }
    
System.out.println("\nHeads up: Finished code generation. Starting to compile. \n");

    new DelegateCompiler().compile(mdTypes);
  }
  
  public static void forceRegenerateAndCompile(Collection<MdTypeDAOIF> mdTypes)
  {
    for (MdTypeDAOIF type : mdTypes)
    {
      if (!GenerationUtil.isSkipCompileAndCodeGeneration(type))
      {
        GenerationManager.forceRegenerate(type);
      }
    }
    
    new DelegateCompiler().compile(mdTypes);
  }
  
  /**
   * Compiles all classes (without aspect weaving!), but produces no .class files. This
   * serves to check for references to dropped attributes or classes.
   */
  public static void compileAllNoOutput()
  {
    ServerProperties.getJavaCompiler().compileAllNoOutput();
  }

  public static void compileAll()
  {
    new DelegateCompiler().compileAll();
  }

  /**
   * Compiles the source for the given types. Assumes that the source files have
   * already been generated.
   * 
   * @param mdTypes
   */
  public static void compile(Collection<? extends MdTypeDAOIF> mdTypes)
  {
    if (mdTypes.isEmpty())
    {
      return;
    }

    new DelegateCompiler().compile(mdTypes);
  }
}
