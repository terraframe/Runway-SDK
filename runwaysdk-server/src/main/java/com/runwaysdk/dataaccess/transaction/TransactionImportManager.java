/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.transaction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.RunwayMetadataVersion;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.dataaccess.resolver.DefaultConflictResolver;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;
import com.runwaysdk.vault.VaultDAO;
import com.runwaysdk.vault.VaultDAOIF;
import com.runwaysdk.vault.WebFileDAO;
import com.runwaysdk.vault.WebFileDAOIF;

public class TransactionImportManager implements IPropertyListener
{
  private static Logger           logger = LoggerFactory.getLogger(TransactionImportManager.class);

  private String                  importZipFileLocation;

  private File                    restoreDirectory;

  private String                  exportXMLFileLocation;

  private String                  backupVaultFileLocation;

  private File                    vaultsDir;

  private String                  backupWebFileLocation;

  private File                    webDir;

  private List<ITaskListener>     taskListeners;

  private String                  backupAppFileLocation;

  private File                    applicationDirectory;

  private Boolean                 importApplicationFiles;

  private IConflictResolver       resolver;

  /**
   * Version of the importer
   */
  private RunwayMetadataVersion   version;

  private List<IPropertyListener> propertyListeners;

  /**
   * List of relative path from the webapp root directory for files and
   * directories to delete beforing restoring the application directory
   */
  private List<String>            filesToDelete;

  /**
   * 
   * @param importZipFileLocation
   */
  public TransactionImportManager(String importZipFileLocation, IConflictResolver resolver)
  {
    this.resolver = resolver;
    this.importApplicationFiles = true;
    this.importZipFileLocation = importZipFileLocation;
    this.version = RunwayMetadataVersion.getCurrentVersion();
    this.filesToDelete = new LinkedList<String>();
    this.taskListeners = new LinkedList<ITaskListener>();
    this.propertyListeners = new LinkedList<IPropertyListener>();
    this.propertyListeners.add(this);

    File zipFile = new File(this.importZipFileLocation);

    String zipFileName = zipFile.getName();

    String rootZipFileName = zipFileName.substring(0, zipFileName.indexOf(".zip"));

    String restoreDirectoryString = zipFile.getParent() + File.separator + rootZipFileName + File.separator;

    this.restoreDirectory = new File(restoreDirectoryString);
    if (!this.restoreDirectory.mkdir())
    {
      logger.debug("Unable to create folder [" + this.restoreDirectory.getAbsolutePath() + "]");
    }

    this.exportXMLFileLocation = restoreDirectoryString + TransactionExportManager.XML_EXPORT_FILE;

    this.backupVaultFileLocation = this.restoreDirectory.getPath() + File.separator + TransactionExportManager.VAULTS_DIR_NAME + File.separator;

    this.vaultsDir = new File(this.backupVaultFileLocation);

    this.backupWebFileLocation = this.restoreDirectory.getPath() + File.separator + TransactionExportManager.WEBFILES_DIR_NAME + File.separator;

    this.webDir = new File(this.backupWebFileLocation);

    this.backupAppFileLocation = this.restoreDirectory.getPath() + File.separator + TransactionExportManager.APP_FILES_DIR_NAME + File.separator;

    this.applicationDirectory = new File(this.backupAppFileLocation);
  }

  public void setVersion(RunwayMetadataVersion version)
  {
    this.version = version;
  }

  public void addTaskListener(ITaskListener listener)
  {
    this.taskListeners.add(listener);
  }

  public void removeTaskListener(ITaskListener listener)
  {
    this.taskListeners.remove(listener);
  }

  public void addPropertyListener(IPropertyListener listener)
  {
    this.propertyListeners.add(listener);
  }

  public void removePropertyListener(IPropertyListener listener)
  {
    this.propertyListeners.remove(listener);
  }

  public void addFileToDelete(String path)
  {
    this.filesToDelete.add(path);
  }

  public void removeFileToDelete(String path)
  {
    this.filesToDelete.remove(path);
  }

  public Boolean getImportApplicationFiles()
  {
    return importApplicationFiles;
  }

  public void setImportApplicationFiles(Boolean importApplicationFiles)
  {
    this.importApplicationFiles = importApplicationFiles;
  }

