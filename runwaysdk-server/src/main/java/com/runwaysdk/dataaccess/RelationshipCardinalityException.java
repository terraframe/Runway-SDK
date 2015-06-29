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

public class RelationshipCardinalityException extends RelationshipConstraintException
{
  /**
   * 
   */
  private static final long serialVersionUID = 7554700302175584955L;

  /**
   * Type of the object being added to the relationship.
   */
  private MdBusinessDAOIF addedMdBusinessIF;
  
  /**
   * Cardinality upper bound.
   */
  private int cardinality;
  
  /**
   * Type of the relationship.
   */
  private MdRelationshipDAOIF mdRelationshipIF;
  
  /**
   * Id of the object being added.
   */
  private String addedObjectId;
  
  /**
   * Type of the object that is having an object added to it.
   */
  private MdBusinessDAOIF otherMdBusinessIF; 
  
  /**
   * Thrown when a relationship is defined that would violate a cardinality constraint.
   * Constructs a new DuplicateGraphPathException with the specified
   * developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdBusinessIF 
   *          Type of the object being added to the relationship.
   * @param cardinality
   *          Cardinality upper bound.
   * @param mdRelationshipIF
   *          Type of the relationship.
   * @param addedObjectId
   *          Id of the object being added.
   * @param otherMdBusinessIF
   *          Type of the object that is having an object added to it.
   *          
   */
  public RelationshipCardinalityException(
      String devMessage, MdBusinessDAOIF mdBusinessIF, int cardinality, 
      MdRelationshipDAOIF mdRelationshipIF, String addedObjectId, MdBusinessDAOIF otherMdBusinessIF)
  {
    super(devMessage);
    this.addedMdBusinessIF = mdBusinessIF;
    this.cardinality = cardinality;
    this.mdRelationshipIF = mdRelationshipIF;
    this.addedObjectId = addedObjectId;
    this.otherMdBusinessIF = otherMdBusinessIF;
  }

  /**
   * Thrown when a relationship is defined that would violate a cardinality constraint.
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
   * @param mdBusinessIF 
   *          Type of the object being added to the relationship.
   * @param cardinality
   *          Cardinality upper bound.
   * @param mdRelationshipIF
   *          Type of the relationship.
   * @param addedObjectId
   *          Id of the object being added.
   * @param otherMdBusinessIF
   *          Type of the object that is having an object added to it.
   */
  public RelationshipCardinalityException(
      String devMessage, Throwable cause, MdBusinessDAOIF mdBusinessIF, int cardinality, 
      MdRelationshipDAOIF mdRelationshipIF, String addedObjectId, MdBusinessDAOIF otherMdBusinessIF)
  {
    super(devMessage, cause);
    this.addedMdBusinessIF = mdBusinessIF;
    this.mdRelationshipIF = mdRelationshipIF;
    this.addedObjectId = addedObjectId;
    this.otherMdBusinessIF = otherMdBusinessIF;
  }

  /**
   * Thrown when a relationship is defined that would violate a cardinality constraint.
   * Constructs a new DuplicateGraphPathException with the specified cause
   * and a developer message taken from the cause. This constructor is useful if
   * the RelationshipConstraintException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param mdBusinessIF 
   *          Type of the object being added to the relationship.
   * @param cardinality
   *          Cardinality upper bound.
   * @param mdRelationshipIF
   *          Type of the relationship.
   * @param addedObjectId
   *          Id of the object being added.
   * @param otherMdBusinessIF
   *          Type of the object that is having an object added to it.
   */
  public RelationshipCardinalityException(
      Throwable cause, MdBusinessDAOIF mdBusinessIF, int cardinality, 
      MdRelationshipDAOIF mdRelationshipIF, String addedObjectId, MdBusinessDAOIF otherMdBusinessIF)
  {
    super(cause);
    this.addedMdBusinessIF = mdBusinessIF;
    this.mdRelationshipIF = mdRelationshipIF;
    this.addedObjectId = addedObjectId;
    this.otherMdBusinessIF = otherMdBusinessIF;
  }

  
  /**
   * Uses the stored parameters and the given Locale to produce an error message
   * suitable for display to end users.
   * 
   */
  public String getLocalizedMessage()
  {
    Business business = Business.get(this.addedObjectId);
    
    return ServerExceptionMessageLocalizer.relationshipCardinalityException(
        this.getLocale(), this.addedMdBusinessIF.getDisplayLabel(this.getLocale()), Integer.toString(cardinality), this.mdRelationshipIF.getDisplayLabel(this.getLocale()), 
        business.toString(), this.otherMdBusinessIF.getDisplayLabel(this.getLocale()));
  }
}
