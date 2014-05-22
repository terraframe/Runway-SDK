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
package com.runwaysdk.transport.metadata.caching;

import java.util.HashMap;
import java.util.Map;

import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;

public class MetadataRetriever
{
  
  public static ClassMdSession getMetadataForType(String type, Map<String, ClassMdSession> classMdSessionMap)
  {
    if (classMdSessionMap.containsKey(type))
    {
      return classMdSessionMap.get(type);
    }
    
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(type);

    // Get system attributes
    String definesType = mdClass.definesType();
    String description = mdClass.getDescription(Session.getCurrentLocale());
    String displayLabel = mdClass.getDisplayLabel(Session.getCurrentLocale());
    String id = mdClass.getId();

    // Get all user defined, (non-system) attributes to the attributeMap
    Map<String, AttributeMdSession> mdAttributeMap = new HashMap<String, AttributeMdSession>();
    Map<String, ? extends MdAttributeDAOIF> attributeDAOMap = mdClass.getAllDefinedMdAttributeMap();
    for (MdAttributeDAOIF attr : attributeDAOMap.values())
    {
      // if (attr.getValue("system").equals(MdAttributeBooleanInfo.FALSE)) {
      mdAttributeMap.put(attr.definesAttribute(), attr.getAttributeMdSession());
      // }
    }

    // Get super type
    ClassMdSession superType = null;
    MdClassDAOIF superClass = mdClass.getSuperClass();
    if (superClass != null)
    {
      superType = getMetadataForType(superClass.definesType(), classMdSessionMap);
    }

    Session session = (Session)Session.getCurrentSession();
    
    boolean readable = SessionFacade.checkTypeAccess(session.getId(), Operation.READ, type);

    boolean writable = SessionFacade.checkTypeAccess(session.getId(), Operation.WRITE, type);
    
    boolean delete = SessionFacade.checkTypeAccess(session.getId(), Operation.DELETE, type);
    
    ClassMdSession classMdSession = new ClassMdSession(definesType, displayLabel, description, id, superType, readable, writable, delete, mdAttributeMap);
    
    classMdSessionMap.put(type, classMdSession);
    
    return classMdSession;
    
  }
}
