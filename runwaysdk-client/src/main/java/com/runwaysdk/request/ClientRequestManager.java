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
package com.runwaysdk.request;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.XMLConstants;

/**
 * Manages connections through an XML file for RMI and web services.
 */
public class ClientRequestManager
{
  private Logger                       log                = LoggerFactory.getLogger(ClientRequestManager.class);

  /**
   * HashMap to hold mappings between labels and Connection objects.
   */
  private Map<String, ConnectionLabel> connections;

  // /**
  // * The path to the XML file holding the connections.
  // */
  // private static String CONNECTIONS_XML_FILE =
  // ClientProperties.getConnectionsFile();
  //
  // /**
  // * The path to the schema file defining the XML connection file.
  // */
  // private static String CONNECTIONS_SCHEMA_FILE = ClientProperties
  // .getConnectionsSchemaFile();

  /**
   * The string label denoting the default connection for all connections.
   */
  private static String                DEFAULT_LABEL      = "default";

  /**
   * The string denoting the element that represents a connection.
   */
  private static String                CONNECTION_ELEMENT = "connection";

  /**
   * The position of the label.
   */
  private static String                LABEL_ELEMENT      = "label";

  /**
   * The position of the type element.
   */
  private static String                TYPE_ELEMENT       = "type";

  /**
   * The position of the address element.
   */
  private static String                ADDRESS_ELEMENT    = "address";

  /**
   * The single instance of this class.
   */
  public static class Singleton
  {
    public static final ClientRequestManager INSTANCE = new ClientRequestManager();
  }

  /**
   * Constructs a new ConnectionManager object by reading in an xml file
   * detailing connections to servers and then populating a HashMap of
   * Connection objects.
   */
  private ClientRequestManager()
  {
    // initialize the connections and proxies.
    connections = new HashMap<String, ConnectionLabel>();

    URL connectionsXmlFile;
    try
    {
      connectionsXmlFile = ConfigurationManager.getResource(ConfigGroup.CLIENT, "connections.xml");
    }
    catch (RunwayConfigurationException e)
    {
      log.warn("connections.xml configuration file not found.", e);
      return;
    }

    InputStream connectionsSchemaFile;
    try
    {
      connectionsSchemaFile = ConfigurationManager.getResource(ConfigGroup.XSD, "connectionsSchema.xsd").openStream();
    }
    catch (IOException e)
    {
      throw new RunwayConfigurationException(e);
    }

    Document document = null;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);
    factory.setAttribute(XMLConstants.JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA);
    factory.setAttribute(XMLConstants.JAXP_SCHEMA_SOURCE, connectionsSchemaFile);

    DocumentBuilder builder;
    try
    {
      builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new XMLConnectionsErrorHandler());
      document = builder.parse(connectionsXmlFile.openStream());
    }
    catch (ParserConfigurationException e)
    {
      throw new ClientRequestException(e);
    }
    catch (SAXException e)
    {
      throw new ClientRequestException(e);
    }
    catch (IOException e)
    {
      throw new ClientRequestException(e);
    }
    parseDocument(document);
  }

  /**
   * Parses an XML document to extract connection information, ultimately
   * creating Connection objects.
   * 
   * @param document
   */
  private void parseDocument(Document document)
  {
    NodeList connectionsList = document.getElementsByTagName(CONNECTION_ELEMENT);

    // go through each connection
    for (int i = 0; i < connectionsList.getLength(); i++)
    {
      Node connection = connectionsList.item(i);
      NodeList connectionData = connection.getChildNodes();

      // get the data for each connection
      String label = null;
      ConnectionLabel.Type type = null;
      String address = null;

      // we have to loop through all child nodes since whitespace
      // counts as a text node
      for (int j = 0; j < connectionData.getLength(); j++)
      {
        Node data = connectionData.item(j);

        // ignore all \n\t text nodes
        if (data.getNodeType() == Node.TEXT_NODE)
        {
          continue;
        }

        if (data.getNodeName().equals(LABEL_ELEMENT))
        {
          label = data.getTextContent();
        }
        else if (data.getNodeName().equals(TYPE_ELEMENT))
        {
          String typeValue = data.getTextContent();
          type = ConnectionLabel.Type.dereference(typeValue);
        }
        else if (data.getNodeName().equals(ADDRESS_ELEMENT))
        {
          address = data.getTextContent();
        }
      }

      connections.put(label, new ConnectionLabel(label, type, address));
    }
  }

  /**
   * Returns a ClientRequest object associated with the default connection for
   * all connection types.
   * 
   * @return ClientRequest object associated with the default connection.
   */
  public static ConnectionLabel getDefaultConnection()
  {
    return getConnection(DEFAULT_LABEL);
  }

  /**
   * Returns a ClientRequest object that is associated with the specified label.
   * 
   * @param label
   *          The label to uniquely identify a connection.
   * @return The specified Connection object.
   */
  public static ConnectionLabel getConnection(String label)
  {
    // create the singleton instance if needed
    synchronized (ClientRequestManager.class)
    {
      if (Singleton.INSTANCE.connections.containsKey(label))
      {
        return Singleton.INSTANCE.connections.get(label);
      }
      else
      {
        String error = "The client requested a connection to an unknown connection endpoint with the label [" + label + "]";
        throw new ClientRequestException(error);
      }
    }
  }

  /**
   * Adds a new connection label, available for use in methods that create a new
   * session. A programmatic way of modifying connections.xml at runtime. The
   * connection is not saved to the connections.xml file, it is stored in
   * memory.
   * 
   * @param conn
   */
  public static void addConnection(ConnectionLabel conn)
  {
    Singleton.INSTANCE.connections.put(conn.getLabel(), conn);
  }

  /**
   * Error handler for connections.
   */
  private class XMLConnectionsErrorHandler implements ErrorHandler
  {

    public void warning(SAXParseException exception) throws SAXException
    {
      throw new ClientRequestException(exception);
    }

    public void error(SAXParseException exception) throws SAXException
    {
      throw new ClientRequestException(exception);
    }

    public void fatalError(SAXParseException exception) throws SAXException
    {
      throw new ClientRequestException(exception);
    }
  }
}
