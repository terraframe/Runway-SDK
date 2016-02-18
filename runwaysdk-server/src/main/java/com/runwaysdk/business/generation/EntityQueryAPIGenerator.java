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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeFileDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.AttributeEnumeration;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.AttributeMultiReference;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.AttributeStruct;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.Join;
import com.runwaysdk.query.QueryException;

public abstract class EntityQueryAPIGenerator extends ComponentQueryAPIGenerator
{
  public static final String QUERY_API_SUFFIX = "Query";

  public static final String ITERATOR_METHOD  = "getIterator";

  /**
   * Returns the name of the class that implements the custom query API for the
   * given type.
   * 
   * @param mdEntityIF
   * @return name of the class that implements the custom query API for the
   *         given type.
   */
  protected static String getQueryClassName(MdEntityDAOIF mdEntityIF)
  {
    return mdEntityIF.getTypeName() + EntityQueryAPIGenerator.QUERY_API_SUFFIX;
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * API for the given type.
   * 
   * @param mdEntityIF
   * @return qualified name of the class that implements the custom query API
   *         for the given type.
   */
  public static String getQueryClass(MdEntityDAOIF mdEntityIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdEntityIF.definesType());
  }

  /**
   * Returns the qualified name of the class that implements the custom query
   * API for the given type.
   * 
   * @param String
   *          type string.
   * @return qualified name of the class that implements the custom query API
   *         for the given type.
   */
  public static String getQueryClass(String type)
  {
    return type + EntityQueryAPIGenerator.QUERY_API_SUFFIX;
  }

