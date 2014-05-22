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
package com.runwaysdk.query;

import com.runwaysdk.ServerExceptionMessageLocalizer;

public class InvalidComparisonOperator extends QueryBuilderException
{
  /**
   * 
   */
  private static final long serialVersionUID = -1481002966420705996L;

  private String invalidOperator;
  
  private Enum selectableEnum;
  
  /**
   * Constructs a new <code>InvalidComparisonOperator</code> with the specified developer message
   * and a default business message. Leaving the default business message is
   * discouraged, as it provides no context information for end users.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param invalidOperator  invalid comparison operator.
   * @param selectableEnum  selectable for which the given operator is invalid.
   */
  public InvalidComparisonOperator(String devMessage, String invalidOperator, Enum selectableEnum)
  {
    super(devMessage);

    this.invalidOperator = invalidOperator;
    this.selectableEnum = selectableEnum;
  }

  /**
   * Constructs a new <code>InvalidComparisonOperator</code> with the specified detail message and
   * cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this <code>RunwayException</code>'s detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param invalidOperator invalid comparison operator.
   * @param selectableEnum  selectable for which the given operator is invalid.
   */
  public InvalidComparisonOperator(String devMessage, Throwable cause, String invalidOperator, Enum selectableEnum)
  {
    super(devMessage, cause);

    this.invalidOperator = invalidOperator;
    this.selectableEnum = selectableEnum;
  }

  /**
   * Constructs a new <code>InvalidComparisonOperator</code> with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * <code>RunwayException</code> is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param invalidOperator  invalid comparison operator.
   * @param selectableEnum  selectable for which the given operator is invalid.
   */
  public InvalidComparisonOperator(Throwable cause, String invalidOperator, Enum selectableEnum)
  {
    super(cause);

    this.invalidOperator = invalidOperator;
    this.selectableEnum = selectableEnum;
  }

  /**
   * Returns the invalid operator.
   * @return the invalid operator.
   */
  protected String getInvalidOperator()
  {
    return this.invalidOperator;
  }
  
  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    if (selectableEnum.equals(Enum.AGGREGATE_FUNCTION))
    {
      return ServerExceptionMessageLocalizer.invalidAggregateOperatorException(this.getLocale(), this.getInvalidOperator());
    }
    else if (selectableEnum.equals(Enum.MIN_MAX))
    {
      return ServerExceptionMessageLocalizer.invalidMinMaxOperatorException(this.getLocale(), this.getInvalidOperator());
    }
    else if (selectableEnum.equals(Enum.NUMBER))
    {
      return ServerExceptionMessageLocalizer.invalidNumberOperatorException(this.getLocale(), this.getInvalidOperator());
    }
    else if (selectableEnum.equals(Enum.STRING))
    {
      return ServerExceptionMessageLocalizer.invalidStringOperatorException(this.getLocale(), this.getInvalidOperator());
    }
    else if (selectableEnum.equals(Enum.BOOLEAN))
    {
      return ServerExceptionMessageLocalizer.invalidBooleanOperatorException(this.getLocale(), this.getInvalidOperator());
    }
    else if (selectableEnum.equals(Enum.MOMENT))
    {
      return ServerExceptionMessageLocalizer.invalidMomentOperatorException(this.getLocale(), this.getInvalidOperator());
    }
    else if (selectableEnum.equals(Enum.BLOB))
    {
      return ServerExceptionMessageLocalizer.invalidBlobOperatorException(this.getLocale(), this.getInvalidOperator());
    }   
    else if (selectableEnum.equals(Enum.STRUCT))
    {
      return ServerExceptionMessageLocalizer.invalidStructOperatorException(this.getLocale(), this.getInvalidOperator());
    } 
    else if (selectableEnum.equals(Enum.REF))
    {
      return ServerExceptionMessageLocalizer.invalidRefOperatorException(this.getLocale(), this.getInvalidOperator());
    } 
    else
    {
      return this.getMessage();
    }
  }
  
  public static enum Enum
  {
    AGGREGATE_FUNCTION,

    MIN_MAX,
    
    NUMBER,
    
    STRING,
    
    BOOLEAN,

    MOMENT,

    BLOB,
    
    REF,
    
    STRUCT;
  }
}
