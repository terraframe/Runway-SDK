/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MetadataUUIDFixer
{
  private Map<String, String> ids     = new HashMap<String, String>();

  private Map<String, String> typeIds = new HashMap<String, String>();

  private Pattern             pattern = Pattern.compile("'([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})'");

  public void run()
  {
    String dir = "C:/Users/admin/git/Runway-SDK/runwaysdk-server/src/main/resources/com/runwaysdk/resources/metadata/";
    this.run(dir, "metadata.xml.bak", "metadata.xml");
  }

  public void run(String dir, String infile, String outfile)
  {

    try
    {
      // <attribute>
      // <name>rootId</name>
      // <value>xybe6y9kczkjsi3pdmqmqrjcpfd2frwo</value>
      // <definingComponent>com.runwaysdk.system.metadata.MdType</definingComponent>
      // </attribute>
      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(new FileReader(new File(dir, infile))));
      Document doc = parser.getDocument();

      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList results = (NodeList) xPath.compile("//name[.=\"rootId\"]").evaluate(doc, XPathConstants.NODESET);

      for (int i = 0; i < results.getLength(); i++)
      {
        Node result = results.item(i);
        Node sibling = result.getNextSibling().getNextSibling();

        String oldId = sibling.getTextContent();
        String uuid = String.format("%06x", ( 0xFFFFFF & i ));

        // System.out.println(result.getNodeName() + " - " +
        // result.getTextContent());
        // System.out.println(oldId + " : " + uuid);

        sibling.setTextContent(uuid);

        typeIds.put(oldId, uuid);
      }

      // // Update the database oid size
      // results = (NodeList)
      // xPath.compile("//databaseSize[.=\"64\"]").evaluate(doc,
      // XPathConstants.NODESET);
      //
      // for (int i = 0; i < results.getLength(); i++)
      // {
      // Node result = results.item(i);
      // boolean isOid = false;
      //
      // Node parent = result.getParentNode();
      // NodeList children = parent.getChildNodes();
      //
      // for (int j = 0; j < children.getLength(); j++)
      // {
      // Node node = children.item(j);
      //
      // if (node instanceof Element)
      // {
      // Element child = (Element) node;
      //
      // if (child.getTagName().equals("attributeName") &&
      // child.getTextContent().equals("oid"))
      // {
      // isOid = true;
      // }
      // }
      // }
      //
      // if (isOid)
      // {
      // result.setTextContent("36");
      // }
      // }

      String[] tags = new String[] { "oid", "parent_oid", "child_oid", "value", "createdBy", "lastUpdatedBy", "definingMdClass", "mdStruct", "mdEnumeration", "mdBusiness", "item_id", "owner", "defaultValue" };

      for (String tag : tags)
      {
        results = (NodeList) xPath.compile("//" + tag).evaluate(doc, XPathConstants.NODESET);
        // Update references
        Set<Entry<String, String>> entries = typeIds.entrySet();
        for (Entry<String, String> entry : entries)
        {
          String oid = entry.getKey();
          String value = entry.getValue();

          for (int i = 0; i < results.getLength(); i++)
          {
            Node result = results.item(i);

            String oldId = result.getTextContent();

            if (oldId.length() == 36 && oldId.endsWith(oid) && !this.ids.containsValue(oldId))
            {
              UUID uuid = UUID.nameUUIDFromBytes(oldId.getBytes());
              String newId = uuid.toString().substring(0, 30) + value;

              // System.out.println("Setting " + tag + " from [" + oldId + "]
              // to
              // [" + newId + "]");

              result.setTextContent(newId);

              ids.putIfAbsent(oldId, newId);
            }
          }
        }
      }

      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      // transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
      // "2");

      DOMSource source = new DOMSource(doc);
      transformer.transform(source, new StreamResult(new File(dir, outfile)));

    }
    catch (SAXException | IOException | XPathExpressionException | TransformerException e)
    {
      e.printStackTrace();
    }

    System.out.println();
    System.out.println();
    System.out.println();
    System.out.println();

    for (Entry<String, String> entry : ids.entrySet())
    {
      System.out.println(entry.getKey() + "," + entry.getValue());
    }
  }

  public void updateIDs(File directory) throws IOException
  {
    Charset charset = StandardCharsets.UTF_8;

    File[] files = directory.listFiles(new FileFilter()
    {
      @Override
      public boolean accept(File pathname)
      {
        return pathname.isDirectory() || pathname.getName().endsWith(".java") || pathname.getName().endsWith(".sql");
      }
    });

    for (File file : files)
    {
      if (file.isDirectory())
      {
        this.updateIDs(file);
      }
      else
      {
//        System.out.println("Updating ids in file: " + file.getAbsolutePath());
        Path path = Paths.get(file.toURI());

        String content = new String(Files.readAllBytes(path), charset);

        for (Entry<String, String> entry : ids.entrySet())
        {
          content = content.replaceAll(entry.getKey(), entry.getValue());
        }

        if (file.getName().endsWith(".sql"))
        {
          Matcher matcher = pattern.matcher(content);
          Map<String, String> temp = new HashMap<String, String>();

          while (matcher.find())
          {
            String oldId = matcher.group(1);
            String current = oldId.substring(30);

            if (!this.ids.containsValue(oldId) && !( current.startsWith("0000") || current.startsWith("0001") || current.startsWith("0002") ))
            {
              String key = current.substring(2);
              String value = this.typeIds.get(key);

              if (value != null)
              {
                UUID uuid = UUID.nameUUIDFromBytes(oldId.getBytes());
                String newId = uuid.toString().substring(0, 30) + value;

                temp.put(oldId, newId);
              }
            }
          }

          for (Entry<String, String> entry : temp.entrySet())
          {
            System.out.println("Changing: " + entry.getKey() + " to " + entry.getValue());

            content = content.replaceAll(entry.getKey(), entry.getValue());
          }
        }

        Files.write(path, content.getBytes(charset));
      }
    }
  }

  public void fixType()
  {

    String dir = "C:/Users/admin/git/Runway-SDK/runwaysdk-server/src/main/resources/com/runwaysdk/resources/metadata/";
    String infile = "metadata.xml.bak";
    String outfile = "metadata.xml";

    try
    {
      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(new FileReader(new File(dir, infile))));
      Document doc = parser.getDocument();

      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList results = (NodeList) xPath.compile("//attributeName[.=\"oid\"]").evaluate(doc, XPathConstants.NODESET);

      for (int i = 0; i < results.getLength(); i++)
      {
        Node result = results.item(i);
        Element parent = (Element) result.getParentNode();
        String tagName = parent.getTagName();

        if (tagName.equalsIgnoreCase("mdattributecharacter"))
        {
          doc.renameNode(parent, "", "mdattributeuuid");
        }

        Node type = parent.getElementsByTagName("type").item(0);
        type.setTextContent("com.runwaysdk.system.metadata.MdAttributeUUID");

        Node oid = parent.getElementsByTagName("oid").item(0);
        String oldId = oid.getTextContent();
        String newId = oldId.substring(0, 30) + "000132";

        this.ids.put(oldId, newId);

        oid.setTextContent(newId);

        parent.removeChild(parent.getElementsByTagName("databaseSize").item(0));
        parent.removeChild(parent.getElementsByTagName("defaultValue").item(0));
      }

      String[] tags = new String[] { "parent_oid", "child_oid" };

      for (String tag : tags)
      {
        results = (NodeList) xPath.compile("//" + tag).evaluate(doc, XPathConstants.NODESET);
        // Update references

        for (int i = 0; i < results.getLength(); i++)
        {
          Node result = results.item(i);

          String oldId = result.getTextContent();

          if (this.ids.containsKey(oldId))
          {
            String newId = this.ids.get(oldId);
            result.setTextContent(newId);
          }
        }
      }

      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();

      DOMSource source = new DOMSource(doc);
      transformer.transform(source, new StreamResult(new File(dir, outfile)));

    }
    catch (SAXException | IOException | XPathExpressionException | TransformerException e)
    {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) throws IOException
  {
    // new MetadataUUIDFixer().run();

    File file = new File("C:\\Users\\admin\\git\\Runway-SDK");
    // File file = new
    // File("C:\\Users\\admin\\git\\Runway-SDK\\runwaysdk-common\\src\\main\\java\\com\\runwaysdk\\constants");

    MetadataUUIDFixer fixer = new MetadataUUIDFixer();
    fixer.run();
    fixer.updateIDs(file);

    // fixer.fixType();
    // fixer.updateIDs(file);
  }
}
