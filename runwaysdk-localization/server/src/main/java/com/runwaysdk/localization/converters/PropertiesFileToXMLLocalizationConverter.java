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
package com.runwaysdk.localization.converters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXExporter;
import com.runwaysdk.localization.LocalizedValueStore;
import com.runwaysdk.localization.LocalizedValueStoreQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class PropertiesFileToXMLLocalizationConverter
{
  private static final Logger logger = LoggerFactory.getLogger(LocalizedValueStore.class);
  
  private InputStream input;
  
  private OutputStream output;

  private String tagName;
  
  public PropertiesFileToXMLLocalizationConverter()
  {
  }
  
  public PropertiesFileToXMLLocalizationConverter(InputStream input, OutputStream output)
  {
    this.input = input;
    this.output = output;
  }
  
  public static void main(String[] args)
  {
    mainInReq(args);
  }
  
  @Request
  private static void mainInReq(String[] args)
  {
    PropertiesFileToXMLLocalizationConverter converter = new PropertiesFileToXMLLocalizationConverter();
    converter.processCLIArgs(args);
    converter.convert();
  }
  
  protected void processCLIArgs(String[] args)
  {
    CommandLineParser parser = new DefaultParser();
    Options options = new Options();
    options.addOption(Option.builder("propertyFile").hasArg().argName("propertyFile").longOpt("propertyFile").desc("An absolute path to a properties file.").optionalArg(false).build());
    options.addOption(Option.builder("xmlFile").hasArg().argName("xmlFile").longOpt("xmlFile").desc("An absolute path to a target XML file which will be generated.").optionalArg(false).build());
    options.addOption(Option.builder("tagName").hasArg().argName("tagName").longOpt("tagName").desc("The tag name to use for the LocalizedValueStore.").optionalArg(false).build());
    
    try
    {
      CommandLine line = parser.parse( options, args );
      
      String propertyFile = line.getOptionValue("propertyFile");
      String xmlFile = line.getOptionValue("xmlFile");
      
      logger.info("Reading properties file [" + propertyFile + "] and writing to xml file [" + xmlFile + "].");
      
      this.input = new FileInputStream(new File(propertyFile));
      this.output = new FileOutputStream(new File(xmlFile));
      this.tagName = line.getOptionValue("tagName");
    }
    catch (ParseException | FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void convert()
  {
    SAXExporter exporter = new SAXExporter(this.output, "classpath:com/runwaysdk/resources/xsd/datatype.xsd");
    
    exporter.open();
    
    try
    {
      Properties props = new Properties();
      
      props.load(this.input);
      
      Set<Object> keys = props.keySet();
      
      int newProps = 0;
      int replacedProps = 0;
      for (Object key : keys)
      {
        String sKey = (String) key;
        String sValue = props.getProperty(sKey);
        
        if (sValue == null || sValue.length() == 0)
        {
          logger.error("Skipping key [" + sKey + "] because the value is either null or empty.");
          continue;
        }
        
        BusinessDAO storeDAO = BusinessDAOFactory.newInstance(LocalizedValueStore.CLASS);
        storeDAO.setValue(LocalizedValueStore.STOREKEY, sKey);
        storeDAO.setValue(LocalizedValueStore.STORETAG, tagName);
        storeDAO.setStructValue(LocalizedValueStore.STOREVALUE, MdAttributeLocalInfo.DEFAULT_LOCALE, sValue);
        storeDAO.setKey(sKey);
        
//        LocalizedValueStoreQuery storeQuery = new LocalizedValueStoreQuery(new QueryFactory());
//        storeQuery.WHERE(storeQuery.getStoreKey().EQ(sKey));
//        if (storeQuery.getCount() > 0)
//        {
//          exporter.writeUpdate(storeDAO);
//          replacedProps++;
//        }
//        else
//        {
          exporter.writeCreate(storeDAO);
          newProps++;
//        }
      }
      
      logger.info("Generated " + newProps + " new localized objects and updated " + replacedProps + " values.");
    }
    catch (Throwable e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      exporter.close();
    }
  }
}
