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
package com.runwaysdk.dataaccess.transaction;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import com.runwaysdk.RunwayMetadataVersion;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.TransactionRecordInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.WebFileInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.TransactionItemDAOIF;
import com.runwaysdk.dataaccess.TransactionRecordDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.FileIO;
import com.runwaysdk.util.IdParser;
import com.runwaysdk.vault.VaultDAOIF;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;
import com.runwaysdk.vault.WebFileDAO;
import com.runwaysdk.vault.WebFileDAOIF;

public class TransactionExportManager
{
  public static String                  XML_EXPORT_FILE       = "export.xml";

  public static String                  TRANSACTIONS_DIR_NAME = "transactions";

  public static String                  VAULTS_DIR_NAME       = "vaults";

  public static String                  WEBFILES_DIR_NAME     = "webfiles";

  public static String                  APP_FILES_DIR_NAME    = "applicationfiles";

  public static String                  EXT_DELIMETER         = ".";

  private static final SimpleDateFormat formatter             = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");

  private List<String>                  applicationFiles;

  private String                        schemaLocation;

  private String                        exportFileRootName;

  private String                        exportFileDirLocation;

  private String                        tempExportDirLocation;

  private File                          tempExportDir;

  private String                        xmlExportFileLocation;

  private String                        transactionsDirLocation;

  private String                        timeStampedName;

  private String                        webFilesDirLocation;

  private File                          webFilesDir;

  private String                        appFilesDirLocation;

  private File                          appFilesDir;

  private String                        vaultsDirLocation;

  private File                          vaultsDir;

  private java.util.Date                now                   = new java.util.Date();

  private String                        vaultFileMdClassRootId;

  private String                        webFileMdClassRootId;

  /**
   * Flag denoting if this should export the stored application file zip instead
   * of creating a new zip with the specified application files
   */
  private Boolean                       exportStoredApplicationFiles;

  private List<ITaskListener>           listeners;

  private HashMap<String, String>       properties;

  /**
   * 
   * @param _appDirs
   *          list of directories to backup from the application. The path
   *          starts at the root of the web application.
   * @param _schemaLocation
   * @param _exportFileRootName
   * @param _exportFileLocationDir
   */
  public TransactionExportManager(List<String> _appDirs, String _schemaLocation, File _exportFile)
  {
    this(_appDirs, _schemaLocation, _exportFile.getName(), _exportFile.getParent());
  }

  public TransactionExportManager(List<String> _appDirs, String _schemaLocation, String _exportFileRootName, String _exportFileLocationDir)
  {
    this.listeners = new LinkedList<ITaskListener>();
    this.exportStoredApplicationFiles = false;

    this.applicationFiles = _appDirs;
    this.schemaLocation = _schemaLocation;

    this.properties = new HashMap<String, String>();
    this.properties.put(TransactionConstants.VERSION_PROPERTY, RunwayMetadataVersion.getCurrentVersion().toString());

    // Remove the zip extension if it has one
    StringTokenizer toke = new StringTokenizer(_exportFileRootName, EXT_DELIMETER);

    if (toke.hasMoreTokens())
    {
      _exportFileRootName = toke.nextToken();
    }

    this.exportFileRootName = _exportFileRootName;
    this.exportFileDirLocation = _exportFileLocationDir;

    this.timeStampedName = this.exportFileRootName + "-" + formatter.format(now);
    this.tempExportDirLocation = exportFileDirLocation + File.separator + "_temp_" + this.timeStampedName + File.separator;

    this.transactionsDirLocation = this.tempExportDirLocation + File.separator + TRANSACTIONS_DIR_NAME + File.separator;

    // remove double separators
    while (this.tempExportDirLocation.contains(File.separator + File.separator))
    {
      this.tempExportDirLocation = this.tempExportDirLocation.replace(File.separator + File.separator, File.separator);
    }

    this.xmlExportFileLocation = this.tempExportDirLocation + XML_EXPORT_FILE;

    this.tempExportDir = new File(this.tempExportDirLocation);
    this.tempExportDir.mkdirs();

    this.webFilesDirLocation = this.tempExportDirLocation + File.separator + WEBFILES_DIR_NAME + File.separator;
    this.webFilesDir = new File(webFilesDirLocation);
    this.webFilesDir.mkdirs();

    // Create directories for all of the vaults
    this.vaultsDirLocation = this.tempExportDirLocation + File.separator + VAULTS_DIR_NAME + File.separator;
    this.vaultsDir = new File(this.vaultsDirLocation);
    this.vaultsDir.mkdirs();

    // Creates director for the application files
    this.appFilesDirLocation = this.tempExportDirLocation + File.separator + APP_FILES_DIR_NAME + File.separator;
    this.appFilesDir = new File(this.appFilesDirLocation);
    this.appFilesDir.mkdirs();

    QueryFactory qf = new QueryFactory();
    BusinessDAOQuery vaultQ = qf.businessDAOQuery(VaultInfo.CLASS);

    OIterator<BusinessDAOIF> i = vaultQ.getIterator();
    try
    {
      for (BusinessDAOIF vaultDAOIF : i)
      {
        new File(this.getExportVaultLocation((VaultDAOIF) vaultDAOIF)).mkdirs();
      }
    }
    finally
    {
      i.close();
    }

    // Check if it is a web file or a vault file
    MdClassDAOIF vaultFileMdClass = MdClassDAO.getMdClassDAO(VaultFileInfo.CLASS);
    MdClassDAOIF webFileMdClass = MdClassDAO.getMdClassDAO(WebFileInfo.CLASS);

    this.vaultFileMdClassRootId = IdParser.parseRootFromId(vaultFileMdClass.getId());
    this.webFileMdClassRootId = IdParser.parseRootFromId(webFileMdClass.getId());

  }

