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
package com.runwaysdk.transport.conversion;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.axis.AxisFault;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ClientProgrammingErrorException;
import com.runwaysdk.ClientRequest;
import com.runwaysdk.MessageExceptionDTO;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.RunwayExceptionIF;
import com.runwaysdk.ServerSideException;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ExceptionDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.RunwayProblemDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.request.WebServiceClientRequestException;
import com.runwaysdk.session.InvalidSessionExceptionDTO;
import com.runwaysdk.transport.conversion.dom.DocToAttributeProblemDTO;
import com.runwaysdk.transport.conversion.dom.DocToExcelProblemDTO;
import com.runwaysdk.transport.conversion.dom.DocToExceptionDTO;
import com.runwaysdk.transport.conversion.dom.DocToInformationDTO;
import com.runwaysdk.transport.conversion.dom.DocToProblemDTO;
import com.runwaysdk.transport.conversion.dom.DocToWarningDTO;
import com.runwaysdk.transport.conversion.dom.Elements;
import com.runwaysdk.web.json.JSONProblemExceptionDTO;
import com.runwaysdk.web.json.JSONRunwayExceptionDTO;
import com.runwaysdk.web.json.JSONSmartExceptionDTO;

public class ClientConversionFacade 
{

  public static RuntimeException buildThrowableFromWebService(Throwable throwable, ClientRequestIF clientRequestIF, boolean typeSafe)
  {
    return ClientConversionFacade.buildThrowable(throwable, clientRequestIF, true, typeSafe);
  }

  public static RuntimeException buildThrowable(Throwable throwable, ClientRequestIF clientRequestIF, boolean webServiceCall)
  {
    return ClientConversionFacade.buildThrowable(throwable, clientRequestIF, webServiceCall, true);
  }

