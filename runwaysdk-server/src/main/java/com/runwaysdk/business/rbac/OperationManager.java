/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.business.rbac;

import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdSessionDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.DomainTupleDAOIF;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

public class OperationManager
{
  /**
   * Singleton OperationManager
   */
  private static OperationManager operationManager;

  /**
   * The set of operations valid for a MdMethod
   */
  private Set<Operation>          mdMethodOperations;

  /**
   * The set of operations valid for a MdBusiness
   */
  private Set<Operation>          mdBusinessOperations;

  /**
   * The set of operations valid for a MdRelationship
   */
  private Set<Operation>          mdRelationshipOperations;

  /**
   * The set of operations valid for a MdSessions
   */
  private Set<Operation>          mdSessionOperations;

  /**
   * The set of operations valid for a MdAttribute
   */
  private Set<Operation>          mdAttributeOperations;

  /**
   * The set of operations valid for a BusinessDAO
   */
  private Set<Operation>          businessDAOoperations;

  /**
   * The set of operations valid when the Metadata attribute on TypeTuple
   * references a MdRelationship.
   */
  private Set<Operation>          tupleMdRelationshipOperations;

  private OperationManager()
  {
    mdBusinessOperations = new TreeSet<Operation>();
    mdBusinessOperations.add(Operation.CREATE);
    mdBusinessOperations.add(Operation.READ);
    mdBusinessOperations.add(Operation.WRITE);
    mdBusinessOperations.add(Operation.READ_ALL);
    mdBusinessOperations.add(Operation.WRITE_ALL);
    mdBusinessOperations.add(Operation.DELETE);
    mdBusinessOperations.add(Operation.DENY_CREATE);
    mdBusinessOperations.add(Operation.DENY_READ);
    mdBusinessOperations.add(Operation.DENY_WRITE);
    mdBusinessOperations.add(Operation.DENY_DELETE);
    mdBusinessOperations.add(Operation.GRANT);
    mdBusinessOperations.add(Operation.REVOKE);

    mdRelationshipOperations = new TreeSet<Operation>();
    mdRelationshipOperations.add(Operation.CREATE);
    mdRelationshipOperations.add(Operation.DELETE);
    mdRelationshipOperations.add(Operation.WRITE);
    mdRelationshipOperations.add(Operation.READ);
    mdRelationshipOperations.add(Operation.WRITE_ALL);
    mdRelationshipOperations.add(Operation.READ_ALL);
    mdRelationshipOperations.add(Operation.DENY_CREATE);
    mdRelationshipOperations.add(Operation.DENY_DELETE);
    mdRelationshipOperations.add(Operation.DENY_WRITE);
    mdRelationshipOperations.add(Operation.DENY_READ);
    mdRelationshipOperations.add(Operation.GRANT);
    mdRelationshipOperations.add(Operation.REVOKE);
    mdRelationshipOperations.add(Operation.ADD_CHILD);
    mdRelationshipOperations.add(Operation.ADD_PARENT);
    mdRelationshipOperations.add(Operation.DELETE_CHILD);
    mdRelationshipOperations.add(Operation.DELETE_PARENT);
    mdRelationshipOperations.add(Operation.WRITE_CHILD);
    mdRelationshipOperations.add(Operation.WRITE_PARENT);
    mdRelationshipOperations.add(Operation.READ_CHILD);
    mdRelationshipOperations.add(Operation.READ_PARENT);

    mdSessionOperations = new TreeSet<Operation>();
    mdSessionOperations.add(Operation.CREATE);
    mdSessionOperations.add(Operation.READ);
    mdSessionOperations.add(Operation.WRITE);
    mdSessionOperations.add(Operation.READ_ALL);
    mdSessionOperations.add(Operation.WRITE_ALL);
    mdSessionOperations.add(Operation.DELETE);
    mdSessionOperations.add(Operation.DENY_CREATE);
    mdSessionOperations.add(Operation.DENY_READ);
    mdSessionOperations.add(Operation.DENY_WRITE);
    mdSessionOperations.add(Operation.DENY_DELETE);
    mdSessionOperations.add(Operation.GRANT);
    mdSessionOperations.add(Operation.REVOKE);

    mdAttributeOperations = new TreeSet<Operation>();
    mdAttributeOperations.add(Operation.READ);
    mdAttributeOperations.add(Operation.WRITE);
    mdAttributeOperations.add(Operation.DENY_READ);
    mdAttributeOperations.add(Operation.DENY_WRITE);
    mdAttributeOperations.add(Operation.GRANT);
    mdAttributeOperations.add(Operation.REVOKE);
    mdAttributeOperations.add(Operation.WRITE_CHILD);
    mdAttributeOperations.add(Operation.WRITE_PARENT);
    mdAttributeOperations.add(Operation.READ_CHILD);
    mdAttributeOperations.add(Operation.READ_PARENT);

    businessDAOoperations = new TreeSet<Operation>();
    businessDAOoperations.add(Operation.WRITE);
    businessDAOoperations.add(Operation.READ);
    businessDAOoperations.add(Operation.DELETE);
    businessDAOoperations.add(Operation.DENY_WRITE);
    businessDAOoperations.add(Operation.DENY_READ);
    businessDAOoperations.add(Operation.DENY_DELETE);
    businessDAOoperations.add(Operation.GRANT);
    businessDAOoperations.add(Operation.REVOKE);

    mdMethodOperations = new TreeSet<Operation>();
    mdMethodOperations.add(Operation.EXECUTE);
    mdMethodOperations.add(Operation.GRANT);

    tupleMdRelationshipOperations = new TreeSet<Operation>();
    tupleMdRelationshipOperations.add(Operation.ADD_CHILD);
    tupleMdRelationshipOperations.add(Operation.ADD_PARENT);
    tupleMdRelationshipOperations.add(Operation.DELETE_CHILD);
    tupleMdRelationshipOperations.add(Operation.DELETE_PARENT);
    tupleMdRelationshipOperations.add(Operation.WRITE_CHILD);
    tupleMdRelationshipOperations.add(Operation.WRITE_PARENT);
    tupleMdRelationshipOperations.add(Operation.READ_CHILD);
    tupleMdRelationshipOperations.add(Operation.READ_PARENT);
  }

