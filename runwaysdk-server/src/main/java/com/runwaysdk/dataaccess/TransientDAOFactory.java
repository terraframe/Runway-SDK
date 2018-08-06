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
package com.runwaysdk.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.attributes.tranzient.Attribute;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeFactory;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeMultiReference;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeStruct;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;
import com.runwaysdk.util.IdParser;

public class TransientDAOFactory
{
  /**
   * 
   * @param attributeMap
   * @param type
   * @param oid
   * @return
   */
  public static TransientDAO factoryMethod(Map<String, Attribute> attributeMap, String type)
  {
    return new TransientDAO(attributeMap, type);
  }

  /**
   * Returns a new TransientDAO instance of the given class name. Default values
   * are assigned attributes if specified by the metadata.
   * 
   * <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> !type.trim().equals("")
   * 
   * @param type
   *          Class name of the new TransientDAO to instantiate
   * @return new TransientDAO of the given TransientDAO
   * @throws DataAccessException
   *           if the given class name is abstract
   * @throws DataAccessException
   *           if metadata is not defined for the given class name
   */
  public static TransientDAO newInstance(String type)
  {
    // get the meta data for the given class
    MdTransientDAOIF mdTransientIF = MdTransientDAO.getMdTransientDAO(type);

    if (mdTransientIF.isAbstract())
    {
      String error = "Transient [" + mdTransientIF.definesType() + "] is abstract and cannot be instantiated";
      throw new AbstractInstantiationException(error, mdTransientIF);
    }

    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    // get list of all classes in inheritance relationship
    List<? extends MdTransientDAOIF> superMdTransientIFList = mdTransientIF.getSuperClasses();

    for (MdTransientDAOIF superMdTransientIF : superMdTransientIFList)
    {
      attributeMap.putAll(TransientDAOFactory.createRecordsForTransient(superMdTransientIF));
    }

    String newId = IdParser.buildId(ServerIDGenerator.nextID(), mdTransientIF.getOid());

    // Create the transientDAO
    TransientDAO newTransientDAO = factoryMethod(attributeMap, mdTransientIF.definesType());

    newTransientDAO.getAttribute(ComponentInfo.OID).setValue(newId);

    newTransientDAO.setTypeName(mdTransientIF.definesType());
    // This used to be in EntityDAO.save(), but has been moved here to help with
    // distributed issues

    newTransientDAO.setIsNew(true);

    return newTransientDAO;
  }

  /**
   * Returns a Hashtable of Attribute objects for the given transient name, not
   * including attributes that are inherited from other transient objects.
   * Attributes are initialized to an empty String. If a default value is
   * defined for the attribute, it is assigned to the attribute.
   * 
   * <br/>
   * <b>Precondition:</b> mdEntityIF != null
   * 
   * @param mdTransientDAOIF
   *          Create Attribute objects for attributes defined by the given
   *          entity.
   * @return Hashtable of Attribute objects for the given entity, not including
   *         attributes that are inherited from other entities
   */
  protected static Map<String, Attribute> createRecordsForTransient(MdTransientDAOIF mdTransientDAOIF)
  {
    Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    // get the all attribute meta data for this class
    List<? extends MdAttributeDAOIF> mdAttributeList = mdTransientDAOIF.definesAttributes();

    Attribute attribute = null;
    for (MdAttributeDAOIF mdAttributeIF : mdAttributeList)
    {
      MdAttributeConcreteDAOIF mdAttributeConcreteIF = mdAttributeIF.getMdAttributeConcrete();

      String attrName = mdAttributeIF.definesAttribute();

      // assign a default value (if any) as defined in the meta data
      String attrDefaultValue = mdAttributeIF.getAttributeInstanceDefaultValue();

      // New enumeration attributes need a unique oid so they can map to the
      // MdEnumerationIF.getDatabaseTableName() table
      if (mdAttributeConcreteIF instanceof MdAttributeEnumerationDAOIF)
      {
        String setOid = ServerIDGenerator.nextID();
        attribute = AttributeFactory.createAttribute(mdAttributeConcreteIF.getType(), mdAttributeIF.getKey(), attrName, mdTransientDAOIF.definesType(), setOid);

        if (!attrDefaultValue.equals(""))
        {
          ( (AttributeEnumeration) attribute ).setDefaultValue(attrDefaultValue);
        }
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        String setOid = ServerIDGenerator.nextID();
        attribute = AttributeFactory.createAttribute(mdAttributeConcreteIF.getType(), mdAttributeIF.getKey(), attrName, mdTransientDAOIF.definesType(), setOid);

        if (!attrDefaultValue.equals(""))
        {
          ( (AttributeMultiReference) attribute ).setDefaultValue(attrDefaultValue);
        }
      }
      else if (mdAttributeConcreteIF instanceof MdAttributeStructDAOIF)
      {
        MdStructDAOIF mdStructIF = ( (MdAttributeStructDAO) mdAttributeConcreteIF ).getMdStructDAOIF();
        StructDAO structDAO = StructDAO.newInstance(mdStructIF.definesType());

        attribute = AttributeFactory.createAttribute(mdAttributeConcreteIF.getType(), mdAttributeIF.getKey(), attrName, mdTransientDAOIF.definesType(), attrDefaultValue);
        ( (AttributeStruct) attribute ).setStructDAO(structDAO);
      }
      else
      {
        attribute = AttributeFactory.createAttribute(mdAttributeConcreteIF.getType(), mdAttributeIF.getKey(), attrName, mdTransientDAOIF.definesType(), attrDefaultValue);
      }
      attributeMap.put(attribute.getName(), attribute);
    }

    return attributeMap;
  }

