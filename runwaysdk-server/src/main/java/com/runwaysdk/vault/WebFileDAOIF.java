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
package com.runwaysdk.vault;

import java.io.InputStream;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.BusinessDAOIF;


public interface WebFileDAOIF extends BusinessDAOIF, FileIF
{
  /**
   * Class WebFile.
   */
  public static final String CLASS = Constants.SYSTEM_PACKAGE + "." + "WebFile";

  /**
   * The name of the extension attribute in WebFile
   */
  public static final String EXTENSION = "fileExtension";

  /**
   * The name of the file name attribute in WebFile
   */
  public static final String FILE_NAME = "fileName";

  /**
   * The name of the vault file path attribute in WebFile
   */
  public static final String FILE_PATH = "filePath";

  /**
   * Return the extension of the WebFile
   * @return
   */
  public String getExtension();

  /**
   * Return the file name of the WebFile
   * @return
   */
  public String getFileName();

  /**
   * Return the path of the WebFile
   * @return
   */
  public String getFilePath();

  /**
   * Returns the fully qualified location of the file.
   *
   * @return fully qualified location of the file.
   */
  public String getFullFilePathAndName();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public WebFileDAO getBusinessDAO();

  /**
   * @return A stream containing the content of the web file
   */
  public InputStream getFile();

  /**
   * Writes an array of bytes to the web file
   *
   * @param bytes The byte array to write
   */
  public void putFile(byte[] bytes);

  /**
   * Writes a stream of data test the web file
   *
   * @param stream Stream to write
   */
  public void putFile(InputStream stream);
}
