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
  READ("567c687f-cd66-3436-af9f-e54517000058", "475d5b55-eb91-3e36-8e49-24ad37000058", false),

  READ_ALL("064c2c3c-677b-3aee-ade1-e37464000058"),
  
  /**
   * Negation of Read permissions for a type
   */
  DENY_READ("475d5b55-eb91-3e36-8e49-24ad37000058", "567c687f-cd66-3436-af9f-e54517000058", true),

  /**
   * Writes the data to a type
   */
  WRITE("e02145d2-afbd-3cec-a330-2ef4b1000058", "cd935282-741f-3880-96f7-d8ac22000058", false),

  WRITE_ALL("46f5b01c-6e0d-3b20-b9a2-e4c8d3000058"),
  
  /**
   * Negation of Write permissions for a type
   */
  DENY_WRITE("cd935282-741f-3880-96f7-d8ac22000058", "e02145d2-afbd-3cec-a330-2ef4b1000058", true),

  /**
   * Alter the state of a type
   */
  PROMOTE("17521de8-c361-301f-ae8d-1da789000058"),

  /**
   * Create a new instance of a type
   */
  CREATE("e3494d59-45b3-3eda-a842-d3e070000058", "8a5d97cd-84c1-3353-b2c2-60ccc3000058", false),

  /**
   * Negation of CREATE permissions for a type
   */
  DENY_CREATE("8a5d97cd-84c1-3353-b2c2-60ccc3000058", "e3494d59-45b3-3eda-a842-d3e070000058", true),

  /**
   * Delete an existing instance of a type
   */
  DELETE("8f3764a5-955d-31f3-b778-2ed465000058", "28688129-e5fb-377c-893a-28035e000058", false),

  /**
   * Negation of DELETE permissions for a type
   */
  DENY_DELETE("28688129-e5fb-377c-893a-28035e000058", "8f3764a5-955d-31f3-b778-2ed465000058", true),

  /**
   * Add a parent a given relationship
   */
  ADD_PARENT("f8de543d-99a7-3913-b26b-33cc71000058"),

  /**
   * Add a child to a given relationship
   */
  ADD_CHILD("e17c9a0d-80f5-3933-bbf4-ea0488000058"),

  /**
   * Delete a parent from a given relationship
   */
  DELETE_PARENT("c6870bf7-6962-3f18-8e4a-e5fb31000058"),

  /**
   * Delete a child from an existing relationship
   */
  DELETE_CHILD("957fc46e-91b5-3b09-8fd0-096c6e000058"),

  /**
   * The relationship object may be written by the user who is the owner of the
   * parent object.
   */
  WRITE_PARENT("ace47a8b-a7fb-3709-b176-4ec403000058"),

  /**
   * The relationship object may be written by the user who is the owner of the
   * child object.
   */
  WRITE_CHILD("79a41560-6a3f-380c-b232-01874d000058"),

  /**
   * The relationship object may be read by the user who is the owner of the
   * parent object.
   */
  READ_PARENT("27a12b26-075a-3681-a227-f0487a000058"),

  /**
   * The relationship object may be read by the user who is the owner of the
   * child object.
   */
  READ_CHILD("227c3e0e-d5a3-336d-85e6-96e424000058"),

  /**
   * Grant permission to a given businessDAO to other users
   */
  GRANT("5d6fb806-1198-3ebb-8e4f-553a4c000058"),

  /**
   * Revoke permissions of another user from a given businessDAO
   */
  REVOKE("1a9a4641-8626-37df-8d4f-52f590000058"),

  /**
   * Execute a Method
   */
  EXECUTE("5607c75d-c3a3-3520-99d5-7de6fa000058");

  private String oid;

  private String negationId;

  private String mdBusinessId;
  
  private Boolean isDeny;

  private Operation(String oid)
  {
    this.oid = oid;
    this.negationId = null;
    this.isDeny = false;
    this.mdBusinessId = "d0b14aa8-5972-33c5-ae62-09d67e00003a";
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
