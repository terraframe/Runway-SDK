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
package com.runwaysdk.web.applet;

import java.awt.Color;
import java.awt.Container;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.TransferHandler;

import com.runwaysdk.web.applet.common.RemoteAppletAdapter;

public class UploadApplet extends JApplet
{
  /**
   * Auto generated serial ID
   */
  private static final long   serialVersionUID = -4597200402195985189L;

  /**
   * Default timeout threshold on RMI calls
   */
  private static final int    THRESHOLD        = 10000;

  /**
   * Container for all of the other panels
   */
  private Container           mainPanel;

  /**
   * List of files which have not yet been uploaded
   */
  private Set<File>           files;

  /**
   * Defines the drop logic of the main panel
   */
  private TransferHandler     handler;

  /**
   * Called when the entire file batch is done. This callback is
   * handed a json object that represents all uploaded files. When this
   * is called, the upload is complete.
   */
  private String onBatchSuccess;

  /**
   * Called for each successful file upload. This callback is
   * handed a json object that represents the uploaded file.
   */
  private String onFileSuccess;

  /**
   * The failure function callback
   */
  private String onFailure;

  /**
   * A json object that is the secondary input of the success and failure callbacks.
   * This can be used to store initialization data for the applet, especially if multiple applet
   * instances can exist on one page.
   */
  private String jsonInfo;

  /**
   * Session id of the user running the applet
   */
  private String              sessionId;

  /**
   * The RMI port number of the server
   */
  private Integer             portNumber;

  /**
   * The name of the server RMI registry
   */
  private String              registryName;

  /**
   * RMI adapter to communicate with the server
   */
  private RemoteAppletAdapter remoteAdapter;

  /**
   * Total number of bytes to be uploaded
   */
  private long                totalBytes;

  /**
   * Policy determining the look of applet
   */
  private DisplayPolicy       displayPolicy;

  /**
   * Flag indicating that the applet is uploading to the server
   */
  private boolean             uploading;

  /**
   * Self reference used in UploadTask
   */
  private JApplet             applet;

  /**
   * Timeout threshold for RMI calls
   */
  private int                 timeout;

  /**
   * Flag indicating if the applet is connected to the server
   */
  private boolean             connected;

  /**
   * Flag indicating if the applet only performs a single upload
   */
  private boolean             singleUpload;

  /**
   * Invokes the onFailure callback if one was specified.
   */
  private void invokeOnFailure(String error)
  {
    invokeJSFunction(onFailure, error);
  }

  /**
   * Invokes the onFileSuccess callback.
   *
   * @param json
   */
  private void invokeOnFileSuccess(String json)
  {
    invokeJSFunction(onFileSuccess, json);
  }

  /**
   * Invokes the onBatchSuccess callback.
   *
   * @param json
   */
  private void invokeBatchSuccess(String json)
  {
    invokeJSFunction(onBatchSuccess, json);
  }

  /**
   * Invokes a javascript method and escapes the parameter string correctly.
   *
   * @param json
   */
  private void invokeJSFunction(String method, String input)
  {
    if(method != null)
    {
      // force the input to be a string and escape all single quotes
      String escapedJSON = input != null ? input.replaceAll("'", "\\\\'") : "";
      String escapedJSONInfo = jsonInfo != null ? jsonInfo.replaceAll("'", "\\\\'") : "";
//      JSObject.getWindow(applet).eval(method + "(String('" + escapedJSON + "'), String('"+escapedJSONInfo+"'))");
    }
  }

  /**
   * Task for sequentially uploading a set of files in the background of the
   * applet.
   *
   * @author Justin Smethie
   */
  class UploadTask extends SwingWorker
  {
    /**
     * Set of files to upload
     */
    private Set<File>     files;

    /**
     * Policy determining the look of the applet
     */
    private DisplayPolicy displayPolicy;

    /**
     * Global total number of bytes which have been uploaded
     */
    private long          totalAmount;

    /**
     * The batch of files as a json array.
     */
    private JSONFileBatch fileBatch;

    /**
     * @param files Set of files to upload
     * @param displayPolicy Policy for displaying the applet
     * @param batch Flag indicating if the applet should make a batch callback
     */
    UploadTask(Set<File> files, DisplayPolicy displayPolicy)
    {
      this.files = files;
      this.displayPolicy = displayPolicy;
      this.totalAmount = 0;
      this.fileBatch = new JSONFileBatch();
    }

