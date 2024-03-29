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
package com.runwaysdk.business;

import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.util.IdParser;

public class Util extends SessionComponent
{
  /**
   * 
   */
  private static final long serialVersionUID = -555244264545654598L;

  /**
   * Blank constructor can be used for new or existing instances. It is <b>critical</b> that subclasses call {@link Util#setTransientDAO(TransientDAO)} to correclty initialize the entity.
   */
  public Util()
  {
    super();
  }

  /**
   * Creates a new View object that may not be typesafe. The DAO the new object represents will be of the specified type, but if this constructor has been super()d into, the concrete java type of the
   * constructed object will be unknown.
   * 
   * To guarantee that the java type and DAO type correspond correctly, use {@link View#View()}, which uses polymorphism instead of a paramater.
   * 
   * @param type
   *          The type of the DAO that this View object will represent
   */
  public Util(String type)
  {
    super(type);
  }

  /**
   * Returns the Util object with the given oid, if it has been persisted to the user's session.
   * 
   * @param oid
   * @return Util object with the given oid.
   */
  public static Util get(String oid)
  {
    // An empty string likely indicates the value was never set. Return null
    if (oid.length() == 0)
      return null;

    SessionIF session = Session.getCurrentSession();
    if (session != null)
    {
      Mutable mutable = session.get(oid);

      if (mutable != null)
      {
        return (Util) mutable;
      }
    }

    String errMsg = "An instance of type [" + Util.class.getName() + "] with oid [" + oid + "] does not exist in the user's session.";
    throw new DataNotFoundException(errMsg, MdClassDAO.getMdClassByRootId(IdParser.parseMdTypeRootIdFromId(oid)));
  }

  @Override
  protected String getDeclaredType()
  {
    // HEADS UP
    throw new UnsupportedOperationException("");
  }

}
