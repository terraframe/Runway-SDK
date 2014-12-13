/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.business.generation.json;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.dto.ClassDTOBaseGenerator;
import com.runwaysdk.constants.ExceptionInfo;
import com.runwaysdk.constants.JSON;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.generation.CommonGenerationUtil;

public abstract class ClassJSGenerator extends TypeJSGenerator
{

  /**
   * Constructor to set the session id and MdClassIF for javascript generation.
   * 
   * @param sessionId
   * @param mdClassIF
   */
  public ClassJSGenerator(String sessionId, MdClassDAOIF mdClassIF)
  {
    super(sessionId, mdClassIF);
  }

  @Override
  protected MdClassDAOIF getMdTypeIF()
  {
    return (MdClassDAOIF) super.getMdTypeIF();
  }

  @Override
  protected String getClassName()
  {
    return getMdTypeIF().definesType();
  }

  /**
   * Writes the accessor that denotes if the attribute can be read.
   */
  private Declaration writeIsReadable(MdAttributeDAOIF mdAttribute)
  {
    Declaration readable = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = "is" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "Readable";

    readable.writeln(methodName + " : function()");
    readable.openBracketLn();
    readable.writeln("return this.getAttributeDTO('" + attributeName + "').isReadable();");
    readable.closeBracket();

    return readable;
  }

  /**
   * Writes the accessor that denotes if the attribute can be written.
   */
  private Declaration writeIsWritable(MdAttributeDAOIF mdAttribute)
  {
    Declaration writable = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = "is" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "Writable";

    writable.writeln(methodName + " : function()");
    writable.openBracketLn();
    writable.writeln("return this.getAttributeDTO('" + attributeName + "').isWritable();");
    writable.closeBracket();

    return writable;
  }

  /**
   * Writes the accessor that denotes if the attribute has been modified.
   */
  private Declaration writeIsModified(MdAttributeDAOIF mdAttribute)
  {
    Declaration modified = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();

    String methodName = "is" + CommonGenerationUtil.upperFirstCharacter(attributeName) + "Modified";

    modified.writeln(methodName + " : function()");
    modified.openBracketLn();
    modified.writeln("return this.getAttributeDTO('" + attributeName + "').isModified();");
    modified.closeBracket();

    return modified;
  }

  /**
   * Write a getter for an enumeration
   * 
   * @param mdAttributeEnumeration
   */
  private Declaration writeEnumerationGetter(MdAttributeDAOIF mdAttributeEnumeration)
  {
    Declaration getter = this.newDeclaration();

    String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeEnumeration.definesAttribute());
    String mdEnumType = namespaceType( ( (MdAttributeEnumerationDAOIF) mdAttributeEnumeration.getMdAttributeConcrete() ).getMdEnumerationDAO().definesType());

    getter.writeln(methodName + " : function()");
    getter.openBracketLn();
    getter.writeln("var attributeDTO = this.getAttributeDTO('" + mdAttributeEnumeration.definesAttribute() + "');");
    getter.writeln("var names = attributeDTO.getEnumNames();");
    getter.writeln("var enums = [];");
    getter.writeln("for(var i=0; i<names.length; i++)");
    getter.openBracketLn();
    getter.writeln("enums.push(" + mdEnumType + "[names[i]]);");
    getter.closeBracketLn();
    getter.writeln("return enums;");
    getter.closeBracket();

