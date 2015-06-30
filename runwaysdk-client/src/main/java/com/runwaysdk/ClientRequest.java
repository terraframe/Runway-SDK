/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.constants.ClientProperties;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.JavaClientRequestInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.constants.WebFileInfo;
import com.runwaysdk.file.FileCache;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.request.ClientRequestException;
import com.runwaysdk.request.ConnectionLabel;
import com.runwaysdk.request.RMIClientRequest;
import com.runwaysdk.request.WebServiceClientRequest;
import com.runwaysdk.transport.conversion.ConversionFacade;

/**
 * This <code>ClientRequest</code> class defines abstract methods for all
 * <code>ClientRequest</code> subclasses to implement.
 */
public abstract class ClientRequest implements ClientRequestIF
{
  /**
   *
   */
  private static final long serialVersionUID = -1889363548399160805L;

  private ClientSession                               clientSession;

  /**
   * Id of the session.
   */
  private String                                      sessionId;

  private List<MessageDTO>                            messageList;

  private List<WarningDTO>                            warningList;

  private List<InformationDTO>                        informationList;

  private List<ProblemDTO>                            problemList;

  private Map<String, List<AttributeNotificationDTO>> attributeNotificationMap;

  /**
   * True if messages should be kept between requests to the server, false otherwise.
   */
  private boolean keepMessages;

  private final static String                         ATTRIBUTENOTIFICATION_KEY_DELIM = "-";


  /**
   * Creates a <code>CleintRequest</code> object and logs in anonymously.
   *
   * @param clientSession
   * @param locales
   */
  protected ClientRequest(ClientSession clientSession, Locale[] locales)
  {
    this.init(clientSession);

    this.sessionId = this.loginAnonymous(locales);
  }

  /**
   * Creates a <code>CleintRequest</code> object for the given sessionId.
   *
   * @param clientSession
   * @param sessionId
   */
  protected ClientRequest(ClientSession clientSession, String sessionId)
  {
    this.init(clientSession);

    this.sessionId = sessionId;
  }

  /**
   * Logs in under given userName and password.
   *
   * @param clientSession
   * @param userName
   * @param password
   * @param locales
   */
  protected ClientRequest(ClientSession clientSession, String userName, String password, Locale[] locales)
  {
    this.init(clientSession);

    this.sessionId = this.login(userName, password, locales);
  }

  protected void init(ClientSession clientSession)
  {
    this.clientSession = clientSession;
    this.messageList = new LinkedList<MessageDTO>();
    this.warningList = new LinkedList<WarningDTO>();
    this.problemList = new LinkedList<ProblemDTO>();
    this.informationList = new LinkedList<InformationDTO>();
    this.attributeNotificationMap = new HashMap<String, List<AttributeNotificationDTO>>();
    this.keepMessages = true;
  }

  protected void clearNotifications()
  {
    if (!keepMessages)
    {
      this.messageList.clear();
      this.warningList.clear();
      this.informationList.clear();
      this.attributeNotificationMap.clear();
    }
  }

  /**
   * True if messages should be kept between requests to the server, false otherwise.
   *
   * @param keepMessages
   */
  public void setKeepMessages(boolean keepMessages)
  {
    this.keepMessages = keepMessages;
  }

  /**
   * Returns true if messages should be kept between requests to the server, false otherwise.
   *
   * @return true if messages should be kept between requests to the server, false otherwise.
   */
  public boolean getKeepMessages()
  {
    return this.keepMessages;
  }
  /**
   * Returns all messages made in the previous request, including
   * <code>AttributeNotification</code>s.
   *
   * @return all messages made in the previous request, including
   *         <code>AttributeNotification</code>s.
   */
  public List<MessageDTO> getMessages()
  {
    return this.messageList;
  }

  /**
   * Returns all messages made in the previous request, but not including
   * <code>AttributeNotification</code>s.
   *
   * @return all messages made in the previous request, but not including
   *         <code>AttributeNotification</code>s.
   */
  public List<MessageDTO> getMessagesMinusAttributeNotifications()
  {
    List<MessageDTO> messagesWithoutNotifications = new LinkedList<MessageDTO>();

    for (MessageDTO messageDTO : this.messageList)
    {
      if (! ( messageDTO instanceof AttributeNotificationDTO ))
      {
        messagesWithoutNotifications.add(messageDTO);
      }
    }

    return messagesWithoutNotifications;
  }

