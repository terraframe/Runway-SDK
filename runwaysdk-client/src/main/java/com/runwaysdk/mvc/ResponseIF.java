package com.runwaysdk.mvc;

import java.io.IOException;

import javax.servlet.ServletException;

import com.runwaysdk.controller.RequestManager;

public interface ResponseIF
{

  public void handle(RequestManager manager) throws ServletException, IOException;

}
