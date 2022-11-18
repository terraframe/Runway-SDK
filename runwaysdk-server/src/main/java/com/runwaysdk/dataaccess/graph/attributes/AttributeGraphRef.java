/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.dataaccess.graph.attributes;

import java.io.Serializable;
import java.util.Date;

import com.runwaysdk.AttributeUUIDParseException;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeGraphRefDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;
import com.runwaysdk.session.Session;

public class AttributeGraphRef extends Attribute
{
  public static class ID implements Serializable
  {
    private static final long serialVersionUID = -1006406609084207407L;

    private String            oid;

    private Object            rid;

    public ID(String oid, Object rid)
    {
      super();
      this.oid = oid;
      this.rid = rid;
    }

    public String getOid()
    {
      return oid;
    }

    public Object getRid()
    {
      return rid;
    }

  }

  /**
   * 
   */
  private static final long serialVersionUID = 7368938382761174978L;

  private ID                id;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeGraphRef(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeGraphRef(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, String value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeGraphRefDAOIF} that defines the this attribute
   */
  public MdAttributeGraphRefDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeGraphRefDAOIF) super.getMdAttributeConcrete();
  }

  /**
   * 
   * @return
   */
  public ID getRID()
  {
    if (this.id == null && this.value != null)
    {
      VertexObjectDAOIF vertex = dereference(this.getValue());

      if (vertex != null)
      {
        this.id = new ID(vertex.getOid(), vertex.getRID());
      }
    }

    return this.id;
  }

  public ID getId()
  {
    return id;
  }

  public void setId(ID id)
  {
    this.id = id;
  }

  public VertexObjectDAOIF dereference(String referenceId)
  {
    MdAttributeGraphRefDAOIF mdAttribute = getMdAttributeConcrete();
    MdVertexDAOIF mdVertex = (MdVertexDAOIF) mdAttribute.getReferenceMdVertexDAOIF();

    if (referenceId.trim().equals(""))
    {
      return null;
    }
    else
    {
      return VertexObjectDAO.get(mdVertex, referenceId);
    }
  }

  /**
   * @see Attribute#validate(Object)
   *
   */
  @Override
  public void validate(Object valueToValidate)
  {
    if (valueToValidate != null)
    {
      MdAttributeGraphRefDAOIF mdAttributeIF = this.getMdAttributeConcrete();

      // First verify that the object is of the correct type.
      if (! ( ( valueToValidate instanceof String ) || ( valueToValidate instanceof ID ) ))
      {
        String devMessage = "Value is not a " + VertexObjectDAO.class.getName();
        throw new AttributeUUIDParseException(devMessage, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), valueToValidate.getClass().getName());
      }
    }

    super.validate(valueToValidate);
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof VertexObjectDAOIF)
    {
      VertexObjectDAOIF vertex = (VertexObjectDAOIF) value;

      super.setValue(vertex.getOid());

      this.id = new ID(vertex.getOid(), vertex.getRID());
    }
    else if (value instanceof VertexObject)
    {
      VertexObject vertex = (VertexObject) value;

      super.setValue(vertex.getOid());

      this.id = new ID(vertex.getOid(), vertex.getRID());
    }
    else if (value instanceof ID)
    {
      ID id = (ID) value;

      super.setValue(id.oid);

      this.id = id;
    }
    else
    {
      super.setValue(value);

      this.id = null;
    }
  }

  @Override
  public void setValue(Object value, Date startDate, Date endDate)
  {
    if (value instanceof VertexObjectDAOIF)
    {
      VertexObjectDAOIF vertex = (VertexObjectDAOIF) value;

      ID sId = new ID(vertex.getOid(), vertex.getRID());

      super.setValue(sId, startDate, endDate);
    }
    else if (value instanceof VertexObject)
    {
      VertexObject vertex = (VertexObject) value;

      ID sId = new ID(vertex.getOid(), vertex.getRID());

      super.setValue(sId, startDate, endDate);
    }
    else if (value instanceof ID)
    {
      ID id = (ID) value;

      super.setValue(id, startDate, endDate);
    }
    else
    {
      super.setValue(value, startDate, endDate);
    }
  }

  @Override
  public Object getObjectValue(Date date)
  {
    Object objectValue = super.getObjectValue(date);

    if (objectValue instanceof ID)
    {
      ID id = (ID) objectValue;

      return id.oid;
    }

    return objectValue;
  }

  @Override
  public ValueOverTimeCollection getValuesOverTime()
  {
    ValueOverTimeCollection collection = new ValueOverTimeCollection();

    for (ValueOverTime vot : this.valuesOverTime)
    {
      Object objectValue = vot.getValue();

      if (objectValue instanceof ID)
      {
        ID id = (ID) objectValue;

        objectValue = id.oid;
      }

      collection.add(new ValueOverTime(vot.getOid(), vot.getStartDate(), vot.getEndDate(), objectValue));
    }

    return collection;
  }

}
