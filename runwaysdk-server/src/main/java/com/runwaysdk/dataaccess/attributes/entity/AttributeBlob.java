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
package com.runwaysdk.dataaccess.attributes.entity;

import com.runwaysdk.dataaccess.AttributeBlobIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeException;
import com.runwaysdk.dataaccess.attributes.AttributeLengthByteException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.util.Base64;


public class AttributeBlob extends Attribute implements AttributeBlobIF
{
  /**
   * Generated id.
   */
  private static final long serialVersionUID = 702026100547730370L;

  /**
   * Byte array to hold the value of this attribute.
   */
  private byte[]            valueBytes       = new byte[0];

  /**
   * Constructor that does nothing because AttributeBlob uses byte[] instead of
   * Strings for values.
   *
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType
   */
  protected AttributeBlob(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * Constructor that does nothing because AttributeBlob uses byte[] instead of
   * Strings for values.
   *
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType
   * @param value
   */
  protected AttributeBlob(String name, String mdAttributeKey, String definingEntityType, String value)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }

  /**
   * Constructor to create a new AttributeBlob and set the value of
   * this.valueBytes.
   *
   * @param name
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType
   * @param bytes
   */
  protected AttributeBlob(String name, String mdAttributeKey, String definingEntityType, byte[] bytes)
  {
    super(name, mdAttributeKey, definingEntityType);
    valueBytes = bytes;
  }

  /**
   * Validates the value of this AttributeBlob. Because the data for a blob
   * cannot be accurately represented by a string value, this method will always
   * throw an exception when invoked for this attribute.
   */
  protected void validate(String valueToValidate)
  {
    String error = "The value of an attribute BLOB cannot be validated directly. The special AttributeBlob methods "
        + "must be used instead.";
    throw new ForbiddenMethodException(error);
  }

  @Override
  public Attribute attributeClone()
  {
    return new AttributeBlob(this.getName(), this.mdAttributeKey, this.getDefiningClassType(),
        new byte[0]);
  }

  /**
   * Sets the value of this AttributeBlob. Because the data for a blob cannot be
   * accurately represented by a string value, this method will always throw an
   * exception when invoked for this attribute.
   */
  public void setValue(String value)
  {
    String error = "The value of an attribute BLOB cannot be set directly. The special AttributeBlob methods "
        + "must be used instead.";
    throw new ForbiddenMethodException(error);
  }

  /**
   * Sets the state of the attribute object after a transaction has been committed/
   *
   */
  public void setCommitState()
  {
     super.setCommitState();
     this.flushBlobCache();
  }

  /**
   * Sets the value of this AttributeBlob. Because the data for a blob cannot be
   * accurately represented by a string value, this method will always throw an
   * exception when invoked for this attribute.
   */
  public void setValueAndValidate(String value)
  {
    String error = "The value of an attribute BLOB cannot be set directly. The special AttributeBlob methods "
        + "must be used instead.";
    throw new ForbiddenMethodException(error);
  }

  /**
   * Gets the value of this AttributeBlob. Because the data for a blob cannot be
   * accurately represented by a string value, this method will always return
   * just an empty string.
   */
  public String getValue()
  {
    return "";
  }

  /**
   * Flushes all of the bytes currently stored in this.valueBytes. This method
   * is mainly called after the value of this attribute has been saved to the
   * database.
   */
  public void flushBlobCache()
  {
    valueBytes = new byte[0];
  }

  /**
   * Returns the blob as an array of bytes.
   *
   * @return The byte array value of this blob attribute.
   */
  public byte[] getBlobAsBytes()
  {
    // fetch the blob from the cache if the containing component has not
    // been applied
    // to the database. Otherwise, fetch directly from the database.
    if (! this.getContainingComponent().isAppliedToDB())
    {
      return valueBytes.clone();
    }
    else
    {
      String table = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();
      String columnName = this.getMdAttribute().getColumnName();
      String id = this.getContainingComponent().getId();
      return Database.getBlobAsBytes(table, columnName, id);
    }
  }

