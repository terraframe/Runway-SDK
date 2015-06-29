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
package com.runwaysdk.business.generation;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.util.FileIO;

public class JavaArtifactMdEnumerationCommand extends JavaArtifactMdTypeCommand
{

  public JavaArtifactMdEnumerationCommand(MdEnumerationDAOIF mdTypeIF, Operation operation, Connection conn)
  {
    super(mdTypeIF, operation, conn);
    
    this.businessBaseSourceFile = TypeGenerator.getJavaSrcFilePath(mdTypeIF);
    this.businessBaseClassDiretory  = TypeGenerator.getStubClassDirectory(mdTypeIF);
    this.dtoBaseSourceFile = TypeGenerator.getDTOstubSrcFilePath(mdTypeIF);
    this.dtoBaseClassDirectory = TypeGenerator.getDTOstubClassDirectory(mdTypeIF);
  }

  protected MdEnumerationDAOIF getMdTypeIF()
  {
    return (MdEnumerationDAOIF)super.getMdTypeIF();
  }
  
  /**
   * Deletes the class file and the source file from the filesystem, if they exist.
   *
   */
  protected void deleteMdTypeFiles()
  {
    try
    {
      File baseSourceFile = new File(this.businessBaseSourceFile);
      FileIO.deleteFile(baseSourceFile);
      
      File dtoSourceFile = new File(this.dtoBaseSourceFile);
      FileIO.deleteFile(dtoSourceFile);
      
      String dtoBaseClassType = this.getMdTypeIF().getTypeName() + ComponentDTOGenerator.DTO_SUFFIX;
      ClassManager.deleteClasses(dtoBaseClassDirectory, dtoBaseClassType);
      
      String baseClassType = this.getMdTypeIF().getTypeName();
      ClassManager.deleteClasses(businessBaseClassDiretory, baseClassType);
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }
}