  /**
   * 
   * @param mdEntityIF
   */
  public EntityQueryAPIGenerator(MdEntityDAOIF mdEntityIF)
  {
    super(mdEntityIF);
    this.queryTypeName = EntityQueryAPIGenerator.getQueryClassName(mdEntityIF);
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Check our special cases
    if (GenerationUtil.isSkipCompileAndCodeGeneration(this.getMdClassIF()))
      return;

    // First set the base writer
    File srcFile = new File(TypeGenerator.getQueryAPIsourceFilePath(this.getMdClassIF()));
    try
    {
      this.srcBuffer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(srcFile)));
    }
    catch (FileNotFoundException e)
    {
      throw new FileWriteException(srcFile, e);
    }

    super.go(forceRegeneration);
  }

  /**
   * Returns a list of the fully qualified paths of the files generated.
   * 
   * @return
   */
  public String getPath()
  {
    return TypeGenerator.getQueryAPIsourceFilePath(this.getMdClassIF());
  }

  /**
   * Returns the reference to the MdEntityDAOIF object that defines the entity
   * type for which this object generates a query API object for.
   * 
   * @return reference to the MdEntityDAOIF object that defines the entity type
   *         for which this object generates a query API object for.
   */
  protected MdEntityDAOIF getMdClassIF()
  {
    return (MdEntityDAOIF) super.getMdClassIF();
  }

  /**
   * General case generation of getter for an attribute
   * 
   * @param mdAttributeDAOIF
   *          Attribute to generate accessor methods for
   */
  protected void addAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeDAOIF)
  {
    String attributeClassName = mdAttributeDAOIF.queryAttributeClass();

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeDAOIF.definesAttribute());

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeDAOIF);

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attributeClassName + ")this.getComponentQuery().get(" + attribNameConst + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attributeClassName + ")this.getComponentQuery().get(" + attribNameConst + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * General case generation of getter for an attribute
   * 
   * @param mdAttributeDAOIF
   *          Attribute to generate accessor methods for
   */
  protected void addInnerAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeDAOIF)
  {
    String attributeClassName = mdAttributeDAOIF.queryAttributeClass();

    MdClassDAOIF definingMdClass = mdAttributeDAOIF.definedByClass();

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeDAOIF.definesAttribute());

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attributeClassName + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeDAOIF.definesAttribute().toUpperCase() + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attributeClassName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attributeClassName + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeDAOIF.definesAttribute().toUpperCase() + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * Generation of getter for an attribute reference
   * 
   * @param mdAttributeRefDAOIF
   *          Attribute to generate accessor methods for
   */
  protected void addRefAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeRefDAOIF)
  {
    MdBusinessDAOIF refMdBusinessIF = ( (MdAttributeRefDAOIF) mdAttributeRefDAOIF.getMdAttributeConcrete() ).getReferenceMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeRefDAOIF.definesAttribute());

    String attrRefName = BusinessQueryAPIGenerator.getRefInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeRefDAOIF);

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");

  }

  /**
   * Generation of getter for an attribute reference
   * 
   * @param mdAttributeRefIF
   *          Attribute to generate accessor methods for
   */
  protected void addInnerRefAccessor(BufferedWriter bufferedWriter, MdAttributeRefDAOIF mdAttributeRefIF)
  {
    MdBusinessDAOIF refMdBusinessIF = mdAttributeRefIF.getReferenceMdBusinessDAO();

    MdClassDAOIF definingMdClass = mdAttributeRefIF.definedByClass();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeRefIF.definesAttribute());

    String attrRefName = BusinessQueryAPIGenerator.getRefInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeRefIF.definesAttribute().toUpperCase() + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeRefIF.definesAttribute().toUpperCase() + ",  alias, displayLabel);\n");
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
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrStructName + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrStructName + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * Generation of getter for an attribute struct
   * 
   * @param mdAttributeStructIF
   *          Attribute to generate accessor methods for
   */
  protected void addInnerStructAccessor(BufferedWriter bufferedWriter, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    MdStructDAOIF structMdBusinessIF = mdAttributeStructIF.getMdStructDAOIF();

    MdClassDAOIF definingClass = mdAttributeStructIF.definedByClass();

    if (GenerationUtil.isReservedAndHardcoded(structMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeStructIF.definesAttribute());

    String attrStructName = StructQueryAPIGenerator.getAttrStructInterface(structMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrStructName + ")this.attributeFactory(" + definingClass.definesType() + "." + mdAttributeStructIF.definesAttribute().toUpperCase() + ", " + mdAttributeStructIF.getType() + ".CLASS, alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrStructName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrStructName + ")this.attributeFactory(" + definingClass.definesType() + "." + mdAttributeStructIF.definesAttribute().toUpperCase() + ", " + mdAttributeStructIF.getType() + ".CLASS, alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * Generation of getter for an attribute enumeration
   * 
   * @param mdAttributeEnumerationIF
   *          Attribute to generate accessor methods for
   */
  protected void addMultiReferenceAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeMultiReferenceDAOIF)
  {
    MdBusinessDAOIF refMdBusinessIF = ( (MdAttributeMultiReferenceDAOIF) mdAttributeMultiReferenceDAOIF.getMdAttributeConcrete() ).getReferenceMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeMultiReferenceDAOIF.definesAttribute());

    String attributeName = BusinessQueryAPIGenerator.getMultiReferenceInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attributeName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeMultiReferenceDAOIF);

    writeLine(bufferedWriter, "  public " + attributeName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attributeName + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attributeName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attributeName + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");

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

    // String attributeName = mdAttributeEnumerationIF.definesAttribute();

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeEnumerationIF.definesAttribute());
    String attrEnumType = BusinessQueryAPIGenerator.getEnumSubInterface(mdAttributeConcrete.getMdEnumerationDAO());

    String attribNameConst = TypeGenerator.buildAttributeConstant(this.getMdClassIF(), mdAttributeEnumerationIF);

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");

    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrEnumType + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    " + MdAttributeDAOIF.class.getName() + " mdAttributeIF = this.getComponentQuery().getMdAttributeROfromMap(" + attribNameConst + ");");
    writeLine(bufferedWriter, "");
    writeLine(bufferedWriter, "    return (" + attrEnumType + ")this.getComponentQuery().internalAttributeFactory(" + attribNameConst + ", mdAttributeIF, this, alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * Generation of getter for an attribute enumeration
   * 
   * @param mdAttributeEnumerationIF
   *          Attribute to generate accessor methods for
   */
  protected void addInnerEnumAccessor(BufferedWriter bufferedWriter, MdAttributeEnumerationDAOIF mdAttributeEnumerationIF)
  {
    MdBusinessDAOIF masterMdBusinessIF = mdAttributeEnumerationIF.getMdEnumerationDAO().getMasterListMdBusinessDAO();

    MdClassDAOIF definingMdClass = mdAttributeEnumerationIF.definedByClass();

    if (GenerationUtil.isReservedAndHardcoded(masterMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeEnumerationIF.definesAttribute());
    String attrEnumType = BusinessQueryAPIGenerator.getEnumSubInterface(mdAttributeEnumerationIF.getMdEnumerationDAO());

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrEnumType + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeEnumerationIF.definesAttribute().toUpperCase() + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrEnumType + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeEnumerationIF.definesAttribute().toUpperCase() + ", alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   * Generation of getter for an attribute enumeration
   * 
   * @param mdAttributeEnumerationIF
   *          Attribute to generate accessor methods for
   */
  protected void addInnerMultiReferenceAccessor(BufferedWriter bufferedWriter, MdAttributeMultiReferenceDAOIF mdAttributeMultiReferenceIF)
  {
    MdBusinessDAOIF refMdBusinessIF = mdAttributeMultiReferenceIF.getReferenceMdBusinessDAO();

    MdClassDAOIF definingMdClass = mdAttributeMultiReferenceIF.definedByClass();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeMultiReferenceIF.definesAttribute());

    String attrRefName = BusinessQueryAPIGenerator.getRefInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "()");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return " + accessorName + "(null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeMultiReferenceIF.definesAttribute().toUpperCase() + ", alias, null);\n");
    writeLine(bufferedWriter, "  }");

    writeLine(bufferedWriter, " ");

    writeLine(bufferedWriter, "  public " + attrRefName + " " + accessorName + "(String alias, String displayLabel)");
    writeLine(bufferedWriter, "  {");
    writeLine(bufferedWriter, "    return (" + attrRefName + ")this.get(" + definingMdClass.definesType() + "." + mdAttributeMultiReferenceIF.definesAttribute().toUpperCase() + ",  alias, displayLabel);\n");
    writeLine(bufferedWriter, "  }");
  }

  /**
   *
   *
   */
  protected abstract void addConstructors();

  /**
   * Creates a factory that creates subclasses of AttributeReference.
   * 
   */
  protected void createAttributeRefFactory(BufferedWriter bufferedWriter)
  {
    List<MdAttributeRefDAOIF> mdAttributeRefList = new LinkedList<MdAttributeRefDAOIF>();

    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (mdAttributeIF instanceof MdAttributeReferenceDAOIF)
      {
        MdBusinessDAOIF referenceMdBusinessIF = ( (MdAttributeReferenceDAOIF) mdAttributeIF ).getReferenceMdBusinessDAO();

        if (!GenerationUtil.isReservedAndHardcoded(referenceMdBusinessIF))
        {
          mdAttributeRefList.add((MdAttributeRefDAOIF) mdAttributeIF);
        }
      }
      else if (mdAttributeIF instanceof MdAttributeFileDAOIF)
      {
        mdAttributeRefList.add((MdAttributeFileDAOIF) mdAttributeIF);
      }
    }

    if (mdAttributeRefList.size() > 0)
    {
      String methodName = "referenceFactory";

      String parameterString = "((" + MdAttributeRefDAOIF.class.getName() + ")mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);";

      write(bufferedWriter, "  protected " + AttributeReference.class.getName() + " " + methodName + "(");
      write(bufferedWriter, " " + MdAttributeRefDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
      writeLine(bufferedWriter, " " + MdBusinessDAOIF.class.getName() + " referenceMdBusinessIF, String referenceTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)");
      writeLine(bufferedWriter, "  {");
      writeLine(bufferedWriter, "    String name = mdAttributeIF.definesAttribute();");
      writeLine(bufferedWriter, "    ");

      for (int i = 0; i < mdAttributeRefList.size(); i++)
      {
        MdAttributeRefDAOIF mdAttributeRefIF = mdAttributeRefList.get(i);

        if (i == 0)
        {
          write(bufferedWriter, "    if ");
        }
        else
        {
          write(bufferedWriter, "    else if ");
        }

        writeLine(bufferedWriter, "(name.equals(" + this.getMdClassIF().definesType() + "." + mdAttributeRefIF.definesAttribute().toUpperCase() + ")) ");
        writeLine(bufferedWriter, "    {");

        MdBusinessDAOIF referenceMdBusinessIF = null;

        referenceMdBusinessIF = mdAttributeRefIF.getReferenceMdBusinessDAO();

        String attributeReferenceName = BusinessQueryAPIGenerator.getRefClass(referenceMdBusinessIF);

        writeLine(bufferedWriter, "       return new " + attributeReferenceName + parameterString);
        writeLine(bufferedWriter, "    }");
      }

      writeLine(bufferedWriter, "    else ");
      writeLine(bufferedWriter, "    {");
      if (this.getMdClassIF().getSuperClass() == null)
      {
        writeLine(bufferedWriter, "      String error = \"Attribute type [\"+mdAttributeIF.getType()+\"] is invalid.\";");
        writeLine(bufferedWriter, "      throw new " + QueryException.class.getName() + "(error);");
      }
      else
      {
        writeLine(bufferedWriter, "      return super." + methodName + "(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);");
      }

      writeLine(bufferedWriter, "    }");

      writeLine(bufferedWriter, "  }\n");
    }

  }

  /**
   * Creates a factory that creates subclasses of AttributeStruct.
   * 
   */
  protected void createAttributeStructFactory(BufferedWriter bufferedWriter)
  {
    List<MdAttributeStructDAOIF> mdAttrStructList = new LinkedList<MdAttributeStructDAOIF>();

    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (mdAttributeIF instanceof MdAttributeStructDAOIF && ! ( mdAttributeIF instanceof MdAttributeLocalDAOIF ))
      {
        MdStructDAOIF structMdBusinessIF = ( (MdAttributeStructDAOIF) mdAttributeIF ).getMdStructDAOIF();

        if (!GenerationUtil.isReservedAndHardcoded(structMdBusinessIF))
        {
          mdAttrStructList.add((MdAttributeStructDAOIF) mdAttributeIF);
        }
      }
    }

    if (mdAttrStructList.size() > 0)
    {
      String methodName = "structFactory";

      String parameterString = "((" + MdAttributeStructDAOIF.class.getName() + ")mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);";

      write(bufferedWriter, "  protected " + AttributeStruct.class.getName() + " " + methodName + "(");
      write(bufferedWriter, " " + MdAttributeStructDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
      writeLine(bufferedWriter, " " + MdStructDAOIF.class.getName() + " mdStructIF, String structTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)");
      writeLine(bufferedWriter, "  {");
      writeLine(bufferedWriter, "    String name = mdAttributeIF.definesAttribute();");
      writeLine(bufferedWriter, "    ");

      for (int i = 0; i < mdAttrStructList.size(); i++)
      {
        MdAttributeStructDAOIF mdAttributeStructIF = mdAttrStructList.get(i);

        if (i == 0)
        {
          write(bufferedWriter, "    if ");
        }
        else
        {
          write(bufferedWriter, "    else if ");
        }

        writeLine(bufferedWriter, "(name.equals(" + this.getMdClassIF().definesType() + "." + mdAttributeStructIF.definesAttribute().toUpperCase() + ")) ");
        writeLine(bufferedWriter, "    {");

        MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();
        String attrStructClass = StructQueryAPIGenerator.getAttrStructClass(mdStructIF);

        writeLine(bufferedWriter, "       return new " + attrStructClass + parameterString);
        writeLine(bufferedWriter, "    }");
      }

      writeLine(bufferedWriter, "    else ");
      writeLine(bufferedWriter, "    {");
      if (this.getMdClassIF().getSuperClass() == null)
      {
        writeLine(bufferedWriter, "      String error = \"Attribute type [\"+mdAttributeIF.getType()+\"] is invalid.\";");
        writeLine(bufferedWriter, "      throw new " + QueryException.class.getName() + "(error);");
      }
      else
      {
        writeLine(bufferedWriter, "      return super." + methodName + "(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);");
      }

      writeLine(bufferedWriter, "    }");

      writeLine(bufferedWriter, "  }\n");
    }

  }

  /**
   * Creates a factory that creates subclasses of AttributeStruct.
   * 
   */
  protected void createAttributeLocalFactory(BufferedWriter bufferedWriter)
  {
    List<MdAttributeLocalDAOIF> mdAttrLocalList = new LinkedList<MdAttributeLocalDAOIF>();

    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (mdAttributeIF instanceof MdAttributeLocalDAOIF)
      {
        MdStructDAOIF structMdBusinessIF = ( (MdAttributeStructDAOIF) mdAttributeIF ).getMdStructDAOIF();

        if (!GenerationUtil.isReservedAndHardcoded(structMdBusinessIF))
        {
          mdAttrLocalList.add((MdAttributeLocalDAOIF) mdAttributeIF);
        }
      }
    }

    if (mdAttrLocalList.size() > 0)
    {
      String methodName = "localFactory";

      String parameterString = "((" + MdAttributeLocalDAOIF.class.getName() + ")mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);";

      write(bufferedWriter, "  protected " + AttributeLocal.class.getName() + " " + methodName + "(");
      write(bufferedWriter, " " + MdAttributeLocalDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
      writeLine(bufferedWriter, " " + MdLocalStructDAOIF.class.getName() + " mdLocalStructIF, String structTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)");
      writeLine(bufferedWriter, "  {");
      writeLine(bufferedWriter, "    String name = mdAttributeIF.definesAttribute();");
      writeLine(bufferedWriter, "    ");

      for (int i = 0; i < mdAttrLocalList.size(); i++)
      {
        MdAttributeLocalDAOIF mdAttributeLocalStructIF = mdAttrLocalList.get(i);

        if (i == 0)
        {
          write(bufferedWriter, "    if ");
        }
        else
        {
          write(bufferedWriter, "    else if ");
        }

        writeLine(bufferedWriter, "(name.equals(" + this.getMdClassIF().definesType() + "." + mdAttributeLocalStructIF.definesAttribute().toUpperCase() + ")) ");
        writeLine(bufferedWriter, "    {");

        MdLocalStructDAOIF mdLocalStructIF = mdAttributeLocalStructIF.getMdStructDAOIF();
        String attrLocalStructClass = StructQueryAPIGenerator.getAttrStructClass(mdLocalStructIF);

        writeLine(bufferedWriter, "       return new " + attrLocalStructClass + parameterString);
        writeLine(bufferedWriter, "    }");
      }

      writeLine(bufferedWriter, "    else ");
      writeLine(bufferedWriter, "    {");
      if (this.getMdClassIF().getSuperClass() == null)
      {
        writeLine(bufferedWriter, "      String error = \"Attribute type [\"+mdAttributeIF.getType()+\"] is invalid.\";");
        writeLine(bufferedWriter, "      throw new " + QueryException.class.getName() + "(error);");
      }
      else
      {
        writeLine(bufferedWriter, "      return super." + methodName + "(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdLocalStructIF, structTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);");
      }

      writeLine(bufferedWriter, "    }");

      writeLine(bufferedWriter, "  }\n");
    }

  }

  /**
   * Creates a factory that creates subclasses of AttributeEnumeration.
   * 
   */
  protected void createAttributeEnumerationFactory(BufferedWriter bufferedWriter)
  {
    List<MdAttributeEnumerationDAOIF> mdAttrEnumList = new LinkedList<MdAttributeEnumerationDAOIF>();

    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        MdBusinessDAOIF masterMdBusinessIF = ( (MdAttributeEnumerationDAOIF) mdAttributeIF ).getMdEnumerationDAO().getMasterListMdBusinessDAO();

        if (!GenerationUtil.isReservedAndHardcoded(masterMdBusinessIF))
        {
          mdAttrEnumList.add((MdAttributeEnumerationDAOIF) mdAttributeIF);
        }
      }
    }

    if (mdAttrEnumList.size() > 0)
    {
      String methodName = "enumerationFactory";

      String parameterString = "((" + MdAttributeEnumerationDAOIF.class.getName() + ")mdAttributeIF,  attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);";

      write(bufferedWriter, "  protected " + AttributeEnumeration.class.getName() + " " + methodName + "(");
      write(bufferedWriter, " " + MdAttributeEnumerationDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
      writeLine(bufferedWriter, " String mdEnumerationTableName, " + MdBusinessDAOIF.class.getName() + " masterListMdBusinessIF, String masterListTalbeAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)");
      writeLine(bufferedWriter, "  {");
      writeLine(bufferedWriter, "    String name = mdAttributeIF.definesAttribute();");
      writeLine(bufferedWriter, "    ");

      for (int i = 0; i < mdAttrEnumList.size(); i++)
      {
        MdAttributeEnumerationDAOIF mdAttributeEnumerationIF = mdAttrEnumList.get(i);

        if (i == 0)
        {
          write(bufferedWriter, "    if ");
        }
        else
        {
          write(bufferedWriter, "    else if ");
        }

        writeLine(bufferedWriter, "(name.equals(" + this.getMdClassIF().definesType() + "." + mdAttributeEnumerationIF.definesAttribute().toUpperCase() + ")) ");
        writeLine(bufferedWriter, "    {");

        String attrEnumClass = BusinessQueryAPIGenerator.getEnumSubClass(mdAttributeEnumerationIF.getMdEnumerationDAO());

        writeLine(bufferedWriter, "       return new " + attrEnumClass + parameterString);
        writeLine(bufferedWriter, "    }");
      }

      writeLine(bufferedWriter, "    else ");
      writeLine(bufferedWriter, "    {");
      if (this.getMdClassIF().getSuperClass() == null)
      {
        writeLine(bufferedWriter, "      String error = \"Attribute type [\"+mdAttributeIF.getType()+\"] is invalid.\";");
        writeLine(bufferedWriter, "      throw new " + QueryException.class.getName() + "(error);");
      }
      else
      {
        writeLine(bufferedWriter, "      return super." + methodName + "(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterListMdBusinessIF, masterListTalbeAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);");
      }

      writeLine(bufferedWriter, "    }");

      writeLine(bufferedWriter, "  }\n");
    }

  }

  /**
   * Creates a factory that creates subclasses of AttributeMultiReference.
   * 
   */
  protected void createAttributeMultiReferenceFactory(BufferedWriter bufferedWriter)
  {
    List<MdAttributeMultiReferenceDAOIF> mdAttributeMultiReferenceList = new LinkedList<MdAttributeMultiReferenceDAOIF>();

    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        MdBusinessDAOIF referenceMdBusinessIF = ( (MdAttributeMultiReferenceDAOIF) mdAttributeIF ).getReferenceMdBusinessDAO();

        if (!GenerationUtil.isReservedAndHardcoded(referenceMdBusinessIF))
        {
          mdAttributeMultiReferenceList.add((MdAttributeMultiReferenceDAOIF) mdAttributeIF);
        }
      }
    }

    if (mdAttributeMultiReferenceList.size() > 0)
    {
      String methodName = "multiReferenceFactory";

      String parameterString = "((" + MdAttributeMultiReferenceDAOIF.class.getName() + ")mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);";

      write(bufferedWriter, "  protected " + AttributeMultiReference.class.getName() + " " + methodName + "(");
      write(bufferedWriter, " " + MdAttributeMultiReferenceDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, String mdMultiReferenceTableName, ");
      writeLine(bufferedWriter, " " + MdBusinessDAOIF.class.getName() + " referenceMdBusinessIF, String referenceTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)");
      writeLine(bufferedWriter, "  {");
      writeLine(bufferedWriter, "    String name = mdAttributeIF.definesAttribute();");
      writeLine(bufferedWriter, "    ");

      for (int i = 0; i < mdAttributeMultiReferenceList.size(); i++)
      {
        MdAttributeMultiReferenceDAOIF mdAttributeRefIF = mdAttributeMultiReferenceList.get(i);

        if (i == 0)
        {
          write(bufferedWriter, "    if ");
        }
        else
        {
          write(bufferedWriter, "    else if ");
        }

        writeLine(bufferedWriter, "(name.equals(" + this.getMdClassIF().definesType() + "." + mdAttributeRefIF.definesAttribute().toUpperCase() + ")) ");
        writeLine(bufferedWriter, "    {");

        MdBusinessDAOIF referenceMdBusinessIF = mdAttributeRefIF.getReferenceMdBusinessDAO();

        String multiReferenceAttributeClassName = BusinessQueryAPIGenerator.getMultiReferenceClass(referenceMdBusinessIF);

        writeLine(bufferedWriter, "       return new " + multiReferenceAttributeClassName + parameterString);
        writeLine(bufferedWriter, "    }");
      }

      writeLine(bufferedWriter, "    else ");
      writeLine(bufferedWriter, "    {");
      if (this.getMdClassIF().getSuperClass() == null)
      {
        writeLine(bufferedWriter, "      String error = \"Attribute type [\"+mdAttributeIF.getType()+\"] is invalid.\";");
        writeLine(bufferedWriter, "      throw new " + QueryException.class.getName() + "(error);");
      }
      else
      {
        writeLine(bufferedWriter, "      return super." + methodName + "(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdMultiReferenceTableName, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);");
      }

      writeLine(bufferedWriter, "    }");

      writeLine(bufferedWriter, "  }\n");
    }

  }

  /**
   *
   *
   */
  protected void addInnerClassAccessors()
  {
    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (! ( mdAttributeIF instanceof MdAttributeEnumerationDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeStructDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeEncryptionDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeFileDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeReferenceDAOIF ))
      {
        this.addInnerAccessor(this.srcBuffer, mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
      {
        this.addInnerStructAccessor(this.srcBuffer, (MdAttributeStructDAOIF) mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        this.addInnerEnumAccessor(this.srcBuffer, (MdAttributeEnumerationDAOIF) mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        this.addInnerMultiReferenceAccessor(this.srcBuffer, (MdAttributeMultiReferenceDAOIF) mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeRefDAOIF)
      {
        this.addInnerRefAccessor(this.srcBuffer, (MdAttributeRefDAOIF) mdAttributeIF);
      }
    }
  }

  /**
   *
   *
   */
  protected void addInnerInterfaceAccessors()
  {
    for (MdAttributeDAOIF mdAttributeIF : this.getMdClassIF().definesAttributesOrdered())
    {
      if (! ( mdAttributeIF instanceof MdAttributeEnumerationDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeStructDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeEncryptionDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeFileDAOIF ) && ! ( mdAttributeIF instanceof MdAttributeReferenceDAOIF ))
      {
        this.addInterfaceAccessor(this.srcBuffer, mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        this.addInterfaceMultiReferenceAccessor(this.srcBuffer, (MdAttributeMultiReferenceDAOIF) mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeReferenceDAOIF)
      {
        this.addInterfaceRefAccessor(this.srcBuffer, (MdAttributeReferenceDAOIF) mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeStructDAOIF)
      {
        this.addInterfaceStructAccessor(this.srcBuffer, (MdAttributeStructDAOIF) mdAttributeIF);
      }
      else if (mdAttributeIF instanceof MdAttributeEnumerationDAOIF)
      {
        this.addInterfaceEnumerationAccessor(this.srcBuffer, (MdAttributeEnumerationDAOIF) mdAttributeIF);
      }
    }
  }

  /**
   * General case generation of getter for an attribute on an interface.
   * 
   * @param mdAttributeIF
   *          Attribute to generate accessor methods for
   */
  protected void addInterfaceAccessor(BufferedWriter bufferedWriter, MdAttributeDAOIF mdAttributeIF)
  {
    String attributeClassName = mdAttributeIF.queryAttributeClass();

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());

    writeLine(bufferedWriter, "    public " + attributeClassName + " " + accessorName + "();");
    writeLine(bufferedWriter, "    public " + attributeClassName + " " + accessorName + "(String alias);");
    writeLine(bufferedWriter, "    public " + attributeClassName + " " + accessorName + "(String alias, String displayLabel);");
  }

  /**
   * Generation of getter for an attribute reference on an interface.
   * 
   * @param mdAttributeRefIF
   *          Attribute to generate accessor methods for
   */
  protected void addInterfaceRefAccessor(BufferedWriter bufferedWriter, MdAttributeReferenceDAOIF mdAttributeRefIF)
  {
    MdBusinessDAOIF refMdBusinessIF = mdAttributeRefIF.getReferenceMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeRefIF.definesAttribute());

    String attrRefName = BusinessQueryAPIGenerator.getRefInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "    public " + attrRefName + " " + accessorName + "();");
    writeLine(bufferedWriter, "    public " + attrRefName + " " + accessorName + "(String alias);");
    writeLine(bufferedWriter, "    public " + attrRefName + " " + accessorName + "(String alias, String displayLabel);");
  }

  /**
   * Generation of getter for an attribute struct on an interface.
   * 
   * @param mdAttributeStructIF
   *          Attribute to generate accessor methods for
   */
  protected void addInterfaceStructAccessor(BufferedWriter bufferedWriter, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    MdStructDAOIF mdStructIF = mdAttributeStructIF.getMdStructDAOIF();

    if (GenerationUtil.isReservedAndHardcoded(mdStructIF))
    {
      return;
    }
    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeStructIF.definesAttribute());

    String attrStructName = StructQueryAPIGenerator.getAttrStructInterface(mdStructIF);
    writeLine(bufferedWriter, "    public " + attrStructName + " " + accessorName + "();");
    writeLine(bufferedWriter, "    public " + attrStructName + " " + accessorName + "(String alias);");
    writeLine(bufferedWriter, "    public " + attrStructName + " " + accessorName + "(String alias, String displayLabel);");
  }

  protected void addInterfaceMultiReferenceAccessor(BufferedWriter bufferedWriter, MdAttributeMultiReferenceDAOIF mdAttributeMultiReferenceIF)
  {
    MdBusinessDAOIF refMdBusinessIF = mdAttributeMultiReferenceIF.getReferenceMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(refMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeMultiReferenceIF.definesAttribute());

    String attrRefName = BusinessQueryAPIGenerator.getRefInterface(refMdBusinessIF);
    writeLine(bufferedWriter, "    public " + attrRefName + " " + accessorName + "();");
    writeLine(bufferedWriter, "    public " + attrRefName + " " + accessorName + "(String alias);");
    writeLine(bufferedWriter, "    public " + attrRefName + " " + accessorName + "(String alias, String displayLabel);");
  }

  /**
   * Generation of getter for an attribute enumeration on an interface.
   * 
   * @param mdAttributeEnumerationIF
   *          Attribute to generate accessor methods for
   */
  protected void addInterfaceEnumerationAccessor(BufferedWriter bufferedWriter, MdAttributeEnumerationDAOIF mdAttributeEnumerationIF)
  {
    MdBusinessDAOIF masterMdBusinessIF = mdAttributeEnumerationIF.getMdEnumerationDAO().getMasterListMdBusinessDAO();

    if (GenerationUtil.isReservedAndHardcoded(masterMdBusinessIF))
    {
      return;
    }

    String accessorName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeEnumerationIF.definesAttribute());

    String attrEnumType = BusinessQueryAPIGenerator.getEnumSubInterface(mdAttributeEnumerationIF.getMdEnumerationDAO());

    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "();");
    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias);");
    writeLine(bufferedWriter, "  public " + attrEnumType + " " + accessorName + "(String alias, String displayLabel);");
  }

  /**
   * Creates emthods that will return type safe iterators of the query result.
   * 
   */
  protected abstract void createIteratorMethods();
}