  /**
   * Gets the value of this blob as the specified bytes. This method works the
   * same as the Blob.getBytes(long pos, int length) as specified in the JDBC
   * 3.0 API. Because of this, the first element in the bytes to write to is
   * actually element 1 (as opposed to the standard array treatment where the
   * first element is at position 0).
   *
   * @param pos
   *          The starting position. The first element is at position 1.
   * @param length
   *          The length in bytes to grab after (and including) the starting
   *          position.
   * @return byte[]
   */
  public byte[] getBlobAsBytes(long pos, int length)
  {
    // fetch the blob from the cache if the containing component has not
    // been applied
    // to the database. Otherwise, fetch directly from the database.
    if (!this.getContainingComponent().isAppliedToDB())
    {
      byte[] temp = new byte[length];
      pos = pos - 1;
      for (int i = 0; i < length; i++)
      {
        temp[i] = valueBytes[(int) pos];
        pos++;
      }
      return temp;
    }
    else
    {
      String table = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();
      String columnName = this.getMdAttribute().getColumnName();
      String id = this.getContainingComponent().getId();
      return Database.getBlobAsBytes(table, columnName, id, pos, length).clone();
    }
  }

  /**
   * Sets the value of this blob as the specified bytes.
   *
   * @param bytes
   * @return The number of bytes written.
   */
  public int setBlobAsBytes(byte[] bytes)
  {
    if (bytes == null)
    {
      return 0;
    }

    this.setModified(true);

    // if the blob's containing component has not been applied to the
    // database,
    // then set the blob in the cache. Otherwise, use the database blob.
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();
    if (!this.getContainingComponent().isAppliedToDB())
    {
      this.validate(bytes, mdAttributeIF);
      this.valueBytes = bytes;
      return bytes.length;
    }
    else
    {
      String table = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();
      String columnName = this.getMdAttribute().getColumnName();
      String id = this.getContainingComponent().getId();
      return Database.setBlobAsBytes(table, columnName, id, bytes);
    }
  }

  /**
   * The import equivalent of setValue.  Used only during importing from an xml file.
   * By default has the same behavior as setValue.  However, special logic may be added for children classes.
   * @param value The value to set the attribute
   */
  public void importValue(String value)
  {
    this.setModified(true);
    this.setBlobAsBytes(Base64.decode(value));
  }

  /**
   * Sets the value of this blob as the specified bytes. This method works the
   * same as the Blob.setBytes(long pos, byte[], int offset, int length) as
   * specified in the JDBC 3.0 API. Because of this, the first element in the
   * bytes to write to is actually element 1 (as opposed to the standard array
   * treatment where the first element is at position 0).
   *
   * @param pos
   *          The position of the blob to start at. The first element is at
   *          position 1.
   * @param bytes
   *          The bytes to be written.
   * @param offset
   *          The offset of bytes.
   * @param length
   *          The length of bytes to write
   * @return The number of bytes written.
   */
  public int setBlobAsBytes(long pos, byte[] bytes, int offset, int length)
  {
    if (bytes == null)
    {
      return 0;
    }

    this.setModified(true);

    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();

    // if the blob's containing component has not been applied to the
    // database,
    // then set the blob in the cache. Otherwise, use the database blob.
    if (! ( (EntityDAO) this.getContainingComponent() ).isAppliedToDB())
    {
      pos = pos - 1; // subtract one to use positioning like a normal
      // array
      int written = 0;

      // check to see if the bytes will run longer than the current length
      // of the blob length.
      if ( ( pos + length ) > valueBytes.length)
      {
        // get the "old" bytes up until pos
        byte[] temp = new byte[(int) ( pos + length )];
        for (int i = 0; i < pos; i++)
        {
          temp[i] = valueBytes[i];
        }

        // set the new bytes
        for (int i = 0; i < length; i++)
        {
          temp[(int) pos] = bytes[offset];
          offset++;
          pos++;
          written++;
        }
        validate(temp, mdAttributeIF);
        valueBytes = temp;
      }
      else
      {
        for (int i = 0; i < length; i++)
        {
          valueBytes[(int) pos] = bytes[offset];
          offset++;
          pos++;
          written++;
        }
        validate(valueBytes, mdAttributeIF);
      }
      return written;
    }
    else
    {
      String table = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();
      String columnName = this.getMdAttribute().getColumnName();
      String id = this.getContainingComponent().getId();
      return Database.setBlobAsBytes(table, columnName, id, pos, bytes, offset, length);
    }

  }

