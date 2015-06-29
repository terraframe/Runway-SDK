/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;

import com.runwaysdk.business.BusinessEnumeration;
import com.runwaysdk.business.InvalidEnumerationName;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.constants.MdAttributeDateUtil;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.conversion.ConversionException;
import com.runwaysdk.util.DateUtilities;

public class AttributeColumn extends ExcelColumn
{
  private MdAttributeDAOIF mdAttribute;

  public AttributeColumn(MdAttributeDAOIF mdAttribute, int index)
  {
    super(mdAttribute.definesAttribute(), mdAttribute.getDisplayLabel(Session.getCurrentLocale()), mdAttribute.getDescription(Session.getCurrentLocale()), index, mdAttribute.isRequired());
    this.mdAttribute = mdAttribute;
  }

  public AttributeColumn(MdAttributeDAOIF mdAttribute)
  {
    this(mdAttribute, 0);
  }

  /**
   * @return The name of the setter method (in the generated source) for this
   *         column
   */
  public String getSetterMethodName()
  {
    String attributeName = CommonGenerationUtil.upperFirstCharacter(mdAttribute.definesAttribute());

    if (mdAttribute instanceof MdAttributeEnumerationDAOIF)
    {
      return "add" + attributeName;
    }
    else
    {
      return CommonGenerationUtil.SET + attributeName;
    }
  }

  public boolean isEnum()
  {
    return mdAttribute instanceof MdAttributeEnumerationDAOIF;
  }

  public MdAttributeDAOIF getMdAttribute()
  {
    return mdAttribute;
  }

  /**
   * Excel contains several different types of cells, with different getters.
   * This method checks the expected type, calls the appropriate getter on the
   * cell, then wraps the result in the correct java type for use in the
   * typesafe setter methods.
   * 
   * @param cell
   * @param column
   * @return
   */
  public Object getValue(Cell cell) throws Exception
  {
    String type = this.javaType();

    if (this.isEnum())
    {
      String cellValue = ExcelUtil.getString(cell);
      Class<?> enumClass = LoaderDecorator.load(type);

      BusinessEnumeration[] values = (BusinessEnumeration[]) enumClass.getMethod("values").invoke(null);
      for (BusinessEnumeration value : values)
      {
        if (cellValue.equalsIgnoreCase(value.getDisplayLabel()))
        {
          return value;
        }
      }

      // We did not find a matching enum value. That is a problem.
      MdAttributeEnumerationDAO mdAttribute = (MdAttributeEnumerationDAO) this.getMdAttribute().getMdAttributeConcrete();
      throw new InvalidEnumerationName("devMessage", cellValue, mdAttribute.getMdEnumerationDAO());
    }


    /*
     * Check for null values
     */
    if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
    {
      return null;
    }
    else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
    {
      switch (cell.getCachedFormulaResultType())
      {
        case Cell.CELL_TYPE_STRING:
          String value = cell.getRichStringCellValue().getString();

          if (value == null || value.length() == 0)
          {
            return null;
          }

          break;
        case Cell.CELL_TYPE_BLANK:
          return null;
      }
    }
    else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
    {
      String value = cell.getRichStringCellValue().getString();

      if (value == null || value.length() == 0)
      {
        return null;
      }
    }

    if (type.equals(String.class.getName()))
    {
      return ExcelUtil.getString(cell);
    }
    else if (type.equals(Long.class.getName()))
    {
      return new Long(new Double(cell.getNumericCellValue()).longValue());
    }
    else if (type.equals(Float.class.getName()))
    {
      return new Float(new Double(cell.getNumericCellValue()).floatValue());
    }
    else if (type.equals(Double.class.getName()))
    {
      return new Double(cell.getNumericCellValue());
    }
    else if (type.equals(BigDecimal.class.getName()))
    {
      return new BigDecimal(cell.getNumericCellValue());
    }
    else if (type.equals(Integer.class.getName()))
    {
      return new Integer(new Double(cell.getNumericCellValue()).intValue());
    }
    else if (type.equals(Boolean.class.getName()))
    {
      return ExcelUtil.getBoolean(cell, (MdAttributeBooleanDAOIF) this.getMdAttribute().getMdAttributeConcrete());
    }
    else if (type.equals(java.util.Date.class.getName()))
    {
      return cell.getDateCellValue();
    }
    String error = "The type [" + type + "] is not supported as a parameter.";
    throw new ProgrammingErrorException(error);
  }

  @Override
  public void setValue(Cell cell, String value)
  {
    if (value != null && value.length() > 0)
    {
      String type = this.javaType();

      if (type.equals(Long.class.getName()))
      {
        cell.setCellValue(Long.parseLong(value));
      }
      else if (type.equals(Float.class.getName()))
      {
        cell.setCellValue(Float.parseFloat(value));
      }
      else if (type.equals(Double.class.getName()) || type.equals(BigDecimal.class.getName()))
      {
        cell.setCellValue(Double.parseDouble(value));
      }
      else if (type.equals(Integer.class.getName()))
      {
        cell.setCellValue(Integer.parseInt(value));
      }
      else if (type.equals(Boolean.class.getName()))
      {
        cell.setCellValue(Boolean.parseBoolean(value));
      }
      else if (type.equals(java.util.Date.class.getName()))
      {
        try
        {
          Date date = DateUtilities.parseDate(value);
          cell.setCellValue(date);
        }
        catch (ConversionException e)
        {
          Date date = MdAttributeDateUtil.getTypeSafeValue(value);
          cell.setCellValue(date);
        }
      }
      else
      {
        cell.setCellValue(new HSSFRichTextString(value));
      }
    }
  }

  /**
   * @return The fully qualified java type of this column
   */
  public String javaType()
  {
    String javaType = mdAttribute.javaType(false);
    if (javaType.equals(String.class.getSimpleName()))
      return String.class.getName();
    if (javaType.equals(Long.class.getSimpleName()))
      return Long.class.getName();
    if (javaType.equals(Float.class.getSimpleName()))
      return Float.class.getName();
    if (javaType.equals(Double.class.getSimpleName()))
      return Double.class.getName();
    if (javaType.equals(Integer.class.getSimpleName()))
      return Integer.class.getName();
    if (javaType.equals(Boolean.class.getSimpleName()))
      return Boolean.class.getName();
    return javaType;
  }

  public void setInstanceValue(Mutable instance, Object value) throws Exception
  {
    Class<?> businessClass = instance.getClass();
    Class<?> paramClass = LoaderDecorator.load(this.javaType());

    String methodName = this.getSetterMethodName();
    Method method = businessClass.getMethod(methodName, paramClass);
    method.invoke(instance, value);
  }
}