  public Boolean getExportStoredApplicationFiles()
  {
    return exportStoredApplicationFiles;
  }

  public void setExportStoredApplicationFiles(Boolean exportStoredApplicationFiles)
  {
    this.exportStoredApplicationFiles = exportStoredApplicationFiles;
  }

  public void addProperty(String name, String value)
  {
    this.properties.put(name, value);
  }

  /**
   * 
   * @param vaultDAOIF
   * @return
   */
  private String getExportVaultLocation(VaultDAOIF vaultDAOIF)
  {
    return this.vaultsDirLocation + vaultDAOIF.getId();
  }

  /**
   * Produces a ziped export file
   * 
   * @param startExportSequence
   *          incluseive
   * 
   * @return zip file name and location
   */
  public synchronized String export(Long startExportSequence)
  {
    return this.export(startExportSequence, null);
  }

  public void addListener(ITaskListener listener)
  {
    this.listeners.add(listener);
  }

  public void remoteListener(ITaskListener listener)
  {
    this.listeners.remove(listener);
  }

  /**
   * Produces a ziped export file of all records that have been exported since
   * the last export.
   * 
   * 
   * @return zip file name and location
   */
  public synchronized String export()
  {
    QueryFactory qf = new QueryFactory();

    BusinessDAOQuery transRecQ = qf.businessDAOQuery(TransactionRecordInfo.CLASS);
    ValueQuery valueQuery = qf.valueQuery();

    valueQuery.SELECT(F.MAX(transRecQ.aLong(TransactionRecordInfo.EXPORT_SEQUENCE), TransactionRecordInfo.EXPORT_SEQUENCE));
    valueQuery.WHERE(transRecQ.aLong(TransactionRecordInfo.EXPORT_SEQUENCE).NE((Long) null));

    Long lastExportSeq = null;

    OIterator<ValueObject> valueObjects = valueQuery.getIterator();
    for (ValueObject valueObject : valueObjects)
    {
      lastExportSeq = ( (com.runwaysdk.dataaccess.attributes.value.AttributeLong) valueObject.getAttributeIF(TransactionRecordInfo.EXPORT_SEQUENCE) ).getLongValue();
    }

    if (lastExportSeq == null)
    {
      lastExportSeq = 0L - 1;
    }

    return this.export(lastExportSeq + 1, null);
  }