  /**
   * Returns all warnings made in the previous request.
   *
   * @return all warnings made in the previous request.
   */
  public List<WarningDTO> getWarnings()
  {
    return this.warningList;
  }

  /**
   * Returns all information messages made in the previous request.
   *
   * @return all information messages made in the previous request.
   */
  public List<InformationDTO> getInformation()
  {
    return this.informationList;
  }

  private String buildAttributeNotificationKey(String componentId, String attributeName)
  {
    return componentId + ATTRIBUTENOTIFICATION_KEY_DELIM + attributeName;
  }

  protected void addAttributeNotification(AttributeNotificationDTO attributeNotification)
  {
    String key = this.buildAttributeNotificationKey(attributeNotification.getComponentId(), attributeNotification.getAttributeName());

    if (this.attributeNotificationMap.containsKey(key))
    {
      List<AttributeNotificationDTO> notificationList = this.attributeNotificationMap.get(key);
      notificationList.add(attributeNotification);
    }
    else
    {
      List<AttributeNotificationDTO> notificationList = new LinkedList<AttributeNotificationDTO>();
      notificationList.add(attributeNotification);
      this.attributeNotificationMap.put(key, notificationList);
    }
  }

  protected void addProblems(List<ProblemDTO> problems)
  {
    for(ProblemDTO problem : problems)
    {
      problemList.add(problem);
    }
  }

  /**
   * Returns a list of all AttributeNotifications that pertain to the attribute
   * from the component with the given id from the last request.
   *
   * @param componentId
   * @param attributeName
   * @return list of all AttributeNotifications that pertain to the attribute
   *         from the component with the given id from the last request.
   */
  public List<AttributeNotificationDTO> getAttributeNotifications(String componentId, String attributeName)
  {
    return this.attributeNotificationMap.get(this.buildAttributeNotificationKey(componentId, attributeName));
  }

  /**
   * Returns a list of all AttributeNotifications that pertain to any attribute
   * from the component with the given id from the last request.
   *
   * @param componentId
   * @return list of all AttributeNotifications that pertain to the attribute
   *         from the component with the given id from the last request.
   */
  public List<AttributeNotificationDTO> getAttributeNotifications(String componentId)
  {
    List<AttributeNotificationDTO> list = new LinkedList<AttributeNotificationDTO>();

    Set<String> keySet = this.attributeNotificationMap.keySet();

    for(String key : keySet)
    {
      if(key.contains(componentId))
      {
        list.addAll(this.attributeNotificationMap.get(key));
      }
    }

    return list;
  }


  private void updateMessageCollections(MessageDTO typeSafeMessageDTO)
  {
    this.messageList.add(typeSafeMessageDTO);

    if (typeSafeMessageDTO instanceof AttributeNotificationDTO)
    {
      this.addAttributeNotification((AttributeNotificationDTO) typeSafeMessageDTO);
    }

    if (typeSafeMessageDTO instanceof WarningDTO)
    {
      this.warningList.add((WarningDTO) typeSafeMessageDTO);
    }
    else if (typeSafeMessageDTO instanceof InformationDTO)
    {
      this.informationList.add((InformationDTO) typeSafeMessageDTO);
    }
  }

  /**
   * Sets the typesafe messages from the given <code>messageExceptionDTO</code>.
   * <b>precondition:</b>Message lists have been cleared.<br/>
   * <b>precondition:</b>Given messages are typeSafe.<br/>
   *
   * @param messageExceptionDTO
   */
  protected void setMessages(MessageExceptionDTO messageExceptionDTO)
  {
    for (MessageDTO typeSafeMessageDTO : messageExceptionDTO.getMessages())
    {
      updateMessageCollections(typeSafeMessageDTO);
    }
  }

  /**
   * Converts the given generic messages into typesafe ones. <b>precondition:</b>Message
   * lists have been cleared.<br/>
   *
   * @param messageExceptionDTO
   */
  protected void setMessagesConvertToTypeSafe(MessageExceptionDTO messageExceptionDTO)
  {
    for (MessageDTO genericMessageDTO : messageExceptionDTO.getMessages())
    {
      MessageDTO typeSafeMessageDTO = (MessageDTO) ConversionFacade.createTypeSafeCopyWithTypeSafeAttributes(this, genericMessageDTO);

      updateMessageCollections(typeSafeMessageDTO);
    }
  }

  public ClientSession getClientSession()
  {
    return this.clientSession;
  }

  protected void setLoginStatus(String username, boolean isLoggedIn)
  {
    this.getClientSession().setLoginStatus(username, isLoggedIn);
  }

