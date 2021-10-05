/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.dataaccess.metadata;

import java.io.Serializable;
import java.util.List;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.attributes.entity.AttributeFactory;

public abstract class MdAttributeConcreteStrategy implements Serializable
{
  /**
   * 
   */
  private static final long        serialVersionUID = 5618692588284062510L;

  protected MdAttributeConcreteDAO mdAttribute;

  protected boolean                appliedToDB;

  /**
   * @param mdAttribute
   */
  public MdAttributeConcreteStrategy(MdAttributeConcreteDAO mdAttribute)
  {
    super();
    this.mdAttribute = mdAttribute;
    this.appliedToDB = false;
  }

  public void setAppliedToDB(boolean createColumn)
  {
    this.appliedToDB = createColumn;
  }

  /**
   * Returns the MdAttribute.
   * 
   * @return the MdAttribute
   */
  protected MdAttributeConcreteDAO getMdAttribute()
  {
    return this.mdAttribute;
  }

  /**
   * Returns the {@link MdClassDAOIF} that defines this
   * {@link MdAttributeConcreteDAOIF}.
   * 
   * @return the {@link MdClassDAOIF} that defines this
   *         {@link MdAttributeConcreteDAOIF}.
   */
  public MdClassDAOIF definedByClass()
  {
    return (MdClassDAOIF) this.getMdAttribute().definedByClass();
  }

  /**
   * Is called when the {@link MdAttributeConcreteDAOIF} is newly created to see
   * if an attribute with the same name is already defined for non
   * {@link MdEntityDOAIF} only.
   */
  protected void nonMdEntityCheckExistingForAttributeOnCreate()
  {
    if (this.getMdAttribute().isNew() && !this.getMdAttribute().isAppliedToDB())
    {
      MdClassDAOIF definingClass = this.definedByClass();

      List<? extends MdAttributeDAOIF> attributeList = definingClass.definesAttributes();
      for (MdAttributeDAOIF attribute : attributeList)
      {
        if (this.getMdAttribute().definesAttribute().equalsIgnoreCase(attribute.definesAttribute()))
        {
          String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class [" + definingClass.definesType() + "] because that class already has defined an attribute with that name.";
          throw new DuplicateAttributeDefinitionException(msg, this.getMdAttribute(), definingClass);
        }
      }
    }
  }

  /**
   * Common operations that are performed after the {@link this#validate()
   * method for non {@link MdEntityDAOIF}.
   */
  protected void nonMdEntityPostSaveValidationOperations()
  {
    if (this.getMdAttribute().getAttributeIF(MdAttributeConcreteInfo.NAME).isModified())
    {
      MdClassDAOIF definingClass = this.definedByClass();
      List<? extends MdAttributeDAOIF> attributeList = null;
      List<? extends MdClassDAOIF> parentsList = definingClass.getSuperClasses();

      // loop through parents and check for an attribute of the same name
      for (MdClassDAOIF parent : parentsList)
      {
        attributeList = parent.definesAttributes();
        for (MdAttributeDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being
          // compared with itself
          if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()) && !parent.definesType().equals(definingClass.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class [" + definingClass.definesType() + "] because its parent class [" + parent.definesType() + "] already defines an attribute with that name.";
            throw new DuplicateAttributeInInheritedHierarchyException(msg, this.getMdAttribute(), definingClass, parent);
          }
        }
      }

      List<? extends MdClassDAOIF> childrenList = definingClass.getAllSubClasses();
      // loop through children and check for an attribute of the same name
      for (MdClassDAOIF child : childrenList)
      {
        attributeList = child.definesAttributes();
        for (MdAttributeDAOIF attribute : attributeList)
        {
          // compare for the same named attribute, BUT make sure it's not being
          // compared with itself
          if (this.getMdAttribute().definesAttribute().equals(attribute.definesAttribute()) && !child.definesType().equals(definingClass.definesType()))
          {
            String msg = "Cannot add an attribute named [" + this.getMdAttribute().definesAttribute() + "] to class [" + definingClass.definesType() + "] because a child class [" + child.definesType() + "] already defines an attribute with that name.";
            throw new DuplicateAttributeDefinedInSubclassException(msg, this.getMdAttribute(), definingClass, child);
          }
        }
      }
    }

    if (this.getMdAttribute().isNew())
    {
      // Do not create the CLASS_ATTRIBUTE relationship on imports,
      // because the relationship is included in the import file
      if (!this.getMdAttribute().isImport() && !this.appliedToDB)
      {
        // Get the defining parent type
        MdClassDAO parentMdClass = this.definedByClass().getBusinessDAO();
        parentMdClass.addAttributeConcrete(this.getMdAttribute());
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
   * Some common validation for non {@link MdEntityDAOIF}s.
   */
  protected void nonMdEntityValidate()
  {
    if (this.getMdAttribute().hasAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE))
    {
      Attribute attributeIF = this.getMdAttribute().getAttribute(MdAttributeConcreteInfo.DEFAULT_VALUE);

      String value = attributeIF.getValue();

      if (attributeIF.isModified() && !value.trim().equals(""))
      {
        // Get the class that defines the MdAttribute class
        MdClassDAOIF mdClassIF = this.getMdAttribute().getMdClassDAO();

        Attribute spoofAttribute = AttributeFactory.createAttribute(this.getMdAttribute().getKey(), this.getMdAttribute().getType(), MdAttributeConcreteInfo.DEFAULT_VALUE, mdClassIF.definesType(), value);

        spoofAttribute.setContainingComponent(this.getMdAttribute());

        spoofAttribute.validate(this.getMdAttribute(), value);
      }
    }
  }

  /**
   * No special validation logic.
   */
  protected void preSaveValidate()
  {
  }

  /**
   * No special validation logic.
   */
  protected void validate()
  {
  }

  /**
   * No special commit logic.
   * 
   */
  public void setCommitState()
  {
  }

  /**
   * No special save logic
   */
  public void save()
  {
  }

  /**
   * No special delete logic.
   * 
   * @param removeValues
   *          Flag indicating if the delete should also remove the values from
   *          the database if they are not automatically cleaned up on column
   *          drop.
   */
  public void delete(boolean removeValues)
  {
  }

  /**
   * No special post delete logic.
   */
  public void postDelete()
  {
  }
}