  private static RuntimeException buildThrowable(Throwable throwable, ClientRequestIF clientRequestIF, boolean webServiceCall, boolean typeSafe)
  {
    Throwable e = throwable;

    if (e instanceof InvocationTargetException)
    {
      InvocationTargetException invocationTargetException = (InvocationTargetException) e;

      Throwable te = invocationTargetException.getTargetException();

      if (te instanceof RunwayExceptionIF)
      {
        e = te;
      }
    }

    String wrappedExceptionName = "";
    String serverBusinessMessage = "";
    String wrappedDeveloperMessage = "";

    if (e.getMessage() != null)
    {
      String[] exceptionParts = e.getMessage().split(ClientRequestIF.ERROR_MSG_DELIMITER);
      if (exceptionParts != null && exceptionParts.length == 4)
      {
        wrappedExceptionName = exceptionParts[1].trim();
        serverBusinessMessage = exceptionParts[2];
        wrappedDeveloperMessage = exceptionParts[3];
      }
    }

    if (webServiceCall)
    {
      if (e instanceof AxisFault)
      {
        AxisFault af = (AxisFault) e;
        Element[] elementArray = af.getFaultDetails();
        for (Element element : elementArray)
        {
          if (element.getNodeName().equals(Elements.EXCEPTION_DTO.getLabel()))
          {
            DocToExceptionDTO docToSmartExceptionDTO = new DocToExceptionDTO(clientRequestIF, element);
            ExceptionDTO exceptionDTO = docToSmartExceptionDTO.populate();
            SmartExceptionDTO smartExceptionDTO = ComponentDTOFacade
                .buildSmartExceptionDTO(exceptionDTO);
            if (typeSafe == false)
            {
              return smartExceptionDTO;
            }
            else
            {
              return (SmartExceptionDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(
                  clientRequestIF, smartExceptionDTO);
            }
          }
          else if (element.getNodeName().equals(Elements.PROBLEMEXCEPTION_DTO.getLabel()))
          {
            try
            {
              String localizedMessage = ( (String) ConversionFacade.getXPath()
                  .evaluate(Elements.PROBLEMEXCEPTION_LOCALIZED_MESSAGE.getLabel(), element,
                      XPathConstants.STRING) );

              List<ProblemDTOIF> problemDTOIFList = new LinkedList<ProblemDTOIF>();

              NodeList problemNodeList = ( (NodeList) ConversionFacade.getXPath().evaluate(
                  Elements.PROBLEM_DTO.getLabel(), element, XPathConstants.NODESET) );

              for (int i = 0; i < problemNodeList.getLength(); i++)
              {
                Element problemElement = (Element) problemNodeList.item(i);

                NodeList problemElementList = problemElement
                    .getElementsByTagName(Elements.SMART_PROBLEM_DTO.getLabel());
                if (problemElementList.getLength() == 1)
                {
                  Element loopProblemElement = (Element) problemElementList.item(0);
                  DocToProblemDTO docToProblemDTO = new DocToProblemDTO(clientRequestIF, loopProblemElement);
                  ProblemDTO problemDTOUnsafe = docToProblemDTO.populate();

                  if (typeSafe == false)
                  {
                    problemDTOIFList.add(problemDTOUnsafe);
                  }
                  else
                  {
                    ProblemDTO problemDTOSafe = (ProblemDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF, problemDTOUnsafe);
                    problemDTOIFList.add(problemDTOSafe);

                    if (problemDTOSafe instanceof AttributeNotificationDTO)
                    {
                      ClientRequest.addAttributeNotification(((ClientRequest)clientRequestIF), (AttributeNotificationDTO)problemDTOSafe);
                    }
                  }
                }

                NodeList attributeProblemElementList = problemElement.getElementsByTagName(Elements.ATTRIBUTEPROBLEM_DTO.getLabel());
                if (attributeProblemElementList.getLength() == 1)
                {
                  Element attributeProblemElement = (Element) attributeProblemElementList.item(0);
                  DocToAttributeProblemDTO converter = new DocToAttributeProblemDTO(attributeProblemElement);
                  ProblemDTOIF attributeProblem = converter.populate();
                  problemDTOIFList.add(attributeProblem);

                  if (attributeProblem instanceof AttributeNotificationDTO)
                  {
                  }
                }

                NodeList excelProblemElementList = problemElement.getElementsByTagName(Elements.EXCELPROBLEM_DTO.getLabel());
                if (excelProblemElementList.getLength() == 1)
                {
                  Element excelProblemElement = (Element) excelProblemElementList.item(0);
                  DocToExcelProblemDTO converter = new DocToExcelProblemDTO(excelProblemElement);
                  ProblemDTOIF excelProblem = converter.populate();
                  problemDTOIFList.add(excelProblem);
                }
              }

              return new ProblemExceptionDTO(localizedMessage, problemDTOIFList);
            }
            catch (XPathExpressionException ex)
            {
              String errString = "Improper XPath expression: " + ex.getMessage();
              throw new ConversionExceptionDTO(errString, ex);
            }
          }
          else if (element.getNodeName().equals(Elements.MESSAGEEXCEPTION_DTO.getLabel()))
          {
            try
            {
              List<MessageDTO> messageDTOList = new LinkedList<MessageDTO>();
              List<WarningDTO> warningDTOList = new LinkedList<WarningDTO>();
              List<InformationDTO> informationDTOList = new LinkedList<InformationDTO>();

              NodeList messageNodeList = ( (NodeList) ConversionFacade.getXPath().evaluate(
                  Elements.MESSAGE_DTO.getLabel(), element, XPathConstants.NODESET) );

              for (int i = 0; i < messageNodeList.getLength(); i++)
              {
                Element messageElement = (Element) messageNodeList.item(i);

                NodeList messageElementList = messageElement.getElementsByTagName(Elements.WARNING_DTO
                    .getLabel());
                if (messageElementList.getLength() == 1)
                {
                  Element warningElement = (Element) messageElementList.item(0);
                  DocToWarningDTO docToWarningDTO = new DocToWarningDTO(clientRequestIF, warningElement);
                  WarningDTO warningDTOUnsafe = docToWarningDTO.populate();

                  if (typeSafe == false)
                  {
                    messageDTOList.add(warningDTOUnsafe);
                    warningDTOList.add(warningDTOUnsafe);
                  }
                  else
                  {
                    MessageDTO typeSafeMessageDTO = (MessageDTO) ConversionFacade
                    .createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF, warningDTOUnsafe);

                    messageDTOList.add(typeSafeMessageDTO);
                    warningDTOList.add((WarningDTO)typeSafeMessageDTO);
                  }
                }

                NodeList informationElementList = messageElement
                    .getElementsByTagName(Elements.INFORMATION_DTO.getLabel());
                if (informationElementList.getLength() == 1)
                {
                  Element informationElement = (Element) informationElementList.item(0);
                  DocToInformationDTO docToInformationDTO = new DocToInformationDTO(clientRequestIF,
                      informationElement);
                  InformationDTO informationDTOUnsafe = docToInformationDTO.populate();

                  if (typeSafe == false)
                  {
                    messageDTOList.add(informationDTOUnsafe);
                    informationDTOList.add(informationDTOUnsafe);
                  }
                  else
                  {
                    MessageDTO typeSafeMessageDTO = (MessageDTO) ConversionFacade
                    .createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF, informationDTOUnsafe);

                    messageDTOList.add(typeSafeMessageDTO);
                    informationDTOList.add((InformationDTO)typeSafeMessageDTO);
                  }
                }
              }

              Node returnObjectNode = ( (Node) ConversionFacade.getXPath().evaluate(
                  Elements.RETURN_OBJECT.getLabel(), element, XPathConstants.NODE) );

              Element returnObjectElement = (Element) returnObjectNode.getFirstChild();

              Object returnObject = ConversionFacade.getObjectFromElement(clientRequestIF, returnObjectElement, true);

              return new MessageExceptionDTO(returnObject, messageDTOList, warningDTOList,
                  informationDTOList);
            }
            catch (XPathExpressionException ex)
            {
              String errString = "Improper XPath expression: " + ex.getMessage();
              throw new ConversionExceptionDTO(errString, ex);
            }
          }
          else if (element.getNodeName().equals(Elements.RUNWAYEXCEPTION_DTO.getLabel()))
          {
            try
            {
              String runwayExceptionDTOtype = ( (String) ConversionFacade.getXPath().evaluate(
                  Elements.RUNWAYEXCEPTION_DTO_TYPE.getLabel(), element, XPathConstants.STRING) );
              String localizedMessage = ( (String) ConversionFacade.getXPath().evaluate(
                  Elements.RUNWAYEXCEPTION_LOCALIZED_MESSAGE.getLabel(), element,
                  XPathConstants.STRING) );
              String developerMessage = ( (String) ConversionFacade.getXPath().evaluate(
                  Elements.RUNWAYEXCEPTION_DEVELOPER_MESSAGE.getLabel(), element,
                  XPathConstants.STRING) );

              return ConversionFacade.buildRunwayExceptionDTO(runwayExceptionDTOtype, localizedMessage,
                  developerMessage);
            }
            catch (XPathExpressionException ex)
            {
              String errString = "Improper XPath expression: " + ex.getMessage();
              throw new ConversionExceptionDTO(errString, ex);
            }
          }
        }
      }
      if (wrappedExceptionName.equals(""))
      {
        return new WebServiceClientRequestException(e);
      }
    }

