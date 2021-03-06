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

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.ServerExceptionMessageLocalizer;

/**
 * Thrown when a new instance would conflict with existing data.
 * 
 * @author Eric Grunzke
 */
public class DuplicateDataException extends DataAccessException
{
  /**
   * Generated by Eclipse
   */
  private static final long        serialVersionUID = -2807424816759278871L;

  /**
   * The type of the attribute being set.
   */
  protected MdClassDAOIF           mdClassDAOIF;

  /**
   * The attribute being set
   */
  protected List<MdAttributeDAOIF> MdAttributeDAOIFList;

  /**
   * The display labels of the attributes being set
   */
  protected List<String>           localizedAttrLabels;

  /**
   * The duplicate value(s)
   */
  protected List<String>           valueList;

  /**
   * Constructs a new DuplicateDataException with the specified developer
   * message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param mdClassIF
   *          The object type being set.
   * @param MdAttributeDAOIFList
   *          The attributes being set.
   * @param valueList
   *          The invalid values.
   */
  public DuplicateDataException(String devMessage, MdClassDAOIF mdClassIF, List<MdAttributeDAOIF> MdAttributeDAOIFList, List<String> valueList)
  {
    super(devMessage);
    this.mdClassDAOIF = mdClassIF;
    this.MdAttributeDAOIFList = MdAttributeDAOIFList;
    this.localizedAttrLabels = this.getLocalAttrNames(MdAttributeDAOIFList);
    this.valueList = valueList;
  }

  /**
   * Constructs a new DuplicateDataException with the specified developer
   * message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this DuplicateDataException's
   * detail message.
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
   * @param mdClassIF
   *          The object type being set.
   * @param MdAttributeDAOIFList
   *          The attributes being set.
   * @param valueList
   *          The invalid values.
   */
  public DuplicateDataException(String devMessage, Throwable cause, MdClassDAOIF mdClassIF, List<MdAttributeDAOIF> MdAttributeDAOIFList, List<String> valueList)
  {
    super(devMessage, cause);
    this.mdClassDAOIF = mdClassIF;
    this.MdAttributeDAOIFList = MdAttributeDAOIFList;
    this.localizedAttrLabels = this.getLocalAttrNames(MdAttributeDAOIFList);
    this.valueList = valueList;
  }

  /**
   * Constructs a new DuplicateDataException with the specified developer
   * message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is
   * <i>not</i> automatically incorporated in this DuplicateDataException's
   * detail message.
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
   */
  protected DuplicateDataException(String devMessage, Throwable cause)
  {
    super(devMessage, cause);
    this.valueList = new LinkedList<String>();
    this.MdAttributeDAOIFList = new LinkedList<MdAttributeDAOIF>();
  }

  /**
   * Constructs a new DuplicateDataException with the specified cause and a
   * developer message taken from the cause. This constructor is useful if the
   * DuplicateDataException is a wrapper for another throwable.
   * 
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is permitted,
   *          and indicates that the cause is nonexistent or unknown.)
   * @param mdEntityIF
   *          The object type being set.
   * @param MdAttributeDAOIFList
   *          The attributes being set.
   * @param valueList
   *          The invalid values.
   */
  public DuplicateDataException(Throwable cause, MdElementDAOIF mdEntityIF, List<MdAttributeDAOIF> MdAttributeDAOIFList, List<String> valueList)
  {
    super(cause);
    this.mdClassDAOIF = mdEntityIF;
    this.MdAttributeDAOIFList = MdAttributeDAOIFList;
    this.localizedAttrLabels = this.getLocalAttrNames(MdAttributeDAOIFList);
    this.valueList = valueList;
  }

  /**
   * Constructs a new DuplicateDataException with the specified cause and a
   * developer message taken from the cause. Do not call this constructor unless
   * you absolutely have to, it's much preferred to specify the MdAttributeDAOIF's.
   * 
   * @param devMsg
   * @param localizedAttrLabels2
   * @param mdClassDAO
   * @param valueList2
   */
  public DuplicateDataException(String devMsg, List<String> localizedAttrLabels2, MdClassDAOIF mdClassDAO, List<String> valueList2)
  {
    super(devMsg);

    this.mdClassDAOIF = mdClassDAO;
    this.MdAttributeDAOIFList = new LinkedList<MdAttributeDAOIF>();
    this.localizedAttrLabels = localizedAttrLabels2;
    this.valueList = valueList2;
  }

  /**
   * Returns the list of attribute(s) which the system was attempting to set and
   * could not.
   */
  public List<MdAttributeDAOIF> getAttributes()
  {
    return this.MdAttributeDAOIFList;
  }

  /**
   * Returns the list of values which were attempted to being set.
   * 
   * @return
   */
  public List<String> getValues()
  {
    return this.valueList;
  }

  private List<String> getLocalAttrNames(List<MdAttributeDAOIF> MdAttributeDAOIFList)
  {
    List<String> returnList = new LinkedList<String>();

    MdAttributeDAOIFList.forEach(a -> returnList.add(a.getDisplayLabel(this.getLocale())));

    return returnList;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    if (this.localizedAttrLabels.size() == 1)
    {
      String displayLabel = localizedAttrLabels.get(0);
      String value = this.valueList.get(0);
      return ServerExceptionMessageLocalizer.duplicateDataExceptionSingle(this.getLocale(), this.mdClassDAOIF.getDisplayLabel(this.getLocale()), displayLabel, value);
    }
    else
    {
      String displayLabels = "";
      String values = "";

      boolean firstIteration = true;
      for (String localizedLabel : this.localizedAttrLabels)
      {
        if (!firstIteration)
        {
          displayLabels += ", ";
        }
        else
        {
          firstIteration = false;
        }
        displayLabels += "[" + localizedLabel + "]";
      }

      firstIteration = true;
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
        values += "[" + value + "]";
      }

      return ServerExceptionMessageLocalizer.duplicateDataExceptionMultiple(this.getLocale(), this.mdClassDAOIF.getDisplayLabel(this.getLocale()), displayLabels, values);
    }
  }

  // /**
  // * Fetches the localized message template and plugs in the correct
  // parameters
  // * to set the business error message.
  // *
  // */
  // public String getLocalizedMessage()
  // {
  // if (MdAttributeDAOIFList.size() == 1)
  // {
  // String displayLabel =
  // MdAttributeDAOIFList.get(0).getDisplayLabel(this.getLocale());
  // String value = MdAttributeDAOIFList.get(0).getValue();
  // return
  // ServerExceptionMessageLocalizer.duplicateDataExceptionSingle(this.getLocale(),
  // this.mdClassDAOIF.getDisplayLabel(this.getLocale()), displayLabel, value);
  // }
  // else
  // {
  // String displayLabels = "";
  // String values = "";
  //
  // boolean firstIteration = true;
  // for (MdAttributeDAOIF MdAttributeDAOIF : MdAttributeDAOIFList)
  // {
  // if (!firstIteration)
  // {
  // displayLabels += ", ";
  // }
  // else
  // {
  // firstIteration = false;
  // }
  // displayLabels += "["+MdAttributeDAOIF.getDisplayLabel(this.getLocale())+"]";
  // }
  //
  // firstIteration = true;
  // for (String value : valueList)
  // {
  // if (!firstIteration)
  // {
  // values += ", ";
  // }
  // else
  // {
  // firstIteration = false;
  // }
  // values += "["+value+"]";
  // }
  //
  // return
  // ServerExceptionMessageLocalizer.duplicateDataExceptionMultiple(this.getLocale(),
  // this.mdClassDAOIF.getDisplayLabel(this.getLocale()), displayLabels,
  // values);
  // }
  // }
}
