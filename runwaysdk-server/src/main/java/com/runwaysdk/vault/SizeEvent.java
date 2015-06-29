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
package com.runwaysdk.vault;

public class SizeEvent implements FileEvent
{
  /**
   * Enumeration specifying the type of size event which can occur.
   * 
   * @author Justin Snmethie
   */
  public enum EventType
  {
    /**
     * The size of the file has changed
     */
    SIZE_CHANGE();
  }
  
  /**
   * Type of the SizeEvent
   */
  private EventType eventType;
  
  /**
   * The amount of the size event 
   */
  private long amount;
  
  public SizeEvent(EventType eventType, long amount)
  {
    this.eventType = eventType;
    this.amount = amount;
  }
    
  /**
   * Returns the amount attribute of the SizeEvent
   * 
   * @return
   */
  public long getAmount()
  {
    return amount;
  }
  
  /**
   * Returns the type of size event
   * 
   * @return
   */
  public EventType getEventType()
  {
    return eventType;
  }
}
