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
package com.runwaysdk.business.generation.json;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.constants.JSON;
import com.runwaysdk.dataaccess.MdTypeDAOIF;

public abstract class JSGenerator
{
  
  public static class Writer
  {
    private StringBuilder builder; 
    
    private int level;
    
    private Writer()
    {
      builder = new StringBuilder();
      level = 0;
    }
    
    public void endEntry(boolean addComma)
    {
      builder.append(addComma ? "," : "");
      builder.append("\n");
    }
    
    public void write(String line)
    {
      String pad = "";
      for(int i=0; i<level; i++)
      {
        pad += "  ";
      }
      
      builder.append(pad+line);
    }
    
    public void writeln()
    {
      builder.append("\n");
    }
    
    public void writeRaw(String line)
    {
      builder.append(line);
    }
    
    public void openBracket()
    {
      write("{");
      level++;
    }
    
    public void openBracketLn()
    {
      openBracket();
      builder.append("\n");
    }
    
    public void closeBracket()
    {
      level--;
      write("}");
    }
    
    public void closeBracketLn()
    {
      closeBracket();
      builder.append("\n");
    }
    
    public String toString()
    {
      return builder.toString();
    }
    
    public void writeln(String line)
    {
      write(line+"\n");
    }
    
    int getLevel()
    {
      return this.level;
    }
  }
  
  protected static class Declaration
  {
    private Writer writer;
    
    
    private Declaration()
    {
      writer = new Writer();
    }
    
    private Declaration(int indent)
    {
      writer = new Writer();
      writer.level = indent;
    }
    
    public void writeln(String line)
    {
      writer.writeln(line);
    }
    
    public void write(String line)
    {
      writer.write(line);
    }
    
    public void openBracket()
    {
      writer.openBracket();
    }
    
    public void closeBracket()
    {
      writer.closeBracket();
    }
    
    public void openBracketLn()
    {
      writer.openBracketLn();
    }
    
    public void closeBracketLn()
    {
      writer.closeBracketLn();
    }
    
    public String toString()
    {
      return writer.toString();
    }
    
    int getLevel()
    {
      return writer.getLevel();
    }
    
  }
  
  /**
   * The namespaced type of the MdTypeIF for javascript.
   */
  private String type;

  /**
   * The MdType for which to generate JSON.
   */
  private MdTypeDAOIF mdTypeIF;
  
  /**
   * The id of the session requesting the javascript class.
   */
  private String sessionId;
  
  private Writer writer;
  
  public JSGenerator(String sessionId, MdTypeDAOIF mdTypeIF)
  {
    this.writer = new Writer();
    this.sessionId = sessionId;
    this.mdTypeIF = mdTypeIF;
    this.type = namespaceType(mdTypeIF.definesType());
  }
  
  /**
   * Returns the class name to be used as the name of the
   * javascript class.
   * 
   * @return
   */
  protected abstract String getClassName();
  
  /**
   * Generates and returns a JSON string
   * @return
   */
  public String getDefinition()
  {
    MdTypeDAOIF md = getMdTypeIF();
    
    String className = getClassName();
    writer.write(JSON.RUNWAY_NEW_CLASS.getLabel()+"('"+className+"', ");
    writer.openBracketLn(); // Start class
    
    /*
    // Alias (attach to $ namespace to avoid clobbering)
    writer.write("Alias : "+JSON.RUNWAY_PACKAGE_NS.getLabel());
    writer.endEntry(true);
    */
    
    
    // Parent (a value of null means the parent is the Base class)
    String parent = getParent();
    if(parent != null)
    {
      writer.write("Extends : '"+parent+"'");
      writer.endEntry(true);
    }
    
    if(isAbstract())
    {
      writer.write("IsAbstract : true");
      writer.endEntry(true);
    }
    
    if(isSingleton())
    {
      writer.write("IsSingleton : true");
      writer.endEntry(true);
    }
    
    writer.writeln("Constants : ");
    writer.openBracketLn();
    
    // Constants
    List<Declaration> constants = this.getConstants();
    if(constants == null)
    {
      constants = new LinkedList<Declaration>();
    }
    
    Declaration classDec = this.newDeclaration();
    
    // All classes have the CLASS constant
    classDec.write("CLASS : '"+md.definesType()+"'");
    constants.add(classDec);
    
    this.writeDeclarations(writer, constants);
    
    writer.closeBracket();
    writer.endEntry(true);    
    
    // Instance methods
    writer.writeln("Instance: ");
    writer.openBracketLn();
    
    List<Declaration> instances = this.getInstanceMethods();
    if(instances == null)
    {
      instances = new LinkedList<Declaration>();
    }
    
    // Every class has an initialize, even if it just supers up.
    Declaration initialize = this.getInitialize();
    instances.add(initialize);
    
    this.writeDeclarations(writer, instances);
    
    writer.closeBracket();
    writer.endEntry(true);    
    
    // Static methods
    writer.writeln("Static: ");
    writer.openBracketLn();
    
    List<Declaration> statics = this.getStaticMethods();
    if(statics == null)
    {
      statics = new LinkedList<Declaration>();
    }
    
    this.writeDeclarations(writer, statics);
    
    writer.closeBracket();
    writer.endEntry(false);    

    // End class
    writer.closeBracket();
    writer.write(");");
    writer.writeln();
    
    postProcess(writer);
    
    return writer.toString();
  }

  protected abstract boolean isAbstract();
  
  protected boolean isSingleton()
  {
    return false;
  }
  
  /**
   * Returns the MdTypeIF for use with generating JSON
   * 
   * @return
   */
  protected MdTypeDAOIF getMdTypeIF()
  {
    return mdTypeIF;
  }
  
  protected void postProcess(Writer writer)
  {
    // do nothing by default
  }
  
  /**
   * Returns the session id that is requesting the javascript class.
   */
  protected String getSessionId()
  {
    return sessionId;
  }

  /**
   * Returns the type prepended with the javascript namespace
   * as specified by JSONProperties.RUNWAY.getLable().
   * 
   * @param type
   * @return
   */
  public static String namespaceType(String type)
  {
    return JSON.RUNWAY_PACKAGE_NS.getLabel()+"."+type;
  }
  
  /**
   * Returns the defined type.
   * 
   * @return
   */
  protected String getType()
  {
    return type;
  }
  
  private void writeDeclarations(Writer writer, List<Declaration> declarations)
  {
    for(int i=0; i< declarations.size(); i++)
    {
      Declaration declaration = declarations.get(i);
      writer.writeRaw(declaration.toString());
     
      // don't write comma on last entry
      writer.endEntry(i != declarations.size()-1);
    }
  }
  
  protected abstract String getParent();
  
  protected abstract List<Declaration> getConstants();
  
  protected Declaration getInitialize()
  {
    Declaration init = this.newDeclaration();
    init.writeln("initialize : function(obj)");
    init.openBracketLn();
    init.writeln("this.$initialize(obj);");
    init.closeBracket();
    
    return init;
  }
  
  protected abstract List<Declaration> getInstanceMethods();
  
  protected abstract List<Declaration> getStaticMethods();
  
  protected Declaration newDeclaration()
  {
    return new Declaration(writer.getLevel());
  }
  
}
