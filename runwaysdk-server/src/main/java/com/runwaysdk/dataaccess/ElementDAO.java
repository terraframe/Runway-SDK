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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;

public abstract class ElementDAO extends EntityDAO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -3507979218575642113L;
  
  /**
   * Only to be used by transaction management on rollbacks.
   */
  private String oldSequenceNumber;

  /**
   * The default constructor, does not set any attributes
   */
  public ElementDAO()
  {
    super();
  }

  /**
   * Constructs a ElementDAO from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> classType != null
   * 
   * @param attributeMap
   * @param classType
   */
  public ElementDAO(Map<String, Attribute> attributeMap, String classType)
  {
    super(attributeMap, classType);
    this.oldSequenceNumber = this.getAttribute(ElementInfo.SEQUENCE).getValue();
  }

  /**
   * Gets the sequence number of the entity object
   * 
   * @return The sequence number of the entity
   */
  public long getSequence()
  {
    return Long.parseLong(this.getAttributeIF(ElementInfo.SEQUENCE).getValue());
  }

  /**
   * Sets appliedToDB to false if the object is new, as the database will
   * rollback any newly inserted records.
   * 
   */
  public void rollbackState()
  {
    // Restore the old sequence number
    if (oldSequenceNumber.trim().length() != 0)
    {
      this.getAttribute(ElementInfo.SEQUENCE).setValue(this.oldSequenceNumber);
    }

    super.rollbackState();
  }

  /**
   *
   */
  public String save(boolean validateRequired)
  {
    this.oldSequenceNumber = this.getAttribute(ElementInfo.SEQUENCE).getValue();

    if (this.isNew() && !this.isImport())
    {
      // Set the default owner
      this.setDefaultOwner();
      this.initSequence();
    }

    // set the date when this was updated if it is not an import save
    java.util.Date date = new java.util.Date();

    return this.save(validateRequired, date);
  }

  /**
   * This is a hook method for aspects. The date parameter will be supplied a
   * value that is the date for this transaction.
   * 
   * @param validateRequired
   * @param date
   * @return
   */
  private String save(boolean validateRequired, Date date)
  {

    String dateTimeFormat = Constants.DATETIME_FORMAT;
    String currentDateStr = new SimpleDateFormat(dateTimeFormat).format(date);

    if (!this.isImport())
    {
      this.getAttribute(ElementInfo.LAST_UPDATE_DATE).setValue(currentDateStr);
      if (this.getAttribute(ElementInfo.LAST_UPDATED_BY).getValue().trim().equals(""))
      {
        this.getAttribute(ElementInfo.LAST_UPDATED_BY).setValue(ServerConstants.SYSTEM_USER_ID);
      }
    }

    if (this.isAppliedToDB() == false)
    {
      if (!this.isImport())
      {
        this.getAttribute(ElementInfo.CREATED_BY).setValue(this.getAttributeIF(ElementInfo.LAST_UPDATED_BY).getValue());
        this.getAttribute(ElementInfo.CREATE_DATE).setValue(currentDateStr);
      }
    }

    return super.save(validateRequired);

  }

  /**
   * Precondition: isNew() == true
   * 
   */
  protected void setDefaultOwner()
  {
    if (this.hasAttribute(ElementInfo.OWNER) && !this.isImport() && this.getAttributeIF(ElementInfo.OWNER).getValue().trim().equals(""))
    {
      this.getAttribute(ElementInfo.OWNER).setValue(ServerConstants.SYSTEM_USER_ID);
    }
  }

  /**
   * Precondition: isNew() == true
   * 
   */
  protected void initSequence()
  {
    // if this is a new instance, give it a sequence number
    if (!this.isAppliedToDB() && !this.isImport())
    {
      Attribute seq = this.getAttribute(ElementInfo.SEQUENCE);
      String value = seq.getValue();

      if (value == null || value.length() == 0)
      {
        seq.setValue(Database.getNextSequenceNumber());
      }
    }
  }

  /**
   * Returns a ElementDAO of the given id in the database.
   * 
   * <br/>
   * <b>Precondition:</b> id != null <br/>
   * <b>Precondition:</b> !id.trim().equals("") <br/>
   * <b>Precondition:</b> given id represents a valid item in the database <br/>
   * <b>Postcondition:</b> return value may not be null <br/>
   * <b>Postcondition:</b> ElementDAOIF representing the item in the database of
   * the given id is returned
   * 
   * @param id
   *          element id of an item in the database
   * @return ElementDAOIF instance of the given id, of the given type
   */
  public static ElementDAOIF get(String id)
  {
    return ObjectCache.getElementDAO(id);
  }

}
