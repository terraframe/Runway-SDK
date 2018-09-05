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
  READ("a406f02d-faf6-3c0a-bc7e-f0663b180088", "2f656424-e386-34ca-9969-4325a4490088", false),

  READ_ALL("8acafd0b-aa11-3501-8287-7f3c2a4e0088"),
  
  /**
   * Negation of Read permissions for a type
   */
  DENY_READ("2f656424-e386-34ca-9969-4325a4490088", "a406f02d-faf6-3c0a-bc7e-f0663b180088", true),

  /**
   * Writes the data to a type
   */
  WRITE("6bb8b23d-1d1d-3ff8-a9cd-62d15ebd0088", "f1694027-3d20-32cf-99b8-b540f09f0088", false),

  WRITE_ALL("2c195ddd-0520-32eb-b1b0-29241eb80088"),
  
  /**
   * Negation of Write permissions for a type
   */
  DENY_WRITE("f1694027-3d20-32cf-99b8-b540f09f0088", "6bb8b23d-1d1d-3ff8-a9cd-62d15ebd0088", true),

  /**
   * Alter the state of a type
   */
  PROMOTE("23c1380c-dc4e-3e15-85f5-9c3bc08f0088"),

  /**
   * Create a new instance of a type
   */
  CREATE("9c05d611-007f-34bb-a1e8-0b30c05d0088", "e24f9c8f-09d9-3b56-9c88-50c73bbd0088", false),

  /**
   * Negation of CREATE permissions for a type
   */
  DENY_CREATE("e24f9c8f-09d9-3b56-9c88-50c73bbd0088", "9c05d611-007f-34bb-a1e8-0b30c05d0088", true),

  /**
   * Delete an existing instance of a type
   */
  DELETE("e0e56cb0-0519-361c-be86-a28c36870088", "80901ebe-a3e3-393e-93bf-6addb6240088", false),

  /**
   * Negation of DELETE permissions for a type
   */
  DENY_DELETE("80901ebe-a3e3-393e-93bf-6addb6240088", "e0e56cb0-0519-361c-be86-a28c36870088", true),

  /**
   * Add a parent a given relationship
   */
  ADD_PARENT("6ea2235f-2f10-3b81-a274-4a40e20a0088"),

  /**
   * Add a child to a given relationship
   */
  ADD_CHILD("e1996339-2c9c-3d95-ba62-b70fd1800088"),

  /**
   * Delete a parent from a given relationship
   */
  DELETE_PARENT("48f2a3a8-17c1-3672-969d-5c6364f30088"),

  /**
   * Delete a child from an existing relationship
   */
  DELETE_CHILD("2c8da293-42e1-3580-abb1-51bbedc60088"),

  /**
   * The relationship object may be written by the user who is the owner of the
   * parent object.
   */
  WRITE_PARENT("db312447-66aa-35ec-8b17-a62733400088"),

  /**
   * The relationship object may be written by the user who is the owner of the
   * child object.
   */
  WRITE_CHILD("63623176-50f7-32e2-ba1f-e909a34a0088"),

  /**
   * The relationship object may be read by the user who is the owner of the
   * parent object.
   */
  READ_PARENT("653d0ffd-1514-3549-b127-1ee9ce270088"),

  /**
   * The relationship object may be read by the user who is the owner of the
   * child object.
   */
  READ_CHILD("cb9e6e5c-0e3b-36bf-a5dc-c60f0f9a0088"),

  /**
   * Grant permission to a given businessDAO to other users
   */
  GRANT("3f12fef3-f5ad-3c50-8d32-aea04a500088"),

  /**
   * Revoke permissions of another user from a given businessDAO
   */
  REVOKE("ba8da233-0fb0-3d15-bda3-50a7f7950088"),

  /**
   * Execute a Method
   */
  EXECUTE("a51dd2b7-a181-3d30-b60c-29aa52fb0088");

  private String oid;

  private String negationId;

  private String mdBusinessId;
  
  private Boolean isDeny;

  private Operation(String oid)
  {
    this.oid = oid;
    this.negationId = null;
    this.isDeny = false;
    this.mdBusinessId = "d4c16b69-a4e4-32fb-84d5-41f78b450058";
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