  protected void setLoginStatus(boolean isPublicUser, boolean isLoggedIn)
  {
    this.getClientSession().setLoginStatus(isPublicUser, isLoggedIn);
  }

  protected void setIsPublicUser(String username)
  {
    this.getClientSession().setIsPublicUser(username);
  }

  protected void setIsLoggedIn(boolean isLoggedIn)
  {
    this.getClientSession().setIsLoggedIn(isLoggedIn);
  }

  /**
   * Returns the session id used by the clientRequest to connect to the back-end.
   * @return session id used by the clientRequest to connect to the back-end.
   */
  public String getSessionId()
  {
    return this.sessionId;
  }

  /**
   * Returns the <code>Locale</code> used by the clientRequest.
   * @return <code>Locale</code> used by the clientRequest.
   */
  public Locale[] getLocales()
  {
    return clientSession.getLocales();
  }

  protected void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
    this.getClientSession().setSessionId(sessionId);
  }

  /**
   * Returns true if the <code>CleintRequest</code> is logged in as the
   * anonymouse public user, false otherwise.
   *
   * @return true if the <code>CleintRequest</code> is logged in as the
   *         anonymouse public user, false otherwise.
   */
  public boolean isPublicUser()
  {
    return this.getClientSession().isPublicUser();
  }

  /**
   * Returns true if the <code>CleintRequest</code> is loggedIn, false
   * otherwise.
   *
   * @return true if the <code>CleintRequest</code> is loggedIn, false
   *         otherwise.
   */
  public boolean isLoggedIn()
  {
    return this.getClientSession().isLoggedIn();
  }

  /**
   * Creates a session with the public user.
   *
   * @param locales
   * @return id of the new session.
   */
  protected abstract String loginAnonymous(Locale[] locales);

  /**
   * Creates a session with the public user for the given dimension.
   *
   * @param dimension
   * @param locales
   * @return id of the new session.
   */
  protected abstract String loginAnonymous(String dimension, Locale[] locales);

  /**
   * Attempts to log a user in with the specified username and password.
   *
   * @param username
   * @param password
   * @param locales
   * @return A String representing the user's session id.
   */
  protected abstract String login(String username, String password, Locale[] locales);

  /**
   * Sets the dimension of an existing Session.
   *
   * @param sessionId The id of the Session.
   * @param dimensionKey key of a MdDimension.
   */
  protected abstract void setDimension(String sessionId, String dimensionKey);

  /**
   * Attempts to log a user in with the specified username and password for the given dimension.
   *
   * @param username
   * @param password
   * @param dimensionKey
   * @param locales
   * @return A String representing the user's session id.
   */
  protected abstract String login(String username, String password, String dimensionKey, Locale[] locales);

  /**
   * Attempts to log a user in with the specified username and password.
   *
   * @param username
   * @param password
   * @return Object containging info on the user.
   */
  protected abstract void changeLogin(String username, String password);

  /**
   * Logs a user out with the specified session id.
   *
   */
  protected abstract void logout();

  /**
   * Returns the <code>CleintRequest</code> with a session logged in as the
   * anonymous user. The default locale is used.
   *
   * @param clientSession
   * @param userName
   * @param password
   * @return <code>CleintRequest</code> with a session logged in as the
   *         anonymous user.
   */
  protected static ClientRequestIF getRequest(ClientSession clientSession, Locale[] locales)
  {
    ClientRequestIF clientRequestIF = null;

    ConnectionLabel.Type type = clientSession.getConnectionLabel().getType();
    if (type.equals(ConnectionLabel.Type.JAVA))
    {
      // we must use reflection here so that the runwaysdk client jar can
      // be compiled without the JavaCleintRequest -> JavaController dependency.
      try
      {
        Class<?> clazz = LoaderDecorator.load(JavaClientRequestInfo.CLASS);
        Constructor<?> constructor = clazz.getConstructor(ClientSession.class, Locale[].class);

        Object object = constructor.newInstance(clientSession, locales);

        return (ClientRequestIF) object;
      }
      catch (InvocationTargetException e)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te instanceof RuntimeException)
        {
          throw (RuntimeException) te;
        }
        else
        {
          throw new ClientRequestException(e);
        }
      }
      catch (Exception e)
      {
        throw new ClientRequestException(e);
      }
    }
    else if (type.equals(ConnectionLabel.Type.RMI))
    {
      clientRequestIF = new RMIClientRequest(clientSession, locales);
    }
    else if (type.equals(ConnectionLabel.Type.WEB_SERVICE))
    {
      clientRequestIF = new WebServiceClientRequest(clientSession, locales);
    }

    return clientRequestIF;
  }

  /**
   * Returns the clientRequest for a session with the given id.
   *
   * @param clientSession
   * @param sessionId
   * @return clientRequest for a session with the given id.
   */
  protected static ClientRequestIF getRequest(ClientSession clientSession, String sessionId)
  {
    ClientRequestIF clientRequest = null;

    ConnectionLabel.Type type = clientSession.getConnectionLabel().getType();
    if (type.equals(ConnectionLabel.Type.JAVA))
    {
      // we must use reflection here so that the runwaysdk client jar can
      // be compiled without the JavaClientRequest -> JavaController dependency.
      try
      {
        Class<?> clazz = LoaderDecorator.load(JavaClientRequestInfo.CLASS);
        Object object = clazz.getConstructor(ClientSession.class, String.class).newInstance(clientSession, sessionId);
        return (ClientRequestIF) object;
      }
      catch (InvocationTargetException e)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te instanceof RuntimeException)
        {
          throw (RuntimeException) te;
        }
        else
        {
          throw new ClientRequestException(e);
        }
      }
      catch (Exception e)
      {
        throw new ClientRequestException(e);
      }
    }
    else if (type.equals(ConnectionLabel.Type.RMI))
    {
      clientRequest = new RMIClientRequest(clientSession, sessionId);
    }
    else if (type.equals(ConnectionLabel.Type.WEB_SERVICE))
    {
      clientRequest = new WebServiceClientRequest(clientSession, sessionId);
    }

    return clientRequest;
  }

  /**
   * Returns the clientRequest with a session logged in as the given user. The
   * default locale is used.
   *
   * @param clientSession
   * @param userName
   * @param password
   * @return clientRequest with a session logged in as the given user.
   */
  protected static ClientRequestIF getRequest(ClientSession clientSession, String userName, String password)
  {
    return getRequest(clientSession, userName, password, new Locale[]{CommonProperties.getDefaultLocale()});
  }

  /**
   * Returns the clientRequest for the given label with a session logged in as
   * the given user.
   *
   * @param clientSession
   * @param label
   * @param userName
   * @param password
   * @param locales
   * @return clientRequest for the given label with a session logged in as the
   *         given user.
   */
  protected static ClientRequestIF getRequest(ClientSession clientSession, String userName, String password, Locale[] locales)
  {
    ClientRequestIF clientRequest = null;

    ConnectionLabel.Type type = clientSession.getConnectionLabel().getType();
    if (type.equals(ConnectionLabel.Type.JAVA))
    {
      // we must use reflection here so that the runwaysdk client jar can
      // be compiled without the JavaClientRequest -> JavaController dependency.
      try
      {
        Class<?> clazz = LoaderDecorator.load(JavaClientRequestInfo.CLASS);

        Constructor<?> constructor = clazz.getConstructor(ClientSession.class, String.class, String.class, Locale[].class);
        Object object = constructor.newInstance(clientSession, userName,password, locales);

        return (ClientRequestIF) object;
      }
      catch (InvocationTargetException e)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te instanceof RuntimeException)
        {
          throw (RuntimeException) te;
        }
        else
        {
          throw new ClientRequestException(e);
        }
      }
      catch (Exception e)
      {
        throw new ClientRequestException(e);
      }
    }
    else if (type.equals(ConnectionLabel.Type.RMI))
    {
      clientRequest = new RMIClientRequest(clientSession, userName, password, locales);
    }
    else if (type.equals(ConnectionLabel.Type.WEB_SERVICE))
    {
      clientRequest = new WebServiceClientRequest(clientSession, userName, password, locales);
    }

    return clientRequest;
  }

  protected abstract InputStream getFileFromServer(String fileId);

  public InputStream getFile(String fileId)
  {
    if (!ClientProperties.getEnableFileCache())
    {
      return this.getFileFromServer(fileId);
    }

    // Get the web file information
    MutableDTO fileDTO = this.get(fileId);

    FileCache cache = FileCache.instance();
    String dir = fileDTO.getValue(WebFileInfo.FILE_PATH);
    String fileName = fileDTO.getValue(WebFileInfo.FILE_NAME);

    try
    {
      return cache.getFile(dir, fileName);
    }
    catch (IOException e)
    {
      // The file is not cached or an error occured when retreving the file from
      // the cache.

      // Get it directly from the server and try to store it in the cache
      try
      {
        InputStream stream = this.getFileFromServer(fileId);
        cache.setFile(dir, fileName, stream);

        return cache.getFile(dir, fileName);
      }
      catch (IOException e2)
      {
        // Storing it in the cache failed: try to return a direct stream from
        // the server
        return this.getFileFromServer(fileId);
      }
    }
  }

  protected abstract InputStream getSecureFileFromServer(String fileId);

  public InputStream getSecureFile(String fileId)
  {
    if (!ClientProperties.getEnableFileCache())
    {
      return this.getSecureFileFromServer(fileId);
    }

    // Get the vault file information
    MutableDTO fileDTO = this.get(fileId);

    FileCache cache = FileCache.instance();
    String dir = fileDTO.getValue(VaultFileInfo.VAULT_FILE_PATH);
    String fileName = fileDTO.getValue(VaultFileInfo.VAULT_FILE_NAME);

    try
    {
      return cache.getFile(dir, fileName);
    }
    catch (IOException e)
    {
      // The file is not cached or an error occured when retreving the file from
      // the cache.

      try
      {
        // Get it directly from the server and try to store it in the cache
        InputStream stream = this.getSecureFileFromServer(fileDTO.getId());
        cache.setFile(dir, fileName, stream);

        return cache.getFile(dir, fileName);
      }
      catch (IOException e2)
      {
        // Storing it in the cache failed: try to return a direct stream from
        // the server
        return this.getSecureFileFromServer(fileDTO.getId());
      }
    }
  }

  /**
   * Converts the given generic messages into typesafe ones. <b>precondition:</b>Message
   * lists have been cleared.<br/>
   *
   * @param clientRequest
   * @param messageExceptionDTO
   */
  public static void setMessagesConvertToTypeSafe(ClientRequest clientRequest, MessageExceptionDTO messageExceptionDTO)
  {
    clientRequest.setMessagesConvertToTypeSafe(messageExceptionDTO);
  }

  /**
   * Sets the type safe messages onto the given clientRequest. <b>precondition:</b>Message
   * lists have been cleared.<br/>
   *
   * @param clientRequest
   * @param messageExceptionDTO
   */
  public static void setMessages(ClientRequest clientRequest, MessageExceptionDTO messageExceptionDTO)
  {
    clientRequest.setMessages(messageExceptionDTO);
  }

  /**
   * Adds the given <code>AttributeNotification</code> to the given
   * clientRequest. <b>precondition:</b>Message lists have been cleared.<br/>
   *
   * @param clientRequest
   * @param messageExceptionDTO
   */
  public static void addAttributeNotification(ClientRequest clientRequest, AttributeNotificationDTO attributeNotification)
  {
    clientRequest.addAttributeNotification(attributeNotification);
  }

  public static void addProblems(ClientRequest clientRequest, List<ProblemDTO> problems)
  {
    clientRequest.addProblems(problems);
  }

  /**
   * Clears all messages on the clientRequest object.
   *
   * @param clientRequest
   */
  public static void clearNotifications(ClientRequest clientRequest)
  {
    clientRequest.clearNotifications();
  }

  protected abstract InputStream getSecureFileFromServer(String attributeName, String type, String fileId);

  public InputStream getSecureFile(String attributeName, String type, String fileId)
  {
    if (!ClientProperties.getEnableFileCache())
    {
      return this.getSecureFileFromServer(attributeName, type, fileId);
    }

    FileCache cache = FileCache.instance();

    BusinessDTO fileDTO = this.getVaultFileDTO(type, attributeName, fileId);

    String dir = fileDTO.getValue(VaultFileInfo.VAULT_FILE_PATH);
    String fileName = fileDTO.getValue(VaultFileInfo.VAULT_FILE_NAME);

    try
    {
      return cache.getFile(dir, fileName);
    }
    catch (IOException e)
    {
      // The file is not cached or an error occured when retreving the file from
      // the cache.

      try
      {
        // Get it directly from the server and try to store it in the cache
        InputStream stream = this.getSecureFileFromServer(attributeName, type, fileId);
        cache.setFile(dir, fileName, stream);

        return cache.getFile(dir, fileName);
      }
      catch (IOException e2)
      {
        // Storing it in the cache failed: try to return a direct stream from
        // the server
        return this.getSecureFileFromServer(attributeName, type, fileId);
      }
    }
  }
}
