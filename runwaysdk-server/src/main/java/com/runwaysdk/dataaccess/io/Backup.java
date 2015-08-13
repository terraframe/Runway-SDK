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
package com.runwaysdk.dataaccess.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.VaultProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.BackupReadException;
import com.runwaysdk.system.metadata.CreateBackupException;
import com.runwaysdk.util.FileIO;

public class Backup
{
  public static final String            WEBAPP_DIR_NAME  = "webapp";
  
  public static final String            PROVILE_DIR_NAME = "profiles";

  public static final String            VAULT_DIR_NAME   = "vaults";

  public static final String            WEBFILE_DIR_NAME = "webfiles";

  public static final String            CACHE            = "cache";

  public static final String            SQL              = "sql";

  private static final SimpleDateFormat formatter        = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");

  private PrintStream                   logPrintStream;

  private String                        backupFileRootName;

  private String                        backupFileLocation;

  private String                        tempBackupFileLocation;

  private File                          tempBackupDir;

   private boolean backupVaults;
  //
  // private boolean backupWebFiles;

  private String                        timeStampedName;

  private String                        cacheDir;

  private String                        cacheName;

  private java.util.Date                now              = new java.util.Date();

  private List<BackupAgent>             agents;

  private Log                           log;

  public Backup(PrintStream logPrintStream, String backupFileRootName, String backupFileLocationDir, boolean backupVaults, boolean backupWebFiles)
  {
    log = LogFactory.getLog(this.getClass());

    this.logPrintStream = logPrintStream;

    this.backupFileRootName = backupFileRootName;
    this.backupFileLocation = backupFileLocationDir;

     this.backupVaults = backupVaults;
    // this.backupWebFiles = backupWebFiles;

    this.timeStampedName = this.backupFileRootName + "-" + formatter.format(now);
    this.tempBackupFileLocation = backupFileLocationDir + File.separator + "_temp_" + this.timeStampedName;

    this.tempBackupDir = new File(this.tempBackupFileLocation);
    this.tempBackupDir.mkdirs();

    this.cacheDir = ServerProperties.getGlobalCacheFileLocation();
    this.cacheName = ServerProperties.getGlobalCacheName();

    this.agents = new LinkedList<BackupAgent>();
  }

  /**
   * Returns the name and location of the zip file.
   * 
   * @return name and location of the zip file.
   */
  public String backup()
  {
    return backup(true);
  }

  /**
   * Returns the name and location of the zip file.
   * 
   * @param useNamespace
   *          flag for backing up database by schema or by list of application
   *          tables
   * 
   * @return name and location of the zip file.
   */
  public String backup(boolean useNamespace)
  {
    this.log.trace("Starting backup for [" + backupFileLocation + "][" + backupFileRootName + "].");

    for (BackupAgent agent : agents)
    {
      agent.preBackup();
    }

    // backupCacheFile();

    backupDatabase(useNamespace);

    backupWebapp();
    
    backupVaults();
    
    if (this.backupVaults) { backupVaults(); }

    /*
     * // backup the profiles backupProfiles();
     * 
     * if (this.backupWebFiles) { backupWebFilesVaults(); }
     */
    now = new java.util.Date();
    String zipBackupFileName = this.timeStampedName + ".zip";

    String zipFileNameAndLocation = zipFiles(zipBackupFileName);

    try
    {
      FileIO.deleteDirectory(this.tempBackupDir);
    }
    catch (IOException e)
    {
      CreateBackupException cbe = new CreateBackupException(e);
      cbe.setLocation(this.tempBackupDir.getAbsolutePath());
      throw cbe;
    }

    this.logPrintStream.println(ServerExceptionMessageLocalizer.backupCompleteMessage(Session.getCurrentLocale()));

    for (BackupAgent agent : agents)
    {
      agent.postBackup();
    }

    this.log.trace("Finished backup file [" + zipFileNameAndLocation + "]");

    return zipFileNameAndLocation;
  }
  
