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
package com.runwaysdk.form;

import java.util.Map;
import java.util.Set;

import com.runwaysdk.form.field.FieldIF;

public abstract class FormObject
{

  /**
   * Metadata about the form (based on the MdForm definition).
   */
  private FormMd               formMd;

  /**
   * Collection of all the fields defined by the MdForm.
   */
  private Map<String, FieldIF> fields;

  /**
   * The oid of this FormObject.
   */
  private String               oid;

  /**
   * The oid of the underlying data object.
   */
  private String               dataId;

  private Boolean              newInstance;

  private Boolean              disconnected;

  private Boolean              readable;

  private Boolean              writable;

  private String               type;

  /**
   * FIXME needs to take in TypeMd to describe the underlying data class
   * 
   * @param formMd
   * @param formData
   * @param fields2
   */
  protected FormObject(FormMd formMd, Map<String, FieldIF> fields)
  {
    this.formMd = formMd;
    this.newInstance = null;
    this.readable = null;
    this.writable = null;
    this.type = null;

    this.fields = fields;
  }

  public String getOid()
  {
    return this.oid;
  }

  public Boolean isNewInstance()
  {
    return this.newInstance;
  }

  protected void setNewInstance(Boolean newInstance)
  {
    this.newInstance = newInstance;
  }

  public Boolean isDisconnected()
  {
    return this.disconnected;
  }

  protected void setDisconnected(Boolean _disconnected)
  {
    this.disconnected = _disconnected;
  }

  public FormMd getMd()
  {
    return formMd;
  }

  public String getType()
  {
    return this.type;
  }

  protected void setType(String type)
  {
    this.type = type;
  }

  public String getValue(String fieldName)
  {
    return this.fields.get(fieldName).getValue();
  }

  public Map<String, ? extends FieldIF> getFieldMap()
  {
    return this.fields;
  }

  public String[] getFieldNames()
  {
    Set<String> names = this.fields.keySet();
    return names.toArray(new String[names.size()]);
  }

  public FieldIF[] getFields()
  {
    Map<String, ? extends FieldIF> map = this.getFieldMap();
    return map.values().toArray(new FieldIF[map.size()]);
  }

  public Boolean isReadable()
  {
    return this.readable;
  }

  protected void setReadable(Boolean readable)
  {
    this.readable = readable;
  }

  public Boolean isWritable()
  {
    return this.writable;
  }

  protected void setOid(String oid)
  {
    this.oid = oid;
  }

  protected void setDataId(String dataId)
  {
    this.dataId = dataId;
  }

  public String getDataId()
  {
    return dataId;
  }

  protected void setWritable(Boolean writable)
  {
    this.writable = writable;
  }
}
