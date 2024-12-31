/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.system;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.resource.ApplicationFileResource;
import com.runwaysdk.resource.ApplicationTreeResource;
import com.runwaysdk.resource.CloseableFile;
import com.runwaysdk.resource.FileResource;
import com.runwaysdk.resource.ResourceException;
import com.runwaysdk.session.CreatePermissionException;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.vault.VaultFileDAO;

public class VaultFile extends VaultFileBase implements ApplicationFileResource
{
  private static final long serialVersionUID = 1229405886296L;
  
  public VaultFile()
  {
    super();
  }
  
  /**
   * Creates and applies a new VaultFile using the given parameters. Purely a convenience method.
   * 
   * @param filename The name of the file, including extension.
   * @param file
   */
  public static VaultFile createAndApply(String fileName, InputStream file)
  {
    VaultFile entity = new VaultFile();
    VaultFileDAO fileDao = (VaultFileDAO) BusinessFacade.getEntityDAO(entity);

    checkVaultPermissions(entity, Operation.CREATE);

    String onlyName = FilenameUtils.getBaseName(fileName);
    String extension = FilenameUtils.getExtension(fileName);

    entity.setValue(VaultFileInfo.FILE_NAME, onlyName);
    entity.setValue(VaultFileInfo.EXTENSION, extension);

    fileDao.setSize(0);
    entity.apply();
    fileDao.putFile(file);
    
    return entity;
  }
  
  public File getFile()
  {
    VaultFileDAO fileDAO = (VaultFileDAO) BusinessFacade.getEntityDAO(this);
    
    return fileDAO.getFile();
  }
  
  @Override
  public String getAbsolutePath()
  {
    return this.getFile().getAbsolutePath();
  }
  
  /**
   * Returns a temporary file with the proper name and extension of the vault file (not scrambled). This new
   * temp file should be cleaned up after usage with the AutoCloseable Java paradigm.
   */
  public CloseableFile openNewFile()
  {
    VaultFileDAO fileDAO = (VaultFileDAO) BusinessFacade.getEntityDAO(this);
    
    try
    {
      File scrambledVF = fileDAO.getFile();
      
      CloseableFile tempFile = new CloseableFile(scrambledVF.getParent(), this.getName(), true);
      // TODO : And if this tempFile already exists?
      
      FileUtils.copyFile(scrambledVF, tempFile);
      
      return tempFile;
    }
    catch (IOException e)
    {
      throw new ResourceException(e);
    }
  }
  
  /**
   * Synonym for getFile.
   */
  @Override
  public File getUnderlyingFile()
  {
    return this.getFile();
  }
  
  public InputStream getFileStream()
  {
    VaultFileDAO fileDAO = (VaultFileDAO) BusinessFacade.getEntityDAO(this);
    return fileDAO.getFileStream();
  }
  
  private static void checkVaultPermissions(VaultFile entity, Operation operation)
  {
    SessionIF session = Session.getCurrentSession();

    if (session != null)
    {
      String sessionId = session.getOid();
      boolean access = SessionFacade.checkAccess(sessionId, operation, entity);

      if (!access)
      {
        SingleActorDAOIF user = SessionFacade.getUser(sessionId);
        String errorMsg = "User [" + user.getSingleActorName() + "] does not have permission to " + operation.name() + " a vault file.";
        throw new CreatePermissionException(errorMsg, entity, user);
      }
    }
  }

  @Override
  public InputStream openNewStream()
  {
    return this.getFileStream();
  }

  @Override
  public String getName()
  {
    return this.getFileName() + "." + this.getFileExtension();
  }

  @Override
  public String getBaseName()
  {
    return FilenameUtils.getBaseName(this.getName());
  }

  @Override
  public String getNameExtension()
  {
    return FilenameUtils.getExtension(this.getName());
  }

  @Override
  public boolean isRemote()
  {
    return false;
  }

  @Override
  public void close()
  {
    
  }

  @Override
  public boolean exists()
  {
    return this.getFile().exists();
  }

  @Override
  public boolean isDirectory()
  {
    return false;
  }
  
  @Override
  public Iterator<ApplicationTreeResource> getChildren()
  {
    ArrayList<ApplicationTreeResource> children = new ArrayList<ApplicationTreeResource>();
    
    for (File file : this.getFile().listFiles())
    {
      children.add(new FileResource(file));
    }
    
    return children.iterator();
  }
  
  @Override
  public Iterator<ApplicationFileResource> getChildrenFiles()
  {
    ArrayList<ApplicationFileResource> children = new ArrayList<ApplicationFileResource>();
    
    for (File file : this.getFile().listFiles())
    {
      children.add(new FileResource(file));
    }
    
    return children.iterator();
  }

  @Override
  public ApplicationTreeResource getParent()
  {
    return new FileResource(this.getFile().getParentFile());
  }
  
  @Override
  public ApplicationFileResource getParentFile()
  {
    return (ApplicationFileResource) this.getParent();
  }

  @Override
  public ApplicationTreeResource getChild(String path)
  {
    return new FileResource(new File(this.getFile().getAbsolutePath() + File.separator + path));
  }
  
  @Override
  public ApplicationFileResource getChildFile(String path)
  {
    return (ApplicationFileResource) this.getChild(path);
  }
  
}