    @Override
    public Object construct()
    {
      // Create the global progress bar
      UnlimitedProgressBar globalProgressBar = displayPolicy.getGlobalProgress();

      for (File file : files)
      {
        FileProgressBar progressBar = (FileProgressBar) displayPolicy.getFileComponent(file);

        try
        {
          // Start the task for polling the monitored stream and updating the local progress bar
          MonitorWorker monitor = new MonitorWorker(progressBar, globalProgressBar, timeout, remoteAdapter, sessionId);
          String json = monitor.write();

          // Notify the browser a file has been uploaded.
          invokeOnFileSuccess(json);

          // add the json to the file batch
          fileBatch.add(json);
        }
        catch (FileNotFoundException e)
        {
          progressBar.setForeground(new Color(243, 71, 67));
          String error = progressBar.getFile().getName() + " - Unable to upload file.";
          progressBar.setString(error);
          invokeOnFailure(error);
        }
        catch (TimeoutException e)
        {
          progressBar.setForeground(new Color(243, 71, 67));
          String error = progressBar.getFile().getName() + " - Connection timed out.";
          progressBar.setString(error);
          invokeOnFailure(error);
        }
        catch (Throwable e)
        {
          progressBar.setForeground(new Color(243, 71, 67));
          String error = progressBar.getFile().getName() + " - Unable to upload file.";
          progressBar.setString(error);
          invokeOnFailure(error);
        }

        totalAmount += file.length();
      }

      return null;
    }

    @Override
    public void finished()
    {
      // Notify the browser of the batch upload
      invokeBatchSuccess(fileBatch.toString());

      // Clear the list of all files which need to be uploaded
      files.clear();
      totalBytes = 0;

      // Change the upload panel back to its pre-upload form
      displayPolicy.finishedUploading();
      displayPolicy.setLabelText(new FileSize(totalBytes).toString() + " Total");

      // Release the lock on uploading
      uploading = false;
    }
  }

  class ConnectionWorker extends SwingWorker
  {
    private int    port;

    private String host;

    private int    timeout;

    public ConnectionWorker(String host, int port, int timeout)
    {
      this.host = host;
      this.port = port;
      this.timeout = timeout;
    }

    public Object construct()
    {
      RMIClientSocketFactory socketFactory = new RMIClientSocketFactory()
      {
        public Socket createSocket(String host, int port) throws IOException
        {
          Socket socket = new Socket();

          // 10 sec timeout
          socket.connect(new InetSocketAddress(host, port), timeout);

          return socket;
        }
      };

      // Connect to the RMI server
      try
      {
        socketFactory.createSocket(host, port);

        Registry registry = LocateRegistry.getRegistry(host, port, socketFactory);
        remoteAdapter = (RemoteAppletAdapter) registry.lookup(registryName);
        displayPolicy.displayError("Connected to server");
        connected = true;
      }
      catch (Exception e)
      {
        String error = "Unable to connect to server: " + e.getLocalizedMessage();
        displayPolicy.displayError(error);
        invokeOnFailure(error);
      }

      return null;
    }
  }

