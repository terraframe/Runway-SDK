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
package com.runwaysdk.web.applet;

import java.text.NumberFormat;

public class FileSize
{

  private double size;
  
  private int unit;
  
  public FileSize(long amount)
  {
    this.size = amount;
    this.unit = 0;
    
    this.convertToLargestUnits();
  }
  
  private final void convertToLargestUnits()
  {
    while(this.size > 1000 && this.unit < 3)
    {
      this.size /= 1000;
      this.unit++;
    }
  }
  
  /**
   * Adds to the file size
   * 
   * @param amount The number of bytes to add
   */
  public void add(long amount)
  {
    size += this.convertTo(amount);
    
    this.convertToLargestUnits();    
  }
  
  /**
   * Substract from the file size
   * 
   * @param amount The number of bytes to remove
   */
  public void subtract(long amount)
  {
    size -= this.convertTo(amount);
    
    this.convertToLargestUnits();  
  }
  
  @Override
  public String toString()
  {
    NumberFormat format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(2);
    format.setMinimumFractionDigits(0);
    
    return format.format(size) + " " + getUnits();
  }
  
  private String getUnits()
  {
    switch(unit)
    {
      case 1: return "Kb";
      case 2: return "Mb";
      case 3: return "Gb";
      default:
        return "b";
    }
  }
  
  public double getSize()
  {
    return size;
  }
  
  /**
   * @param amount The number of bytes
   * 
   * @return Converts the number of bytes into the unit of this FileSize
   */
  public double convertTo(long amount)
  {
    return amount * Math.pow(1000, -unit);
  }
}
