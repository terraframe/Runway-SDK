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
package com.runwaysdk.query;

import com.runwaysdk.dataaccess.MdLocalStructDAOIF;


public interface AttributeLocalIF extends SelectableStruct
{

  /**
   * Returns a query Attribute object with the value from the current locale.
   * 
   * @return query Attribute object with the value from the current locale.
   */
  public AttributePrimitive getSessionLocale();
  
  /**
   * Returns a query Attribute object with the value from the current locale.
   *
   * @param attributeAlias user defined alias.
   * 
   * @return query Attribute object with the value from the current locale.
   */
  public AttributePrimitive getSessionLocale(String attributeAlias);
  
  
  public MdLocalStructDAOIF getMdStructDAOIF();
  
  /**
   * Builds a <code>Coalesce</code> object with the order of localization for the user's locale. 
   * This effectively performs a localized query.
   *
   * @return <code>Coalesce</code> object with the order of localization for the user's locale. 
   */
  public Coalesce localize();
  
  /**
   * Builds a <code>Coalesce</code> object with the order of localization for the user's locale. 
   * This effectively performs a localized query.
   *
   * @return <code>Coalesce</code> object with the order of localization for the user's locale. 
   */
  public Coalesce localize(String attributeAlias);
  
  /**
   * Builds a <code>Coalesce</code> object with the order of localization for the user's locale. 
   * This effectively performs a localized query.
   *
   * @return <code>Coalesce</code> object with the order of localization for the user's locale. 
   */
  public Coalesce localize(String attributeAlias, String displayLabel);
}
