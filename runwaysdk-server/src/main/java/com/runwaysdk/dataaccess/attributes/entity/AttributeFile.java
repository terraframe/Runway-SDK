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
package com.runwaysdk.dataaccess.attributes.entity;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.VaultFileInfo;
import com.runwaysdk.dataaccess.AttributeFileIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.metadata.DeleteContext;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.vault.VaultFileDAO;
import com.runwaysdk.vault.VaultFileDAOIF;

public class AttributeFile extends Attribute implements AttributeFileIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -7259293404064780436L;

  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingEntityType</code>.
   * 
   * @param name The name of this character attribute.
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType The class that defines this attribute.
   */
  protected AttributeFile(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * Inherited constrcutor, sets <code>name</code>, <code>definingEntityType</code>, and
   * <code>value</code>.
   * 
   * @param name The name of this character attribute.
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType The type that defines this attribute.
   * @param value The value of this character. "<code>true</code>" or "<code>false</code>"
   */
  protected AttributeFile(String name, String mdAttributeKey, String definingEntityType, String value)
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
   * @return MdAttributeFileIF that defines the this attribute
   */
  public MdAttributeFileDAOIF getMdAttribute()
  {
    return (MdAttributeFileDAOIF)super.getMdAttribute();
  }
  
  /**
   * Returns the concrete attribute metadata that defines this attribute.  If this is defined 
   * by aa concrete attribute, this object is returned.  If it is a virtual attribute, then the 
   * concrete attribute it references is returned.
   * 
   * @return MdAttributeFileDAOIF that defines the this attribute
   */
  public MdAttributeFileDAOIF getMdAttributeConcrete()
  {
    return this.getMdAttribute();
  }
  
  /**
   * 
   * @param mdAttribute the defining Metadata object of the class that contains this
   *          Attribute
   * @return boolean value representing the validity of the input
   */
  protected void validate(String valueToValidate, MdAttributeDAOIF mdAttribute)
  {
    super.validate(valueToValidate, mdAttribute);

    MdAttributeFileDAOIF mdAttributeFile = (MdAttributeFileDAOIF) mdAttribute;
    
    // if it is not required to have a reference and none is provided, then
    // there is no need to check further.
    if (!mdAttributeFile.isRequired() && valueToValidate.trim().equals(""))
    {
      return;
    }

    // Make sure it is a valid VaultFile reference.   
    MdBusinessDAOIF refMdClass = MdBusinessDAO.getMdBusinessDAO(VaultFileInfo.CLASS);
    
    QueryFactory qFactory = new QueryFactory();
    BusinessDAOQuery doQ = qFactory.businessDAOQuery(refMdClass.definesType());
    doQ.WHERE(doQ.aUUID(EntityInfo.OID).EQ(valueToValidate));
      
    if (doQ.getCount() == 0)
    {
      String errMsg = "Attribute [" + mdAttributeFile.definesAttribute() + "] on type ["
          + mdAttributeFile.definedByClass().definesType() + "] does not reference a valid ["
          + refMdClass.definesType() + "]";
        
      throw new InvalidReferenceException(errMsg, mdAttributeFile);   
    }

  }
  
  /**
   * 
   * @return
   */
  public VaultFileDAOIF dereference()
  {
    MdAttributeFileDAOIF mdAttribute = (MdAttributeFileDAOIF) getMdAttribute();

    if (this.getValue().trim().equals(""))
    {
      // do not dereference the reference (as you normally would), as it produces an infinite loop within this method
      MdBusinessDAOIF refMdClass = MdBusinessDAO.getMdBusinessDAO(VaultFileInfo.CLASS);

      String errMsg = "Attribute [" + mdAttribute.definesAttribute() + "] on type ["
          + mdAttribute.definedByClass().definesType() + "] does not reference a valid ["
          + refMdClass.definesType() + "]";
      
      throw new InvalidReferenceException(errMsg, mdAttribute);  
    }
    
    // The class of the referenced object cannot be derived from the metadata.  The runtime class of the object
    // may be a sublcass of the class specified in the metadata.
    return VaultFileDAO.get(this.getValue());
  }
  
  /**
   * Removes the VaultFile object this AttributeFile references.
   */
  public void removeReferences(EntityDAO entityDAO, DeleteContext context)
  {
    super.removeReferences(entityDAO, context);
    
    // remove the file
    if(!this.getValue().trim().equals(""))
    {
      VaultFileDAO.get(this.getValue()).getBusinessDAO().delete();
    }
  }

  
  /**
   * Returns a deep clone of this attribute.
   * 
   * <br/><b>Precondition: </b> true <br/><b>Postcondition: </b> true
   * 
   * @return a deep clone of this Attribute
   */
  public AttributeFile attributeClone()
  {
    return new AttributeFile(this.getName(), this.mdAttributeKey, this.getDefiningClassType(), this.getRawValue());
  }

}
