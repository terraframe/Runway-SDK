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
package com.runwaysdk.business.rbac;

import java.io.Serializable;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public enum Operation implements Serializable {
  /**
   * Reads the data from a type
   */
  READ("0000000000000000000000000000053400000000000000000000000000000536", "JS06222010000000000000000000000100000000000000000000000000000536", false),

  READ_ALL("qm4oamyh1j84klvfhrwksxr1ylnyt0dd00000000000000000000000000000536"),
  
  /**
   * Negation of Read permissions for a type
   */
  DENY_READ("JS06222010000000000000000000000100000000000000000000000000000536", "0000000000000000000000000000053400000000000000000000000000000536", true),

  /**
   * Writes the data to a type
   */
  WRITE("0000000000000000000000000000053200000000000000000000000000000536", "JS06222010000000000000000000000200000000000000000000000000000536", false),

  WRITE_ALL("6z2uvfsw02815880iq8vji71v806u25i00000000000000000000000000000536"),
  
  /**
   * Negation of Write permissions for a type
   */
  DENY_WRITE("JS06222010000000000000000000000200000000000000000000000000000536", "0000000000000000000000000000053200000000000000000000000000000536", true),

  /**
   * Alter the state of a type
   */
  PROMOTE("0000000000000000000000000000053000000000000000000000000000000536"),

  /**
   * Create a new instance of a type
   */
  CREATE("0000000000000000000000000000052800000000000000000000000000000536", "JS06222010000000000000000000000300000000000000000000000000000536", false),

  /**
   * Negation of CREATE permissions for a type
   */
  DENY_CREATE("JS06222010000000000000000000000300000000000000000000000000000536", "0000000000000000000000000000052800000000000000000000000000000536", true),

  /**
   * Delete an existing instance of a type
   */
  DELETE("0000000000000000000000000000052700000000000000000000000000000536", "JS06222010000000000000000000000400000000000000000000000000000536", false),

  /**
   * Negation of DELETE permissions for a type
   */
  DENY_DELETE("JS06222010000000000000000000000400000000000000000000000000000536", "0000000000000000000000000000052700000000000000000000000000000536", true),

  /**
   * Add a parent a given relationship
   */
  ADD_PARENT("0000000000000000000000000000071200000000000000000000000000000536"),

  /**
   * Add a child to a given relationship
   */
  ADD_CHILD("0000000000000000000000000000071400000000000000000000000000000536"),

  /**
   * Delete a parent from a given relationship
   */
  DELETE_PARENT("20062108JS000000000000000000078300000000000000000000000000000536"),

  /**
   * Delete a child from an existing relationship
   */
  DELETE_CHILD("20062108JS000000000000000000078100000000000000000000000000000536"),

  /**
   * The relationship object may be written by the user who is the owner of the
   * parent object.
   */
  WRITE_PARENT("20071027NM000000000000000000000100000000000000000000000000000536"),

  /**
   * The relationship object may be written by the user who is the owner of the
   * child object.
   */
  WRITE_CHILD("20071027NM000000000000000000000300000000000000000000000000000536"),

  /**
   * The relationship object may be read by the user who is the owner of the
   * parent object.
   */
  READ_PARENT("20071027NM000000000000000000000500000000000000000000000000000536"),

  /**
   * The relationship object may be read by the user who is the owner of the
   * child object.
   */
  READ_CHILD("20071027NM000000000000000000000700000000000000000000000000000536"),

  /**
   * Grant permission to a given businessDAO to other users
   */
  GRANT("0000000000000000000000000000073200000000000000000000000000000536"),

  /**
   * Revoke permissions of another user from a given businessDAO
   */
  REVOKE("20062108JS000000000000000000077900000000000000000000000000000536"),

  /**
   * Execute a Method
   */
  EXECUTE("JS20070723000000000000000000156600000000000000000000000000000536");

  private String oid;

  private String negationId;

  private String mdBusinessId;
  
  private Boolean isDeny;

  private Operation(String oid)
  {
    this.oid = oid;
    this.negationId = null;
    this.isDeny = false;
    this.mdBusinessId = "000000000000000000000000000005360058";
  }

  private Operation(String oid, String negationId, boolean isDeny)
  {
    this(oid);

    this.negationId = negationId;
    this.isDeny = isDeny;
  }

  public String getOid()
  {
    return oid;
  }

  public String getNegationId()
  {
    return negationId;
  }

  public Operation getNegation()
  {
    if(negationId != null)
    {
     Operation[] operations = Operation.values(); 
     
     for(Operation operation : operations)
     {
       if(operation.getOid().equals(negationId))
       {
         return operation;
       }
     }
    }
    
    return null;
  }
  
  public MdBusinessDAOIF getMdBusiness()
  {
    return MdBusinessDAO.getMdBusinessDAO(this.mdBusinessId);
  }

  public boolean negates(Operation o1)
  {
    if (this.negationId != null && o1.getNegationId() != null)
    {
      return this.getNegationId().equals(o1.getOid()) || this.getOid().equals(o1.getNegationId());
    }

    return false;
  }
  
  public Boolean isDeny()
  {
    return isDeny;
  }
}