  /**
   * Returns the set of valid operations of a given businessDAO.
   *
   * @param businessDAO
   *          The BusinessDAO to get the valid operations.
   * @return Returns the set of valid operations of a given businessDAO.
   */
  public Set<Operation> getValidOperations(BusinessDAOIF businessDAO)
  {
    if (businessDAO instanceof MdClassDimensionDAOIF)
    {
      businessDAO = ( (MdClassDimensionDAOIF) businessDAO ).definingMdClass();
    }

    if (businessDAO instanceof MdBusinessDAOIF || businessDAO instanceof MdStructDAOIF)
    {
      return mdBusinessOperations;
    }
    else if (businessDAO instanceof MdRelationshipDAOIF)
    {
      return mdRelationshipOperations;
    }
    else if (businessDAO instanceof MdAttributeConcreteDAOIF)
    {
      return mdAttributeOperations;
    }
    else if (businessDAO instanceof MdSessionDAOIF)
    {
      return mdSessionOperations;
    }
    else if (businessDAO instanceof DomainTupleDAOIF)
    {
      DomainTupleDAOIF tuple = (DomainTupleDAOIF) businessDAO;
      MetadataDAOIF metadata = tuple.getMetaData();

      return getValidOperations(metadata);
    }
    else if (businessDAO instanceof TypeTupleDAOIF)
    {
      // If the MetaData attribute references a MdRelationship
      // then return the StateMaster-MdRelationship operations
      // else return the StateMaster-MdAttribute operations

      TypeTupleDAOIF typeTuple = (TypeTupleDAOIF) businessDAO;
      MetadataDAOIF metadata = typeTuple.getMetaData();

      if (metadata instanceof MdRelationshipDAOIF)
      {
        return tupleMdRelationshipOperations;
      }

      return mdAttributeOperations;
    }
    else if (businessDAO instanceof MdMethodDAOIF)
    {
      return mdMethodOperations;
    }

    return businessDAOoperations;
  }

  /**
   * Return the singleton OperationManager
   *
   * @return The singleton instance of the OperationManager
   */
  public static OperationManager getOperationManager()
  {
    if (operationManager == null)
    {
      operationManager = new OperationManager();
    }

    return operationManager;
  }

  /**
   * Returns the Operation that corresponds to a given id
   *
   * @param id
   *          The id of the Operation
   * @return The corresponding enumeration Operation of a given id
   */
  public static Operation getOperation(String id)
  {
    // Iterate over each operation and check for a match
    for (Operation operation : Operation.values())
    {
      if (id.equals(operation.getId()))
      {
        return operation;
      }
    }

    // No matching operations found
    String error = "Cannot find operation [" + id + "] - the id is not recognized.";
    throw new DataNotFoundException(error, Operation.READ.getMdBusiness());
  }

  /**
   * Returns an array of all Operations that correspond to the provided ids
   *
   * @param ids
   * @return An array of corresponding enumeration Operations for the given ids
   */
  public static Operation[] getOperations(String[] ids)
  {
    Operation[] operations = new Operation[ids.length];
    for (int i = 0; i < ids.length; i++)
    {
      operations[i] = getOperation(ids[i]);
    }

    return operations;
  }

  /**
   * Dereferences the Operations from a Permissions Relationships and gets the
   * operated Java 5 Operation Enumeration.
   *
   * @param relationship
   * @return
   */
  public static Set<Operation> getOperations(RelationshipDAOIF relationship)
  {
    Set<Operation> operations = new TreeSet<Operation>();

    AttributeEnumerationIF attribute = (AttributeEnumerationIF) relationship.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Get a list of all the operations on the enumeration
    BusinessDAOIF[] ops = attribute.dereference();

    for (BusinessDAOIF op : ops)
    {
      // Add the proper operation
      operations.add(OperationManager.getOperation(op.getId()));
    }

    return operations;
  }

  /**
   * Determines if a businessDAO in a list is referring, aka has the same id ,
   * to an Operation
   *
   * @param list
   *          The list of businessDAOs
   * @param o
   *          The operation to compare for
   * @return If the list of businessDAO contains the operation o
   */
  public static boolean containsOperation(BusinessDAOIF[] list, Operation o)
  {

    for (BusinessDAOIF businessDAO : list)
    {
      if (businessDAO.getId().equals(o.getId()))
      {
        return true;
      }
    }

    return false;
  }
}
