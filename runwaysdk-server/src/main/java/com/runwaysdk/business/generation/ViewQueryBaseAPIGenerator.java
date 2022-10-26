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
package com.runwaysdk.business.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.ValueObject;
import com.runwaysdk.dataaccess.database.EntityDAOFactory;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.GeneratedViewQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueIterator;
import com.runwaysdk.query.ViewIterator;
import com.runwaysdk.query.ViewQueryBuilder;

public class ViewQueryBaseAPIGenerator extends ComponentQueryAPIGenerator
{
  public static final String QUERY_API_BASE_SUFFIX = "QueryBase";

  /**
   * Returns the name of the class that implements the type safe QueryDTO for
   * the given type.
   * 
   * @param mdTypeIF
   * @return name of the class that implements the type safe QueryDTO for the
   *         given type.
   */
  public static String getQueryClassName(MdTypeDAOIF mdTypeIF)
  {
    return mdTypeIF.getTypeName() + TypeGeneratorInfo.QUERY_DTO_SUFFIX;
  }

  /**
   * Returns the name of the class that implements the custom query base API for
   * the given type.
   * 
   * @param mdViewIF
   * @return name of the class that implements the custom query base API for the
   *         given type.
   */
  protected static String getQueryBaseClassName(MdViewDAOIF mdViewIF)
  {
    return mdViewIF.getTypeName() + ViewQueryBaseAPIGenerator.QUERY_API_BASE_SUFFIX;
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * base API for the given type.
   * 
   * @param mdViewIF
   * @return qualified name of the class that implements the custom query base
   *         API for the given type.
   */
  public static String getQueryBaseClass(MdViewDAOIF mdViewIF)
  {
    return ViewQueryBaseAPIGenerator.getQueryBaseClass(mdViewIF.definesType());
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * base API for the given type.
   * 
   * @param String
   *          type string.
   * @return qualified name of the class that implements the custom query base
   *         API for the given type.
   */
  public static String getQueryBaseClass(String type)
  {
    return type + ViewQueryBaseAPIGenerator.QUERY_API_BASE_SUFFIX;
  }

  /**
   * Returns the qualified name of the class that implements the custom Iterator
   * for the given type.
   * 
   * @param type
   * @return qualified name of the class that implements the custom Iterator for
   *         the given type.
   */
  public static String getIteratorClassCompiled(String type)
  {
    String typeName = EntityDAOFactory.getTypeNameFromType(type);
    return ViewQueryBaseAPIGenerator.getQueryBaseClass(type) + "$" + typeName + "Iterator";
  }

  /**
   * 
   * @param mdClassIF
   */
  public ViewQueryBaseAPIGenerator(MdViewDAOIF mdViewIF)
  {
    super(mdViewIF);
    this.queryTypeName = ViewQueryBaseAPIGenerator.getQueryBaseClassName(mdViewIF);
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // do not generate if the file has already been generated and is
    // semantically equivilant.
    if (LocalProperties.isKeepBaseSource() && AbstractGenerator.hashEquals(this.getSerialVersionUID(), TypeGenerator.getBaseQueryAPIsourceFilePath(this.getMdClassIF())))
    {
      return;
    }

    super.go(forceRegeneration);

    // First set the base writer
    File baseSrcFile = new File(TypeGenerator.getBaseQueryAPIsourceFilePath(this.getMdClassIF()));
    try
    {
      this.srcBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(baseSrcFile), "UTF-8"));
    }
    catch (FileNotFoundException | UnsupportedEncodingException e)
    {
      throw new FileWriteException(baseSrcFile, e);
    }

    this.addPackage();
    this.addSignatureAnnotation();
    this.addClassName();
    this.addSerialVersionUID();
    this.addConstructor();
    this.addGetClassTypeMethod();
    this.addAccessors();

    this.createIteratorMethods();

    this.close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("BASE_QUERY", this.getSignature());
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
    return TypeGenerator.getBaseQueryAPIsourceFilePath(this.getMdClassIF());
  }

  protected String getClassAbstract()
  {
    return " abstract ";
  }

  /**
   * 
   * @param parent
   */
  protected void addExtends(MdClassDAOIF parentMdViewIF)
  {
    if (parentMdViewIF == null)
    {
      writeLine(srcBuffer, GeneratedViewQuery.class.getName());
    }
    else
    {
      writeLine(srcBuffer, ViewQueryStubAPIGenerator.getQueryStubClass( ( parentMdViewIF.definesType() )));
    }
  }

