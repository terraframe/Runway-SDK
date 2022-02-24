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
package com.runwaysdk;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.WarningDTO;

public class MessageExceptionDTO extends RuntimeException implements RunwayExceptionIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 3221228141764108867L;

  private List<MessageDTO>     messageDTOList;

  private List<WarningDTO>     warningDTOList;

  private List<InformationDTO> informationDTOList;
  
  private Object               returnObject;

  /**
   * Constructs a new MessageExceptionDTO with the specified localized message from the server. 
   * 
   * @param localizedMessage
   *          localizedMessage end user error message.
   * @param returnObject;
   * @param messageDTOList;
   * @param warningDTOList;
   * @param informationDTOList;
   */
  public MessageExceptionDTO(Object returnObject, 
      List<MessageDTO> messageDTOList, List<WarningDTO> warningDTOList, List<InformationDTO> informationDTOList)
  {
    super("Container exception for warnings.");
    this.returnObject       = returnObject;
    this.messageDTOList     = messageDTOList;
    this.warningDTOList     = warningDTOList;
    this.informationDTOList = informationDTOList;
  }

  public Object getReturnObject()
  {
    return returnObject;
  }
  
  public List<MessageDTO> getMessages()
  {
    return this.messageDTOList;
  }

  public List<WarningDTO> getWarnings()
  {
    return this.warningDTOList;
  }

  public List<InformationDTO> getInformation()
  {
    return this.informationDTOList;
  }

  /**
   * Returns a List of localized messages from the contained
   * MessageDTO objects. This has the same result as iterating
   * through the results of a MessageExceptionDTO.getMessages() 
   * call and putting the result of each MessageDTO.getMessages()
   * call into a List. 
   * 
   * @return A list of all localized problem messages.
   */
  public List<String> getMessageStrings()
  {
    List<String> messages = new LinkedList<String>();
    
    for(MessageDTO messageDTO : getMessages())
    {
      messages.add(messageDTO.getMessage());
    }
    
    return messages;
  }
}
