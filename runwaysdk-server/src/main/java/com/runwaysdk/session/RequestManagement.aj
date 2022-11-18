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
package com.runwaysdk.session;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.RunwayException;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.RunwayExceptionIF;
import com.runwaysdk.RunwayProblem;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.Information;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.Message;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.Problem;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.Warning;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.transaction.LockObject;
import com.runwaysdk.localization.CommonExceptionMessageLocalizer;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.transport.conversion.RunwayProblemToRunwayProblemDTO;
import com.runwaysdk.transport.conversion.business.InformationToInformationDTO;
import com.runwaysdk.transport.conversion.business.ProblemToProblemDTO;
import com.runwaysdk.transport.conversion.business.SmartExceptionToExceptionDTO;
import com.runwaysdk.transport.conversion.business.WarningToWarningDTO;

public aspect RequestManagement extends AbstractRequestManagement
{

  public RequestManagement()
  {
    super();
    
    this.state               = new RequestAspectState();
  }
  
  public RequestAspectState getState()
  {
    return (RequestAspectState) this.state;
  }


  public pointcut request()
  : (nonThreadRequest() && !cflow(threadRequest(Request)));

  // public pointcut request(Request request)
  // : (nonThreadRequest(Request) && !cflow(threadRequest(Request)) &&
  // @annotation(request));

  public pointcut enterSession()
  : request();
  // : nonAnnotizedRequestEntryPoints() || request(Request);

  // Initialize session information
  Object around(String _sessionId) :  topLevelPermission(_sessionId)
  {
    try
    {
      this.getState().openRequest(_sessionId);

      Object returnObject = proceed(this.getState().getRequestState().getSession().getOid());

      this.getState().closeRequest(returnObject);

      return returnObject;
    }
    catch (Throwable ex)
    {
      throw this.getState().handleException(ex);
    }    
  }

  // Process any error message that occurs when logging in.
  Object around() :
    execution (* com.runwaysdk.facade.Facade.login(String, String, Locale[]))  ||
    execution (* com.runwaysdk.facade.Facade.loginUser(String, String))  ||
    execution (* com.runwaysdk.facade.Facade.logout(String))  ||
    execution (* com.runwaysdk.facade.Facade.changeLogin(String, String, String))
  {
    try
    {
      return proceed();
    }
    catch (Throwable ex)
    {
      throw this.getState().processException(ex, Locale.getDefault());
    }
  }

  after() returning : topLevelSession()
  {
    this.getState().afterReturning(thisJoinPoint);
  }

  after() throwing : topLevelSession()
  {
    this.getState().afterThrowing(thisJoinPoint);
  }
}
