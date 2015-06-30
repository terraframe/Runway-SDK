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
package com.runwaysdk.business.generation.view;

import java.io.File;
import java.util.HashMap;

import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.io.FileMarkupWriter;
import com.runwaysdk.generation.CommonGenerationUtil;

/**
 * Abstract class which provides a lot of helper methods used to generated .jsps
 * with the Runway tag library
 * 
 * @author Justin Smethie
 */
public abstract class AbstractViewGenerator
{
  /**
   * The output stream to write with
   */
  private FileMarkupWriter      writer;

  /**
   * The package of the file to write
   */
  private String                pack;

  /**
   * The file name of the file to write
   */
  private String                fileName;

  /**
   * The extension of the file to write
   */
  private String                extension;

  private MdEntityDAOIF         mdEntity;

  private static final String   NAMESPACE            = "mjl";

  private static final String   DELIMETER            = ":";

  private static final String   JSTL_NAMESPACE       = "c";

  protected static final String FORM_TAG             = NAMESPACE + DELIMETER + "form";

  protected static final String COMMAND_TAG          = NAMESPACE + DELIMETER + "command";

  protected static final String COMMAND_LINK_TAG     = NAMESPACE + DELIMETER + "commandLink";

  protected static final String SELECT_TAG           = NAMESPACE + DELIMETER + "select";

  protected static final String OPTION_TAG           = NAMESPACE + DELIMETER + "option";

  protected static final String COMPONENT_TAG        = NAMESPACE + DELIMETER + "component";

  protected static final String STRUCT_TAG           = NAMESPACE + DELIMETER + "struct";

  protected static final String INPUT_TAG            = NAMESPACE + DELIMETER + "input";

  protected static final String BOOLEAN_TAG          = NAMESPACE + DELIMETER + "boolean";

  protected static final String MESSAGES_TAG         = NAMESPACE + DELIMETER + "messages";

  protected static final String MESSAGE_TAG          = NAMESPACE + DELIMETER + "message";

  protected static final String CONTAINS_FUNCTION    = NAMESPACE + DELIMETER + "contains";

  protected static final String TABLE_TAG            = NAMESPACE + DELIMETER + "table";

  protected static final String CONTEXT_TAG          = NAMESPACE + DELIMETER + "context";

  protected static final String COLUMNS_TAG          = NAMESPACE + DELIMETER + "columns";

  protected static final String STRUCT_COLUMN_TAG    = NAMESPACE + DELIMETER + "structColumn";

  protected static final String ATTRIBUTE_COLUMN_TAG = NAMESPACE + DELIMETER + "attributeColumn";

  protected static final String FREE_COLUMN_TAG      = NAMESPACE + DELIMETER + "freeColumn";

  protected static final String HEADER_TAG           = NAMESPACE + DELIMETER + "header";

  protected static final String ROW_TAG              = NAMESPACE + DELIMETER + "row";

  protected static final String FOOTER_TAG           = NAMESPACE + DELIMETER + "footer";

  protected static final String PAGINATION_TAG       = NAMESPACE + DELIMETER + "pagination";

  protected static final String PAGE_TAG             = NAMESPACE + DELIMETER + "page";

  protected static final String PROPERTY_TAG         = NAMESPACE + DELIMETER + "property";

  protected static final String RUNWAY_DT_TAG        = NAMESPACE + DELIMETER + "dt";

  protected static final String CHOOSE_TAG           = JSTL_NAMESPACE + DELIMETER + "choose";

  protected static final String WHEN_TAG             = JSTL_NAMESPACE + DELIMETER + "when";

  protected static final String OTHERWISE_TAG        = JSTL_NAMESPACE + DELIMETER + "otherwise";

  protected static final String FOR_EACH_TAG         = JSTL_NAMESPACE + DELIMETER + "forEach";

  protected static final String JSP_NAMESPACE        = "jsp";

  protected static final String INCLUDE_TAG          = JSP_NAMESPACE + DELIMETER + "include";

