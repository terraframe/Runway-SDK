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
package com.runwaysdk.business.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.QueryException;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ViewQueryBuilder;

public class ViewQueryStubAPIGenerator extends ComponentQueryAPIGenerator
{
  public static final String QUERY_API_STUB_SUFFIX = "Query";

  private String             defaultQueryBuilder;

  /**
   * Returns the name of the class that implements the custom query stub API for
   * the given type.
   * 
   * @param mdViewIF
   * @return name of the class that implements the custom query stub API for the
   *         given type.
   */
  protected static String getQueryStubClassName(MdViewDAOIF mdViewIF)
  {
    return mdViewIF.getTypeName() + ViewQueryStubAPIGenerator.QUERY_API_STUB_SUFFIX;
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * stub API for the given type.
   * 
   * @param String
   *          type string.
   * @return qualified name of the class that implements the custom query stub
   *         API for the given type.
   */
  public static String getQueryStubClass(String type)
  {
    return type + ViewQueryStubAPIGenerator.QUERY_API_STUB_SUFFIX;
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * stub API for the given type.
   * 
   * @param mdViewIF
   * @return qualified name of the class that implements the custom query stub
   *         API for the given type.
   */
  public static String getQueryStubClass(MdViewDAOIF mdViewIF)
  {
    return ViewQueryStubAPIGenerator.getQueryStubClass(mdViewIF.definesType());
  }

  /**
   * 
   * @param mdClassIF
   */
  public ViewQueryStubAPIGenerator(MdViewDAOIF mdViewIF)
  {
    super(mdViewIF);
    this.queryTypeName = ViewQueryStubAPIGenerator.getQueryStubClassName(mdViewIF);
    this.defaultQueryBuilder = "Default" + mdViewIF.getTypeName() + "Builder";
  }

  /**
   * Returns the reference to the MdViewDAOIF object that defines the entity
   * type for which this object generates a query API object for.
   * 
   * @return reference to the MdViewDAOIF object that defines the entity type
   *         for which this object generates a query API object for.
   */
  protected MdViewDAOIF getMdClassIF()
  {
    return (MdViewDAOIF) super.getMdClassIF();
  }

  /**
   * Returns a list of the fully qualified paths of the files generated.
   * 
   * @return
   */
  public String getPath()
  {
    return TypeGenerator.getStubQueryAPIsourceFilePath(this.getMdClassIF());
  }

  private void initiateBuffer()
  {
    // First set the base writer
    File stubSrcFile = new File(TypeGenerator.getStubQueryAPIsourceFilePath(this.getMdClassIF()));
    try
    {
      this.srcBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(stubSrcFile)));
    }
    catch (FileNotFoundException e)
    {
      throw new FileWriteException(stubSrcFile, e);
    }
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    super.go(forceRegeneration);

    if (forceRegeneration)
    {
      generate();
      return;
    }

    // This cast is OK, as the mdClass is not modified here, just read.
    AttributeIF stubSource = ( (MdClassDAO) this.getMdClassIF() ).getAttributeIF(MdViewInfo.QUERY_STUB_SOURCE);
    boolean empty = stubSource.getValue().trim().equals("");

    // If the database contains new source, just write that to the file system
    if (stubSource.isModified() && !empty)
    {
      this.initiateBuffer();
      this.writeLine(this.srcBuffer, this.getMdClassIF().getAttributeIF(MdViewInfo.QUERY_STUB_SOURCE).getValue());
      this.closeBuffer();
      return;
    }

    // If we're keeping existing stub source, and a file exists, leave it
    if (LocalProperties.isKeepSource() && new File(getPath()).exists())
    {
      return;
    }

    if (this.getMdClassIF().isNew() || empty)
    {
      generate();
      return;
    }
  }

  private void generate()
  {
    this.initiateBuffer();

    this.addPackage();

    this.addClassName();

    this.addSerialVersionUID();

    this.addConstructor();
    this.addDefaultBuilder();

    this.close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("STUB_QUERY", this.getSignature());
  }

  /**
   * Generates the class name declaration
   */
  protected void addClassName()
  {
    String baseTypeName = ViewQueryBaseAPIGenerator.getQueryBaseClass(this.getMdClassIF());

    // Add a javadoc to the base file that yells at the user to not make changes
    this.writeLine(this.srcBuffer, "/**");
    this.writeLine(this.srcBuffer, " *");
    this.writeLine(this.srcBuffer, " * @author Autogenerated by RunwaySDK");
    this.writeLine(this.srcBuffer, " */");
    this.write(this.srcBuffer, "public class " + this.queryTypeName + " extends " + baseTypeName + " ");

    if (!this.getMdClassIF().isSystemPackage())
    {
      this.write(this.srcBuffer, Reloadable.IMPLEMENTS);
    }
    this.writeLine(this.srcBuffer, "");

    this.writeLine(this.srcBuffer, "{");
  }

  /**
   *
   *
   */
  protected void addConstructor()
  {
    // Constructor for the class
    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + QueryFactory.class.getName() + " queryFactory)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(queryFactory);");
    writeLine(this.srcBuffer, "    this.buildQuery(new " + this.defaultQueryBuilder + "(queryFactory));");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + QueryFactory.class.getName() + " queryFactory, " + ViewQueryBuilder.class.getName() + " viewQueryBuilder)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(queryFactory, viewQueryBuilder);");
    writeLine(this.srcBuffer, "  }");
  }

  /**
   *
   *
   */
  protected void addDefaultBuilder()
  {
    writeLine(this.srcBuffer, "");
    write(this.srcBuffer, "  class " + this.defaultQueryBuilder + " extends " + ViewQueryBuilder.class.getName());

    if (!this.getMdClassIF().isSystemPackage())
    {
      write(this.srcBuffer, " implements " + Reloadable.class.getName());
    }

    writeLine(this.srcBuffer, "");

    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    public " + this.defaultQueryBuilder + "(" + QueryFactory.class.getName() + " queryFactory)");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      super(queryFactory);");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    protected " + this.queryTypeName + " getViewQuery()");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      return (" + this.queryTypeName + ")super.getViewQuery();");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    /**");
    writeLine(this.srcBuffer, "     * build the select clause");
    writeLine(this.srcBuffer, "     */");
    writeLine(this.srcBuffer, "    protected void buildSelectClause()");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      String errMsg = \"buildSelectClause() method in class " + this.defaultQueryBuilder + " needs to be overwritten.\";");
    writeLine(this.srcBuffer, "      throw new " + QueryException.class.getName() + "(errMsg);");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    /**");
    writeLine(this.srcBuffer, "     * Implement only if additional join criteria is required.");
    writeLine(this.srcBuffer, "     */");
    writeLine(this.srcBuffer, "    protected void buildWhereClause()");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  }");
  }

  @Override
  protected void addAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeIF)
  {
  }

  @Override
  protected void addEnumAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeEnumerationIF)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.business.generation.ComponentQueryAPIGenerator#
   * addMultiReferenceAccessor(java.io.BufferedWriter,
   * com.runwaysdk.dataaccess.MdAttributeDAOIF)
   */
  @Override
  protected void addMultiReferenceAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeMultiReferenceIF)
  {
  }

  @Override
  protected void addExtends(MdClassDAOIF parentMdClassIF)
  {
  }

  @Override
  protected void addRefAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeRefIF)
  {
  }

  @Override
  protected void addStructAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeStructIF)
  {
  }

  @Override
  protected void createIteratorMethods()
  {
  }

}
