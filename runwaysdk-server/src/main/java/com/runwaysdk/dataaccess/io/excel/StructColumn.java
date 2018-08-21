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
package com.runwaysdk.dataaccess.io.excel;

import java.lang.reflect.Method;
import java.util.Map;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.MutableWithStructs;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;

public class StructColumn extends AttributeColumn
{
  private MdAttributeStructDAOIF mdAttributeStruct;

  public StructColumn(MdAttributeStructDAOIF struct, MdAttributeDAOIF mdAttribute)
  {
    this(struct, mdAttribute, 0);
  }

  public StructColumn(MdAttributeStructDAOIF struct, MdAttributeDAOIF mdAttribute, int index)
  {
    super(mdAttribute, index);

    this.mdAttributeStruct = struct;
    this.attributeName = mdAttributeStruct.definesAttribute() + '.' + attributeName;
    this.displayLabel = mdAttributeStruct.getDisplayLabel(Session.getCurrentLocale()) + ": " + displayLabel;
  }

  /**
   * @return The name of the getter method (in the generated source) for the
   *         struct attribute this column represents
   */
  public String getStructGetter()
  {
    return CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeStruct.definesAttribute());
  }

  /**
   * @return The fully qualified type of the struct this column references
   */
  public String getStructType()
  {
    return mdAttributeStruct.javaType(false);
  }

  @Override
  public void setInstanceValue(Mutable instance, Object value) throws Exception
  {
    Class<?> businessClass = instance.getClass();
    Class<?> paramClass = LoaderDecorator.load(this.javaType());

    // Change the instance from the business object to the Struct object
    Mutable struct = (Mutable) businessClass.getMethod(this.getStructGetter()).invoke(instance);
    Class<?> structClass = LoaderDecorator.load(this.getStructType());

    String methodName = this.getSetterMethodName();
    Method method = structClass.getMethod(methodName, paramClass);
    method.invoke(struct, value);
  }

  /**
   * @param component
   * @param overrides
   * @return
   */
  public String getValue(ComponentIF component, Map<String, String> overrides)
  {
    String attributeName = this.getAttributeName();

    if (overrides.containsKey(attributeName))
    {
      return overrides.get(attributeName);
    }

    int index = attributeName.lastIndexOf(".");
    String structName = attributeName.substring(0, index);
    String structAttribute = attributeName.substring(index + 1);

    String value = ( (MutableWithStructs) component ).getStructValue(structName, structAttribute);

    if (this.getTransform() != null)
    {
      value = (String) this.getTransform().transform(value);
    }

    return value;
  }
}
