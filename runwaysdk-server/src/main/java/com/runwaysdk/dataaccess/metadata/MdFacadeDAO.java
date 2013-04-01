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
package com.runwaysdk.dataaccess.metadata;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdTypeCommand;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.business.generation.facade.FacadeBaseGenerator;
import com.runwaysdk.business.generation.facade.FacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.FacadeProxyIFGenerator;
import com.runwaysdk.business.generation.facade.FacadeStubGenerator;
import com.runwaysdk.business.generation.facade.JavaAdapterGenerator;
import com.runwaysdk.business.generation.facade.JavaArtifactMdFacadeCommand;
import com.runwaysdk.business.generation.facade.JavaFacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.RMIAdapterGenerator;
import com.runwaysdk.business.generation.facade.RMIFacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.RemoteAdapterGenerator;
import com.runwaysdk.business.generation.facade.WebServiceAdapterGenerator;
import com.runwaysdk.business.generation.facade.WebServiceFacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.json.JSONAdapterGenerator;
import com.runwaysdk.business.generation.facade.json.JSONAdapterGenericGenerator;
import com.runwaysdk.business.generation.facade.json.JSONFacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.json.JSONFacadeProxyIFGenerator;
import com.runwaysdk.business.generation.facade.json.JSONJavaAdapterGenerator;
import com.runwaysdk.business.generation.facade.json.JSONJavaFacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.json.JSONRMIAdapterGenerator;
import com.runwaysdk.business.generation.facade.json.JSONRMIFacadeProxyGenerator;
import com.runwaysdk.business.generation.facade.json.JSONRemoteAdapterGenerator;
import com.runwaysdk.business.generation.facade.json.JSONWebServiceAdapterGenerator;
import com.runwaysdk.business.generation.facade.json.JSONWebServiceFacadeProxyGenerator;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.util.FileIO;

public class MdFacadeDAO extends MdTypeDAO implements MdFacadeDAOIF
{
  /**
   * Eclipse auto generated ID
   */
  private static final long serialVersionUID = 120934785209785L;

  /**
   * The default constructor, does not set any attributes
   */
  public MdFacadeDAO()
  {
    super();
  }

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    String signature = super.getSignature()+ " Methods[";
    boolean firstIteration = true;
    for (MdMethodDAOIF mdMethodDAOIF : this.getMdMethodsOrdered())
    {
      if (!firstIteration)
      {
        signature +=", ";
      }
      else
      {
        firstIteration = false;
      }
      signature += mdMethodDAOIF.getSignature();
    }
    signature += "]";

    return signature;
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   *
   * <br/><b>Precondition:</b> attributeMap != null
   * <br/><b>Precondition:</b> type != null <br/><b>Precondition:</b>
   * ObjectCache.isSubClass(type, Constants.MD_TYPE)
   *
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdFacadeDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdFacadeDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdFacadeDAO(attributeMap, MdFacadeInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdFacadeDAO getBusinessDAO()
  {
    return (MdFacadeDAO) super.getBusinessDAO();
  }

  /**
   *
   * @param businessContext true if this is being called from a business context, false
   * otherwise. If true then cascading deletes of other Entity objects will happen at the Business
   * layer instead of the data access layer.
   *
   */
  @Override
  public void delete(boolean businessContext)
  {
    // 1. Delete all MdMethod objects that this type defines
    this.dropMdMethods(businessContext);

    super.delete(businessContext);
  }

  /**
   *
   * @param facadeType
   * @return
   */
  public static MdFacadeDAOIF getMdFacadeDAO(String facadeType)
  {
    MdFacadeDAOIF mdFacadeIF = ObjectCache.getMdFacadeDAO(facadeType);

    if (mdFacadeIF==null)
    {
      String error = "Metadata not found for entity [" + facadeType + "]";

      // Feed in the MdEntity for MdFacade.  Yes, it's self-describing.
      throw new DataNotFoundException(error, MdElementDAO.getMdElementDAO(MdElementInfo.CLASS));
    }

    return mdFacadeIF;
  }

  /**
   * Removes any MdMdethod that are defined by this MdEntity.
   *
   * @param businessContext true if this is being called from a business context, false
   * otherwise. If true then cascading deletes of other Entity objects will happen at the Business
   * layer instead of the data access layer.
   *
   */
  private void dropMdMethods(boolean businessContext)
  {
    for(MdMethodDAOIF mdMethod : this.getMdMethods())
    {
      mdMethod.getBusinessDAO().delete(businessContext);
    }
  }

  /**
   * Returns a command object that either creates or updates Java artifacts for this type.
   *
   * @param conn database connection object.
   *
   * @return command object that either creates or updates Java artifacts for this type.
   */
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    Command command;

    if (this.isNew())
    {
      command = new JavaArtifactMdFacadeCommand(this, JavaArtifactMdTypeCommand.Operation.CREATE, conn);
    }
    else
    {
      command = new JavaArtifactMdFacadeCommand(this, JavaArtifactMdTypeCommand.Operation.UPDATE, conn);
    }

    return command;
  }