  /**
   * Produces a ziped export file
   * 
   * @param startExportSequence
   *          incluseive
   * @param endExportSequence
   *          inclusive
   * 
   * @return zip file name and location
   */
  public synchronized String export(Long startExportSequence, Long endExportSequence)
  {
    boolean success = true;

    this.fireStart();

    try
    {
      TransactionRecordDAO.incrementTransactionRecordSequences();

      FileMarkupWriter writer = new FileMarkupWriter(this.xmlExportFileLocation);
      TransactionExportXML transactionExportXML = new TransactionExportXML(this.transactionsDirLocation, writer, schemaLocation);

      boolean firstTransactionItem = true;

      QueryFactory qf = new QueryFactory();

      BusinessDAOQuery q = qf.businessDAOQuery(TransactionRecordInfo.CLASS);
      q.WHERE(q.aLong(TransactionRecordInfo.EXPORT_SEQUENCE).GE(startExportSequence));
      if (endExportSequence != null)
      {
        q.AND(q.aLong(TransactionRecordInfo.EXPORT_SEQUENCE).LE(endExportSequence));
      }
      q.ORDER_BY_ASC(q.aLong(TransactionRecordInfo.EXPORT_SEQUENCE));

      Long numberOftransactions = q.getCount();

      OIterator<BusinessDAOIF> transactionRecords = q.getIterator();

      if (numberOftransactions > 0)
      {
        boolean proceesedTransaction = false;
        int transactionCount = 0;

        this.fireStartTask("Export_Transaction_Records", 100);

        for (BusinessDAOIF recordBusinessDAOIF : transactionRecords)
        {
          TransactionRecordDAOIF transactionRecordDAOIF = (TransactionRecordDAOIF) recordBusinessDAOIF;

          if (firstTransactionItem)
          {
            transactionExportXML.open(transactionRecordDAOIF.getExportSequence(), numberOftransactions);
            transactionExportXML.writeProperties(properties);
            firstTransactionItem = false;
          }

          transactionExportXML.exportTransaction(transactionRecordDAOIF);

          this.processTransactionItems(transactionRecordDAOIF);
          proceesedTransaction = true;

          transactionCount += 1;

          Integer currentPercentage = (int) ( ( (double) transactionCount / (double) numberOftransactions ) * 100 );

          this.fireProgressEvent(currentPercentage);
        }

        if (proceesedTransaction)
        {
          transactionExportXML.writeImportLog();
          transactionExportXML.close();
        }
      }
      else
      {
        transactionExportXML.open(1L, 1L);
        transactionExportXML.writeProperties(properties);
        transactionExportXML.writeImportLog();
        transactionExportXML.close();
      }

      writer.close();

      this.fireStartTask("Export_application_files", -1);

      this.exportApplicationFiles();

      String zipFileName = this.exportFileRootName + ".zip";

      this.fireStartTask("Create_zip_files", -1);

      String zipFileNameAndLocation = zipFiles(this.tempExportDirLocation, this.exportFileDirLocation, zipFileName);

      this.fireStartTask("Cleanup_temp_files", -1);

      try
      {
        FileIO.deleteDirectory(this.tempExportDir);
      }
      catch (IOException e)
      {
        // throw new ProgrammingErrorException(e);
      }

      return zipFileNameAndLocation;
    }
    catch (RuntimeException e)
    {
      success = false;

      throw e;
    }
    finally
    {
      this.fireDone(success);
    }
  }

  private void exportApplicationFiles()
  {
    if (!this.exportStoredApplicationFiles)
    {
      this.copyAndZipApplicationFiles();
    }
    else
    {
      this.copyStoredApplicateFiles();
    }
  }

  /**
   * Copies the highest stored application file zip into the temp zip directory
   */
  private void copyStoredApplicateFiles()
  {
    File current = TransactionImportManager.getHighestZipFile();

    if (current != null)
    {
      String path = this.appFilesDir + File.separator + current.getName();
      File file = new File(path);
      try
      {
        new File(this.appFilesDirLocation + File.separator).mkdirs();

        FileOutputStream out = new FileOutputStream(file);
        FileInputStream in = new FileInputStream(current);

        FileIO.write(out, in);
      }
      catch (IOException e)
      {
        throw new FileWriteException(file, e);
      }
    }
  }

  /**
   * Copies the given application dirs and zips them up
   */
  private void copyAndZipApplicationFiles()
  {
    if (this.applicationFiles.size() == 0)
    {
      return;
    }

    String deployPath = DeployProperties.getDeployPath();

    for (String applicationFile : applicationFiles)
    {
      String sourcePathLocation = deployPath + File.separator + applicationFile;
      File sourcePath = new File(sourcePathLocation);

      String destPathLocation = this.appFilesDirLocation + File.separator + applicationFile;
      File destPath = new File(destPathLocation);

      try
      {
        FileIO.copy(sourcePath, destPath);
      }
      catch (IOException e)
      {
        throw new FileWriteException(destPath, e);
      }
    }

    final String zipFileName = Database.getNextSequenceNumber() + ".zip";

    zipFiles(this.appFilesDirLocation, this.appFilesDirLocation, zipFileName);

    File directory = new File(this.appFilesDirLocation);

    try
    {
      // Delete all files except for the zip file of all application files
      FileFilter filter = new FileFilter()
      {
        @Override
        public boolean accept(File file)
        {
          return ! ( file.getName().equals(zipFileName) );
        }
      };

      FileIO.deleteFolderContent(directory, filter);
    }
    catch (IOException e)
    {
      throw new FileWriteException(directory, e);
    }
  }

