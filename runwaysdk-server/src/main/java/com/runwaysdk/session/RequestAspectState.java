package com.runwaysdk.session;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.aspectj.lang.JoinPoint;

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

public class RequestAspectState extends AbstractRequestAspectState
{
  public RequestAspectState()
  {
    super();

    this.requestState = new RequestState();
  }

  public void openRequest(String _sessionId)
  {
    this.messageList.clear();

    if (_sessionId == null || _sessionId.trim().equals("") || !SessionFacade.containsSession(_sessionId))
    {
      // throw an invalid session exception
      String errorMsg = "Session [" + _sessionId + "] does not exist or has expired.";

      throw new InvalidSessionException(errorMsg);
    }

    this.getRequestState().setSession(SessionFacade.getSessionForRequest(_sessionId));

    SessionFacade.renewSession(this.getRequestState().getSession().getOid());
  }

  public void closeRequest(Object returnObject)
  {
    if (messageList.size() > 0)
    {
      List<MessageDTO> messageDTOList = new LinkedList<MessageDTO>();
      List<WarningDTO> warningDTOList = new LinkedList<WarningDTO>();
      List<InformationDTO> informationDTOList = new LinkedList<InformationDTO>();

      for (Message message : messageList)
      {
        message.setLocale(this.getRequestState().getSession().getLocale());

        if (message instanceof Warning)
        {
          WarningToWarningDTO converter = new WarningToWarningDTO(this.getRequestState().getSession().getOid(), (Warning) message, false);
          WarningDTO warningDTO = converter.populate();
          warningDTOList.add(warningDTO);
          messageDTOList.add(warningDTO);
        }
        else if (message instanceof Information)
        {
          InformationToInformationDTO converter = new InformationToInformationDTO(this.getRequestState().getSession().getOid(), (Information) message, false);
          InformationDTO informationDTO = converter.populate();
          informationDTOList.add(informationDTO);
          messageDTOList.add(informationDTO);
        }
      }

      throw new MessageExceptionDTO(returnObject, messageDTOList, warningDTOList, informationDTOList);
    }
  }

  public RuntimeException handleException(Throwable ex)
  {
    if (ex instanceof InvalidSessionException)
    {
      InvalidSessionException isEx = (InvalidSessionException) ex;
      isEx.setLocale(Locale.getDefault());
      RunwayExceptionDTO runEx = new RunwayExceptionDTO(isEx.getClass().getName(), isEx.getLocalizedMessage(), isEx.getMessage());

      log.info(RunwayLogUtil.getExceptionLoggableMessage(runEx), runEx);

      return runEx;
    }
    else
    {
      if (ex instanceof MessageExceptionDTO)
      {
        log.info(RunwayLogUtil.getExceptionLoggableMessage(ex), ex);

        return (MessageExceptionDTO) ex;
      }
      else
      {
        return processException(ex, this.getRequestState().getSession().getLocale());
      }
    }

  }

