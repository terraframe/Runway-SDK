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
package com.runwaysdk.form.web.condition;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.BasicConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.form.web.metadata.AndFieldConditionMd;
import com.runwaysdk.form.web.metadata.CharacterConditionMd;
import com.runwaysdk.form.web.metadata.ConditionMdBuilders;
import com.runwaysdk.form.web.metadata.ConditionMdBuilders.BasicConditionMdBuilder;
import com.runwaysdk.form.web.metadata.ConditionMdBuilders.CompositeFieldConditionMdBuilder;
import com.runwaysdk.form.web.metadata.ConditionMdBuilders.ConditionMdBuilder;
import com.runwaysdk.form.web.metadata.DateConditionMd;
import com.runwaysdk.form.web.metadata.DoubleConditionMd;
import com.runwaysdk.form.web.metadata.LongConditionMd;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.system.EnumerationMasterDTO;
import com.runwaysdk.system.metadata.AndFieldConditionDTO;
import com.runwaysdk.system.metadata.CharacterConditionDTO;
import com.runwaysdk.system.metadata.CompositeFieldConditionDTO;
import com.runwaysdk.system.metadata.DateConditionDTO;
import com.runwaysdk.system.metadata.DoubleConditionDTO;
import com.runwaysdk.system.metadata.FieldConditionDTO;
import com.runwaysdk.system.metadata.LongConditionDTO;
import com.runwaysdk.system.metadata.MdFieldDTO;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdWebFieldDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;
import com.runwaysdk.util.IDGenerator;

public class ConditionBuilders
{
  private static Map<String, ConditionBuilder> builders;

  static
  {
    builders = new HashMap<String, ConditionBuilder>();
    builders.put(CharacterConditionDTO.CLASS, new CharacterConditionBuilder());
    builders.put(DateConditionDTO.CLASS, new DateConditionBuilder());
    builders.put(DoubleConditionDTO.CLASS, new DoubleConditionBuilder());
    builders.put(LongConditionDTO.CLASS, new LongConditionBuilder());
    builders.put(AndFieldConditionDTO.CLASS, new AndFieldConditionBuilder());
  }

  /**
   * Returns the appropriate BuilderIF for the given field type.
   * 
   * @param type
   * @return
   */
  public static ConditionBuilder getBuilder(MdFieldDTO field)
  {
    FieldConditionDTO fc = field.getFieldCondition();
    String type = fc.getType();

    if (builders.containsKey(type))
    {
      return builders.get(type);
    }
    else
    {
      String msg = "Could not find the builder for the field type [" + type + "].";
      throw new ConversionExceptionDTO(msg);
    }
  }
  
  public static ConditionBuilder getBuilder(FieldConditionDTO condition)
  {
    String type = condition.getType();
    if (builders.containsKey(type))
    {
      return builders.get(type);
    }
    else
    {
      String msg = "Could not find the builder for the field type [" + type + "].";
      throw new ConversionExceptionDTO(msg);
    }
  }

  /**
   * Builder class to construct a Condition from a FieldConditionDTO
   * 
   */
  public static abstract class ConditionBuilder
  {
    private ConditionMdBuilder mdBuilder;

    private ConditionBuilder(ConditionMdBuilder mdBuilder)
    {
      super();
      this.mdBuilder = mdBuilder;
    }
    
    protected ConditionMdBuilder getMdBuilder()
    {
      return this.mdBuilder;
    }

    public abstract Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField);

