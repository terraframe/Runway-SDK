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

import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.graph.VertexObjectDAO;
import com.runwaysdk.dataaccess.graph.VertexObjectDAOIF;

public class AttributeClassification extends AttributeGraphRef
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private boolean isSkipValidation = false;

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
    this.setValue(value, isSkipValidation);
  }
  
  public void setValue(Object value, boolean skipValidation)
  {
    if (!skipValidation)
    {
      if (value instanceof VertexObjectDAOIF)
      {
        this.validateRid( ( (VertexObjectDAOIF) value ).getRID(), true);
      }
      else if (value instanceof VertexObject)
      {
        this.validateRid( ( (VertexObject) value ).getRID(), true);
      }
      else if (value instanceof ID)
      {
        this.validateRid( ( (ID) value ).getRid(), true);
      }
    }

    super.setValue(value);
  }
  
  public void setSkipValidation(boolean skip)
  {
    this.isSkipValidation = skip;
  }
  
  public boolean getSkipValidation()
  {
    return this.isSkipValidation;
  }
  
  // Our super will end up invoking the setValue method above, which ends up validating for us. No need to validate twice.
//  @Override
//  public void setValue(Object value, Date startDate, Date endDate)
//  {
//    if (value instanceof VertexObjectDAOIF)
//    {
//      this.validateRid( ( (VertexObjectDAOIF) value ).getRID(), true);
//    }
//    else if (value instanceof VertexObject)
//    {
//      this.validateRid( ( (VertexObject) value ).getRID(), true);
//    }
//    else if (value instanceof ID)
//    {
//      this.validateRid( ( (ID) value ).getRid(), true);
//    }
//    
//    super.setValue(value, startDate, endDate);
//  }

  public boolean validateRid(Object childRid, boolean throwException)
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
            if (throwException)
            {
              throw new ClassificationValidationException("Value must be a child of the attribute root");
            }
            else
            {
              return false;
            }
          }
        }
      }
    }
    
    return true;
  }

}