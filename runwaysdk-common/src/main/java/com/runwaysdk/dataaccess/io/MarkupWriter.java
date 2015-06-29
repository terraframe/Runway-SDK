/**
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
 */
package com.runwaysdk.dataaccess.io;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * @author terraframe
 *
 */
public abstract class MarkupWriter
{
  /**
   * The amount of spaces for each indent
   */
  private static final int   INDENT_AMOUNT = 2;

  /**
   * The amount of indenting to append before each line
   */
  private int                indent;

  /**
   * The ouput stream writer
   */
  private Writer out;

  /**
   * Tracks the scoping of open tags
   */
  private Stack<String>      tagScope;

  /**
   *
   */
  protected MarkupWriter() {}

  /**
   * @param _out
   */
  public MarkupWriter(Writer _out)
  {
    init(_out);
  }

  protected void init(Writer _out)
  {
    this.out = _out;
    this.indent = 0;
    this.tagScope = new Stack<String>();
  }

  /**
   * Returns a reference to the <code>Writer</code>
   * @return reference to the <code>Writer</code>
   */
  protected Writer getWriter()
  {
    return this.out;
  }

  /** Adds the proper indencencies to a string and calls toStream
   * @param indent the amount to indent
   * @param str the string to be indented
   */
  private void write(String str, int indent)
  //Prints out the stream with number of indent before it
  {
    String in = "";

    //Indent the nessarcy number of levels
    for (int i = 0; i < indent; i++)
    {
      //Indent the proper number of spaces
      for (int j = 0; j < INDENT_AMOUNT; j++)
      {
        in += " ";
      }
    }
    write(in + str);
  }


  /**
   * Writes a string without adding any indentation
   * @param str The string to write
   */
  private void write(String str)
  {
    try
    {
      out.write(str);
      out.flush();
    }
    catch (IOException e)
    {
      throwIOException(e);
    }
  }

  /**
   * Throws custom exception for the given <code>IOException</code> depending
   * on what the output stream is writing to.zz
   */
  protected abstract void throwIOException(IOException e);

  /**
   * Writes an attribute to the out stream
   * @param name The name of the attribute
   * @param value The value of the attribute
   */
  public void writeAttribute(String name, String value)
  {
    write(" " + name + "=\"" + value + "\"");
  }

  /**
   * Writes an attribute to the out stream after printing the indentation.
   * It should be used when every attribute is printed on every new line.
   * It does not add a new line after the attribute. It is the responsibility of the
   * caller to add a new line character after it.
   *
   * @param name
   * @param value
   */
  public void writeAttributeln(String name, String value) {
    write(" " + name + "=\"" + value + "\"", indent+1);

  }

  public void writeEscapedAttribute(String name, String value)
  {
    String escapedValue = escapeValue(value);
    write(" " + name + "=\"" + escapedValue + "\"");
  }

  public static String escapeValue(String value)
  {
    String escaped = value;
    escaped = escaped.replace("&", "&amp;");
    escaped = escaped.replace("\"", "&quot;");
    escaped = escaped.replace("'", "&apos;");
    escaped = escaped.replace("<", "&lt;");
    escaped = escaped.replace(">", "&gt;");
    //      escaped = escaped.replace("\\", "\\\\");
    return escaped;
  }


  /**
   * Writes a tag that has children, but does not have any attributes
   * @param tag The name of the tag to open
   */
  public void openTag(String tag)
  {
    //Make the tag name in scope
    tagScope.push(tag);

    write("<" + tag, indent);

    //Close the opening tag
    write(">" + '\n');

    //Increment the index
    indent++;
  }

  /**
   * Writes a tag with attributes that does not have any children
   * @param tag The name of the tag
   * @param attributes The attributes-value mapping of the tag
   */
  public void writeEmptyTag(String tag, HashMap<String, String> attributes)
  {
    writeEmptyTag("<", "/>", tag, attributes);
  }

  public void writeEmptyTag(String openTag, String closeTag, String tag, HashMap<String, String> attributes)
  {
    write(openTag + tag, indent);

    Set<String> keys = attributes.keySet();

    //Write all of the attributes
    for (String key : keys)
    {
      writeAttribute(key, attributes.get(key));
    }

    //Close the opening tag
    write(" " + closeTag + '\n');
  }

