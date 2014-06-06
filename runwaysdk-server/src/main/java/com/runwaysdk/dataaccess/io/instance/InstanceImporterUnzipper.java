/**
 * 
 */
package com.runwaysdk.dataaccess.io.instance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.dataaccess.resolver.DefaultConflictResolver;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

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

/**
 * Unzips a directory of zip files into a temporary file, and then runs the InstanceImporter on the temp directory.
 */
public class InstanceImporterUnzipper
{
  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      String msg = "Please include the arguments 1) A directory containing zip file(s)";
      
      throw new RuntimeException(msg);
    }
    
    processZipDir(args[0] + "/universals");
    processZipDir(args[0] + "/geoentities");
    
    // Because we're not exporting the relationship with the root term, we need to do a query here to find all orphaned terms and append them to the root.
    // The reason we're not exporting the relationship with the root term is because the root term does not yet have a predictable id.
    appendOrphansToRoot("com.runwaysdk.system.gis.geo.Universal", "com.runwaysdk.system.gis.geo.AllowedIn");
    appendOrphansToRoot("com.runwaysdk.system.gis.geo.GeoEntity", "com.runwaysdk.system.gis.geo.LocatedIn");
  }
  
  private static void appendOrphansToRoot(String termType, String relType) {
    QueryFactory queryFactory = new QueryFactory();

    RelationshipQuery rq = queryFactory.relationshipQuery(relType);

    BusinessQuery bq = queryFactory.businessQuery(termType);
    bq.WHERE(bq.isNotChildIn(rq));
    
    OIterator<Business> it = bq.getIterator();
    try {
      while (it.hasNext()) {
        Term orphan = (Term) it.next();
        
        orphan.addLink(Term.getRoot(termType), relType);
      }
    }
    finally {
      it.close();
    }
  }
  
  private static void processZipDir(String dir) {
    final File outputDir = new File(dir + "/temp"); 
    
    if (outputDir.exists()) {
      outputDir.delete();
    }
    outputDir.mkdir();
    
    File directory = new File(dir);
    for (File zip : directory.listFiles()) {
      if (zip.getName().endsWith(".gz")) {
        System.out.println("Unzipping " + zip.getAbsolutePath() + " to " + outputDir + ".");
        gunzip(zip, new File(outputDir, zip.getName().substring(0, zip.getName().length() - 3)));
      }
    }
    
    InstanceImporter.runImport(outputDir, (String)null, new DefaultConflictResolver());
  }
  
  private static void gunzip(File zipFile, File extractFile) 
  {
    GZIPInputStream in = null;
    OutputStream out = null;
    try {
       in = new GZIPInputStream(new FileInputStream(zipFile));
       out = new FileOutputStream(extractFile);
       byte[] buf = new byte[1024 * 4];
       int len;
       while ((len = in.read(buf)) > 0) {
           out.write(buf, 0, len);
       }
    }
    catch (IOException e) {
       e.printStackTrace();
    }
    finally
    {
      if (in != null) {
        try
        {
          in.close();
        }
        catch (IOException ignore)
        {
        }
      }
      if (out != null) {
        try
        {
          out.close();
        }
        catch (IOException ignore)
        {
        }
      }
    }
  }
}
