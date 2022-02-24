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
package com.runwaysdk.business.generation.dto;

import java.util.Locale;

import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.MdExceptionDAOIF;

public class SmartExceptionDTOStubGenerator extends LocalizableDTOStubGenerator
{
  public SmartExceptionDTOStubGenerator(MdExceptionDAOIF mdExceptionIF)
  {
    super(mdExceptionIF);
  }

  @Override
  protected void writeConstructor()
  {
    super.writeConstructor();

    String string = String.class.getName();
    String clientRequest = ClientRequestIF.class.getName();
    String throwable = Throwable.class.getName();
    String locale = Locale.class.getName();

    // Constructor: (ExceptionDTO exceptionDTO)
    getWriter().writeLine(
        "public " + getFileName() + "(" + ExceptionDTO.class.getName() + " exceptionDTO)");
    getWriter().openBracket();
    getWriter().writeLine("super(exceptionDTO);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor: ClientRequestIF clientRequest, Locale locale)
    getWriter().writeLine("public " + getFileName() + "(" + clientRequest + " clientRequest, " + locale + " locale)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, locale);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor: ClientRequestIF clientRequest, Locale locale, String developerMessage)
    getWriter().writeLine(
        "public " + getFileName() + "(" + clientRequest + " clientRequest, " + locale + " locale,"
            + String.class.getName() + " developerMessage)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, locale, developerMessage);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor: ClientRequestIF clientRequest, Locale locale, Exception cause)
    getWriter().writeLine(
        "public " + getFileName() + "(" + clientRequest + " clientRequest, " + locale + " locale, " + throwable
            + " cause)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, locale, cause);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor: ClientRequestIF clientRequest, Locale locale, String developerMessage,
    // Exception cause)
    getWriter().writeLine(
        "public " + getFileName() + "(" + clientRequest + " clientRequest, " + locale + " locale, "
            + String.class.getName() + " developerMessage, " + throwable + " cause)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, locale, developerMessage, cause);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor: (ClientRequestIF clientRequest, Exception cause)
    getWriter().writeLine("public " + getFileName() + "(" + clientRequest + " clientRequest, " + throwable + " cause)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, cause);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Constructor: (ClientRequestIF clientRequest, String msg, String msg, Excpetion cause)
    getWriter()
        .writeLine(
            "public " + getFileName() + "(" + clientRequest + " clientRequest, " + string + " msg, " + throwable
                + " cause)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest, msg, cause);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

}
