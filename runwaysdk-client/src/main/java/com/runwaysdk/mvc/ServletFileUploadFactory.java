package com.runwaysdk.mvc;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.runwaysdk.controller.RequestManager;

public interface ServletFileUploadFactory
{
  public ServletFileUpload instance(RequestManager manager);
}
