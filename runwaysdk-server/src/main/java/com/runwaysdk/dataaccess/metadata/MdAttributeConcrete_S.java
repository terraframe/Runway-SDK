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
package com.runwaysdk.dataaccess.metadata;

import java.util.List;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdTransientDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;

public class MdAttributeConcrete_S extends MdAttributeConcreteStrategy
{
  /**
   * 
   */
  private static final long serialVersionUID = -2282911016723560870L;

  /**
   * @param mdAttribute
   */
  public MdAttributeConcrete_S(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  } 
  
  /**
   * Returns the MdTransientIF that defines this MdAttribute.
   * 
   * @return the MdTransientIF that defines this MdAttribute.
   */
  public MdTransientDAOIF definedByClass()
  { 
    return (MdTransientDAOIF) this.getMdAttribute().definedByClass();    
  }
  
  protected void preSaveValidate()
  {
    super.preSaveValidate();
 
    if (this.getMdAttribute().isNew() && !this.getMdAttribute().isAppliedToDB())
    {
      MdTransientDAOIF definingTransient = this.definedByClass();

      List<? extends MdAttributeDAOIF> attributeList = definingTransient.definesAttributes();
      for (MdAttributeDAOIF attribute : attributeList)
      {
        if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()))
        {
          String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class ["
              + definingTransient.definesType() + "] because that class already has defined an attribute with that name.";
          throw new DuplicateAttributeDefinitionException(msg, this.getMdAttribute(), definingTransient);
        }
      }
    }
    
    if (this.getMdAttribute().isNew() && !this.appliedToDB)
    {
      // Supply a default value, as it does not make sense to have a column name for a transient attribute.
      this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.COLUMN_NAME).setValue("n_a");
    }
  }
  
  /**
   * Contains special logic for saving an attribute.
   */
  public void save()
  {
    this.validate();
    
    if (this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.NAME).isModified())
    {
      MdTransientDAOIF definingTransient = this.definedByClass();
      List<? extends MdAttributeDAOIF> attributeList = null;
      List<? extends MdTransientDAOIF> parentsList = definingTransient.getSuperClasses();
      
      // loop through parents and check for an attribute of the same name
      for (MdTransientDAOIF parent : parentsList)
      {
        attributeList = parent.definesAttributes();
        for (MdAttributeDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being compared with itself
          if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()) && !parent.definesType().equals(definingTransient.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class ["
                + definingTransient.definesType() + "] because its parent class ["+parent.definesType()+"] already defines an attribute with that name.";
            throw new DuplicateAttributeInInheritedHierarchyException(msg, this.getMdAttribute(), definingTransient, parent);
          }
        }
      }
      
      List<? extends MdTransientDAOIF> childrenList = definingTransient.getAllSubClasses();
      // loop through children and check for an attribute of the same name
      for (MdTransientDAOIF child : childrenList)
      {
        attributeList = child.definesAttributes();
        for (MdAttributeDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being compared with itself
          if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()) && !child.definesType().equals(definingTransient.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class ["
            + definingTransient.definesType() + "] because a child class ["+child.definesType()+"] already defines an attribute with that name.";
            throw new DuplicateAttributeDefinedInSubclassException(msg, this.getMdAttribute(), definingTransient, child);
          }
        }
      }      
    }
    
    if (this.getMdAttribute().isNew())
    {
      //Do not create the CLASS_ATTRIBUTE relationship on imports,
      //because the relationship is included in the import file
      if(!this.getMdAttribute().isImport() && !this.appliedToDB)
      {
        // Get the defining parent type
        MdTransientDAO parentMdTransient = this.definedByClass().getBusinessDAO();
        parentMdTransient.addAttributeConcrete(this.getMdAttribute());
      }
    }

    if (this.appliedToDB)
    {
      MdAttributeConcreteDAO mdAttributeConcreteDAO = this.getMdAttribute();
      Attribute keyAttribute = mdAttributeConcreteDAO.getAttribute(ComponentInfo.KEY);
      
      if (keyAttribute.isModified())
      {
        mdAttributeConcreteDAO.changeClassAttributeRelationshipKey();
      }
    }
  }

  /**
   * No special validation logic.
   */
  protected void validate() 
  {
    if (this.getMdAttribute().hasAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE))
    {
      Attribute attributeIF = this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);
    
      String value = attributeIF.getValue();

      if (attributeIF.isModified() && !value.trim().equals(""))
      {
        // Get the class that defines the MdAttribute class
        MdClassDAOIF mdClassIF = this.getMdAttribute().getMdClassDAO();

        Attribute spoofAttribute = 
          AttributeFactory.createAttribute(this.getMdAttribute().getKey(), this.getMdAttribute().getType(), MdAttributeConcreteInfo.DEFAULT_VALUE, 
              mdClassIF.definesType(), value);

        spoofAttribute.setContainingComponent(this.getMdAttribute());

        spoofAttribute.validate(this.getMdAttribute(), value);
      }
    }
  }
  
}
