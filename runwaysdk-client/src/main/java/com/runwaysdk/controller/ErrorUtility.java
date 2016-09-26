/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.request.ServletRequestIF;
import com.runwaysdk.request.ServletResponseIF;
import com.runwaysdk.session.AttributeReadPermissionExceptionDTO;
import com.runwaysdk.session.ReadTypePermissionExceptionDTO;
import com.runwaysdk.util.Base64;
import com.runwaysdk.web.json.JSONProblemExceptionDTO;
import com.runwaysdk.web.json.JSONRunwayExceptionDTO;

public class ErrorUtility implements Reloadable
{
  public static final String ERROR_MESSAGE_ARRAY = "errorMessageArray";

  public static final String ERROR_MESSAGE       = "errorMessage";

  public static final String DEVELOPER_MESSAGE   = "developerMessage";

  public static final String MESSAGE_ARRAY       = "messageArray";

  /**
   * Handles errors that are generated from a request sent asynchronously from
   * the RunwayControllerForm js widget.
   * 
   * @param t
   * @param req
   * @param resp
   * @throws IOException
   * @returns boolean Whether or not a redirect is required. A redirect is
   *          required if and only if the error is inlined.
   */
  public static boolean handleFormError(Throwable t, ServletRequestIF req, ServletResponseIF resp) throws IOException
  {
    t = ErrorUtility.filterServletException(t);

    if (t instanceof ProblemExceptionDTO)
    {
      ErrorUtility.prepareProblems((ProblemExceptionDTO) t, req, false);
      return true;
    }
    else
    {
      JSONRunwayExceptionDTO jsonE = new JSONRunwayExceptionDTO(t);
      resp.setStatus(500);
      resp.getWriter().print(jsonE.getJSON());
      return false;
    }
  }

  public static void prepareAjaxThrowable(Throwable t, ServletResponseIF resp) throws IOException
  {
    while (t instanceof InvocationTargetException)
    {
      t = t.getCause();
    }

    if (t instanceof ProblemExceptionDTO)
    {
      JSONProblemExceptionDTO jsonE = new JSONProblemExceptionDTO((ProblemExceptionDTO) t);
      resp.setStatus(500);
      resp.getWriter().print(jsonE.getJSON());
    }
    else
    {
      JSONRunwayExceptionDTO jsonE = new JSONRunwayExceptionDTO(t);
      resp.setStatus(500);
      resp.getWriter().print(jsonE.getJSON());
    }
  }

  private static void prepareProblems(ProblemExceptionDTO e, ServletRequestIF req, boolean ignoreNotifications)
  {
    List<String> messages = new LinkedList<String>();

    for (ProblemDTOIF problem : e.getProblems())
    {
      if ( ( !ignoreNotifications ) && ( problem instanceof AttributeNotificationDTO ))
      {
        String message = problem.getMessage();

        messages.add(message);
      }
    }

    if (messages.size() > 0)
    {
      req.setAttribute(ErrorUtility.ERROR_MESSAGE_ARRAY, messages.toArray(new String[messages.size()]));
    }
  }

  public static void prepareInformation(List<InformationDTO> list, ServletRequestIF req)
  {
    List<String> messages = new LinkedList<String>();

    for (InformationDTO problem : list)
    {
      messages.add(problem.getMessage());
    }

    if (messages.size() > 0)
    {
      req.setAttribute(ErrorUtility.MESSAGE_ARRAY, messages.toArray(new String[messages.size()]));
    }
  }

  public static boolean prepareThrowable(Throwable t, ServletRequestIF req, ServletResponseIF resp, Boolean isAsynchronus) throws IOException
  {
    return ErrorUtility.prepareThrowable(t, req, resp, isAsynchronus, true);
  }

  public static boolean prepareThrowable(Throwable t, ServletRequestIF req, ServletResponseIF resp, Boolean isAsynchronus, boolean ignoreNotifications) throws IOException
  {
    return prepareThrowable(t, req, resp.getOutputStream(), resp, isAsynchronus, ignoreNotifications);
  }

  public static boolean prepareThrowable(Throwable t, ServletRequestIF req, OutputStream out, ServletResponseIF resp, Boolean isAsynchronus, boolean ignoreNotifications) throws IOException
  {
    t = ErrorUtility.filterServletException(t);

    if (isAsynchronus)
    {
      if (t instanceof ProblemExceptionDTO)
      {
        JSONProblemExceptionDTO jsonE = new JSONProblemExceptionDTO((ProblemExceptionDTO) t);
        resp.setStatus(500);
        out.write(jsonE.getJSON().getBytes());
      }
      else
      {
        JSONRunwayExceptionDTO jsonE = new JSONRunwayExceptionDTO(t);
        resp.setStatus(500);
        out.write(jsonE.getJSON().getBytes());
      }

      return true;
    }
    else
    {
      if (t instanceof AttributeReadPermissionExceptionDTO)
      {
        throw (AttributeReadPermissionExceptionDTO) t;
      }
      else if (t instanceof ReadTypePermissionExceptionDTO)
      {
        throw (ReadTypePermissionExceptionDTO) t;
      }
      else if (t instanceof ProblemExceptionDTO)
      {
        ErrorUtility.prepareProblems((ProblemExceptionDTO) t, req, ignoreNotifications);
      }
      else
      {
        ErrorUtility.prepareThrowable(t, req);
      }
    }

    return false;
  }

