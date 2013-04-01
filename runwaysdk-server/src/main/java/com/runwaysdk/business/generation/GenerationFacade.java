/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.util.FileIO;

public class GenerationFacade
{
  private static final boolean USE_ASPECT_COMPILER = ServerProperties.compileTimeWeaving();

  static volatile HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();

  public static String getDTOstubSource(MdTypeDAOIF mdTypeDAOIF)
  {
    String stubSourceDirectory =  AbstractClientGenerator.getRootClientOrCommonStubDirectory(mdTypeDAOIF);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdTypeDAOIF.definesType());

    File file = getFile(stubSourceDirectory, fileSystemType, TypeGeneratorInfo.DTO_SUFFIX+".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  public static byte[] getDTOstubClass(MdTypeDAOIF type)
  {
    return ClassManager.readDTOstubClasses(type);
  }

  public static String getDTObaseSource(MdTypeDAOIF mdTypeDAOIF)
  {
    String baseSourceDirectory =  AbstractClientGenerator.getRootClientOrCommonBaseDirectory(mdTypeDAOIF);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdTypeDAOIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, TypeGeneratorInfo.DTO_BASE_SUFFIX+".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
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
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  public static String getStubSource(MdTypeDAOIF type)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(type);
    String stubSourceDirectory =  AbstractServerGenerator.getRootServerStubDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(type.definesType());

    File file = getFile(stubSourceDirectory, fileSystemType, ".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
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
    String baseSourceDirectory =  AbstractServerGenerator.getRootServerBaseDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdEntityIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, EntityQueryAPIGenerator.QUERY_API_SUFFIX+".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  public static String getQueryAPIbaseSource(MdViewDAOIF mdViewIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdViewIF);
    String baseSourceDirectory =  AbstractServerGenerator.getRootServerBaseDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdViewIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, ViewQueryBaseAPIGenerator.QUERY_API_BASE_SUFFIX+".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  public static String getQueryAPIstubSource(MdViewDAOIF mdViewIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdViewIF);
    String stubSourceDirectory =  AbstractServerGenerator.getRootServerStubDirectory(fileSystemPackage);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdViewIF.definesType());

    File file = getFile(stubSourceDirectory, fileSystemType, ViewQueryStubAPIGenerator.QUERY_API_STUB_SUFFIX+".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  public static String getQueryDTOsource(MdClassDAOIF mdClassDAOIF)
  {
    String baseSourceDirectory =  AbstractClientGenerator.getRootClientOrCommonBaseDirectory(mdClassDAOIF);
    String fileSystemType = CommonGenerationUtil.replaceTypeDotsWithSlashes(mdClassDAOIF.definesType());

    File file = getFile(baseSourceDirectory, fileSystemType, TypeGeneratorInfo.QUERY_DTO_SUFFIX+".java");
    try
    {
      return FileIO.readString(file);
    }
    catch(IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  public static byte[] getGeneratedServerClasses(MdFacadeDAOIF mdFacadeIF)
  {
    return ClassManager.readGeneratedServerClasses(mdFacadeIF);
  }

  public static byte[] getGeneratedCommonClasses(MdFacadeDAOIF mdFacadeIF)
  {
    return ClassManager.readGeneratedCommonClasses(mdFacadeIF);
  }

  public static byte[] getGeneratedClientClasses(MdFacadeDAOIF mdFacadeIF)
  {
    return ClassManager.readGeneratedClientClasses(mdFacadeIF);
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
      if (!( GenerationUtil.isReservedType(typeDAOIF)) &&
          GenerationUtil.shouldGenerate(typeDAOIF))
      {
        GenerationManager.generate(typeDAOIF);
        typesToCompile.add(typeDAOIF);
      }
    }
    if (USE_ASPECT_COMPILER) new AspectJCompiler().compile(typesToCompile);
    else new EclipseCompiler().compile(typesToCompile);
  }

  public static void forceRegenerateAndCompile(Collection<MdTypeDAOIF> mdTypes)
  {
    for (MdTypeDAOIF type : mdTypes)
    {
      if (! GenerationUtil.isReservedType(type) )
      {
        GenerationManager.forceRegenerate(type);
      }
    }
    if (USE_ASPECT_COMPILER) new AspectJCompiler().compile(mdTypes);
    else new EclipseCompiler().compile(mdTypes);
  }

  /**
   * Compiles all classes using the ECJ, but produces no .class files. This
   * serves to check for references to dropped attributes or classes.
   */
  public static void compileAllNoOutput()
  {
    new EclipseCompiler().compileAllNoOutput();
  }

  public static void compileAll()
  {
    if (USE_ASPECT_COMPILER) new AspectJCompiler().compileAll();
    else new EclipseCompiler().compileAll();
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
    
    if (USE_ASPECT_COMPILER)
    {
      new AspectJCompiler().compile(mdTypes);
    }
    else 
    {
      new EclipseCompiler().compile(mdTypes);
    }
  }
}
