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
/**
 * 
 */
package com.runwaysdk.dataaccess.attributes.value;

import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.AttributeMultiReferenceIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;

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
public class AttributeMultiReference extends Attribute implements AttributeMultiReferenceIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -5578731490483594872L;

  private Set<String>       itemIdSet;

  /**
   * Creates an attribute with the given name.
   * 
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> definingEntityType != null <br>
   * <b>Precondition: </b> !definingEntityType().equals("") <br>
   * <b>Precondition: </b> definingEntityType is the name of a class that
   * defines an attribute with this name
   * 
   * @param name
   *          name of the attribute
   * @param the
   *          value of the attribute
   * @param definingEntityType
   *          name of the class that defines this attribute from which the value
   *          came
   * @param mdAttributeIF
   *          metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset
   *          all MdAttributes that were involved in constructing this
   *          attribute.
   */
  protected AttributeMultiReference(String name, String value, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, value, definingEntityType, mdAttributeIF, entityMdAttributeIFset);

    this.itemIdSet = new TreeSet<String>();
  }

  /**
   * Initializes a local cache in this object of enumeration mappings.
   * 
   * @param idList
   */
  public void initMappingCache(Set<String> itemIds)
  {
    this.itemIdSet.addAll(itemIds);
  }

  /**
   * Returns the MdAttribute that defines the attribute from which the value
   * came.
   * 
   * @return MdAttribute that defines the attribute from which the value came.
   */
  public MdAttributeMultiReferenceDAOIF getMdAttribute()
  {
    return (MdAttributeMultiReferenceDAOIF) this.mdAttributeIF;
  }

  /**
   * Returns the concrete attribute metadata that defines this attribute. If
   * this is defined by aa concrete attribute, this object is returned. If it is
   * a virtual attribute, then the concrete attribute it references is returned.
   * 
   * @return MdAttributeMultiReferenceDAOIF that defines the this attribute
   */
  public MdAttributeMultiReferenceDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }

  /**
   *
   */
  public BusinessDAOIF[] dereference()
  {
    BusinessDAOIF[] enumerationItemDAOArray = new BusinessDAOIF[this.itemIdSet.size()];

    int loopCount = 0;
    for (String itemId : this.itemIdSet)
    {
      // structs cannot be used to define an enumeration
      enumerationItemDAOArray[loopCount] = BusinessDAO.get(itemId);
      loopCount++;
    }

    return enumerationItemDAOArray;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AttributeMultiReferenceIF#getCachedItemIdSet()
   */
  @Override
  public Set<String> getCachedItemIdSet()
  {
    return this.itemIdSet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AttributeMultiReferenceIF#getItemIdList()
   */
  @Override
  public Set<String> getItemIdList()
  {
    return this.getCachedItemIdSet();
  }

  /**
   * Builds a
   * <code>com.runwaysdk.dataaccess.attributes.tranzient.Attribute</code> for a
   * <code>TransientDAO</code> object.
   * 
   * @return <code>com.runwaysdk.dataaccess.attributes.tranzient.Attribute</code>
   *         for a <code>TransientDAO</code> object.
   */
  public com.runwaysdk.dataaccess.attributes.tranzient.Attribute buildTransientAttribute(String transientType)
  {
    com.runwaysdk.dataaccess.attributes.tranzient.AttributeMultiReference attribute = (com.runwaysdk.dataaccess.attributes.tranzient.AttributeMultiReference) com.runwaysdk.dataaccess.attributes.tranzient.AttributeFactory.createAttribute(this.mdAttributeIF.getType(), this.mdAttributeIF.getKey(), this.getName(), transientType, this.getObjectValue());

    String attrDefaultValue = this.mdAttributeIF.getAttributeInstanceDefaultValue();

    if (!attrDefaultValue.equals(""))
    {
      attribute.setDefaultValue(attrDefaultValue);
    }

    // copy over the mapped enum items.
    for (String itemId : this.itemIdSet)
    {
      attribute.addItem(itemId);
    }

    return attribute;
  }
}
