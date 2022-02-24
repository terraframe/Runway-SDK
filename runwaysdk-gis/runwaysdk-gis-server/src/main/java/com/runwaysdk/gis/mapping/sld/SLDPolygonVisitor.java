/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.gis.mapping.sld;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.system.gis.mapping.AndRule;
import com.runwaysdk.system.gis.mapping.CompositeRule;
import com.runwaysdk.system.gis.mapping.ExactRule;
import com.runwaysdk.system.gis.mapping.GreaterThanOrEqualRule;
import com.runwaysdk.system.gis.mapping.GreaterThanRule;
import com.runwaysdk.system.gis.mapping.Layer;
import com.runwaysdk.system.gis.mapping.LayerStyle;
import com.runwaysdk.system.gis.mapping.LessThanOrEqualRule;
import com.runwaysdk.system.gis.mapping.LessThanRule;
import com.runwaysdk.system.gis.mapping.OrRule;
import com.runwaysdk.system.gis.mapping.ThematicAttribute;
import com.runwaysdk.transport.conversion.ConversionException;

public class SLDPolygonVisitor implements SLDVisitorIF
{
  private Stack<Node>              parents;

  private Stack<ThematicAttribute> thematicAttributes;

  private Stack<SLDVisitable>      stack;
  
  private Map<SLDVisitable, Node> nodes;

  private Document                 doc;

  public SLDPolygonVisitor()
  {
    // get the factory and builder to create a Document
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;

    try
    {
      builder = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException e)
    {
      throw new ConversionException(e);
    }

    this.parents = new Stack<Node>();
    this.thematicAttributes = new Stack<ThematicAttribute>();
    this.stack = new Stack<SLDVisitable>();
    this.nodes = new HashMap<SLDVisitable, Node>();

    this.doc = builder.newDocument();

    this.doc.setStrictErrorChecking(false);
    this.doc.setXmlStandalone(true);

    this.push(doc);

    this.push("StyledLayerDescriptor").attr("xmlns", "http://www.opengis.net/sld").attr("xmlns:sld",
        "http://www.opengis.net/sld").attr("xmlns:ogc", "http://www.opengis.net/ogc").attr("xmlns:gml",
        "http://www.opengis.net/gml").attr("version", "1.0.0");
  }