    return getter;
  }

  /**
   * Write a getter for an enumeration
   * 
   * @param mdAttributeMultiReference
   */
  private Declaration writeMultiReferenceGetter(MdAttributeDAOIF mdAttributeMultiReference)
  {
    Declaration getter = this.newDeclaration();

    String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(mdAttributeMultiReference.definesAttribute());

    getter.writeln(methodName + " : function()");
    getter.openBracketLn();
    getter.writeln("var attributeDTO = this.getAttributeDTO('" + mdAttributeMultiReference.definesAttribute() + "');");
    getter.writeln("return attributeDTO.getItemIds();");
    getter.closeBracket();

    return getter;
  }

  /**
   * Writes the setter for an attribute
   * 
   * @param mdAttribute
   */
  private Declaration writeSetter(MdAttributeDAOIF mdAttribute)
  {
    Declaration setter = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = CommonGenerationUtil.SET + CommonGenerationUtil.upperFirstCharacter(attributeName);

    setter.writeln(methodName + " : function(value)");
    setter.openBracketLn();
    setter.writeln("var attributeDTO = this.getAttributeDTO('" + attributeName + "');");
    setter.writeln("attributeDTO.setValue(value);");
    setter.writeln("this.setModified(true);");
    setter.closeBracket();

    return setter;
  }

  private Declaration writeReferenceSetter(MdAttributeDAOIF mdAttribute)
  {
    Declaration setter = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = CommonGenerationUtil.SET + CommonGenerationUtil.upperFirstCharacter(attributeName);

    setter.writeln(methodName + " : function(ref)");
    setter.openBracketLn();
    setter.writeln("var attributeDTO = this.getAttributeDTO('" + attributeName + "');");
    setter.writeln("attributeDTO.setValue(" + JSON.RUNWAY_IS_OBJECT.getLabel() + "(ref) ? ref.getId() : ref);");
    setter.writeln("this.setModified(true);");
    setter.closeBracket();

    return setter;
  }

  /**
   * Writes the setter(s) for a enumeration
   * 
   * @param mdAttribute
   */
  private List<Declaration> writeEnumerationSetter(MdAttributeDAOIF mdAttributeEnumeration)
  {
    List<Declaration> setters = new LinkedList<Declaration>();

    String enumName = mdAttributeEnumeration.definesAttribute();

    // removeEnumItem
    Declaration remove = this.newDeclaration();
    String removeMethodName = "remove" + CommonGenerationUtil.upperFirstCharacter(enumName);

    remove.writeln(removeMethodName + " : function(enumValue)");
    remove.openBracketLn();
    remove.writeln("var attributeDTO = this.getAttributeDTO('" + enumName + "');");
    remove.writeln("attributeDTO.remove(enumValue);");
    remove.writeln("this.setModified(true);");
    remove.closeBracket();

    // clearEnum
    Declaration clear = this.newDeclaration();
    String clearMethodName = "clear" + CommonGenerationUtil.upperFirstCharacter(enumName);

    clear.writeln(clearMethodName + " : function()");
    clear.openBracketLn();
    clear.writeln("var attributeDTO = this.getAttributeDTO('" + enumName + "');");
    clear.writeln("attributeDTO.clear();");
    clear.writeln("this.setModified(true);");
    clear.closeBracket();

    // addEnumItem
    Declaration add = this.newDeclaration();
    String methodName = "add" + CommonGenerationUtil.upperFirstCharacter(enumName);

    add.writeln(methodName + " : function(enumValue)");
    add.openBracketLn();
    add.writeln("var attributeDTO = this.getAttributeDTO('" + enumName + "');");
    add.writeln("attributeDTO.add(enumValue);");
    add.writeln("this.setModified(true);");
    add.closeBracket();

    setters.add(remove);
    setters.add(clear);
    setters.add(add);

    return setters;
  }

  /**
   * Writes the setter(s) for a enumeration
   * 
   * @param mdAttribute
   */
  private List<Declaration> writeMultiReferenceSetter(MdAttributeDAOIF mdAttributeMultiReference)
  {
    List<Declaration> setters = new LinkedList<Declaration>();

    String attributeName = mdAttributeMultiReference.definesAttribute();

    // remove Mutli Item
    Declaration remove = this.newDeclaration();
    String removeMethodName = "remove" + CommonGenerationUtil.upperFirstCharacter(attributeName);

    remove.writeln(removeMethodName + " : function(item)");
    remove.openBracketLn();
    remove.writeln("var attributeDTO = this.getAttributeDTO('" + attributeName + "');");
    remove.writeln("attributeDTO.remove(item);");
    remove.writeln("this.setModified(true);");
    remove.closeBracket();

    // clear multi item
    Declaration clear = this.newDeclaration();
    String clearMethodName = "clear" + CommonGenerationUtil.upperFirstCharacter(attributeName);

    clear.writeln(clearMethodName + " : function()");
    clear.openBracketLn();
    clear.writeln("var attributeDTO = this.getAttributeDTO('" + attributeName + "');");
    clear.writeln("attributeDTO.clear();");
    clear.writeln("this.setModified(true);");
    clear.closeBracket();

    // add multi item
    Declaration add = this.newDeclaration();
    String methodName = "add" + CommonGenerationUtil.upperFirstCharacter(attributeName);

    add.writeln(methodName + " : function(item)");
    add.openBracketLn();
    add.writeln("var attributeDTO = this.getAttributeDTO('" + attributeName + "');");
    add.writeln("attributeDTO.add(item);");
    add.writeln("this.setModified(true);");
    add.closeBracket();

    setters.add(remove);
    setters.add(clear);
    setters.add(add);

    return setters;
  }

  /**
   * Writes the constants for all attributes and the class name.
   */
  @Override
  protected final List<Declaration> getConstants()
  {
    List<Declaration> constants = new LinkedList<Declaration>();

    MdClassDAOIF mdClassIF = getMdTypeIF();

    // Write all contants that map to attribute names
    for (MdAttributeDAOIF mdAttributeIF : mdClassIF.definesAttributesOrdered())
    {
      Declaration constant = this.newDeclaration();

      String attributeName = mdAttributeIF.definesAttribute();
      String attributeNameU = attributeName.toUpperCase();
      constant.write(attributeNameU + " : '" + attributeName + "'");

      constants.add(constant);
    }

    return constants;
  }

  @Override
  protected List<Declaration> getInstanceMethods()
  {
    List<Declaration> instances = new LinkedList<Declaration>();

    instances.addAll(writeAttributes());
    instances.addAll(writeInstanceMdMethods());

    return instances;
  }

  @Override
  protected List<Declaration> getStaticMethods()
  {
    List<Declaration> statics = new LinkedList<Declaration>();

    statics.addAll(writeStaticMdMethods());

    return statics;
  }

  /**
   * Write the getter for a struct
   * 
   * @param mdAttributeStruct
   */
  private Declaration writeStructGetter(MdAttributeDAOIF mdAttribute)
  {
    Declaration getter = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);
    String structType = ( (MdAttributeStructDAOIF) mdAttribute.getMdAttributeConcrete() ).getMdStructDAOIF().definesType();

    // We use lazy type-safe instantiation for structs.
    getter.writeln(methodName + " : function()");
    getter.openBracketLn();
    getter.writeln("if(" + JSON.RUNWAY_META_CLASS_EXISTS.getLabel() + "('" + structType + "'))");
    getter.openBracketLn();

    // struct type exists, so convert the raw struct object into the type-safe
    // representation
    getter.writeln("var structDTO = this.getAttributeDTO('" + attributeName + "').getStructDTO();");
    getter.writeln("if(structDTO == null)");
    getter.openBracketLn();
    getter.writeln("return null;");
    getter.closeBracketLn();
    getter.writeln("else if(structDTO instanceof " + namespaceType(structType) + ")");
    getter.openBracketLn();
    getter.writeln("return structDTO;");
    getter.closeBracketLn();
    getter.writeln("else");
    getter.openBracketLn();
    // instantiate the struct and reset the internal value to the type-safe
    // object
    getter.writeln("structDTO = new " + namespaceType(structType) + "(structDTO);");
    getter.writeln("this.getAttributeDTO('" + attributeName + "').setStructDTO(structDTO);");
    getter.writeln("return structDTO;");
    getter.closeBracketLn();

    getter.closeBracketLn();
    getter.writeln("else");
    getter.openBracketLn();
    getter.writeln("throw new " + JSON.RUNWAY_PACKAGE_NS.getLabel() + "." + ExceptionInfo.CLASS + "('Must import type " + structType + "');");
    getter.closeBracketLn();
    getter.closeBracket();

    return getter;
  }

  private boolean doGetter(MdAttributeDAOIF mdAttribute)
  {
    return mdAttribute.getGetterVisibility().equals(VisibilityModifier.PUBLIC);
  }

  private boolean doSetter(MdAttributeDAOIF mdAttribute)
  {
    return mdAttribute.getSetterVisibility().equals(VisibilityModifier.PUBLIC) && !mdAttribute.isSystem();
  }

  /**
   * Writes the getter for an attribute
   * 
   * @param mdAttribute
   */
  private Declaration writeGetter(MdAttributeDAOIF mdAttribute)
  {
    Declaration getter = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);

    getter.writeln(methodName + " : function()");
    getter.openBracketLn();
    getter.writeln("return this.getAttributeDTO('" + attributeName + "').getValue();");
    getter.closeBracket();

    return getter;
  }

  /**
   * Writes the class field for attribute metadata.
   * 
   * @param m
   */
  private Declaration writeAttributeMetaDataAccessor(MdAttributeDAOIF m)
  {
    Declaration metadata = this.newDeclaration();

    String attrMdName = ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + CommonGenerationUtil.upperFirstCharacter(m.definesAttribute()) + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX;

    metadata.writeln(attrMdName + " : function()");
    metadata.openBracketLn();
    metadata.writeln("return this.getAttributeDTO('" + m.definesAttribute() + "').getAttributeMdDTO();");
    metadata.closeBracket();

    return metadata;
  }

  /**
   * Writes all static MdMethods and the static method counterparts to instance
   * MdMethods.
   */
  private List<Declaration> writeStaticMdMethods()
  {
    List<Declaration> methods = new LinkedList<Declaration>();

    MdClassDAOIF mdClassIF = getMdTypeIF();

    for (MdMethodDAOIF mdMethod : mdClassIF.getAllMdMethods())
    {
      String methodName = mdMethod.getValue(MdMethodInfo.NAME);

      // parameter metadata
      List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();

      // method changes depending on static/instance method
      if (mdMethod.isStatic())
      {
        methods.add(writeMdMethod(false, list, methodName));
      }
      else
      {
        // write the static version of the instance method
        list.add(0, GenerationUtil.getMdParameterId());
        methods.add(writeMdMethod(false, list, methodName));
      }
    }

    return methods;
  }

  /**
   * Writes the attribute getters and setters (if requested) for a type.
   * 
   * @param readOnly
   *          denotes if only getters should be generated
   */
  private List<Declaration> writeAttributes()
  {
    List<Declaration> attrDecs = new LinkedList<Declaration>();

    MdClassDAOIF mdClassIF = getMdTypeIF();

    // NOTE: Don't change write[Attribute]getter/setter() methods to use
    // polymorphic
    // calls because it doesn't work with virtual MdAttributes.
    for (MdAttributeDAOIF mdAttributeIF : mdClassIF.definesAttributesOrdered())
    {
      MdAttributeConcreteDAOIF mdAttributeConcreteDAOIF = mdAttributeIF.getMdAttributeConcrete();

      // skip system and special attributes
      if (GenerationUtil.isDTOSpecialCase(mdAttributeIF))
        continue;

      if (mdAttributeConcreteDAOIF instanceof MdAttributeBlobDAOIF)
      {
        // DO NOTHING
        // Blobs have no behavior in javascript
        continue;
      }

      if (mdAttributeConcreteDAOIF instanceof MdAttributeEnumerationDAOIF)
      {
        if (doGetter(mdAttributeIF))
        {
          attrDecs.add(writeEnumerationGetter(mdAttributeIF));
        }

        if (doSetter(mdAttributeIF))
        {
          attrDecs.addAll(writeEnumerationSetter(mdAttributeIF));
        }
      }
      else if (mdAttributeConcreteDAOIF instanceof MdAttributeMultiReferenceDAOIF)
      {
        if (doGetter(mdAttributeIF))
        {
          attrDecs.add(writeMultiReferenceGetter(mdAttributeIF));
        }

        if (doSetter(mdAttributeIF))
        {
          attrDecs.addAll(writeMultiReferenceSetter(mdAttributeIF));
        }
      }
      else if (mdAttributeConcreteDAOIF instanceof MdAttributeStructDAOIF)
      {
        if ( ( (MdAttributeStructDAOIF) mdAttributeConcreteDAOIF ).getMdStructDAOIF().isPublished() && doGetter(mdAttributeIF))
        {
          attrDecs.add(writeStructGetter(mdAttributeIF));
        }
      }
      else if (mdAttributeConcreteDAOIF instanceof MdAttributeReferenceDAOIF)
      {
        if ( ( (MdAttributeReferenceDAOIF) mdAttributeConcreteDAOIF ).getReferenceMdBusinessDAO().isPublished())
        {
          if (doGetter(mdAttributeIF))
          {
            attrDecs.add(writeReferenceGetter(mdAttributeIF));
          }

          if (doSetter(mdAttributeIF))
          {
            attrDecs.add(writeReferenceSetter(mdAttributeIF));
          }
        }
      }
      else
      {
        if (doGetter(mdAttributeIF))
        {
          attrDecs.add(writeGetter(mdAttributeIF));
        }

        if (doSetter(mdAttributeIF))
        {
          attrDecs.add(writeSetter(mdAttributeIF));
        }
      }

      // attribute status accessors
      attrDecs.add(writeIsReadable(mdAttributeIF));
      attrDecs.add(writeIsWritable(mdAttributeIF));
      attrDecs.add(writeIsModified(mdAttributeIF));

      // metadata accessor
      attrDecs.add(writeAttributeMetaDataAccessor(mdAttributeIF));
    }

    return attrDecs;
  }

  /**
   * Write the instance MdMethods defined on this type (but not the static
   * counterparts)
   */
  protected List<Declaration> writeInstanceMdMethods()
  {
    List<Declaration> methods = new LinkedList<Declaration>();

    MdClassDAOIF mdClassIF = getMdTypeIF();

    for (MdMethodDAOIF mdMethod : mdClassIF.getAllMdMethods())
    {
      if (!mdMethod.isStatic())
      {
        String methodName = mdMethod.getValue(MdMethodInfo.NAME);

        // parameter metadata
        List<MdParameterDAOIF> list = mdMethod.getMdParameterDAOs();

        // write the instance method
        methods.add(writeMdMethod(true, list, methodName));
      }
    }

    return methods;
  }

  private Declaration writeReferenceGetter(MdAttributeDAOIF mdAttribute)
  {
    Declaration getter = this.newDeclaration();

    String attributeName = mdAttribute.definesAttribute();
    String methodName = CommonGenerationUtil.GET + CommonGenerationUtil.upperFirstCharacter(attributeName);

    getter.writeln(methodName + " : function(clientRequest)");
    getter.openBracketLn();
    getter.writeln("var attributeDTO = this.getAttributeDTO('" + attributeName + "');");
    getter.writeln("var value = attributeDTO.getValue();");
    getter.writeln("if(value == null || value == '')");
    getter.openBracketLn();
    getter.writeln("clientRequest.onSuccess(null);");
    getter.closeBracketLn();
    getter.writeln("else");
    getter.openBracketLn();
    getter.writeln(JSON.RUNWAY_FACADE.getLabel() + ".get(clientRequest, value);");
    getter.closeBracketLn();
    getter.closeBracket();

    return getter;
  }

  /**
   * Writes the facade method.
   * 
   * @param method
   * @param methodName
   * @param methodTypes
   * @param entityName
   * @param invokeCall
   */
  protected Declaration writeMdMethod(boolean isInstance, List<MdParameterDAOIF> parameters, String methodName)
  {
    Declaration methodDec = this.newDeclaration();

    String entityName = getMdTypeIF().definesType();
    String parameterStr = GenerationUtil.buildJSONParameters(parameters);
    String methodTypes = GenerationUtil.buildMethodArray(parameters);
    String thisRef = isInstance ? "this" : "null";

    String method = methodName + " : function(" + parameterStr + ")";

    methodDec.writeln(method);
    methodDec.openBracketLn();
    methodDec.writeln("var metadata = {" + JSON.METHOD_METADATA_CLASSNAME.getLabel() + ":'" + entityName + "', " + JSON.METHOD_METADATA_METHODNAME.getLabel() + ":'" + methodName + "', " + JSON.METHOD_METADATA_DECLARED_TYPES.getLabel() + ": [" + methodTypes + "]};");
    methodDec.writeln(JSON.RUNWAY_FACADE.getLabel() + ".invokeMethod(clientRequest, metadata, " + thisRef + ", [].splice.call(arguments, 1));");

    methodDec.closeBracket();

    return methodDec;
  }
}
