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
/**
 * 
 */
package com.runwaysdk.dataaccess.attributes.entity;

import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.AttributeMultiTermIF;
import com.runwaysdk.dataaccess.MdAttributeMultiTermDAOIF;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class AttributeMultiTerm extends AttributeMultiReference implements AttributeMultiTermIF
{
  /**
   * 
   */
  private static final long serialVersionUID = 5952556989900694653L;

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntity
   * @param value
   */
  public AttributeMultiTerm(String name, String mdAttributeKey, String definingEntity, String value)
  {
    super(name, mdAttributeKey, definingEntity, value);
  }

  /**
   * 
   * @param name
   * @param mdAttributeKey
   *          key of the defining attribute metadata
   * @param definingEntityType
   * @param value
   */
  public AttributeMultiTerm(String name, String mdAttributeKey, String definingEntityType, String value, Set<String> itemIdSet)
  {
    super(name, mdAttributeKey, definingEntityType, value);
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return BusinessDAO that defines the this attribute
   */
  public MdAttributeMultiTermDAOIF getMdAttribute()
  {
    return (MdAttributeMultiTermDAOIF) super.getMdAttribute();
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeMultiTermDAOIF that defines the this attribute
   */
  public MdAttributeMultiTermDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a deep clone of this Attribute
   */
  public synchronized Attribute attributeClone()
  {
    Set<String> cloneSet = new TreeSet<String>(this.getCachedItemIdSet());

    return new AttributeMultiTerm(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), new String(this.getRawValue()), cloneSet);
  }

  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/>
   * <b>Precondition:</b> true <br/>
   * <b>Postcondition:</b> true
   * 
   * @return a deep clone of this Attribute
   */
  public synchronized Attribute attributeCopy()
  {
    Set<String> cloneSet = new TreeSet<String>(this.getCachedItemIdSet());

    return new AttributeMultiTerm(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), ServerIDGenerator.nextID(), cloneSet);
  }
}