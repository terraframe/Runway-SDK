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

import java.util.List;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.StructInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.constants.VisibilityModifier;
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
import com.runwaysdk.dataaccess.metadata.ForbiddenMethodException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.Type;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;

/**
 * !IMPORTANT! If you're changing the way base classes are generated then its
 * probably a good time to add a generation version number to the class
 * signature. The reason is because you must regenerate all the base classes of
 * applications that depend on runway (even though the metadata may not have
 * changed). If you don't regenerate these base classes, then the app can break
 * at runtime if the generated file is different than what it was copiled
 * against. See DDMS ticket #3298 * !IMPORTANT!
 */
public abstract class ClassBaseGenerator extends TypeGenerator
{
  public ClassBaseGenerator(MdClassDAOIF mdClass)
  {
    super(mdClass, getGeneratedName(mdClass));
  }

  static String getGeneratedName(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.getTypeName() + TypeGeneratorInfo.SYSTEM_BASE_SUFFIX;
    }
    else
    {
      return mdClass.getTypeName() + TypeGeneratorInfo.BASE_SUFFIX;
    }
  }

  protected static String getParentClass(MdClassDAOIF mdClass)
  {
    if (GenerationUtil.isHardcodedType(mdClass))
    {
      return mdClass.definesType() + TypeGeneratorInfo.SYSTEM_SUFFIX;
    }
    else
    {
      return mdClass.definesType();
    }
  }

  protected String getBaseClassName()
  {
    return getGeneratedName(this.getMdTypeDAOIF());
  }

  protected String getSubClassName()
  {
    return ClassStubGenerator.getGeneratedName(this.getMdTypeDAOIF());
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // do not generate if the file has already been generated and is
    // semantically equivilant.
    if (LocalProperties.isKeepBaseSource() && AbstractGenerator.hashEquals(this.getSerialVersionUID(), this.getPath()))
    {
      return;
    }

    addPackage();
    addSignatureAnnotation();
    addClassName();
    addStaticInitializerBlock();
    addFields();
    addSerialVersionUID();
    addConstructor();
    addMethods();
    addMdMethods();
    addStaticMethods();
    addToString();
    getWriter().closeBracket();
    getWriter().close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("BASE", this.getSignature());
  }

  @Override
  protected MdClassDAOIF getMdTypeDAOIF()
  {
    return (MdClassDAOIF) super.getMdTypeDAOIF();
  }

  /**
   * Generate the package declaration
   */
  protected void addPackage()
  {
    getWriter().writeLine("package " + this.getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  protected void addStaticInitializerBlock()
  {

  }

  /**
   * Generates the class name declaration
   */
  protected void addClassName()
  {
    MdClassDAOIF parent = this.getMdTypeDAOIF().getSuperClass();

    String typeName = this.getMdTypeDAOIF().getTypeName();
    String classSignature = "public abstract class " + this.getBaseClassName() + " extends " + getExtends(parent);

    // Add a javadoc to the base file that yells at the user to not make changes
    getWriter().writeLine("/**");
    getWriter().writeLine(" * This class is generated automatically.");
    getWriter().writeLine(" * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN");
    getWriter().writeLine(" * Custom business logic should be added to " + typeName + ".java");
    getWriter().writeLine(" *");
    getWriter().writeLine(" * @author Autogenerated by RunwaySDK");
    getWriter().writeLine(" */");
    getWriter().writeLine(classSignature);
    getWriter().openBracket();
  }

  protected abstract String getExtends(MdClassDAOIF parent);

  /**
   * Struct attributes are actually entities stored as class variables. This
   * method generates the declaration of all necessary class variables.
   */
  protected void addFields()
  {
    getWriter().writeLine("public final static String CLASS = \"" + this.getMdTypeDAOIF().definesType() + "\";");

    for (MdAttributeDAOIF m : this.getMdTypeDAOIF().definesAttributesOrdered())
    {
      getWriter().writeLine("public final static " + String.class.getName() + " " + m.definesAttribute().toUpperCase() + " = \"" + m.definesAttribute() + "\";");

      if (m.getMdAttributeConcrete() instanceof MdAttributeStructDAOIF)
      {
        String structName = CommonGenerationUtil.lowerFirstCharacter(m.definesAttribute());
        getWriter().writeLine("private " + StructInfo.CLASS + " " + structName + " = null;");
        getWriter().writeLine("");
      }
    }
  }

  /**
   *
   */
  protected abstract void addConstructor();

  /**
   * Structs are stored as private class variables. This method generates their
   * initialization.
   */
  protected void addStructInitializers()
  {
    for (MdAttributeDAOIF m : this.getMdTypeDAOIF().definesAttributesOrdered())
    {
      if (m.getMdAttributeConcrete() instanceof MdAttributeStructDAOIF)
      {
        String structName = CommonGenerationUtil.lowerFirstCharacter(m.definesAttribute());
        getWriter().writeLine(structName + " = super.getStruct(\"" + m.definesAttribute() + "\");");
      }
    }
  }

  /**
   * Calls appropriate generation methods for every attribute defined by this
   * type
   */
  protected void addMethods()
  {
    for (MdAttributeDAOIF m : this.getMdTypeDAOIF().definesAttributesOrdered())
    {
      MdAttributeConcreteDAOIF mdAttributeConcrete = m.getMdAttributeConcrete();
      if (mdAttributeConcrete instanceof MdAttributeEnumerationDAOIF)
      {
        addEnumerationMethods(m);
      }
      else if (mdAttributeConcrete instanceof MdAttributeMultiReferenceDAOIF)
      {
        addMultiReferenceMethods(m);
      }
      else
      {
        addMethods(m);
      }
    }

    // Override getDeclaredType();
    getWriter().writeLine("protected String getDeclaredType()");
    getWriter().openBracket();
    getWriter().writeLine("return CLASS;");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Enumerated attributes have different methods than regular attributes: get /
   * add / remove / clear instead of get / set. This method generates methods
   * for enumerated attributes.
   * 
   * @param m
   *          - MdAttributeDAOIF to generate. Either it is a virtual attribute
   *          that references an MdAttributeEnumerationDAO or is an
   *          MdAttributeEnumerationDAO.
   */
  private void addEnumerationMethods(MdAttributeDAOIF m)
  {
    MdAttributeEnumerationDAOIF mdAttributeEnumeration = (MdAttributeEnumerationDAOIF) m.getMdAttributeConcrete();

    if (m.getGenerateAccessor() && mdAttributeEnumeration.getMdEnumerationDAO().isGenerateSource())
    {
      String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
      String attributeNameConstant = m.definesAttribute().toUpperCase();

      VisibilityModifier getterVisibility = m.getGetterVisibility();

      // Getter
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(getterVisibility.getJavaModifier() + " java.util.List<" + m.javaType(false) + "> " + CommonGenerationUtil.GET + attributeName + "()");
      getWriter().openBracket();
      getWriter().writeLine("return " + m.generatedServerGetter() + ';');
      getWriter().closeBracket();
      getWriter().writeLine("");

      VisibilityModifier setterVisibility = m.getSetterVisibility();

      // Add an item
      getWriter().writeLine(setterVisibility.getJavaModifier() + " void add" + attributeName + "(" + m.javaType(false) + " value)");
      getWriter().openBracket();
      getWriter().writeLine("if(value != null)");
      getWriter().openBracket();
      getWriter().writeLine("addEnumItem(" + attributeNameConstant + ", value.getOid());");
      getWriter().closeBracket();
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove an item
      getWriter().writeLine(setterVisibility.getJavaModifier() + " void remove" + attributeName + "(" + m.javaType(false) + " value)");
      getWriter().openBracket();
      getWriter().writeLine("if(value != null)");
      getWriter().openBracket();
      getWriter().writeLine("removeEnumItem(" + attributeNameConstant + ", value.getOid());");
      getWriter().closeBracket();
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Clear all items
      getWriter().writeLine(setterVisibility.getJavaModifier() + " void clear" + attributeName + "()");
      getWriter().openBracket();
      getWriter().writeLine("clearEnum(" + attributeNameConstant + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      addValidator(m);

      addAttributeMetaDataGetter(m);
    }
  }

  private void addMultiReferenceMethods(MdAttributeDAOIF m)
  {
    if (m.getGenerateAccessor())
    {
      String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
      String attributeNameConstant = m.definesAttribute().toUpperCase();

      VisibilityModifier getterVisibility = m.getGetterVisibility();

      // Getter
      getWriter().writeLine("@SuppressWarnings(\"unchecked\")");
      getWriter().writeLine(getterVisibility.getJavaModifier() + " java.util.List<" + m.javaType(false) + "> " + CommonGenerationUtil.GET + attributeName + "()");
      getWriter().openBracket();
      getWriter().writeLine("return " + m.generatedServerGetter() + ';');
      getWriter().closeBracket();
      getWriter().writeLine("");

      VisibilityModifier setterVisibility = m.getSetterVisibility();

      // Add an item
      getWriter().writeLine(setterVisibility.getJavaModifier() + " void add" + attributeName + "(" + m.javaType(false) + " value)");
      getWriter().openBracket();
      getWriter().writeLine("if(value != null)");
      getWriter().openBracket();
      getWriter().writeLine("this.addMultiItem(" + attributeNameConstant + ", value.getOid());");
      getWriter().closeBracket();
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Remove an item
      getWriter().writeLine(setterVisibility.getJavaModifier() + " void remove" + attributeName + "(" + m.javaType(false) + " value)");
      getWriter().openBracket();
      getWriter().writeLine("if(value != null)");
      getWriter().openBracket();
      getWriter().writeLine("removeMultiItem(" + attributeNameConstant + ", value.getOid());");
      getWriter().closeBracket();
      getWriter().closeBracket();
      getWriter().writeLine("");

      // Clear all items
      getWriter().writeLine(setterVisibility.getJavaModifier() + " void clear" + attributeName + "()");
      getWriter().openBracket();
      getWriter().writeLine("clearMultiItems(" + attributeNameConstant + ");");
      getWriter().closeBracket();
      getWriter().writeLine("");

      addValidator(m);

      addAttributeMetaDataGetter(m);

    }
  }

  /**
   * General case generation of getter and setter for an attribute
   * 
   * @param m
   *          Attribute to generate accessor methods for
   */
  private void addMethods(MdAttributeDAOIF m)
  {
    if (m.getGenerateAccessor())
    {
      if (m.getMdAttributeConcrete() instanceof MdAttributeHashDAOIF)
      {
        addEquals(m);
      }
      else if (m.getMdAttributeConcrete() instanceof MdAttributeReferenceDAOIF)
      {
        addReferenceGetter((MdAttributeReferenceDAOIF) m);
      }
      else
      {
        addGetter(m);
      }

      addValidator(m);

      addAttributeMetaDataGetter(m);

      // Structs don't have setters
      if (m.getMdAttributeConcrete() instanceof MdAttributeStructDAOIF)
        return;

      if (!GenerationUtil.isSpecialCaseSetter(m))
      {
        addSetter(m);

        if (m.getMdAttributeConcrete() instanceof MdAttributeReferenceDAOIF)
        {
          addReferenceSetter(m);
        }
      }
    }
  }

  /**
   * Hashed attribute are not allowed to be retreived - only compared against.
   * This method generates the comparison method.
   * 
   * @param m
   *          MdAttributeHash to generate an equals method for
   */
  protected void addEquals(MdAttributeDAOIF m)
  {
    VisibilityModifier getterVisibility = m.getGetterVisibility();

    String attributeName = CommonGenerationUtil.lowerFirstCharacter(m.definesAttribute());
    getWriter().writeLine(getterVisibility.getJavaModifier() + " boolean " + attributeName + "Equals(String value)");
    getWriter().openBracket();
    getWriter().writeLine("return " + m.generatedServerGetter() + ';');
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a Getter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addGetter(MdAttributeDAOIF m)
  {
    VisibilityModifier getterVisibility = m.getGetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(getterVisibility.getJavaModifier() + " " + m.javaType(false) + " " + CommonGenerationUtil.GET + attributeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("return " + m.generatedServerGetter() + ';');
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a Getter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addReferenceGetter(MdAttributeReferenceDAOIF m)
  {
    VisibilityModifier getterVisibility = m.getGetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(getterVisibility.getJavaModifier() + " " + m.javaType(false) + " get" + attributeName + "()");
    getWriter().openBracket();

    getWriter().writeLine("if (getValue(" + m.definesAttribute().toUpperCase() + ").trim().equals(\"\"))");
    getWriter().openBracket();
    getWriter().writeLine("return null;");
    getWriter().closeBracket();

    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("return " + m.generatedServerGetter() + ';');
    getWriter().closeBracket();

    getWriter().closeBracket();
    getWriter().writeLine("");

    // Generate an accessor that returns the reference oid
    String refAttributeIdName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute()) + CommonGenerationUtil.upperFirstCharacter(ComponentInfo.OID);
    String getRefIdReturnType = m.getMdAttributeDAO(ComponentInfo.OID).javaType(false);
    getWriter().writeLine(getterVisibility.getJavaModifier() + " " + getRefIdReturnType + " get" + refAttributeIdName + "()");
    getWriter().openBracket();

    MdAttributeReferenceDAOIF mdAttributeReference = (MdAttributeReferenceDAOIF) m.getMdAttributeConcrete();

    getWriter().writeLine("return " + mdAttributeReference.generatedServerGetterRefId() + ';');

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a setter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addSetter(MdAttributeDAOIF m)
  {
    // do not generate a setter for a system attribute.
    if (m.isSystem())
    {
      return;
    }

    VisibilityModifier setterVisibility = m.getSetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(setterVisibility.getJavaModifier() + " void set" + attributeName + "(" + m.javaType(false) + " value)");
    getWriter().openBracket();
    getWriter().writeLine("if(value == null)");
    getWriter().openBracket();
    getWriter().writeLine(m.setterWrapper("\"\"") + ";");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine(m.generatedServerSetter() + ";");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a setter for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addReferenceSetter(MdAttributeDAOIF m)
  {
    // do not generate a setter for a system attribute.
    if (m.isSystem())
    {
      return;
    }

    VisibilityModifier setterVisibility = m.getSetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(setterVisibility.getJavaModifier() + " void set" + attributeName + "Id(" + String.class.getName() + " oid)");
    getWriter().openBracket();
    getWriter().writeLine("if(oid == null)");
    getWriter().openBracket();
    getWriter().writeLine(m.setterWrapper("\"\"") + ";");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine(m.setterWrapper("oid") + ";");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a Validate method for the given attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  protected void addValidator(MdAttributeDAOIF m)
  {
    VisibilityModifier setterVisibility = m.getSetterVisibility();

    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());
    getWriter().writeLine(setterVisibility.getJavaModifier() + " void validate" + attributeName + "()");
    getWriter().openBracket();
    getWriter().writeLine("this.validateAttribute(" + m.definesAttribute().toUpperCase() + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Generates a method that returns the {@link MdAttributeDAOIF} for the given
   * attribute in the base .java file
   * 
   * @param m
   *          MdAttribute to generate
   */
  private void addAttributeMetaDataGetter(MdAttributeDAOIF m)
  {
    VisibilityModifier setterVisibility = m.getGetterVisibility();
    String attributeName = CommonGenerationUtil.upperFirstCharacter(m.definesAttribute());

    getWriter().writeLine(setterVisibility.getJavaModifier() + " static " + m.getInterfaceClassName() + " " + CommonGenerationUtil.GET + attributeName + "Md()");
    getWriter().openBracket();
    getWriter().writeLine(MdClassDAOIF.class.getName() + " mdClassIF = " + MdClassDAO.class.getName() + ".getMdClassDAO(" + this.getMdTypeDAOIF().definesType() + ".CLASS" + ");");
    getWriter().writeLine("return (" + m.getInterfaceClassName() + ")mdClassIF.definesAttribute(" + m.definesAttribute().toUpperCase() + ");");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void addMdMethods()
  {
    for (MdMethodDAOIF mdMethod : this.getMdTypeDAOIF().getMdMethodsOrdered())
    {
      addMdMethod(mdMethod);

      if (!mdMethod.isStatic())
      {
        List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
        MdParameterDAO oid = GenerationUtil.getMdParameterId();
        list.add(0, oid);

        addMdMethod(mdMethod.getName(), mdMethod.getReturnType(), list, true);
      }
    }
  }

  private void addMdMethod(String methodName, Type returnType, List<MdParameterDAOIF> list, boolean isFinal)
  {
    String parameters = GenerationUtil.buildBusinessParameters(list, true);
    String parameterNames = GenerationUtil.buildParameterArray(list.subList(1, list.size()));
    String modifier = GenerationUtil.getModifier(true, isFinal);

    getWriter().writeLine("public " + modifier + returnType.getType() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();
    getWriter().writeLine(this.getMdTypeDAOIF().getTypeName() + " _instance = " + this.getMdTypeDAOIF().getTypeName() + ".get(oid);");

    if (!returnType.isVoid())
    {
      getWriter().writeLine("return _instance." + methodName + "(" + parameterNames + ");");
    }
    else
    {
      getWriter().writeLine("_instance." + methodName + "(" + parameterNames + ");");
    }

    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  private void addMdMethod(MdMethodDAOIF mdMethod)
  {
    String methodName = mdMethod.getValue(MdMethodInfo.NAME);
    Type returnType = mdMethod.getReturnType();
    List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();
    String parameters = GenerationUtil.buildBusinessParameters(list, true);
    String modifier = GenerationUtil.getModifier(mdMethod.isStatic(), false);

    getWriter().writeLine("public " + modifier + returnType.getType() + " " + methodName + "(" + parameters + ")");
    getWriter().openBracket();
    getWriter().writeLine("String msg = \"This method should never be invoked.  It should be overwritten in " + this.getMdTypeDAOIF().definesType() + ".java\";");
    getWriter().writeLine("throw new " + ForbiddenMethodException.class.getName() + "(msg);");
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  /**
   * Some classes have static methods (lock / unlock for example). This method
   * is a hook that can be overridden to add generation of static methods. By
   * default nothing is generated.
   */
  protected void addStaticMethods()
  {
    // This is a hook point - there is no default behavior.
  }

  protected void addToString()
  {
    getWriter().writeLine("public String toString()");
    getWriter().writeLine("{");
    getWriter().writeLine("  if (this.isNew())");
    getWriter().writeLine("  {");
    getWriter().writeLine("    return \"New: \"+ this.getClassDisplayLabel();");
    getWriter().writeLine("  }");
    getWriter().writeLine("  else");
    getWriter().writeLine("  {");

    MdClassDAOIF mdClass = this.getMdTypeDAOIF();

    if (mdClass.definesType().equals(MdTypeInfo.CLASS) || mdClass.definesType().equals(TypeTupleDAOIF.CLASS) || mdClass.definesType().equals(MdParameterInfo.CLASS) || mdClass.definesType().equals(MdMethodInfo.CLASS) || mdClass.definesType().equals(MdIndexInfo.CLASS) || mdClass.definesType().equals(MdDomainInfo.CLASS) || mdClass.definesType().equals(MdAttributeVirtualInfo.CLASS) || mdClass.definesType().equals(MdAttributeConcreteInfo.CLASS) || mdClass.definesType().equals(MdTypeInfo.CLASS))
    {
      getWriter().writeLine("    return this.getClassDisplayLabel();");
    }
    else if (mdClass.definesType().equals(UserInfo.CLASS))
    {
      getWriter().writeLine("    return this.getValue(\"" + UserInfo.USERNAME + "\");");
    }
    else
    {
      getWriter().writeLine("    return super.toString();");
    }

    getWriter().writeLine("  }");
    getWriter().writeLine("}");
  }

  @Override
  public String getClassAttribute()
  {
    return MdClassInfo.BASE_CLASS;
  }

  @Override
  public String getSourceAttribute()
  {
    return MdClassInfo.BASE_SOURCE;
  }
}
