/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
import com.runwaysdk.session.Request;


public class SolrSandbox
{

  public static void main(String[] args)
  {
    // TODO Auto-generated method stub

    test();
    
  }
  
  @Request
  public static void test()
  {    
//    String url = ServerGISproperties.getSolrGeoEntitiesUrl();
//   
//    System.out.println(url);
//    
//    SolrClient solr = new HttpSolrClient.Builder(url).build();
//    
//    /*
//     * Delete existing data
//     */
//    try
//    {
//      solr.deleteByQuery("*:*");
//    
//      solr.commit();
//    }
//    catch (Exception e)
//    {
//      
//    }
    
/*    
    SolrInputDocument document = new SolrInputDocument();
    document.addField("path", relative.getPath());
    document.addField("filename", file.getName());
    document.addField("text", content + " TV");
    
    solr.add(document);
    
    solr.commit();
*/
    
    
  }
  

}
