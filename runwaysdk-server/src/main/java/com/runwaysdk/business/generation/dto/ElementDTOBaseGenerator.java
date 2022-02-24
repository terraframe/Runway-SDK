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
package com.runwaysdk.business.generation.dto;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.generation.ClassStubGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.Type;

public abstract class ElementDTOBaseGenerator extends EntityDTOBaseGenerator
{
  /**
   * @param mdEntity
   */
  public ElementDTOBaseGenerator(MdElementDAOIF mdElementIF)
  {
    super(mdElementIF);  
  }

  @Override
  protected void write()
  {
    super.write();

    writeLock();
    writeUnlock();
  }
  /**
   * Write the method to lock the current object
   */
  private void writeLock()
  {
    getWriter().writeLine("public void lock()");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().lock(this);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    Type returnType = new Type(ClassStubGenerator.getGeneratedType(this.getMdTypeDAOIF()));
    MdParameterDAO oid = GenerationUtil.getMdParameterId();

    List<MdParameterDAOIF> list = new LinkedList<MdParameterDAOIF>();
    list.add(oid);

    writeMdMethod(this.getDTOStubClassType()+".CLASS", list, "lock", returnType, true, false);
  }

  /**
   * Write the method to unlock the current object
   */
  private void writeUnlock()
  {
    getWriter().writeLine("public void unlock()");
    getWriter().openBracket();
    getWriter().writeLine("getRequest().unlock(this);");
    getWriter().closeBracket();
    getWriter().writeLine("");

    Type returnType = new Type(ClassStubGenerator.getGeneratedType(this.getMdTypeDAOIF()));
    MdParameterDAO oid = GenerationUtil.getMdParameterId();

    List<MdParameterDAOIF> list = new LinkedList<MdParameterDAOIF>();
    list.add(oid);

    writeMdMethod(this.getDTOStubClassType()+".CLASS", list, "unlock", returnType, true, false);
  }
}