  @Override
  public void init()
  {
    // Load parameters
    sessionId = this.getParameter("sessionId");
    registryName = this.getParameter("registryName");
    portNumber = this.getIntegerParameter("portNumber", 0);
    timeout = this.getIntegerParameter("timeout", THRESHOLD);
    singleUpload = this.getBooleanParameter("singleUpload", false);

    // set the javascript callbacks (they will be set to null if not specified)
    onFileSuccess = getParameter("onFileSuccess");
    onBatchSuccess = getParameter("onBatchSuccess");
    onFailure = getParameter("onFailure");
    jsonInfo = getParameter("jsonInfo");

    // Initialize fields
    files = new TreeSet<File>(new Comparator<File>()
    {
      public int compare(File o1, File o2)
      {
        return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
      }
    });

    handler = this.buildTransferHandler();
    totalBytes = 0;
    uploading = false;
    applet = this;

    if(singleUpload)
    {
      displayPolicy = new SinglePolicy(this);
    }
    else
    {
      displayPolicy = new StandardPolicy(this);
    }

    displayPolicy.getMainPanel().setTransferHandler(handler);
    displayPolicy.getClearButton().addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (!uploading)
        {
          totalBytes = 0;
          files.clear();

          // Update the display
          displayPolicy.clearFileComponents();
          displayPolicy.setLabelText(new FileSize(totalBytes).toString() + " Total");
        }
      }
    });

    displayPolicy.getBrowseButton().addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (!uploading && !(singleUpload && files.size() > 0))
        {
          JFileChooser chooser = new JFileChooser();
          chooser.setMultiSelectionEnabled(true);

          int returnVal = chooser.showOpenDialog(mainPanel);

          if (returnVal == JFileChooser.APPROVE_OPTION)
          {
            File[] selectedFiles = chooser.getSelectedFiles();
            addFiles(selectedFiles);
          }
        }
      }
    });

    final JButton uploadButton = displayPolicy.getUploadButton();
    uploadButton.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(MouseEvent e)
      {
        if (connected)
        {
          uploading = true;

          displayPolicy.createUploadingDisplays(totalBytes, files);

          UploadTask task = new UploadTask(files, displayPolicy);
          task.start();
        }
      }

      @Override
      public void mouseEntered(MouseEvent e)
      {
        displayPolicy.onUploadButton(uploadButton);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        displayPolicy.offUploadButton(uploadButton);
      }

    });

    displayPolicy.displayError("Connecting to server...");
    new ConnectionWorker(this.getCodeBase().getHost(), portNumber, timeout).start();

    this.setVisible(true);
    super.init();
  }

  private void addFiles(File[] array)
  {
    for (File file : array)
    {
      if (file.isDirectory())
      {
        // Recursivley get all of the file in the folder
        if(!singleUpload)
        {
          addFiles(file.listFiles());
        }
      }
      else if (!files.contains(file))
      {
        files.add(file);

        final JButton deleteButton = displayPolicy.createFileComponent(file);
        deleteButton.addMouseListener(new MouseFileAdapter(file)
        {
          @Override
          public void mouseEntered(MouseEvent e)
          {
            displayPolicy.onDeleteButton(deleteButton);
          }

          @Override
          public void mouseExited(MouseEvent e)
          {
            displayPolicy.offDeleteButton(deleteButton);
          }

          @Override
          public void mouseClicked(MouseEvent e)
          {
            // Delete the file from the file list
            files.remove(this.getFile());
            totalBytes -= this.getFile().length();

            displayPolicy.removeFileComponent(this.getFile());
            displayPolicy.setLabelText(new FileSize(totalBytes).toString() + " Total");
          }
        });

        totalBytes += file.length();
      }
    }

    // Display the updated total size
    displayPolicy.setLabelText(new FileSize(totalBytes).toString() + " Total");
  }

  /**
   * @return The TransferHandler responible for drop actions on the main panel
   */
  private final TransferHandler buildTransferHandler()
  {
    return new TransferHandler()
    {
      /**
       * Auto Generated serial ID
       */
      private static final long serialVersionUID = 5990284648843424528L;

      @SuppressWarnings("unchecked")
      @Override
      public boolean importData(JComponent comp, Transferable t)
      {
        if (!canImport(comp, t.getTransferDataFlavors()))
        {
          return false;
        }

        try
        {
          // Get the files that were dropped onto the panel
          List<File> selectedFiles = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

          // Add the files to the array of files to upload
          addFiles(selectedFiles.toArray(new File[selectedFiles.size()]));
        }
        catch (UnsupportedFlavorException e)
        {
          return false;
        }
        catch (IOException e)
        {
          return false;
        }

        return true;
      }

      @Override
      public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
      {

        // Do not add more files to upload if an upload is already in progress
        if (uploading || (singleUpload && files.size() > 0))
        {
          return false;
        }

        // make sure that one of the available flavors is of java file list type
        for (DataFlavor transferFlavor : transferFlavors)
        {
          if (transferFlavor.isFlavorJavaFileListType())
          {
            return true;
          }
        }

        return false;
      }
    };
  }

  /**
   * Helper class to print a file batch as a json array.
   */
  private class JSONFileBatch
  {
    private List<String> batch;

    private JSONFileBatch()
    {
      batch = new LinkedList<String>();
    }

    /**
     * Adds an individual file to the json array.
     *
     * @param fileJSON
     */
    private void add(String fileJSON)
    {
      batch.add(fileJSON);
    }

    /**
     * Returns the String of this object, which is a json array of json objects.
     */
    public String toString()
    {
      String json = "[";

      for (int i = 0; i < batch.size(); i++)
      {
        json += batch.get(i);

        // don't leave a comment after the last element
        if (i != batch.size() - 1)
        {
          json += ",";
        }
      }

      json += "]";

      return json;
    }
  }

  private int getIntegerParameter(String parameterName, int defaultValue)
  {
    try
    {
      return Integer.parseInt(this.getParameter(parameterName));
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }

  private boolean getBooleanParameter(String parameterName, boolean defaultValue)
  {
    try
    {
      return Boolean.parseBoolean(this.getParameter(parameterName));
    }
    catch (Exception e)
    {
      return defaultValue;
    }
  }
}
