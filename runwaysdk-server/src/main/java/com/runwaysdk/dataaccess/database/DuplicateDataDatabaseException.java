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
package com.runwaysdk.dataaccess.database;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.ServerExceptionMessageLocalizer;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdIndexDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;

public class DuplicateDataDatabaseException extends DuplicateDataException
{
  /**
   *
   */
  private static final long serialVersionUID = -8506727072225445532L;

  /**
   * Name of the index.
   */
  private String indexName;
  
  /**
   * Name of the database table. If set, then the primary key has been violated.
   */
  private String pkTableName;

  /**
   * Only not null if the index constraint was on an individual attribute
   */
  MdAttributeConcreteDAOIF mdAttributeWithIndex = null;

  private String attributeNames         = "";
  private String attributeDisplayLabels = "";

  private String errorMessage = null;

  /**
   * Should only be called when executing MySQL, as MySQL does not yet provide the name of the contstraint.
   * Constructs a new DuplicateDataException with the specified developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this DuplicateDataException's detail message.
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
  public DuplicateDataDatabaseException(String devMessage, Throwable cause)
  {
    super(devMessage, cause);
    this.indexName = null;
    this.pkTableName = null;
  }

  /**
   * Should only be called when executing MySQL, as MySQL does not yet provide the name of the constraint.
   * Constructs a new DuplicateDataException with the specified developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this DuplicateDataException's detail message.
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
   * @param indexName
   *          Name of the index.
   */
  public DuplicateDataDatabaseException(String devMessage, Throwable cause, String indexName)
  {
    super(devMessage, cause);
    this.indexName = indexName;
    this.pkTableName = null;
  }

  /**
   * Should only be called when executing MySQL, as MySQL does not yet provide the name of the constraint.
   * Constructs a new DuplicateDataException with the specified developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this DuplicateDataException's detail message.
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
   * @param indexName
   *          Name of the index.
   * @param _pkTableName
   *          If a value is provided, then the primary key (the id) has been violated.
   *          Name of the table that the index is defined on.
   */
  public DuplicateDataDatabaseException(String devMessage, Throwable cause, String indexName, String _pkTableName)
  {
    super(devMessage, cause);
    this.indexName = indexName;
    
    // If it is a primary key constraint that has been violated, check to see if it is on a table 
    // that is defined by entity metadata. If not, then pass through the database exception.
    try
    {
      this.mdEntityIF = MdEntityDAO.getMdEntityByTableName(_pkTableName);
      this.pkTableName = _pkTableName;
    }
    catch (DataNotFoundException e)
    {
      this.pkTableName = null;
    }
    
  }
  
  /**
   * Returns true if the violation is a primary key constraint, false otherwise.
   * 
   * @return true if the violation is a primary key constraint, false otherwise.
   */
  public boolean isIdPrimaryKeyViolation()
  {
    if (this.pkTableName != null)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public String getMessage()
  {
    if (this.errorMessage != null)
    {
      return this.errorMessage;
    }
    else if (this.isIdPrimaryKeyViolation())
    {
      buildPrimaryKeyErrorMessage();
      return "Duplicate value on ["+this.mdEntityIF.definesType()+"] for attribute(s) "+this.attributeNames;
    }
    else
    {
      return super.getMessage();
    }
  }

  private void buildErrorStrings()
  {
    if (this.isIdPrimaryKeyViolation())
    {
      buildPrimaryKeyErrorMessage();
    }
    else
    { 
      buildMdIndexErrorMessage();
    }
    
    this.errorMessage = "Duplicate value on ["+this.mdEntityIF.definesType()+"] for attribute(s) "+this.attributeNames;
  }

  private void buildPrimaryKeyErrorMessage()
  {
    MdAttributeDAOIF mdAttributeDAOIF = this.mdEntityIF.getMdAttributeDAO(ComponentInfo.ID);
    
    this.attributeNames += "["+mdAttributeDAOIF.definesAttribute()+"]";
    this.attributeDisplayLabels += "["+mdAttributeDAOIF.getDisplayLabel(this.getLocale())+"]";
  }

  private void buildMdIndexErrorMessage()
  {
    List<MdAttributeConcreteDAOIF> mdAttributeIFList = null;

    this.mdAttributeWithIndex = null;

    try
    {
      if (this.indexName != null)
      {
        this.mdAttributeWithIndex = MdAttributeConcreteDAO.getMdAttributeWithIndex(this.indexName);

        if (this.mdEntityIF == null)
        {
          this.mdEntityIF = (MdEntityDAOIF)this.mdAttributeWithIndex.definedByClass();
        }

        mdAttributeIFList = new LinkedList<MdAttributeConcreteDAOIF>();
        mdAttributeIFList.add(this.mdAttributeWithIndex);
      }
    }
    catch (DataNotFoundException e) {}

    // Index constraint was not for an individual attribute, but rather for a group of attributes.
    if (this.mdAttributeWithIndex == null)
    {
      MdIndexDAOIF mdIndexIF = ObjectCache.getMdIndexDAO(this.indexName);

      if (this.mdEntityIF == null)
      {
        this.mdEntityIF = mdIndexIF.definesIndexForEntity();
      }

      mdAttributeIFList = mdIndexIF.getIndexedAttributes();
    }


    String attributeNames = "";
    String displayLabels = "";

    boolean firstIteration = true;
    for (MdAttributeConcreteDAOIF mdAttributeIF : mdAttributeIFList)
    {
      if (!firstIteration)
      {
        attributeNames += ", ";
        displayLabels += ", ";
      }
      else
      {
        firstIteration = false;
      }
      attributeNames += "["+mdAttributeIF.definesAttribute()+"]";
      displayLabels += "["+mdAttributeIF.getDisplayLabel(this.getLocale())+"]";
    }

    this.attributeNames = attributeNames;
    this.attributeDisplayLabels = displayLabels;

  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   *
   */
  public String getLocalizedMessage()
  {
    if (this.indexName == null)
    {
      return ServerExceptionMessageLocalizer.duplicateDataException(this.getLocale());
    }
    else
    {
      this.buildErrorStrings();

      String values = "";
      boolean firstIteration = true;
      for (String value : valueList)
      {
        if (!firstIteration)
        {
          values += ", ";
        }
        else
        {
          firstIteration = false;
        }
        values += "["+value+"]";
      }

      if (mdAttributeWithIndex != null)
      {
        if (valueList.size() > 0)
        {
          return ServerExceptionMessageLocalizer.duplicateDataExceptionSingle(this.getLocale(), this.mdEntityIF.getDisplayLabel(this.getLocale()), this.attributeDisplayLabels, values);
        }
        else
        {
          return ServerExceptionMessageLocalizer.duplicateDataExceptionSingleNoValue(this.getLocale(), this.mdEntityIF.getDisplayLabel(this.getLocale()), this.attributeDisplayLabels);
        }
      }
      else
      {
        if (valueList.size() > 0)
        {
          return ServerExceptionMessageLocalizer.duplicateDataExceptionMultiple(this.getLocale(), this.mdEntityIF.getDisplayLabel(this.getLocale()), this.attributeDisplayLabels, values);
        }
        else
        {
          return ServerExceptionMessageLocalizer.duplicateDataExceptionMultipleNoValues(this.getLocale(), this.mdEntityIF.getDisplayLabel(this.getLocale()), this.attributeDisplayLabels);
        }
      }
    }
  }
}
