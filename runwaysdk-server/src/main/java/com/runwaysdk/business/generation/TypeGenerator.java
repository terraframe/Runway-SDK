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

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;


public abstract class TypeGenerator extends AbstractServerGenerator
{
  protected static String dtoPath = LocalProperties.getClientGenSrc();

  /**
   * Generates a file in the Server directory with the given package and file name
   *
   * @param mdTypeDAOIF Type for which this generator will generate code artifacts.
   * @param fileName The name of the file to generate
   */
  public TypeGenerator(MdTypeDAOIF mdTypeDAOIF, String fileName)
  {
    super(mdTypeDAOIF, fileName);
  }

  /**
   * Returns the complete path of the stub java file for the given MdType.
   *
   * @param mdTypeIF
   *
   * @return complete path of the stub java file for the given MdType.
   */
  public static String getJavaSrcFilePath(MdTypeDAOIF mdTypeIF)
  {
    String pack = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return AbstractServerGenerator.getRootServerStubDirectory(pack) + pack + mdTypeIF.getTypeName() + ".java";
  }

  /**
   * Returns the complete path of the dto stub .class file for the given MdType
   *
   * @param mdTypeIF
   * @return
   */
  public static File getDTOstubClassDirectory(MdTypeDAOIF mdTypeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the dto stub .java file for the given MdType
   * @param mdTypeIF
   * @return
   */
  public static String getDTOstubSrcFilePath(MdTypeDAOIF mdTypeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return AbstractClientGenerator.getRootClientOrCommonStubDirectory(mdTypeIF) + fileSystemPackage + mdTypeIF.getTypeName() + TypeGeneratorInfo.DTO_SUFFIX + ".java";
  }

  /**
   * Returns the complete path of the dto base .class file for the given MdType
   *
   * @param mdTypeIF
   * @return
   */
  public static File getDTObaseClassDirectory(MdTypeDAOIF mdTypeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the dto base .java file for the given MdType
   * @param mdTypeIF
   * @return
   */
  public static String getDTObaseSrcFilePath(MdTypeDAOIF mdTypeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return AbstractClientGenerator.getRootClientOrCommonBaseDirectory(mdTypeIF) + fileSystemPackage + mdTypeIF.getTypeName() + TypeGeneratorInfo.DTO_BASE_SUFFIX + ".java";

  }

  /**
   * Returns the complete path of the stub class file for the given MdType.
   *
   * @param mdTypeIF
   *
   * @return complete path of the stub class file for the given MdType.
   */
  public static File getStubClassDirectory(MdTypeDAOIF mdTypeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the generated server source files for the given MdFacadeIF.
   *
   * @param mdFacadeIF
   *
   * @return complete path of the generated server source files for the given MdFacadeIF.
   */
  public static String getGeneratedServerSourceDirectory(MdFacadeDAOIF mdFacadeIF)
  {
    String pack = GenerationUtil.getPackageForFileSystem(mdFacadeIF);
    return AbstractServerGenerator.getRootServerBaseDirectory(pack) + pack;
  }

  /**
   * Returns the complete path of the generated server class files for the given MdFacadeIF.
   *
   * @param mdFacadeIF
   *
   * @return complete path of the generated server class files for the given MdFacadeIF.
   */
  public static File getGeneratedServerClassesDirectory(MdFacadeDAOIF mdFacadeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdFacadeIF);
    return new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
  }


  /**
   * Returns the complete path of the generated common source files for the given MdFacadeIF.
   *
   * @param mdFacadeIF
   *
   * @return complete path of the generated common source files for the given MdFacadeIF.
   */
  public static String getGeneratedCommonSourceDirectory(MdFacadeDAOIF mdFacadeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdFacadeIF);
    return AbstractCommonGenerator.getRootCommonBaseDirectory(fileSystemPackage) + fileSystemPackage;
  }

  /**
   * Returns the complete path of the generated common class files for the given MdFacadeIF.
   *
   * @param mdFacadeIF
   *
   * @return complete path of the generated common class files for the given MdFacadeIF.
   */
  public static File getGeneratedCommonClassesDirectory(MdFacadeDAOIF mdFacadeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdFacadeIF);
    return new File(AbstractCommonGenerator.getRootCommonBaseDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the generated client source files for the given MdFacadeIF.
   *
   * @param mdFacadeIF
   *
   * @return complete path of the generated client source files for the given MdFacadeIF.
   */
  public static String getGeneratedClientSourceDirectory(MdFacadeDAOIF mdFacadeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdFacadeIF);
    return AbstractClientGenerator.getRootClientBaseDirectory(mdFacadeIF) + fileSystemPackage;
  }

  /**
   * Returns the complete path of the generated client class files for the given MdFacadeIF.
   *
   * @param mdFacadeIF
   *
   * @return complete path of the generated client class files for the given MdFacadeIF.
   */
  public static File getGeneratedClientClassesDirectory(MdFacadeDAOIF mdFacadeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdFacadeIF);
    return new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the Base java file for the given MdType.
   *
   * @param mdTypeIF
   *
   * @return complete path of the Base java file for the given MdType.
   */
  public static String getBaseSrcFilePath(MdTypeDAOIF mdTypeIF)
  {
    if (mdTypeIF instanceof MdEnumerationDAOIF)
      return getJavaSrcFilePath(mdTypeIF);

    String pack = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return AbstractServerGenerator.getRootServerBaseDirectory(pack)  + pack + mdTypeIF.getTypeName() + "Base.java";
  }

  /**
   * Returns the complete path of the Base class file for the given MdType.
   *
   * @param mdTypeIF
   *
   * @return complete path of the Base class file for the given MdType.
   */
  public static File getBaseClassDirectory(MdTypeDAOIF mdTypeIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdTypeIF);
    return new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the QueryAPI java file for the given MdEntity.
   *
   * @param mdEntityIF
   *
   * @return complete path of the QueryAPI java file for the given MdType.
   */
  public static String getQueryAPIsourceFilePath(MdEntityDAOIF mdEntityIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdEntityIF);
    return AbstractServerGenerator.getRootServerBaseDirectory(fileSystemPackage) + fileSystemPackage + mdEntityIF.getTypeName() + EntityQueryAPIGenerator.QUERY_API_SUFFIX+".java";
  }

