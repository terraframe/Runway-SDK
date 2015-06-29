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
package com.runwaysdk.dataaccess;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.business.Business;

public class RelationshipRecursionException extends RelationshipConstraintException
{
  /**
   * Tree type.
   */
  private MdRelationshipDAOIF mdTreeIF;

  /**
   * Id of the parent object.
   */
  private String              parentId;

  /**
   * Id of the child object.
   */
  private String              childId;

  /**
   * 
   */
  private static final long   serialVersionUID = 8258452022694573098L;

  /**
   * Thrown when an attempt is made to define a recursive relationship on a
   * Tree. Constructs a new RelationshipConstraintException with the specified
   * developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdTreeIF
   *          Tree type.
   * @param parentId
   *          Id of the parent object.
   * @param childId
   *          Id of the child object.
   */
  public RelationshipRecursionException(String devMessage, MdRelationshipDAOIF mdTreeIF, String parentId, String childId)
  {
    super(devMessage);
    this.mdTreeIF = mdTreeIF;
    this.parentId = parentId;
    this.childId = childId;
  }

  /**
   * Thrown when an attempt is made to define a recursive relationship on a
   * Tree. Constructs a new RelationshipConstraintException with the specified
   * developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this
   * RelationshipConstraintException's detail message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   * @param mdTreeIF
   *          Tree type.
   * @param parentId
   *          Id of the parent object.
   * @param childId
   *          Id of the child object.
   */
  public RelationshipRecursionException(String devMessage, Throwable cause, MdRelationshipDAOIF mdTreeIF, String parentId, String childId)
  {
    super(devMessage, cause);
    this.mdTreeIF = mdTreeIF;
    this.parentId = parentId;
    this.childId = childId;
  }

  /**
   * Thrown when an attempt is made to define a recursive relationship on a
   * Tree. Constructs a new RelationshipConstraintException with the specified
   * cause and a developer message taken from the cause. This constructor is
   * useful if the RelationshipConstraintException is a wrapper for another
   * throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   * @param mdTreeIF
   *          Tree type.
   * @param parentId
   *          Id of the parent object.
   * @param childId
   *          Id of the child object..
   */
  public RelationshipRecursionException(Throwable cause, MdRelationshipDAOIF mdTreeIF, String parentId, String childId)
  {
    super(cause);
    this.mdTreeIF = mdTreeIF;
    this.parentId = parentId;
    this.childId = childId;
  }

  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    String treeLabel = this.mdTreeIF.getDisplayLabel(this.getLocale());
    String parentUniqueLabel = Business.get(this.parentId).toString();
    String childUniqueLabel = Business.get(this.childId).toString();

    return ServerExceptionMessageLocalizer.relationshipRecursionException(this.getLocale(), treeLabel, parentUniqueLabel, childUniqueLabel);
  }
}
