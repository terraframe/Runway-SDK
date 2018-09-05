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
  public Map<String, String> run()
  {
    String dir = "C:/Users/admin/git/Runway-SDK/runwaysdk-server/src/main/resources/com/runwaysdk/resources/metadata/";
    return this.run(dir, "metadata.xml.bak", "metadata.xml");
  }

  public Map<String, String> run(String dir, String infile, String outfile)
  {
    Map<String, String> ids = new HashMap<String, String>();

    try
    {
      // <attribute>
      // <name>rootId</name>
      // <value>xybe6y9kczkjsi3pdmqmqrjcpfd2frwo</value>
      // <definingComponent>com.runwaysdk.system.metadata.MdType</definingComponent>
      // </attribute>
      NumberFormat format = NumberFormat.getInstance();
      format.setMinimumIntegerDigits(4);
      format.setGroupingUsed(false);

      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(new FileReader(new File(dir, infile))));
      Document doc = parser.getDocument();

      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList results = (NodeList) xPath.compile("//name[.=\"rootId\"]").evaluate(doc, XPathConstants.NODESET);

      Map<String, String> typeIds = new HashMap<String, String>();

      for (int i = 0; i < results.getLength(); i++)
      {
        Node result = results.item(i);
        Node sibling = result.getNextSibling().getNextSibling();

        String oldId = sibling.getTextContent();
        String uuid = format.format(i);

        // System.out.println(result.getNodeName() + " - " +
        // result.getTextContent());
        // System.out.println(oldId + " : " + uuid);

        sibling.setTextContent(uuid);

        typeIds.put(oldId, uuid);
      }

      // Update the database oid size
      results = (NodeList) xPath.compile("//databaseSize[.=\"64\"]").evaluate(doc, XPathConstants.NODESET);

      for (int i = 0; i < results.getLength(); i++)
      {
        Node result = results.item(i);
        boolean isOid = false;

        Node parent = result.getParentNode();
        NodeList children = parent.getChildNodes();

        for (int j = 0; j < children.getLength(); j++)
        {
          Node node = children.item(j);

          if (node instanceof Element)
          {
            Element child = (Element) node;

            if (child.getTagName().equals("attributeName") && child.getTextContent().equals("oid"))
            {
              isOid = true;
            }
          }
        }

        if (isOid)
        {
          result.setTextContent("36");
        }
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

            if (oldId.endsWith(oid))
            {
              UUID uuid = UUID.nameUUIDFromBytes(oldId.getBytes());
              String newId = uuid.toString().substring(0, 32) + value;

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

      StringWriter sw = new StringWriter();
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

    return ids;
  }

  public void updateIDs(Map<String, String> ids, File directory) throws IOException
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
        this.updateIDs(ids, file);
      }
      else
      {
        System.out.println("Updating ids in file: " + file.getAbsolutePath());
        Path path = Paths.get(file.toURI());

        String content = new String(Files.readAllBytes(path), charset);

        for (Entry<String, String> entry : ids.entrySet())
        {
          content = content.replaceAll(entry.getKey(), entry.getValue());
        }

        Files.write(path, content.getBytes(charset));
      }
    }
  }

  public static void main(String[] args) throws IOException
  {
    // new MetadataUUIDFixer().run();

    File file = new File("C:\\Users\\admin\\git\\Runway-SDK");
//    File file = new File("C:\\Users\\admin\\git\\Runway-SDK\\runwaysdk-common\\src\\main\\java\\com\\runwaysdk\\constants");

    MetadataUUIDFixer fixer = new MetadataUUIDFixer();
    Map<String, String> ids = fixer.run();
    fixer.updateIDs(ids, file);
  }
}