  private void backupVaults()
  {
    log.trace("Starting backup of vaults.");
    
    QueryFactory qf = new QueryFactory();
    BusinessQuery vaultQ = qf.businessQuery(VaultInfo.CLASS);

    String backupVaultFileLocation = this.tempBackupFileLocation + File.separator + VAULT_DIR_NAME
        + File.separator;

    OIterator<Business> i = vaultQ.getIterator();
    try
    {
      for (Business vault : i)
      {
        String vaultName = vault.getValue(VaultInfo.VAULT_NAME);
        String vaultLocation = VaultProperties.getPath(vaultName);
        
        File vaultLocationFile = new File(vaultLocation);

        if (vaultLocationFile != null && vaultLocationFile.exists())
        {
          log.debug("Backing up vault [" + vaultName + "] at location [" + vaultLocation + "].");
          
          FileIO.copyFolder(new File(vaultLocation), new File(backupVaultFileLocation + File.separator
              + vault.getId() + File.separator));
        }
        else
        {
          log.warn("Skipped backup of vault [" + vaultName + "] at location [" + vaultLocation + "] because it does not exist.");
        }
      }
    }
    finally
    {
      i.close();
    }
  }

  private void backupWebapp()
  {
    File backupProfileLocationFile = new File(this.tempBackupFileLocation + File.separator + WEBAPP_DIR_NAME + File.separator);

    String webappRootDir = DeployProperties.getDeployPath();

    File webappRootFile = new File(webappRootDir);

    FilenameFilter filenameFilter = new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
        if (name.endsWith(".svn") || dir.getName().startsWith("."))
        {
          return false;
        }

        return true;
      }
    };

    boolean success = FileIO.copyFolder(webappRootFile, backupProfileLocationFile, filenameFilter);
    if (!success)
    {
      // TODO : This success stuff is garbage, I want the actual IOException why swallow it
      CreateBackupException cbe = new CreateBackupException();
      cbe.setLocation(backupProfileLocationFile.getAbsolutePath());
      throw cbe;
    }
  }

  @SuppressWarnings("unused")
  private void backupCacheFile()
  {
    this.logPrintStream.println(ServerExceptionMessageLocalizer.backingUpCacheMessage(Session.getCurrentLocale()));
    try
    {
      // Make the temp cache directory
      File directory = new File(this.tempBackupFileLocation + File.separator + CACHE + File.separator);
      String copyTo = directory.getAbsolutePath();
      directory.mkdirs();

      File cacheDir = new File(this.cacheDir);
      this.log.trace("Backing up cache files from [" + cacheDir + "] to [" + copyTo + "].");

      FileFilter filter = new FileFilter()
      {
        @Override
        public boolean accept(File file)
        {
          return file.getName().startsWith(Backup.this.cacheName);
        }
      };

      File[] files = cacheDir.listFiles(filter);

      if (files != null)
      {
        for (File file : files)
        {
          this.log.debug("Backing up cache file [" + file + "].");

          FileInputStream iStream = new FileInputStream(file);
          FileOutputStream oStream = new FileOutputStream(new File(copyTo + File.separator + file.getName()));

          FileIO.write(oStream, iStream);
        }
      }
    }
    catch (IOException e)
    {
      CreateBackupException cbe = new CreateBackupException(e);
      cbe.setLocation(new File(this.tempBackupFileLocation + File.separator + CACHE + File.separator).getAbsolutePath());
      throw cbe;
    }

    this.log.trace("Finished backing up the cache files.");
  }

  private void backupDatabase(boolean useNamespace)
  {
    this.logPrintStream.println(ServerExceptionMessageLocalizer.backingUpDatabaseMessage(Session.getCurrentLocale()));

    // Make the temp sql directory
    File directory = new File(this.tempBackupFileLocation + File.separator + SQL + File.separator);
    directory.mkdirs();

    String createdFile;
    if (useNamespace)
    {
      String namespace = Database.getApplicationNamespace();

      this.log.debug("Backing up the database with namespace [" + namespace + "]");

      createdFile = Database.backup(namespace, directory.getAbsolutePath(), this.timeStampedName, false);
    }
    else
    {
      List<String> tableNames = Database.getAllApplicationTables();

      createdFile = Database.backup(tableNames, directory.getAbsolutePath(), backupFileRootName, false);
    }

    this.log.debug("Finished backing up the database without output file [" + createdFile + "]");
  }

  /**
   * Zips the directory and then returns the name and location of the zip file.
   * 
   * @param zipBackupFileName
   * @return the name and location of the zip file.
   */
  private String zipFiles(String zipBackupFileName)
  {
    this.logPrintStream.println(ServerExceptionMessageLocalizer.compressingDirectoryMessage(Session.getCurrentLocale(), this.tempBackupFileLocation));

    String zipFileNameAndLocation = this.backupFileLocation + File.separator + zipBackupFileName;

    this.log.trace("Creating zipfile [" + zipBackupFileName + "]");

    try
    {
      FileFilter fileFilter = new FileFilter()
      {
        public boolean accept(File file)
        {
          return true;
        }
      };

      FileIO.zip(this.tempBackupDir, fileFilter, new File(zipFileNameAndLocation));
    }
    catch (IOException e)
    {
      CreateBackupException cbe = new CreateBackupException(e);
      cbe.setLocation(this.tempBackupDir.getAbsolutePath());
      throw cbe;
    }

    this.log.trace("Finished creating zipfile [" + zipBackupFileName + "]");

    return zipFileNameAndLocation;
  }

  public void addAgents(BackupAgent agent)
  {
    agents.add(agent);
  }

  /*
   * private void backupWebFilesVaults() { QueryFactory qf = new QueryFactory();
   * BusinessDAOQuery webFileQ = qf.businessDAOQuery(WebFileInfo.CLASS);
   * 
   * String backupWebFileFileLocation =
   * this.tempBackupFileLocation+File.separator+WEBFILE_DIR_NAME+File.separator;
   * 
   * // Create the web file dir File backupWebFileFile = new
   * File(backupWebFileFileLocation); backupWebFileFile.mkdir();
   * 
   * OIterator<BusinessDAOIF> i = webFileQ.getIterator(); try { for
   * (BusinessDAOIF businessDAOIF : i) { WebFileDAOIF webFileDAOIF =
   * (WebFileDAOIF)businessDAOIF;
   * 
   * InputStream inputStream = webFileDAOIF.getFile();
   * 
   * File thisBackupWebFileFile = new
   * File(backupWebFileFileLocation+webFileDAOIF.getId());
   * 
   * if (thisBackupWebFileFile != null && thisBackupWebFileFile.exists()) { try
   * { BufferedOutputStream outputStream = new BufferedOutputStream(new
   * FileOutputStream(thisBackupWebFileFile)); FileIO.write(outputStream,
   * inputStream); } catch (IOException e) { throw new
   * FileWriteException(thisBackupWebFileFile, e); } }
   * 
   * } } finally { i.close(); } }
   */
  /*
   * private void backupProfiles() { File backupProfileLocationFile = new
   * File(this
   * .tempBackupFileLocation+File.separator+PROVILE_DIR_NAME+File.separator);
   * 
   * String profileRootDir = ProfileManager.getProfileRootDir().toString();
   * 
   * File profileRootFile = new File(profileRootDir);
   * 
   * FilenameFilter filenameFilter = new FilenameFilter() { public boolean
   * accept(File dir, String name) { if (name.endsWith(".properties") ||
   * name.endsWith(".xml") || name.endsWith(".xsd") ) { return true; }
   * 
   * if (dir.isDirectory() && !dir.getName().startsWith(".")) { return true; }
   * 
   * return false; } };
   * 
   * FileIO.copyFolder(profileRootFile, backupProfileLocationFile,
   * filenameFilter); }
   */

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    try
    {
      Backup.start(args);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  @Request
  private static void start(String[] args)
  {
    if (args.length != 2)
    {
      String errMessage = "Invalid number of arguments given.  Please provide 1) the root file name of the backup and 2) the backup file location.\n" + "The name of the backup file will include the root file name, a timestamp, and will contain the \".zip\" extension.";
      throw new RuntimeException(errMessage);
    }
    else
    {
      Backup backup = new Backup(System.out, args[0], args[1], true, true);
      backup.backup();
    }
  }
}
