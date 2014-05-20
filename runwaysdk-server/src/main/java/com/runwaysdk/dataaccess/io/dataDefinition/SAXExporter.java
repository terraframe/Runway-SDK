/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.InvalidArgumentException;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.dataaccess.io.MarkupWriter;
import com.runwaysdk.dataaccess.io.OutputStreamMarkupWriter;

/**
 * Exports non system datatype definition and instances to an xml document that
 * conforms to the datatype.xsd schema
 *
 * @author Justin Smethie
 * @date 6/09/06
 */
public class SAXExporter
{
  /**
   * Writes the XML code
   */
  protected MarkupWriter      writer;
  
  protected MarkupVisitor activeVisitor;

  /**
   * The location of the schema file
   */
  protected String         schemaLocation;

  /**
   * Metadata containing information about which compoments to export.
   */
  protected ExportMetadata metadata;

  /**
   * Constructor Exports metadata to an xml file called file_name.
   *
   * @param fileName
   *            The file_name to write the xml.
   * @param schemaLocation
   *            Either a valid URL, an absolute or relative file path to a file that exists, or an entity on the classpath prefixed with 'classpath:/'.
   * @param metadata
   *            Metadata listing what components to export
   */
  public SAXExporter(String fileName, String schemaLocation, ExportMetadata metadata)
  {
    this.writer = new FileMarkupWriter(fileName);
    this.metadata = metadata;
    
    if (schemaLocation.startsWith("classpath:")) {
      this.schemaLocation = schemaLocation;
      return;
    }
    
    // Is schemaLocation a file?
    try {
      File file = new File(schemaLocation);
      if (file.exists()) {
        this.schemaLocation = file.toURI().toURL().toString();
        return;
      }
    }
    catch (Throwable t)
    {
    }
    
    // Is schemaLocation a URL?
    try
    {
      URL url = new URL(schemaLocation);
      InputStream is = url.openStream();
      is.close();
      this.schemaLocation = url.toString();
      return;
    }
    catch (Throwable t)
    {
      throw new InvalidArgumentException("schemaLocation must reference a valid entity. Valid entities include a URL, an absolute or relative file path, or an entity on the classpath prefixed with 'classpath:/'.", t);
    }
  }
  
  /**
   * Allows for writing large datasets to an OutputStream in a safe fashion, without risking blowing the memory stack.
   * 
   * @param out
   * @param schemaLocation
   */
  public SAXExporter(OutputStream out, String schemaLocation)
  {
    this.schemaLocation = schemaLocation;
    this.writer = new OutputStreamMarkupWriter(out);
  }
  
  public void writeDelete(ComponentIF component) {
    if (activeVisitor == null) {
      activeVisitor = new DeleteVisitor(writer);
      writer.openTag(XMLTags.DELETE_TAG);
    }
    else if (!(activeVisitor instanceof DeleteVisitor)) {
      writer.closeTag();
      
      activeVisitor = new DeleteVisitor(writer);
      writer.openTag(XMLTags.DELETE_TAG);
    }
    
    activeVisitor.visit(component);
  }
  
  public void writeCreate(ComponentIF component) {
    if (activeVisitor == null) {
      activeVisitor = new ExportVisitor(writer, false);
      writer.openTag(XMLTags.CREATE_TAG);
    }
    else if (!(activeVisitor instanceof ExportVisitor)) {
      writer.closeTag();
      
      activeVisitor = new ExportVisitor(writer, false);
      writer.openTag(XMLTags.CREATE_TAG);
    }
    
    activeVisitor.visit(component);
  }
  
  public void writeUpdate(ComponentIF component) {
    if (activeVisitor == null) {
      activeVisitor = new UpdateVisitor(writer, false);
      writer.openTag(XMLTags.UPDATE_TAG);
    }
    else if (!(activeVisitor instanceof UpdateVisitor)) {
      writer.closeTag();
      
      activeVisitor = new UpdateVisitor(writer, false);
      writer.openTag(XMLTags.UPDATE_TAG);
    }
    
    activeVisitor.visit(component);
  }
  
  /**
   * Creates the header of the xml stream.
   */
  public void open()
  {
    HashMap<String, String> attributes = new HashMap<String, String>();

    attributes.put("xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE);
    attributes.put("xsi:noNamespaceSchemaLocation", schemaLocation);

    writer.openEscapedTag("Runway", attributes);
  }
  
  /**
   * Closes the document, writing the final statements and preventing any further components from being written.
   */
  public void close()
  {
    if (activeVisitor != null) {
      writer.closeTag();
      activeVisitor = null;
    }
    
    writer.closeTag();
  }
  
  public void export()
  {
    this.open();
    
    DeleteVisitor deleteVisitor = new DeleteVisitor(writer);
    writer.openTag(XMLTags.DELETE_TAG);
    for (ComponentIF component : metadata.getDeleteList())
    {
      deleteVisitor.visit(component);
    }
    writer.closeTag();
    
    ExportVisitor exportVisitor = new ExportVisitor(writer, metadata);
    writer.openTag(XMLTags.CREATE_TAG);
    for (ComponentIF component : metadata.getCreateList())
    {
      exportVisitor.visit(component);
    }
    writer.closeTag();
    
    UpdateVisitor updateVisitor = new UpdateVisitor(writer, metadata);
    writer.openTag(XMLTags.UPDATE_TAG);
    for (ComponentIF component : metadata.getUpdateList())
    {
      updateVisitor.visit(component);
    }
    writer.closeTag();

    writer.openTag(XMLTags.PERMISSIONS_TAG);

    PermissionVisitor grantVisitor = new GrantVisitor(writer);

    for (ComponentIF component : metadata.getGrantPermissionList())
    {
      grantVisitor.visit(component);
    }

    PermissionVisitor revokeVisitor = new RevokeVisitor(writer);

    for (ComponentIF component : metadata.getRevokePermissionList())
    {
      revokeVisitor.visit(component);
    }

    writer.closeTag();

    this.close();
  }

  public static void export(String fileName, String schemaLocation, ExportMetadata metadata)
  {
    SAXExporter exporter = new SAXExporter(fileName, schemaLocation, metadata);
    exporter.export();
  }
}
