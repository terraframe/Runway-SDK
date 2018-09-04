package com.runwaysdk;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

public class MetadataUUIDFixer implements Runnable
{
  @Override
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
      NumberFormat format = NumberFormat.getInstance();
      format.setMinimumIntegerDigits(4);
      format.setGroupingUsed(false);

      DOMParser parser = new DOMParser();
      parser.parse(new InputSource(new FileReader(new File(dir, infile))));
      Document doc = parser.getDocument();

      XPath xPath = XPathFactory.newInstance().newXPath();
      NodeList results = (NodeList) xPath.compile("//name[.=\"rootId\"]").evaluate(doc, XPathConstants.NODESET);

      Map<String, String> ids = new HashMap<String, String>();

      for (int i = 0; i < results.getLength(); i++)
      {
        Node result = results.item(i);
        Node sibling = result.getNextSibling().getNextSibling();

        String oldId = sibling.getTextContent();
        String uuid = format.format(i);

        // System.out.println(result.getNodeName() + " - " +
        // result.getTextContent());
        System.out.println(oldId + " : " + uuid);

        sibling.setTextContent(uuid);

        ids.put(oldId, uuid);
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
        Set<Entry<String, String>> entries = ids.entrySet();
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
              int index = oldId.lastIndexOf(oid);
              String tail = oldId.substring(index).replaceFirst(oid, value);
              String newId = oldId.substring(0, index) + tail;

              System.out.println("Setting " + tag + " from [" + oldId + "] to [" + newId + "]");

              result.setTextContent(newId);
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

  }

  public static void main(String[] args)
  {
    new MetadataUUIDFixer().run();
  }
}