  private void fireProgressEvent(int progress)
  {
    for (ITaskListener listener : this.listeners)
    {
      listener.taskProgress(progress);
    }
  }

  private void fireStartTask(String taskName, int amount)
  {
    for (ITaskListener listener : this.listeners)
    {
      listener.taskStart(taskName, amount);
    }
  }

  private void fireDone(boolean success)
  {
    for (ITaskListener listener : this.listeners)
    {
      listener.done(success);
    }
  }

  private void fireStart()
  {
    for (ITaskListener listener : this.listeners)
    {
      listener.start();
    }
  }

  /**
   * Returns the name and location of the zip file.
   * 
   * @param sourceDirLocation
   *          Location
   * @param zipFileDirLocation
   *          File
   * @param zipFileName
   *          Name of the zip file including extension
   * @return the name and location of the zip file.
   */
  private static String zipFiles(String sourceDirLocation, String zipFileDirLocation, String zipFileName)
  {
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

    File source = new File(sourceDirLocation);
    String zipFileNameAndLocation = zipFileDirLocation + File.separator + zipFileName;

    try
    {
      FileIO.zip(source, fileFilter, new File(zipFileNameAndLocation));
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }

    return zipFileNameAndLocation;
  }

  private void processTransactionItems(TransactionRecordDAOIF transactionRecordDAOIF)
  {
    OIterator<BusinessDAOIF> transactionItems = transactionRecordDAOIF.getTransactionItems();

    for (BusinessDAOIF itemBusinessDAOIF : transactionItems)
    {
      // Do not export the file if it has been deleted
      TransactionItemDAOIF transactionItemDAOIF = (TransactionItemDAOIF) itemBusinessDAOIF;

      if (transactionItemDAOIF.getItemAction().equals(ActionEnumDAO.DELETE))
      {
        continue;
      }
      String componentId = transactionItemDAOIF.getComponentId();

      String mdTypeRootId = IdParser.parseMdTypeRootIdFromId(componentId);

      if (mdTypeRootId.equals(this.vaultFileMdClassRootId))
      {
        // Even though this is an update or a create action, the object could
        // have been deleted later;
        VaultFileDAOIF vaultFileDAOIF;
        try
        {
          vaultFileDAOIF = VaultFileDAO.get(transactionItemDAOIF.getComponentId());
        }
        catch (DataNotFoundException e)
        {
          continue;
        }
        this.exportVaultFile(vaultFileDAOIF);
      }
      else if (mdTypeRootId.equals(this.webFileMdClassRootId))
      {
        // Even though this is an update or a create action, the object could
        // have been deleted later;
        WebFileDAOIF webFileDAOIF;
        try
        {
          webFileDAOIF = WebFileDAO.get(transactionItemDAOIF.getComponentId());
        }
        catch (DataNotFoundException e)
        {
          continue;
        }
        this.exportWebFile(webFileDAOIF);
      }
    }
  }

