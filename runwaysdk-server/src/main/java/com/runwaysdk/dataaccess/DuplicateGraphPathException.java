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
package com.runwaysdk.dataaccess;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.Business;

public class DuplicateGraphPathException extends RelationshipConstraintException
{
  /**
   *
   */
  private static final long serialVersionUID = -3387488031990006874L;

  /**
   * Type of the relationship.
   */
  private MdRelationshipDAOIF mdRelationshipIF;

  /**
   * Id of the parent object in the relationship.
   */
  private String parentOid;

  /**
   * Id of the child object in the relationship.
   */
  private String childOid;

  /**
   * Thrown when an attempt is made to create a graph relationship between a parent and a child that already
   * has a relationship defined between those two objects.
   * Constructs a new DuplicateGraphPathException with the specified
   * developer message.
   *
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   */
  public DuplicateGraphPathException(String devMessage)
  {
    super(devMessage);
  }

  /**
   * Thrown when an attempt is made to create a graph relationship between a parent and a child that already
   * has a relationship defined between those two objects.
   * Constructs a new DuplicateGraphPathException with the specified
   * developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this RelationshipConstraintException's detail
   * message.
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
   */
  public DuplicateGraphPathException(String devMessage, Throwable cause)
  {
    super(devMessage, cause);
  }

  /**
   * Thrown when an attempt is made to create a graph relationship between a parent and a child that already
   * has a relationship defined between those two objects.
   * Constructs a new DuplicateGraphPathException with the specified cause
   * and a developer message taken from the cause. This constructor is useful if
   * the RelationshipConstraintException is a wrapper for another throwable.
   *
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   */
  public DuplicateGraphPathException(Throwable cause)
  {
    super(cause);
  }


  /**
   *
   * @param mdRelationshipIF
   * @param parentOid
   * @param childOid
   */
  public void init(MdRelationshipDAOIF mdRelationshipIF, String parentOid, String childOid)
  {
    this.mdRelationshipIF  = mdRelationshipIF;
    this.parentOid          = parentOid;
    this.childOid           = childOid;
  }



  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   *
   */
  public String getLocalizedMessage()
  {
    if (this.mdRelationshipIF != null &&
        this.parentOid         != null &&
        this.childOid          != null)
    {
      Business parentBusiness = Business.get(this.parentOid);
      String childRelLabel = this.mdRelationshipIF.getChildDisplayLabel(this.getLocale());
      Business childBusiness = Business.get(this.childOid);

      return  ServerExceptionMessageLocalizer.duplicateGraphPathException(
          this.getLocale(), parentBusiness.toString(), childRelLabel, childBusiness.toString());
    }
    else
    {
      return super.getLocalizedMessage();
    }
  }

}