  public static List<String> getAllTransientNames()
  {
    List<String> transientFields = new LinkedList<String>();
    transientFields.add(MdTypeDAOIF.TYPE_NAME_COLUMN);
    transientFields.add(MdTypeDAOIF.PACKAGE_NAME_COLUMN);

    List<String> mdBusinessTable = new LinkedList<String>();
    mdBusinessTable.add(MdTransientDAOIF.TABLE);
    mdBusinessTable.add(MdTypeDAOIF.TABLE);

    List<String> conditions = new LinkedList<String>();
    conditions.add(MdTransientDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN + " = " + MdTypeDAOIF.TABLE + "." + EntityDAOIF.ID_COLUMN);

    ResultSet resultSet = Database.query(Database.selectClause(transientFields, mdBusinessTable, conditions));

    List<String> returnList = new LinkedList<String>();

    try
    {
      while (resultSet.next())
      {
        String className = resultSet.getString(MdTypeDAOIF.TYPE_NAME_COLUMN);
        String packageName = resultSet.getString(MdTypeDAOIF.PACKAGE_NAME_COLUMN);
        String type = EntityDAOFactory.buildType(packageName, className);
        returnList.add(type);
      }
    }
    catch (SQLException sqlEx1)
    {
      Database.throwDatabaseException(sqlEx1);
    }
    finally
    {
      try
      {
        java.sql.Statement statement = resultSet.getStatement();
        resultSet.close();
        statement.close();
      }
      catch (SQLException sqlEx2)
      {
        Database.throwDatabaseException(sqlEx2);
      }
    }

    return returnList;
  }

  /**
   * Builds a TransientDAO object using the attributes form the given
   * valueObject.
   * 
   * @param transientType
   * @param valueObject
   * @return TransientDAO object using the attributes form the given
   *         valueObject.
   */
  public static TransientDAO buildTransientDAOfromValueObject(String transientType, ValueObject valueObject)
  {
    Map<String, com.runwaysdk.dataaccess.attributes.value.Attribute> valueAttributeMap = valueObject.attributeMap;

    Map<String, Attribute> tranisentAttributeMap = new HashMap<String, Attribute>();

    for (com.runwaysdk.dataaccess.attributes.value.Attribute attribute : valueAttributeMap.values())
    {
      tranisentAttributeMap.put(attribute.getName(), attribute.buildTransientAttribute(transientType));
    }

    TransientDAO transientDAO = TransientDAO.newInstance(transientType);
    transientDAO.addAttributes(tranisentAttributeMap);
    transientDAO.apply();

    return transientDAO;
  }

}
