/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.excel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;
import com.runwaysdk.system.metadata.MdAttributeBlob;
import com.runwaysdk.system.metadata.MdAttributeEncryption;
import com.runwaysdk.system.metadata.MdAttributeRef;

public class ExcelUtil
{
  /**
   * A comparator that sorts mdAttributes alphabetically
   */
  private static Comparator<MdAttributeDAOIF> alphabetical = new Comparator<MdAttributeDAOIF>()
                                                           {
                                                             public int compare(MdAttributeDAOIF o1, MdAttributeDAOIF o2)
                                                             {
                                                               return o1.definesAttribute().compareTo(o2.definesAttribute());
                                                             }
                                                           };

  /**
   * Returns a sorted list of attributes for the given type. Excludes attributes
   * that we can't or don't want to export, like {@link MdAttributeRef}s,
   * {@link MdAttributeEncryption}s, {@link MdAttributeBlob}s, etc.
   * 
   * @param mdClass
   * @return
   */
  @SuppressWarnings("unchecked")
  public static List<? extends MdAttributeDAOIF> getAttributes(MdClassDAOIF mdClass, MdAttributeFilter filter)
  {
    List<? extends MdAttributeDAOIF> mdAttributeDAOs = mdClass.getAllDefinedMdAttributes();

    Iterator<? extends MdAttributeDAOIF> iterator = mdAttributeDAOs.iterator();
    while (iterator.hasNext())
    {
      MdAttributeDAOIF mdAttribute = iterator.next().getMdAttributeConcrete();

      // Some attributes don't make sense to try to export/import. Remove them
      if (!filter.accept(mdAttribute))
      {
        iterator.remove();
      }
    }

    Class<?> clazz = LoaderDecorator.load(mdClass.definesType());
    List<String> customOrder;
    try
    {
      customOrder = (List<String>) clazz.getMethod("customAttributeOrder").invoke(null);
      Collections.sort(mdAttributeDAOs, new CustomExcelComparator(customOrder));
    }
    catch (Exception e)
    {
      // If the method isn't defined, we just sort alphabetically
      Collections.sort(mdAttributeDAOs, alphabetical);
    }
    return mdAttributeDAOs;
  }

  public static Boolean getBoolean(HSSFCell cell, MdAttributeBooleanDAOIF mdAttribute)
  {
    String positiveLabel = mdAttribute.getPositiveDisplayLabel(Session.getCurrentLocale());
    String negativeLabel = mdAttribute.getNegativeDisplayLabel(Session.getCurrentLocale());
    return getBooleanInternal(cell, positiveLabel, negativeLabel);
  }

  private static Boolean getBooleanInternal(HSSFCell cell, String positiveLabel, String negativeLabel)
  {
    if (cell == null)
    {
      return Boolean.FALSE;
    }

    int cellType = cell.getCellType();
    // In the case of formula, find out what type the formula will produce
    if (cellType == HSSFCell.CELL_TYPE_FORMULA)
    {
      cellType = cell.getCachedFormulaResultType();
    }

    if (cellType == HSSFCell.CELL_TYPE_STRING)
    {
      String value = cell.getRichStringCellValue().getString().trim();

      if (value.equalsIgnoreCase(positiveLabel) || value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("x"))
      {
        return Boolean.TRUE;
      }
      if (value.equalsIgnoreCase(negativeLabel) || value.equalsIgnoreCase("false") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("n") || value.length() == 0)
      {
        return Boolean.FALSE;
      }

      throw new AttributeValueException("[" + value + "] is not a recognized boolean in excel", value);
    }
    else if (cellType == HSSFCell.CELL_TYPE_NUMERIC)
    {
      Double value = new Double(cell.getNumericCellValue());

      return value.equals(new Double(1));
    }
    else
    {
      return Boolean.valueOf(cell.getBooleanCellValue());
    }
  }

  public static Boolean getBoolean(HSSFCell cell)
  {
    return getBooleanInternal(cell, "Y", "N");
  }

  public static String getString(HSSFCell cell)
  {
    if (cell == null)
    {
      return null;
    }

    int cellType = cell.getCellType();

    // In the case of formula, find out what type the formula will produce
    if (cellType == HSSFCell.CELL_TYPE_FORMULA)
    {
      cellType = cell.getCachedFormulaResultType();
    }

    if (cellType == HSSFCell.CELL_TYPE_BLANK)
    {
      return "";
    }
    else if (cellType == HSSFCell.CELL_TYPE_NUMERIC)
    {
      return ( new BigDecimal(cell.getNumericCellValue()) ).toString();
    }
    else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN)
    {
      return new Boolean(cell.getBooleanCellValue()).toString();
    }
    else
    {
      return cell.getRichStringCellValue().getString().trim();
    }
  }

  public static Integer getInteger(HSSFCell cell)
  {
    if (cell == null)
    {
      return null;
    }

    int cellType = cell.getCellType();

    // In the case of formula, find out what type the formula will produce
    if (cellType == HSSFCell.CELL_TYPE_FORMULA)
    {
      cellType = cell.getCachedFormulaResultType();
    }

    if (cellType == HSSFCell.CELL_TYPE_BLANK)
    {
      return null;
    }
    else if (cellType == HSSFCell.CELL_TYPE_NUMERIC)
    {
      return ( new BigDecimal(cell.getNumericCellValue()) ).intValue();
    }
    else
    {
      return Integer.parseInt(cell.getRichStringCellValue().getString().trim());
    }
  }
}