  public RuntimeException processException(Throwable throwable, Locale locale)
  {
    Throwable ex = throwable;

    boolean isError = false;
    RuntimeException returnEx;

    if (ex instanceof InvocationTargetException)
    {
      InvocationTargetException invocationTargetException = (InvocationTargetException) ex;

      Throwable te = invocationTargetException.getTargetException();

      if (te instanceof RunwayExceptionIF)
      {
        ex = te;
      }
    }

    if (ex instanceof AttributeNotification)
    {
      AttributeNotification attributeNotification = (AttributeNotification) ex;
      // if a new instance has problems associated with it, restore the original
      // oid that was sent to the server
      // from a client.
      if (this.idMap.containsKey(attributeNotification.getComponentId()))
      {
        attributeNotification.setComponentId(this.idMap.get(attributeNotification.getComponentId()));
      }
    }

    if (ex instanceof SmartException)
    {
      SmartException smartException = (SmartException) ex;
      BusinessFacade.setSmartExceptionLocale(smartException, locale);

      SmartExceptionToExceptionDTO converter = new SmartExceptionToExceptionDTO(this.getRequestState().getSession().getOid(), smartException, false);
      ExceptionDTO exceptionDTO = converter.populate();

      returnEx = ComponentDTOFacade.newSmartExceptionDTO(exceptionDTO);
    }
    else if (ex instanceof ProblemException)
    {
      ProblemException problemException = (ProblemException) ex;
      List<ProblemDTOIF> problemDTOIFList = new LinkedList<ProblemDTOIF>();

      for (ProblemIF problemIF : problemException.getProblems())
      {
        if (problemIF instanceof AttributeNotification)
        {
          AttributeNotification attributeNotification = (AttributeNotification) problemIF;
          // if a new instance has problems associated with it, restore the
          // original oid that was sent to the server
          // from a client.
          if (this.idMap.containsKey(attributeNotification.getComponentId()))
          {
            attributeNotification.setComponentId(this.idMap.get(attributeNotification.getComponentId()));
          }
        }

        problemIF.setLocale(locale);

        ProblemDTOIF problemDTOIF = null;

        if (problemIF instanceof Problem)
        {
          ProblemToProblemDTO converter = new ProblemToProblemDTO(this.getRequestState().getSession().getOid(), (Problem) problemIF, false);
          problemDTOIF = converter.populate();
        }
        else
        {
          RunwayProblemToRunwayProblemDTO converter = RunwayProblemToRunwayProblemDTO.getConverter((RunwayProblem) problemIF);
          problemDTOIF = converter.populate();
        }
        if (this.getRequestState().getSession() != null && !this.getRequestState().getSession().userHasRole(RoleDAOIF.DEVELOPER_ROLE))
        {
          problemDTOIF.setDeveloperMessage("");
        }
        problemDTOIFList.add(problemDTOIF);
      }

      problemException.setLocale(locale);

      returnEx = new ProblemExceptionDTO(problemException.getLocalizedMessage(), problemDTOIFList);
    }
    else if (ex instanceof RunwayException)
    {
      RunwayException fwEx = ( (RunwayException) ex );
      isError = fwEx instanceof ProgrammingErrorException; // special case to
      // capture abnormal
      // errors

      fwEx.setLocale(locale);

      String developerMessage;

      if (this.getRequestState().getSession() != null && this.getRequestState().getSession().userHasRole(RoleDAOIF.DEVELOPER_ROLE))
      {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        fwEx.printStackTrace(printWriter);
        developerMessage = result.toString();
      }
      else
      {
        developerMessage = "";
      }

      returnEx = new RunwayExceptionDTO(fwEx.getClass().getName(), fwEx.getLocalizedMessage(), developerMessage);
    }
    // If a runway exception was not thrown, then most likely a programming
    // error occurred.
    else
    {
      String developerMessage = "";

      if (this.getRequestState().getSession() != null && this.getRequestState().getSession().userHasRole(RoleDAOIF.DEVELOPER_ROLE))
      {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        developerMessage = result.toString();
      }

      // This exception will notify developers/administrators that
      // a serious problem occurred.
      ProgrammingErrorException programmingErrorException = new ProgrammingErrorException(ex);
      programmingErrorException.setStackTrace(ex.getStackTrace());
      isError = true;

      String errMessage = ClientRequestIF.ERROR_MSG_DELIMITER + programmingErrorException.getClass().getName() + ClientRequestIF.ERROR_MSG_DELIMITER + CommonExceptionMessageLocalizer.runwayException(locale) + ClientRequestIF.ERROR_MSG_DELIMITER + developerMessage + ClientRequestIF.ERROR_MSG_DELIMITER;

      returnEx = new RuntimeException(errMessage);
    }

    if (isError)
    {
      log.error(RunwayLogUtil.getExceptionLoggableMessage(ex), ex);
    }
    else
    {
      log.info(RunwayLogUtil.getExceptionLoggableMessage(ex), ex);
    }

    return returnEx;
  }

  public void afterReturning(JoinPoint joinPoint)
  {
    try
    {
      if (this.getRequestState().getSession() != null)
      {
        ( (Session) this.getRequestState().getSession() ).setFirstMdMethodDAOIF(null);
        if (this.getRequestState().getSession().closeOnEndOfRequest())
        {
          SessionFacade.closeSession(this.getRequestState().getSession().getOid());

        }

      }

      this.getRequestState().commitAndCloseConnection(joinPoint);
    }
    finally
    {
      ( LockObject.getLockObject() ).releaseAppLocks(setAppLocksSet);
      setAppLocksSet.clear();

      this.idMap.clear();

      this.getRequestState().setSession(null);
    }
  }

  public void afterThrowing(JoinPoint joinPoint)
  {
    try
    {
      if (this.getRequestState().getSession() != null)
      {
        ( (Session) this.getRequestState().getSession() ).setFirstMdMethodDAOIF(null);
        SessionFacade.endOfRequest(this.getRequestState().getSession().getOid());

      }

      this.getRequestState().rollbackAndCloseConnection(joinPoint);
    }
    finally
    {
      ( LockObject.getLockObject() ).releaseAppLocks(setAppLocksSet);
      setAppLocksSet.clear();

      this.idMap.clear();

      this.getRequestState().setSession(null);
    }
  }

}