  /**
   * Writes the file to the export directory of the vault
   * 
   * @param vaultFile
   */
  private void exportVaultFile(VaultFileDAOIF vaultFile)
  {
    VaultDAOIF vault = vaultFile.getVault();
    String exportVault = this.getExportVaultLocation(vault);

    if (!vaultFile.getFile().exists())
    {
      return;
    }

    String exportLocation = exportVault + File.separator + vaultFile.getVaultFilePath() + vaultFile.getVaultFileName();
    File exportFile = new File(exportLocation);

    // Make the parent directories
    exportFile.getParentFile().mkdirs();

    try
    {
      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(exportFile));
      try
      {
        InputStream inputStream = vaultFile.getFileStream();

        try
        {
          FileIO.write(outputStream, inputStream);
        }
        finally
        {
          inputStream.close();
        }
      }
      finally
      {
        outputStream.close();
      }
    }
    catch (IOException e)
    {
      throw new FileWriteException(exportFile, e);
    }
  }

  /**
   * Writes the file to the export directory of the webfile
   * 
   * @param webFileDAOIF
   */
  private void exportWebFile(WebFileDAOIF webFileDAOIF)
  {
    File thisExportWebFileFile = new File(this.webFilesDirLocation + webFileDAOIF.getId());

    try
    {
      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(thisExportWebFileFile));

      try
      {
        InputStream inputStream = webFileDAOIF.getFile();

        try
        {
          FileIO.write(outputStream, inputStream);
        }
        finally
        {
          inputStream.close();
        }
      }
      finally
      {
        outputStream.close();
      }
    }
    catch (IOException e)
    {
      throw new FileWriteException(thisExportWebFileFile, e);
    }
  }

  /**
   * arg[0] root file name of the export file. Example: "backup" would produce a
   * "backup.zip" file. arg[1] directory the zip file will go arg[2] start
   * transaction export sequence number arg[3] end transaction export sequence
   * number (optional)
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    if (args.length < 2 || args.length > 4)
    {
      String errMessage = "Invalid number of arguments given.  Please provide \n" + "1) root file name of the export file.  Example: \"backup\" would produce a \"backup.zip\" file\n" + "2) directory the zip file will go\n" + "3) start transaction export sequence number (optional)\n" + "4) end transaction export sequence number (optional)\n";
      throw new RuntimeException(errMessage);
    }
    else if (args.length == 2)
    {
      export(new LinkedList<String>(), CommonProperties.getTransactionXMLschemaLocation(), args[0], args[1], new TransactionEventChangeListener());
    }
    else if (args.length == 3)
    {
      export(new LinkedList<String>(), Long.parseLong(args[2]), CommonProperties.getTransactionXMLschemaLocation(), args[0], args[1], new TransactionEventChangeListener());
    }
    else if (args.length == 4)
    {
      export(new LinkedList<String>(), Long.parseLong(args[2]), Long.parseLong(args[3]), CommonProperties.getTransactionXMLschemaLocation(), args[0], args[1], new TransactionEventChangeListener());
    }
  }

  /**
   * Produces a ziped export file of all records that have been exported since
   * the last export.
   * 
   * @param _appDirs
   *          list of directories to backup from the application. The path
   *          starts at the root of the web application.
   * @param _schemaLocation
   * @param _exportFileRootName
   * @param _exportFileLocationDir
   * @param startExportSequence
   */
  @Request
  public static void export(List<String> _appDirs, String _schemaLocation, String _exportFileRootName, String _exportFileLocationDir, ITaskListener... listeners)
  {
    TransactionExportManager manager = new TransactionExportManager(_appDirs, _schemaLocation, _exportFileRootName, _exportFileLocationDir);

    for (ITaskListener listener : listeners)
    {
      manager.addListener(listener);
    }

    manager.export();
  }

  @Request
  public static void export(List<String> _appDirs, String _schemaLocation, File _exportFile, ITaskListener... listeners)
  {
    TransactionExportManager manager = new TransactionExportManager(_appDirs, _schemaLocation, _exportFile);

    for (ITaskListener listener : listeners)
    {
      manager.addListener(listener);
    }

    manager.export();
  }

  /**
   * 
   * @param _appDirs
   *          list of directories to backup from the application. The path
   *          starts at the root of the web application.
   * @param _startExportSequence
   * @param _schemaLocation
   * @param _exportFileRootName
   * @param _exportFileLocationDir
   */
  @Request
  public static void export(List<String> _appDirs, Long _startExportSequence, String _schemaLocation, String _exportFileRootName, String _exportFileLocationDir, ITaskListener... listeners)
  {
    export(_appDirs, _startExportSequence, null, _schemaLocation, _exportFileRootName, _exportFileLocationDir, listeners);
  }

  /**
   * 
   * @param _appDirs
   *          list of directories to backup from the application. The path
   *          starts at the root of the web application.
   * @param _startExportSequence
   * @param _endExportSequence
   * @param _schemaLocation
   * @param _exportFileRootName
   * @param _exportFileLocationDir
   */
  @Request
  public static void export(List<String> _appDirs, Long _startExportSequence, Long _endExportSequence, String _schemaLocation, String _exportFileRootName, String _exportFileLocationDir, ITaskListener... listeners)
  {
    TransactionExportManager manager = new TransactionExportManager(_appDirs, _schemaLocation, _exportFileRootName, _exportFileLocationDir);

    for (ITaskListener listener : listeners)
    {
      manager.addListener(listener);
    }

    manager.export(_startExportSequence, _endExportSequence);
  }
}
