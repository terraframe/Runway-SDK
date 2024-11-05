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
package com.runwaysdk.mvc;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItemFactory;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;

import com.runwaysdk.controller.RequestManager;

public class DefaultServletFileUploadFactory implements ServletFileUploadFactory
{
  @Override
  public JakartaServletFileUpload instance(RequestManager manager)
  {
    // Create a factory for disk-based file items
    FileItemFactory factory;
    try
    {
      factory = DiskFileItemFactory.builder()
          .setPath(Files.createTempDirectory(UUID.randomUUID().toString()))
          .get();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    // Create a new file upload handler
    JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);

    return upload;
  }

}
