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
package com.runwaysdk.form.web.metadata;

import com.runwaysdk.system.metadata.FieldConditionDTO;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdWebFieldDTO;

/**
 * Creates
 * 
 * @author William Rose
 * 
 */
public class ConditionMdBuilders
{
  /**
   * MdBuilder class to construct a WebField from an MdWebFieldDTO.
   * 
   */
  public static abstract class ConditionMdBuilder
  {
    public abstract ConditionMd create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO fieldCondition);

    /**
     * Sets the properties from the source FieldConditionDTO to the ConditionMd
     * destination object..
     * 
     * @param mdField
     * @param conditionMd
     * @param fieldCondition
     */
    protected void init(MdWebFieldDTO mdField, ConditionMd conditionMd, FieldConditionDTO fieldCondition)
    {
      conditionMd.setOid(fieldCondition.getOid());
      conditionMd.setReferencingMdField(mdField.getOid());
      conditionMd.setReferencingMdForm(mdField.getDefiningMdFormId());
    }
  }

  public static abstract class BasicConditionMdBuilder extends ConditionMdBuilder
  {
    /**
     * Sets the properties from the source FieldConditionDTO to the ConditionMd
     * destination object..
     * 
     * @param mdField
     * @param conditionMd
     * @param fieldCondition
     */
    
    protected void init(MdWebFieldDTO mdField, ConditionMd conditionMd, FieldConditionDTO fieldCondition)
    {
      super.init(mdField, conditionMd, fieldCondition);
    }
  }

  public static class CharacterConditionMdBuilder extends BasicConditionMdBuilder
  {
    @Override
    protected void init(MdWebFieldDTO mdField, ConditionMd md, FieldConditionDTO fieldCondition)
    {
      super.init(mdField, md, fieldCondition);
    }
    
    @Override
    public ConditionMd create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO fieldCondition)
    {
      CharacterConditionMd cMd = new CharacterConditionMd();
      this.init(mdField, cMd, fieldCondition);
      return cMd;
    }
  }
  
  public static class DoubleConditionMdBuilder extends BasicConditionMdBuilder
  {
    @Override
    public ConditionMd create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO fieldCondition)
    {
      DoubleConditionMd cMd = new DoubleConditionMd();
      this.init(mdField, cMd, fieldCondition);     
      return cMd;
    }
  }

  public static class LongConditionMdBuilder extends BasicConditionMdBuilder
  {
    @Override
    public ConditionMd create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO fieldCondition)
    {
      LongConditionMd cMd = new LongConditionMd();
      this.init(mdField, cMd, fieldCondition);
      return cMd;
    }
  }

  public static class DateConditionMdBuilder extends BasicConditionMdBuilder
  {
    @Override
    public ConditionMd create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO fieldCondition)
    {
      DateConditionMd cMd = new DateConditionMd();
      this.init(mdField, cMd, fieldCondition);
      return cMd;
    }
  }
  
  public abstract static class CompositeFieldConditionMdBuilder extends ConditionMdBuilder
  {

  }
  
  public static class AndFieldConditionMdBuilder extends CompositeFieldConditionMdBuilder
  {
    /**
     * Sets the properties from the source FieldConditionDTO to the ConditionMd
     * destination object..
     * 
     * @param mdField
     * @param conditionMd
     * @param fieldCondition
     */
    
    protected void init(MdWebFieldDTO mdField, ConditionMd conditionMd, FieldConditionDTO fieldCondition)
    {
      super.init(mdField, conditionMd, fieldCondition);
    }

    @Override
    public ConditionMd create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO fieldCondition)
    {
      AndFieldConditionMd cMd = new AndFieldConditionMd();
      
      this.init(mdField, cMd, fieldCondition);
      return cMd;
    }
  }
}
