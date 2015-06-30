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
package com.runwaysdk.vault;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.runwaysdk.SystemException;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.util.IdParser;


public class WebFileDAO extends BusinessDAO implements WebFileDAOIF, SpecializedDAOImplementationIF
{
  /**
   * An auto-generated eclipse unique id
   */
  private static final long serialVersionUID = 627578319131662925L;

  /**
   * The depth of the file in the vault
   */
  private static final int DEPTH = 6;

  public WebFileDAO()
  {
    super();
  }

  public WebFileDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);

    if (this.getFilePath().trim().equals(""))
    {
      MdClassDAOIF mdClassIF = MdClassDAO.getMdClassDAO(type);
      this.setFilePath(this.parsePath(IdParser.buildId(ServerIDGenerator.nextID(), mdClassIF.getId())));
    }
  }
  
  @Override
  public String apply()
  {
    if(this.isNew())
    {
      String directory = filePath();
      String fileName = this.getFileName();
      String filePath = directory + fileName;
      this.setKey(filePath);
    }
    
    return super.apply();
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static WebFileDAOIF get(String fileId)
  {
    EntityDAOIF entityDAO = EntityDAO.get(fileId);
    WebFileDAOIF file = (WebFileDAOIF) entityDAO;

    return file;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public WebFileDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new WebFileDAO(attributeMap, type);
  }

  /**
   * @return
   */
  public static WebFileDAO newInstance()
  {
    return (WebFileDAO) BusinessDAO.newInstance(WebFileDAOIF.CLASS);

//    WebFile file = (WebFile) BusinessDAO.newInstance(WebFileIF.CLASS);
//
//    file.setFilePath(file.parsePath(file.getId()));
//
//    return file;
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public WebFileDAO getBusinessDAO()
  {
    return (WebFileDAO) super.getBusinessDAO();
  }

  /**
   * @param ext
   */
  public void setExtension(String ext)
  {
    this.setValue(WebFileDAOIF.EXTENSION, ext);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.vault.VaultFileIF#getExtension()
   */
  public String getExtension()
  {
    return this.getValue(WebFileDAOIF.EXTENSION);
  }

  /**
   * @param fileName
   */
  public void setFileName(String fileName)
  {
    this.setValue(WebFileDAOIF.FILE_NAME, fileName);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.vault.VaultFileIF#getFileName()
   */
  public String getFileName()
  {
    return this.getValue(WebFileDAOIF.FILE_NAME);
  }
  /**
   * @param path
   */
  public void setFilePath(String path)
  {
    this.setValue(WebFileDAOIF.FILE_PATH, path);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.vault.VaultFileIF#getVaultFilePath()
   */
  public String getFilePath()
  {
    return this.getValue(WebFileDAOIF.FILE_PATH);
  }
  
  @Override
  public void delete(boolean businessContext)
  {
    String path = this.getFullFilePathAndName();

    DeleteFileCommand command = new DeleteFileCommand(path, this, true);

    command.doIt();

    super.delete(businessContext);
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.vault.VaultFileIF#getFile()
   */
  public InputStream getFile()
  {
    String filePath = this.getFullFilePathAndName();
    File file = new File(filePath);

    try
    {
      return new FileInputStream(file);
    }
    catch(IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Returns the fully qualified location of the file.
   *
   * @return fully qualified location of the file.
   */
  public String getFullFilePathAndName()
  {
    return this.filePath() + this.getFileName() +"."+ this.getExtension();
  }


  /* (non-Javadoc)
   * @see com.runwaysdk.vault.VaultFileIF#putFile(byte[])
   */
  public void putFile(byte[] bytes)
  {
    //Insert the file into the vault
    String directory = filePath();

    String filePath = this.getFullFilePathAndName();

    WriteFileCommand command = new WriteFileCommand(directory, filePath, new ByteArrayInputStream(bytes), this);
    command.doIt();
  }

  public void putFile(InputStream stream)
  {
    //Insert the file into the vault
    String directory = filePath();

    String filePath = this.getFullFilePathAndName();

    WriteFileCommand command = new WriteFileCommand(directory, filePath, stream, this);
    command.doIt();
  }

  /**
   * Parses the last VAULT_DEPTH characters of the given id into a path
   *
   * @param id The id to parse
   * @return The path resulting from the parse of the id
   */
  private String parsePath(String id)
  {
    //Load the new path
    StringBuffer newPath = new StringBuffer();

    int length = id.length();

    for(int i = 0; i < DEPTH; i++)
    {
      int index = length-i-1;
      newPath.append(id.charAt(index) + "/");
    }

    return newPath.toString();
  }

  /**
   * Returns the absolute path of a file based upon the
   * VaultFile metadata and the root path of the Vault
   *
   * @param file The VaultFile metadata
   * @return The absolute path of the file
   */
  private String filePath()
  {
    String rootPath = LocalProperties.getWebDirectory();
    String filePath = this.getFilePath();

    return rootPath + "/" + filePath;
  }

  public void notify(FileEvent e)
  {
    //BALK: Does not take any actions on a file event
  }
}
