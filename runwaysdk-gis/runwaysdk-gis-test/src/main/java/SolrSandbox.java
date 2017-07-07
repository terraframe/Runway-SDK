import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.runwaysdk.gis.constants.ServerGISproperties;
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