  /**
   * Returns a command object that deletes Java artifacts for this type.
   *
   * @param conn database connection object.
   *
   * @return command object that deletes Java artifacts for this type.
   */
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdFacadeCommand(this, JavaArtifactMdTypeCommand.Operation.DELETE, conn);
  }

  /**
   * Returns a command object that cleans Java artifacts for this type.
   *
   * @param conn database connection object.
   *
   * @return command object that cleans Java artifacts for this type.
   */
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdFacadeCommand(this, JavaArtifactMdTypeCommand.Operation.CLEAN, conn);
  }

  /**
   * Copies all Java source and class files from this object into files on the file system.
   */
  public void writeJavaToFile()
  {
    // Write the stub and base .class files to the filesystem
    byte[] stubclass     = this.getBlob(MdFacadeInfo.STUB_CLASS);
    ClassManager.writeClasses(TypeGenerator.getStubClassDirectory(this), stubclass);

    byte[] baseclass     = this.getBlob(MdFacadeInfo.BASE_CLASS);
    ClassManager.writeClasses(TypeGenerator.getBaseClassDirectory(this), baseclass);

    byte[] serverClasses = this.getBlob(MdFacadeInfo.SERVER_CLASSES);
    ClassManager.writeClasses(TypeGenerator.getBaseClassDirectory(this), serverClasses);

    byte[] commonClasses = this.getBlob(MdFacadeInfo.COMMON_CLASSES);
    ClassManager.writeClasses(TypeGenerator.getBaseClassDirectory(this), commonClasses);

    byte[] clientClasses = this.getBlob(MdFacadeInfo.CLIENT_CLASSES);
    ClassManager.writeClasses(TypeGenerator.getBaseClassDirectory(this), clientClasses);

    try
    {
      String stubsource = this.getAttribute(MdClassInfo.STUB_SOURCE).getValue();
      String basesource = this.getAttribute(MdElementInfo.BASE_SOURCE).getValue();
      FileIO.write(TypeGenerator.getJavaSrcFilePath(this), stubsource);
      FileIO.write(TypeGenerator.getBaseSrcFilePath(this), basesource);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Copies all Java source and class files from the file system and stores them
   * in the database.
   *
   * @param conn
   *            database connection object. This method is used during the a
   *            transaction. Consequently, the transaction must be managed
   *            manually.
   */
  public void writeFileArtifactsToDatabaseAndObjects(Connection conn)
  {
    if (!this.isSystemPackage())
    {
      String baseSource = null;

      File baseSourceFile = new File(TypeGenerator.getBaseSrcFilePath(this));

      try
      {
        baseSource = FileIO.readString(baseSourceFile);
      }
      catch (IOException e)
      {
        throw new SystemException(e);
      }
      // Update the base and stub class and source in the business layer
      byte[] baseClassBytes = GenerationFacade.getBaseClass(this);
      String baseClassColumnName = MdTypeDAOIF.BASE_CLASS_COLUMN;
      String baseSourceColumnName = MdTypeDAOIF.BASE_SOURCE_COLUMN;
      Database.updateClassAndSource(this.getId(), MdTypeDAOIF.TABLE, baseClassColumnName, baseClassBytes,
          baseSourceColumnName, baseSource, conn);

      String stubSource = GenerationFacade.getStubSource(this);
      byte[] stubClassBytes = GenerationFacade.getStubClass(this);
      String stubClassColumnName = MdFacadeDAOIF.STUB_CLASS_COLUMN;
      String stubSourceColumnName = MdFacadeDAOIF.STUB_SOURCE_COLUMN;
      // Update base fields that are used.
      Database.updateClassAndSource(this.getId(), MdFacadeDAOIF.TABLE, stubClassColumnName, stubClassBytes,
          stubSourceColumnName, stubSource, conn);

      String serverClassesColumnName = MdFacadeDAOIF.SERVER_CLASSES_COLUMN;
      byte[] serverClassesBytes = GenerationFacade.getGeneratedServerClasses(this);
      String commonClassesColumnName = MdFacadeDAOIF.COMMON_CLASSES_COLUMN;
      byte[] commonClassesBytes = GenerationFacade.getGeneratedCommonClasses(this);
      String clientClassesColumnName = MdFacadeDAOIF.CLIENT_CLASSES_COLUMN;
      byte[] clientClassesBytes = GenerationFacade.getGeneratedClientClasses(this);
      Database.updateMdFacadeGeneratedClasses(this.getId(), MdFacadeDAOIF.TABLE, serverClassesColumnName,
          serverClassesBytes, commonClassesColumnName, commonClassesBytes, clientClassesColumnName,
          clientClassesBytes, conn);

      // Only update the source. The blob attributes just point to the database
      // anyway.
      this.getAttribute(MdFacadeInfo.BASE_SOURCE).setValue(baseSource);
      this.getAttribute(MdFacadeInfo.STUB_SOURCE).setValue(stubSource);

      // Mark the class artifacts as modified, so that their values will be logged (if enabled)
      this.getAttribute(MdFacadeInfo.BASE_CLASS).setModified(true);
      this.getAttribute(MdFacadeInfo.STUB_CLASS).setModified(true);
      this.getAttribute(MdFacadeInfo.SERVER_CLASSES).setModified(true);
      this.getAttribute(MdFacadeInfo.COMMON_CLASSES).setModified(true);
      this.getAttribute(MdFacadeInfo.CLIENT_CLASSES).setModified(true);
    }
  }

  /**
   * Returns true if an attribute that stores source or class has been modified.
   *
   * @return true if an attribute that stores source or class has been modified.
   */
  @Override
  public boolean javaArtifactsModifiedOnObject()
  {
    if (!this.isSystemPackage())
    {
      if (this.getAttribute(MdFacadeInfo.BASE_SOURCE).isModified()     ||
          this.getAttribute(MdFacadeInfo.STUB_SOURCE).isModified()     ||
          this.getAttribute(MdFacadeInfo.BASE_CLASS).isModified()      ||
          this.getAttribute(MdFacadeInfo.STUB_CLASS).isModified()      ||
          this.getAttribute(MdFacadeInfo.SERVER_CLASSES).isModified()  ||
          this.getAttribute(MdFacadeInfo.COMMON_CLASSES).isModified()  ||
          this.getAttribute(MdFacadeInfo.CLIENT_CLASSES).isModified())
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a list of Strings representing the names of the generated server files, minus the ".class" or ".java" extension.
   * @return list of Strings representing the names of the generated server files, minus the ".class" or ".java" extension.
   */
  public static List<String> generatedServerFileNames(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.add(JavaAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(WebServiceAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(RMIAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONJavaAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONRMIAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONWebServiceAdapterGenerator.getGeneratedName(mdFacadeIF));

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated server class files.
   * @return list of Strings representing the names of the generated server class files.
   */
  public static List<String> generatedServerClassFiles(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.addAll(generatedServerFileNames(mdFacadeIF));

    for (int i=0; i<list.size(); i++)
    {
      list.set(i, list.get(i) + ".class");
    }

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated server source files.
   * @return list of Strings representing the names of the generated server source files.
   */
  public static List<String> generatedServerSourceFiles(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.addAll(generatedServerFileNames(mdFacadeIF));

    for (int i=0; i<list.size(); i++)
    {
      list.set(i, list.get(i) + ".java");
    }

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated common files, minus the ".class" or ".java" extension.
   * @return list of Strings representing the names of the generated common files, minus the ".class" or ".java" extension.
   */
  public static List<String> generatedCommonFileNames(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.add(RemoteAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONFacadeProxyIFGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONRemoteAdapterGenerator.getGeneratedName(mdFacadeIF));

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated common class files.
   * @return list of Strings representing the names of the generated common class files.
   */
  public static List<String> generatedCommonClassFiles(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.addAll(generatedCommonFileNames(mdFacadeIF));

    for (int i=0; i<list.size(); i++)
    {
      list.set(i, list.get(i) + ".class");
    }

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated common source files.
   * @return list of Strings representing the names of the generated common source files.
   */
  public static List<String> generatedCommonSourceFiles(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.addAll(generatedCommonFileNames(mdFacadeIF));

    for (int i=0; i<list.size(); i++)
    {
      list.set(i, list.get(i) + ".java");
    }

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated client files, minus the ".class" or ".java" extension.
   * @return list of Strings representing the names of the generated client files, minus the ".class" or ".java" extension.
   */
  public static List<String> generatedClientFileNames(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.add(FacadeProxyIFGenerator.getGeneratedName(mdFacadeIF));
    list.add(FacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(JavaFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(RMIFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(WebServiceFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONJavaFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONRMIFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONWebServiceFacadeProxyGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONAdapterGenerator.getGeneratedName(mdFacadeIF));
    list.add(JSONAdapterGenericGenerator.getGeneratedName(mdFacadeIF));

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated client class files.
   * @return list of Strings representing the names of the generated client class files.
   */
  public static List<String> generatedClientClassFiles(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.addAll(generatedClientFileNames(mdFacadeIF));

    for (int i=0; i<list.size(); i++)
    {
      list.set(i, list.get(i) + ".class");
    }

    return list;
  }

  /**
   * Returns a list of Strings representing the names of the generated client source files.
   * @return list of Strings representing the names of the generated client source files.
   */
  public static List<String> generatedClientSourceFiles(MdFacadeDAOIF mdFacadeIF)
  {
    List<String> list = new LinkedList<String>();

    list.addAll(generatedClientFileNames(mdFacadeIF));

    for (int i=0; i<list.size(); i++)
    {
      list.set(i, list.get(i) + ".java");
    }

    return list;
  }

  /**
   * Returns a new MdFacade.
   * Some attributes will contain default values, as defined in the attribute
   * metadata. Otherwise, the attributes will be blank.
   *
   * @return MdFacade
   */
  public static MdFacadeDAO newInstance()
  {
    return (MdFacadeDAO) BusinessDAO.newInstance(MdFacadeInfo.CLASS);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdFacadeDAOIF get(String id)
  {
    return (MdFacadeDAOIF) BusinessDAO.get(id);
  }

  /**
   * Returns a list of all generators used to generate source
   * for this MdType.
   *
   * @return
   */
  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    //Don't generate reserved types
    if (GenerationUtil.isReservedType(this))
    {
      return list;
    }

    list.add(new FacadeStubGenerator(this));
    list.add(new FacadeBaseGenerator(this));

    // Server Classes
    list.add(new JavaAdapterGenerator(this));
    list.add(new WebServiceAdapterGenerator(this));
    list.add(new RMIAdapterGenerator(this));
    list.add(new JSONJavaAdapterGenerator(this));
    list.add(new JSONRMIAdapterGenerator(this));
    list.add(new JSONWebServiceAdapterGenerator(this));

    // Client Classes
    list.add(new FacadeProxyIFGenerator(this));
    list.add(new FacadeProxyGenerator(this));
    list.add(new JavaFacadeProxyGenerator(this));
    list.add(new RMIFacadeProxyGenerator(this));
    list.add(new WebServiceFacadeProxyGenerator(this));
    list.add(new JSONFacadeProxyGenerator(this));
    list.add(new JSONJavaFacadeProxyGenerator(this));
    list.add(new JSONRMIFacadeProxyGenerator(this));
    list.add(new JSONWebServiceFacadeProxyGenerator(this));
    list.add(new JSONAdapterGenerator(this));
    list.add(new JSONAdapterGenericGenerator(this));

    // Common Classes
    list.add(new RemoteAdapterGenerator(this));
    list.add(new JSONFacadeProxyIFGenerator(this));
    list.add(new JSONRemoteAdapterGenerator(this));

    return list;
  }
}