  @Request
  public void importTransactions()
  {
    this.fireStartEvent();

    try
    {
      this.fireStartTaskEvent("Unzipping_archive", -1);

      this.unzipFile();

      this.fireStartTaskEvent("Import_transaction_records", 100);

      TransactionSAXImporter.runImport(new File(this.exportXMLFileLocation), CommonProperties.getTransactionXMLschemaLocation(), this.resolver, this.taskListeners, this.propertyListeners);

      try
      {
        this.fireStartTaskEvent("Deleting_out_of_date_files", -1);

        this.deleteFiles();
      }
      finally
      {

        this.fireStartTaskEvent("Restore_vault_files", -1);

        this.restoreVaults();

        this.fireStartTaskEvent("Restore_web_files", -1);

        this.restoreWebFiles();

        this.fireStartTaskEvent("Restore_application_files", -1);

        this.restoreApplicationFiles();
      }

      // // IMPORTANT: It is required that we update the databaes source and
      // class
      // // after restoring the application files. This is due to the fact that
      // the
      // // new application files might contain updated source and class files.
      // this.fireStartTaskEvent("Update_database_source_and_class", -1);
      //
      // UpdateDatabaseSourceAndClasses.storeSourceAndClassesInDatabase();

      this.fireStartTaskEvent("Cleanup_temp_files", -1);

      try
      {
        FileIO.deleteDirectory(this.restoreDirectory);
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
    catch (RuntimeException e)
    {
      this.fireDoneEvent(false);

      throw e;
    }

    this.fireDoneEvent(true);
  }

  private void deleteFiles()
  {
    for (String path : filesToDelete)
    {
      File file = new File(DeployProperties.getDeployPath() + File.separator + path);

      try
      {
        FileIO.deleteDirectory(file);
      }
      catch (IOException e)
      {
        throw new FileWriteException(file, e);
      }
    }
  }

  private void fireStartTaskEvent(String taskName, int amount)
  {
    for (ITaskListener listener : this.taskListeners)
    {
      listener.taskStart(taskName, amount);
    }
  }

  private void fireDoneEvent(boolean success)
  {
    for (ITaskListener listener : this.taskListeners)
    {
      listener.done(success);
    }
  }

  private void fireStartEvent()
  {
    for (ITaskListener listener : this.taskListeners)
    {
      listener.start();
    }
  }

  private void unzipFile()
  {
    try
    {
      FileIO.write(new ZipFile(new File(this.importZipFileLocation)), this.restoreDirectory.getPath());
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private void restoreVaults()
  {
    if (!vaultsDir.exists())
    {
      return;
    }

    FileFilter fileFilter = new FileFilter()
    {
      public boolean accept(File file)
      {
        if (file.getName().startsWith("."))
        {
          return false;
        }
        else
        {
          return true;
        }
      }
    };

    // The root is a folder - get all of its files
    File[] children = vaultsDir.listFiles(fileFilter);

    if (children != null)
    {

      for (File child : children)
      {
        String vaultId = child.getName();

        try
        {
          VaultDAOIF vaultDAOIF = VaultDAO.get(vaultId);
          restoreValut(vaultDAOIF);
        }
        catch (DataNotFoundException e)
        {
          // No vault with the given oid exists. Vault may have been deleted.
        }
      }
    }
  }

  /**
   * Restores all of the files in the vault.
   * 
   * @param vaultDAOIF
   */
  private void restoreValut(VaultDAOIF vaultDAOIF)
  {
    String vaultLocation = vaultDAOIF.getVaultPath();

    File vaultLocationFile = new File(vaultLocation);

    if (vaultLocationFile != null && vaultLocationFile.exists())
    {
      FileIO.copyFolder(new File(this.backupVaultFileLocation + File.separator + vaultDAOIF.getOid() + File.separator), new File(vaultLocation));
    }
  }

  private void restoreWebFiles()
  {
    if (!webDir.exists())
    {
      return;
    }

    FileFilter fileFilter = new FileFilter()
    {
      public boolean accept(File file)
      {
        if (file.getName().startsWith("."))
        {
          return false;
        }
        else
        {
          return true;
        }
      }
    };

    // The root is a folder - get all of its files
    File[] children = webDir.listFiles(fileFilter);

    if (children != null)
    {
      for (File child : children)
      {
        String webFileId = child.getName();

        try
        {
          WebFileDAOIF webFileDAOIF = WebFileDAO.get(webFileId);

          BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(child));
          webFileDAOIF.putFile(inputStream);
        }
        catch (IOException e)
        {
          throw new FileWriteException(child, e);
        }
        catch (DataNotFoundException e)
        {
          // No webfile with the given oid exists. it may have been deleted.
        }
      }
    }
  }

  private void restoreApplicationFiles()
  {
    if (!applicationDirectory.exists() || !this.importApplicationFiles)
    {
      return;
    }

    // The root is a folder - get all of its files
    List<File> files = FileIO.listFilesRecursively(applicationDirectory);
    String root = DeployProperties.getDeployPath();

    // Get the sequence number of the stored application folders
    Long current = TransactionImportManager.getHighestZipSequence();
    File file = TransactionImportManager.getHighestZipFile();

    for (File child : files)
    {
      try
      {
        Long sequence = TransactionImportManager.parseSequenceNumber(child);

        if (current == null || ( sequence != null && sequence > current ))
        {
          file = child;
        }

        // Unzip the contents of the file into its correct location
        FileIO.write(new ZipFile(file), root);

        // Write the zipFile to the WEB-INF/applicationFiles directory
        String directory = DeployProperties.getStoredTransactionAppFiles();
        String path = directory + child.getName();

        // Make the directory if it doesn't exist
        File outFile = new File(directory);
        if (!outFile.mkdirs())
        {
          logger.debug("Unable to create folder [" + outFile.getAbsolutePath() + "]");
        }

        FileOutputStream out = new FileOutputStream(new File(path));
        FileInputStream in = new FileInputStream(child);

        FileIO.write(out, in);
      }
      catch (IOException e)
      {
        throw new FileWriteException(child, e);
      }
    }
  }

  @Override
  public void handleProperty(String name, String value)
  {
    if (name.equals(TransactionConstants.VERSION_PROPERTY))
    {
      RunwayMetadataVersion importVersion = new RunwayMetadataVersion(value);

      if (importVersion.isGreater(version))
      {
        String msg = "Transaction version mismatch [" + version + "] and [" + value + "]";

        throw new TransactionVersionException(msg, version.toString(), value);
      }
    }
  }

  @Override
  public void handlePropertiesFinished()
  {
    // Do nothing
  }

  public static File getHighestZipFile()
  {
    Long sequence = null;
    File storedFile = null;
    File directory = new File(DeployProperties.getStoredTransactionAppFiles());

    File[] files = directory.listFiles();

    if (files != null)
    {
      for (File file : files)
      {
        if (file.isFile())
        {
          Long current = TransactionImportManager.parseSequenceNumber(file);

          if (sequence == null || ( current != null && current > sequence ))
          {
            sequence = current;
            storedFile = file;
          }
        }
      }
    }

    return storedFile;
  }

  public static Long getHighestZipSequence()
  {
    File file = TransactionImportManager.getHighestZipFile();

    if (file != null)
    {
      return TransactionImportManager.parseSequenceNumber(file);
    }

    return null;
  }

  public static Long parseSequenceNumber(File file)
  {
    try
    {
      StringTokenizer toke = new StringTokenizer(file.getName(), ".");

      if (toke.hasMoreTokens())
      {
        String sequence = toke.nextToken();

        return Long.parseLong(sequence);
      }

      return null;
    }
    catch (Exception e)
    {
      return null;
    }
  }

  /**
   * arg[0] location of the import zip file.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      String errMessage = "Invalid number of arguments given.  Please provide \n" + "1) location of the import zip file.";
      throw new RuntimeException(errMessage);
    }
    else
    {
      importTransactions(args[0], new DefaultConflictResolver(), new TransactionEventChangeListener());
    }
  }

  /**
   * 
   * @param importZipFileLocation
   * @param resolver
   *          TODO
   * @param listeners
   */
  public static void importTransactions(String importZipFileLocation, IConflictResolver resolver, ITaskListener... listeners)
  {
    TransactionImportManager manager = new TransactionImportManager(importZipFileLocation, resolver);

    for (ITaskListener listener : listeners)
    {
      manager.addTaskListener(listener);
    }

    manager.importTransactions();
  }
}