  /**
   * Returns the complete path of the QueryAPI class directory for the given MdType.
   *
   * @param mdClassIF
   *
   * @return complete path of the QueryAPI directory file for the given MdType.
   */
  public static File getQueryAPIclassDirectory(MdClassDAOIF mdClassIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdClassIF);
    return new File(AbstractServerGenerator.getRootServerBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns the complete path of the base QueryAPI java file for the given MdView.
   *
   * @param mdViewIF
   *
   * @return complete path of the base QueryAPI java file for the given MdView.
   */
  public static String getBaseQueryAPIsourceFilePath(MdViewDAOIF mdViewIF)
  {
    String pack = GenerationUtil.getPackageForFileSystem(mdViewIF);
    return AbstractServerGenerator.getRootServerBaseDirectory(pack)  + pack + mdViewIF.getTypeName() + ViewQueryBaseAPIGenerator.QUERY_API_BASE_SUFFIX+".java";
  }

  /**
   * Returns the complete path of the stub QueryAPI java file for the given MdView.
   *
   * @param mdViewIF
   *
   * @return complete path of the stub QueryAPI java file for the given MdView.
   */
  public static String getStubQueryAPIsourceFilePath(MdViewDAOIF mdViewIF)
  {
    String pack = GenerationUtil.getPackageForFileSystem(mdViewIF);
    return AbstractServerGenerator.getRootServerStubDirectory(pack) + pack + mdViewIF.getTypeName() + ViewQueryStubAPIGenerator.QUERY_API_STUB_SUFFIX+".java";
  }


  /**
   * Returns the complete path of the QueryDTO java file for the given MdEntity.
   *
   * @param mdEntityIF
   *
   * @return the complete path of the QueryDTO java file for the given MdEntity.
   */
  public static String getQueryDTOsourceFilePath(MdClassDAOIF mdClassIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdClassIF);
    return AbstractClientGenerator.getRootClientOrCommonBaseDirectory(mdClassIF) + fileSystemPackage + mdClassIF.getTypeName() + TypeGeneratorInfo.QUERY_DTO_SUFFIX+".java";
  }

  /**
   * Returns the complete path of the QueryDTO class directory for the given MdType.
   *
   * @param mdEntityIF
   *
   * @return complete path of the QueryDTO directory file for the given MdType.
   */
  public static File getQueryDTOclassDirectory(MdClassDAOIF mdClassIF)
  {
    String fileSystemPackage = GenerationUtil.getPackageForFileSystem(mdClassIF);
    return new File(AbstractClientGenerator.getRootClientBinDirectory(fileSystemPackage), fileSystemPackage);
  }

  /**
   * Returns a string representing the name of the constant that defines the attribute name as a string.
   * @param mdClassIF
   * @param mdAttributeIF
   * @return string representing the name of the constant that defines the attribute name as a string.
   */
  public static String buildAttributeConstant(MdClassDAOIF mdClassIF, MdAttributeDAOIF mdAttributeIF)
  {
    return mdClassIF.definesType()+"."+mdAttributeIF.definesAttribute().toUpperCase();
  }

  /**
   * Returns a string representing the name of the constant that defines the attribute name as a string.
   * @param mdClassIF
   * @param attributeName
   * @return string representing the name of the constant that defines the attribute name as a string.
   */
  public static String buildAttributeConstant(MdClassDAOIF mdClassIF, String attributeName)
  {
    return buildAttributeConstant(mdClassIF.definesType(), attributeName);
  }

  /**
   * Returns a string representing the name of the constant that defines the attribute name as a string.
   * @param type
   * @param attributeName
   * @return string representing the name of the constant that defines the attribute name as a string.
   */
  public static String buildAttributeConstant(String type, String attributeName)
  {
    return type+"."+attributeName.toUpperCase();
  }
}
