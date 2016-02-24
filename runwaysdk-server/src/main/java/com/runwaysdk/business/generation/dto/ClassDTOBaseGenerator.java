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
package com.runwaysdk.business.generation.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.MethodMetaData;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeHashDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.transport.conversion.ConversionFacade;

public abstract class ClassDTOBaseGenerator extends ComponentDTOGenerator
{
  /**
   * The prefix of the function to access an attribute's metadata
   */
  public static final String   ATTRIBUTE_MD_PREFIX = "get";

  /**
   * The suffix of the function to access an attribute's metadata
   */
  public static final String   ATTRIBUTE_MD_SUFFIX = "Md";

  /**
   * Hash key of the generated method signature code
   */
  private static final Integer METHOD_SIGNATURE    = new Integer(0);

  /**
   * Hash key of the generated method call code
   */
  private static final Integer METHOD_CALL         = new Integer(1);

  /**
   * @param mdClassDAOIF
   *          Type for which this generator will generate code artifacts.
   */
  public ClassDTOBaseGenerator(MdClassDAOIF mdClassDAOIF)
  {
    super(mdClassDAOIF, getGeneratedName(mdClassDAOIF));
  }

  static String getGeneratedName(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.getTypeName() + TypeGeneratorInfo.DTO_SYSTEM_BASE_SUFFIX;
    }
    else
    {
      return mdClass.getTypeName() + TypeGeneratorInfo.DTO_BASE_SUFFIX;
    }
  }

  public static String getParentClass(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.definesType() + TypeGeneratorInfo.DTO_SYSTEM_SUFFIX;
    }
    else
    {
      return mdClass.definesType() + TypeGeneratorInfo.DTO_SUFFIX;
    }
  }

  protected String getBaseClassName()
  {
    return getGeneratedName(this.getMdTypeDAOIF());
  }

  protected String getDTOStubClassName()
  {
    return getDTOStubClassName(this.getMdTypeDAOIF());
  }

  protected static String getDTOStubClassName(MdClassDAOIF mdClassDAOIF)
  {
    return ClassDTOStubGenerator.getGeneratedName(mdClassDAOIF);
  }

  protected String getDTOStubClassType()
  {
    return getDTOStubClassType(this.getMdTypeDAOIF());
  }

  protected static String getDTOStubClassType(MdClassDAOIF mdClassDAOIF)
  {
    return ClassDTOStubGenerator.getGeneratedType(mdClassDAOIF);
  }

  protected static String getDTOStubClassTypeHardcoded(MdClassDAOIF mdClassDAOIF)
  {
    return ClassDTOStubGenerator.getGeneratedTypeHardcoded(mdClassDAOIF);
  }

  /**
   * Get the DTO type extension of the DTO depending on the type of the
   * MdEntity, e.g. MdBusiness vs MdRelationship vs ...
   * 
   * @param parent
   *          parent class
   * 
   * @return
   */
  protected abstract String getExtends(MdClassDAOIF parent);

  protected boolean hashEquals()
  {
    return AbstractGenerator.hashEquals(this.getSerialVersionUID(), this.getPath());
  }

  @Override
  protected void write()
  {
    // Write the class constant
    writeClassConstant(this.getMdTypeDAOIF().definesType());

    addSerialVersionUID();

    // Write constructors
    writeConstructor();

    writeDefaultType();

    addConstants();

    // Write the getter and setters for the attributes
    writeAttributeMethods();

    // Write the Methods that have been defined for this MdClass
    writeMdMethods();

    // Write methods common for all classes
    writeConcrete();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("DTO_BASE", this.getSignature());
  }

  /**
   * Write methods which are specific to a concrete type which extends
   * EntityDTOGenerator.
   */
  protected abstract void writeConcrete();

  private void writeDefaultType()
  {
    getWriter().writeLine("protected " + String.class.getName() + " getDeclaredType()");
    getWriter().openBracket();
    getWriter().writeLine("return CLASS;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Struct attributes are actually entities stored as class variables. This
   * method generates the declaration of all necessary class variables.
   */
  private void addConstants()
  {
    for (MdAttributeDAOIF m : this.getMdTypeDAOIF().definesAttributesOrdered())
    {
      getWriter().writeLine("public static " + String.class.getName() + " " + m.definesAttribute().toUpperCase() + " = \"" + m.definesAttribute() + "\";");
    }
  }

  /**
   * Write the getter and setters for the MdAttributes defined for the MdEntity
   */
  protected void writeAttributeMethods()
  {
    for (MdAttributeDAOIF m : this.getMdTypeDAOIF().definesAttributesOrdered())
    {
      // Do not generate type safe getters and setters for system attributes
      if (!GenerationUtil.isDTOSpecialCase(m) && m.getGenerateAccessor())
      {
        writeAttributeMethods(m);
      }

    }
  }

  /**
   * Write the getter and setters for a given MdAttribute
   * 
   * @param m
   *          The MdAttribute to generate
   */
  private void writeAttributeMethods(MdAttributeDAOIF m)
  {
    boolean writeAttributeMdAccessor = true;

    // Special logic is required for MdAttributeEnumerations, MdAttributeStruct,
    // MdAttributeBlob, and MdAttributeReference
    MdAttributeConcreteDAOIF mdAttributeConcrate = m.getMdAttributeConcrete();

    if (mdAttributeConcrate instanceof MdAttributeEnumerationDAOIF)
    {
      writeEnumerationGetter(m);
      writeEnumerationSetter(m);
    }
    else if (mdAttributeConcrate instanceof MdAttributeMultiReferenceDAOIF)
    {
      writeMultiReferenceGetter(m);
      writeMultiReferenceSetter(m);
    }
    else if (mdAttributeConcrate instanceof MdAttributeStructDAOIF)
    {
      MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeConcrate;
      if (mdAttributeStructIF.getMdStructDAOIF().isPublished())
      {
        writeStructGetter(m);
        writeStructSetter(m);
      }
      else
      {
        writeAttributeMdAccessor = false;
      }
    }
    else if (mdAttributeConcrate instanceof MdAttributeBlobDAOIF)
    {
      writeBlobGetter(m);
      writeBlobSetter(m);
    }
    else if (mdAttributeConcrate instanceof MdAttributeReferenceDAOIF)
    {
      MdAttributeReferenceDAOIF mdAttributeReferenceIF = (MdAttributeReferenceDAOIF) mdAttributeConcrate;
      if (mdAttributeReferenceIF.getReferenceMdBusinessDAO().isPublished())
      {
        writeReferenceGetter(m);
        writeReferenceSetter(m);
      }
      else
      {
        writeAttributeMdAccessor = false;
      }
    }
    else if (mdAttributeConcrate instanceof MdAttributeHashDAOIF)
    {
      writeHashGetter(m);
      writeSetter(m);
    }
    else
    {
      writeGetter(m);
      writeSetter(m);
    }

    writeIsWritable(m);
    writeIsReadable(m);
    writeIsModified(m);

    if (writeAttributeMdAccessor)
    {
      writeAttributeMetaDataAccessor(m); // metadata accessor
    }
  }

  /**
   * Write type safe isModified method for a given attribute
   * 
   */
  private void writeIsModified(MdAttributeDAOIF m)
  {
    if (!m.getSetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();

    getWriter().writeLine("public boolean is" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "Modified()");
    getWriter().openBracket();
    getWriter().writeLine("return isModified(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the type safe isWritable method for a given attribute
   * 
   * @param m
   *          MdAttribute to write
   */
  protected void writeIsWritable(MdAttributeDAOIF m)
  {
    if (!m.getSetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();

    getWriter().writeLine("public boolean is" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "Writable()");
    getWriter().openBracket();
    getWriter().writeLine("return isWritable(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the type safe isReadable method for a given attribute
   * 
   * @param m
   *          MdAttribute to write
   */
  protected void writeIsReadable(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();

    getWriter().writeLine("public boolean is" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "Readable()");
    getWriter().openBracket();
    getWriter().writeLine("return isReadable(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for the MdAttriubte which is not a MdAttributeEnumeration
   * or MdAttributeStruct
   * 
   * @param m
   *          The MdAttribute
   */
  protected void writeGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();

    String getter = m.generatedClientGetter();
    if (m.getMdAttributeConcrete() instanceof MdAttributeHashDAOIF)
    {
      getter = "getValue(" + attributeConstant + ")";
    }

    getWriter().writeLine("public " + m.javaType(false) + " get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("return " + getter + ';');
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the setter for the MdAttribute which is not a MdAttributeEnumeration
   * or MdAttributeStruct
   * 
   * @param m
   */
  protected void writeSetter(MdAttributeDAOIF m)
  {
    if (GenerationUtil.isDTOSpecialCaseSetter(m))
    {
      return;
    }

    String attributeName = m.definesAttribute();

    getWriter().writeLine("public void set" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + m.javaType(false) + " value)");
    getWriter().openBracket();
    getWriter().writeLine("if(value == null)");
    getWriter().openBracket();
    getWriter().writeLine(m.setterWrapper("\"\"") + ";");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine(m.generatedClientSetter() + ";");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for MdAttributeEnumerations
   * 
   * @param m
   */
  protected void writeEnumerationGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    MdAttributeEnumerationDAOIF mdAttributeEnumeration = (MdAttributeEnumerationDAOIF) m.getMdAttributeConcrete();

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String returnType = "java.util.List<" + m.javaType(true) + DTO_SUFFIX + ">";

    if (mdAttributeEnumeration.getMdEnumerationDAO().isGenerateSource())
    {
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine("public " + returnType + " get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
      getWriter().openBracket();

      getWriter().writeLine("return (" + returnType + ") " + ConversionFacade.class.getName() + ".convertEnumDTOsFromEnumNames(getRequest(), " + m.javaType(false) + DTO_SUFFIX + ".CLASS, getEnumNames(" + attributeConstant + "));");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }

    String enumNamesGetter = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName) + TypeGeneratorInfo.ATTRIBUTE_ENUMERATION_ENUM_NAMES_SUFFIX;
    getWriter().writeLine("public java.util.List<String> " + enumNamesGetter + "()");
    getWriter().openBracket();
    getWriter().writeLine("return getEnumNames(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the setter for MdAttributeEnumerations
   * 
   * @param m
   */
  protected void writeEnumerationSetter(MdAttributeDAOIF m)
  {
    if (GenerationUtil.isDTOSpecialCaseSetter(m))
    {
      return;
    }

    MdAttributeEnumerationDAOIF mdAttributeEnumeration = (MdAttributeEnumerationDAOIF) m.getMdAttributeConcrete();

    String attributeName = m.definesAttribute();
    String attributeConstant = m.definesAttribute().toUpperCase();
    String paramType = m.javaType(false) + DTO_SUFFIX;

    if (mdAttributeEnumeration.getMdEnumerationDAO().isGenerateSource())
    {
      // Write add Enumeration Item
      getWriter().writeLine("public void add" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + paramType + " enumDTO)");
      getWriter().openBracket();
      getWriter().writeLine("addEnumItem(" + attributeConstant + ", enumDTO.toString());");
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Write remove Enumeration Item
      getWriter().writeLine("public void remove" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + paramType + " enumDTO)");
      getWriter().openBracket();
      getWriter().writeLine("removeEnumItem(" + attributeConstant + ", enumDTO.toString());");
      getWriter().closeBracket();
      getWriter().writeLine("");
    }

    // Write clear Enumeration
    getWriter().writeLine("public void clear" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("clearEnum(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for MdAttributeEnumerations
   * 
   * @param m
   */
  protected void writeMultiReferenceGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();

    getWriter().writeLine("public java.util.List<String>  get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("return this.getMultiItems(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the setter for MdAttributeEnumerations
   * 
   * @param m
   */
  protected void writeMultiReferenceSetter(MdAttributeDAOIF m)
  {
    if (GenerationUtil.isDTOSpecialCaseSetter(m))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = m.definesAttribute().toUpperCase();
    String paramType = m.javaType(false) + DTO_SUFFIX;

    // Write add multi Item
    getWriter().writeLine("public void add" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + paramType + " itemDTO)");
    getWriter().openBracket();
    getWriter().writeLine("this.addMultiItem(" + attributeConstant + ", itemDTO.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Write remove multi item
    getWriter().writeLine("public void remove" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + paramType + " itemDTO)");
    getWriter().openBracket();
    getWriter().writeLine("this.removeMultiItem(" + attributeConstant + ", itemDTO.getId());");
    getWriter().closeBracket();
    getWriter().writeLine("");

    // Write clear multi items
    getWriter().writeLine("public void clear" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("this.clearMultiItems(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for MdAttributeStruct
   * 
   * @param m
   */
  protected void writeReferenceGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) m.getMdAttributeConcrete();

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String returnType = ClassDTOBaseGenerator.getDTOStubClassTypeHardcoded(mdAttributeReference.getReferenceMdBusinessDAO());

    getWriter().writeLine("public " + returnType + " get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();

    getWriter().writeLine("if(getValue(" + attributeConstant + ") == null || getValue(" + attributeConstant + ").trim().equals(\"\"))");
    getWriter().openBracket();
    getWriter().writeLine("return null;");
    getWriter().closeBracket();

    getWriter().writeLine("else");
    getWriter().openBracket();

    // If the dto being created is a BusinessDTO then
    // create through the ClientRequest instead of the BusinessDTO class
    // because the method newInstance(String, ClientRequest) does not exist
    // in the class and cannont due to RMI serialization issues.
    if (returnType.equals(BusinessDTO.class.getName()))
    {
      getWriter().writeLine(BusinessDTO.class.getName() + " dto = (" + BusinessDTO.class.getName() + ") getRequest().get(getValue(" + attributeConstant + "));");
      getWriter().writeLine("");
      getWriter().writeLine("return dto;");
    }
    else
    {
      getWriter().writeLine("return " + returnType + ".get(getRequest(), getValue(" + attributeConstant + "));");
    }
    getWriter().closeBracket();

    getWriter().closeBracket();
    getWriter().writeLine("");

    // Generate an accessor that returns the reference id
    String refAttributeIdName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute()) + CommonGenerationUtil.upperFirstCharacter(ComponentInfo.ID);
    String getRefIdReturnType = m.getMdAttributeDAO(ComponentInfo.ID).javaType(false);
    getWriter().writeLine("public " + getRefIdReturnType + " get" + refAttributeIdName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return " + mdAttributeReference.generatedClientGetterRefId() + ';');
    getWriter().closeBracket();
    getWriter().writeLine("");

  }

  protected void writeReferenceSetter(MdAttributeDAOIF m)
  {
    if (GenerationUtil.isDTOSpecialCaseSetter(m))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String parmType = ClassDTOBaseGenerator.getDTOStubClassTypeHardcoded( ( (MdAttributeReferenceDAOIF) m.getMdAttributeConcrete() ).getReferenceMdBusinessDAO());

    getWriter().writeLine("public void set" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + parmType + " value)");
    getWriter().openBracket();

    getWriter().writeLine("if(value == null)");
    getWriter().openBracket();
    getWriter().writeLine("setValue(" + attributeConstant + ", \"\");");
    getWriter().closeBracket();

    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("setValue(" + attributeConstant + ", value.getId());");
    getWriter().closeBracket();

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for MdAttributeStruct
   * 
   * @param m
   */
  protected void writeStructGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String returnType = m.javaType(false) + DTO_SUFFIX;

    getWriter().writeLine("public " + returnType + " get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + returnType + ") this.getAttributeStructDTO(" + attributeConstant + ").getStructDTO();");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for MdAttributeStruct
   * 
   * @param m
   */
  protected void writeStructSetter(MdAttributeDAOIF m)
  {
    // Write nothing, setters don't exist for MdAttributeStructs
  }

  /**
   * Write the getter for MdAttributeBlob
   * 
   * @param m
   */
  protected void writeBlobGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String returnType = "byte[]";

    getWriter().writeLine("public " + returnType + " get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("return super.getBlob(" + attributeConstant + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for MdAttributeBlob
   * 
   * @param m
   */
  protected void writeBlobSetter(MdAttributeDAOIF m)
  {
    if (GenerationUtil.isDTOSpecialCaseSetter(m))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String parmType = "byte[]";

    getWriter().writeLine("public void set" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "(" + parmType + " bytes)");
    getWriter().openBracket();
    getWriter().writeLine("super.setBlob(" + attributeConstant + ", bytes);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the getter for the MdAttriubte which is not a MdAttributeEnumeration
   * or MdAttributeStruct
   * 
   * @param m
   *          The MdAttribute
   */
  protected void writeHashGetter(MdAttributeDAOIF m)
  {
    if (!m.getGetterVisibility().equals(VisibilityModifier.PUBLIC))
    {
      return;
    }

    String attributeName = m.definesAttribute();
    String attributeConstant = attributeName.toUpperCase();
    String getter = "getValue(" + attributeConstant + ")";

    getWriter().writeLine("public " + m.javaType(false) + " get" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "()");
    getWriter().openBracket();
    getWriter().writeLine("return " + getter + ';');
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public boolean " + attributeName + "Equals(" + String.class.getName() + " value, " + boolean.class.getName() + " alreadyEncrypted)");
    getWriter().openBracket();
    getWriter().writeLine("return getAttributeHashDTO(" + attributeConstant + ").encryptionEquals(value, alreadyEncrypted);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Writes the class field for attribute metadata.
   * 
   * @param m
   */
  private void writeAttributeMetaDataAccessor(MdAttributeDAOIF m)
  {
    String attributeName = m.definesAttribute();
    String attrMdName = ATTRIBUTE_MD_PREFIX + CommonGenerationUtil.upperFirstCharacter(attributeName) + ATTRIBUTE_MD_SUFFIX;
    String attributeMdDTOType = m.attributeMdDTOType();

    String attributeConstant = attributeName.toUpperCase();

    getWriter().writeLine("public final " + attributeMdDTOType + " " + attrMdName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return (" + attributeMdDTOType + ") getAttributeDTO(" + attributeConstant + ").getAttributeMdDTO();");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Write the MdMethods defined on this type. If a MdMethod is not static then
   * it also writes the static representation of the instance method.
   */
  private void writeMdMethods()
  {
    String typeCLASSconstant = this.getDTOStubClassType() + ".CLASS";

    for (MdMethodDAOIF mdMethod : this.getMdTypeDAOIF().getMdMethodsOrdered())
    {
      String methodName = mdMethod.getName();
      Type returnType = mdMethod.getReturnType();
      List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();

      writeMdMethod(typeCLASSconstant, list, methodName, returnType, mdMethod.isStatic(), true);

      // Write the static form of the instance method which
      // additionally requires the id of instance
      if (!mdMethod.isStatic())
      {
        MdParameterDAO id = GenerationUtil.getMdParameterId();
        id.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod.getId());
        list.add(0, id);

        writeMdMethod(typeCLASSconstant, list, methodName, returnType, true, true);
      }
    }
  }

  /**
   * Writes a single MdMethod defined for the MdEntity
   * 
   * @param typeCLASSconstant
   *          The CLASS constant variable that has the full type name.
   * @param list
   *          Ordered list of MdParameterIF defined for the MdMethod
   * @param methodName
   *          The name of the method
   * @param returnType
   *          The Type of the return object
   * @param isStatic
   *          Flag denoting if the method is static or not
   * @param isFinal
   *          Flag denoting if the method is final or not
   */
  protected void writeMdMethod(String typeCLASSconstant, List<MdParameterDAOIF> list, String methodName, Type returnType, boolean isStatic, boolean isFinal)
  {
    String metadataName = MethodMetaData.class.getName();
    String methodTypes = GenerationUtil.buildMethodArray(list);
    String params = GenerationUtil.buildParameterArray(list);
    Map<Integer, String> methodInfo = getMethodInfo(list, methodName, returnType.getDTOType(), isStatic, isFinal);

    getWriter().writeLine(methodInfo.get(METHOD_SIGNATURE));
    getWriter().openBracket();

    getWriter().writeLine("String[] _declaredTypes = new String[]{" + methodTypes + "};");
    getWriter().writeLine("Object[] _parameters = new Object[]{" + params + "};");
    getWriter().writeLine(metadataName + " _metadata = new " + metadataName + "(" + typeCLASSconstant + ", \"" + methodName + "\", _declaredTypes);");

    if (!returnType.isVoid())
    {
      getWriter().writeLine("return (" + returnType.getDTOType() + ") " + methodInfo.get(METHOD_CALL));
    }
    else
    {
      getWriter().writeLine(methodInfo.get(METHOD_CALL));
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Returns the method signature and code to invoke the method based upon if
   * the method is static or not.
   * 
   * @param list
   *          Ordered list of MdParameterIF defined for the MdMethod
   * @param methodName
   *          The name of the method
   * @param returnType
   *          A String representing the DTO type of the return object
   * @param isStatic
   *          Flag denoting if the method is static or not
   * @param isFinal
   *          Flag denoting if the method is final or not
   * @return
   */
  private Map<Integer, String> getMethodInfo(List<MdParameterDAOIF> list, String methodName, String returnType, boolean isStatic, boolean isFinal)
  {
    Map<Integer, String> output = new HashMap<Integer, String>();
    String modifier = GenerationUtil.getModifier(isStatic, isFinal);

    if (isStatic)
    {
      String parameters = GenerationUtil.buildDTOParameters(list, false);

      output.put(METHOD_SIGNATURE, "public " + modifier + returnType + " " + methodName + "(" + ClientRequestIF.class.getName() + " clientRequest" + parameters + ")");

      output.put(METHOD_CALL, "clientRequest.invokeMethod(_metadata, null, _parameters);");
    }
    else
    {
      String parameters = GenerationUtil.buildDTOParameters(list, true);

      output.put(METHOD_SIGNATURE, "public " + modifier + returnType + " " + methodName + "(" + parameters + ")");

      output.put(METHOD_CALL, "getRequest().invokeMethod(_metadata, this, _parameters);");
    }

    return output;
  }

  /**
   * Writes the Class definition of the DTO
   */
  @Override
  protected void writeClassName()
  {
    addSignatureAnnotation();

    MdClassDAOIF parent = this.getMdTypeDAOIF().getSuperClass();

    String extend = this.getExtends(parent);

    getWriter().write("public abstract class " + getFileName() + " extends " + extend);

    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(Reloadable.IMPLEMENTS);
    }

    getWriter().writeLine("");

    getWriter().openBracket();
  }

  /**
   * Write the default constructor for the generated ClassDTO stub class
   */
  protected void writeConstructor()
  {
    getWriter().writeLine("protected " + getFileName() + "(" + ClientRequestIF.class.getName() + " clientRequest)");
    getWriter().openBracket();
    getWriter().writeLine("super(clientRequest);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Returns the base DTO type of a given MdType. If the MdType is reserved then
   * the reserved DTO type is returned instead.
   * 
   * @param type
   * @return
   */
  public static String getDTOBaseType(String type)
  {
    return type + DTO_BASE_SUFFIX;
  }

  @Override
  protected MdClassDAOIF getMdTypeDAOIF()
  {
    return (MdClassDAOIF) super.getMdTypeDAOIF();
  }

  @Override
  public String getClassAttribute()
  {
    return MdClassInfo.DTO_BASE_CLASS;
  }

  @Override
  public String getSourceAttribute()
  {
    return MdClassInfo.DTO_BASE_SOURCE;
  }
}
