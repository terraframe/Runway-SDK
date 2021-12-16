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

import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.SpecializedDAOImplementationIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.util.IdParser;

public class VaultFileDAO extends BusinessDAO implements VaultFileDAOIF, SpecializedDAOImplementationIF
{
  /**
   * An eclipse auto-generated oid
   */
  private static final long serialVersionUID = 3176705241297763253L;

  /**
   * The depth of the file in the vault
   */
  private static final int  DEPTH            = 6;

  public VaultFileDAO()
  {
    super();
  }

  public VaultFileDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static VaultFileDAOIF get(String vaultId)
  {
    EntityDAOIF entityDAO = EntityDAO.get(vaultId);
    VaultFileDAOIF file = (VaultFileDAOIF) entityDAO;

    return file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public VaultFileDAO create(Map<String, Attribute> attributeMap, String type)
  {
    return new VaultFileDAO(attributeMap, type);
  }

  /**
   *
   */
  public String apply()
  {
    if (this.isNew() && !this.isAppliedToDB())
    {
      VaultDAOIF vault = VaultManager.nextVault();
      String rootId = IdParser.parseRootFromId(this.getOid());
      this.getAttribute(VaultFileInfo.VAULT_FILE_NAME).setValue(rootId);
      this.getAttribute(VaultFileInfo.VAULT_FILE_PATH).setValue(this.parsePath(rootId));
      this.getAttribute(VaultFileInfo.VAULT_REF).setValue(vault.getOid());

      // Set the key value: The key = Id
      this.setKey(this.getOid());
    }

    return super.apply();
  }

  /**
   * @return
   */
  public static VaultFileDAO newInstance()
  {
    return (VaultFileDAO) BusinessDAO.newInstance(VaultFileInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public VaultFileDAO getBusinessDAO()
  {
    return (VaultFileDAO) super.getBusinessDAO();
  }

  /**
   * @param ext
   */
  public void setExtension(String ext)
  {
    this.setValue(VaultFileInfo.EXTENSION, ext);
  }

  /**
   * Records the size of the file in bytes.
   * 
   * @param ext
   */
  public void setSize(long size)
  {
    this.setValue(VaultFileInfo.FILE_SIZE, Long.toString(size / 1000));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#getExtension()
   */
  public String getExtension()
  {
    return this.getValue(VaultFileInfo.EXTENSION);
  }

  /**
   * @param fileName
   */
  public void setFileName(String fileName)
  {
    this.setValue(VaultFileInfo.FILE_NAME, fileName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#getFileName()
   */
  public String getFileName()
  {
    return this.getValue(VaultFileInfo.FILE_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#getVaultFileName()
   */
  public String getVaultFileName()
  {
    return this.getValue(VaultFileInfo.VAULT_FILE_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#getVaultFilePath()
   */
  public String getVaultFilePath()
  {
    return this.getValue(VaultFileInfo.VAULT_FILE_PATH);
  }

  /**
   * @param reference
   */
  public void setVaultReference(String reference)
  {
    this.setValue(VaultFileInfo.VAULT_REF, reference);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#getVaultReference()
   */
  public String getVaultReference()
  {
    return this.getValue(VaultFileInfo.VAULT_REF);
  }

  /**
   * Returns a reference to the Vault which the file resides within.
   * 
   * @return reference to the Vault which the file resides within.
   */
  public VaultDAOIF getVault()
  {
    return VaultDAO.get(this.getVaultReference());
  }

  /**
   * 
   * @param businessContext
   *          true if this is being called from a business context, false
   *          otherwise. If true then cascading deletes of other Entity objects
   *          will happen at the Business layer instead of the data access
   *          layer.
   * 
   */
  @Override
  public void delete(DeleteContext context)
  {
    VaultDAOIF vault = VaultDAO.get(this.getVaultReference());

    String directory = filePath(vault);
    String path = directory + this.getVaultFileName();

    DeleteFileCommand command = new DeleteFileCommand(path, this, true);
    command.doIt();

    super.delete(context);
  }

  public void deleteNoNotify(DeleteContext context)
  {
    VaultDAOIF vault = VaultDAO.get(this.getVaultReference());

    String directory = filePath(vault);
    String path = directory + this.getVaultFileName();

    DeleteFileCommand command = new DeleteFileCommand(path, this, false);
    command.doIt();

    super.delete(context);
  }

  /**
   * 
   * @see com.runwaysdk.vault.VaultFileIF#getFileStream()
   */
  public InputStream getFileStream()
  {
    File file = new File(filePath() + this.getVaultFileName());

    try
    {
      return new FileInputStream(file);
    }
    catch (IOException e)
    {
      throw new FileReadException(file, e);
    }
  }

  /**
   * @return A File object that references the vault file.
   */
  public File getFile()
  {
    return new File(filePath() + this.getVaultFileName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#putFile(byte[])
   */
  public void putFile(byte[] bytes)
  {
    // Insert the file into the vault
    VaultDAOIF vault = VaultDAO.get(this.getVaultReference());
    String directory = filePath(vault);

    String filePath = directory + this.getVaultFileName();
    WriteFileCommand command = new WriteFileCommand(directory, filePath, new ByteArrayInputStream(bytes), this);
    command.doIt();

    this.setSize(command.getSize());
    this.apply();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.VaultFileIF#putFile(java.io.InputStream)
   */
  public void putFile(InputStream stream)
  {
    // Insert the file into the vault
    VaultDAOIF vault = VaultDAO.get(this.getVaultReference());
    String directory = filePath(vault);

    String filePath = directory + this.getVaultFileName();
    WriteFileCommand command = new WriteFileCommand(directory, filePath, stream, this);
    command.doIt();

    this.setSize(command.getSize());
    this.apply();
  }

  /**
   * Parses the last VAULT_DEPTH characters of the given oid into a path
   * 
   * @param oid
   *          The oid to parse
   * @return The path resulting from the parse of the oid
   */
  private String parsePath(String oid)
  {
    // Load the new path
    StringBuffer newPath = new StringBuffer();

    int length = oid.length();

    for (int i = 0; i < DEPTH; i++)
    {
      int index = length - i - 1;
      newPath.append(oid.charAt(index) + "/");
    }

    return newPath.toString();
  }

  /**
   * Returns the absolute path of a file based upon the VaultFile metadata and
   * the root path of the Vault
   * 
   * @return The absolute path of the file
   */
  protected String filePath()
  {
    VaultDAOIF vault = VaultDAO.get(this.getVaultReference());

    return this.filePath(vault);
  }

  /**
   * Returns the absolute path of a file based upon the VaultFile metadata and
   * the root path of the Vault
   * 
   * @return The absolute path of the file
   */
  protected String filePath(VaultDAOIF vault)
  {
    String rootPath = vault.getVaultPath();
    String filePath = this.getVaultFilePath();

    return rootPath + "/" + filePath;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.vault.FileIF#notify(com.runwaysdk.vault.FileEvent)
   */
  public void notify(FileEvent e)
  {
//    if (e instanceof SizeEvent)
//    {
//      SizeEvent event = (SizeEvent) e;
//
//      if (event.getEventType() == SizeEvent.EventType.SIZE_CHANGE)
//      {
//         Update the byte count on the vault which this vault file references
//         LockObject.getLockObject().appLock(this.getVaultReference());
//
//        try
//        {
//          VaultDAOIF vault = VaultDAO.get(this.getVaultReference());
//          vault.incrementByteCount(event.getAmount());
//        }
//        finally
//        {
//          // This is commented out because of a postgres bug, however
//          // If a transaction updates more than one file then the server will
//          // hang.
//
//          // LockObject.getLockObject().releaseAppLock(this.getVaultReference());
//        }
//      }
//    }
  }
}