  /**
   *
   *
   */
  protected void addConstructor()
  {
    // Constructor for the class
    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + QueryFactory.class.getName() + " componentQueryFactory)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(componentQueryFactory);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + QueryFactory.class.getName() + " componentQueryFactory, " + ViewQueryBuilder.class.getName() + " viewQueryBuilder)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(componentQueryFactory, viewQueryBuilder);");
    writeLine(this.srcBuffer, "  }");
  }

  /**
   * General case generation of getter for an attribute
   * 
   * @param mdAttributeIF
   *          Attribute to generate accessor methods for
   */
  protected void addAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeIF)
  {
    String attributeClassName = mdAttributeIF.queryAttributeClass();

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeIF);

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");
    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attributeClassName + ")this.getSelectable(" + attribNameConst + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");
    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attributeClassName + ")this.getSelectable(" + attribNameConst + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
    writeLine(bufferedWriter, " ");
  }

  /**
   * Generation of getter for an attribute reference
   * 
   * @param mdAttributeRefIF
   *          Attribute to generate accessor methods for
   */
  protected void addRefAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeRefIF)
  {
    MdAttributeRefDAOIF mdAttributeConcrete = (MdAttributeRefDAOIF) mdAttributeRefIF.getMdAttributeConcrete();

    MdBusinessDAOIF refMdBusinessIF = mdAttributeConcrete.getReferenceMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeRefIF);

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeRefIF.definesAttribute());

    String attrRefName = BusinessQueryAPIGenerator.getRefInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.getSelectable(" + attribNameConst + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.getSelectable(" + attribNameConst + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * Generation of getter for an attribute struct
   * 
   * @param mdAttributeStructIF
   *          Attribute to generate accessor methods for
   */
  protected void addStructAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeStructIF)
  {
    MdAttributeStructDAOIF mdAttributeConcrete = (MdAttributeStructDAOIF) mdAttributeStructIF.getMdAttributeConcrete();

    MdStructDAOIF structMdBusinessIF = mdAttributeConcrete.getMdStructDAOIF();

    if (GenerationUtil.isReservedAndHardcoded(structMdBusinessIF))
    {
      return;
    }

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeStructIF);

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeStructIF.definesAttribute());

    String attrStructName = StructQueryAPIGenerator.getAttrStructInterface(structMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrStructName + ")this.getSelectable(" + attribNameConst + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrStructName + ")this.getSelectable(" + attribNameConst + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");
  }

  /**
   * Generation of getter for an attribute enumeration
   * 
   * @param mdAttributeEnumerationIF
   *          Attribute to generate accessor methods for
   */
  protected void addEnumAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeEnumerationIF)
  {
    MdAttributeEnumerationDAOIF mdAttributeConcrete = (MdAttributeEnumerationDAOIF) mdAttributeEnumerationIF.getMdAttributeConcrete();

    MdBusinessDAOIF masterMdBusinessIF = mdAttributeConcrete.getMdEnumerationDAO().getMasterListMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(masterMdBusinessIF))
    {
      return;
    }

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeEnumerationIF);

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeEnumerationIF.definesAttribute());
    String attrEnumType = BusinessQueryAPIGenerator.getEnumSubInterface(mdAttributeConcrete.getMdEnumerationDAO());

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrEnumType + ")this.getSelectable(" + attribNameConst + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrEnumType + ")this.getSelectable(" + attribNameConst + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
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
    // TODO Auto-generated method stub
  }

  /**
   * Creates emthods that will return type safe iterators of the query result.
   * 
   */
  protected void createIteratorMethods()
  {
    writeLine(this.srcBuffer, "  /**  ");
    writeLine(this.srcBuffer, "   * Returns an iterator of Business objects that match the query criteria specified");
    writeLine(this.srcBuffer, "   * on this query object. ");
    writeLine(this.srcBuffer, "   * @return iterator of Business objects that match the query criteria specified");
    writeLine(this.srcBuffer, "   * on this query object.");
    writeLine(this.srcBuffer, "   */");
    writeLine(this.srcBuffer, "  @" + SuppressWarnings.class.getName() + "(\"unchecked\")");
    writeLine(this.srcBuffer, "  public " + OIterator.class.getName() + "<? extends " + this.getMdClassIF().getTypeName() + ">" + " " + EntityQueryAPIGenerator.ITERATOR_METHOD + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + ValueIterator.class.getName() + " valueIterator;");
    writeLine(this.srcBuffer, "    if (_pageSize != null && _pageNumber != null)");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      valueIterator = (" + ValueIterator.class.getName() + "<" + ValueObject.class.getName() + ">)this.getComponentQuery().getIterator(_pageSize, _pageNumber);");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "    else");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      valueIterator = (" + ValueIterator.class.getName() + "<" + ValueObject.class.getName() + ">)this.getComponentQuery().getIterator();");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "    return new " + ViewIterator.class.getName() + "<" + this.getMdClassIF().getTypeName() + ">(this.getMdClassIF(), valueIterator);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

}