  protected static final String DL_TAG               = "dl";

  protected static final String DT_TAG               = "dt";

  protected static final String LABEL_TAG            = "label";

  protected static final String DD_TAG               = "dd";

  protected static final String UL_TAG               = "ul";

  protected static final String LI_TAG               = "li";

  protected static final String BR_TAG               = "br";

  protected static final String COMPONENT_SUFFIX     = "Component";

  public static final String    CONTROLLER_SUFFIX    = "Controller";

  /**
   * Constructs a generator to generate a file with the given package and
   * filename.
   * 
   * @param mdEntity
   *          TODO
   * @param fileName
   *          The name of the file to generate
   */
  protected AbstractViewGenerator(MdEntityDAOIF mdEntity, String fileName, String extension)
  {
    this.mdEntity = mdEntity;
    this.pack = mdEntity.definesType();
    this.fileName = fileName;
    this.extension = extension;
  }

  public MdEntityDAOIF getMdEntity()
  {
    return mdEntity;
  }

  /**
   * @return The name of the file generated
   */
  public String getFileName()
  {
    return fileName;
  }

  /**
   * @return The extension of the generated file
   */
  public String getExtension()
  {
    return extension;
  }

  /**
   * Returns the package of the file to generate
   * 
   * @return
   */
  protected String getPackage()
  {
    return pack;
  }

  protected String getFileAndExtension()
  {
    return fileName + "." + extension;
  }

  /**
   * Lazy constructs the output stream to write the file with.
   * 
   * @return
   */
  protected synchronized FileMarkupWriter getWriter()
  {
    if (writer == null)
    {
      writer = new FileMarkupWriter(getDirectory(), getFileName(), getExtension());
    }

    return writer;
  }

  protected void writeIncludes()
  {
    getWriter().writeValue("<%@ taglib uri=\"/WEB-INF/tlds/runwayLib.tld\" prefix=\"mjl\"%>");
    getWriter().writeValue("<%@ taglib uri=\"http://java.sun.com/jsp/jstl/core\" prefix=\"c\" %>");
  }
  
  protected void writeTitle(String title)
  {
    getWriter().writeValue("<c:set var=\"page_title\" scope=\"request\" value=\"" + title + "\"/>");
  }

