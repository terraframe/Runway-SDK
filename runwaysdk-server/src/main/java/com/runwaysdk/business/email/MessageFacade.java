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
package com.runwaysdk.business.email;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

/**
 * Class to send either a single message or a batch of messages.
 *
 */
public class MessageFacade
{
  // email info
  private static final String smtpHost  = EmailProperties.getSmtpHost();

  private static final String loginUser = EmailProperties.getLoginUser();

  private static final String loginPass = EmailProperties.getLoginPass();

  public static void sendSingleMessage(Message message)
  {    
    SingleEmailDispatch dispatch = new SingleEmailDispatch(message);
    new Thread(dispatch).start();
  }

  /**
   * Sends an email.
   * 
   * @param email
   */
  public static void sendSingleEmail(Email email)
  {
    sendSingleMessage(email.getMessage());
  }
  
  /**
   * Sends a batch of emails.
   * 
   * @param emails
   */
  public static void sendBatchEmail(List<Email> emails)
  {
    List<Message> messages = new LinkedList<Message>();
    
    for(Email email : emails)
    {
      messages.add(email.getMessage());
    }
    
    sendBatchMessage(messages);
  }
  
  public static void sendBatchMessage(List<Message> messages)
  {
    BatchEmailDispatch dispatch = new BatchEmailDispatch(messages);
    new Thread(dispatch).start();
  }
  
  /**
   * Returns the default, shared mail session.
   * 
   * @return mail session
   */
  public synchronized static Session getDefaultSession()
  {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props);
    return session;
  }
  

  
  /**
   * Runnable class that when run, sends a single email.
   */
  private static class SingleEmailDispatch implements Runnable
  {
    private Message message;
    
    private SingleEmailDispatch(Message message)
    {
     this.message = message; 
    }
    
    public void run()
    {
      // send the email
      Transport transport = null;
      Address[] addresses = null;
      try
      {
        addresses = message.getAllRecipients();
        
        try
        {
          transport = getDefaultSession().getTransport(EmailProperties.PROTOCOL);
          transport.connect(smtpHost, EmailProperties.PORT, loginUser, loginPass);

          transport.sendMessage(message, message.getAllRecipients());
        }
        finally
        {
          if (transport != null && transport.isConnected())
          {
            transport.close();
          }
        }
      }
      catch (MessagingException e)
      {
        String to = addresses != null ? InternetAddress.toString(addresses) : "";
        
        String error = "An email could not be sent to [" + to + "]";
        throw new EmailException(error, e);
      }
    }
  }
  
  /**
   * Runnable class that when run, sends a batch of emails.
   */
  private static class BatchEmailDispatch implements Runnable
  {
    private List<Message> messages;
    
    private BatchEmailDispatch(List<Message> messages)
    {
      this.messages = messages;
    }
    
    public void run()
    {
      if (messages.size() > 0)
      {
        // send the email
        Transport transport = null;
        try
        {
          try
          {
            transport = getDefaultSession().getTransport(EmailProperties.PROTOCOL);
            transport.connect(smtpHost, EmailProperties.PORT, loginUser, loginPass);

            // send each email while using the same connection
            for (Message message : messages)
            {
              transport.sendMessage(message, message.getAllRecipients());
            }
          }
          finally
          {
            if (transport != null && transport.isConnected())
            {
              transport.close();
            }
          }
        }
        catch (MessagingException e)
        {
          String error = "An email batch could not be sent.";
          throw new EmailException(error, e);
        }
      }
    }
  }
}
