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

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class that represents an email.
 */
public abstract class Email
{
  /**
   * The email recipient.
   */
  private String to;
  
  /**
   * The email sender.
   */
  private String from;
  
  /**
   * The email subject.
   */
  private String subject;
  
  /**
   * The email body.
   */
  private String body;
  
  /**
   * Constructor to set the basic email properties.
   * 
   * @param to
   * @param from
   * @param subject
   * @param body
   */
  public Email(String to, String from, String subject, String body)
  {
    this.to = to;
    this.from = from;
    this.subject = subject;
    this.body = body;
  }
  
  /**
   * Sets the mail body in the message, which differs depending on the type
   * of body (text or html). Subclasses must override this to set the mail
   * body correctly.
   * 
   * @param message
   * @param body
   */
  protected abstract void setBodyInMessage(Message message, String body) throws MessagingException;
  
  synchronized String getTo()
  {
    return to;
  }
  
  /**
   * Send the email to the recipient.
   * @throws MessagingException 
   * 
   */
  synchronized Message getMessage()
  {
    Session session = MessageFacade.getDefaultSession();
    
    Message message = new MimeMessage(session);
    
    try
    {
      // subject
      message.setSubject(subject);
      
      // date
      message.setSentDate(new java.util.Date());
  
      // to
      Address toAddress = new InternetAddress(to);
      message.setRecipient(Message.RecipientType.TO, toAddress);    
      
      // from
      Address fromAddress = new InternetAddress(from);
      message.setFrom(fromAddress);
      
      // set the body
      setBodyInMessage(message, body);
      
      return message;
    }
    catch(MessagingException e)
    {
      e.printStackTrace();
      
      String error = "An email could not be sent to ["+to+"]";
      
      EmailException ex = new EmailException(error, e);
      
      throw ex;
    }
  }
}