  /**
   * Writes a tag with attributes that does not have any children
   * @param tag The name of the tag
   * @param attributes The attributes-value mapping of the tag
   */
  public void writeEmptyEscapedTag(String tag, HashMap<String, String> attributes)
  {
    write("<" + tag, indent);

    Set<String> keys = attributes.keySet();

    //Write all of the attributes
    for (String key : keys)
    {
      writeEscapedAttribute(key, attributes.get(key));
    }

    //Close the opening tag
    write(" />" + '\n');
  }

  /**
   * Writes a tag without attributes that does not have any children
   * @param tag The name of the tag
   */
  public void writeEmptyTag(String tag)
  {
    write("<" + tag, indent);

    //Close the opening tag
    write(" />" + '\n');
  }

  /**
   * Write a tag with attributes that has childrens
   * @param tag The name of the tag to write
   * @param attributes The attribute-value mappings of the tag
   */
  public void openEscapedTag(String tag, HashMap<String, String> attributes)
  {
    //Make the tag name in scope
    tagScope.push(tag);

    write("<" + tag, indent);

    Set<String> keys = attributes.keySet();

    //Write all of the attributes
    for (String key : keys)
    {
      writeEscapedAttribute(key, attributes.get(key));
    }

    //Close the opening tag
    write(">" + '\n');

    //Increment the index
    indent++;
  }

  /**
   * Write a tag with attributes that has childrens
   * @param tag The name of the tag to write
   * @param attributes The attribute-value mappings of the tag
   */
  public void openTag(String tag, HashMap<String, String> attributes)
  {
    //Make the tag name in scope
    tagScope.push(tag);

    write("<" + tag, indent);

    Set<String> keys = attributes.keySet();

    //Write all of the attributes
    for (String key : keys)
    {
      writeAttribute(key, attributes.get(key));
    }

    //Close the opening tag
    write(">" + '\n');

    //Increment the index
    indent++;
  }

  /**
   * Write a tag with a single text value.
   *
   * @param tag The name of the tag to write
   * @param attributes The attribute-value mappings of the tag
   */
  public void writeTagSingleValue(String tag, String value, HashMap<String, String> attributes)
  {
    write("<" + tag, indent);
    
    Set<String> keys = attributes.keySet();

    //Write all of the attributes
    for (String key : keys)
    {
      writeAttribute(key, attributes.get(key));
    }
    
    write(">" + value + "</" + tag + ">" + '\n');
  }

  /**
   * Write a tag with a single text value.
   *
   * @param tag The name of the tag to write
   * @param attributes The attribute-value mappings of the tag
   */
  public void writeTagSingleValue(String tag, String value)
  {
    write("<" + tag, indent);
    write(">" + value + "</" + tag + ">" + '\n');
  }
  
  public void openTagln(String tag, HashMap<String, String> attributes) {
    tagScope.push(tag);
    write("<"+tag, indent);
    Set<String> keys = attributes.keySet();
    if (!keys.isEmpty()) write("\n");
    Iterator<String> keyIterator = keys.iterator();
    while(keyIterator.hasNext()) {
      String key = keyIterator.next();
      if (keyIterator.hasNext())
      {
        writeAttributeln(key, attributes.get(key));
        write("\n");
      } else {
        writeAttributeln(key, attributes.get(key));
      }
    }
    write(">"+'\n');
    indent++;
  }

  /**
   * Writes the value of a child
   * @param value The value to write
   */
  public void writeCData(String value)
  {
    write("<![CDATA[" + value + "]]>\n", indent);
  }

  public void writeValue(String value)
  {
    write(value + "\n", indent);
  }

  /**
   * Closes the tag in scope
   */
  public void closeTag()
  {
    //decrement the indent counter
    indent--;

    //Get the tag in scope
    String tag = tagScope.pop();

    write("</" + tag + ">" + '\n', indent);
  }

  /**
   * Closes the underlying stream
   */
  public void close()
  {
    try
    {
      out.close();
    }
    catch (IOException e)
    {
      throwIOException(e);
    }
  }
}
