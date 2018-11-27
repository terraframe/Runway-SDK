package com.runwaysdk.mvc;

import javax.servlet.ServletContext;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.runwaysdk.controller.RequestManager;

public class DefaultServletFileUploadFactory implements ServletFileUploadFactory
{
  @Override
  public ServletFileUpload instance(RequestManager manager)
  {
    // Create a factory for disk-based file items
    FileItemFactory factory = new DiskFileItemFactory();

    // Create a new file upload handler
    ServletFileUpload upload = new ServletFileUpload(factory);

    return upload;
  }

}