  private static Throwable filterServletException(Throwable t)
  {
    int i = 0;
    while (t instanceof ServletException && i < 50)
    {
      t = t.getCause();
      i++;
    }
    return t;
  }

  private static void prepareThrowable(Throwable t, ServletRequestIF req)
  {
    String localizedMessage = t.getLocalizedMessage();

    req.setAttribute(ErrorUtility.ERROR_MESSAGE, localizedMessage);

    if (t instanceof ProgrammingErrorExceptionDTO)
    {
      try
      {
        ProgrammingErrorExceptionDTO pee = (ProgrammingErrorExceptionDTO) t;
        String developerMessage = pee.getDeveloperMessage();

        req.setAttribute(ErrorUtility.DEVELOPER_MESSAGE, developerMessage);
      }
      catch (Exception e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    else if (t instanceof RuntimeException)
    {
      throw (RuntimeException) t;
    }

  }

  private static String getErrorMessage(ServletRequestIF req)
  {
    Object errorMessage = req.getAttribute(ErrorUtility.ERROR_MESSAGE);

    if (errorMessage != null && errorMessage instanceof String)
    {
      return ErrorUtility.encodeMessage((String) errorMessage);
    }

    return null;
  }

  private static String getErrorMessageArray(ServletRequestIF req)
  {
    Object errorMessage = req.getAttribute(ErrorUtility.ERROR_MESSAGE_ARRAY);

    if (errorMessage != null && errorMessage instanceof String[])
    {
      StringBuffer buffer = new StringBuffer();

      for (String msg : (String[]) errorMessage)
      {
        buffer.append(msg + "\n");
      }

      return ErrorUtility.encodeMessage(buffer.toString());
    }

    return null;
  }

  private static String getMessageArray(ServletRequestIF req)
  {
    Object message = req.getAttribute(ErrorUtility.MESSAGE_ARRAY);

    if (message != null && message instanceof String[])
    {
      StringBuffer buffer = new StringBuffer();

      for (String msg : (String[]) message)
      {
        buffer.append(msg + "\n");
      }

      String compress = ErrorUtility.compress(buffer.toString());

      return ErrorUtility.encodeMessage(compress);
    }

    return null;
  }

  public static String getMessagesForJavascript(ServletRequestIF req)
  {
    Object object = req.getAttribute(ErrorUtility.MESSAGE_ARRAY);

    if (object != null && object instanceof String[])
    {
      String[] messages = (String[]) object;
      List<String> list = new LinkedList<String>();

      for (String message : messages)
      {
        message = message.replaceAll("\\s", " ");
        message = message.replaceAll("'", "\'");
        message = message.replaceAll("\"", "\\\"");

        list.add(message);
      }

      return "['" + StringUtils.join(list, "','") + "']";
    }

    return "null";
  }

  @SuppressWarnings("deprecation")
  private static String encodeMessage(String errorMessage)
  {
    try
    {
      return URLEncoder.encode(errorMessage, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      return URLEncoder.encode(errorMessage);
    }
  }

  public static void addErrorMessages(ServletRequestIF req, URLUtility utility)
  {
    String errorMessage = ErrorUtility.getErrorMessage(req);
    String errorMessageArray = ErrorUtility.getErrorMessageArray(req);
    String compressedMessage = ErrorUtility.getMessageArray(req);

    if (errorMessage != null)
    {
      utility.addParameter(ERROR_MESSAGE, errorMessage);
    }

    if (errorMessageArray != null)
    {
      utility.addParameter(ERROR_MESSAGE_ARRAY, errorMessageArray);
    }

    if (compressedMessage != null)
    {
      utility.addParameter(MESSAGE_ARRAY, compressedMessage);
    }
  }

  public static void prepareMessages(ServletRequestIF req)
  {
    String errorMessage = req.getParameter(ErrorUtility.ERROR_MESSAGE);
    String errorMessageArray = req.getParameter(ErrorUtility.ERROR_MESSAGE_ARRAY);
    String compressedMessage = req.getParameter(ErrorUtility.MESSAGE_ARRAY);

    if (errorMessage != null)
    {
      req.setAttribute(ERROR_MESSAGE, errorMessage);
    }

    if (errorMessageArray != null)
    {
      String[] array = errorMessageArray.split("\\n");

      req.setAttribute(ERROR_MESSAGE_ARRAY, array);
    }

    if (compressedMessage != null)
    {
      String message = ErrorUtility.decompress(compressedMessage);
      String[] array = message.split("\\n");

      req.setAttribute(MESSAGE_ARRAY, array);
    }
  }

  private static String decompress(String message)
  {
    try
    {
      byte[] bytes = Base64.decode(message);

      ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
      BufferedInputStream bufis = new BufferedInputStream(new GZIPInputStream(bis));
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      int len;
      while ( ( len = bufis.read(buf) ) > 0)
      {
        bos.write(buf, 0, len);
      }
      String retval = bos.toString();
      bis.close();
      bufis.close();
      bos.close();
      return retval;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private static String compress(String message)
  {
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      BufferedOutputStream bufos = new BufferedOutputStream(new GZIPOutputStream(bos));
      bufos.write(message.getBytes("UTF-8"));
      bufos.close();
      bos.close();

      byte[] bytes = bos.toByteArray();

      return Base64.encodeToString(bytes, false);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