  /**
   * Returns the size of the blob.
   *
   * @return The length of this blob (in bytes).
   */
  public long getBlobSize()
  {
    if (! ( (EntityDAO) this.getContainingComponent() ).isAppliedToDB())
    {
      return valueBytes.length;
    }
    else
    {
      String table = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();
      String columnName = this.getMdAttribute().getColumnName();
      String id = this.getContainingComponent().getId();
      return Database.getBlobSize(table, columnName, id);
    }
  }

  /**
   * Validates the size of this blob attribute.
   *
   * @param bytes
   */
  public void validate(byte[] bytes, MdAttributeDAOIF mdAttributeIF)
  {
    EntityDAO containingEntity = (EntityDAO) this.getContainingComponent();
    if (containingEntity.isAppliedToDB())
    {
      this.validateMutable(mdAttributeIF);
    }

    if (bytes.length > MdAttributeBlobDAO.getMaxLength())
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType()
          + "] may not exceed " + MdAttributeBlobDAO.getMaxLength() + " bytes in length.";
      throw new AttributeLengthByteException(error, this, MdAttributeBlobDAO.getMaxLength());
    }
  }

  /**
   * This is used to validate the default value of the given {@link# MdAttributeIF}.
   * Runs validation tests that are common to all Attribute classes, but not the required attribute test.
   *
   * @param mdAttributeIF
   * @param valueToValidate the String value to be validated
   * @return true if the value is valid for all common tests
   * @throws AttributeException
   *    if the attribute is not valid.
   */
  public void validate(MdAttributeDAOIF mdAttributeIF, String valueToValidate)
  {
    String errMsg = "Blobs cannot have default values";
    throw new ForbiddenMethodException(errMsg);
  }

  /**
   * Validates this attribute by checking for required values.
   *
   * @param bytes
   * @param mdAttributeIF
   */
  public void validateRequired(byte[] bytes, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    // FIXME
    /*
    if (mdAttributeIF.isRequired() && (bytes == null || bytes.length == 0))
    {
      String error = "Attribute [" + getName() + "] on type [" + getDefiningClassType()
          + "] requires a value";
      EmptyValueProblem problem =
        new EmptyValueProblem(this.getContainingComponent().getProblemNotificationId(), mdAttributeIF.definedByClass(), mdAttributeIF, error, this);
      problem.throwIt();
    }
    */
  }

  /**
   * Truncates the blob to specified length. If the truncate length is greater
   * than or equal to the actual blob length, don't do anything.
   *
   * @param length
   *          The length to truncate the blob.
   */
  public void truncateBlob(long length)
  {
    MdAttributeConcreteDAOIF mdAttributeIF = this.getMdAttribute();

    if (! ( (EntityDAO) this.getContainingComponent() ).isAppliedToDB())
    {
      byte[] temp = new byte[(int) length];
      for (int i = 0; i < length; i++)
      {
        temp[i] = valueBytes[i];
      }
      validate(temp, mdAttributeIF);
      valueBytes = temp;
    }
    else
    {
      String table = ((MdEntityDAOIF)this.getMdAttribute().definedByClass()).getTableName();
      String columnName = this.getMdAttribute().getColumnName();
      String id = this.getContainingComponent().getId();
      Database.truncateBlob(table, columnName, id, length);
    }
  }
}
