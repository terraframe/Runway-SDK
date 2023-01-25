package com.runwaysdk.build.domain;

import java.util.HashMap;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MigrationUtil;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.ServerIDGenerator;
import com.runwaysdk.dataaccess.graph.GraphDBService;
import com.runwaysdk.dataaccess.graph.GraphRequest;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClass;
import com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClassDAO;
import com.runwaysdk.util.IdParser;

public class EmbeddedGraphClassPatcher
{
  public static void main(String[] args)
  {
    doIt();
  }
  
  @Request
  public static void doIt()
  {
    patchPostgres();
    patchOrientDB();
  }
  
  public static void patchPostgres()
  {
    postgresMetadata();
    postgresMigrateIds();
  }
  
  public static void postgresMetadata()
  {
    ObjectCache.shutdownGlobalCache();
    
    Database.parseAndExecute("UPDATE metadata SET type='com.runwaysdk.system.metadata.graph.MdEmbeddedGraphClass' WHERE key_name='com.runwaysdk.graph.EmbeddedLocalValue';");
    Database.parseAndExecute("INSERT INTO md_embedded_graph_class SELECT oid FROM metadata where key_name='com.runwaysdk.graph.EmbeddedLocalValue';");
    Database.parseAndExecute("DELETE FROM md_vertex WHERE oid=(select oid from metadata where key_name='com.runwaysdk.graph.EmbeddedLocalValue');");
  }
  
  @Transaction
  public static void postgresMigrateIds()
  {
    MdEntityDAOIF newMdClass = MdEntityDAO.getMdEntityDAO(MdEmbeddedGraphClass.CLASS);
    MdEmbeddedGraphClassDAO entityDAO = (MdEmbeddedGraphClassDAO) MdEmbeddedGraphClassDAO.getMdEmbeddedGraphClassDAO("com.runwaysdk.graph.EmbeddedLocalValue");
    
    Attribute keyAttribute = entityDAO.getAttribute(ComponentInfo.KEY);
    String keyValue = keyAttribute.getValue();
    
    String newId = IdParser.buildId(ServerIDGenerator.generateId(keyValue), newMdClass.getRootId());
    
    if (!MigrationUtil.updateEntityDAOId(entityDAO, newId))
    {
      throw new ProgrammingErrorException("Could not migrate MdEmbeddedGraphClass oid.");
    }
    
    entityDAO.apply();
  }
  
  public static void patchOrientDB()
  {
    final String[] removeVertexClasses = new String[] {
        "embedded_local_value",
        "embedded_local_value_cot",
        "ChangeOverTime",
        "EnumChangeOverTime",
        "OLineString_cot",
        "OMultiLineString_cot",
        "OPoint_cot",
        "OMultiPoint_cot",
        "OPolygon_cot",
        "OMultiPolygon_cot",
        "OShape_cot"
    };
    
    GraphDBService service = GraphDBService.getInstance();
    GraphRequest request = service.getGraphDBRequest();
    GraphRequest ddlRequest = service.getDDLGraphDBRequest();
    
    for (String clazz : removeVertexClasses)
    {
      if (service.isClassDefined(ddlRequest, clazz))
      {
        final String osql = "ALTER CLASS " + clazz + " SUPERCLASS -V;";
        service.ddlCommand(request, ddlRequest, osql, new HashMap<String, Object>()).execute();
      }
    }
  }
}
