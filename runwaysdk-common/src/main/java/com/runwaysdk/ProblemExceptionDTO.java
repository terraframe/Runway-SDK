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

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.constants.ProblemExceptionDTOInfo;

public class ProblemExceptionDTO extends RuntimeException implements RunwayExceptionIF
{
  public static final String CLASS            = ProblemExceptionDTOInfo.CLASS;

  /**
   * 
   */
  private static final long  serialVersionUID = 5597485074460606469L;

  private List<? extends ProblemDTOIF> problemDTOIFList;

  /**
   * Constructs a new ProblemCollectionExceptionDTO with the specified localized
   * message from the server.
   * 
   * @param localizedMessage
   *          localizedMessage end user error message.
   * @param problemDTOIFList
   *          ;
   */
//  public ProblemExceptionDTO(String localizedMessage, List<ProblemDTOIF> problemDTOIFList)
//  {
//    super(localizedMessage);
//    this.problemDTOIFList = problemDTOIFList;
//  }
  
  public ProblemExceptionDTO(String localizedMessage, List<? extends ProblemDTOIF> problemDTOIFList)
  {
    super(localizedMessage);
    this.problemDTOIFList = problemDTOIFList;
  }

  /**
   * Returns all problems made in the previous request, including
   * <code>AttributeNotification</code>s.
   * 
   * @return all problems made in the previous request, including
   *         <code>AttributeNotification</code>s.
   */
  public List<? extends ProblemDTOIF> getProblems()
  {
    return this.problemDTOIFList;
  }

  /**
   * Returns all problems made in the previous request, but not including
   * <code>AttributeNotification</code>s.
   * 
   * @return all problems made in the previous request, but not including
   *         <code>AttributeNotification</code>s.
   */
  public List<ProblemDTOIF> getProblemsMinusAttributeNotifications()
  {
    List<ProblemDTOIF> problemsWithoutNotifications = new LinkedList<ProblemDTOIF>();

    for (ProblemDTOIF problemDTO : this.problemDTOIFList)
    {
      if (! ( problemDTO instanceof AttributeNotificationDTO ))
      {
        problemsWithoutNotifications.add(problemDTO);
      }
    }

    return problemsWithoutNotifications;
  }

  /**
   * Returns a List of localized messages from the contained ProblemDTOIF
   * objects. This has the same result as iterating through the results of a
   * ProblemExceptionDTO.getProblems() call and putting the result of each
   * ProblemDTOIF.getMessage() call into a List.
   * 
   * @return A list of all localized problem messages.
   */
  public List<String> getProblemMessages()
  {
    List<String> messages = new LinkedList<String>();

    for (ProblemDTOIF problemDTOIF : getProblems())
    {
      messages.add(problemDTOIF.getMessage());
    }

    return messages;
  }

  public String getLocalizedMessage()
  {
    String message = super.getLocalizedMessage() + ": ";
    
    List<? extends ProblemDTOIF> problems = this.getProblems();
    
    for(ProblemDTOIF problem : problems)
    {
      message += ", " + problem.getMessage();
    }
    
    return message.replaceFirst(", ", "");    
  }
}
