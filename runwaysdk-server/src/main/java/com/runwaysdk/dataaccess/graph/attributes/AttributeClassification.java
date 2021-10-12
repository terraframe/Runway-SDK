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
package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Date;

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;

public class AttributeClassification extends AttributeGraphRef
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String)
   */
  protected AttributeClassification(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass)
  {
    super(mdAttributeDAOIF, definingGraphClass);
  }

  /**
   * @see Attribute(MdAttributeConcreteDAOIF, String, String)
   */
  protected AttributeClassification(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingGraphClass, String value)
  {
    super(mdAttributeDAOIF, definingGraphClass, value);
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by a concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return {@link MdAttributeClassificationDAOIF} that defines the this
   *         attribute
   */
  public MdAttributeClassificationDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeClassificationDAOIF) super.getMdAttributeConcrete();
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof VertexObjectDAOIF)
    {
      this.validateRid( ( (VertexObjectDAOIF) value ).getRID());
    }
    else if (value instanceof VertexObject)
    {
      this.validateRid( ( (VertexObject) value ).getRID());
    }
    else if (value instanceof ID)
    {
      this.validateRid( ( (ID) value ).getRid());
    }

    super.setValue(value);
  }

  @Override
  public void setValue(Object value, Date startDate, Date endDate)
  {
    if (value instanceof VertexObjectDAOIF)
    {
      this.validateRid( ( (VertexObjectDAOIF) value ).getRID());
    }
    else if (value instanceof VertexObject)
    {
      this.validateRid( ( (VertexObject) value ).getRID());
    }
    else if (value instanceof ID)
    {
      this.validateRid( ( (ID) value ).getRid());
    }

    super.setValue(value, startDate, endDate);
  }

  private void validateRid(Object childRid)
  {
    MdAttributeClassificationDAOIF mdAttributeConcrete = this.getMdAttributeConcrete();

    if (mdAttributeConcrete != null)
    {
      MdClassificationDAOIF mdClassification = mdAttributeConcrete.getMdClassificationDAOIF();

      if (mdClassification != null)
      {
        VertexObjectDAOIF root = mdAttributeConcrete.getRoot();

        if (root != null)
        {
          MdEdgeDAOIF mdEdge = mdClassification.getReferenceMdEdgeDAO();

          if (!VertexObjectDAO.isChild(root.getRID(), childRid, mdEdge))
          {
            throw new ProgrammingErrorException("Value must be a child of the attribute root");
          }
        }
      }
    }
  }

}