  public String getSLD()
  {
    try
    {
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      return writer.toString();
    }
    catch (TransformerException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private SLDPolygonVisitor push(String element)
  {
    this.push(this.doc.createElement(element));
    return this;
  }

  private SLDPolygonVisitor text(String text)
  {
    this.parents.peek().appendChild(this.doc.createTextNode(text));
    return this;
  }

  private SLDPolygonVisitor push(String element, String text)
  {
    this.push(element);
    this.text(text);
    return this;
  }

  private SLDPolygonVisitor push(String element, String name, String value)
  {
    this.push(element);
    this.attr(name, value);
    return this;
  }
  
  private Node peek()
  {
    return this.parents.peek();
  }

  private SLDPolygonVisitor push(Node node)
  {
    if (this.parents.size() > 0)
    {
      this.parents.peek().appendChild(node);
    }

    this.parents.push(node);
    return this;
  }

  /**
   * Should only be called for Elements.
   * 
   * @param name
   * @param value
   */
  private SLDPolygonVisitor attr(String name, String value)
  {
    ( (Element) this.parents.peek() ).setAttribute(name, value);
    return this;
  }

  private SLDPolygonVisitor pop()
  {
    this.parents.pop();
    return this;
  }

  @Override
  public void visit(Layer layer)
  {
    this.push("sld:NamedLayer");

    this.push("sld:Name").text(layer.getLayerName().getValue()).pop();

    this.push("sld:UserStyle");
    this.push("sld:FeatureTypeStyle");

    this.push("sld:Rule");
    
    this.stack.add(layer);
  }

  @Override
  public void visit(LayerStyle style, SLDVisitable parent)
  {
    // the style can be optional to ignore nulls
    if(style == null)
    {
      return;
    }
    
    // if our parent is a composite then we are done adding the constituent rules
    // so remove the And/Or tag and Filter
    if(parent instanceof CompositeRule)
    {
      this.pop();
      this.pop();
    }
    
    this.push("PolygonSymbolizer");
    this.push("Fill");

    this.push("CssParameter", "name", "fill").text(style.getPolygonFill());

    this.pop(); // Css
    this.pop(); // Fill

    this.push("Stroke");

    this.push("CssParameter", "name", "stroke").text(style.getPolygonStroke()).pop(); // css

    this.push("CssParameter", "name", "stroke-width").text(style.getPolygonStrokeWidth().toString())
        .pop(); // css

    this.pop(); // Stroke

    this.pop(); // polygonSymbolizer
    this.pop(); // sld:Rule/
  }

  @Override
  public void visit(ThematicAttribute thematicAttribute)
  {
    this.thematicAttributes.push(thematicAttribute);

    this.stack.add(thematicAttribute);
  }
  
  @Override
  public void visit(AndRule andRule)
  {
    this.push("sld:Rule");
    this.push("ogc:Filter");
    this.push("ogc:And");
    
    this.nodes.put(andRule, this.peek());
    
    this.stack.add(andRule);
  }

  @Override
  public void visit(OrRule orRule)
  {
    this.push("sld:Rule");
    this.push("ogc:Filter");
    this.push("ogc:Or");
    
    this.nodes.put(orRule, this.peek());

    this.stack.add(orRule);
  }
  
  @Override
  public void visit(ExactRule exactRule, SLDVisitable parent)
  {
    if(parent instanceof ThematicAttribute)
    {
      this.push("sld:Rule");
      this.push("sld:Filter");
    }
    
    this.push("ogc:PropertyIsEqualTo");
    this.push("ogc:PropertyName", this.thematicAttributes.peek().getMdAttribute().getAttributeName())
    .pop();
    
    this.push("ogc:Literal", exactRule.getAttributeValue()).pop();
    
    this.pop();
    
    if(parent instanceof ThematicAttribute)
    {
      this.pop();
    }
  }

  @Override
  public void visit(GreaterThanOrEqualRule gtorRule, SLDVisitable parent)
  {
    if(parent instanceof ThematicAttribute)
    {
      this.push("sld:Rule");
      this.push("sld:Filter");
    }
    
    this.push("ogc:PropertyIsGreaterThanOrEqualTo");
    this.push("ogc:PropertyName", this.thematicAttributes.peek().getMdAttribute().getAttributeName())
        .pop();

    this.push("ogc:Literal", gtorRule.getAttributeValue()).pop();

    this.pop();
    
    if(parent instanceof ThematicAttribute)
    {
      this.pop();
    }
  }

  @Override
  public void visit(GreaterThanRule gtRule, SLDVisitable parent)
  {
    if(parent instanceof ThematicAttribute)
    {
      this.push("sld:Rule");
      this.push("sld:Filter");
    }
    
    this.push("ogc:PropertyIsGreaterThan");

    this.push("ogc:PropertyName", this.thematicAttributes.peek().getMdAttribute().getAttributeName())
        .pop();

    this.push("ogc:Literal", gtRule.getAttributeValue()).pop();

    this.pop();
    
    if(parent instanceof ThematicAttribute)
    {
      this.pop();
    }
  }

  @Override
  public void visit(LessThanOrEqualRule ltoeRule, SLDVisitable parent)
  {
    if(parent instanceof ThematicAttribute)
    {
      this.push("sld:Rule");
      this.push("sld:Filter");
    }
    
    this.push("ogc:PropertyIsLessThanOrEqualTo");

    this.push("ogc:PropertyName", this.thematicAttributes.peek().getMdAttribute().getAttributeName())
        .pop();

    this.push("ogc:Literal", ltoeRule.getAttributeValue()).pop();

    this.pop();
    
    if(parent instanceof ThematicAttribute)
    {
      this.pop();
    }
  }
  
  @Override
  public void visit(LessThanRule ltRule, SLDVisitable parent)
  {
    if(parent instanceof ThematicAttribute)
    {
      this.push("sld:Rule");
      this.push("sld:Filter");
    }
    
    this.push("ogc:PropertyIsLessThan");

    this.push("ogc:PropertyName", this.thematicAttributes.peek().getMdAttribute().getAttributeName())
        .pop();

    this.push("ogc:Literal", ltRule.getAttributeValue()).pop();

    this.pop();
    
    if(parent instanceof ThematicAttribute)
    {
      this.pop();
    }
  }

}
