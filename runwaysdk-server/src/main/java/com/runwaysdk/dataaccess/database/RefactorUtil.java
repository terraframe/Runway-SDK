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
package com.runwaysdk.dataaccess.database;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class RefactorUtil
{
  /**
   * Changes the type and the rootid of the given id to the new id for the object, relationships,
   * enum item ids, and references in the system.
   * Assumes all of the records have been copied over to the new tables and their ids need to be updated.
   *
   * @param originalId
   * @param oldType
   * @param newType
   */
  public static void refactorAttributeType(String originalId, String oldType, String newId, String newType)
  {
    // Update ids in the object tables
    System.out.println("-- Update the ids and the type fields in the object tables");
    MdBusinessDAOIF newTypeMdBusinessDAOIF = MdBusinessDAO.getMdBusinessDAO(newType);

    List<? extends MdBusinessDAOIF> superMdBusinessList = newTypeMdBusinessDAOIF.getSuperClasses();

    for (MdBusinessDAOIF superClass : superMdBusinessList)
    {
      String
        sql =  "UPDATE "+superClass.getTableName();
        sql += "\n   SET id = '"+newId+"'";

        if (superClass.isRootOfHierarchy())
        {
          sql += ",\n       type = '"+newType+"'";
        }

        sql += "\n WHERE id = '"+originalId+"';\n";
      System.out.println(sql);
    }

    List<MdRelationshipDAOIF> parentMdRelationships = newTypeMdBusinessDAOIF.getAllParentMdRelationships();
    List<MdRelationshipDAOIF> childMdRelationships = newTypeMdBusinessDAOIF.getAllChildMdRelationships();

    Set<String> tableNameSet = new HashSet<String>();

    System.out.println("\n--Parent Relationships");
    for (MdRelationshipDAOIF mdRelationshipDAOIF : parentMdRelationships)
    {
      List<MdRelationshipDAOIF> subMdRelationshipList = mdRelationshipDAOIF.getAllSubClasses();
      List<MdRelationshipDAOIF> superMdRelationshipList = mdRelationshipDAOIF.getSuperClasses();

      for (MdRelationshipDAOIF subClass : subMdRelationshipList)
      {
        tableNameSet.add(subClass.getTableName());
      }

      for (MdRelationshipDAOIF superClass : superMdRelationshipList)
      {
        tableNameSet.add(superClass.getTableName());
      }
    }
    Iterator<String> tableNameIterator = tableNameSet.iterator();
    while (tableNameIterator.hasNext())
    {
      String
        sql =  "UPDATE "+tableNameIterator.next();
        sql += "\n   SET "+RelationshipInfo.PARENT_ID+" = '"+newId+"'";
        sql += "\n WHERE "+RelationshipInfo.PARENT_ID+" = '"+originalId+"';\n";
      System.out.println(sql);
    }

    tableNameSet.clear();
    System.out.println("\n--Child Relationships");
    for (MdRelationshipDAOIF mdRelationshipDAOIF : childMdRelationships)
    {
      List<MdRelationshipDAOIF> subMdRelationshipList = mdRelationshipDAOIF.getAllSubClasses();
      List<MdRelationshipDAOIF> superMdRelationshipList = mdRelationshipDAOIF.getSuperClasses();

      for (MdRelationshipDAOIF subClass : subMdRelationshipList)
      {
        tableNameSet.add(subClass.getTableName());
      }

      for (MdRelationshipDAOIF superClass : superMdRelationshipList)
      {
        tableNameSet.add(superClass.getTableName());
      }
    }
    tableNameIterator = tableNameSet.iterator();
    while (tableNameIterator.hasNext())
    {
      String
        sql =  "UPDATE "+tableNameIterator.next();
        sql += "\n   SET "+RelationshipInfo.CHILD_ID+" = '"+newId+"'";
        sql += "\n WHERE "+RelationshipInfo.CHILD_ID+" = '"+originalId+"';\n";
      System.out.println(sql);
    }

    System.out.println("\n-- Reference Attributes");
    List<MdAttributeReferenceDAOIF> mdAttrRefList = newTypeMdBusinessDAOIF.getAllReferenceAttributes();
    for (MdAttributeReferenceDAOIF mdAttributeReferenceDAOIF : mdAttrRefList)
    {
      MdClassDAOIF mdClassDAOIF = mdAttributeReferenceDAOIF.definedByClass();

      if (mdClassDAOIF instanceof MdEntityDAOIF)
      {
        String
          sql =  "UPDATE "+((MdEntityDAOIF)mdClassDAOIF).getTableName();
          sql += "\n   SET "+mdAttributeReferenceDAOIF.getColumnName()+" = '"+newId+"'";
          sql += "\n WHERE "+mdAttributeReferenceDAOIF.getColumnName()+" = '"+originalId+"';\n";
        System.out.println(sql);
      }
    }

    System.out.println("-- Updating item_ids in MdEnumeration tables.");
    if (newTypeMdBusinessDAOIF.getSuperClass().definesType().equals(EnumerationMasterInfo.CLASS))
    {
      List<MdEnumerationDAOIF> mdEnumList = newTypeMdBusinessDAOIF.getMdEnumerationDAOs();

      for (MdEnumerationDAOIF mdEnumerationDAOIF : mdEnumList)
      {
        String
          sql =  "UPDATE "+mdEnumerationDAOIF.getTableName();
          sql += "\n   SET "+MdEnumerationInfo.ITEM_ID+" = '"+newId+"'";
          sql += "\n WHERE "+MdEnumerationInfo.ITEM_ID+" = '"+originalId+"';\n";
        System.out.println(sql);
      }

    }

  }

}
