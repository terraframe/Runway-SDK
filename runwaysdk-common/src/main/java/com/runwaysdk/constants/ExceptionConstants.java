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
package com.runwaysdk.constants;


public enum ExceptionConstants
{
  AttributeException(Constants.DATAACCESS_ATTRIBUTES_PACKAGE+".AttributeException"),

  ClientProgrammingErrorException(Constants.ROOT_PACKAGE+".ClientProgrammingErrorException"),

  AttributeReadPermissionExceptionDTO(Constants.SESSION_PACKAGE+".AttributeReadPermissionException"+TypeGeneratorInfo.DTO_SUFFIX),

  ConfigurationException(Constants.ROOT_PACKAGE+".ConfigurationException"),

  CoreException(Constants.DATAACCESS_PACKAGE+".CoreException"),

  ConversionException(Constants.CONVERSION_PACKAGE+".ConversionException"),

  FileWriteException(Constants.DATAACCESS_IO_PACKAGE+".FileWriteException"),

  ForbiddenMethodException(Constants.DATAACCESS_METADATA_PACKAGE+".ForbiddenMethodException"),

  LoaderDecoratorException(Constants.GENERATION_BUSINESS_PACKAGE+".LoaderDecoratorException"),

  ProgrammingErrorException(Constants.DATAACCESS_PACKAGE+".ProgrammingErrorException"),

  RMIClientException(Constants.REQUEST_PACKAGE+".RMIClientException"),

  SystemException(Constants.ROOT_PACKAGE+".SystemException"),

  UndefinedControllerActionException(Constants.CONTROLLER_PACKAGE+".UndefinedControllerActionException"),

  WebServiceClientRequestException(Constants.REQUEST_PACKAGE+".WebServiceClientRequestException");

  private String exceptionClass;

  private ExceptionConstants(String exceptionClass)
  {
    this.exceptionClass = exceptionClass;
  }

  /**
   * Return class name of exception.
   *
   * @return class name of exception.
   */
  public String getExceptionClass()
  {
    return this.exceptionClass;
  }

}