    if (e instanceof MessageExceptionDTO)
    {
      MessageExceptionDTO messageExceptionDTO = (MessageExceptionDTO) e;

      List<MessageDTO> typeSafeMessageDTOList = new LinkedList<MessageDTO>();
      List<WarningDTO> typeSafeWarningDTOList = new LinkedList<WarningDTO>();
      List<InformationDTO> typeSafeInformationDTOList = new LinkedList<InformationDTO>();

      for (MessageDTO genericMessageDTO : messageExceptionDTO.getMessages())
      {
        MessageDTO typeSafeMessageDTO = (MessageDTO) ConversionFacade
            .createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF, genericMessageDTO);
        typeSafeMessageDTOList.add(typeSafeMessageDTO);

        if (typeSafeMessageDTO instanceof WarningDTO)
        {
          typeSafeWarningDTOList.add((WarningDTO) typeSafeMessageDTO);
        }
        else if (typeSafeMessageDTO instanceof InformationDTO)
        {
          typeSafeInformationDTOList.add((InformationDTO) typeSafeMessageDTO);
        }
      }

      Object typeUnsafeReturnObject = messageExceptionDTO.getReturnObject();

      return new MessageExceptionDTO(typeUnsafeReturnObject, typeSafeMessageDTOList,
          typeSafeWarningDTOList, typeSafeInformationDTOList);

    }
    else if (e instanceof ProblemExceptionDTO)
    {
      ProblemExceptionDTO problemExceptionDTO = (ProblemExceptionDTO) e;

      List<ProblemDTOIF> typeSafeProblemDTOList = new LinkedList<ProblemDTOIF>();
      List<? extends ProblemDTOIF> typeUnSafeProblemDTOList = problemExceptionDTO.getProblems();

      for (ProblemDTOIF problemDTOIF : typeUnSafeProblemDTOList)
      {
        ProblemDTOIF typeSafeCopy = null;

        if (problemDTOIF instanceof ProblemDTO)
        {
          typeSafeCopy = (ProblemDTO) ConversionFacade
            .createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF, (ProblemDTO) problemDTOIF);
          typeSafeProblemDTOList.add(typeSafeCopy);
        }
        else if (problemDTOIF instanceof AttributeProblemDTO)
        {
          typeSafeCopy = ConversionFacade.createTypeSafeCopy((AttributeProblemDTO) problemDTOIF);
          typeSafeProblemDTOList.add(typeSafeCopy);
        }
        else if (problemDTOIF instanceof RunwayProblemDTO)
        {
          typeSafeCopy = (RunwayProblemDTO) problemDTOIF;
          typeSafeProblemDTOList.add(typeSafeCopy);
        }

        if (typeSafeCopy != null && typeSafeCopy instanceof AttributeNotificationDTO)
        {
          ClientRequest.addAttributeNotification(((ClientRequest)clientRequestIF), (AttributeNotificationDTO)typeSafeCopy);
        }

      }

      return new ProblemExceptionDTO(problemExceptionDTO.getLocalizedMessage(), typeSafeProblemDTOList);

    }
    else if (e instanceof RunwayExceptionDTO)
    {
      RunwayExceptionDTO runwayExceptionDTO = (RunwayExceptionDTO) e;
      return ConversionFacade.buildRunwayExceptionDTO(runwayExceptionDTO.getType(), runwayExceptionDTO
          .getMessage(), runwayExceptionDTO.getDeveloperMessage());
    }
    else if (e instanceof SmartExceptionDTO)
    {
      return (SmartExceptionDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(clientRequestIF,
          (SmartExceptionDTO) e);
    }
    // If the string does not contain the delimiter, then the exception was not
    // thrown by the core.
    // Rather, it was thrown as a result of an error in the middleware.
    else
    {
      String dtoExceptionName = wrappedExceptionName + TypeGeneratorInfo.DTO_SUFFIX;

      try
      {
        Class<?> exceptionDTOclass = LoaderDecorator.load(dtoExceptionName);
        return (RuntimeException) exceptionDTOclass.getConstructor(String.class, String.class,
            String.class).newInstance(wrappedExceptionName, serverBusinessMessage,
            wrappedDeveloperMessage);
      }
      catch (IllegalArgumentException e1)
      {
        throw new ClientProgrammingErrorException(e);
      }
      catch (SecurityException e1)
      {
        throw new ClientProgrammingErrorException(e);
      }
      catch (InstantiationException e1)
      {
        throw new ClientProgrammingErrorException(e);
      }
      catch (IllegalAccessException e1)
      {
        throw new ClientProgrammingErrorException(e);
      }
      catch (InvocationTargetException e1)
      {
        throw new ClientProgrammingErrorException(e);
      }
      catch (NoSuchMethodException e1)
      {
        throw new ClientProgrammingErrorException(e);
      }
      catch (RuntimeException ex)
      {
        if (ex instanceof LoaderDecoratorExceptionIF)
        {
          return new ServerSideException(e, wrappedExceptionName, serverBusinessMessage);
        }
        else
        {
          throw ex;
        }
      }
    }

  }

  public static RuntimeException buildJSONThrowable(Throwable throwable, String sessionId, boolean webServiceCall)
  {
    Throwable e = throwable;

    if (e instanceof InvocationTargetException)
    {
      InvocationTargetException invocationTargetException = (InvocationTargetException) e;

      e = invocationTargetException.getTargetException();
    }

    String wrappedExceptionName = "";
    String serverBusinessMessage = "";
    String developerErrorMessage = "";

    if (e.getMessage() != null)
    {
      if (e.getMessage().contains(ClientRequestIF.ERROR_MSG_DELIMITER)) {
        String[] exceptionParts = e.getMessage().split(ClientRequestIF.ERROR_MSG_DELIMITER);
        if (exceptionParts != null && exceptionParts.length == 4)
        {
          wrappedExceptionName = exceptionParts[1].trim();
          serverBusinessMessage = exceptionParts[2];
          developerErrorMessage = exceptionParts[3];
        }
        else {
          developerErrorMessage = e.getMessage();
        }
      }
      else {
        developerErrorMessage = e.getMessage();
      }
    }

    if (webServiceCall)
    {
      // first build the exception as a DTO because there is no doc to json
      // conversion. The clientRequest is set to null because no type-safe
      // conversion
      // needs to take place (which requires a clientRequest call back to the
      // server).
      RuntimeException ex = buildThrowableFromWebService(e, null, false);
      return ClientConversionFacade.convertThrowableToJSONException(ex, wrappedExceptionName, developerErrorMessage, serverBusinessMessage);
    }

    return ClientConversionFacade.convertThrowableToJSONException(e, wrappedExceptionName, developerErrorMessage, serverBusinessMessage);
  }

  protected static RuntimeException convertThrowableToJSONException(Throwable e, String wrappedExceptionName, String developerMessage, String localizedMessage)
  {
    if (e instanceof SmartExceptionDTO)
    {
      throw new JSONSmartExceptionDTO((SmartExceptionDTO) e);
    }
    else if (e instanceof ProblemExceptionDTO)
    {
      throw new JSONProblemExceptionDTO((ProblemExceptionDTO) e);
    }
    else if (e instanceof RunwayExceptionDTO)
    {
      RunwayExceptionDTO re = (RunwayExceptionDTO) e;
      
      if (re.getType().equals(InvalidSessionExceptionDTO.CLASS)) {
        throw new InvalidSessionExceptionDTO(InvalidSessionExceptionDTO.CLASS, localizedMessage, developerMessage);
      }
      
      throw new JSONRunwayExceptionDTO((RunwayExceptionDTO) e);
    }
    else
    {
      throw new JSONRunwayExceptionDTO(wrappedExceptionName, developerMessage, localizedMessage);
    }
  }

}