    public abstract Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO conditionDTO);

    /**
     * Copies the values of the attribute to the field.
     * @param mdField
     * @param condition
     * @param conditionDTO
     */
    protected abstract void init(MdWebFieldDTO mdField, Condition condition, FieldConditionDTO conditionDTO);
    
    protected void setValue(AttributeDTO attr, Condition condition)
    {
      if (attr.isReadable())
      {
        // FIXME always use string values with the generics?
        String accessor = CommonGenerationUtil.SET + CommonGenerationUtil.upperFirstCharacter(attr.getName());
        try
        {
          ComponentDTO dto = attr.getContainingDTO();
          LoaderDecorator.load(dto.getType() + TypeGeneratorInfo.DTO_SUFFIX).getMethod(accessor).invoke(dto);
        }
        catch (Throwable t)
        {
          String msg = ""; //"Could not copy the value of the attribute [" + attr.getName() + "] to the field [" + condition.getFieldName() + "].";
          throw new ConversionExceptionDTO(msg, t);
        }
        //condition.setValue(value != null ? value.toString() : null);
      }
    }
  }
  
  public static abstract class BasicConditionBuilder extends ConditionBuilder
  {
    public BasicConditionBuilder(BasicConditionMdBuilder mdBuilder)
    {
      super(mdBuilder);
    }
    
    protected void init(MdWebFieldDTO mdField, Condition c, FieldConditionDTO conditionDTO)
    {
      List<? extends BusinessDTO> enums = conditionDTO.getEnumValues(BasicConditionInfo.OPERATION);
      String op = enums.get(0).getValue(EnumerationMasterDTO.ENUMNAME);
      c.setOid(conditionDTO.getOid());
      c.setOperation(op);
      c.setDefiningMdField(conditionDTO.getValue(BasicConditionInfo.DEFINING_MD_FIELD));
    }
  }

  public static class CharacterConditionBuilder extends BasicConditionBuilder
  {
    public CharacterConditionBuilder()
    {
      super(new ConditionMdBuilders.CharacterConditionMdBuilder());
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      CharacterConditionMd cMd = (CharacterConditionMd) this.getMdBuilder().create(mdForm, mdField, mdField.getFieldCondition());
      CharacterCondition c = new CharacterCondition(cMd);

      this.init(mdField, c, mdField.getFieldCondition());

      return c;
    }
    
    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO conditionDTO)
    {
      CharacterConditionMd cMd = (CharacterConditionMd) this.getMdBuilder().create(mdForm, mdField, conditionDTO);
      CharacterCondition c = new CharacterCondition(cMd);

      this.init(mdField, c, conditionDTO);

      return c;
    }
    
    @Override
    protected void init(MdWebFieldDTO mdField, Condition condition, FieldConditionDTO conditionDTO)
    {
      CharacterCondition c = (CharacterCondition) condition;
      super.init(mdField, c, conditionDTO);
      String value = conditionDTO.getValue(CharacterConditionInfo.VALUE);
      c.setValue(value);
    }
  }

  public static class DoubleConditionBuilder extends BasicConditionBuilder
  {
    public DoubleConditionBuilder()
    {
      super(new ConditionMdBuilders.DoubleConditionMdBuilder());
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      DoubleConditionMd cMd = (DoubleConditionMd) this.getMdBuilder().create(mdForm, mdField, mdField.getFieldCondition());
      DoubleCondition c = new DoubleCondition(cMd);

      this.init(mdField, c, mdField.getFieldCondition());

      return c;
    }
    
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO conditionDTO)
    {
      DoubleConditionMd cMd = (DoubleConditionMd) this.getMdBuilder().create(mdForm, mdField, conditionDTO);
      DoubleCondition c = new DoubleCondition(cMd);

      this.init(mdField, c, conditionDTO);

      return c;
    }
    
    @Override
    protected void init(MdWebFieldDTO mdField, Condition condition, FieldConditionDTO conditionDTO)
    {
      DoubleCondition c = (DoubleCondition) condition;
      super.init(mdField, c, conditionDTO);
      String value = conditionDTO.getValue(DoubleConditionInfo.VALUE);
      Double dVal = new Double(value);
      c.setValue(dVal);
    }
  }

  public static class LongConditionBuilder extends BasicConditionBuilder
  {
    public LongConditionBuilder()
    {
      super(new ConditionMdBuilders.LongConditionMdBuilder());
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      LongConditionMd cMd = (LongConditionMd) this.getMdBuilder().create(mdForm, mdField, mdField.getFieldCondition());
      LongCondition c = new LongCondition(cMd);

      this.init(mdField, c, mdField.getFieldCondition());

      return c;
    }
    
    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO conditionDTO)
    {
      LongConditionMd cMd = (LongConditionMd) this.getMdBuilder().create(mdForm, mdField, conditionDTO);
      LongCondition c = new LongCondition(cMd);

      this.init(mdField, c, conditionDTO);

      return c;
    }

    protected void init(MdWebFieldDTO mdField, Condition condition, FieldConditionDTO conditionDTO)
    {
      LongCondition c = (LongCondition) condition;
      super.init(mdField, c, conditionDTO);
      String value = conditionDTO.getValue(LongConditionInfo.VALUE);
      Long lVal = new Long(value);
      c.setValue(lVal);
    }

  }

  public static class DateConditionBuilder extends BasicConditionBuilder
  {
    public DateConditionBuilder()
    {
      super(new ConditionMdBuilders.DateConditionMdBuilder());
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      DateConditionMd cMd = (DateConditionMd) this.getMdBuilder().create(mdForm, mdField, mdField.getFieldCondition());
      DateCondition c = new DateCondition(cMd);

      this.init(mdField, c, mdField.getFieldCondition());

      return c;
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO conditionDTO)
    {
      DateConditionMd cMd = (DateConditionMd) this.getMdBuilder().create(mdForm, mdField, conditionDTO);
      DateCondition c = new DateCondition(cMd);

      this.init(mdField, c, conditionDTO);

      return c;
    }

    protected void init(MdWebFieldDTO mdField, Condition condition, FieldConditionDTO conditionDTO)
    {
      DateCondition c = (DateCondition) condition;
      super.init(mdField, c, conditionDTO);
      String value = conditionDTO.getValue(DateConditionInfo.VALUE);
      Date dVal = MdAttributeDateUtil.getTypeSafeValue(value);
      c.setValue(dVal);
    }

  }
  
  public abstract static class CompositeFieldConditionBuilder extends ConditionBuilder
  {
    public CompositeFieldConditionBuilder(CompositeFieldConditionMdBuilder mdBuilder)
    {
      super(mdBuilder);
    }

    public void init(MdWebFieldDTO mdField, CompositeFieldCondition c, FieldConditionDTO conditionDTO)
    {
      ClientRequestIF request = mdField.getDefiningMdForm().getRequest();
      
      String firstId = ((CompositeFieldConditionDTO)conditionDTO).getFirstConditionId();
      String secondId = ((CompositeFieldConditionDTO)conditionDTO).getSecondConditionId();
      
      FieldConditionDTO first = FieldConditionDTO.get(request, firstId);
      FieldConditionDTO second = FieldConditionDTO.get(request, secondId);
      
      ConditionBuilder firstCB = ConditionBuilders.getBuilder(first);
      ConditionBuilder secondCB = ConditionBuilders.getBuilder(second);
            
      Condition firstCondition = firstCB.create(mdField.getDefiningMdForm(), mdField, first);
      Condition secondCondition = secondCB.create(mdField.getDefiningMdForm(), mdField, second);
      
      c.setOid(conditionDTO.getOid());
      c.setFirstCondition(firstCondition);
      c.setSecondCondition(secondCondition);
      c.setDefiningMdField(ComponentDTOIF.EMPTY_VALUE); // Composites don't have a defining MdField (FIXME remove)
    }
  }
  
  public static class AndFieldConditionBuilder extends CompositeFieldConditionBuilder
  {
    public AndFieldConditionBuilder()
    {
      super(new ConditionMdBuilders.AndFieldConditionMdBuilder());
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      AndFieldConditionMd cMd = (AndFieldConditionMd) this.getMdBuilder().create(mdForm, mdField, mdField.getFieldCondition());
      AndFieldCondition c = new AndFieldCondition(cMd);
            
      this.init(mdField, c, mdField.getFieldCondition());
      
      return c;
    }

    @Override
    public Condition create(MdFormDTO mdForm, MdWebFieldDTO mdField, FieldConditionDTO conditionDTO)
    {
      AndFieldConditionMd cMd = (AndFieldConditionMd) this.getMdBuilder().create(mdForm, mdField, conditionDTO);
      AndFieldCondition c = new AndFieldCondition(cMd);
            
      this.init(mdField, c, conditionDTO);
      
      return c;
    }

    protected void init(MdWebFieldDTO mdField, Condition condition, FieldConditionDTO conditionDTO)
    {
      super.init(mdField, (AndFieldCondition)condition, conditionDTO);
      
    }
    
    public static AndFieldCondition newInstance(MdWebFieldDTO mdField, Condition first, Condition second)
    {
      ConditionMdBuilders.AndFieldConditionMdBuilder mdBuilder = new ConditionMdBuilders.AndFieldConditionMdBuilder();
      AndFieldConditionDTO andCond = new AndFieldConditionDTO(mdField.getRequest());
      AndFieldConditionMd cMd = (AndFieldConditionMd) mdBuilder.create(mdField.getDefiningMdForm(), mdField, andCond);
      AndFieldCondition c = new AndFieldCondition(cMd);
      
      c.setOid(IDGenerator.nextID());
      c.setFirstCondition(first);
      c.setSecondCondition(second);
      c.setDefiningMdField(ComponentDTOIF.EMPTY_VALUE); // Composites don't have a defining MdField (FIXME remove)
      
      return c;
    }
  }
}
