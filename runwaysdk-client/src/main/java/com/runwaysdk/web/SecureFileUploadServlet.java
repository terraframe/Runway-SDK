/**
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
 */
package com.runwaysdk.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.io.FileWriteExceptionDTO;
import com.scand.fileupload.ProgressMonitorFileItemFactory;

public class SecureFileUploadServlet extends FileUploadServlet
{
  /**
   * Eclipse auto generated id
   */
  private static final long serialVersionUID = 4321135637358960948L;

  @SuppressWarnings("unchecked")
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,
      IOException
  {
    ClientRequestIF clientRequest = (ClientRequestIF)req.getAttribute(ClientConstants.CLIENTREQUEST);

    boolean isMultipart = ServletFileUpload.isMultipartContent(req);

    if (!isMultipart)
    {
      // TODO Change exception type
      String msg = "The HTTP Request must contain multipart content.";
      throw new RuntimeException(msg);
    }
    
    String fileId = req.getParameter("sessionId").toString().trim();
    FileItemFactory factory = new ProgressMonitorFileItemFactory(req, fileId);
    ServletFileUpload upload = new ServletFileUpload();
    
    upload.setFileItemFactory(factory);

    try
    {
      // Parse the request

      FileItemIterator iter = upload.getItemIterator(req);
      while (iter.hasNext())
      {
        FileItemStream item = iter.next();

        if(!item.isFormField())
        {
          String fullName = item.getName();
          int extensionInd = fullName.lastIndexOf(".");
          String fileName = fullName.substring(0, extensionInd);
          String extension = fullName.substring(extensionInd + 1);
          InputStream stream = item.openStream();
          
          BusinessDTO fileDTO = clientRequest.newSecureFile(fileName, extension, stream);
          
          // return the vault id to the dhtmlxVault callback
          req.getSession().setAttribute("FileUpload.Progress."+fileId, fileDTO.getId());
        }
      }
    }
    catch (FileUploadException e)
    {
      throw new FileWriteExceptionDTO(e.getLocalizedMessage());
    }
    catch (RuntimeException e)
    {
      req.getSession().setAttribute("FileUpload.Progress."+fileId, "fail: "+e.getLocalizedMessage());
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException
  {
    doPost(req, resp);
  }
}
