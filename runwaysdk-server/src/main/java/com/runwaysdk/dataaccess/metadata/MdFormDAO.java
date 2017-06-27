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

import java.sql.Connection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.constants.MdFormInfo;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;

public abstract class MdFormDAO extends MdTypeDAO implements MdFormDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -4953956280033922009L;

  public MdFormDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  public MdFormDAO()
  {
    super();
  }

  @Override
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return this.createMdFormCommand();
  }

  @Override
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    return this.createMdFormCommand();
  }

  @Override
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return this.createMdFormCommand();
  }

  @Override
  public boolean javaArtifactsModifiedOnObject()
  {
    return false;
  }

  @Override
  public void writeFileArtifactsToDatabaseAndObjects(Connection conn)
  {
    // NOOP
  }

  @Override
  public void writeJavaToFile()
  {
    // NOOP
  }

  @Override
  public List<GeneratorIF> getGenerators()
  {
    return new LinkedList<GeneratorIF>(); // NOOP
  }

  @Override
  public MdClassDAOIF getFormMdClass()
  {
    return (MdClassDAOIF) ( (AttributeReferenceIF) this.getAttribute(MdFormInfo.FORM_MD_CLASS) ).dereference();
  }

  @Override
  public String apply()
  {
    this.setKey(this.definesType());

    return super.apply();
  }

  /**
   * Deletes this form and all MdField children.
   */
  @Override
  public void delete(boolean businessContext)
  {
    List<? extends MdFieldDAOIF> fields = this.getAllMdFieldsForDelete();

    for (MdFieldDAOIF field : fields)
    {
      List<FieldConditionDAOIF> conditions = field.getConditions();

      for (FieldConditionDAOIF condition : conditions)
      {
        condition.getBusinessDAO().delete();
      }
    }

    fields = this.getAllMdFieldsForDelete();

    for (MdFieldDAOIF field : fields)
    {
      // IMPORTANT: Deleting one of the other fields may modify this field if
      // there is a condition reference. At that point the field reference
      // becomes stale. As such before deleting a field we must retrieve a new
      // instance to ensure that the field is up to date.
      field.getBusinessDAO().delete(businessContext, false);
    }

    super.delete(businessContext);
  }

  @Override
  public String getFormName()
  {
    return this.getAttribute(MdFormInfo.FORM_NAME).getValue();
  }

  public List<? extends MdFieldDAOIF> getSortedFields()
  {
    List<? extends MdFieldDAOIF> mdFields = this.getAllMdFields();

    Collections.sort(mdFields, new Comparator<MdFieldDAOIF>()
    {
      @Override
      public int compare(MdFieldDAOIF field1, MdFieldDAOIF field2)
      {
        Integer order1 = Integer.parseInt(field1.getFieldOrder());
        Integer order2 = Integer.parseInt(field2.getFieldOrder());

        return order1.compareTo(order2);
      }
    });

    return mdFields;
  }

  /**
   * Creates a NOOP Command for this MdForm because this MdType does not manage
   * any file artifacts (as of now), hence there is nothing to generate or clean
   * up.
   * 
   * @return
   */
  private Command createMdFormCommand()
  {
    return new Command()
    {

      @Override
      public void doFinally()
      {
      }

      @Override
      public void doIt()
      {
      }

      @Override
      public String doItString()
      {
        return "Executing MdForm NOOP doIt() command.";
      }

      @Override
      public boolean isUndoable()
      {
        return false;
      }

      @Override
      public void undoIt()
      {
      }

      @Override
      public String undoItString()
      {
        return "Executing MdForm NOOP undoIt() command.";
      }
    };
  }
}
