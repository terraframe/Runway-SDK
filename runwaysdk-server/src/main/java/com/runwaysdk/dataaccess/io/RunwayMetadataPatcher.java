package com.runwaysdk.dataaccess.io;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;
import com.runwaysdk.util.ServerInitializerFacade;

public class RunwayMetadataPatcher
{
  private static Logger logger = LoggerFactory.getLogger(RunwayMetadataPatcher.class);
  
  public static final String RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY = "000000000000000000002";
  // delete from dynamic_properties where id ='000000000000000000002'
  
  private static final String DATE_PATTEN  = "\\d{4,}";
  
  private static final String NAME_PATTERN = "^[A-Za-z_\\-\\d\\.]*\\((" + DATE_PATTEN + ")\\)[A-Za-z_\\-\\d\\.]*.(?:sql|xml|xmli|class)$";
  
  private static final String METADATA_CLASSPATH_LOC = "metadata";
  
  class VersionComparator implements Comparator<ClasspathResource>
  {
    public int compare(ClasspathResource arg0, ClasspathResource arg1)
    {
      return RunwayMetadataPatcher.compare(arg0, arg1);
    }
  }
  
  /**
   * List of timestamps which have already been imported
   */
  protected Set<Date> timestamps;
  
  /**
   * List of all schema resources in the given location in order from earliest to
   * latest
   */
  protected Set<ClasspathResource>       ordered;

  /**
   * Mapping between a resource and its timestamp
   */
  protected Map<Date, ClasspathResource> map;
  
  public RunwayMetadataPatcher()
  {
    initialize();
  }
  
  private void initialize()
  {
    logger.info("Initializing Runway metadata patcher.");
    
    bootstrap();
    
    this.map = new HashMap<Date, ClasspathResource>();
    this.ordered = new TreeSet<ClasspathResource>(new VersionComparator());

    for (ClasspathResource resource : getTimestampedResources(METADATA_CLASSPATH_LOC))
    {
      ordered.add(resource);

      map.put(getDate(resource), resource);
    }

    timestamps = new TreeSet<Date>();

    // Get a list of all the imported versions
    List<String> values = Database.getPropertyValue(RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);

    for (String timestamp : values)
    {
      timestamps.add(new Date(Long.parseLong(timestamp)));
    }
  }
  
  public static void bootstrap()
  {
    if (Database.tableExists("md_class"))
    {
      return;
    }
    
    logger.info("Bootstrapping Runway onto an empty database.");
    
    ClasspathResource metadataRs = new ClasspathResource("(0000000000000001)bootstrap.xmli", METADATA_CLASSPATH_LOC);
    InputStream schema = ClasspathResource.class.getClassLoader().getResourceAsStream("com/runwaysdk/resources/xsd/schema.xsd");
    InputStream metadata = metadataRs.getStream();
    InputStream[] xmlFilesIS = new InputStream[]{metadata};
    
    try
    {
      XMLImporter importer = new XMLImporter(schema, xmlFilesIS);
      importer.toDatabase();

      Database.addPropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(getDate(metadataRs).getTime()).format(), RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);
    }
    finally
    {
      try
      {
        schema.close();
        metadata.close();
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }
  
  protected void performDoIt(ClasspathResource resource, Date timestamp)
  {
    // Only perform the doIt if this file has not already been imported
    if (!timestamps.contains(timestamp))
    {
      logger.info("Importing metadata classpath resource [" + resource.getAbsolutePath() + "].");
      
      Database.addPropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, new TimeFormat(timestamp.getTime()).format(), RUNWAY_METADATA_VERSION_TIMESTAMP_PROPERTY);

      InputStream stream = null;
      
      // Its better if we don't specify the schema that way we're not making any assumptions and the file itself can say what schema it wants.
      
      try
      {
        if (resource.getNameExtension().equals("sql"))
        {
          stream = resource.getStream();
          String sql = IOUtils.toString(stream, "UTF-8");
          
          Database.executeStatement(sql);
        }
        else if (resource.getNameExtension().equals("xml"))
        {
          SAXImporter.runImport(new ResourceStreamSource(resource.getAbsolutePath()), null);
        }
        else if (resource.getNameExtension().equals("xmli"))
        {
          stream = resource.getStream();
          InputStream[] xmlFilesIS = new InputStream[]{stream};
          
          XMLImporter importer = new XMLImporter(null, xmlFilesIS);
          importer.toDatabase();
        }
        else if (resource.getNameExtension().equals("class"))
        {
          // TODO : Run the java file
        }
        else
        {
          throw new CoreException("Unknown extension [" + resource.getNameExtension() + "].");
        }
      }
      catch (IOException e)
      {
        throw new ProgrammingErrorException(e);
      }
      finally
      {
        try {
          if (stream != null)
          {
            stream.close();
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
      
      timestamps.add(timestamp);
    }
  }
  
  @Transaction
  public void performDoIt(List<ClasspathResource> resources)
  {
    for (ClasspathResource resource : resources)
    {
      Date date = getDate(resource);

      this.performDoIt(resource, date);
    }
  }
  
  /**
   * Migrates databases using the legacy patcher system to our new versioned patcher.
   */
//  public void migrateToNewPatcher()
//  {
//    RunwayMetadataVersion version = Database.getMetadataVersion();
//    
//    String sql = null;
//    
//    if (version.toString().equals("1.27.0"))
//    {
//      
//    }
//  }
  
  public void doAll() throws ParseException
  {
    LocalProperties.setSkipCodeGenAndCompile(true);
    
    List<ClasspathResource> list = new LinkedList<ClasspathResource>(ordered);

    this.performDoIt(list);
  }
  
  public static void main(String[] args)
  {
    try
    {
//      RunwayMetadataPatcher.bootstrap();
      RunwayMetadataPatcher.run(args);
    }
    finally
    {
      CacheShutdown.shutdown();
      
    }
  }
  
  @Request
  public static void run(String[] args)
  {
    try
    {
      RunwayMetadataPatcher patcher = new RunwayMetadataPatcher();
      patcher.doAll();
    }
    catch (ParseException e)
    {
      throw new CoreException(e);
    }
  }

  public static Long getTimestamp(ClasspathResource resource)
  {
    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    Matcher nameMatcher = namePattern.matcher(resource.getName());

    if (nameMatcher.find())
    {
      Long timeStamp = Long.parseLong(nameMatcher.group(1));
      return timeStamp;
    }

    return null;
  }

  public static Date getDate(ClasspathResource resource)
  {
    Long timeStamp = getTimestamp(resource);

    if (timeStamp != null)
    {
      return new Date(timeStamp);
    }

    return null;
  }

  public static int compare(ClasspathResource resource1, ClasspathResource resource2)
  {
    return getTimestamp(resource1).compareTo(getTimestamp(resource2));
  }

  public static List<ClasspathResource> getTimestampedResources(String cpPackage)
  {
    List<ClasspathResource> list = new LinkedList<ClasspathResource>();

    Pattern namePattern = Pattern.compile(NAME_PATTERN);
    
    List<ClasspathResource> resources = ClasspathResource.getResourcesInPackage(cpPackage);
    
    for (ClasspathResource resource : resources)
    {
      String name = resource.getName();
      Matcher nameMatcher = namePattern.matcher(name);
      
      if (nameMatcher.find())
      {
        list.add(resource);
      }
    }

    return list;
  }
}
