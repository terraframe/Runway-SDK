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
import java.io.File;
import java.io.IOException;

import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class ImportModel
{
  /**
   * @param args
   * @throws IOException 
   */
  @Request
  public static void main(String[] args) throws IOException
  {
    if (args.length == 1)
    {
      importModel(args[0]);
    }
    else if (args.length == 2)
    {
      importModel(args[0], args[1]);
    }
    else
    {
      String errMessage = 
        "Invalid arguments.  First argument is the location to the domain model xml file.  The second (optional) is the location of the xsd file.";
      throw new RuntimeException(errMessage);
    }
  }
  
  @Transaction
  public static void importModel(String xmlFile) throws IOException
  {       
    SAXImporter.runImport(new File(xmlFile));
  }
  
  @Transaction
  public static void importModel(String xmlFile, String schemaLocation) throws IOException
  {       
    SAXImporter.runImport(new File(xmlFile), schemaLocation);
  }
}
