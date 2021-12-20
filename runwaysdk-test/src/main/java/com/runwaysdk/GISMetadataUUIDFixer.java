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
package com.runwaysdk;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class GISMetadataUUIDFixer
{
  private Map<String, String> ids     = new HashMap<String, String>();

  private Map<String, String> typeIds = new HashMap<String, String>();

  private Pattern             pattern = Pattern.compile("'(/w{64})'");
  
  private int count = 0;

  public void run()
  {
    String dir = "C:/Users/admin/git/Runway-SDK/runwaysdk-server/src/main/resources/com/runwaysdk/resources/metadata/";
    String gisdir = "C:/Users/admin/git/Runway-SDK/runwaysdk-gis/runwaysdk-gis-server/src/main/resources/com/runwaysdk/resources/metadata/";
    
    this.loadRootIds(dir, "metadata.xml.bak");    
    this.loadRootIds(gisdir, "gismetadata.xml.bak");
    this.updateIds(gisdir, "gismetadata.xml.bak", "gismetadata.xml");
    
//    try
//    {
//      this.updateIDs(new File("C:/Users/admin/git/Runway-SDK"));
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }
  
  public void loadRootIds(String dir, String infile)
  {
    try
    {
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
        String uuid = String.format("%06x", ( 0xFFFFFF & count ));

        if(oldId != null && oldId.length() > 0)
        {
          typeIds.put(oldId, uuid);
        }
        else
        {
          System.out.println("No root id found");
        }
        
        count++;
      }
    }
    catch (SAXException | IOException | XPathExpressionException e)
    {
      e.printStackTrace();
    }
  }
  
  public void updateIds(String dir, String infile, String outfile)
  {
    try
    {
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
        String uuid = this.typeIds.get(oldId);
        
        sibling.setTextContent(uuid);
      }
      
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

              // System.out.println("Setting " + tag + " from [" + oldId + "] to
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
        // System.out.println("Updating ids in file: " +
        // file.getAbsolutePath());
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
            String key = oldId.substring(32);
            String value = this.typeIds.get(key);

            UUID uuid = UUID.nameUUIDFromBytes(oldId.getBytes());
            String newId = uuid.toString().substring(0, 32) + value;

            temp.put(oldId, newId);
          }

          for (Entry<String, String> entry : temp.entrySet())
          {
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

    GISMetadataUUIDFixer fixer = new GISMetadataUUIDFixer();
    fixer.run();

//    fixer.fixType();
//    fixer.updateIDs(file);
  }
}