  protected void writeDocType()
  {
    getWriter().writeValue("<%@ page language=\"java\" contentType=\"text/html; charset=ISO-8859-1\" pageEncoding=\"ISO-8859-1\"%>");
    getWriter().writeValue("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
  }

  protected void writeHTML()
  {
    HashMap<String, String> htmlMap = new HashMap<String, String>();
    htmlMap.put("xmlns", "http://www.w3.org/1999/xhtml");

    getWriter().openEscapedTag("html", htmlMap);
  }

  protected void writeLabel(String label)
  {
    // Open the DT tag
    getWriter().openTag(DT_TAG);

    // Open the label tag
    getWriter().openTag(LABEL_TAG);

    // Write the label
    getWriter().writeValue(label);

    // Close the label tag
    getWriter().closeTag();

    // Close the DT tag
    getWriter().closeTag();
  }

  protected void writerHeader(String title)
  {
    // Write header
    getWriter().openTag("head");

    HashMap<String, String> metaMap = new HashMap<String, String>();
    metaMap.put("http-equiv", "Content-Type");
    metaMap.put("content", "text/html; charset=ISO-8859-1");

    getWriter().writeEmptyEscapedTag("meta", metaMap);

    // Write title tag
    getWriter().openTag("title");
    getWriter().writeValue(title);
    getWriter().closeTag();

    // Close header
    getWriter().closeTag();
  }

  protected void writeCommand(String action, String name, String value)
  {
    HashMap<String, String> updateMap = new HashMap<String, String>();
    updateMap.put("action", action);
    updateMap.put("name", name);
    updateMap.put("value", value);

    getWriter().writeEmptyEscapedTag(COMMAND_TAG, updateMap);
  }

  protected void writeSelect(String items, String var, String param, String attribute)
  {
    writeSelect(items, var, param, attribute, false);
  }

  protected void writeSelect(String items, String var, String param, String attribute, boolean disabled)
  {
    HashMap<String, String> selectMap = new HashMap<String, String>();
    selectMap.put("items", items);
    selectMap.put("var", var);
    selectMap.put("param", param);
    selectMap.put("valueAttribute", attribute);

    if (disabled)
    {
      selectMap.put("disabled", "disabled");
    }

    getWriter().openEscapedTag(SELECT_TAG, selectMap);
  }

  protected void writeMessages()
  {
    getWriter().openTag(MESSAGES_TAG);
    getWriter().writeEmptyTag(MESSAGE_TAG);
    getWriter().closeTag();
  }

  protected void writeMessages(String attribute)
  {
    HashMap<String, String> messagesMap = new HashMap<String, String>();
    messagesMap.put("attribute", attribute);

    getWriter().openEscapedTag(MESSAGES_TAG, messagesMap);
    getWriter().writeEmptyEscapedTag(MESSAGE_TAG, new HashMap<String, String>());
    getWriter().closeTag();
  }

  protected void writeInput(String param, String type)
  {
    writeInput(param, type, false, null);
  }

  protected void writeInput(String param, String type, boolean readonly)
  {
    writeInput(param, type, readonly, null);
  }

  protected void writeInput(String param, String type, String value)
  {
    writeInput(param, type, false, value);
  }

  protected void writeInput(String param, String type, boolean readonly, String value)
  {
    HashMap<String, String> inputMap = new HashMap<String, String>();
    inputMap.put("param", param);
    inputMap.put("type", type);

    if (readonly)
    {
      inputMap.put("readonly", "true");
    }

    if (value != null)
    {
      inputMap.put("value", value);
    }

    getWriter().writeEmptyEscapedTag(INPUT_TAG, inputMap);
  }

  protected void writeBoolean(String param)
  {
    HashMap<String, String> booleanMap = new HashMap<String, String>();
    booleanMap.put("param", param);

    getWriter().writeEmptyEscapedTag(BOOLEAN_TAG, booleanMap);
  }

  protected void writeOption(String value)
  {
    writeOption(value, null);
  }

  protected void writeOption(String value, String selected)
  {
    HashMap<String, String> optionMap = new HashMap<String, String>();

    if (selected != null)
    {
      optionMap.put("selected", selected);
    }

    getWriter().openTag(OPTION_TAG, optionMap);
    getWriter().writeValue(value);
    getWriter().closeTag();
  }

  protected void writeStruct(String param)
  {
    HashMap<String, String> structMap = new HashMap<String, String>();
    structMap.put("param", param);

    getWriter().openEscapedTag(STRUCT_TAG, structMap);
  }

  protected void writeForm(String method, String id, String name)
  {
    HashMap<String, String> formMap = new HashMap<String, String>();
    formMap.put("method", method);
    formMap.put("id", id);
    formMap.put("name", name);

    getWriter().openEscapedTag(FORM_TAG, formMap);
  }

  protected void writeComponent(String item, String param)
  {
    HashMap<String, String> componentMap = new HashMap<String, String>();
    componentMap.put("item", "${" + item + "}");
    componentMap.put("param", param);

    getWriter().openEscapedTag(COMPONENT_TAG, componentMap);
  }

  protected void writeDT(String attributeName)
  {
    HashMap<String, String> componentMap = new HashMap<String, String>();
    componentMap.put("attribute", attributeName);

    getWriter().openEscapedTag(RUNWAY_DT_TAG, componentMap);
  }

  protected void writeTable(String query, String var)
  {
    HashMap<String, String> tableMap = new HashMap<String, String>();
    tableMap.put("query", query);
    tableMap.put("var", var);

    getWriter().openEscapedTag(TABLE_TAG, tableMap);
  }

  protected void writeContex(String action)
  {
    HashMap<String, String> contextMap = new HashMap<String, String>();
    contextMap.put("action", action);

    getWriter().writeEmptyEscapedTag(CONTEXT_TAG, contextMap);
  }

  protected void writeColumns()
  {
    getWriter().openTag(COLUMNS_TAG);
  }

  protected void writeFreeColumn()
  {
    getWriter().openTag(FREE_COLUMN_TAG);
  }

  protected void writeStructColumn(String attributeName)
  {
    HashMap<String, String> attributeMap = new HashMap<String, String>();
    attributeMap.put("attributeName", attributeName);

    getWriter().openEscapedTag(STRUCT_COLUMN_TAG, attributeMap);
  }

  protected void writeAttributeColumn(String attributeName)
  {
    HashMap<String, String> attributeMap = new HashMap<String, String>();
    attributeMap.put("attributeName", attributeName);

    getWriter().openEscapedTag(ATTRIBUTE_COLUMN_TAG, attributeMap);
  }

  protected void writeTableHeader(String value)
  {
    getWriter().openTag(HEADER_TAG);
    getWriter().writeValue(value);
    getWriter().closeTag();
  }

  protected void writeTableRow()
  {
    getWriter().openTag(ROW_TAG);
  }

  protected void writeTableFooter(String value)
  {
    getWriter().openTag(FOOTER_TAG);
    getWriter().writeValue(value);
    getWriter().closeTag();
  }

  protected void writePagination()
  {
    getWriter().openTag(PAGINATION_TAG);
    getWriter().writeEmptyTag(PAGE_TAG);
    getWriter().closeTag();
  }

  protected void writeCommandLinkWithNoProperties(String action, String name, String display)
  {
    HashMap<String, String> commandLinkMap = new HashMap<String, String>();
    commandLinkMap.put("action", action);
    commandLinkMap.put("name", name);

    getWriter().openEscapedTag(COMMAND_LINK_TAG, commandLinkMap);
    getWriter().writeValue(display);
    getWriter().closeTag();
  }

  protected void writeCommandLink(String action, String name, String display)
  {
    HashMap<String, String> commandLinkMap = new HashMap<String, String>();
    commandLinkMap.put("action", action);
    commandLinkMap.put("name", name);

    getWriter().openEscapedTag(COMMAND_LINK_TAG, commandLinkMap);
    getWriter().writeValue(display);
  }

  protected void writeProperty(String name, String value)
  {
    HashMap<String, String> propertyMap = new HashMap<String, String>();
    propertyMap.put("name", name);
    propertyMap.put("value", value);

    getWriter().writeEmptyEscapedTag(PROPERTY_TAG, propertyMap);
  }

  protected void writeInclude(String page)
  {
    HashMap<String, String> includeMap = new HashMap<String, String>();
    includeMap.put("page", page);

    getWriter().writeEmptyEscapedTag(INCLUDE_TAG, includeMap);
  }

  protected void writeInclude(String page, boolean flush)
  {
    HashMap<String, String> includeMap = new HashMap<String, String>();
    includeMap.put("page", page);
    includeMap.put("flush", new Boolean(flush).toString());

    getWriter().writeEmptyEscapedTag(INCLUDE_TAG, includeMap);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.generation.GeneratorIF#getPath()
   */
  public String getPath()
  {
    return this.getDirectory() + getFileName() + "." + getExtension();
  }

  public String getRelativePath()
  {
    return CommonGenerationUtil.replacePackageDotsWithSlashes(getPackage()) + getFileName() + "." + getExtension();
  }

  public String getDirectory()
  {
    String jspDir;

    if (LocalProperties.isDeployedInContainer())
    {
      jspDir = DeployProperties.getJspDir();
    }
    else
    {
      jspDir = LocalProperties.getJspDir();
    }

    return jspDir + "/" + CommonGenerationUtil.replacePackageDotsWithSlashes(getPackage());
  }

  public boolean alreadyExists()
  {
    String path = this.getPath();
    boolean exists = new File(path).exists();

    return exists;
  }
}
