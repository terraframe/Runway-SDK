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

import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.MdWebBooleanDAOIF;
import com.runwaysdk.dataaccess.MdWebDateDAOIF;
import com.runwaysdk.dataaccess.MdWebDecimalDAOIF;
import com.runwaysdk.dataaccess.MdWebDoubleDAOIF;
import com.runwaysdk.dataaccess.MdWebFloatDAOIF;
import com.runwaysdk.dataaccess.MdWebIntegerDAOIF;
import com.runwaysdk.dataaccess.MdWebLongDAOIF;

public class ColumnFactory
{
  public ExcelColumn getColumn(MdWebAttributeDAOIF mdField)
  {
    if (mdField instanceof MdWebBooleanDAOIF)
    {
      return new BooleanFieldColumn((MdWebBooleanDAOIF) mdField);
    }
    else if (mdField instanceof MdWebFloatDAOIF)
    {
      return new FloatFieldColumn((MdWebFloatDAOIF) mdField);
    }
    else if (mdField instanceof MdWebDoubleDAOIF)
    {
      return new DoubleFieldColumn((MdWebDoubleDAOIF) mdField);
    }
    else if (mdField instanceof MdWebDecimalDAOIF)
    {
      return new DecimalFieldColumn((MdWebDecimalDAOIF) mdField);
    }
    else if (mdField instanceof MdWebLongDAOIF)
    {
      return new LongFieldColumn((MdWebLongDAOIF) mdField);
    }
    else if (mdField instanceof MdWebIntegerDAOIF)
    {
      return new IntegerFieldColumn((MdWebIntegerDAOIF) mdField);
    }
    else if (mdField instanceof MdWebDateDAOIF)
    {
      return new DateFieldColumn((MdWebDateDAOIF) mdField);
    }

    return new StringFieldColumn((MdWebAttributeDAOIF) mdField);
  }
}
